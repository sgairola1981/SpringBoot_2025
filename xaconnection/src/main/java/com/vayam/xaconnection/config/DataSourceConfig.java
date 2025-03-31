package com.vayam.xaconnection.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    @Bean(name = "oracleDataSource")
    public DataSource oracleDataSource() {
        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setUniqueResourceName("oracleXa");
        dataSource.setXaDataSourceClassName("oracle.jdbc.xa.client.OracleXADataSource");

        Properties xaProperties = new Properties();
        xaProperties.setProperty("user", "HR");
        xaProperties.setProperty("password", "HR");
        xaProperties.setProperty("url", "jdbc:oracle:thin:@localhost:1521:XE");
        xaProperties.setProperty("driverType", "thin");
        dataSource.setXaProperties(xaProperties);

        dataSource.setMinPoolSize(5);
        dataSource.setMaxPoolSize(20);
        return dataSource;
    }

    @Bean(name = "sqlServerDataSource")
    public DataSource sqlServerDataSource() {
        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setUniqueResourceName("sqlServerXa");
        dataSource.setXaDataSourceClassName("com.microsoft.sqlserver.jdbc.SQLServerXADataSource");

        Properties xaProperties = new Properties();
        xaProperties.setProperty("user", "gsirola");
        xaProperties.setProperty("password", "gairola");
        xaProperties.setProperty("serverName", "localhost");
        xaProperties.setProperty("portNumber", "1433");
        xaProperties.setProperty("databaseName", "GAIROLA_DB");
        xaProperties.setProperty("encrypt", "true");
        xaProperties.setProperty("trustServerCertificate", "true");
        dataSource.setXaProperties(xaProperties);

        dataSource.setMinPoolSize(5);
        dataSource.setMaxPoolSize(20);
        return dataSource;
    }

    @Bean(name = "oracleJdbcTemplate")
    public JdbcTemplate oracleJdbcTemplate(DataSource oracleDataSource) {
        return new JdbcTemplate(oracleDataSource);
    }

    @Bean(name = "sqlServerJdbcTemplate")
    public JdbcTemplate sqlServerJdbcTemplate(DataSource sqlServerDataSource) {
        return new JdbcTemplate(sqlServerDataSource);
    }
}
