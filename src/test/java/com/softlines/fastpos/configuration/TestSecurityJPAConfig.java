package com.softlines.fastpos.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;
@Configuration
@EnableJpaRepositories(
        basePackages = "com.softlines.fastpos.jwtsecurity.securityrepository",
        entityManagerFactoryRef = "authEntityManagerFactory",
        transactionManagerRef = "authTransactionManager"
)
@EnableTransactionManagement
public class TestSecurityJPAConfig {
    @Bean
    @Primary
    @Profile("test")
    public DataSource authDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl("jdbc:mysql://localhost:3306/dbsecuritytesting?createDatabaseIfNotExist=true");
        dataSource.setUsername("root");
        dataSource.setPassword("");

        return dataSource;
    }
    @Bean
    @Profile("test")
    public PlatformTransactionManager authTransactionManager()
    {
        EntityManagerFactory factory = authEntityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }

    @Bean
    @Profile("test")
    public LocalContainerEntityManagerFactoryBean authEntityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(authDataSource());
        factory.setPackagesToScan("com.softlines.fastpos.jwtsecurity.securitydomain");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", "create-drop");
        jpaProperties.put("spring.jpa.database-platform", "org.hibernate.dialect.MySQL5Dialect");
        jpaProperties.put("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        jpaProperties.put("hibernate.show-sql", true);
        factory.setJpaProperties(jpaProperties);
        return factory;
    }

}
