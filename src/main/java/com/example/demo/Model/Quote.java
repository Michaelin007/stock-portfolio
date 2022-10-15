package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor  
@AllArgsConstructor  
@ToString
public class Quote {
	private String companyName;
	private float latestPrice;
	private String symbol;

}
