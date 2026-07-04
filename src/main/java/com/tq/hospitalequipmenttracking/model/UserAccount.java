package com.tq.hospitalequipmenttracking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

//    @OneToOne(cascade = CascadeType.ALL): delete account not equal delete person
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="person_id")
    private Person person;

    @Column(nullable=false)
    private boolean enabled=true;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public Person getPerson() {
        return person;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
