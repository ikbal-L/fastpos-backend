package com.softlines.fastpos.repository.em;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.Metamodel;
import java.util.*;
import java.util.stream.Collectors;


public class RepositoryDecoratorImp<T, ID> implements RepositoryDecorator<T, ID> {

    private  JpaRepository<T, ID> repository;
    private EntityManager entityManager;


    public RepositoryDecoratorImp(JpaRepository<T, ID> repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(transactionManager = "transactionManager")
    public void deleteSetNull(T entity, ID id) {

//        em.getTransaction().begin();

        repository.delete(entity);
        var associations = associations(entityManager, (Class<T>) entity.getClass());
        for (Map.Entry<String, String> tableAndForeignKEy :associations.entrySet()) {

//            String query = buildQuery(association,foreignKeyName,id);
            String query = buildQuery(tableAndForeignKEy.getKey(),tableAndForeignKEy.getValue(),id);
            var q = entityManager.createNativeQuery(query);
            q.executeUpdate();
        }
//        em.getTransaction().commit();
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

    private Map<String,String> associations(EntityManager em, Class<T> entityClass) {
        Metamodel meta = em.getMetamodel();
        EntityType<T> entityType = meta.entity(entityClass);
        var attributes = entityType.getAttributes().stream().filter(Attribute::isAssociation).collect(Collectors.toList());

        Map<String,String> associations = new HashMap<>();
        for (Attribute<? super T, ?> attribute : attributes) {

            String associationTableName = getTableName(em, ((ListAttribute) attribute).getBindableJavaType());
            var foreignKey =getForeignKEy(((ListAttribute) attribute).getBindableJavaType(),entityClass);
           
            associations.put(associationTableName,foreignKey);
        }
        return associations;
    }

    private String getForeignKEy(Class<T> entityClass,Class<T> foreignEntityClass){

        var field = Arrays.stream(entityClass.getDeclaredFields()).filter(f -> f.getType() == foreignEntityClass).findFirst();
        if (field.isPresent()){
            JoinColumn column = field.get().getAnnotation(JoinColumn.class);
            return (column == null) ? field.get().getName() : column.name();
        }
       return null;
    }
}
