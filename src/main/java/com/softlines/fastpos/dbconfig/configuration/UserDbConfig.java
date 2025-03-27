package com.softlines.fastpos.dbconfig.configuration;


import com.softlines.fastpos.security.securityconfiguration.AuditorAwareImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import org.sqlite.SQLiteConfig;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.softlines.fastpos.security.securityrepository",
        entityManagerFactoryRef = "authEntityManagerFactory",
        transactionManagerRef = "authTransactionManager"
)
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class UserDbConfig {

    @Autowired
    private Environment env;

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "com.softlines.fastpos.security")
    public DataSourceProperties authDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName ;


    @Bean
    public DataSource authDataSource() throws Exception {
        try{
            DataSourceProperties authDataSourceProperties = authDataSourceProperties();

            SQLiteConfig config = new SQLiteConfig();
            config.setReadOnly(false);
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(driverClassName);
            dataSource.setUrl(authDataSourceProperties.getUrl());
            dataSource.setUsername(authDataSourceProperties.getUsername());
            dataSource.setPassword(authDataSourceProperties.getPassword());
            dataSource.setConnectionProperties(config.toProperties());

            return dataSource;
//            return DataSourceBuilder.create()
////
//                    .driverClassName(driverClassName)
//                    .url(authDataSourceProperties.getUrl())
//                    .username(authDataSourceProperties.getUsername())
//                    .password(authDataSourceProperties.getPassword())
//
//                    .build();
        }catch (Exception e){
            throw new Exception("DB not Found Exception: "+ e.getMessage());
        }
    }

    @Bean()
    //@Primary
    public PlatformTransactionManager authTransactionManager() throws Exception {
        try{
            EntityManagerFactory factory = authEntityManagerFactory().getObject();
            return new JpaTransactionManager(factory);
        }catch (Exception e){
            throw new Exception("DB not Found Exception: "+ e);
        }
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean authEntityManagerFactory() throws Exception {
        try {
            LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
            factory.setDataSource(authDataSource());
            factory.setPackagesToScan("com.softlines.fastpos.security.securitydomain");
            factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
            Properties jpaProperties = new Properties();
            jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
            jpaProperties.put("hibernate.show-sql", env.getProperty("hibernate.show-sql"));
            factory.setJpaProperties(jpaProperties);
            return factory;
        }catch (Exception e){
            throw new Exception("DB not Found Exception: "+ e);
        }
    }

    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

}
