package com.example.demo.Model;
import javax.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor  
@AllArgsConstructor  
@ToString
@Table(name="userdb")
public class User {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    
    @Column(nullable = false, length = 64)
    private String username;
    @Column(nullable = false, length = 64)
    private String password;
    private double cash;
    @OneToMany(mappedBy = "shares", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Shares> shares;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public List<Shares> getShares() {
        return shares;
    }

    public void setShares(List<Shares> shares) {
        this.shares = shares;
    }
}
