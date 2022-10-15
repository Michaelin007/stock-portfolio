package com.example.demo.Controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.*;
import com.example.demo.Model.Quote;
import com.example.demo.Model.Shares;
import com.example.demo.Model.User;
import com.example.demo.Repository.ShareRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.CustomUserDetails;

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
	public String home(Principal user,Model model) {

		//model.addAttribute("user", new User());
		
		//String username = c.getUsername();
		//User user = userRepository.findByUsername(username);
		// model.addAttribute("share", share);
		//String cash = user.getName();
		model.addAttribute("user", user);

		return "index";
	}

	@PostMapping("/process_register")
	public String processRegister(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		user.setCash(10000);

		userRepository.save(user);

		return"redirect:/";
	}

	@PostMapping("/buy")
	public String buyStock(@AuthenticationPrincipal CustomUserDetails c, HttpServletRequest request, Model model) {
		String username = c.getUsername();
		User user = userRepository.findByUsername(username);
		Shares share = new Shares();
		String symbol = request.getParameter("symbol");
		String up = lookup.getProductAsJson(symbol);
		String shares = request.getParameter("shares");
		int shares_int = Integer.parseInt(shares);
		// using json to get specific json object
		JSONObject obj = new JSONObject(up);
		int price = obj.getInt("latestPrice");
		float transaction = shares_int * price;

		int new_cash = (int) (user.getCash() - transaction);
		user.setUsername(user.getUsername());
		user.setPassword(user.getPassword());
		user.setCash(new_cash);

		userRepository.save(user);

		String d_symbol = obj.getString("symbol");

		share.setPrice(price);
		share.setSymbol(d_symbol);
		share.setShares(shares_int);
		share.setDate(new Date());

		shareRepository.save(share);

		return "";
	}

	@PostMapping("/sell")
	public String sellStock(@AuthenticationPrincipal CustomUserDetails c, HttpServletRequest request, Model model) {
		String username = c.getUsername();
		User user = userRepository.findByUsername(username);
		Shares share = new Shares();

		String symbol = request.getParameter("symbol");
		String u_symbol = symbol.toUpperCase();
		String shares = request.getParameter("shares");
		int shares_int = Integer.parseInt(shares);
		String up = lookup.getProductAsJson(u_symbol);
		JSONObject obj = new JSONObject(up);
		int price = obj.getInt("latestPrice");
		float transaction = shares_int * price;

		int new_cash = (int) (user.getCash() + transaction);
		user.setUsername(user.getUsername());
		user.setPassword(user.getPassword());
		user.setCash(new_cash);

		userRepository.save(user);

		String d_symbol = obj.getString("symbol");

		share.setPrice(price);
		share.setSymbol(d_symbol);
		share.setShares(-1 * shares_int);
		share.setDate(new Date());

		shareRepository.save(share);

		return "";
	}

	@GetMapping("/quote")
	public String getQuote(HttpServletRequest request, Model model) {

		String symbol = request.getParameter("symbol");
		String u_symbol = symbol.toUpperCase();
		String up = lookup.getProductAsJson(u_symbol);
		JSONObject obj = new JSONObject(up);
		int price = obj.getInt("latestPrice");
		String sysm = obj.getString("symbol");

		model.addAttribute("price", price);
		model.addAttribute("sysm", sysm);

		return "";
	}

	@GetMapping("/history")
	public String getHistory(Model model, @AuthenticationPrincipal CustomUserDetails c) {
		String username = c.getUsername();
		User user = userRepository.findByUsername(username);

		// List a = user.getPassword();

		// List<User> share=(List<Shares>) shareRepository.findAll();
		// model.addAttribute("share", share);

		return "";
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
