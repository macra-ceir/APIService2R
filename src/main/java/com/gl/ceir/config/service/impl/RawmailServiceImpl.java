package com.gl.ceir.config.service.impl;

import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.model.app.MessageConfigurationDb;
import com.gl.ceir.config.model.app.RawMail;
import com.gl.ceir.config.repository.app.MessageConfigurationDbRepository;

@Service
public class RawmailServiceImpl {

	private static final Logger logger = LogManager.getLogger(RawmailServiceImpl.class);

	@Autowired
	MessageConfigurationDbRepository messageConfigurationDbRepository;

	public String createMailContent(RawMail rawMail) {
		return createMailContent(rawMail.getTag(), rawMail.getPlaceholders());
	}
	
	public String createMailContent(String tag, Map<String, String> placeholders) {
		String message = "";
		MessageConfigurationDb messageDB = messageConfigurationDbRepository.getByTagAndActive(tag, 0);
		logger.info("Message for tag [" + tag + "] " + messageDB);
		
		if(Objects.isNull(messageDB)) {
			logger.info("No mail content is configured for tag [" + tag + "]");
			return message;
		}else {
			message = messageDB.getValue();	
		}

		// Replace Placeholders from message.
		if(Objects.nonNull(placeholders)) {
			for (Map.Entry<String, String> entry : placeholders.entrySet()) {
				logger.info("Placeholder key : " + entry.getKey() + " value : " + entry.getValue());
				message = message.replaceAll(entry.getKey(), entry.getValue());
			}
		}

		return message;
	}
}
