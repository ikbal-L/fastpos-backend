package com.softlines.fastpos.configuration;

import com.softlines.fastpos.dbconfig.configuration.CustomRoutingDataSource;
import com.softlines.fastpos.jwtsecurity.securitydomain.DbInfo;
import com.softlines.fastpos.jwtsecurity.securityrepository.DbInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.softlines.fastpos.repository",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
@EnableTransactionManagement
public class RoutingDatasourceTestProfileJPAConfig {

    @Autowired
    private DbInfoRepository dbInfoRepository;

    @Bean
    @Profile("test")
    public CustomRoutingDataSource testingCustomRoutingDataSource(){
        initDB();
        Map<Object, Object> map=new HashMap<>();
        List<DbInfo> dbInfos = dbInfoRepository.findAll();
        for (DbInfo dbInfo1 :
                dbInfos) {
            map.put(dbInfo1.getId(), createDataSources(dbInfo1));
        }
        CustomRoutingDataSource customRoutingDataSource=new CustomRoutingDataSource();
        customRoutingDataSource.setTargetDataSources(map);
        customRoutingDataSource.setDefaultTargetDataSource(createDataSources(dbInfos.get(0)));
        return customRoutingDataSource;
    }

    private void initDB() {
        DbInfo dbInfo = new DbInfo();

        dbInfo.setId(1l);
        dbInfo.setName("firstTestDB");
        dbInfo.setUrl("jdbc:mysql://localhost:3306/dbtesting1?createDatabaseIfNotExist=true");
        dbInfo.setUsername("root");
        dbInfo.setPassword("");
        dbInfoRepository.save(dbInfo);
        dbInfo.setId(2l);
        dbInfo.setName("secondTestDB");
        dbInfo.setUrl("jdbc:mysql://localhost:3306/dbtesting2?createDatabaseIfNotExist=true");
        dbInfo.setUsername("root");
        dbInfo.setPassword("");
        dbInfoRepository.save(dbInfo);
    }

    private DriverManagerDataSource createDataSources(DbInfo dbInfo) {
        DriverManagerDataSource dataSource= new DriverManagerDataSource();
        dataSource.setUsername(dbInfo.getUsername());
        dataSource.setPassword(dbInfo.getPassword());
        dataSource.setUrl(dbInfo.getUrl());
        return dataSource;
    }

    @Bean
    @Profile("test")
    public PlatformTransactionManager transactionManager()
    {
        EntityManagerFactory factory = entityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }

    @Bean
    @Profile("test")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(testingCustomRoutingDataSource());
        factory.setPackagesToScan(new String[]{"com.softlines.fastpos.domain"});
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", "create-drop");
        jpaProperties.put("hibernate.show-sql", true);
        jpaProperties.put("spring.jpa.database-platform", "org.hibernate.dialect.MySQL5Dialect");
        jpaProperties.put("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        factory.setJpaProperties(jpaProperties);
        return factory;
    }

}
