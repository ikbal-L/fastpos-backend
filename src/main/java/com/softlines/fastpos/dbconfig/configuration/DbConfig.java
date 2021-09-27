package com.softlines.fastpos.dbconfig.configuration;

import com.softlines.fastpos.jwtsecurity.securityconfiguration.AuditorAwareImpl;
import com.softlines.fastpos.jwtsecurity.securitydomain.*;
import com.softlines.fastpos.jwtsecurity.securityrepository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Configuration
@Profile("prod")
@EnableJpaRepositories(
        basePackages = "com.softlines.fastpos.repository",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class DbConfig {

    @Autowired
    private Environment env;

    @Autowired
    private JWTuserRepository jwTuserRepository;

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

    @Value("${com.softlines.fastpos.jwtsecurity.db.name}")
    private String dbName;

    @Value("${com.softlines.fastpos.jwtsecurity.db.port}")
    private String dbPort;

    public DriverManagerDataSource createDataSources(DbInfo dbInfo) throws Exception {
        try {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setUsername(dbInfo.getUsername());
            dataSource.setPassword(dbInfo.getPassword());
            dataSource.setUrl(dbInfo.getUrl());
            return dataSource;
        } catch (Exception e) {
            throw new Exception("DB not Found Exception: " + e);
        }
    }

    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    @Bean
    @Profile("prod")
    public CustomRoutingDataSource customRoutingDataSource() throws Exception {
        var roles = initRolesAndPrivileges();
        initiateUserDB(roles);

        try {
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
        } catch (Exception e) {
            throw new Exception("DB not Found Exception: " + e);
        }
    }

    @Bean
    @Profile("prod")
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
    @Profile("prod")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception {
        try {
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
        } catch (Exception e) {
            throw new Exception("DB not Found Exception: " + e);
        }
    }

    public void initiateUserDB(List<Role> roles) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        DbInfo dbInfo = new DbInfo();
        dbInfo.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dbInfo.setUrl("jdbc:mysql://localhost:$port/$db_name?createDatabaseIfNotExist=true&useUnicode=yes&characterEncoding=UTF-8"
                .replace("$db_name",dbName)
                .replace("$port",dbPort));


        dbInfo.setName("defaultDB");
        dbInfo.setUsername("root");
        dbInfo.setPassword("");

        DbInfo dbInfo2 = new DbInfo();
        dbInfo2.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dbInfo2.setUrl("jdbc:mysql://localhost:$port/$db_name2?createDatabaseIfNotExist=true&useUnicode=yes&characterEncoding=UTF-8"
                .replace("$db_name",dbName)
                .replace("$port",dbPort));


        dbInfo2.setName("firstDB");
        dbInfo2.setUsername("root");
        dbInfo2.setPassword("");

        DbInfo dbInfo3 = new DbInfo();
        dbInfo3.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dbInfo3.setUrl("jdbc:mysql://localhost:$port/$db_name3?createDatabaseIfNotExist=true&useUnicode=yes&characterEncoding=UTF-8"
                .replace("$db_name",dbName)
                .replace("$port",dbPort));


        dbInfo3.setName("secondDB");
        dbInfo3.setUsername("root");
        dbInfo3.setPassword("");

        var db1 = createIfNotFound(dbInfo);
        var db2 = createIfNotFound(dbInfo2);
        var db3 = createIfNotFound(dbInfo3);

        if (db1 != null && db2 != null && db3 != null) {
            var createdDbInfo1 = dbInfoRepository.save(db1);
            var createdDbInfo2 = dbInfoRepository.save(db2);
            var createdDbInfo3 = dbInfoRepository.save(db3);

            Annex annex1 = createAnnexIfNotFound("Annex1",createdDbInfo1);
            Annex annex2 = createAnnexIfNotFound("Annex2",createdDbInfo2);
            Annex annex3 = createAnnexIfNotFound("Annex3",createdDbInfo3);


            createTerminalIfNotFound(annex1);
            createTerminalIfNotFound(annex2);
            createTerminalIfNotFound(annex3);
        }


        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        JWTuser admin = new JWTuser();
        admin.setUsername("admin");
        admin.setFirstName("Test");
        admin.setLastName("Test");
        admin.setPassword(encoder.encode("admin"));
        admin.setEmail("admin@test.com");
        admin.setRoles(roles);
        admin.setEnabled(true);
        admin.setCreatedDate(LocalDateTime.now());
        admin.setModifiedDate(LocalDateTime.now());
        admin.setCreationSessionId(UUID.randomUUID().toString());
        admin.setModificationSessionId(UUID.randomUUID().toString());
        admin.setBackgroundString("");
        dbInfo = dbInfoRepository.findByName("defaultDB");
        if (jwTuserRepository.findByUsername("admin") == null){
            jwTuserRepository.save(admin);
        }


//        Role userRole = roleRepository.findByName("ROLE_USER");
//        JWTuser user = new JWTuser();
//        user.setUsername("user");
//        user.setFirstName("Test");
//        user.setLastName("Test");
//        user.setPassword(encoder.encode("user"));
//        user.setEmail("user@test.com");
//        user.setRoles(Arrays.asList(userRole));
//        user.setEnabled(true);
//
//        user.setCreatedDate(LocalDateTime.now());
//        user.setModifiedDate(LocalDateTime.now());
//        user.setCreationSessionId(UUID.randomUUID().toString());
//        user.setModificationSessionId(UUID.randomUUID().toString());
//        user.setBackgroundString("");
//
//        dbInfo2 = dbInfoRepository.findByName("firstDB");
//        if (jwTuserRepository.findByUsername("user") == null){
//            jwTuserRepository.save(user);
//        }


    }

    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            return privilegeRepository.save(privilege);
        }
        return privilege;
    }

    Annex createAnnexIfNotFound(String name, DbInfo dbInfo) {
        var annex = annexRepository.findByName(name);
        if (annex == null) {
            annex = Annex.builder()
                    .name(name)
                    .address("Address of " + name)
                    .serverLicenceKey(UUID.randomUUID().toString())
                    .dbInfo(dbInfo)
                    .build();
            return annexRepository.save(annex);
        }

        return annex;
    }
    Terminal createTerminalIfNotFound(Annex annex){

//        var terminal = terminalRepository.findByAnnex(annex);
//        if (terminal == null){
            var terminal = Terminal.builder().active(true).licenceKey(UUID.randomUUID().toString()).annex(annex).build();
            return terminalRepository.save(terminal);
//        }
//        return terminal;
    }

    Role createRoleIfNotFound(String name, List<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            return roleRepository.save(role);
        }
        return role;
    }

    private DbInfo createIfNotFound(DbInfo dbInfo) {
        DbInfo hDbInfo = dbInfoRepository.findByName(dbInfo.getName());
        if (hDbInfo == null) {
            return dbInfo;
        }
        return null;
    }

    private List<Role> initRolesAndPrivileges() {
        String[] entities = {"Product", "Additive", "Category", "Customer", "Order", "OrderItem", "Deliveryman", "Waiter"};
        List<Privilege> privileges = new ArrayList<>();
        for (String s : entities) {

            var createTypePrivilege = createPrivilegeIfNotFound("Create_" + s);
            var readTypePrivilege = createPrivilegeIfNotFound("Read_" + s);
            var updateTypePrivilege = createPrivilegeIfNotFound("Update_" + s);
            var deleteTypePrivilege = createPrivilegeIfNotFound("Delete_" + s);

            if (createTypePrivilege != null) privileges.add(createTypePrivilege);
            if (readTypePrivilege != null) privileges.add(readTypePrivilege);
            if (updateTypePrivilege != null) privileges.add(updateTypePrivilege);
            if (deleteTypePrivilege != null) privileges.add(deleteTypePrivilege);
        }
        List<Role> roles = new ArrayList<>();
        Role admin = createRoleIfNotFound("ROLE_ADMIN", privileges);
        var orderPrivilegesCreateUpdate = privileges.stream().filter(p -> p.getName() == "Create_Order" || p.getName() == "Update_Order").collect(Collectors.toList());

        Role clerk = createRoleIfNotFound("ROLE_CLERK", orderPrivilegesCreateUpdate);
        roles.add(admin);
        roles.add(clerk);

        return roles;
    }


}
