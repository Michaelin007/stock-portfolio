package com.example.demo.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Model.Quote;
import com.google.gson.Gson;

public class Restemplate {
    
    public String getProductAsJson(String symbol) {
        RestTemplate restTemplate = new RestTemplate();
        String API_KEY="";

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
        String json = new Gson().toJson(quote );
        return json;
        
        
    }
    
}