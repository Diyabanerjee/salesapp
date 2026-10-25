package com.salesflow.SalesFlowApplication.user;

import jakarta.persistence.*;

@Entity
@Table(name="sales_users")
public class SalesUser {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false,length=100)
    private String name;

    @Column(nullable=false,unique=true,length=255)
    private String email;

    protected SalesUser(){

    }

    public SalesUser(String name,String email){
        this.name=name;
        this.email=email;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
