package edu.unlv.cs.rebelhotel.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVWriter;

import edu.unlv.cs.rebelhotel.domain.Major;
import edu.unlv.cs.rebelhotel.domain.Student;
import edu.unlv.cs.rebelhotel.form.FormStudentQuery;

@Service
public class StudentQueryService {			
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'")
	public List<Object> queryStudents(FormStudentQuery formStudentQuery, String sorting) throws Exception {
		return queryStudents(formStudentQuery, sorting, null, null);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'")
	public List<Object> queryStudents(FormStudentQuery formStudentQuery, String sorting, Integer start, Integer size) throws Exception {
		DetachedCriteria search = DetachedCriteria.forClass(Student.class, "oq");
		
		if (formStudentQuery.getUseUserId()) {
			search.add(Restrictions.eq("userId", formStudentQuery.getUserId()));
		}
		if (formStudentQuery.getUseModified()) {
			search.add(Restrictions.between("lastModified", formStudentQuery.getLastModifiedStart(), formStudentQuery.getLastModifiedEnd()));
		}
		if (formStudentQuery.getUseCatalogTerm()) {
			search.createCriteria("majors")
			.createCriteria("catalogTerm")
			.add(Example.create(formStudentQuery.getCatalogTerm()));
		}
		if (formStudentQuery.getUseGradTerm()) {
			search.createCriteria("gradTerm")
			.add(Example.create(formStudentQuery.getGradTerm()));
		}
		if (formStudentQuery.getUseMajor() || formStudentQuery.getUseMilestone()) {
			DetachedCriteria majorSearch = search.createCriteria("majors");
			if (formStudentQuery.getUseMajor()) {
				majorSearch.add(Restrictions.eq("major.degreeCode", formStudentQuery.getDegreeCode()));
			}
			if (formStudentQuery.getUseMilestone()) {
				majorSearch.add(Restrictions.eq("major.reachedMilestone", formStudentQuery.getHasMilestone()));
			}
		}
		if (formStudentQuery.getUseFirstName()) {
			String firstName = formStudentQuery.getFirstName();
			if (firstName.length() > 0) {
				firstName = "%" + firstName + "%";
			}
			else {
				firstName = "%";
			}
			search.add(Restrictions.like("firstName", firstName));
		}
		if (formStudentQuery.getUseMiddleName()) {
			String middleName = formStudentQuery.getMiddleName();
			if (middleName.length() > 0) {
				middleName = "%" + middleName + "%";
			}
			else {
				middleName = "%";
			}
			search.add(Restrictions.like("middleName", middleName));
		}
		if (formStudentQuery.getUseLastName()) {
			String lastName = formStudentQuery.getLastName();
			if (lastName.length() > 0) {
				lastName = "%" + lastName + "%";
			}
			else {
				lastName = "%";
			}
			search.add(Restrictions.like("lastName", lastName));
		}
		
		// this is so grossly inefficient that it should be replaced with an HQL query or a "totalHours" property should be stored on students
		// left in for now because it has the correct functionality
		if (formStudentQuery.getUseHours()) {
			DetachedCriteria innerQuery = DetachedCriteria.forClass(Student.class, "iq")
			.createAlias("workEffort", "we")
			.setProjection(Projections.projectionList()
					.add(Projections.sum("we.duration.hours")))
					.add(Restrictions.eqProperty("iq.id", "oq.id"));
			if (formStudentQuery.getValidationSelected()) {
				innerQuery.add(Restrictions.eq("we.validation", formStudentQuery.getValidation()));
			}
			if (formStudentQuery.getVerificationSelected()) {
				innerQuery.add(Restrictions.eq("we.verification", formStudentQuery.getVerification()));
			}
			
			search.createAlias("workEffort", "owe")
			.setProjection(Projections.projectionList()
					.add(Projections.sum("owe.duration.hours").as("totalHours"))
					.add(Projections.groupProperty("id"))
					.add(Projections.property("id")))
			.setResultTransformer(Transformers.aliasToBean(Student.class)); // this will have no visual effect due to "search" being a subquery
			if (formStudentQuery.getValidationSelected()) {
				search.add(Restrictions.eq("owe.validation", formStudentQuery.getValidation()));
			}
			if (formStudentQuery.getVerificationSelected()) {
				search.add(Restrictions.eq("owe.verification", formStudentQuery.getVerification()));
			}
			if (formStudentQuery.getHoursLow() != null) {
				search.add(Subqueries.le(new Long(formStudentQuery.getHoursLow()), innerQuery));
			}
			if (formStudentQuery.getHoursHigh() != null) {
				search.add(Subqueries.ge(new Long(formStudentQuery.getHoursHigh()), innerQuery));
			}
			if (formStudentQuery.getEmployerName() != "") {
				search.add(Restrictions.like("owe.employer.name", "%" + formStudentQuery.getEmployerName() + "%"));
			}
			if (formStudentQuery.getEmployerLocation() != "") {
				search.add(Restrictions.like("owe.employer.location", "%" + formStudentQuery.getEmployerLocation() + "%"));
			}
			if (formStudentQuery.getWorkEffortStartDate() != null) {
				search.add(Restrictions.ge("owe.duration.startDate", formStudentQuery.getWorkEffortStartDate()));
			}
			if (formStudentQuery.getWorkEffortEndDate() != null) {
				search.add(Restrictions.le("owe.duration.endDate", formStudentQuery.getWorkEffortEndDate()));
			}
		}
		else {
			if (formStudentQuery.getVerificationSelected() || formStudentQuery.getValidationSelected() || formStudentQuery.getEmployerName() != ""
			|| formStudentQuery.getEmployerLocation() != "" || formStudentQuery.getWorkEffortStartDate() != null || formStudentQuery.getWorkEffortEndDate() != null) {
				DetachedCriteria jobCriteria = search.createCriteria("workEffort");
				if (formStudentQuery.getVerificationSelected()) {
					jobCriteria.add(Restrictions.eq("verification", formStudentQuery.getVerification()));
				}
				if (formStudentQuery.getValidationSelected()) {
					jobCriteria.add(Restrictions.eq("validation", formStudentQuery.getValidation()));
				}
				if (formStudentQuery.getEmployerName() != "") {
					jobCriteria.add(Restrictions.like("owe.employer.name", "%" + formStudentQuery.getEmployerName() + "%"));
				}
				if (formStudentQuery.getEmployerLocation() != "") {
					jobCriteria.add(Restrictions.like("owe.employer.location", "%" + formStudentQuery.getEmployerLocation() + "%"));
				}
				if (formStudentQuery.getWorkEffortStartDate() != null) {
					jobCriteria.add(Restrictions.ge("owe.duration.startDate", formStudentQuery.getWorkEffortStartDate()));
				}
				if (formStudentQuery.getWorkEffortEndDate() != null) {
					jobCriteria.add(Restrictions.le("owe.duration.endDate", formStudentQuery.getWorkEffortEndDate()));
				}
			}
		}
		
		List students;
		
		DetachedCriteria rootQuery = DetachedCriteria.forClass(Student.class);
		search.setProjection(Projections.distinct(Projections.projectionList().add(Projections.alias(Projections.property("id"), "id"))));
		rootQuery.add(Subqueries.propertyIn("id", search));
		DetachedCriteria countQuery = DetachedCriteria.forClass(Student.class);
		countQuery.add(Subqueries.propertyIn("id", search));
		countQuery.setProjection(Projections.rowCount());
		
		if (sorting != null) {
			if (sorting != "") {
				int sort_value = Integer.parseInt(sorting.trim());
				String property = getPropertyFromIndex(formStudentQuery, sort_value);
				if (sort_value % 2 == 0) {
					rootQuery.addOrder(Order.asc(property));
					
				}
				else {
					rootQuery.addOrder(Order.desc(property));
				}
			}
		}
		
		Session session = ((Session) Student.entityManager().unwrap(Session.class)).getSessionFactory().openSession();
		Criteria query = rootQuery.getExecutableCriteria(session);
		if (start != null && size != null) {
			query.setFirstResult(start);
			query.setMaxResults(size);
		}
		Transaction transaction = null;
		Long count;
		
		try {
			transaction = session.beginTransaction();
			students = query.list();
			transaction.commit();
			transaction = session.beginTransaction();
			count = (Long) countQuery.getExecutableCriteria(session).list().get(0);
			transaction.commit();
			
			for (Student student : (List<Student>) students) {
				Hibernate.initialize(student);
				Hibernate.initialize(student.getMajors());
			}
		}
		catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}
		finally {
			session.close();
		}
				
		List<Object> resultList = new LinkedList<Object>();
		resultList.add(count);
		resultList.add(students);
				
		return resultList;
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'")
	public String buildLabelsString(FormStudentQuery formStudentQuery, MessageSource messageSource) {
		String properties = messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_id", null, LocaleContextHolder.getLocale());
		if (formStudentQuery.getShowUserId()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_userid", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowEmail()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_email", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowFirstName()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_firstname", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowMiddleName()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_middlename", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowLastName()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_lastname", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowAdmitTerm()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_admitterm", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowGradTerm()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_gradterm", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowCodeOfConductSigned()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_codeofconductsigned", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowLastModified()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_lastmodified", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowUserAccount()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_useraccount", null, LocaleContextHolder.getLocale());
		}
		return properties;
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'")
	public String getPropertyFromIndex(FormStudentQuery formStudentQuery, Integer index) {
		index = index / 2;
		Integer iterator = new Integer(0);
		if (index == 0) {
			return "id";
		}
		iterator = iterator + 1;
		if (formStudentQuery.getShowUserId()) {
			if (iterator == index) {
				return "userId";
			}
			iterator = iterator + 1;
		}
		if (formStudentQuery.getShowEmail()) {
			if (iterator == index) {
				return "email";
			}
			iterator = iterator + 1;
		}
		if (formStudentQuery.getShowFirstName()) {
			if (iterator == index) {
				return "firstName";
			}
			iterator = iterator + 1;
		}
		if (formStudentQuery.getShowMiddleName()) {
			if (iterator == index) {
				return "middleName";
			}
			iterator = iterator + 1;
		}
		if (formStudentQuery.getShowLastName()) {
			if (iterator == index) {
				return "lastName";
			}
			iterator = iterator + 1;
		}
		if (formStudentQuery.getShowAdmitTerm()) {
			if (iterator == index) {
				return "admitTerm";
			}
			iterator = iterator + 1;
		}
		if (formStudentQuery.getShowGradTerm()) {
			if (iterator == index) {
				return "gradTerm";
			}
			iterator = iterator + 1;
		}
		if (formStudentQuery.getShowCodeOfConductSigned()) {
			if (iterator == index) {
				return "codeOfConductSigned";
			}
			iterator = iterator + 1;
		}
		if (formStudentQuery.getShowLastModified()) {
			if (iterator == index) {
				return "lastModified";
			}
			iterator = iterator + 1;
		}
		if (formStudentQuery.getShowUserAccount()) {
			if (iterator == index) {
				return "userAccount";
			}
			iterator = iterator + 1;
		}
		
		return "invalid index"; // likely will want to throw an exception instead
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'")
	public String generateCsv(FormStudentQuery formStudentQuery, List<Student> students, MessageSource messageSource) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(byteStream), ',');
		// commas cannot be in the locale messages in the comma-separated label string, just so you know
		String[] columns = buildLabelsString(formStudentQuery, messageSource).split(",");
		if (formStudentQuery.getShowMajor()) {
			String[] columnsMajor = new String[columns.length + 15];
			int i;
			for (i = 0; i < columns.length; i++) {
				columnsMajor[i] = columns[i];
			}
			columnsMajor[i++] = "Major 1";
			columnsMajor[i++] = "Catalog Term";
			columnsMajor[i++] = "Total Hours";
			columnsMajor[i++] = "Related Hours";
			columnsMajor[i++] = "Milestone";
			columnsMajor[i++] = "Major 2";
			columnsMajor[i++] = "Catalog Term";
			columnsMajor[i++] = "Total Hours";
			columnsMajor[i++] = "Related Hours";
			columnsMajor[i++] = "Milestone";
			columnsMajor[i++] = "Major 3";
			columnsMajor[i++] = "Catalog Term";
			columnsMajor[i++] = "Total Hours";
			columnsMajor[i++] = "Related Hours";
			columnsMajor[i++] = "Milestone";
			writer.writeNext(columnsMajor);
		}
		else {
			writer.writeNext(columns);
		}
		for (Student student: students) {
			ArrayList<String> entries = new ArrayList<String>();
			entries.add(student.getId().toString());
			if (formStudentQuery.getShowUserId()) {
				if (student.getUserId() != null) {
					entries.add(student.getUserId().toString());
				}
				else {
					entries.add("");
				}
			}
			if (formStudentQuery.getShowEmail()) {
				if (student.getUserAccount().getEmail() != null) {
					entries.add(student.getUserAccount().getEmail().toString());
				}
				else {
					entries.add("");
				}
			}
			if (formStudentQuery.getShowFirstName()) {
				entries.add(student.getFirstName());
			}
			if (formStudentQuery.getShowMiddleName()) {
				entries.add(student.getMiddleName());
			}
			if (formStudentQuery.getShowLastName()) {
				entries.add(student.getLastName());
			}
			if (formStudentQuery.getShowAdmitTerm()) {
				if (student.getAdmitTerm() != null) {
					entries.add(student.getAdmitTerm().toString());
				}
				else {
					entries.add("");
				}
			}
			if (formStudentQuery.getShowGradTerm()) {
				if (student.getGradTerm() != null) {
					entries.add(student.getGradTerm().toString());
				}
				else {
					entries.add("");
				}
			}
			if (formStudentQuery.getShowCodeOfConductSigned()) {
				if (student.getCodeOfConductSigned() != null) {
					entries.add(student.getCodeOfConductSigned().toString());
				}
				else {
					entries.add("");
				}
			}
			if (formStudentQuery.getShowLastModified()) {
				if (student.getLastModified() != null) {
					entries.add(student.getLastModified().toString());
				}
				else {
					entries.add("");
				}
			}
			if (formStudentQuery.getShowUserAccount()) {
				if (student.getUserAccount() != null) {
					entries.add(student.getUserAccount().toString());
				}
				else {
					entries.add("");
				}
			}
			if (formStudentQuery.getShowMajor()) {
				List<Major> majors = new LinkedList<Major>(student.getMajors());
				Major majorArray[] = new Major[majors.size()];
				
				int position = 0;
				for (int i = 0; i < majors.size(); i++) {
					int lowest = i;
					for (int j = 0; j < majors.size(); j++) {
						if (majors.get(j).getId().longValue() < majors.get(i).getId().longValue()) {
							lowest = j;
						}
					}
					majorArray[position++] = majors.get(lowest);
					majors.remove(lowest);
					i--;
				}
				
				for (int i = 0; i < majorArray.length; i++) {
					entries.add(majorArray[i].getDegreeCode() != null ? majorArray[i].getDegreeCode() : "");
					entries.add(majorArray[i].getCatalogTerm() != null ? majorArray[i].getCatalogTerm().toString() : "");
					entries.add(majorArray[i].getTotalHours() != null ? majorArray[i].getTotalHours().toString() : "");
					entries.add(majorArray[i].getMajorHours() != null ? majorArray[i].getMajorHours().toString() : "");
					entries.add(majorArray[i].isReachedMilestone() ? "yes" : "no");
				}
				
				// NOTE assumed major limit of 3 hard coded
				for (int i = 3-majorArray.length; i > 0; i--) {
					entries.add("");
					entries.add("");
					entries.add("");
					entries.add("");
					entries.add("");
				}
			}
			String[] stringEntries = entries.toArray(new String[0]);
			writer.writeNext(stringEntries);
		}
		
		writer.flush();
		String contents = new String(byteStream.toByteArray());
		writer.close();
		return contents;
	}
}
