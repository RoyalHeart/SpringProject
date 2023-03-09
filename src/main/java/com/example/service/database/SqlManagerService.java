package com.example.service.database;

import jp.sf.amateras.mirage.SqlManager;

public interface SqlManagerService extends SqlManager {

    Long getNextValBySeqName(String sequenceName);
}
