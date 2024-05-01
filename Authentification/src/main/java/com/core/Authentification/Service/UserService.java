package com.core.Authentification.Service;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.core.Authentification.Entities.User;
import com.core.Authentification.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

	@Autowired
	UserRepository  userRepo ;
	public void updateResetPasswordToken(String token, String email) throws AccountNotFoundException {
        User customer = userRepo.findEmail(email);
        if (customer != null) {
            customer.setResetPasswordToken(token);
            userRepo.save(customer);
        } else {
            throw new AccountNotFoundException("Could not find any customer with the email " + email);
        }
    }
	
	
	 public User getByResetPasswordToken(String token) {
	        return userRepo.findByResetPasswordToken(token);
	    }
	     
	    public void updatePassword(User customer, String newPassword) {
	        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        String encodedPassword = passwordEncoder.encode(newPassword);
	        customer.setPassword(encodedPassword);
	         
	        customer.setResetPasswordToken(null);
	        userRepo.save(customer);
	    }
}
