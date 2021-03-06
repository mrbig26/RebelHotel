package edu.unlv.cs.rebelhotel.domain;

import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Progress {

	private String degreeCode;

	private Integer relatedHours;

	private Integer remainingHours;

	private Integer totalHours;
	
	public Integer getTotalHours() {
        return this.totalHours;
    }
    
    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }

	public void setDegreeCode(String degreeCode) {
		this.degreeCode = degreeCode;
	}

	public String getDegreeCode() {
		return this.degreeCode;
	}

	public void setRelatedHours(Integer approvedHours) {
		this.relatedHours = approvedHours;

	}

	public Integer getRelatedHours() {
		return this.relatedHours;
	}

	public void setRemainingHours(Integer remainingHours) {
		this.remainingHours = remainingHours;
	}

	public Integer getRemainingHours() {
		return this.remainingHours;
	}

	public Progress() {
	}

	public Progress(Major major, Set<WorkEffort> workHistory) {
		this.degreeCode = major.getDegreeCode();
		this.relatedHours = major.calculateRelatedHoursWorked(workHistory);
		this.remainingHours = major.calculateHoursRemaining(workHistory);
		this.totalHours = major.calculateTotalHours(workHistory);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Progress rhs = (Progress) obj;
		return new EqualsBuilder().append(degreeCode, rhs.degreeCode)
				.append(relatedHours, rhs.relatedHours)
				.append(remainingHours, rhs.remainingHours)
				.append(totalHours, rhs.totalHours).isEquals();
	}
	
	
	
	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(4001,999).append(degreeCode).append(relatedHours)
				.append(remainingHours).append(totalHours).toHashCode();
	}
	
	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DegreeCode: ").append(getDegreeCode()).append(", ");
        sb.append("RelatedHours: ").append(getRelatedHours()).append(", ");
        sb.append("RemainingHours: ").append(getRemainingHours()).append(", ");
        sb.append("TotalHours: ").append(getTotalHours());
        return sb.toString();
    }

}
