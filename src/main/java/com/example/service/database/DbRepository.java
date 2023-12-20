package com.example.service.database;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface DbRepository<E, ID extends Serializable>
        extends PagingAndSortingRepository<E, ID>, CrudRepository<E, ID> {

}
