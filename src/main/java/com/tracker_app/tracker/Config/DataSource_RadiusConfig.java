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
    basePackages = "com.tracker_app.tracker.DataSource_Radius", 
    entityManagerFactoryRef = "RadiusEntityManager", 
    transactionManagerRef = "RadiusTransactionManager"
)
public class DataSource_RadiusConfig {
    @Autowired
    private Environment env;

    public DataSource_RadiusConfig() {
        super();
    }

    @Bean(name = "RadiusEntityManager")
    public LocalContainerEntityManagerFactoryBean RadiusEntityManager() {
        final LocalContainerEntityManagerFactoryBean EntityManager = new LocalContainerEntityManagerFactoryBean();
        EntityManager.setDataSource(RadiusDataSource());
        EntityManager.setPackagesToScan("com.tracker_app.tracker.DataSource_Radius.Entity");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        EntityManager.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("radius.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        EntityManager.setJpaPropertyMap(properties);

        return EntityManager;
    }

    @Bean
    public DataSource RadiusDataSource() {
 
        DriverManagerDataSource dataSource
          = new DriverManagerDataSource();
        dataSource.setDriverClassName(
          env.getProperty("radius.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("radius.datasource.url"));
        dataSource.setUsername(env.getProperty("radius.datasource.username"));
        dataSource.setPassword(env.getProperty("radius.datasource.password"));
 
        return dataSource;
    }

    @Bean(name = "RadiusTransactionManager")
    public PlatformTransactionManager RadiusTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(RadiusEntityManager().getObject());
        return transactionManager;
    }
}
