package edu.unlv.cs.rebelhotel.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import edu.unlv.cs.rebelhotel.domain.Term;
import edu.unlv.cs.rebelhotel.domain.WorkEffort;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findStudentsByFirstNameEquals", "findStudentsByFirstNameLike", "findStudentsByUserAccount", "findStudentsByUserIdEquals" })
public class Student {

    @NotNull
    @Column(unique = true)
    private String userId;

    @NotNull
    @Size(min = 5)
    private String email = "default";

    @NotNull
    @Size(min = 2)
    private String firstName;

    private String middleName;

    private String lastName;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Major> majors = new HashSet<Major>();

    @ManyToOne
    private Term admitTerm;

    @ManyToOne
    private Term gradTerm;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<WorkEffort> workEffort = new HashSet<WorkEffort>();

    private Boolean codeOfConductSigned;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date lastModified;

    @OneToOne(optional = false)
    private UserAccount userAccount;
    
    /*@ManyToOne
    private Set<Integer> approvedHoursList = new HashSet<Integer>();
    
    @ManyToOne
    private Set<Integer> remainingHoursList = new HashSet<Integer>();
    
	List<Integer> approvedHoursList = new ArrayList<Integer>();
	List<Integer> remainingHoursList = new ArrayList<Integer>();*/
	
    @PreUpdate
    @PrePersist
    public void onUpdate() {
    	lastModified = new Date();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(" + userId + ")");
        sb.append(" " + firstName);
        if (lastName != null) {
            sb.append(" " + lastName);
        }
        return sb.toString();
    }

    public void addWorkEffort(WorkEffort we) {
        workEffort.add(we);
    }
    
    /*public void addApprovedHours(Integer approvedHours){
    	approvedHoursList.add(approvedHours);
    }
    
    public void addRemainingHours(Integer remainingHours){
    	remainingHoursList.add(remainingHours);
    }    */
    
    public String getName() {
    	String name = firstName;
    	if (lastName != null) {
    		name += " " + lastName;
    	}
    	return name;
    }
}
