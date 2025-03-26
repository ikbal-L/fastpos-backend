package com.softlines.fastpos.repository.em;

import org.hibernate.metamodel.model.domain.internal.AbstractPluralAttribute;
import org.hibernate.metamodel.model.domain.internal.SetAttributeImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.Metamodel;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;


public class RepositoryDecoratorImp<T, ID> implements RepositoryDecorator<T, ID> {

    private  JpaRepository<T, ID> repository;
    private EntityManager entityManager;
    private Class<T> entityClassType;

    public RepositoryDecoratorImp(JpaRepository<T, ID> repository, EntityManager entityManager,Class<T> entityClassType) {
        this.repository = repository;
        this.entityManager = entityManager;
        this.entityClassType = entityClassType;
    }

    @Override
    @Transactional(transactionManager = "transactionManager")
    public void deleteSetNull(T entity, ID id) {

        entityManager.getTransaction().begin();

        repository.delete(entity);

        var associations = associations((Class<T>) entity.getClass());
        associations.forEach((table, foreignKey) -> {
            String query = buildQuery(table,foreignKey,id);
            var q = entityManager.createNativeQuery(query);
            q.executeUpdate();
        });
        entityManager.getTransaction().commit();
    }

    @Transactional(transactionManager = "transactionManager")
    public void softDeleteAllById(List<ID> ids){

//        entityManager.getTransaction().begin();
        var q1 = softDeleteAllByIdQuery(ids);
        q1.executeUpdate();
        var associations = associations(this.entityClassType);
        associations.forEach((table, foreignKey) -> {
            var q2= deleteSetNullAllById(table,foreignKey,ids);

            q2.executeUpdate();
        });
//        entityManager.getTransaction().commit();

    }
    private Query softDeleteAllByIdQuery(List<ID> ids){

        var tableName = getTableName(this.entityClassType);
        String query = "update :table set deleted = true  where id IN (:ids)".replace(":table",tableName);

        return entityManager.createNativeQuery(query).setParameter("ids",ids);
    }


    private String buildQuery(String tableName, String foreignKeyName, ID id) {
      String query = "update :table set :foreignKey = NULL  where :foreignKey = :id";
      query =query
              .replace(":table",tableName)
              .replace(":foreignKey",foreignKeyName)
              .replace(":id",id.toString());
        return query;
    }

    private Query deleteSetNullAllById( String tableName, String foreignKeyName, List<ID> fks) {
        String query = "update `:table`set :foreignKey = NULL  where `:foreignKey` IN (:fks)";

        query =query
                .replace(":table",tableName)
                .replace(":foreignKey",foreignKeyName);

        return entityManager.createNativeQuery(query).setParameter("fks",fks);
    }

    private String getTableName(Class<T> entityClass) {
        Metamodel meta = entityManager.getMetamodel();
        EntityType<?> entityType = meta.entity(entityClass);
        Table table = entityClass.getAnnotation(Table.class);

        return (table == null) ? entityType.getName().toUpperCase() : table.name();
//        return entityType.getName();
    }

    private Map<String,String> associations( Class<T> entityClass) {
        Metamodel meta = entityManager.getMetamodel();
        EntityType<T> entityType = meta.entity(entityClass);
        var attributes = entityType.getAttributes().stream().filter(Attribute::isAssociation).collect(Collectors.toList());

        Map<String,String> associations = new HashMap<>();
        for (Attribute<? super T, ?> attribute : attributes) {

            String associationTableName = getTableName( ((AbstractPluralAttribute) attribute).getBindableJavaType());
            var foreignKey =getForeignKEy(((AbstractPluralAttribute) attribute).getBindableJavaType(),entityClass);
           
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
