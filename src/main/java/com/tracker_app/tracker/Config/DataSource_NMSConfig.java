package com.tracker_app.tracker.Config;

import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@EnableCaching
@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(
  basePackages = "com.tracker_app.tracker.DataSource_NMS",
  entityManagerFactoryRef = "NMSEntityManager",
  transactionManagerRef = "NMSTransactionManager"
)
public class DataSource_NMSConfig {

  @Autowired
  private Environment env;

  public DataSource_NMSConfig() {
    super();
  }

  @Bean(name = "NMSEntityManager")
  public LocalContainerEntityManagerFactoryBean NMSEntityManager() {
    final LocalContainerEntityManagerFactoryBean EntityManager = new LocalContainerEntityManagerFactoryBean();
    EntityManager.setDataSource(NMSDataSource());
    EntityManager.setPackagesToScan(
      "com.tracker_app.tracker.DataSource_NMS.Entity"
    );

    final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    EntityManager.setJpaVendorAdapter(vendorAdapter);
    final HashMap<String, Object> properties = new HashMap<String, Object>();
    properties.put(
      "hibernate.hbm2ddl.auto",
      env.getProperty("nms.jpa.hibernate.ddl-auto")
    );
    properties.put(
      "hibernate.dialect",
      env.getProperty("postgress.jpa.properties.hibernate.dialect")
    );
    EntityManager.setJpaPropertyMap(properties);

    return EntityManager;
  }

  @Bean
  public DataSource NMSDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(env.getProperty("nms.datasource.url"));
    dataSource.setUsername(env.getProperty("nms.datasource.username"));
    dataSource.setPassword(env.getProperty("nms.datasource.password"));

    return dataSource;
  }

  @Bean(name = "NMSTransactionManager")
  public PlatformTransactionManager NMSTransactionManager() {
    final JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(NMSEntityManager().getObject());
    return transactionManager;
  }
}
