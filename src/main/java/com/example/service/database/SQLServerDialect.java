package com.example.service.database;

import jp.sf.amateras.mirage.annotation.PrimaryKey.GenerationType;
import jp.sf.amateras.mirage.dialect.StandardDialect;

public class SQLServerDialect extends StandardDialect {

    @Override
    public String getName() {
        return "sqlserver";
    }

    @Override
    public String getCountSql(String sql) {
        return "SELECT COUNT(*) FROM (" + sql + ") A";
    }

    @Override
    public boolean supportsGenerationType(GenerationType generationType) {
        if (generationType == GenerationType.IDENTITY) {
            return false;
        }
        return true;
    }

    @Override
    public String getSequenceSql(String sequenceName) {
        return String.format(
                "SELECT nextval('%s') AS NEXTVAL", sequenceName);
    }

}
