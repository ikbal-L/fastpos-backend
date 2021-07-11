package com.softlines.fastpos.dbconfig.configuration;

import com.softlines.fastpos.jwtsecurity.securityconfiguration.AuditorAwareImpl;
import com.softlines.fastpos.jwtsecurity.securitydomain.*;
import com.softlines.fastpos.jwtsecurity.securityrepository.*;
import org.springframework.beans.factory.annotation.Autowired;
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

        var db1 = createIfNotFound(dbInfo);
        var db2 = createIfNotFound(dbInfo2);
        var db3 = createIfNotFound(dbInfo3);

        if (db1 != null && db2 != null && db3 != null) {
            var createdDbInfo1 = dbInfoRepository.save(db1);
            var createdDbInfo2 = dbInfoRepository.save(db2);
            var createdDbInfo3 = dbInfoRepository.save(db3);

            Annex annex1 = Annex.builder()
                    .name("Annex1")
                    .address("Address1")
                    .serverLicenceKey(UUID.randomUUID().toString())
                    .dbInfo(createdDbInfo1)
                    .build();
            Annex annex2 = Annex.builder()
                    .name("Annex2")
                    .address("Address2")
                    .serverLicenceKey(UUID.randomUUID().toString())
                    .dbInfo(createdDbInfo2)
                    .build();
            Annex annex3 = Annex.builder()
                    .name("Annex2")
                    .address("Address2")
                    .serverLicenceKey(UUID.randomUUID().toString())
                    .dbInfo(createdDbInfo3)
                    .build();
            Annex createdAnnex1 = annexRepository.save(annex1);
            Annex createdAnnex2 = annexRepository.save(annex2);
            Annex createdAnnex3 = annexRepository.save(annex3);
            Terminal terminal1 = Terminal.builder().active(true).licenceKey(UUID.randomUUID().toString()).annex(createdAnnex1).build();
            Terminal terminal2 = Terminal.builder().active(true).licenceKey(UUID.randomUUID().toString()).annex(createdAnnex2).build();
            Terminal terminal3 = Terminal.builder().active(true).licenceKey(UUID.randomUUID().toString()).annex(createdAnnex3).build();
            terminalRepository.save(terminal1);
            terminalRepository.save(terminal2);
            terminalRepository.save(terminal3);
        }


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
        admin.setCreatedDate(LocalDateTime.now());
        admin.setModifiedDate(LocalDateTime.now());
        admin.setCreationSessionId(UUID.randomUUID().toString());
        admin.setModificationSessionId(UUID.randomUUID().toString());
        admin.setBackgroundString("");
        dbInfo = dbInfoRepository.findByName("defaultDB");
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

        user.setCreatedDate(LocalDateTime.now());
        user.setModifiedDate(LocalDateTime.now());
        user.setCreationSessionId(UUID.randomUUID().toString());
        user.setModificationSessionId(UUID.randomUUID().toString());
        user.setBackgroundString("");

        dbInfo2 = dbInfoRepository.findByName("firstDB");
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

    Role createRoleIfNotFound(String name, List<Privilege> privileges) {

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
        if (hDbInfo == null) {
            return dbInfo;
        }
        return null;
    }

    private List<Role> initRolesAndPrivileges() {
        String[] entities = {"Product", "Additive", "Category", "Customer", "Order", "OrderItem", "Deliveryman", "Waiter"};
        List<Privilege> privileges = new ArrayList<>();
        for (String s : entities) {
            privileges.add(Privilege.builder().name("Create_" + s).build());
            privileges.add(Privilege.builder().name("Read_" + s).build());
            privileges.add(Privilege.builder().name("Update_" + s).build());
            privileges.add(Privilege.builder().name("Delete_" + s).build());
        }
        var createdPrivileges = privilegeRepository.saveAll(privileges);
        List<Role> roles = new ArrayList<>();
        Role admin = Role.builder().name("ROLE_ADMIN").privileges(createdPrivileges).build();
        var privilegeStream = createdPrivileges.stream().filter(p -> p.getName() == "Create_Order" || p.getName() == "Update_Order");
        var orderPrivilegesCreateUpdate = privilegeStream.collect(Collectors.toList());
        Role clerk = Role.builder().name("ROLE_CLERK").privileges(orderPrivilegesCreateUpdate).build();
        roles.add(admin);
        roles.add(clerk);
        var savedRoles = roleRepository.saveAll(roles);

return savedRoles;
    }


}
