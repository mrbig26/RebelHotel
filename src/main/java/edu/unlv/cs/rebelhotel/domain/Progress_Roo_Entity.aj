// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.rebelhotel.domain;

import edu.unlv.cs.rebelhotel.domain.Progress;
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

privileged aspect Progress_Roo_Entity {
    
    declare @type: Progress: @Entity;
    
    @PersistenceContext
    transient EntityManager Progress.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Progress.id;
    
    @Version
    @Column(name = "version")
    private Integer Progress.version;
    
    public Long Progress.getId() {
        return this.id;
    }
    
    public void Progress.setId(Long id) {
        this.id = id;
    }
    
    public Integer Progress.getVersion() {
        return this.version;
    }
    
    public void Progress.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Progress.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Progress.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Progress attached = Progress.findProgress(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Progress.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public Progress Progress.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Progress merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager Progress.entityManager() {
        EntityManager em = new Progress().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Progress.countProgresses() {
        return entityManager().createQuery("select count(o) from Progress o", Long.class).getSingleResult();
    }
    
    public static List<Progress> Progress.findAllProgresses() {
        return entityManager().createQuery("select o from Progress o", Progress.class).getResultList();
    }
    
    public static Progress Progress.findProgress(Long id) {
        if (id == null) return null;
        return entityManager().find(Progress.class, id);
    }
    
    public static List<Progress> Progress.findProgressEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Progress o", Progress.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}