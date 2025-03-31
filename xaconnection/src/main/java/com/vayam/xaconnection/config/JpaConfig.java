package com.vayam.xaconnection.config;

import jakarta.persistence.EntityManagerFactory;

import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.icatch.jta.UserTransactionImp;

import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
public class JpaConfig {

    @Bean(name = "transactionManager")
    public JtaTransactionManager transactionManager(UserTransaction userTransaction, TransactionManager transactionManager) {
        return new JtaTransactionManager(userTransaction, transactionManager);
    }

    @Bean(name = "oracleEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(DataSource oracleDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(oracleDataSource);
        em.setPackagesToScan("com.vayam.xaconnection.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.OracleDialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.transaction.jta.platform", "AtomikosJtaPlatform");
        properties.setProperty("jakarta.persistence.transactionType", "JTA");

        em.setJpaProperties(properties);
        return em;
    }

    @Bean(name = "sqlServerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sqlServerEntityManagerFactory(DataSource sqlServerDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(sqlServerDataSource);
        em.setPackagesToScan("com.vayam.xaconnection.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.transaction.jta.platform", "AtomikosJtaPlatform");
        properties.setProperty("jakarta.persistence.transactionType", "JTA");

        em.setJpaProperties(properties);
        return em;
    }

    @Bean(name = "userTransaction")
    public UserTransactionImp userTransaction() throws Throwable {
        UserTransactionImp userTransaction = new UserTransactionImp();
       userTransaction.setTransactionTimeout(300);
        return userTransaction;
    }

    @Bean(name = "atomikosTransactionManager")
    public UserTransactionManager atomikosTransactionManager() throws SystemException {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.init();
        return userTransactionManager;
    }

    @Bean(name = "transactionManagerAtomikos")
    public JtaTransactionManager atomikosJtaTransactionManager(UserTransaction userTransaction, TransactionManager transactionManager) {
        return new JtaTransactionManager(userTransaction, transactionManager);
    }
}
