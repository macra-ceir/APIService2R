package com.gl.ceir.config.service.impl;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.app.FilterRequest;
import com.gl.ceir.config.model.app.GenricResponse;
import com.gl.ceir.config.model.app.MessageConfigurationDb;
import com.gl.ceir.config.model.app.Notification;
import com.gl.ceir.config.model.app.SearchCriteria;
import com.gl.ceir.config.model.app.SystemConfigListDb;
import com.gl.ceir.config.model.app.SystemConfigurationDb;
import com.gl.ceir.config.model.aud.AuditTrail;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.model.constants.Tags;
import com.gl.ceir.config.repository.app.MessageConfigurationDbRepository;
import com.gl.ceir.config.repository.app.NotificationRepository;
import com.gl.ceir.config.repository.app.SystemConfigListRepository;
import com.gl.ceir.config.repository.app.SystemConfigurationDbRepository;
import com.gl.ceir.config.repository.aud.AuditTrailRepository;
import com.gl.ceir.config.specificationsbuilder.GenericSpecificationBuilder;
import com.gl.ceir.config.util.InterpSetter;

@Service
public class ConfigurationManagementServiceImpl {

	private static final Logger logger = LogManager.getLogger(ConfigurationManagementServiceImpl.class);

	@Autowired
	SystemConfigurationDbRepository systemConfigurationDbRepository;

	@Autowired
	MessageConfigurationDbRepository messageConfigurationDbRepository;

	@Autowired
	SystemConfigListRepository systemConfigListRepository;

	@Autowired
	NotificationRepository notificationRepository;

	@Autowired
	AuditTrailRepository auditTrailRepository;

	@Autowired
	PropertiesReader propertiesReader;

	@Autowired
	InterpSetter interpSetter;

	public List<SystemConfigurationDb> getAllInfo(){
		try {
			return systemConfigurationDbRepository.findAll();
		} catch (Exception e) {
			logger.info("Exception found="+e.getMessage());
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public Page<SystemConfigurationDb> filterSystemConfiguration(FilterRequest filterRequest, Integer pageNo, Integer pageSize){
		try {

			Pageable pageable = PageRequest.of(pageNo, pageSize);
			GenericSpecificationBuilder<SystemConfigurationDb> sb = new GenericSpecificationBuilder<SystemConfigurationDb>(propertiesReader.dialect);

			if(Objects.nonNull(filterRequest.getTag()))
				sb.with(new SearchCriteria("tag", filterRequest.getTag(), SearchOperation.EQUALITY, Datatype.STRING));

			if(Objects.nonNull(filterRequest.getType()))
				sb.with(new SearchCriteria("type", filterRequest.getType(), SearchOperation.EQUALITY, Datatype.STRING));
			
			if(Objects.nonNull(filterRequest.getSearchString()) && !filterRequest.getSearchString().isEmpty()){
				// sb.orSearch(new SearchCriteria("type", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
				sb.orSearch(new SearchCriteria("description", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
				sb.orSearch(new SearchCriteria("value", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			}

			Page<SystemConfigurationDb> page = systemConfigurationDbRepository.findAll(sb.build(), pageable);

			for(SystemConfigurationDb systemConfigurationDb : page.getContent()) {	
				systemConfigurationDb.setTypeInterp(interpSetter.setConfigInterp(Tags.CONFIG_TYPE, systemConfigurationDb.getType()));
			}
			return page;

		} catch (Exception e) {
			logger.info("Exception found="+e.getMessage());
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public SystemConfigurationDb findByTag(SystemConfigurationDb systemConfigurationDb){
		try {
			return systemConfigurationDbRepository.getByTag(systemConfigurationDb.getTag());
		} catch (Exception e) {
			logger.info("Exception found="+e.getMessage());
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public SystemConfigurationDb findByTag(String tag){
		try {
			return systemConfigurationDbRepository.getByTag(tag);
		} catch (Exception e) {
			logger.info("Exception found="+e.getMessage());
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public List<MessageConfigurationDb> getMessageConfigAllDetails(){

		try {

			return messageConfigurationDbRepository.findAll();

		} catch (Exception e) {
			logger.info("Exception found="+e.getMessage());
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}	

	}

	public Page<MessageConfigurationDb> filterMessageConfiguration(FilterRequest filterRequest, Integer pageNo, Integer pageSize){
		try {

			Pageable pageable = PageRequest.of(pageNo, pageSize);
			GenericSpecificationBuilder<MessageConfigurationDb> sb = new GenericSpecificationBuilder<>(propertiesReader.dialect);

			if(Objects.nonNull(filterRequest.getTag()))
				sb.with(new SearchCriteria("tag", filterRequest.getTag(), SearchOperation.EQUALITY, Datatype.STRING));

			if(Objects.nonNull(filterRequest.getChannel()))
				sb.with(new SearchCriteria("channel", filterRequest.getChannel(), SearchOperation.EQUALITY, Datatype.STRING));
			
			if(Objects.nonNull(filterRequest.getSearchString()) && !filterRequest.getSearchString().isEmpty()){
				sb.orSearch(new SearchCriteria("description", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
				sb.orSearch(new SearchCriteria("value", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			}
			
			Page<MessageConfigurationDb> page = messageConfigurationDbRepository.findAll(sb.build(), pageable);

			for(MessageConfigurationDb messageConfigurationDb : page.getContent()) {	
				messageConfigurationDb.setChannelInterp(interpSetter.setConfigInterp(Tags.CHANNEL, messageConfigurationDb.getChannel()));
			}

			return page;

		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public MessageConfigurationDb getMessageConfigDetailsByTag(MessageConfigurationDb messageConfigurationDb){
		try {

			return messageConfigurationDbRepository.getByTag(messageConfigurationDb.getTag());

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}	
	}

	@Transactional
	public GenricResponse saveAudit(AuditTrail auditTrail) {
		try {

			auditTrailRepository.save(auditTrail);

			return new GenricResponse(0, "Audit trail save Sucess fully", "");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public GenricResponse saveNotification(Notification notification) {
		try {

			notificationRepository.save(notification);
			return new GenricResponse(0, "Notification have been saved Sucessfully", "");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public GenricResponse saveNotification(String channelType, String message, Long userId, Long featureId, String featureName, 
			String subFeature, String featureTxnId, String subject, int retryCount, String referTable, String roleType, String receiverUserType ) {
		try {

			notificationRepository.save(new Notification(channelType, message, userId, featureId, featureName, 
					subFeature, featureTxnId, subject, retryCount, referTable, roleType, receiverUserType));

			return new GenricResponse(0, "Notification have been saved Sucessfully", "");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public GenricResponse saveAllNotifications(List<Notification> notifications) {
		try {

			List<Notification> notifications2 = notificationRepository.saveAll(notifications);

			return new GenricResponse(0, "Notification have been saved Sucessfully", "", notifications2);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public List<SystemConfigListDb> getSystemConfigListByTag(String tag){
		try {

			logger.debug("getSystemConfigListByTag : " + tag);

			return systemConfigListRepository.findByTag(tag, new Sort(Sort.Direction.ASC, "listOrder"));

		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public SystemConfigurationDb saveSystemConfiguration(SystemConfigurationDb systemConfigurationDb){
		try {
			return systemConfigurationDbRepository.save(systemConfigurationDb);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public MessageConfigurationDb saveMessageConfiguration(MessageConfigurationDb messageConfigurationDb){
		try {
			return messageConfigurationDbRepository.save(messageConfigurationDb);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
}