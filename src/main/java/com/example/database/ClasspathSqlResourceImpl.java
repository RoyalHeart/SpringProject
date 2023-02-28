package com.example.database;

import java.io.IOException;
import java.io.InputStream;

import com.miragesql.miragesql.SqlResource;

public class ClasspathSqlResourceImpl implements SqlResource {

    private final String sqlPath;

    public ClasspathSqlResourceImpl(String sqlPath) {
        this.sqlPath = sqlPath;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return ClasspathSqlResourceImpl.class.getResourceAsStream(sqlPath);
    }
}
