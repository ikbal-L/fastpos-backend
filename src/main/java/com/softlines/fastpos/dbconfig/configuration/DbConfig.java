package com.softlines.fastpos.dbconfig.configuration;

import com.softlines.fastpos.security.securityconfiguration.AuditorAwareImpl;
import com.softlines.fastpos.security.securitydomain.*;
import com.softlines.fastpos.security.securityrepository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import jakarta.persistence.EntityManagerFactory;
import org.sqlite.JDBC;
import org.sqlite.SQLiteConfig;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Configuration
@EnableJpaRepositories(
        basePackages = "com.softlines.fastpos.repository",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Slf4j
public class DbConfig {

    @Autowired
    private Environment env;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DbInfoRepository dbInfoRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private AnnexRepository annexRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    @Value("${com.softlines.fastpos.security.db.name}")
    private String dbName;

//    @Value("${com.softlines.fastpos.security.db.port}")
//    private String dbPort;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName ;


    @Value("${com.softlines.fastpos.security.url}")
    private String dbrul ;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties authDataSourceProperties() {
        return new DataSourceProperties();
    }





    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }


    public CustomRoutingDataSource customRoutingDataSource() throws Exception {


        try {
            List<DbInfo> dbInfos = dbInfoRepository.findAll();
            Map<Object, Object> map = new HashMap<>();
//            for (DbInfo dbInfo1 :
//                    dbInfos) {
//                map.put(dbInfo1.getId(), createDataSources(dbInfo1));
//            }
            CustomRoutingDataSource customRoutingDataSource = new CustomRoutingDataSource();
            customRoutingDataSource.setTargetDataSources(map);

            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setUrl(dbrul.replace("securitydb","db1"));

            SQLiteConfig config = new SQLiteConfig();
            config.setReadOnly(false);
            dataSource.setConnectionProperties(config.toProperties());

            customRoutingDataSource.setDefaultTargetDataSource(dataSource);

            return customRoutingDataSource;
        } catch (Exception e) {
            throw new Exception("DB not Found Exception: " + e);
        }
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        try {
            EntityManagerFactory factory = entityManagerFactory().getObject();
            return new JpaTransactionManager(factory);
        } catch (Exception e) {
            throw new Exception("DB not Found Exception: " + e);
        }
    }

    @Bean
    @Primary

    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception {
        try {
            LocalContainerEntityManagerFactoryBean factory =
                    new LocalContainerEntityManagerFactoryBean();
//            factory.setDataSource(customRoutingDataSource());

            SQLiteConfig config = new SQLiteConfig();
            config.setReadOnly(false);
            factory.setDataSource(new SimpleDriverDataSource(new JDBC(),"jdbc:sqlite:db1.sqlite",config.toProperties()));

            factory.setPackagesToScan("com.softlines.fastpos.domain");
            factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
            Properties jpaProperties = new Properties();
            jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
            jpaProperties.put("hibernate.show-sql", env.getProperty("hibernate.show-sql"));



            factory.setJpaProperties(jpaProperties);
            return factory;
        } catch (Exception e) {
            throw new Exception("DB not Found Exception: " + e.getMessage());
        }
    }




}
