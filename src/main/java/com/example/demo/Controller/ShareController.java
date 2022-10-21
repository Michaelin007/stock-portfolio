package com.example.demo.Controller;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.Model.Shares;
import com.example.demo.Model.User;
import com.example.demo.Repository.ShareRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.CustomUserDetails;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
public class ShareController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ShareRepository shareRepository;

	Restemplate lookup = new Restemplate();

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {

		model.addAttribute("user", new User());

		return "register";
	}
	
	@GetMapping("/login")
	public String showloginForm(Model model) {

		

		return "login";
	}


	@GetMapping("/")
	public String home(Model model , Principal principal)  {

// user = (User<Plug>v
  User user = userRepository.findByUsername(principal.getName());
		String username= principal.getName();

		List<Shares> share = (List<Shares>) shareRepository.findAll().stream()
				.filter(shares -> username.equals(shares.getUser().getUsername()))
				.collect(Collectors.toList());

		model.addAttribute("share", share);
		model.addAttribute("user", user);
    	System.out.println(share);



		return "index";
	}
	@GetMapping("/buy")
	public String buy(Model model) {
		model.addAttribute("user","");
		model.addAttribute("share","");


		return "buy";
	}

	@PostMapping("/process_register")
	public String processRegister(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		user.setCash(10000);

		userRepository.save(user);

		return"redirect:/login";
	}

	@PostMapping("/buy")
	public String buyStock(@ModelAttribute("user") User newUser, Principal principal, HttpServletRequest request, Model model) {
		User user = userRepository.findByUsername(principal.getName());

		//Optional<Shares> share = shareRepository.findById(user.getId());
		Shares share = new Shares();
		//Shares share = user.getShares();

		String symbol = request.getParameter("symbol");
		String u_symbol = symbol.toUpperCase();
		ResponseEntity <String> up = lookup.getAsJson(u_symbol);
		JsonParser parser = new JsonParser();
		JsonObject obj1 = parser.parse(up.getBody()).getAsJsonObject();
		String shares = request.getParameter("shares");
		int shares_int = Integer.parseInt(shares);
		// using json to get specific json object
    

		double price = obj1.get("latestPrice").getAsDouble();
		double transaction = shares_int * price;
		String sysm = obj1.get("symbol").getAsString();
		String companyName= obj1.get("companyName").getAsString();


		int new_cash = (int) (user.getCash() - transaction);
		user.setUsername(user.getUsername());
		user.setPassword(user.getPassword());
		user.setCash(new_cash);
		user.setShares(user.getShares());


		//String d_symbol = obj.getString("symbol");



		share.setPrice( price);
		share.setSymbol(sysm);
		share.setShares(shares_int);
		share.setDate(new Date());
		share.setTotal(transaction);
		share.setName(companyName);
		share.setUser(user);




		shareRepository.save(share);
		userRepository.save(user);

		model.addAttribute("user", user);
		model.addAttribute("share", share);
		model.addAttribute("price", price);



		return "redirect:/?success";
	}
	@GetMapping("/sell")
	public String sell(Model model, Principal principal) {
		String username= principal.getName();

		List<Shares> share = (List<Shares>) shareRepository.findAll().stream()
						.filter(shares -> username.equals(shares.getUser().getUsername()))
								.collect(Collectors.toList());

		model.addAttribute("share", share);


		return "sell";
	}

	@PostMapping("/sell")
	public String sellStock(Principal principal, HttpServletRequest request, Model model) {
		User user = userRepository.findByUsername(principal.getName());
		Shares share = shareRepository.findByUser(user);
		String symbol = request.getParameter("symbol");
		String u_symbol = symbol.toUpperCase();
		String shares = request.getParameter("shares");
		double shares_int = Double.parseDouble(shares);
		ResponseEntity <String> up = lookup.getAsJson(u_symbol);
		JsonParser parser = new JsonParser();
		JsonObject obj1 = parser.parse(up.getBody()).getAsJsonObject();
		double price = obj1.get("latestPrice").getAsDouble();
		String sysm = obj1.get("symbol").getAsString();
		double transaction = shares_int * price;

	String companyName= obj1.get("companyName").getAsString();
		double new_cash = (user.getCash() + transaction);
		user.setUsername(user.getUsername());
		user.setPassword(user.getPassword());
		user.setCash(new_cash);
		user.setShares(user.getShares());

		share.setPrice(price);
		share.setSymbol(sysm);
		share.setShares(share.getShares() - shares_int);
		share.setDate(new Date());
		share.setUser(user);
	//	share.setUser(user.);

	share.setName(companyName);
		share.setTotal(share.getTotal() - transaction);
		shareRepository.save(share);
		userRepository.save(user);


		return "redirect:/?success";
	}

	@GetMapping("/quote")
	public String getQuote(HttpServletRequest request, Model model) {

		
		return "quote";
	}
	
	@GetMapping("/quoted")
	public String getQuoted(HttpServletRequest request, Model model) {
		
		return "quoted";
	}

	@PostMapping(value="/quote")
	public String pQuote(HttpServletRequest request, Model model) {

		String symbol = request.getParameter("symbol");
		String u_symbol = symbol.toUpperCase();
		ResponseEntity<String> up = lookup.getAsJson(u_symbol);

		 JsonParser parser = new JsonParser();
	    JsonObject obj1 = parser.parse(up.getBody()).getAsJsonObject();

		String name= obj1.get("companyName").getAsString();
		double price = obj1.get("latestPrice").getAsDouble();
		String sysm = obj1.get("symbol").getAsString();

		model.addAttribute("price", price);
		model.addAttribute("sysm", sysm);
		model.addAttribute("name", name);

		return "quoted";
	}

	@GetMapping("/history")
	public String getHistory(Model model, Principal principal) {


		String username= principal.getName();

		List<Shares> share = (List<Shares>) shareRepository.findAll().stream()
				.filter(shares -> username.equals(shares.getUser().getUsername()))
				.collect(Collectors.toList());

		model.addAttribute("share", share);






		return "history";
	}

	@GetMapping("/home/")
	public String getUser(@AuthenticationPrincipal CustomUserDetails c, Model model) {

		String username = c.getUsername();
		User user = userRepository.findByUsername(username);
		// model.addAttribute("share", share);
		// model.addAttribute("user", user);

		return "";
	}

}
