package com.tracker_app.tracker.Config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(basePackages = "com.tracker_app.tracker.DataSource_Atis", entityManagerFactoryRef = "AtisEntityManager", transactionManagerRef = "AtisTransactionManager")
public class DataSource_Atis {
    @Autowired
    private Environment env;

    public DataSource_Atis() {
        super();
    }

    @Bean(name = "AtisEntityManager")
    public LocalContainerEntityManagerFactoryBean AtisEntityManager() {
        final LocalContainerEntityManagerFactoryBean EntityManager = new LocalContainerEntityManagerFactoryBean();
        EntityManager.setDataSource(AtisDataSource());
        EntityManager.setPackagesToScan("com.tracker_app.tracker.DataSource_Atis.Entity");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        EntityManager.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("atis.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("postgress.jpa.properties.hibernate.dialect"));
        EntityManager.setJpaPropertyMap(properties);

        return EntityManager;
    }

    @Bean
    public DataSource AtisDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("atis.datasource.url"));
        dataSource.setUsername(env.getProperty("atis.datasource.username"));
        dataSource.setPassword(env.getProperty("atis.datasource.password"));

        return dataSource;
    }

    @Bean(name = "AtisTransactionManager")
    public PlatformTransactionManager AtisTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(AtisEntityManager().getObject());
        return transactionManager;
    }

}
