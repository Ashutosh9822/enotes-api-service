package com.becoder.serviceImpl;


import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.becoder.dto.EmailRequest;
import com.becoder.dto.LoginRequest;
import com.becoder.dto.LoginResponse;
import com.becoder.dto.UserRequest;
import com.becoder.dto.UserResponse;
import com.becoder.entity.AccountStatus;
import com.becoder.entity.Role;
import com.becoder.entity.User;
import com.becoder.repository.RoleRepository;
import com.becoder.repository.UserRepository;
import com.becoder.security.CustomUserDetails;
import com.becoder.service.JwtService;
import com.becoder.service.AuthService;
import com.becoder.util.Validation;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private Validation validation;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private EmailService emailService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;

	@Override
	public Boolean register(UserRequest userDto, String url) throws Exception {

		validation.userValidation(userDto);

		User user = mapper.map(userDto, User.class);

		// Encode password before saving
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));

		setRole(userDto, user);

		AccountStatus status = new AccountStatus();
		status.setIsActive(false);
		status.setVerificationCode(UUID.randomUUID().toString());

		user.setStatus(status);

		User saveUser = userRepository.save(user);

		if (ObjectUtils.isEmpty(saveUser)) {
			return false;
		}

		emailSendForRegister(saveUser, url);

		return true;
	}

	private void emailSendForRegister(User saveUser, String url) throws Exception {
		String message = "Hi,<b>[[username]]</b>" + "<br>Your account Register Successfully</br>"
				+ "<br>Click below link to verify your account</br>" + "<a href='[[url]]'>Click Here</a></br></br>"
				+ "Thanks,</br>Enotes";

		message = message.replace("[[username]]", saveUser.getFirstName());
		message = message.replace("[[url]]", url + "/api/v1/home/verify?uid=" + saveUser.getId() + "&&code="
				+ saveUser.getStatus().getVerificationCode());

		EmailRequest emailRequest = new EmailRequest();
		emailRequest.setTo(saveUser.getEmail());
		emailRequest.setTitle("Account Creating Confirmation");
		emailRequest.setSubject("Account Created Successfully");
		emailRequest.setMessage(message);

		emailService.sendEmail(emailRequest);
	}

	private void setRole(UserRequest userDto, User user) {
		List<Integer> reqRoleId = userDto.getRoles().stream().map(r -> r.getId()).toList();
		List<Role> roles = roleRepository.findAllById(reqRoleId);
		user.setRoles(roles);
	}

	@Override
	public LoginResponse login(LoginRequest loginRequest) {
		
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		CustomUserDetails customUserDetails = (CustomUserDetails) authenticate.getPrincipal();
		String token = jwtService.generateToken(customUserDetails.getUser());
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setUser(mapper.map(customUserDetails.getUser(), UserResponse.class));
		loginResponse.setToken(token);
		
		return loginResponse;
	}

}
