package com.softlines.fastpos.jwtsecurity.securitydomain;


import javax.persistence.*;

@Entity
@Table(name = "users")
public class JWTuser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "pin_code")
    private String pinCode;

    public JWTuser() { }

    public JWTuser(long id, String username, String password, String pinCode) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.pinCode = pinCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
