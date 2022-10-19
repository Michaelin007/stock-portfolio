package com.example.demo.Model;
import javax.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor  
@AllArgsConstructor  
@ToString
public class Shares {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String name;
    private String symbol;
    private Date date = new Date();
    private double shares;
    private double price;
    private double total;
    @ManyToOne(fetch =  FetchType.LAZY, optional = false)
    @JoinColumn(name = "share_id", nullable = true)
    private User user;
    
    

}
