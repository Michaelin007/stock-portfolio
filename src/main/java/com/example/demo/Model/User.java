package com.example.demo.Model;
import javax.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.Set;

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
    private Integer cash;
    
  
    
   


}
