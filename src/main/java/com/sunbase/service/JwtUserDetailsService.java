package com.sunbase.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sunbase.entity.Role;
import com.sunbase.entity.User;
import com.sunbase.repository.RoleRepository;
import com.sunbase.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	private static final Logger log = LogManager.getLogger(JwtUserDetailsService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;


	@Autowired
	private PasswordEncoder bcryptEncoder;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				true, true, true, true, new ArrayList<>());
	}
	
	public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
		User user = userRepository.findByUserEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				true, true, true, true, new ArrayList<>());
	}

	public User save(User user) {

		LocalDate timestamp = LocalDate.now();

		User newUser = new User();

		if(!isUserAlreadyPresent(user) && !isUserAlreadyPresentByEmail(user)) {

			Role userRole = roleRepository.findByRoleName("SITE_USER");
			newUser.setUsername(user.getUsername().trim());
			newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
			newUser.setRole(new HashSet<Role>(Arrays.asList(userRole)));			
			newUser.setEmail(user.getEmail().toLowerCase().trim());
                                                     newUser.setCREATED_DATE(timestamp);
                                                     newUser.setLAST_MODIFIED_DATE(timestamp);
                                                     
			return userRepository.save(newUser);
		}else {
			log.info(user.getUsername() + " already exists. ");
			return null;
		}

	}

	public boolean isUserAlreadyPresent(User user) {
		User rUser = userRepository.findByUsername(user.getUsername().trim());
		if(rUser == null) {
			return false;
		}else {
			return true;
		}
	}

	public boolean isUserAlreadyPresentByEmail(User user) {
		User rUser = userRepository.findByUserEmail(user.getEmail().toLowerCase().trim());
		if(rUser == null) {
			return false;
		}else {
			return true;
		}
	}

	public User getRegisterUser(String username) {
		return userRepository.findByUsername(username); 
	}
	
	
	public Optional<User> findById(long userId) {
		return userRepository.findById(userId);
	}
	
	
}