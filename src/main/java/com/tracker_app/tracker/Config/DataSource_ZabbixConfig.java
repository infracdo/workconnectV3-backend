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
    basePackages = "com.tracker_app.tracker.DataSource_Zabbix", 
    entityManagerFactoryRef = "ZabbixEntityManager", 
    transactionManagerRef = "ZabbixTransactionManager"
)
public class DataSource_ZabbixConfig {
    @Autowired
    private Environment env;

    public DataSource_ZabbixConfig() {
        super();
    }

    @Bean(name = "ZabbixEntityManager")
    public LocalContainerEntityManagerFactoryBean ZabbixEntityManager() {
        final LocalContainerEntityManagerFactoryBean EntityManager = new LocalContainerEntityManagerFactoryBean();
        EntityManager.setDataSource(ZabbixDataSource());
        EntityManager.setPackagesToScan("com.tracker_app.tracker.DataSource_Zabbix.Entity");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        EntityManager.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("zabbix.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        EntityManager.setJpaPropertyMap(properties);

        return EntityManager;
    }

    @Bean
    public DataSource ZabbixDataSource() {
 
        DriverManagerDataSource dataSource
          = new DriverManagerDataSource();
        dataSource.setDriverClassName(
          env.getProperty("zabbix.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("zabbix.datasource.url"));
        dataSource.setUsername(env.getProperty("zabbix.datasource.username"));
        dataSource.setPassword(env.getProperty("zabbix.datasource.password"));
 
        return dataSource;
    }

    @Bean(name = "ZabbixTransactionManager")
    public PlatformTransactionManager ZabbixTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(ZabbixEntityManager().getObject());
        return transactionManager;
    }
    
}
