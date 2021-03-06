package edu.unlv.cs.rebelhotel.service;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import edu.unlv.cs.rebelhotel.domain.Term;
import edu.unlv.cs.rebelhotel.domain.enums.Semester;

@Service
public class TermService {
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	public List<Term> generateCurrentTerms() {
		List<Term> result = new LinkedList<Term>();
		
		Session session = (Session) Term.entityManager().unwrap(Session.class);
		session.beginTransaction();
		
		Calendar now = Calendar.getInstance();
		Integer year = now.get(Calendar.YEAR);
		
		for (Semester semester : Semester.values()) {
			List queryResults = session.createCriteria(Term.class)
			.add(Restrictions.eq("termYear", year))
			.add(Restrictions.eq("semester", semester))
			.list();
			
			if (queryResults.size() == 0) {
				Term term = new Term(year, semester);
				term.persist();
				result.add(term);
			}
		}
		
		session.close();
		
		return result;
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	public List<Term> generateRangeOfTerms(Integer low, Integer high) {
		List<Term> result = new LinkedList<Term>();
		
		for (int i = low.intValue(); i <= high.intValue(); i++) {
			for (Semester semester : Semester.values()) {
				try {
					Term.findTermsBySemesterAndTermYearEquals(semester, i).getSingleResult();
				}
				catch (org.springframework.dao.EmptyResultDataAccessException exception) { // create a new term only if there is not one existing already
					Term term = new Term();
					term.setSemester(semester);
					term.setTermYear(i);
					term.persist();
					result.add(term);
				}
			}
		}
		
		return result;
	}
}