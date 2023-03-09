package com.example.service.database;

import org.springframework.stereotype.Service;

import jp.sf.amateras.mirage.SqlManagerImpl;

@Service
public class SqlManagerServiceImpl extends SqlManagerImpl implements SqlManagerService {

    @Override
    public Long getNextValBySeqName(String seqName) {
        String querySql = dialect.getSequenceSql(seqName);
        Long sequenceValue = super.getSingleResultBySql(Long.class, querySql);
        return sequenceValue;
    }
}
