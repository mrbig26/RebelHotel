// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.rebelhotel.domain;

import edu.unlv.cs.rebelhotel.domain.Student;
import edu.unlv.cs.rebelhotel.domain.Term;
import edu.unlv.cs.rebelhotel.domain.WorkEffort;
import edu.unlv.cs.rebelhotel.domain.enums.Validation;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect WorkEffort_Roo_Finder {
    
    public static TypedQuery<WorkEffort> WorkEffort.findWorkEffortsByStudentEquals(Student student) {
        if (student == null) throw new IllegalArgumentException("The student argument is required");
        EntityManager em = WorkEffort.entityManager();
        TypedQuery<WorkEffort> q = em.createQuery("SELECT WorkEffort FROM WorkEffort AS workeffort WHERE workeffort.student = :student", WorkEffort.class);
        q.setParameter("student", student);
        return q;
    }
    
    public static TypedQuery<WorkEffort> WorkEffort.findWorkEffortsByValidationAndTermSubmitted(Validation validation, Term termSubmitted) {
        if (validation == null) throw new IllegalArgumentException("The validation argument is required");
        if (termSubmitted == null) throw new IllegalArgumentException("The termSubmitted argument is required");
        EntityManager em = WorkEffort.entityManager();
        TypedQuery<WorkEffort> q = em.createQuery("SELECT WorkEffort FROM WorkEffort AS workeffort WHERE workeffort.validation = :validation AND workeffort.termSubmitted = :termSubmitted", WorkEffort.class);
        q.setParameter("validation", validation);
        q.setParameter("termSubmitted", termSubmitted);
        return q;
    }
    
}
