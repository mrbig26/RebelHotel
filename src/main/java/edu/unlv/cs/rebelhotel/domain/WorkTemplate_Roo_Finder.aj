// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.rebelhotel.domain;

import edu.unlv.cs.rebelhotel.domain.Term;
import edu.unlv.cs.rebelhotel.domain.WorkTemplate;
import edu.unlv.cs.rebelhotel.domain.enums.Degree;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect WorkTemplate_Roo_Finder {
    
    public static TypedQuery<WorkTemplate> WorkTemplate.findWorkTemplatesByDegreeAndTermEquals(Degree degree, Term term) {
        if (degree == null) throw new IllegalArgumentException("The degree argument is required");
        if (term == null) throw new IllegalArgumentException("The term argument is required");
        EntityManager em = WorkTemplate.entityManager();
        TypedQuery<WorkTemplate> q = em.createQuery("SELECT WorkTemplate FROM WorkTemplate AS worktemplate WHERE worktemplate.degree = :degree AND worktemplate.term = :term", WorkTemplate.class);
        q.setParameter("degree", degree);
        q.setParameter("term", term);
        return q;
    }
    
}
