package com.core.Authentification.auth;

import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.Authentification.Config.EmailVerificationService;
import com.core.Authentification.Entities.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService service;
	private final JavaMailSender emailSender;
	
	@Autowired
    private EmailVerificationService emailVerificationService;
	
	@GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        try {
            emailVerificationService.verifyEmail(token);
            
            return ResponseEntity.ok("<h1>Email verified succ</h1>");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
	
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> Register(@RequestBody RegisterRequest request) {
	    // Vérifier si l'e-mail existe déjà dans la base de données
	    if (service.emailExists(request.getEmail())) {
	        // Si l'e-mail existe déjà, renvoyer une réponse indiquant que l'e-mail existe
	        return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(new AuthenticationResponse("Email already exists"));
	    } else {
	        // Si l'e-mail n'existe pas, procéder à l'inscription normalement
	        return ResponseEntity.ok(service.registre(request));
	    }
	}
	
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(
			@RequestBody AuthenticationRequest request
			)
	{
		return ResponseEntity.ok(service.authenticate(request));

		//
	}
	
	@GetMapping("/getUserByEmail")
	public Optional<User> getUserByEmail(@RequestParam String email) {
		return service.getUserByEmail(email);
	}
	
	/*@PostMapping("/send-verification-email")
	  public ResponseEntity<?> sendVerificationEmail(@RequestBody Map<String, String> request) {
	    String email = request.get("email");
	    // Logique pour envoyer l'e-mail de vérification à l'adresse e-mail spécifiée
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setTo(email);
	    message.setSubject("Email Verification");
	    message.setText("Please click on the following link to verify your email address.");
	    emailSender.send(message);
	    return ResponseEntity.ok().build();
	  }*/
}
