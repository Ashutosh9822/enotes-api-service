package com.becoder.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.becoder.dto.PasswordChngRequest;
import com.becoder.entity.User;
import com.becoder.repository.UserRepository;
import com.becoder.service.UserService;
import com.becoder.util.CommonUtil;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

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
	
}
