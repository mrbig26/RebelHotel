package edu.unlv.cs.rebelhotel.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import edu.unlv.cs.rebelhotel.domain.Student;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import edu.unlv.cs.rebelhotel.domain.enums.Verification;
import edu.unlv.cs.rebelhotel.domain.Employer;
import edu.unlv.cs.rebelhotel.domain.Supervisor;
import javax.persistence.Embedded;
import edu.unlv.cs.rebelhotel.domain.WorkEffortDuration;
import edu.unlv.cs.rebelhotel.domain.enums.VerificationType;
import javax.persistence.Enumerated;
import edu.unlv.cs.rebelhotel.domain.enums.Validation;
import edu.unlv.cs.rebelhotel.domain.enums.PayStatus;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import edu.unlv.cs.rebelhotel.domain.CatalogRequirement;
import java.util.HashSet;
import javax.persistence.ManyToMany;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findWorkEffortsByStudentEquals", "findWorkEffortsByValidationAndTermSubmitted" })
public class WorkEffort {
    @NotNull
    @ManyToOne
    private Student student;

    private static final Logger LOG = LoggerFactory.getLogger("audit");

    private String workPosition;

    private String comment;

    @Embedded
    private Supervisor supervisor;

    @Embedded
    private Employer employer;

    @Enumerated
    private VerificationType verificationType;

    @Enumerated
    private Validation validation;

    @Enumerated
    private Verification verification;

    @Enumerated
    private PayStatus payStatus;

    @Embedded
    private WorkEffortDuration duration;

    @ManyToOne
    private Term termSubmitted;
    
    @ManyToMany
    private Set<CatalogRequirement> catalogRequirements = new HashSet<CatalogRequirement>();

    public boolean isAccepted() {
        return verification == Verification.ACCEPTED;
    }

    public boolean isValidated() {
        return validation.equals(Validation.VALIDATED);
    }

    /**
	 * After the student submits a work effort towards his/her major, that work
	 * effort has to be verified and maybe also be validated if applicable
	 * before those hours from that work effort can be counted towards the
	 * student's completed hours. This method returns true if that work effort
	 * has been verified and validated
	 * @param major 
	 * 
	 * @return
	 */
    public boolean isApplicable(Major major) {
        boolean applicableRequirement = hasApplicableCatalogRequirement(major);
        boolean validationNotNeeded = doesntNeedValidation();
        boolean accepted = isAccepted();
        boolean validated = isValidated();
        return applicableRequirement && accepted && (validated || validationNotNeeded);
    }

    /**
	 * Returns true if this job is NOT selected for random validation
	 * @return
	 */
    private boolean doesntNeedValidation() {
        return this.validation.equals(Validation.NO_VALIDATION);
    }

    /**
	 * This method returns true if this job satisfies the catalog requirement
	 * of a given major. Returns false otherwise
	 * @param major
	 * @return
	 */
    private boolean hasApplicableCatalogRequirement(Major major) {
        boolean isApplicable = false;
        for (CatalogRequirement requirement : this.getCatalogRequirements()) {
            isApplicable |= major.appliesTo(requirement);
        }
        return isApplicable;
    }

    @PrePersist
    public void persistHours() {
        audit();
        Set<Major> majors = student.getMajors();
        Long totalHours[] = new Long[majors.size()];
        Long majorHours[] = new Long[majors.size()];
        Major majorArray[] = new Major[majors.size()];
        int it = 0;
        for (Major major : majors) {
            majorArray[it] = major;
            majorHours[it] = (long) 0;
            totalHours[it++] = (long) 0;
        }
        Set<WorkEffort> jobs = student.getWorkEffort();
        for (WorkEffort job : jobs) {
            if (job.getValidation().equals(Validation.FAILED_VALIDATION)) {
                continue;
            }
            if (job.getVerification().equals(Verification.DENIED)) {
                continue;
            }
            for (int i = 0; i < majorArray.length; i++) {
                boolean found = false;
                Set<CatalogRequirement> catalogRequirements = job.getCatalogRequirements();
                for (CatalogRequirement catalogRequirement : catalogRequirements) {
                    if (catalogRequirement.matchesMajor(majorArray[i])) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    majorHours[i] += job.getDuration().getHours();
                }
                totalHours[i] += job.getDuration().getHours();
            }
        }
        for (int i = 0; i < majorArray.length; i++) {
            boolean found = false;
            if (validation.equals(Validation.FAILED_VALIDATION)) {
                break;
            }
            if (verification.equals(Verification.DENIED)) {
                break;
            }
            if (catalogRequirements != null) {
                for (CatalogRequirement catalogRequirement : catalogRequirements) {
                    if (catalogRequirement.matchesMajor(majorArray[i])) {
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                majorHours[i] += duration.getHours();
            }
            totalHours[i] += duration.getHours();
        }
        for (int i = 0; i < majorArray.length; i++) {
            majorArray[i].setTotalHours(new Long(totalHours[i]));
            majorArray[i].setMajorHours(new Long(majorHours[i]));
        }
    }

    @PreUpdate
    public void updateHours() {
        audit();
        Set<Major> majors = student.getMajors();
        Long totalHours[] = new Long[majors.size()];
        Long majorHours[] = new Long[majors.size()];
        Major majorArray[] = new Major[majors.size()];
        int it = 0;
        for (Major major : majors) {
            majorArray[it] = major;
            majorHours[it] = (long) 0;
            totalHours[it++] = (long) 0;
        }
        Set<WorkEffort> jobs = student.getWorkEffort();
        for (WorkEffort job : jobs) {
            if (job.getValidation().equals(Validation.FAILED_VALIDATION)) {
                continue;
            }
            if (job.getVerification().equals(Verification.DENIED)) {
                continue;
            }
            if (job.getId().equals(getId())) {
                continue;
            }
            for (int i = 0; i < majorArray.length; i++) {
                boolean found = false;
                Set<CatalogRequirement> catalogRequirements = job.getCatalogRequirements();
                for (CatalogRequirement catalogRequirement : catalogRequirements) {
                    if (catalogRequirement.matchesMajor(majorArray[i])) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    majorHours[i] += job.getDuration().getHours();
                }
                totalHours[i] += job.getDuration().getHours();
            }
        }
        for (int i = 0; i < majorArray.length; i++) {
            boolean found = false;
            if (validation.equals(Validation.FAILED_VALIDATION)) {
                break;
            }
            if (verification.equals(Verification.DENIED)) {
                break;
            }
            if (catalogRequirements != null) {
                for (CatalogRequirement catalogRequirement : catalogRequirements) {
                    if (catalogRequirement.matchesMajor(majorArray[i])) {
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                majorHours[i] += duration.getHours();
            }
            totalHours[i] += duration.getHours();
        }
        for (int i = 0; i < majorArray.length; i++) {
            majorArray[i].setTotalHours(new Long(totalHours[i]));
            majorArray[i].setMajorHours(new Long(majorHours[i]));
        }
    }

    @PreRemove
    public void removeHours() {
        Set<Major> majors = student.getMajors();
        Long totalHours[] = new Long[majors.size()];
        Long majorHours[] = new Long[majors.size()];
        Major majorArray[] = new Major[majors.size()];
        int it = 0;
        for (Major major : majors) {
            majorArray[it] = major;
            majorHours[it] = (long) 0;
            totalHours[it++] = (long) 0;
        }
        Set<WorkEffort> jobs = student.getWorkEffort();
        for (WorkEffort job : jobs) {
            if (job.getValidation().equals(Validation.FAILED_VALIDATION)) {
                continue;
            }
            if (job.getVerification().equals(Verification.DENIED)) {
                continue;
            }
            if (job.getId().equals(getId())) {
                continue;
            }
            for (int i = 0; i < majorArray.length; i++) {
                boolean found = false;
                Set<CatalogRequirement> catalogRequirements = job.getCatalogRequirements();
                for (CatalogRequirement catalogRequirement : catalogRequirements) {
                    if (catalogRequirement.matchesMajor(majorArray[i])) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    majorHours[i] += job.getDuration().getHours();
                }
                totalHours[i] += job.getDuration().getHours();
            }
        }
        for (int i = 0; i < majorArray.length; i++) {
            majorArray[i].setTotalHours(new Long(totalHours[i]));
            majorArray[i].setMajorHours(new Long(majorHours[i]));
        }
        
        jobs.remove(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Position: ").append(getWorkPosition()).append("\n");
        sb.append("At: ").append(getEmployer().getName()).append("\n");
        sb.append("Duration: ").append(getDuration()).append("\n").append("\n");
        return sb.toString();
    }

    public void audit() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAuthentication = (null != authentication);
        String userName = "";
        if (hasAuthentication) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                userName = ((UserDetails) principal).getUsername();
            } else {
                userName = principal.toString();
            }
        }
        String studentId = student.getUserId();
        LOG.info("User {} updated job {} for student {}.", new Object[] { userName, getWorkPosition(), studentId });
    }
    
    public Set<CatalogRequirement> getTransitionCatalogRequirements() {
    	Set<CatalogRequirement> returnedRequirements = new HashSet<CatalogRequirement>();
    	Set<CatalogRequirement> allRequirements = new HashSet<CatalogRequirement>(CatalogRequirement.findAllCatalogRequirements());
    	
    	Set<Major> majors = student.getMajors();
    	for (Iterator<CatalogRequirement> it = allRequirements.iterator(); it.hasNext();) {
    		CatalogRequirement requirement = it.next();
    		boolean found = false;
    		for (Major major : majors) {
    			if (major.getCatalogTerm().isBetween(requirement.getStartTerm(), requirement.getEndTerm())) {
    				found = true;
    				break;
    			}
    		}
    		if (!found) {
    			it.remove();
    		}
    	}
    	
    	Set<CatalogRequirement> jobRequirements = new HashSet<CatalogRequirement>(catalogRequirements);
    	for (CatalogRequirement jobRequirement : jobRequirements) {
    		if (!allRequirements.contains(jobRequirement)) { // if it is not in the allRequirements set then it is outdated
    			CatalogRequirement matchingRequirement = null;
    			for (CatalogRequirement allRequirement : allRequirements) {
    				if (allRequirement.getDegreeCodePrefix().equals(jobRequirement.getDegreeCodePrefix())) {
    					matchingRequirement = allRequirement;
    					break;
    				}
    			}
    			if (matchingRequirement != null) {
    				returnedRequirements.add(matchingRequirement);
    			}
    		}
    		else {
    			returnedRequirements.add(jobRequirement);
    		}
    	}
    	
    	return returnedRequirements;
    }
    
    public String getEmployerName() { // support for student show.jspx table
    	return employer.getName();
    }
    
    public String getDurationDateRange() { // support for student show.jspx table
    	StringBuilder sb = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", LocaleContextHolder.getLocale());
        sb.append(formatter.format(duration.getStartDate()));
        sb.append( " to ");
        sb.append(formatter.format(duration.getEndDate()));
        return sb.toString();
    }
    
    public String getDurationHours() { // support for student show.jspx table
    	return duration.getHours().toString();
    }
    
    public static List<WorkEffort> findStudentWorkEffortsOrderedById(Student student) {
        return entityManager().createQuery("select o from WorkEffort o where o.student = '" + student.getId() + "' order by o.id asc", WorkEffort.class).getResultList();
    }
}
