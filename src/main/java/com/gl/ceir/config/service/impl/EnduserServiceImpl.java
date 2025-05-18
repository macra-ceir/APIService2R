package com.gl.ceir.config.service.impl;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.configuration.FileStorageProperties;
import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.app.EndUserDB;
import com.gl.ceir.config.model.app.GenricResponse;
import com.gl.ceir.config.repository.app.EndUserDbRepository;

@Service
public class EnduserServiceImpl {

	private static final Logger logger = LogManager.getLogger(EnduserServiceImpl.class);

	@Autowired
	EndUserDbRepository endUserDbRepository;

	@Autowired
	PropertiesReader propertiesReader;

	@Autowired
	FileStorageProperties fileStorageProperties;

	public GenricResponse endUserByNid(String nid) {
		try {
			EndUserDB endUserDB = endUserDbRepository.getByNid(nid);

			// End user is not registered with CEIR system.
			if(Objects.nonNull(endUserDB)) {
				return new GenricResponse(1, "End User does exist.", nid);
			}else {
				return new GenricResponse(0, "User does not exist.", "");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException("Custom Service", e.getMessage());
		}
	}

}
