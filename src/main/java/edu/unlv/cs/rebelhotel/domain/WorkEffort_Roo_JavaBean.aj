// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.rebelhotel.domain;

import edu.unlv.cs.rebelhotel.domain.CatalogRequirement;
import edu.unlv.cs.rebelhotel.domain.Employer;
import edu.unlv.cs.rebelhotel.domain.Student;
import edu.unlv.cs.rebelhotel.domain.Supervisor;
import edu.unlv.cs.rebelhotel.domain.Term;
import edu.unlv.cs.rebelhotel.domain.WorkEffortDuration;
import edu.unlv.cs.rebelhotel.domain.enums.PayStatus;
import edu.unlv.cs.rebelhotel.domain.enums.Validation;
import edu.unlv.cs.rebelhotel.domain.enums.Verification;
import edu.unlv.cs.rebelhotel.domain.enums.VerificationType;
import java.lang.String;
import java.util.Set;

privileged aspect WorkEffort_Roo_JavaBean {
    
    public Student WorkEffort.getStudent() {
        return this.student;
    }
    
    public void WorkEffort.setStudent(Student student) {
        this.student = student;
    }
    
    public String WorkEffort.getWorkPosition() {
        return this.workPosition;
    }
    
    public void WorkEffort.setWorkPosition(String workPosition) {
        this.workPosition = workPosition;
    }
    
    public String WorkEffort.getComment() {
        return this.comment;
    }
    
    public void WorkEffort.setComment(String comment) {
        this.comment = comment;
    }
    
    public Supervisor WorkEffort.getSupervisor() {
        return this.supervisor;
    }
    
    public void WorkEffort.setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }
    
    public Employer WorkEffort.getEmployer() {
        return this.employer;
    }
    
    public void WorkEffort.setEmployer(Employer employer) {
        this.employer = employer;
    }
    
    public VerificationType WorkEffort.getVerificationType() {
        return this.verificationType;
    }
    
    public void WorkEffort.setVerificationType(VerificationType verificationType) {
        this.verificationType = verificationType;
    }
    
    public Validation WorkEffort.getValidation() {
        return this.validation;
    }
    
    public void WorkEffort.setValidation(Validation validation) {
        this.validation = validation;
    }
    
    public Verification WorkEffort.getVerification() {
        return this.verification;
    }
    
    public void WorkEffort.setVerification(Verification verification) {
        this.verification = verification;
    }
    
    public PayStatus WorkEffort.getPayStatus() {
        return this.payStatus;
    }
    
    public void WorkEffort.setPayStatus(PayStatus payStatus) {
        this.payStatus = payStatus;
    }
    
    public WorkEffortDuration WorkEffort.getDuration() {
        return this.duration;
    }
    
    public void WorkEffort.setDuration(WorkEffortDuration duration) {
        this.duration = duration;
    }
    
    public Term WorkEffort.getTermSubmitted() {
        return this.termSubmitted;
    }
    
    public void WorkEffort.setTermSubmitted(Term termSubmitted) {
        this.termSubmitted = termSubmitted;
    }
    
    public Set<CatalogRequirement> WorkEffort.getCatalogRequirements() {
        return this.catalogRequirements;
    }
    
    public void WorkEffort.setCatalogRequirements(Set<CatalogRequirement> catalogRequirements) {
        this.catalogRequirements = catalogRequirements;
    }
    
}
