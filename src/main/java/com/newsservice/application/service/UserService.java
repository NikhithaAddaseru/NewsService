package com.newsservice.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.newsservice.application.entity.User;
import com.newsservice.application.repository.NewsCrudRepo;
import com.newsservice.application.repository.ReadStatusCrudRepo;
import com.newsservice.application.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	NewsCrudRepo newsRepo;
	
	@Autowired
	private ReadStatusCrudRepo readRepo;
	
	private final PasswordEncoder passwordEncoder; // Change this

	    // And change the constructor parameter
	   @Autowired
	   public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
	       this.userRepo = userRepository;
	       this.passwordEncoder = passwordEncoder; // And this
	   }
	
	public User save(User user) {
		User resp = new User();
		try {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		resp = userRepo.save(user);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			List<Integer> newsId = newsRepo.getPublishedNewsIds();
			newsId.stream().forEach(x -> {
				readRepo.insertEntriesInStatus(user.getUsername(), x);
			});
		}
		catch(Exception e) {
			e.printStackTrace(); 
		}
		return resp;
	}
}
