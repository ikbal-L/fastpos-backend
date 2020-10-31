package com.softlines.fastpos.dbconfig.configuration;

import com.softlines.fastpos.jwtsecurity.securitydomain.DbInfo;
import com.softlines.fastpos.jwtsecurity.securitydomain.JWTuser;
import com.softlines.fastpos.jwtsecurity.securitydomain.Privilege;
import com.softlines.fastpos.jwtsecurity.securitydomain.Role;
import com.softlines.fastpos.jwtsecurity.securityrepository.DbInfoRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.JWTuserRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.PrivilegeRepository;
import com.softlines.fastpos.jwtsecurity.securityrepository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.util.*;


@Configuration
@EnableJpaRepositories(
        basePackages = "com.softlines.fastpos.repository",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class DbConfig {

    @Autowired
    private Environment env;

    @Autowired
    private JWTuserRepository jwTuserRepository;

    @Autowired
    private DbInfoRepository dbInfoRepository;

    private List<DbInfo> dbInfo = new ArrayList<>();

    public DbConfig(JWTuserRepository jwTuserRepository) throws Exception {
        try{
            this.jwTuserRepository = jwTuserRepository;
            for (JWTuser jwTuser :
                    this.jwTuserRepository.findAll()) {
                dbInfo.add(jwTuser.getDbInfo());
            }
        }catch (Exception e){
            throw new Exception("DB not Found Exception: "+ e);
        }
    }

    public DriverManagerDataSource createDataSources(DbInfo dbInfo) throws Exception {
        try{
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setUsername(dbInfo.getUsername());
            dataSource.setPassword(dbInfo.getPassword());
            dataSource.setUrl(dbInfo.getUrl());
            return dataSource;
        }catch (Exception e){
            throw new Exception("DB not Found Exception: "+ e);
        }
    }


    @Bean
    public CustomRoutingDataSource customRoutingDataSource() throws Exception {
        //initiateDB();
        try{
            List<DbInfo> dbInfos = dbInfoRepository.findAll();
            Map<Object, Object> map = new HashMap<>();
            for (DbInfo dbInfo1 :
                    dbInfos) {
                map.put(dbInfo1.getId(), createDataSources(dbInfo1));
            }
            CustomRoutingDataSource customRoutingDataSource = new CustomRoutingDataSource();
            customRoutingDataSource.setTargetDataSources(map);
            customRoutingDataSource.setDefaultTargetDataSource(createDataSources(dbInfos.get(0)));
            return customRoutingDataSource;
        }catch (Exception e){
            throw new Exception("DB not Found Exception: "+ e);
        }
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        try{
            EntityManagerFactory factory = entityManagerFactory().getObject();
            return new JpaTransactionManager(factory);
        }catch (Exception e){
            throw new Exception("DB not Found Exception: "+ e);
        }
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception {
        try{
            LocalContainerEntityManagerFactoryBean factory =
                    new LocalContainerEntityManagerFactoryBean();
            factory.setDataSource(customRoutingDataSource());
            factory.setPackagesToScan("com.softlines.fastpos.domain");
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

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    public void initiateDB(){
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        DbInfo dbInfo = new DbInfo();
        dbInfo.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dbInfo.setUrl("jdbc:mysql://localhost:3306/jwtauthsoftlines?createDatabaseIfNotExist=true");
        dbInfo.setName("defaultDB");
        dbInfo.setUsername("root");
        dbInfo.setPassword("");

        DbInfo dbInfo2 = new DbInfo();
        dbInfo2.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dbInfo2.setUrl("jdbc:mysql://localhost:3306/jwtauthsoftlines2?createDatabaseIfNotExist=true");
        dbInfo2.setName("firstDB");
        dbInfo2.setUsername("root");
        dbInfo2.setPassword("");

        DbInfo dbInfo3 = new DbInfo();
        dbInfo3.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dbInfo3.setUrl("jdbc:mysql://localhost:3306/jwtauthsoftlines3?createDatabaseIfNotExist=true");
        dbInfo3.setName("secondDB");
        dbInfo3.setUsername("root");
        dbInfo3.setPassword("");

        dbInfoRepository.save(createIfNotFound(dbInfo));
        dbInfoRepository.save(createIfNotFound(dbInfo2));
        dbInfoRepository.save(createIfNotFound(dbInfo3));

        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        JWTuser admin = new JWTuser();
        admin.setUsername("admin");
        admin.setFirstName("Test");
        admin.setLastName("Test");
        admin.setPassword(encoder.encode("admin"));
        admin.setEmail("admin@test.com");
        admin.setRoles(Arrays.asList(adminRole));
        admin.setEnabled(true);

        dbInfo = dbInfoRepository.findByName("defaultDB");
        admin.setDbInfo(dbInfo);
        jwTuserRepository.save(admin);

        Role userRole = roleRepository.findByName("ROLE_USER");
        JWTuser user = new JWTuser();
        user.setUsername("user");
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword(encoder.encode("user"));
        user.setEmail("user@test.com");
        user.setRoles(Arrays.asList(userRole));
        user.setEnabled(true);
        dbInfo2 = dbInfoRepository.findByName("firstDB");
        user.setDbInfo(dbInfo2);
        jwTuserRepository.save(user);
    }

    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

    private DbInfo createIfNotFound(DbInfo dbInfo) {
        DbInfo hDbInfo = dbInfoRepository.findByName(dbInfo.getName());
        if(hDbInfo == null){
            return dbInfo;
        }
        return null;
    }

}
