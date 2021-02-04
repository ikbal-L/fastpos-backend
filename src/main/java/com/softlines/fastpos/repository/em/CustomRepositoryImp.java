package com.softlines.fastpos.repository.em;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
            var q = em.createNativeQuery(query);
            q.executeUpdate();
        }
        em.getTransaction().commit();
    }

    private String buildQuery(String tableName, String foreignKeyName, ID id) {
      String query = "update `:table` set :foreignKey = NULL  where `:foreignKey` = :id";
      query =query
              .replace(":table",tableName)
              .replace(":foreignKey",foreignKeyName)
              .replace(":id",id.toString());
        return query;
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
        var attributes = entityType.getAttributes().stream().filter(Attribute::isAssociation).collect(Collectors.toList());

        List<String> associations = new ArrayList<>();
        for (Attribute<? super T, ?> attribute : attributes) {

            String associationTableName = getTableName(em, ((ListAttribute) attribute).getBindableJavaType());
            associations.add(associationTableName);
        }
        return associations.toArray(String[]::new);
    }
}
