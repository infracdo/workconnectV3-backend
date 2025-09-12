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
@EnableJpaRepositories(
    basePackages = "com.tracker_app.tracker.DataSource_Tracker", 
    entityManagerFactoryRef = "TrackerEntityManager", 
    transactionManagerRef = "TrackerTransactionManager"
)
public class DataSource_TrackerConfig {
    @Autowired
    private Environment env;

    public DataSource_TrackerConfig() {
        super();
    }

    @Bean(name = "TrackerEntityManager")
    public LocalContainerEntityManagerFactoryBean TrackerEntityManager() {
        final LocalContainerEntityManagerFactoryBean EntityManager = new LocalContainerEntityManagerFactoryBean();
        EntityManager.setDataSource(TrackerDataSource());
        EntityManager.setPackagesToScan("com.tracker_app.tracker.DataSource_Tracker.Entity");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        EntityManager.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("tracker.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        EntityManager.setJpaPropertyMap(properties);

        return EntityManager;
    }

    @Bean
    public DataSource TrackerDataSource() {
 
        DriverManagerDataSource dataSource
          = new DriverManagerDataSource();
        dataSource.setDriverClassName(
          env.getProperty("tracker.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("tracker.datasource.url"));
        dataSource.setUsername(env.getProperty("tracker.datasource.username"));
        dataSource.setPassword(env.getProperty("tracker.datasource.password"));
 
        return dataSource;
    }

    @Bean(name = "TrackerTransactionManager")
    public PlatformTransactionManager TrackerTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(TrackerEntityManager().getObject());
        return transactionManager;
    }
    
}
