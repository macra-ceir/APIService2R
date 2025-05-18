package com.gl.ceir.config.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.model.app.User;
import com.gl.ceir.config.repository.app.UserRepository;

@Service
public class UserStaticServiceImpl {

	private static final Logger logger = LogManager.getLogger(UserStaticServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	public User getCeirAdmin() {
		return userRepository.getByUsername("CEIRAdmin");
	}
	
	public List<User> getUserbyUsertypeId(long usertypeId) {
		return userRepository.getByUsertype_Id(usertypeId);
	}
	
	public boolean checkIfUserIsDisabled(long userId) {
		User user = userRepository.getById(userId);
		if( user.getCurrentStatus().equals(5))
			return Boolean.TRUE;
		return Boolean.FALSE;
	}
}