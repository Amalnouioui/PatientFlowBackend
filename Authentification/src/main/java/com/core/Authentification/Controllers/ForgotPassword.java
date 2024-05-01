package com.core.Authentification.Controllers;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.core.Authentification.Entities.User;
import com.core.Authentification.Entities.Utility;
import com.core.Authentification.Service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;

@RestController
@RequestMapping("/api/Password")
public class ForgotPassword {
	
	@Autowired
    private JavaMailSender mailSender;
	
	 @Autowired
	private UserService customerService;
	 
	 /*@GetMapping("/forgot_password")
	    
	 public String showForgotPasswordForm() {
	    	
	        return "<div>\n"
	        		+ "    <h2>Forgot Password</h2>\n"
	        		+ "</div>\n"
	        		+ "     \n"
	        		+ "<div th:if=\"${error != null}\">\n"
	        		+ "    <p class=\"text-danger\">[[${error}]]</p>\n"
	        		+ "</div>\n"
	        		+ "<div th:if=\"${message != null}\">\n"
	        		+ "    <p class=\"text-warning\">[[${message}]]</p>\n"
	        		+ "</div>\n"
	        		+ "         \n"
	        		+ "<form th:action=\"@{/forgot_password}\" method=\"post\" style=\"max-width: 420px; margin: 0 auto;\">\n"
	        		+ "<div class=\"border border-secondary rounded p-3\">\n"
	        		+ "    <div>\n"
	        		+ "        <p>We will be sending a reset password link to your email.</p>\n"
	        		+ "    </div>\n"
	        		+ "    <div>\n"
	        		+ "        <p>\n"
	        		+ "            <input type=\"email\" name=\"email\" class=\"form-control\" placeholder=\"Enter your e-mail\" required autofocus/>\n"
	        		+ "        </p>         \n"
	        		+ "        <p class=\"text-center\">\n"
	        		+ "            <input type=\"submit\" value=\"Send\" class=\"btn btn-primary\" />\n"
	        		+ "        </p>\n"
	        		+ "    </div>\n"
	        		+ "</div>\n"
	        		+ "</form>";
	        	}*/
	 
	 @PostMapping("/forgot_password")
	    public String processForgotPassword(HttpServletRequest request, Model model) {
	        String email = request.getParameter("email");
	        String token = RandomString.make(30);
	         
	        try {
	            customerService.updateResetPasswordToken(token, email);
	            String resetPasswordLink = Utility.getSiteURL(request) + "/api/Password/reset_password?token=" + token;
	            sendEmail(email, resetPasswordLink);
	            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
	             
	        } catch (AccountNotFoundException ex) {
	            model.addAttribute("error", ex.getMessage());
	        } catch (UnsupportedEncodingException | MessagingException e) {
	            model.addAttribute("error", "Error while sending email");
	        }
	         
	        return "forgot_password_success"; // Replace with the actual view name
	    }
	 
	 public void sendEmail(String recipientEmail, String link)
	            throws MessagingException, UnsupportedEncodingException {
	        MimeMessage message = mailSender.createMimeMessage();              
	        MimeMessageHelper helper = new MimeMessageHelper(message);
	         
	        helper.setFrom("contact@clinique.com", "Clinique Support");
	        helper.setTo(recipientEmail);
	         
	        String subject = "Here's the link to reset your password";
	         
	        String content = "<p>Hello,</p>"
	                + "<p>You have requested to reset your password.</p>"
	                + "<p>Click the link below to change your password:</p>"
	                + "<p><a href=\"" + link + "\">Change my password</a></p>"
	                + "<br>"
	                + "<p>Ignore this email if you do remember your password, "
	                + "or you have not made the request.</p>";
	         
	        helper.setSubject(subject);
	         
	        helper.setText(content, true);
	         
	        mailSender.send(message);
	    }
	     

	    @GetMapping("/reset_password")
	    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
	   User customer = customerService.getByResetPasswordToken(token);
	        model.addAttribute("token", token);
	         
	        if (customer == null) {
	            model.addAttribute("message", "Invalid Token");
	            return "invalid token ";
	        }
	         
	        return "<div>\n"
	        		+ "    <h2>Reset Your Password</h2>\n"
	        		+ "</div>\n"
	        		+ "         \n"
	        		+ "<form th:action=\"@{/reset_password}\" method=\"post\" style=\"max-width: 350px; margin: 0 auto;\">\n"
	        		+ "    <input type=\"hidden\" name=\"token\" th:value=\"${token}\" />\n"
	        		+ "<div class=\"border border-secondary rounded p-3\">\n"
	        		+ "    <div>\n"
	        		+ "        <p>\n"
	        		+ "            <input type=\"password\" name=\"password\" id=\"password\" class=\"form-control\"\n"
	        		+ "                placeholder=\"Enter your new password\" required autofocus />\n"
	        		+ "        </p>         \n"
	        		+ "        <p>\n"
	        		+ "            <input type=\"password\" class=\"form-control\" placeholder=\"Confirm your new password\"\n"
	        		+ "                required oninput=\"checkPasswordMatch(this);\" />\n"
	        		+ "        </p>         \n"
	        		+ "        <p class=\"text-center\">\n"
	        		+ "            <input type=\"submit\" value=\"Change Password\" class=\"btn btn-primary\" />\n"
	        		+ "        </p>\n"
	        		+ "    </div>\n"
	        		+ "</div>\n"
	        		+ "</form>";
	    }
	    @PostMapping("/reset_password")
	    public String processResetPassword(HttpServletRequest request, Model model) {
	        String token = request.getParameter("token");
	        String password = request.getParameter("password");
	         
	 User customer = customerService.getByResetPasswordToken(token);
	        model.addAttribute("title", "Reset your password");
	         
	        if (customer == null) {
	            model.addAttribute("message", "Invalid Token");
	            return "message";
	        } else {           
	            customerService.updatePassword(customer, password);
	             
	            model.addAttribute("message", "You have successfully changed your password.");
	        }
	         
	        return "message";
	    }
	 
	 

}
