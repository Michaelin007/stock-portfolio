package com.example.demo.Controller;


import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Model.Quote;
import com.google.gson.Gson;

public class Restemplate {
    
    public List<Quote> getProductAsJson(String symbol) {
        RestTemplate restTemplate = new RestTemplate();
        String API_KEY="pk_52972563d60548fc952715462d4a3bd0";

        String resourceUrl
          = "https://cloud.iexapis.com/stable/stock/" + symbol + "/quote?token=" + API_KEY;

        // Fetch JSON response as String wrapped in ResponseEntity
       // ResponseEntity<String> response
      //    = restTemplate.getForEntity(resourceUrl, String.class);
        
      //  String productsJson = response.getBody();
        
     // Fetch response as List wrapped in ResponseEntity
        ResponseEntity<List> response
          = restTemplate.getForEntity(resourceUrl, List.class);
        
        List<Quote> quote = response.getBody();
        //converting string to return json with gson
       // String json = new Gson().toJson(quote );
        // return json;
        
        return quote;
        
        
    }

    public ResponseEntity<String> getAsJson(String symbol) {
        RestTemplate restTemplate = new RestTemplate();
        String API_KEY="pk_52972563d60548fc952715462d4a3bd0";

        String resourceUrl
          = "https://cloud.iexapis.com/stable/stock/" + symbol + "/quote?token=" + API_KEY;
        	HttpHeaders headers = new HttpHeaders();
        	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        	
        	HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        	ResponseEntity<String> result= restTemplate.exchange(resourceUrl, HttpMethod.GET,  entity, String.class);
        	
        	
        	
       
        return result;
        
    }
    
}