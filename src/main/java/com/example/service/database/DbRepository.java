package com.example.service.database;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;

import jp.xet.sparwings.spring.data.repository.BatchReadableRepository;
import jp.xet.sparwings.spring.data.repository.BatchWritableRepository;
import jp.xet.sparwings.spring.data.repository.ScannableRepository;

@NoRepositoryBean
public interface DbRepository<E, ID extends Serializable>
        extends ScannableRepository<E, ID>, BatchReadableRepository<E, ID>, BatchWritableRepository<E, ID> {

}
