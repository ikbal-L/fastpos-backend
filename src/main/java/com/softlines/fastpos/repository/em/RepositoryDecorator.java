package com.softlines.fastpos.repository.em;

import org.springframework.stereotype.Repository;


public interface RepositoryDecorator<T, ID> {
    void deleteSetNull(T entity, ID id);
}
