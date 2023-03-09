package com.example.service.database;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface MirageRepository<E, ID extends Serializable> extends PagingAndSortingRepository<E, ID> {
    E findOne(ID id);

    List<E> findAll();

    boolean exists(ID id);

    long count();

    <S extends E> S save(S entity);
}