package com.example.config;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.example.service.database.SQLServerDialect;
import com.example.service.database.SqlManagerServiceImpl;

import jp.sf.amateras.mirage.bean.BeanDescFactory;
import jp.sf.amateras.mirage.bean.FieldPropertyExtractor;
import jp.sf.amateras.mirage.integration.spring.SpringConnectionProvider;
import jp.sf.amateras.mirage.naming.RailsLikeNameConverter;
import jp.sf.amateras.mirage.provider.ConnectionProvider;
import jp.xet.springframework.data.mirage.repository.config.EnableMirageRepositories;
import jp.xet.springframework.data.mirage.repository.support.MiragePersistenceExceptionTranslator;

@Configuration
@EnableMirageRepositories(basePackages = "com.example.persistence.mirageRepo", sqlManagerRef = "sqlManagerService")
public class MirageConfig {

    @Autowired
    private DataSourceTransactionManager transactionManager;

    private BeanDescFactory beanDescFactory;

    private SpringConnectionProvider connectionProvider;

    @Bean
    public BeanDescFactory beanDescFactory() {
        if (beanDescFactory == null) {
            beanDescFactory = new BeanDescFactory();
            beanDescFactory.setPropertyExtractor(new FieldPropertyExtractor());
        }
        return beanDescFactory;
    }

    @Bean
    public SqlManagerServiceImpl sqlManagerService() throws SQLException {
        // bridge java.util.logging used by mirage
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        SqlManagerServiceImpl sqlManagerServiceImpl = new SqlManagerServiceImpl();
        sqlManagerServiceImpl.setConnectionProvider(connectionProvider());
        // String driver = "";
        // String dbUrl = "";
        // try {
        // dbUrl =
        // transactionManager.getDataSource().getConnection().getMetaData().getURL();
        // driver =
        //
        DriverManager.getDriver(transactionManager.getDataSource().getConnection().getMetaData().getURL()).toString();
        // } catch (SQLException e) {
        // // Auto-generated catch block
        // e.printStackTrace();
        // }
        // TODO Detect dialect
        // sqlManagerServiceImpl.setDialect(new OracleDialect());
        sqlManagerServiceImpl.setDialect(new SQLServerDialect());
        sqlManagerServiceImpl.setBeanDescFactory(beanDescFactory());
        sqlManagerServiceImpl.setNameConverter(new RailsLikeNameConverter());
        return sqlManagerServiceImpl;
    }

    @Bean
    public MiragePersistenceExceptionTranslator persistenceExceptionTranslator() {
        return new MiragePersistenceExceptionTranslator();
    }

    @Bean
    public ConnectionProvider connectionProvider() {
        if (connectionProvider == null) {
            connectionProvider = new SpringConnectionProvider();
            connectionProvider.setTransactionManager(transactionManager);
        }
        return connectionProvider;
    }
}