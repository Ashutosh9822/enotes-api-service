package com.becoder.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.becoder.controller.HomeController;
import com.becoder.entity.AccountStatus;
import com.becoder.entity.User;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.exception.SuccessException;
import com.becoder.repository.UserRepository;
import com.becoder.service.HomeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HomeServiceImpl implements HomeService {
	
	Logger log=LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public Boolean verifyAccount(Integer userId, String verificationCode) throws Exception {
		log.info("HomeServiceImpl : verifyUserAccount() : start");
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Invalid User"));
		if(user.getStatus().getVerificationCode() == null) {
			log.info("message : Account already verified");
			throw new SuccessException("Account already verified");
		}
		if(user.getStatus().getVerificationCode().equals(verificationCode)) {
			AccountStatus status = user.getStatus();
			status.setIsActive(true);
			status.setVerificationCode(null);
			userRepository.save(user);
			log.info("message : Account verified successfully");
			return true;
		}
		log.info("HomeServiceImpl : verifyUserAccount() : End");
		return false;
	}

}
