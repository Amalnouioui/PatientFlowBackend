package com.core.Authentification.Repository;

import com.core.Authentification.Entities.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByVerificationToken(String verificationToken);
    
    @Query("SELECT c FROM User c WHERE c.email = ?1")

	  public User findEmail(String email); 
	   
	  public User findByResetPasswordToken(String token);
}
