package com.softlines.fastpos.repository.em;

import org.springframework.stereotype.Repository;

@Repository
public interface CustomRepository<T, ID> {
    void delete(T entity, ID id, String foreignKeyName);
}
