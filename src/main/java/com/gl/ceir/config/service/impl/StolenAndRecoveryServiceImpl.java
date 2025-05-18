package com.gl.ceir.config.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.configuration.FileStorageProperties;
import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.app.GenricResponse;
import com.gl.ceir.config.model.app.SingleImeiDetails;
import com.gl.ceir.config.model.app.StolenandRecoveryMgmt;
import com.gl.ceir.config.model.app.SystemConfigListDb;
import com.gl.ceir.config.model.app.SystemConfigurationDb;
import com.gl.ceir.config.model.app.User;
import com.gl.ceir.config.model.app.WebActionDb;
import com.gl.ceir.config.model.aud.AuditTrail;
import com.gl.ceir.config.model.constants.Tags;
import com.gl.ceir.config.repository.app.ConsignmentRepository;
import com.gl.ceir.config.repository.app.ImmegreationImeiDetailsRepository;
import com.gl.ceir.config.repository.app.StockManagementRepository;
import com.gl.ceir.config.repository.app.StolenAndRecoveryRepository;
import com.gl.ceir.config.repository.app.UserRepository;
import com.gl.ceir.config.repository.app.WebActionDbRepository;
import com.gl.ceir.config.repository.aud.AuditTrailRepository;
import com.gl.ceir.config.util.DateUtil;

@Service
public class StolenAndRecoveryServiceImpl {

	private static final Logger logger = LogManager.getLogger(StolenAndRecoveryServiceImpl.class);

	@Autowired
	FileStorageProperties fileStorageProperties;

	@Autowired
	StolenAndRecoveryRepository stolenAndRecoveryRepository;

	@Autowired
	WebActionDbRepository webActionDbRepository;

	@Autowired
	PropertiesReader propertiesReader;

	@Autowired
	StockManagementRepository distributerManagementRepository;

	@Autowired
	ConsignmentRepository consignmentRepository;


	@Autowired
	ImmegreationImeiDetailsRepository immegreationImeiDetailsRepository;

	@Autowired
	ConfigurationManagementServiceImpl configurationManagementServiceImpl; 
	
	@Autowired
	AuditTrailRepository auditTrailRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserStaticServiceImpl userStaticServiceImpl;

	@Transactional
	public GenricResponse blockSigleImei( SingleImeiDetails singleImeiDetails) {
		int count = 0;
		String request = null;
		List< SystemConfigListDb > requestTypes = null;
		try {
			if( userStaticServiceImpl.checkIfUserIsDisabled( singleImeiDetails.getUserId() ))
				return new GenricResponse(5,"This account is disabled. Please enable the account to perform the operation.",
						singleImeiDetails.getTxnId(), "USER_IS_DISABLED");
			requestTypes = configurationManagementServiceImpl.getSystemConfigListByTag(Tags.REQ_TYPE);
			WebActionDb webActionDb = new WebActionDb();
			webActionDb.setSubFeature( "Register" );
			AuditTrail auditTrail = new AuditTrail();
			for( SystemConfigListDb scld : requestTypes ) {
				if( scld.getValue().equals( singleImeiDetails.getRequestType() )) {
					request = scld.getInterpretation();
					webActionDb.setFeature(request);
					//auditTrail.setFeatureName(request);
				}
			}
			webActionDb.setTxnId(singleImeiDetails.getTxnId());
			webActionDb.setState(0);
			if( Objects.nonNull(singleImeiDetails.getFirstImei()) && !singleImeiDetails.getFirstImei().equals("") 
					&& !singleImeiDetails.getFirstImei().equals("null") )
				count += 1;
			if( Objects.nonNull(singleImeiDetails.getSecondImei()) && !singleImeiDetails.getSecondImei().equals("")
					&& !singleImeiDetails.getSecondImei().equals("null"))
				count += 1;
			if( Objects.nonNull(singleImeiDetails.getThirdImei()) && !singleImeiDetails.getThirdImei().equals("") 
					&& !singleImeiDetails.getThirdImei().equals("null"))
				count += 1;
			if( Objects.nonNull(singleImeiDetails.getFourthImei()) && !singleImeiDetails.getFourthImei().equals("") 
					&& !singleImeiDetails.getFourthImei().equals("null"))
				count += 1;
			if( !Objects.nonNull(singleImeiDetails.getBlockingType()) || singleImeiDetails.getBlockingType().trim().equals(""))
				singleImeiDetails.setBlockingType("Immediate");
			if( !Objects.nonNull(singleImeiDetails.getBlockingTimePeriod()) || singleImeiDetails.getBlockingTimePeriod().trim().equals("")
		|| singleImeiDetails.getBlockingTimePeriod().isEmpty() ) {
//				singleImeiDetails.setBlockingTimePeriod(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				singleImeiDetails.setBlockingTimePeriod(this.setBlockingType( singleImeiDetails.getBlockingType()));
			}
			StolenandRecoveryMgmt sarm = new StolenandRecoveryMgmt();
			sarm.setUserId(singleImeiDetails.getUserId());
			sarm.setRequestType(singleImeiDetails.getRequestType());
			sarm.setRoleType( singleImeiDetails.getUserType());
			sarm.setFileStatus(0);
			sarm.setTxnId( singleImeiDetails.getTxnId());
			sarm.setSourceType( singleImeiDetails.getSourceType() );
			sarm.setBlockingType( singleImeiDetails.getBlockingType());
			sarm.setBlockingTimePeriod( singleImeiDetails.getBlockingTimePeriod());
			sarm.setOperatorTypeId( singleImeiDetails.getOperatorTypeId());
			sarm.setQty( count );
			sarm.setDeviceQuantity( singleImeiDetails.getDeviceQuantity());
			sarm.setBlockCategory( singleImeiDetails.getCategory() );
			sarm.setRemark( singleImeiDetails.getRemark() );
			sarm = stolenAndRecoveryRepository.save(sarm);
			singleImeiDetails.setSarmId( sarm.getId());
			immegreationImeiDetailsRepository.save(singleImeiDetails);
			webActionDbRepository.save(webActionDb);
			User user = userRepository.getByid( singleImeiDetails.getUserId());
			auditTrail.setSubFeature("Created");
			auditTrail.setUserId( singleImeiDetails.getUserId() );
			auditTrail.setUserName( user.getUsername());
			auditTrail.setUserType( singleImeiDetails.getUserType());
			auditTrail.setFeatureId(7l);
			auditTrail.setTxnId( singleImeiDetails.getTxnId() );
			auditTrail.setPublicIp(singleImeiDetails.getPublicIp());
			auditTrail.setBrowser(singleImeiDetails.getBrowser());
			auditTrail.setFeatureName("Block/unblock Devices");
			if( Objects.nonNull( singleImeiDetails.getRoleType() )) {
				auditTrail.setRoleType( singleImeiDetails.getRoleType() );
			}else {
				auditTrail.setRoleType( "NA" );
			}
			auditTrailRepository.save(auditTrail);
			return new GenricResponse(0,request+" saved Successfully.", singleImeiDetails.getTxnId());

		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	private String setBlockingType(String blockingType ) {
		String date = null;
		if(Objects.isNull(blockingType)
				|| blockingType.equalsIgnoreCase("Default") 
				|| blockingType.isEmpty()) {
			SystemConfigurationDb systemConfigurationDb = configurationManagementServiceImpl.findByTag("GREY_TO_BLACK_MOVE_PERIOD_IN_DAY");
			date = DateUtil.nextDate(Integer.parseInt(systemConfigurationDb.getValue()), "yyyy-MM-dd");
		}else{
			date = DateUtil.nextDate(0, "yyyy-MM-dd");
		}
		return date;
	}
	
	@Transactional
	public GenricResponse updateSigleBlockImei( SingleImeiDetails singleImeiDetails) {
		String request = null;
		List< SystemConfigListDb > requestTypes = null;
		try {
			if( userStaticServiceImpl.checkIfUserIsDisabled( singleImeiDetails.getUserId() ))
				return new GenricResponse(5,"This account is disabled. Please enable the account to perform the operation.",
						singleImeiDetails.getTxnId(), "USER_IS_DISABLED");
			requestTypes = configurationManagementServiceImpl.getSystemConfigListByTag(Tags.REQ_TYPE);
			WebActionDb webActionDb = new WebActionDb();
			webActionDb.setSubFeature( "Update" );
			for( SystemConfigListDb scld : requestTypes ) {
				if( scld.getValue().equals( singleImeiDetails.getRequestType() )) {
					request = scld.getInterpretation();
					webActionDb.setFeature(request);
				}
			}
			webActionDb.setTxnId(singleImeiDetails.getTxnId());
			webActionDb.setState(0);
			//SingleImeiDetails old = immegreationImeiDetailsRepository.getOne( singleImeiDetails.getId() );
			SingleImeiDetails old = immegreationImeiDetailsRepository.getByTxnId(singleImeiDetails.getTxnId());
			if( old != null ) {
				old.setModifiedOn( new Date());
				old.setProcessState( singleImeiDetails.getProcessState());
				old.setMultipleSimStatus( singleImeiDetails.getMultipleSimStatus() );
				old.setDeviceIdType( singleImeiDetails.getDeviceIdType() );
				old.setDeviceSerialNumber( singleImeiDetails.getDeviceSerialNumber());
				old.setRemark( singleImeiDetails.getRemark() );
				old.setDeviceType( singleImeiDetails.getDeviceType());
				old.setCategory( singleImeiDetails.getCategory() );
				old.setFirstImei( singleImeiDetails.getFirstImei() );
				old.setSecondImei( singleImeiDetails.getSecondImei() );
				old.setThirdImei( singleImeiDetails.getThirdImei() );
				old.setFourthImei( singleImeiDetails.getFourthImei() );
				old.setTxnId( singleImeiDetails.getTxnId() );
				old.setRequestType( singleImeiDetails.getRequestType() );
				old.setSourceType( singleImeiDetails.getSourceType());
				old.setBlockingType( singleImeiDetails.getBlockingType());
				old.setBlockingTimePeriod( singleImeiDetails.getBlockingTimePeriod());
				StolenandRecoveryMgmt sarm = old.getsARm();
				sarm.setId(old.getSarmId());
				sarm.setUserId(singleImeiDetails.getUserId());
				sarm.setRequestType(singleImeiDetails.getRequestType());
				sarm.setRoleType( singleImeiDetails.getUserType());
				sarm.setFileStatus(0);
				sarm.setTxnId( singleImeiDetails.getTxnId());
				sarm.setSourceType( singleImeiDetails.getSourceType() );
				sarm.setBlockingType( singleImeiDetails.getBlockingType());
				sarm.setBlockingTimePeriod( singleImeiDetails.getBlockingTimePeriod());
				sarm.setOperatorTypeId( singleImeiDetails.getOperatorTypeId());
				sarm.setDeviceQuantity( singleImeiDetails.getDeviceQuantity());
				sarm.setRemark(singleImeiDetails.getRemark());
				immegreationImeiDetailsRepository.save(old);
				stolenAndRecoveryRepository.save(sarm);
				webActionDbRepository.save(webActionDb);
				User user = userRepository.getByid( singleImeiDetails.getUserId());
				AuditTrail auditTrail = new AuditTrail();
				auditTrail.setFeatureName("Block/unblock Devices");
				auditTrail.setSubFeature("Updated");
				auditTrail.setUserId( singleImeiDetails.getUserId() );
				auditTrail.setUserName( user.getUsername());
				auditTrail.setUserType( singleImeiDetails.getUserType());
				auditTrail.setFeatureId(7l);
				auditTrail.setTxnId( singleImeiDetails.getTxnId() );
				auditTrail.setPublicIp(singleImeiDetails.getPublicIp());
				auditTrail.setBrowser(singleImeiDetails.getBrowser());
				if( Objects.nonNull( singleImeiDetails.getRoleType() )) {
					auditTrail.setRoleType( singleImeiDetails.getRoleType() );
				}else {
					auditTrail.setRoleType( "NA" );
				}
				auditTrailRepository.save(auditTrail);
				return new GenricResponse(0,request+" updated Successfully.", singleImeiDetails.getTxnId());
			}else {
				return new GenricResponse(4,"TxnId Does Not exist", singleImeiDetails.getTxnId());
			}

		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	@Transactional
	public List<SingleImeiDetails> getBlockUnblockDetailsByTxnId( String txnId ){
		List<SystemConfigListDb> sourceTypes = null;
		List<SingleImeiDetails> singleImeiDetails = null;
		List<SystemConfigListDb> categoryTypes = null;
		List<SystemConfigListDb> deviceTypes   = null;
		List<SystemConfigListDb> deviceIdTypes = null;
		List<SystemConfigListDb> mulSimStatus  = null;
		try {
			sourceTypes   = configurationManagementServiceImpl.getSystemConfigListByTag(Tags.SOURCE_TYPE);
			categoryTypes = configurationManagementServiceImpl.getSystemConfigListByTag( "BLOCK_CATEGORY" );
			deviceTypes   = configurationManagementServiceImpl.getSystemConfigListByTag( "DEVICE_TYPE" );
			deviceIdTypes = configurationManagementServiceImpl.getSystemConfigListByTag( "DEVICE_ID_TYPE" );
			mulSimStatus  = configurationManagementServiceImpl.getSystemConfigListByTag( "MULTI_SIM_STATUS" );
			singleImeiDetails = immegreationImeiDetailsRepository.getAllByTxnId(txnId);
			for(SingleImeiDetails sid : singleImeiDetails) {

				for(SystemConfigListDb configListDb : sourceTypes) {
					if(sid.getSourceType() == configListDb.getValue()) {
						sid.setSourceTypeInterp(configListDb.getInterpretation());
						break;
					}
				}
				for(SystemConfigListDb configListDb : categoryTypes) {
					if(sid.getCategory() == configListDb.getValue()) {
						sid.setCategoryInterp(configListDb.getInterpretation());
						break;
					}
				}
				for(SystemConfigListDb configListDb : deviceTypes) {
					if(sid.getDeviceType() == configListDb.getValue()) {
						sid.setDeviceTypeInterp(configListDb.getInterpretation());
						break;
					}
				}
				for(SystemConfigListDb configListDb : deviceIdTypes) {
					if(sid.getDeviceIdType() == configListDb.getValue()) {
						sid.setDeviceIdTypeInterp(configListDb.getInterpretation());
						break;
					}
				}
				for(SystemConfigListDb configListDb : mulSimStatus) {
					if(sid.getMultipleSimStatus() == configListDb.getValue()) {
						sid.setMultipleSimStatusInterp(configListDb.getInterpretation());
						break;
					}
				}
			}
			return singleImeiDetails;
		}catch( Exception e ) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
}
