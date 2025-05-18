package com.gl.ceir.config.EmailSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.lang.NonNull;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.model.app.MessageConfigurationDb;
import com.gl.ceir.config.model.app.Notification;
import com.gl.ceir.config.model.app.RawMail;
import com.gl.ceir.config.model.app.UserProfile;
import com.gl.ceir.config.model.constants.ChannelType;
import com.gl.ceir.config.repository.app.MessageConfigurationDbRepository;
import com.gl.ceir.config.service.impl.ConfigurationManagementServiceImpl;
import com.gl.ceir.config.service.impl.RawmailServiceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class EmailUtil {

	private Logger logger = (Logger) LogManager.getLogger(EmailUtil.class);

//	@Autowired
//	MailSender mailSender; 
//
//	@Autowired
//	JavaMailSender javaMailSender;

	@Autowired
	ConfigurationManagementServiceImpl configurationManagementServiceImpl;

	@Autowired
	MessageConfigurationDbRepository messageConfigurationDbRepository;
	
	@Autowired
	RawmailServiceImpl rawmailServiceImpl;

//	public boolean sendEmail(String toAddress, String fromAddress, String subject, String msgBody) {
//
//		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//		simpleMailMessage.setFrom(fromAddress);
//		simpleMailMessage.setTo(toAddress);
//		simpleMailMessage.setSubject(subject);
//		simpleMailMessage.setText(msgBody);
//
//		try {
//			mailSender.send(simpleMailMessage);
//		}catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			return Boolean.FALSE;
//		}
//
//		return Boolean.TRUE;
//	}
//
//	public void sendEmailWithAttactment(String toAddress, String fromAddress, String subject, String msgBody, String attachment) {
//
//		MimeMessage message = javaMailSender.createMimeMessage();
//		try{
//			MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//			helper.setFrom(fromAddress);
//			helper.setTo(toAddress);
//			helper.setSubject(subject);
//			helper.setText(msgBody);
//
//			FileSystemResource file = new FileSystemResource(attachment);
//			helper.addAttachment(file.getFilename(), file);
//
//		}catch (MessagingException e) {
//			throw new MailParseException(e);
//		}
//
//		javaMailSender.send(message);
//	}

	public boolean saveNotification(@NonNull String tag, UserProfile userProfile, long featureId, 
			String featureName, String subFeature, String featureTxnId, String subject, 
			Map<String, String> placeholders, String roleType, String receiverUserType) {
		try {
			MessageConfigurationDb messageDB = messageConfigurationDbRepository.getByTagAndActive(tag, 0);
			logger.info("Message for tag [" + tag + "] " + messageDB);
			String message = messageDB.getValue();
			
			if(Objects.isNull(message)) {
				return Boolean.TRUE;
			}

			// Replace Placeholders from message.
			if(Objects.nonNull(placeholders)) {
				for (Map.Entry<String, String> entry : placeholders.entrySet()) {
					logger.info("Placeholder key : " + entry.getKey() + " value : " + entry.getValue());
					message = message.replaceAll(entry.getKey(), entry.getValue());
				}
			}
			// Save email in notification table.
			configurationManagementServiceImpl.saveNotification(ChannelType.EMAIL, message, 
					userProfile.getUser().getId(), featureId, featureName, subFeature, featureTxnId, 
					subject, 0, null, roleType, receiverUserType);

			return Boolean.TRUE;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Boolean.FALSE;
		}
	}
	
	public boolean saveNotificationV2(@NonNull String tag, UserProfile userProfile, long featureId,
            String featureName, String subFeature, String featureTxnId, String subject, 
            Map<String, String> placeholders, String roleType, String receiverUserType, String referTable ) {
	    try {
	            MessageConfigurationDb messageDB = messageConfigurationDbRepository.getByTagAndActive(tag, 0);
	            logger.info("Message for tag [" + tag + "] " + messageDB);
	            String message = messageDB.getValue();
	            subject = messageDB.getSubject();
	            
	            if(Objects.isNull(message)) {
	                    return Boolean.TRUE;
	            }
	
	            // Replace Placeholders from message.
	            if(Objects.nonNull(placeholders)) {
	                    for (Map.Entry<String, String> entry : placeholders.entrySet()) {
	                            logger.info("Placeholder key : " + entry.getKey() + " value : " + entry.getValue());
	                            message = message.replaceAll(entry.getKey(), entry.getValue());
	                            subject = subject.replaceAll(entry.getKey(), entry.getValue());
	                    }
	            }
	            // Save email in notification table.
	            logger.info(ChannelType.EMAIL+","+ message+","+ 
	                            userProfile.getUser().getId()+","+ featureId +","+ featureName +","+ subFeature +","+ featureTxnId+","+ 
	                            subject+", 0, null,"+ roleType);
	            configurationManagementServiceImpl.saveNotification(ChannelType.EMAIL, message, 
	                            userProfile.getUser().getId(), featureId, featureName, subFeature, featureTxnId, 
	                            subject, 0, referTable, roleType, receiverUserType);
	
	            return Boolean.TRUE;
	    }catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            return Boolean.FALSE;
	    }
	}
	
	public boolean saveNotification(List<RawMail> rawMails) {
		List<Notification> notifications = new ArrayList<>();
		
		if(rawMails.isEmpty()) {
			return Boolean.TRUE;
		}
		
		try {
			for(RawMail rawMail : rawMails) {
				String message = rawmailServiceImpl.createMailContent(rawMail);
				if(message.isEmpty()) {
					continue;
				}
				
				notifications.add(new Notification(ChannelType.EMAIL, 
						message, 
						rawMail.getUserProfile().getUser().getId(), 
						rawMail.getFeatureId(),
						rawMail.getFeatureName(), 
						rawMail.getSubFeature(), 
						rawMail.getFeatureTxnId(), 
						rawMail.getSubject(), 
						0,
						rawMail.getReferTable(),
						rawMail.getRoleType(),
						rawMail.getReceiverUserType()));
			}
			
			configurationManagementServiceImpl.saveAllNotifications(notifications);
			
			logger.info("Notification have been saved." + rawMails);
			return Boolean.TRUE;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Boolean.FALSE;
		}
	}
	
	
}

