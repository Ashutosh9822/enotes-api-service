package com.becoder.serviceImpl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.becoder.dto.EmailRequest;
import com.becoder.dto.PasswordChngRequest;
import com.becoder.dto.PswdResetRequest;
import com.becoder.entity.User;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.UserRepository;
import com.becoder.service.UserService;
import com.becoder.util.CommonUtil;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailService emailService;

	@Override
	public void changePassword(PasswordChngRequest passwordChngRequest) {
		
		User loggedInUser = CommonUtil.getLoggedInUser();
		if(!passwordEncoder.matches(passwordChngRequest.getOldPassword(),loggedInUser.getPassword())) {
			throw new IllegalArgumentException("Old password is incorrect");
		}
		String encodePassword = passwordEncoder.encode(passwordChngRequest.getNewPassword());
		loggedInUser.setPassword(encodePassword);
		userRepository.save(loggedInUser);
	}

	@Override
	public void sendEmailPasswordReset(String email,HttpServletRequest request) throws Exception {
		User user = userRepository.findByEmail(email);
		if(ObjectUtils.isEmpty(user)) {
			throw new ResourceNotFoundException("Invalid Email");
		}
		
		String passwordResetToken = UUID.randomUUID().toString();
		user.getStatus().setPasswordResetToken(passwordResetToken);
		User updateUser = userRepository.save(user);
		
		String url = CommonUtil.getUrl(request);
		sendEmailRequest(updateUser,url);
	}

	private void sendEmailRequest(User user,String url) throws Exception {
		String message = "Hi,<b>[[username]]</b>" 
				+ "<p>You have requested to reset your password </p>"
				+ "<br>Click below link to change your password</br>" 
				+ "<p><a href='[[url]]'>change my password</a></p>"
				+ "<p>Ignore this message if you do remember your password,"
				+ "or you have not made the request</p>"
				+ "Thanks,</br>Enotes";

		message = message.replace("[[username]]", user.getFirstName());
		message = message.replace("[[url]]", url + "/api/v1/home/verify-pswd-link?uid=" + user.getId() + "&&code="
				+ user.getStatus().getPasswordResetToken());

		EmailRequest emailRequest = new EmailRequest();
		emailRequest.setTo(user.getEmail());
		emailRequest.setTitle("Password Reset");
		emailRequest.setSubject("Password Reset Link");
		emailRequest.setMessage(message);
		
		emailService.sendEmail(emailRequest);
	}

	@Override
	public void verifyPswdResetLink(Integer uid, String code) throws Exception {
		User user = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("Invalid user"));
		verifyPasswordResetToken(user.getStatus().getPasswordResetToken(),code);
	}

	private void verifyPasswordResetToken(String existToken, String reqToken) {

	    if (!StringUtils.hasText(reqToken)) {
	        throw new IllegalArgumentException("Invalid Token");
	    }

	    if (!StringUtils.hasText(existToken)) {
	        throw new IllegalArgumentException("Already Password reset");
	    }

	    if (!existToken.equals(reqToken)) {
	        throw new IllegalArgumentException("Invalid URL");
	    }
	}

	@Override
	public void resetPassword(PswdResetRequest pswdResetRequest) throws Exception {
		User user = userRepository.findById(pswdResetRequest.getUid()).orElseThrow(() -> new ResourceNotFoundException("Invalid user"));
		String encodePassword = passwordEncoder.encode(pswdResetRequest.getNewPassword());
		user.setPassword(encodePassword);
		user.getStatus().setPasswordResetToken(null);
		userRepository.save(user);
	}
	
}
