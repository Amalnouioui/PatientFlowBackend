package com.core.Authentification.auth;

import com.core.Authentification.Config.EmailVerificationService;
import com.core.Authentification.Config.JwtService;
import com.core.Authentification.Entities.User;
import com.core.Authentification.Enumurations.Role;
import com.core.Authentification.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	@Autowired
    private EmailVerificationService emailVerificationService;
	
	public AuthenticationResponse registre(RegisterRequest request) {
		// Récupérez le rôle à partir de la demande d'inscription
	    Role userRole = request.getRole(); // Utilisez directement la valeur du champ role
		
		var user=User.builder()
				
		.firstName(request.getFirstname())
		.lastName(request.getLastname())
		.email(request.getEmail())
		.password(passwordEncoder.encode(request.getPassword()))
		.role(userRole)
					.build();
		 user.setVerified(false);
			
	    emailVerificationService.sendVerificationEmail(user);
		
		repository.save(user);
		var jwtToken=jwtService.generateToken(user);
		return AuthenticationResponse.builder()
				.token(jwtToken)
				.build();
	}
	
	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword()
						)
				
				
				);
		var user= repository.findByEmail(request.getEmail()).orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder()
				.token(jwtToken)
				.build();
		
	}
	
	  public boolean emailExists(String email) {
	        // Utilisez le repository pour rechercher un utilisateur par e-mail
	        Optional<User> userOptional = repository.findByEmail(email);
	        // Si un utilisateur avec cet e-mail existe, retournez vrai, sinon retournez faux
	        return userOptional.isPresent();
	    }
	  
	  public Optional<User> getUserByEmail(String email) {
		  
		  return repository.findByEmail(email);
	  }

}
