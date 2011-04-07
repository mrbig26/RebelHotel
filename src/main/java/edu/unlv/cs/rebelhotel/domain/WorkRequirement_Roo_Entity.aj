// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.rebelhotel.domain;

import edu.unlv.cs.rebelhotel.domain.WorkRequirement;
import java.lang.Integer;
import java.lang.Long;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.springframework.transaction.annotation.Transactional;

privileged aspect WorkRequirement_Roo_Entity {
    
    declare @type: WorkRequirement: @Entity;
    
    @PersistenceContext
    transient EntityManager WorkRequirement.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long WorkRequirement.id;
    
    @Version
    @Column(name = "version")
    private Integer WorkRequirement.version;
    
    public Long WorkRequirement.getId() {
        return this.id;
    }
    
    public void WorkRequirement.setId(Long id) {
        this.id = id;
    }
    
    public Integer WorkRequirement.getVersion() {
        return this.version;
    }
    
    public void WorkRequirement.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void WorkRequirement.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void WorkRequirement.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            WorkRequirement attached = WorkRequirement.findWorkRequirement(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void WorkRequirement.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public WorkRequirement WorkRequirement.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        WorkRequirement merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager WorkRequirement.entityManager() {
        EntityManager em = new WorkRequirement().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long WorkRequirement.countWorkRequirements() {
        return entityManager().createQuery("select count(o) from WorkRequirement o", Long.class).getSingleResult();
    }
    
    public static List<WorkRequirement> WorkRequirement.findAllWorkRequirements() {
        return entityManager().createQuery("select o from WorkRequirement o", WorkRequirement.class).getResultList();
    }
    
    public static WorkRequirement WorkRequirement.findWorkRequirement(Long id) {
        if (id == null) return null;
        return entityManager().find(WorkRequirement.class, id);
    }
    
    public static List<WorkRequirement> WorkRequirement.findWorkRequirementEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from WorkRequirement o", WorkRequirement.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
