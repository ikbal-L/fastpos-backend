package com.softlines.fastpos.dbconfig.configuration;
import com.softlines.fastpos.security.securitydomain.*;
import com.softlines.fastpos.security.securityrepository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@EnableJpaRepositories(
        basePackages = "com.softlines.fastpos.security.securityrepository",
        entityManagerFactoryRef = "authEntityManagerFactory",
        transactionManagerRef = "authTransactionManager"
)
@Configuration
@Slf4j
public class DatabaseInitializer {

    private  DbInfoRepository dbInfoRepository;
    private  RoleRepository roleRepository;
    private  PrivilegeRepository privilegeRepository;
    private  UserRepository userRepository;
    private AnnexRepository annexRepository;
    private TerminalRepository terminalRepository;


    @Bean
    public ApplicationRunner initDatabase(DbInfoRepository dbInfoRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository, UserRepository userRepository, AnnexRepository annexRepository, TerminalRepository terminalRepository) {
        this.dbInfoRepository = dbInfoRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.userRepository = userRepository;
        this.annexRepository = annexRepository;
        this.terminalRepository = terminalRepository;
        return args -> {
            initRolesAndPrivileges();
            initiateUserDB();
        };
    }


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




    @Transactional()
    public void initiateUserDB() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        DataSourceProperties dsp = new  DataSourceProperties();
        dsp.setUsername("root");
        dsp.setPassword("");
        var driverClassName = "org.sqlite.JDBC";
        var dbrul = "jdbc:sqlite:db$0.sqlite";

        DbInfo dbInfo = new DbInfo();
        DbInfo dbInfo2 = new DbInfo();
        DbInfo dbInfo3 = new DbInfo();

        dbInfo.setUsername(dsp.getUsername());
        dbInfo.setPassword(dsp.getPassword());

        dbInfo2.setUsername(dsp.getUsername());
        dbInfo2.setPassword(dsp.getPassword());

        dbInfo3.setUsername(dsp.getUsername());
        dbInfo3.setPassword(dsp.getPassword());

        dbInfo.setDriverClassName(driverClassName);
        dbInfo.setUrl(dbrul.replace("$0","1"));


        dbInfo.setName("db1");



        dbInfo2.setDriverClassName(driverClassName);
        dbInfo2.setUrl(dbrul.replace("$0","2"));


        dbInfo2.setName("db2");


        dbInfo3.setDriverClassName(driverClassName);
        dbInfo3.setUrl(dbrul.replace("$0","3"));


        dbInfo3.setName("db3");

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
        User admin = new User();
        admin.setUsername("admin");
        admin.setFirstName("John");
        admin.setLastName("Doe");
        admin.setPassword(encoder.encode("123123"));
        admin.setEmail("admin@admin.com");
        admin.setRoles(List.of(adminRole));
        admin.setEnabled(true);
        admin.setCreatedDate(LocalDateTime.now());
        admin.setModifiedDate(LocalDateTime.now());
        admin.setCreationSessionId(UUID.randomUUID().toString());
        admin.setModificationSessionId(UUID.randomUUID().toString());
        admin.setBackgroundString("");

        if (userRepository.findByUsername("admin") == null){
            userRepository.save(admin);
        }
    }

    Privilege createCrudPrivilegeIfNotFound(String crudPrivilege, String entityType) {

        var name = crudPrivilege+"_"+entityType;
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            log.info("Creating Privilege {} for Entity {}",crudPrivilege,entityType);
            privilege = new Privilege();
            privilege.setName(name);
            return privilegeRepository.save(privilege);
        }
        return privilege;
    }

    Privilege createPrivilegeIfNotFound(String privilegeName) {


        Privilege privilege = privilegeRepository.findByName(privilegeName);
        if (privilege == null) {
            log.info("Creating Privilege {} ",privilegeName);
            privilege = new Privilege();
            privilege.setName(privilegeName);
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

    Role createRoleIfNotFound(String name, List<Privilege> privileges ,boolean isPredefined) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            log.info("Creating Role {}",name);
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            role.setPredefined(isPredefined);
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
        String[] entities = {"Product", "Additive", "Category", "Customer", "Order", "OrderItem", "Deliveryman", "Waiter","DailyEarningsReport","Role","User","Payment_Client","Payment_Deliveryman"};
        String[] miscPrivileges = {"Modify_Global_Settings","Modify_Local_Settings","Refund_Order"};
        List<Privilege> privileges = new ArrayList<>();


        for (String s : entities) {

            var createTypePrivilege = createCrudPrivilegeIfNotFound("Create",s);
            var readTypePrivilege = createCrudPrivilegeIfNotFound("Read" , s);
            var updateTypePrivilege = createCrudPrivilegeIfNotFound("Update" , s);
            var deleteTypePrivilege = createCrudPrivilegeIfNotFound("Delete" , s);

            if (createTypePrivilege != null) privileges.add(createTypePrivilege);
            if (readTypePrivilege != null) privileges.add(readTypePrivilege);
            if (updateTypePrivilege != null) privileges.add(updateTypePrivilege);
            if (deleteTypePrivilege != null) privileges.add(deleteTypePrivilege);
        }
        for (String miscPrivilege : miscPrivileges) {
            var privilege = createPrivilegeIfNotFound(miscPrivilege);
            if (privilege!= null) privileges.add(privilege);
        }

        List<Role> roles = new ArrayList<>();
        Role admin = createRoleIfNotFound("ROLE_ADMIN", privileges,true);

        var employeeManagerPrivileges = privileges.stream().filter(p->(p.getName().contains("Deliveryman")&& !p.getName().contains("Payment"))|| p.getName().contains("Waiter")).collect(Collectors.toList());
        var menuManagerPrivileges = privileges.stream().filter(p->p.getName().contains("Category")||p.getName().contains("Product")).collect(Collectors.toList());
        var clientCreditManagerPrivileges = privileges.stream().filter(p->p.getName().contains("Payment_Client")).collect(Collectors.toList());
        var deliveryManagerPrivileges = privileges.stream().filter(p->p.getName().contains("Payment_Deliveryman")).collect(Collectors.toList());
        var refundManagerPrivileges =privileges.stream().filter(p->p.getName().contains("Refund_Order")).collect(Collectors.toList());
        var globalSettingsManagerPrivileges =privileges.stream().filter(p->p.getName().contains("Modify_Global_Settings")).collect(Collectors.toList());
        var localSettingsManagerPrivileges = privileges.stream().filter(p->p.getName().contains("Modify_Local_Settings")).collect(Collectors.toList());
        Role  employeeManager = createRoleIfNotFound("ROLE_GESTION_DELIVERYMAN_WAITER",employeeManagerPrivileges,true);
        Role  menuManager = createRoleIfNotFound("ROLE_GESTION_MENU",menuManagerPrivileges,true);
        Role clientCreditManager  = createRoleIfNotFound("ROLE_GESTION_CREDIT_CLIENT",clientCreditManagerPrivileges,true);
        Role deliveryManager  = createRoleIfNotFound("ROLE_GESTION_DELIVERY",deliveryManagerPrivileges,true);
        Role refundManager  = createRoleIfNotFound("ROLE_GESTION_REMBOURSEMENT",refundManagerPrivileges,true);
        Role globalSettingsManager = createRoleIfNotFound("ROLE_GESTION_PARAMETERES_GLOBAUX",globalSettingsManagerPrivileges,true);
        Role localSettingsManager = createRoleIfNotFound("ROLE_GESTION_PARAMETERES_LOCAUX",localSettingsManagerPrivileges,true);

        roles.add(admin);
        roles.add(employeeManager);
        roles.add(menuManager);
        roles.add(clientCreditManager);
        roles.add(deliveryManager);
        roles.add(refundManager);
        roles.add(globalSettingsManager);
        roles.add(localSettingsManager);

        return roles;
    }
}
