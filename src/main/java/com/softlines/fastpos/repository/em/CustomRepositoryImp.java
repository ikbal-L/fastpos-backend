package com.softlines.fastpos.repository.em;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;



public class CustomRepositoryImp<T, ID> implements CustomRepository<T, ID> {

    private  JpaRepository<T, ID> repository;

    private EntityManagerFactory entityManagerFactory;

    public CustomRepositoryImp(JpaRepository<T, ID> repository, EntityManagerFactory entityManagerFactory) {
        this.repository = repository;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void delete(T entity, ID id, String foreignKeyName) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        repository.delete(entity);
        var associations = associations(em, (Class<T>) entity.getClass());
        for (String association : associations) {
            String query = buildQuery(association,foreignKeyName,id);
            var q = em.createNativeQuery(query).setParameter("id", id);
            q.executeUpdate();
        }
        em.getTransaction().commit();
    }

    private String buildQuery(String tableName, String foreignKeyName, ID id) {
        StringBuilder sb = new StringBuilder();
        sb
                .append("UPDATE FROM ")
                .append("`")
                .append(tableName)
                .append("`")
                .append(" SET ")
                .append("`")
                .append(foreignKeyName)
                .append("`")
                .append(" = NULL")
                .append(" WHERE ")
                .append("`")
                .append(foreignKeyName)
                .append("`")
                .append(" = :id");
        return sb.toString();
    }

    private String getTableName(EntityManager em, Class<T> entityClass) {
        Metamodel meta = em.getMetamodel();
        EntityType<?> entityType = meta.entity(entityClass);
        Table table = entityClass.getAnnotation(Table.class);

        return (table == null) ? entityType.getName().toUpperCase() : table.name();
    }

    private String[] associations(EntityManager em, Class<T> entityClass) {
        Metamodel meta = em.getMetamodel();
        EntityType<T> entityType = meta.entity(entityClass);
        List<String> associations = new ArrayList<>();
        ManyToOne[] manyToOneAssociations = entityClass.getAnnotationsByType(ManyToOne.class);
        for (ManyToOne manyToOne : manyToOneAssociations) {
            String targetEntityTableName = getTableName(em, manyToOne.targetEntity());
            associations.add(targetEntityTableName);
        }
        return associations.toArray(String[]::new);
    }
}
