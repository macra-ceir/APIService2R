package com.gl.ceir.config.service.impl;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.gl.ceir.config.EmailSender.EmailUtil;
import com.gl.ceir.config.EmailSender.MailSubjects;
import com.gl.ceir.config.configuration.FileStorageProperties;
import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.feign.UserFeignClient;
import com.gl.ceir.config.model.app.AttachedFileInfo;
import com.gl.ceir.config.model.app.DashboardUsersFeatureStateMap;
import com.gl.ceir.config.model.app.FileDetails;
import com.gl.ceir.config.model.app.GenricResponse;
import com.gl.ceir.config.model.app.GrievanceFilterRequest;
import com.gl.ceir.config.model.app.SearchCriteria;
import com.gl.ceir.config.model.app.StatesInterpretationDb;
import com.gl.ceir.config.model.app.SystemConfigListDb;
import com.gl.ceir.config.model.app.TypeApproveDelete;
import com.gl.ceir.config.model.app.TypeApproveFilter;
import com.gl.ceir.config.model.app.TypeApprovedAttachedFileInfo;
import com.gl.ceir.config.model.app.TypeApprovedDb;
import com.gl.ceir.config.model.app.TypeApprovedTAC;
import com.gl.ceir.config.model.app.User;
import com.gl.ceir.config.model.app.UserProfile;
import com.gl.ceir.config.model.app.WebActionDb;
import com.gl.ceir.config.model.app.brandRepoModel;
import com.gl.ceir.config.model.app.modelRepoPojo;
import com.gl.ceir.config.model.aud.AuditTrail;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.Features;
import com.gl.ceir.config.model.constants.GrievanceOrderColumnMapping;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.model.constants.Tags;
import com.gl.ceir.config.model.constants.TypeApproveDBOrderColumnMapping;
import com.gl.ceir.config.model.constants.WebActionDbFeature;
import com.gl.ceir.config.model.file.TypeApproveCEIRFileModel;
import com.gl.ceir.config.model.file.TypeApproveFileModel;
import com.gl.ceir.config.repository.app.DashboardUsersFeatureStateMapRepository;
import com.gl.ceir.config.repository.app.ModelRepository;
import com.gl.ceir.config.repository.app.StatesInterpretaionRepository;
import com.gl.ceir.config.repository.app.SystemConfigListRepository;
import com.gl.ceir.config.repository.app.SystemConfigurationDbRepository;
import com.gl.ceir.config.repository.app.TypeApproveRepository;
import com.gl.ceir.config.repository.app.TypeApprovedTACRepository;
import com.gl.ceir.config.repository.app.UserProfileRepository;
import com.gl.ceir.config.repository.app.UserRepository;
import com.gl.ceir.config.repository.app.WebActionDbRepository;
import com.gl.ceir.config.repository.app.brandRepository;
import com.gl.ceir.config.repository.aud.AuditTrailRepository;
import com.gl.ceir.config.request.model.Generic_Response_Notification;
import com.gl.ceir.config.request.model.RegisterationUser;
import com.gl.ceir.config.specificationsbuilder.GenericSpecificationBuilder;
import com.gl.ceir.config.util.CustomMappingStrategy;
import com.gl.ceir.config.util.HttpResponse;

import java.nio.file.Files;
import java.nio.file.Paths;
@Service
public class TypeApproveServiceImpl {

	private static final Logger log = LogManager.getLogger(TypeApproveServiceImpl.class);
	
	@Autowired
	FileStorageProperties fileStorageProperties;
	@Autowired
	TypeApproveRepository typeApproveRepo;	
	@Autowired
	PropertiesReader propertiesReader;
	@Autowired
	SystemConfigurationDbRepository systemConfigurationDbRepository;
	@Autowired
	ModelRepository modelRepository;
	@Autowired
	brandRepository brandRepository;
	@Autowired	
	EmailUtil emailUtil;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AuditTrailRepository auditTrailRepository;
	@Autowired
	StatesInterpretaionRepository statesInterpretaionRepository;
//	@Autowired
//	RestTemplate restTemplate;
	@Autowired
	WebActionDbRepository webActionDbRepository;
	@Autowired
	TypeApprovedTACRepository typeApprovedTACRepository;
	@Autowired
	SystemConfigListRepository systemConfigListRepository;
	@Autowired
	DashboardUsersFeatureStateMapRepository dufsmRepo;
	@Autowired
	UserFeignClient userFeignClient;
	@Autowired
	UserProfileRepository userProfileRepository;
	
	public GenricResponse saveTypeApprove(TypeApprovedDb typeApprove) {
		log.info("inside type approved db controller");  
		log.info("typeApprove data:  "+typeApprove);
		User user = null;
		TypeApprovedDb output = null;
		TypeApprovedAttachedFileInfo attachFileInfo = null;
		try 
		{
			if( typeApprove.getAttachedFiles().isEmpty() ) {
				attachFileInfo = new TypeApprovedAttachedFileInfo();
				attachFileInfo.setDocType(null);
				attachFileInfo.setFileName(null);
				List<TypeApprovedAttachedFileInfo> attach = new ArrayList<TypeApprovedAttachedFileInfo>();
				attach.add(attachFileInfo);
				typeApprove.setAttachedFiles( attach );
			}
			output = typeApproveRepo.getByTacAndUserId( typeApprove.getTac(), typeApprove.getUserId() );
			if( output !=  null || Objects.nonNull(output)) {
				return new GenricResponse(201,"Type Approved for TAC already exists.","0","REGISTER_TYPE_APPROVE_REJECTED");
			}
			output = typeApproveRepo.save(typeApprove);
			if(output!=null) {
				/**Email Notification End**/
				user = userRepository.getByid( typeApprove.getUserId());
				AuditTrail auditTrail = new AuditTrail();
				auditTrail.setFeatureName("Manage Type Approval");
				auditTrail.setSubFeature("Created");
				auditTrail.setFeatureId( typeApprove.getFeatureId());
				auditTrail.setUserId( typeApprove.getUserId() );
				auditTrail.setUserName( user.getUsername());
				auditTrail.setUserType( typeApprove.getUserType());
				auditTrail.setRoleType( typeApprove.getUserType());
				auditTrail.setTxnId( typeApprove.getTxnId() );
				if( Objects.nonNull(typeApprove.getPublicIp()))
					auditTrail.setPublicIp(typeApprove.getPublicIp());
				if( Objects.nonNull(typeApprove.getBrowser()))
					auditTrail.setBrowser(typeApprove.getBrowser());
				auditTrailRepository.save(auditTrail);
				WebActionDb webActionDb = new WebActionDb();
				webActionDb.setFeature(WebActionDbFeature.TYPE_APPROVED.getName());
//				webActionDb.setState(typeApprove.getApproveStatus());
				webActionDb.setState(0);
				webActionDb.setSubFeature("Register");
				webActionDb.setTxnId(typeApprove.getTxnId());
				webActionDbRepository.save(webActionDb);
				return new GenricResponse(200,"Type Approved data has been sucessfully saved",typeApprove.getTxnId(),"REGISTER_TYPE_APPROVE");
			}
			else {
				return new GenricResponse(500,"Type Approved data failed to saved","0","REGISTER_TYPE_APPROVE_FAILED");
			} 
		}
		catch(Exception e) {
			log.info("Exception found ="+e.getMessage());
			log.error(e.getMessage(), e);
			return new GenricResponse(409,"Oops something wrong happened","0","TYPE_APPROVE_ERROR");
		}
	}
	
	public ResponseEntity<?> viewTypeApproveById(long id, long userId, String userType, String publicIp, String browser) {
		List<modelRepoPojo> modelDetails = null;
		List<brandRepoModel> brandDetails = null;
		List<StatesInterpretationDb> states = null;
		log.info("inside type approve view data by Id");
		try {
			log.info("type approve id: "+id);
			TypeApprovedDb output=typeApproveRepo.findById(id);
			if(output==null) {
				HttpResponse response=new HttpResponse();
				response.setResponse("please enter correct Id");
				response.setStatusCode(204);
				return new ResponseEntity<>(response,HttpStatus.OK);
			}
			else {
				modelDetails = modelRepository.findAll();
	            brandDetails = brandRepository.findAll();
	            states = statesInterpretaionRepository.findByFeatureId( output.getFeatureId() );
				for( modelRepoPojo mp : modelDetails ) {
            		if( output.getModelNumber() == mp.getId() )
            			output.setModelNumberInterp( mp.getModelName() );
            	}
            	for( brandRepoModel bm : brandDetails ) {
            		if( bm.getId().equals( output.getProductName() )) {
            			output.setProductNameInterp( bm.getBrand_name() );
            		}
            	}
            	for( StatesInterpretationDb state : states ) {
					if( Objects.nonNull( output.getApproveStatus()) && state.getState().equals( output.getApproveStatus()  )) {
						output.setStateInterp( state.getInterpretation());
					}
					if( Objects.nonNull( output.getAdminApproveStatus()) && state.getState().equals( output.getAdminApproveStatus()  )) {
						output.setAdminStateInterp( state.getInterpretation());
					}
				}
            	User user = userRepository.getByid( userId );
            	AuditTrail auditTrail = new AuditTrail();
				auditTrail.setFeatureName("Manage Type Approval");
				auditTrail.setSubFeature("View");
				auditTrail.setFeatureId( output.getFeatureId());
				auditTrail.setUserId( userId );
				auditTrail.setUserName( user.getUsername() );
				auditTrail.setUserType( userType );
				auditTrail.setRoleType( userType );
				auditTrail.setTxnId( output.getTxnId() );
				auditTrail.setPublicIp(publicIp);
				auditTrail.setBrowser(browser);
				auditTrailRepository.save(auditTrail);
				return new ResponseEntity<>(output,HttpStatus.OK);
			}
		}
		catch(Exception e) {
			log.error(e.getMessage(), e);
			HttpResponse response=new HttpResponse();
			response.setResponse("Oops something wrong happened");
			response.setStatusCode(409);     
			return new ResponseEntity<>(response,HttpStatus.OK);
		}
	
	}
		
	public HttpResponse deleteTypeApprove(Long id, String txnId, long userId, String userType, Integer deleteFlag, String remark,
			String publicIp, String browser) {
		User user = null;
		String status  = null;
		UserProfile userProfile = null;
		String deleteFlagInterp = null;
		TypeApprovedDb typeApprovedDb = null;
		List<StatesInterpretationDb> states = null;
		Map<String, String> placeholders = new HashMap<String, String>();
		List< SystemConfigListDb > deleteFlags = null;
		try 
		{   
			WebActionDb webActionDb = new WebActionDb();
            log.info("type approve id:["+id +"] and txnId: ["+txnId+"], userId:["+userId+"], userType:["+userType+"],"
            		+ " deleteFlag: ["+deleteFlag+"] and remark:["+remark+"]");
            if( Objects.nonNull( id )) {
            	typeApprovedDb = typeApproveRepo.findById(id).get();
            }else {
            	typeApprovedDb = typeApproveRepo.getByTxnId( txnId );
            }
            if( Objects.nonNull( deleteFlag )) {
            	deleteFlags = systemConfigListRepository.findByTag( Tags.DELETE_FLAG,  new Sort(Sort.Direction.ASC, "id") );
            	for( SystemConfigListDb config : deleteFlags) {
            		if( config.getValue().equals( deleteFlag )) {
            			deleteFlagInterp = config.getInterpretation();
            		}
            	}
            }
			if(typeApprovedDb!=null) {
				typeApprovedDb.setDeleteFlag( deleteFlag );
				if(!userType.equalsIgnoreCase("CEIRSystem") ) {
		            states = statesInterpretaionRepository.findByFeatureId( typeApprovedDb.getFeatureId() );
		            for( StatesInterpretationDb state : states ) {
	            		if( userType.equalsIgnoreCase("CEIRAdmin")) {
	            			if( state.getInterpretation().equalsIgnoreCase(Tags.Withdrawn_By_CEIR_Admin)) {
			            		typeApprovedDb.setApproveStatus( state.getState() );
			            		typeApprovedDb.setAdminApproveStatus( state.getState() );
			            		status = Tags.Withdrawn_By_CEIR_Admin;
			            	}
	            		}else {
			            	if( state.getInterpretation().equalsIgnoreCase(Tags.Withdrawn_By_User)) {
			            		typeApprovedDb.setApproveStatus( state.getState() );
			            		typeApprovedDb.setAdminApproveStatus( state.getState() );
			            		status = Tags.Withdrawn_By_User;
			            	}
	            		}
		            }
				}
				if( Objects.nonNull(remark)) {
					typeApprovedDb.setRemark(remark);
				}
				if(userType.equalsIgnoreCase("CEIRAdmin")) {
					typeApprovedDb.setAdminUserId( userId );
					typeApprovedDb.setAdminUserType(userType);
				}
				TypeApprovedDb output=typeApproveRepo.save(typeApprovedDb);
				if(output!=null && !userType.equalsIgnoreCase("CEIRSystem")) {
					TypeApprovedTAC typeApprovedTAC = typeApprovedTACRepository.getByTacAndUserId( typeApprovedDb.getTac(), typeApprovedDb.getUserId() );
					if( typeApprovedTAC !=  null || Objects.nonNull(typeApprovedTAC)) {
						typeApprovedTACRepository.deleteById(typeApprovedTAC.getId());
					}
	            	AuditTrail auditTrail = new AuditTrail();
					auditTrail.setFeatureName("Manage Type Approval");
					auditTrail.setSubFeature("Deleted");
					auditTrail.setFeatureId( output.getFeatureId());
					auditTrail.setUserId( userId );
					auditTrail.setUserType( userType );
					auditTrail.setRoleType( userType );
					auditTrail.setTxnId( output.getTxnId() );
					auditTrail.setPublicIp(publicIp);
					auditTrail.setBrowser(browser);
					if(userType.equalsIgnoreCase("CEIRAdmin") ) {
						user = userRepository.findById(output.getAdminUserId()).get();
						auditTrail.setUserName( user.getUsername());
					}else {
						auditTrail.setUserName( output.getUserForTypeApprove().getUsername());
					}
					auditTrailRepository.save(auditTrail);
					placeholders.put( "<txn_id>", output.getTxnId());
					placeholders.put( "<Txn_Id>", output.getTxnId());
					if( userType.equalsIgnoreCase("CEIRAdmin") ) {
						placeholders.put( "<User first name>", output.getUserForTypeApprove().getUserProfile().getFirstName());
						emailUtil.saveNotificationV2("TypeApproved_Withdrawn_By_Admin", 
								output.getUserForTypeApprove().getUserProfile(), 
								output.getFeatureId(),
								Features.TYPE_APPROVE,
								status,
								output.getTxnId(),
								MailSubjects.SUBJECT,
								placeholders,
								output.getUserType(),
								output.getUserType(),"USERS");
						Generic_Response_Notification genericResponseNotification = userFeignClient.ceirInfoByUserTypeId(8);
						log.info("generic_Response_Notification::::::::"+genericResponseNotification);
						List<RegisterationUser> registerationUserList = genericResponseNotification.getData();
						for(RegisterationUser registerationUser :registerationUserList) {
							userProfile = userProfileRepository.getByUserId(registerationUser.getId());
							placeholders.put( "<User first name>", userProfile.getFirstName());
							emailUtil.saveNotificationV2("TypeApproved_Withdrawn_By_Admin_to_ceir", 
									userProfile, 
									output.getFeatureId(),
									Features.TYPE_APPROVE,
									status,
									output.getTxnId(),
									MailSubjects.SUBJECT,
									placeholders,
									"CEIRAdmin",
									"CEIRAdmin","USERS");
							log.info("Notfication have been saved for CEIR Admin.");
						}
					}else if( !userType.equalsIgnoreCase("CEIRAdmin") && !userType.equalsIgnoreCase("CEIRSystem") ){ 
						placeholders.put( "<User first name>", output.getUserForTypeApprove().getUserProfile().getFirstName());
						emailUtil.saveNotificationV2("TypeApproved_Withdrawn_By_User", 
								output.getUserForTypeApprove().getUserProfile(), 
								output.getFeatureId(),
								Features.TYPE_APPROVE,
								status,
								output.getTxnId(),
								MailSubjects.SUBJECT,
								placeholders,
								output.getUserType(),
								output.getUserType(),"USERS");
					}
					webActionDb.setFeature(WebActionDbFeature.TYPE_APPROVED.getName());
            		webActionDb.setState(0);
					webActionDb.setSubFeature("Delete");
					webActionDb.setTxnId(output.getTxnId());
					webActionDbRepository.save(webActionDb);
					HttpResponse response=new HttpResponse();
					response.setStatusCode(200);
					response.setResponse("Type Approve has been sucessfully withdrawn.");
					response.setTag("TYPE_APPROVE_DELETE_SUCCESS");
					return response;	
				}else if( output!=null && (Objects.nonNull(deleteFlagInterp) && deleteFlagInterp.equalsIgnoreCase(Tags.Processing))) {
					HttpResponse response=new HttpResponse();
					response.setStatusCode(200);
					response.setResponse("Type Approve deletion under processing.");
					response.setTag("TYPE_APPROVE_DELETE_PROCESSING");
					return response;
				}else if( output!=null && (Objects.nonNull(deleteFlagInterp) && deleteFlagInterp.equalsIgnoreCase(Tags.Deleted))) {
					HttpResponse response=new HttpResponse();
					response.setStatusCode(200);
					response.setResponse("Type Approve has been sucessfully marked deleted.");
					response.setTag("TYPE_APPROVE_DELETE_SUCCESS");
					return response;
				}else {
					HttpResponse response=new HttpResponse();
					response.setStatusCode(500);
					response.setResponse("Type approve withdrawn failed.");
					response.setTag("TYPE_APPROVE_DELETE_FAIL");
					return response;
				}
			}
			else {
				HttpResponse response=new HttpResponse();
				response.setStatusCode(204);
				response.setResponse("please enter correct transaction Id.");
				response.setTag("TYPE_APPROVE_WRONG_ID");
				return response;
			} 
		}
		catch(Exception e) {
			log.error(e.getMessage(), e);
			HttpResponse response=new HttpResponse();
			response.setStatusCode(409);
			response.setResponse("Oops something wrong happened");
			response.setTag("TYPE_APPROVE_ERROR");
			return response;
		}
	}
	
	public GenricResponse updateTypeApprove(TypeApprovedDb typeApprove) {
		log.info("inside update type approved db controller");
//		typeApprove.setModifiedOn(new Date());  
		log.info("typeApprove data update typeApprove request:  "+typeApprove.toString());
		List<TypeApprovedAttachedFileInfo> files = null;
		List<StatesInterpretationDb> states = null;
		TypeApprovedDb typeApprovedDb = null;
		TypeApprovedDb output = null;
		int newStatus = 0;
		try 
		{   
			if(typeApprove.getTxnId() != null ) {
				typeApprovedDb = typeApproveRepo.getByTxnId(typeApprove.getTxnId());
			}else {
				typeApprovedDb = typeApproveRepo.findById( typeApprove.getId()).get();
			}
			if(typeApprovedDb==null) {
				return new GenricResponse(204,"please enter correct transaction Id.","0","TYPE_APPROVE_WRONG_ID");
			}
			else {
				if(!typeApprovedDb.getTac().equals(typeApprove.getTac()) ) {
					output = typeApproveRepo.getByTacAndUserId( typeApprove.getTac(), typeApprove.getUserId() );
					if( output !=  null || Objects.nonNull(output)) {
						return new GenricResponse(201,"Type Approved for TAC already exists.","0","REGISTER_TYPE_APPROVE_REJECTED");
					}
				}
	            states = statesInterpretaionRepository.findByFeatureId( typeApprovedDb.getFeatureId() );
				for( StatesInterpretationDb state : states ) {
					if( state.getInterpretation().equalsIgnoreCase(Tags.NEW) ) {
						newStatus = state.getState();
					}
				}
				typeApprove.setId( typeApprovedDb.getId());
				typeApprove.setCreatedOn(typeApprovedDb.getCreatedOn());
				typeApprove.setUserType( typeApprovedDb.getUserType() );
				typeApprove.setApproveStatus( newStatus );
				typeApprove.setAdminApproveStatus( newStatus );
				files = typeApprove.getAttachedFiles();
				if( files != null ) {
					for( TypeApprovedAttachedFileInfo fileInfo : typeApprovedDb.getAttachedFiles()) {
						files.add(fileInfo);
					}
				}else {
					files = typeApprovedDb.getAttachedFiles();
				}
				typeApprove.setAttachedFiles(files);
				output=typeApproveRepo.save(typeApprove);
				User user = userRepository.getByid( output.getUserId());
				AuditTrail auditTrail = new AuditTrail();
				auditTrail.setFeatureName("Manage Type Approval");
				auditTrail.setSubFeature("Updated");
				auditTrail.setFeatureId( output.getFeatureId());
				auditTrail.setUserId( output.getUserId() );
				auditTrail.setUserName( user.getUsername() );
				auditTrail.setUserType( output.getUserType());
				auditTrail.setRoleType( output.getUserType() );
				auditTrail.setTxnId( output.getTxnId());
				if( Objects.nonNull(typeApprove.getPublicIp()))
					auditTrail.setPublicIp(typeApprove.getPublicIp());
				if( Objects.nonNull(typeApprove.getBrowser()))
					auditTrail.setBrowser(typeApprove.getBrowser());
				auditTrailRepository.save(auditTrail);
				WebActionDb webActionDb = new WebActionDb();
				webActionDb.setFeature(WebActionDbFeature.TYPE_APPROVED.getName());
//				webActionDb.setState(typeApprove.getApproveStatus());
        		webActionDb.setState(0);
				webActionDb.setSubFeature("Update");
				webActionDb.setTxnId(typeApprove.getTxnId());
				webActionDbRepository.save(webActionDb);
				/**TypeApprove TAC Maintain DB**/
				if(output!=null) {
					return new GenricResponse(200,"Type Approved data has been successfully updated.",String.valueOf(typeApprove.getTxnId()), "TYPE_APPROVE_UPDATE_SUCCESS");
				}else {
					return new GenricResponse(500,"Type Approved status failed to update.",String.valueOf(typeApprove.getTxnId()),"TYPE_APPROVE_UPDATE_FAIL");
				} 
			}
		}
		catch(Exception e) {
			log.error(e.getMessage(), e);
			return new GenricResponse(409,"Oops something wrong happened","0","UPDATE_ERROR");
		}
	}
	
	public GenricResponse updateTypeApproveRejectStatus(TypeApprovedDb typeApprove) {
		log.info("inside update type approved db controller");
		log.info("typeApprove approve/ reject request:  "+typeApprove.toString());
		Map<String, String> placeholders = new HashMap<String, String>();
		List<StatesInterpretationDb> states = null;
		TypeApprovedDb typeApprovedDb = null;
		UserProfile userProfile = null;
		int processing = 0;
		int approved = 0;
		int rejected = 0;
		int rBySystem = 0;
		int pending  = 0;
		try 
		{   
			if(typeApprove.getTxnId() != null ) {
				typeApprovedDb = typeApproveRepo.getByTxnId(typeApprove.getTxnId());
			}else {
				typeApprovedDb = typeApproveRepo.findById(typeApprove.getId()).get();
			}
			if(typeApprovedDb==null) {
				return new GenricResponse(204,"please enter correct transaction Id","0","TYPE_APPROVE_WRONG_ID");
			}
			else {
				states = statesInterpretaionRepository.findByFeatureId( typeApprovedDb.getFeatureId() );
				typeApprovedDb.setAdminApproveStatus( typeApprove.getAdminApproveStatus());
				if( !typeApprove.getAdminUserType().equalsIgnoreCase("CEIRSystem") ) {
					typeApprovedDb.setRemark( typeApprove.getRemark() );
					typeApprovedDb.setAdminRemark( typeApprove.getAdminRemark());
					typeApprovedDb.setAdminUserId( typeApprove.getAdminUserId());
				}
				typeApprovedDb.setAdminUserType( typeApprove.getAdminUserType());
				for( StatesInterpretationDb state : states ) {
					if( state.getInterpretation().equalsIgnoreCase(Tags.APPROVED) ) {
						approved = state.getState();
					}else if( state.getInterpretation().equalsIgnoreCase(Tags.REGECTED) ) {
						rejected = state.getState();
					}else if( state.getInterpretation().equalsIgnoreCase(Tags.Approved_By_CEIR_Admin) ) {
						approved = state.getState();
					}else if( state.getInterpretation().equalsIgnoreCase(Tags.Rejected_By_CEIR_Admin) ) {
						rejected = state.getState();
					}else if( state.getInterpretation().equalsIgnoreCase(Tags.Pending_From_CEIR_Authority) ) {
						pending = state.getState();
					}else if( state.getInterpretation().equalsIgnoreCase(Tags.Rejected_By_System) ) {
						rBySystem = state.getState();
					}else if( state.getInterpretation().equalsIgnoreCase(Tags.Processing) ) {
						processing = state.getState();
					}
				}
				if( Objects.nonNull(typeApprove.getAdminApproveStatus()) && !typeApprove.getAdminApproveStatus().equals( typeApprovedDb.getApproveStatus())) {
					typeApprove.setApproveStatus( typeApprove.getAdminApproveStatus() );
					typeApprovedDb.setApproveStatus( typeApprove.getAdminApproveStatus() );
				}
				TypeApprovedDb output=typeApproveRepo.save(typeApprovedDb);
				/**TypeApprove TAC Maintain DB**/
				if( output.getApproveStatus().equals( approved ) ) {
					/**URL for deleting entry, if tac exist in pending tac db**/
					TypeApproveDelete typeApproveDelete = new TypeApproveDelete();
					typeApproveDelete.setImporterId(output.getUserId());
					typeApproveDelete.setUserId(output.getUserId());
//					typeApproveDelete.setTxnId( output.getTxnId());
					typeApproveDelete.setTac(output.getTac());
					RestTemplate restTemplate = new RestTemplate();
				    HttpHeaders headers=new HttpHeaders();
				    headers.set("Content-Type", "application/json");
				    HttpEntity requestEntity=new HttpEntity(typeApproveDelete, headers);
				    restTemplate.exchange(systemConfigurationDbRepository.getByTag("tac-delete-url").getValue().replace("$LOCAL_IP", propertiesReader.localIp),
				    		HttpMethod.DELETE,requestEntity,TypeApproveDelete.class);
				    /**URL request control**/
					TypeApprovedTAC typeApprovedTAC = typeApprovedTACRepository.getByTacAndUserId( output.getTac(), output.getUserId() );
				    if(Objects.isNull(typeApprovedTAC)) {
				    	typeApprovedTAC = new TypeApprovedTAC();
				    	typeApprovedTAC.setTac( output.getTac() );
				    	typeApprovedTAC.setTxnId( output.getTxnId() );
				    }
				    typeApprovedTAC.setApproveDisapproveDate( output.getApproveDisapproveDate() );
				    typeApprovedTAC.setCountry( output.getCountry() );
				    typeApprovedTAC.setFrequencyRange( output.getFrequencyRange() );
				    typeApprovedTAC.setManufacturerCountry( output.getManufacturerCountry());
				    typeApprovedTAC.setManufacturerId( output.getManufacturerId());
				    typeApprovedTAC.setManufacturerName( output.getManufacturerName() );
				    typeApprovedTAC.setModelNumber( output.getModelNumber() );
				    typeApprovedTAC.setModelNumber( output.getModelNumber() );
				    typeApprovedTAC.setProductName( output.getProductName() );
				    typeApprovedTAC.setRequestDate( output.getRequestDate() );
				    typeApprovedTAC.setStatus( output.getApproveStatus() );
				    typeApprovedTAC.setTrademark( output.getTrademark());
				    typeApprovedTACRepository.save( typeApprovedTAC );
				}
			    /**TypeApprove TAC Maintain DB**/
				if(output!=null) {
//					log.info("Going to send mail type approves. Admin User type:["+typeApprove.getAdminUserType()+"] and "
//							+ "Type Approve status:["+output.getApproveStatus()+","+rejected+"]");

					placeholders.put( "<txn_id>", typeApprovedDb.getTxnId());
					placeholders.put( "<Txn_Id>", typeApprovedDb.getTxnId());
					if( !typeApprove.getAdminUserType().equalsIgnoreCase("CEIRSystem") ) {
						AuditTrail auditTrail = new AuditTrail();
						auditTrail.setFeatureName("Manage Type Approval");
						auditTrail.setFeatureId( output.getFeatureId());
						auditTrail.setUserType( output.getAdminUserType());
						auditTrail.setRoleType( output.getAdminUserType() );
						auditTrail.setTxnId( output.getTxnId() );
						if( Objects.nonNull(typeApprove.getPublicIp()))
							auditTrail.setPublicIp(typeApprove.getPublicIp());
						if( Objects.nonNull(typeApprove.getBrowser()))
							auditTrail.setBrowser(typeApprove.getBrowser());
						if( Objects.nonNull( output.getAdminUserId() )) {
							User user = userRepository.getByid( output.getAdminUserId());
							auditTrail.setUserId( user.getId() );
							auditTrail.setUserName( user.getUsername());
						}
						WebActionDb webActionDb = new WebActionDb();
						webActionDb.setFeature(WebActionDbFeature.TYPE_APPROVED.getName());
	//					webActionDb.setState(output.getApproveStatus());
						webActionDb.setState(0);
						webActionDb.setSubFeature("Update");
						webActionDb.setTxnId(typeApprove.getTxnId());
						
						/**Email Notification Start **/
						placeholders.put( "<User first name>", typeApprovedDb.getUserForTypeApprove().getUserProfile().getFirstName());
						if( output.getApproveStatus().equals( approved ) ) {
							emailUtil.saveNotificationV2("TypeApproved_Admin_Approved", 
									output.getUserForTypeApprove().getUserProfile(), 
									output.getFeatureId(),
									Features.TYPE_APPROVE,
									Tags.Approved_By_CEIR_Admin,
									output.getTxnId(),
									MailSubjects.SUBJECT,
									placeholders,
									output.getUserType(),
									output.getUserType(),"USERS");
							auditTrail.setSubFeature("Approved");
							webActionDb.setSubFeature("Approved");
						}else if( output.getApproveStatus().equals( rejected ) ){
							TypeApprovedTAC typeApprovedTAC = typeApprovedTACRepository.getByTacAndUserId( typeApprovedDb.getTac(), typeApprovedDb.getUserId() );
							if( typeApprovedTAC !=  null || Objects.nonNull(typeApprovedTAC)) {
								typeApprovedTACRepository.deleteById(typeApprovedTAC.getId());
							}
							placeholders.put("<Reason>", typeApprovedDb.getRemark() );
							emailUtil.saveNotificationV2("TypeApproved_Admin_Rejected", 
									output.getUserForTypeApprove().getUserProfile(), 
									output.getFeatureId(),
									Features.TYPE_APPROVE,
									Tags.Rejected_By_CEIR_Admin,
									output.getTxnId(),
									MailSubjects.SUBJECT,
									placeholders,
									output.getUserType(),
									output.getUserType(),"USERS");
							auditTrail.setSubFeature("Rejected");
							webActionDb.setSubFeature("Rejected");
						}
						auditTrailRepository.save(auditTrail);
						webActionDbRepository.save(webActionDb);
					}else if( typeApprove.getAdminUserType().equalsIgnoreCase("CEIRSystem") && output.getApproveStatus().equals(pending)) {
						placeholders.put( "<User first name>", typeApprovedDb.getUserForTypeApprove().getUserProfile().getFirstName());
						emailUtil.saveNotificationV2("type_approvel_pending_user", 
								output.getUserForTypeApprove().getUserProfile(), 
								output.getFeatureId(),
								Features.TYPE_APPROVE,
								"Pending from CEIR Authority",
								output.getTxnId(),
								MailSubjects.SUBJECT,
								placeholders,
								output.getUserType(),
								output.getUserType(),"USERS");
						Generic_Response_Notification genericResponseNotification = userFeignClient.ceirInfoByUserTypeId(8);
						log.info("generic_Response_Notification::::::::"+genericResponseNotification);
						List<RegisterationUser> registerationUserList = genericResponseNotification.getData(); 
						for(RegisterationUser registerationUser :registerationUserList) { 
							userProfile = userProfileRepository.getByUserId(registerationUser.getId());
							placeholders.put( "<User first name>", userProfile.getFirstName());
							emailUtil.saveNotificationV2("type_approvel_pending_ceir", userProfile,
									output.getFeatureId(),
									Features.TYPE_APPROVE,
									"Pending from CEIR Authority",
									output.getTxnId(),
									MailSubjects.SUBJECT,
									placeholders,
									output.getUserType(),
									"CEIRAdmin",
									"USERS");
							log.info("Notfication have been saved for CEIR Admin."); 
						}
					}else if( typeApprove.getAdminUserType().equalsIgnoreCase("CEIRSystem") && output.getApproveStatus().equals(rBySystem)) {
						TypeApprovedTAC typeApprovedTAC = typeApprovedTACRepository.getByTacAndUserId( typeApprovedDb.getTac(), typeApprovedDb.getUserId() );
						if( typeApprovedTAC !=  null || Objects.nonNull(typeApprovedTAC)) {
							typeApprovedTACRepository.deleteById(typeApprovedTAC.getId());
						}
						placeholders.put( "<User first name>", typeApprovedDb.getUserForTypeApprove().getUserProfile().getFirstName());
						emailUtil.saveNotificationV2("type_approval_rejected_by_ceir_system", 
								output.getUserForTypeApprove().getUserProfile(), 
								output.getFeatureId(),
								Features.TYPE_APPROVE,
								"Rejected by system",
								output.getTxnId(),
								MailSubjects.SUBJECT,
								placeholders,
								output.getUserType(),
								output.getUserType(),"USERS");
					}
					/**Email Notification End**/
					if(typeApprove.getAdminApproveStatus().equals( approved )) {
						return new GenricResponse(200,"Type Approved has been approved by CEIR Admin.",String.valueOf(typeApprove.getTxnId()), "TYPE_APPROVE_APPROVED");
					}else if(typeApprove.getApproveStatus().equals(pending) || typeApprove.getApproveStatus().equals(processing)){
						return new GenricResponse(200,"Type Approved has been changed by CEIRSystem.",String.valueOf(typeApprove.getTxnId()), "TYPE_APPROVE_APPROVED");
					}else {
						return new GenricResponse(200,"Type Approved has been rejected by CEIR Admin.",String.valueOf(typeApprove.getTxnId()), "TYPE_APPROVE_REJECTED");
					}
				}
				else {
					return new GenricResponse(500,"Type Approved status failed to update",String.valueOf(typeApprove.getTxnId()),"TYPE_APPROVE_UPDATE_FAIL");
				} 
			}
		}
		catch(Exception e) {
			log.error(e.getMessage(), e);
			return new GenricResponse(409,"Oops something wrong happened","0","TYPE_APPROVE_ERROR");
		}
	}
	
	public Page<TypeApprovedDb>  viewTypeApprovdeData(TypeApproveFilter filterRequest, Integer pageNo, Integer pageSize){
		Pageable pageable = null;
		boolean isDefaultFilter = this.typeApproveDefaultFilter(filterRequest);
		Page<TypeApprovedDb> results = null;
		List<modelRepoPojo> modelDetails = null;
		List<brandRepoModel> brandDetails = null;
		List<StatesInterpretationDb> states = null;
		log.info("inside type approve db view  data controller");
		try {
             log.info("TypeApproveFilter data: "+filterRequest );
             log.info("pageNo= "+pageNo +"and pageSize="+pageSize);
             Sort.Direction direction;
 			if ( Objects.nonNull(filterRequest.getOrder()) && filterRequest.getOrder().equalsIgnoreCase("asc") ) {
 				direction = Sort.Direction.ASC;
 			} else {
 				direction = Sort.Direction.DESC;				
 			}
// 			log.info("Order column name:["+filterRequest.getOrderColumnName()+"]");
 			if( Objects.nonNull( filterRequest.getOrderColumnName()) 
 					&& Objects.nonNull(TypeApproveDBOrderColumnMapping.getColumnMapping(filterRequest.getOrderColumnName()))) {
 				pageable = PageRequest.of(pageNo, pageSize,
 						new Sort(direction, TypeApproveDBOrderColumnMapping.getColumnMapping(filterRequest.getOrderColumnName()).name()));
 			} else {
 				pageable = PageRequest.of(pageNo, pageSize, new Sort(direction, "modifiedOn"));
 			}
             modelDetails = modelRepository.findAll();
             brandDetails = brandRepository.findAll();

             GenericSpecificationBuilder<TypeApprovedDb> uPSB = new GenericSpecificationBuilder<TypeApprovedDb>(propertiesReader.dialect);
			
			if(Objects.nonNull(filterRequest.getUserId()) && filterRequest.getUserId() != 0 && (Objects.nonNull(filterRequest.getUserType()) 
					&& !filterRequest.getUserType().equalsIgnoreCase("CEIRAdmin")
					&& !filterRequest.getUserType().equalsIgnoreCase("TRC"))) 
				uPSB.with(new SearchCriteria("userId",filterRequest.getUserId(), SearchOperation.EQUALITY, Datatype.LONG));

			if(Objects.nonNull(filterRequest.getStartDate()) && !filterRequest.getStartDate().equals("")) {
				uPSB.with(new SearchCriteria("createdOn",filterRequest.getStartDate(), SearchOperation.GREATER_THAN, Datatype.DATE));
			}
			
			if(Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().equals("")) {
				uPSB.with(new SearchCriteria("createdOn",filterRequest.getEndDate(), SearchOperation.LESS_THAN, Datatype.DATE));
			}

			if(Objects.nonNull(filterRequest.getModifiedOn()) && !filterRequest.getModifiedOn().equals("")) {
				uPSB.with(new SearchCriteria("modifiedOn",filterRequest.getModifiedOn(), SearchOperation.EQUALITY, Datatype.DATE));
			}

			if(Objects.nonNull(filterRequest.getTac()) && !filterRequest.getTac().equals("")) {
				uPSB.with(new SearchCriteria("tac",filterRequest.getTac(), SearchOperation.LIKE, Datatype.STRING));
			}
			
			if(Objects.nonNull(filterRequest.getTxnId()) && !filterRequest.getTxnId().equals("")) {
				uPSB.with(new SearchCriteria("txnId",filterRequest.getTxnId(), SearchOperation.LIKE, Datatype.STRING));
			}
			
			if(Objects.nonNull(filterRequest.getUserType()) && !filterRequest.getUserType().equals("") 
					&& !filterRequest.getUserType().equalsIgnoreCase("CEIRAdmin")) {
				uPSB.with(new SearchCriteria("userType",filterRequest.getUserType(), SearchOperation.EQUALITY, Datatype.STRING));
			}
			
			if(Objects.nonNull(filterRequest.getModelNumber()) && filterRequest.getModelNumber() !=-1)
				uPSB.with(new SearchCriteria("modelNumber",filterRequest.getModelNumber(), SearchOperation.EQUALITY, Datatype.INT));
			
			if(Objects.nonNull(filterRequest.getProductName()) && !filterRequest.getProductName().equals(-1))
				uPSB.with(new SearchCriteria("productName",filterRequest.getProductName(), SearchOperation.EQUALITY, Datatype.LONG));
			
			if(Objects.nonNull(filterRequest.getDisplayName()) && !filterRequest.getDisplayName().equals("")) {
				uPSB.with(new SearchCriteria("userForTypeApprove-userProfile-displayName", filterRequest.getDisplayName(),
						SearchOperation.LIKE, Datatype.STRING));
			}
			
			if(Objects.nonNull(filterRequest.getCountryName()) && !filterRequest.getCountryName().equals("")) {
				uPSB.with(new SearchCriteria("manufacturerCountry",filterRequest.getCountryName(), SearchOperation.LIKE, Datatype.STRING));
			}
			
			if(Objects.nonNull(filterRequest.getFeatureId())) {
				uPSB.with(new SearchCriteria("featureId",filterRequest.getFeatureId(), SearchOperation.EQUALITY, Datatype.LONG));
				states = statesInterpretaionRepository.findByFeatureId( filterRequest.getFeatureId() );
			}			
			if(Objects.nonNull(filterRequest.getFilterUserType()) && !filterRequest.getFilterUserType().equals("")) {
				uPSB.with(new SearchCriteria("userType", filterRequest.getFilterUserType(), SearchOperation.LIKE, Datatype.STRING));
			}
			if(Objects.nonNull(filterRequest.getTrademark()) && !filterRequest.getTrademark().equals("")) {
				uPSB.with(new SearchCriteria("trademark", filterRequest.getTrademark(), SearchOperation.LIKE, Datatype.STRING));
			}
			
			if(Objects.nonNull(filterRequest.getStatus()) && !filterRequest.getStatus().equals(-1))
				uPSB.with(new SearchCriteria("approveStatus",filterRequest.getStatus(), SearchOperation.EQUALITY, Datatype.INT));
			else if( isDefaultFilter ) {
				List<DashboardUsersFeatureStateMap> userDefaultStates = dufsmRepo.
						findByUserTypeIdAndFeatureId(filterRequest.getUserTypeId(), filterRequest.getFeatureId());
				List<Integer> allStates = new ArrayList<Integer>();
				for( DashboardUsersFeatureStateMap state: userDefaultStates) {
					allStates.add( state.getState());
				}
				uPSB.addSpecification( uPSB.in( "approveStatus", allStates));
			}
            results =  typeApproveRepo.findAll(uPSB.build(),pageable);
            log.info("TypeApprovedDb filter data size:["+results.getNumberOfElements()+"]");
            for( TypeApprovedDb result : results ) {
            	for( modelRepoPojo mp : modelDetails ) {
            		if( result.getModelNumber() == mp.getId() )
            			result.setModelNumberInterp( mp.getModelName() );
            	}
            	for( brandRepoModel bm : brandDetails ) {
            		if( bm.getId().equals( result.getProductName() )) {
            			result.setProductNameInterp( bm.getBrand_name() );
            		}
            	}
            	for( StatesInterpretationDb state : states ) {
					if( Objects.nonNull( result.getApproveStatus()) && state.getState().equals( result.getApproveStatus()  )) {
						result.setStateInterp( state.getInterpretation());
					}
					if( Objects.nonNull( result.getAdminApproveStatus()) && state.getState().equals( result.getAdminApproveStatus()  )) {
						result.setAdminStateInterp( state.getInterpretation());
					}
				}
            	result.setUserDisplayName( result.getUserForTypeApprove().getUserProfile().getDisplayName());
            }
            AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName("Manage Type Approval");
			if( !isDefaultFilter )
				auditTrail.setSubFeature("Filter");
			else
				auditTrail.setSubFeature("View All");
			auditTrail.setFeatureId( filterRequest.getFeatureId());
            if( Objects.nonNull(filterRequest.getUserId()) && filterRequest.getUserId() != 0 ) {
            	User user = userRepository.getByid( filterRequest.getUserId());
				auditTrail.setUserId( user.getId() );
				auditTrail.setUserName( user.getUsername());
            }else {
            	auditTrail.setUserName( "NA" );
            }
            if( Objects.nonNull(filterRequest.getUserType()) ) {
    			auditTrail.setUserType( filterRequest.getUserType());
    			auditTrail.setRoleType( filterRequest.getUserType() );
            }else {
            	auditTrail.setUserType("NA");
    			auditTrail.setRoleType("NA");
            }
            if( Objects.nonNull(filterRequest.getPublicIp()))
				auditTrail.setPublicIp(filterRequest.getPublicIp());
			if( Objects.nonNull(filterRequest.getBrowser()))
				auditTrail.setBrowser(filterRequest.getBrowser());
//            if( !Objects.nonNull(filterRequest.getTxnId()) || filterRequest.getTxnId().isEmpty())
            	auditTrail.setTxnId("NA");
			auditTrailRepository.save(auditTrail);
            return results;

		} catch (Exception e) {
			log.info("Exception found ="+e.getMessage());
			log.error(e.getMessage(), e);
			return null;
		}
	}

	public boolean typeApproveDefaultFilter( TypeApproveFilter filterRequest ) {
		try {
			if(Objects.nonNull(filterRequest.getStartDate()) && !filterRequest.getStartDate().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(filterRequest.getModifiedOn()) && !filterRequest.getModifiedOn().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(filterRequest.getStatus()) && !filterRequest.getStatus().equals(-1))
				return Boolean.FALSE;
			if(Objects.nonNull(filterRequest.getTac()) && !filterRequest.getTac().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(filterRequest.getTxnId()) && !filterRequest.getTxnId().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(filterRequest.getModelNumber()) && filterRequest.getModelNumber() !=-1)
				return Boolean.FALSE;
			if(Objects.nonNull(filterRequest.getProductName()) && !filterRequest.getProductName().equals(-1))
				return Boolean.FALSE;
			if(Objects.nonNull(filterRequest.getFilterUserType()) && !filterRequest.getFilterUserType().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(filterRequest.getDisplayName()) && !filterRequest.getDisplayName().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(filterRequest.getCountryName()) && !filterRequest.getCountryName().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(filterRequest.getTrademark()) && !filterRequest.getTrademark().equals(""))
				return Boolean.FALSE;
		}catch( Exception ex) {
			log.error(ex.getMessage(), ex);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	public FileDetails getFilterTACInFileV2(TypeApproveFilter filterRequest, Integer pageNo, Integer pageSize) {
		String fileName = null;
		String attachedFiles   = "";
		Writer writer   = null;
		TypeApproveFileModel tfm = null;
		DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter dtf2  = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		String filePath = systemConfigurationDbRepository.getByTag("file.download-dir").getValue();
		StatefulBeanToCsvBuilder<TypeApproveFileModel> builder = null;
		StatefulBeanToCsv<TypeApproveFileModel> csvWriter      = null;
		List< TypeApproveFileModel> fileRecords                = null;
		CustomMappingStrategy<TypeApproveFileModel> mappingStrategy = new CustomMappingStrategy<TypeApproveFileModel>();
		try {
			pageNo = 0;
			pageSize = Integer.valueOf(systemConfigurationDbRepository.getByTag("file.max-file-record").getValue());
			List<TypeApprovedDb> typeApproveList = this.viewTypeApprovdeData(filterRequest, pageNo, pageSize).getContent();
			fileName = LocalDateTime.now().format(dtf2).replace(" ", "_")+"_TACsInfo.csv";
			writer = Files.newBufferedWriter(Paths.get(filePath+fileName));
			mappingStrategy.setType(TypeApproveFileModel.class);
			builder = new StatefulBeanToCsvBuilder<TypeApproveFileModel>(writer);
//			csvWriter = builder.withMappingStrategy(mappingStrategy).withSeparator(',').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
			csvWriter = builder.withMappingStrategy(mappingStrategy).withSeparator(',').withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER).build();
			if( typeApproveList.size() > 0 ) {
				fileRecords = new ArrayList<TypeApproveFileModel>();
				for( TypeApprovedDb tad : typeApproveList ) {
					tfm = new TypeApproveFileModel();
					if( Objects.nonNull(tad.getCountry() )) {
						tfm.setCountry( tad.getCountry());
					}else {
						tfm.setCountry( tad.getManufacturerCountry());
					}
					tfm.setTac( tad.getTac());
					tfm.setStatus( tad.getStateInterp());
					tfm.setTxnId( tad.getTxnId());
					tfm.setCreatedOn( tad.getCreatedOn().format(dtf));
					tfm.setModelNumber( tad.getModelNumberInterp() );
					tfm.setProductName( tad.getProductNameInterp());
					tfm.setTrandemark( tad.getTrademark() );
					attachedFiles   = "";
					for( TypeApprovedAttachedFileInfo attachedFileInfo : tad.getAttachedFiles()) {
						attachedFiles += attachedFileInfo.getFileName()+"|";
					}
					if( !attachedFiles.equals("") )
						attachedFiles = attachedFiles.substring( 0, attachedFiles.length() - 1);
					tfm.setFileName( attachedFiles );
					fileRecords.add(tfm);
				}
				csvWriter.write(fileRecords);
			}else {
				csvWriter.write( new TypeApproveFileModel());
			}
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName("Manage Type Approval");
			auditTrail.setSubFeature("Export");
			auditTrail.setFeatureId( filterRequest.getFeatureId());
            if( Objects.nonNull(filterRequest.getUserId()) && filterRequest.getUserId() != 0 ) {
            	User user = userRepository.getByid( filterRequest.getUserId());
				auditTrail.setUserId( user.getId() );
				auditTrail.setUserName( user.getUsername());
            }else {
            	auditTrail.setUserName( "NA" );
            }
            if( Objects.nonNull(filterRequest.getUserType()) ) {
    			auditTrail.setUserType( filterRequest.getUserType());
    			auditTrail.setRoleType( filterRequest.getUserType() );
            }else {
            	auditTrail.setUserType("NA");
    			auditTrail.setRoleType("NA");
            }
            if( Objects.nonNull(filterRequest.getPublicIp()))
				auditTrail.setPublicIp(filterRequest.getPublicIp());
			if( Objects.nonNull(filterRequest.getBrowser()))
				auditTrail.setBrowser(filterRequest.getBrowser());
//            if( !Objects.nonNull(filterRequest.getTxnId()) || filterRequest.getTxnId().isEmpty())
            	auditTrail.setTxnId("NA");
			auditTrailRepository.save(auditTrail);
//			return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("file.download-link").getValue()+fileName );
			return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("file.download-link").getValue().replace("$LOCAL_IP",
					propertiesReader.localIp)+fileName );
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			try {

				if( writer != null )
					writer.close();
			} catch (IOException e) {}
		}
	}
	
	public FileDetails getFilterTACInFileCEIRAdmin(TypeApproveFilter filterRequest, Integer pageNo, Integer pageSize) {
		String fileName = null;
		String attachedFiles   = "";
		Writer writer   = null;
		TypeApproveCEIRFileModel tfm = null;
		DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter dtf2  = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		String filePath = systemConfigurationDbRepository.getByTag("file.download-dir").getValue();
		StatefulBeanToCsvBuilder<TypeApproveCEIRFileModel> builder = null;
		StatefulBeanToCsv<TypeApproveCEIRFileModel> csvWriter      = null;
		List< TypeApproveCEIRFileModel> fileRecords                = null;
		CustomMappingStrategy<TypeApproveCEIRFileModel> mappingStrategy = new CustomMappingStrategy<TypeApproveCEIRFileModel>();
		try {
			pageNo = 0;
			pageSize = Integer.valueOf(systemConfigurationDbRepository.getByTag("file.max-file-record").getValue());
			List<TypeApprovedDb> typeApproveList = this.viewTypeApprovdeData(filterRequest, pageNo, pageSize).getContent();
			fileName = LocalDateTime.now().format(dtf2).replace(" ", "_")+"_TACsInfo.csv";
			writer = Files.newBufferedWriter(Paths.get(filePath+fileName));
			mappingStrategy.setType(TypeApproveCEIRFileModel.class);
			builder = new StatefulBeanToCsvBuilder<TypeApproveCEIRFileModel>(writer);
			csvWriter = builder.withMappingStrategy(mappingStrategy).withSeparator(',').withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER).build();
			if( typeApproveList.size() > 0 ) {
				fileRecords = new ArrayList<TypeApproveCEIRFileModel>();
				for( TypeApprovedDb tad : typeApproveList ) {
					tfm = new TypeApproveCEIRFileModel();
					tfm.setCreatedOn( tad.getCreatedOn().format(dtf));
					tfm.setModifiedOn( tad.getModifiedOn().format(dtf));
					tfm.setTxnId( tad.getTxnId());
					tfm.setDisplayName( tad.getUserDisplayName() );
					tfm.setTac( tad.getTac());
					tfm.setProductName( tad.getProductNameInterp());
					tfm.setModelNumber( tad.getModelNumberInterp() );
					if( Objects.nonNull(tad.getCountry() )) {
						tfm.setCountry( tad.getCountry());
					}else {
						tfm.setCountry( tad.getManufacturerCountry());
					}
					tfm.setUserType( tad.getUserType() );
					tfm.setStatus( tad.getStateInterp());
					attachedFiles = "";
					for( TypeApprovedAttachedFileInfo attachedFileInfo : tad.getAttachedFiles()) {
						attachedFiles += attachedFileInfo.getFileName()+"|";
					}
					if( !attachedFiles.equals("") )
						attachedFiles = attachedFiles.substring( 0, attachedFiles.length() - 1);
					tfm.setFileName( attachedFiles );
					fileRecords.add(tfm);
				}
				csvWriter.write(fileRecords);
			}else {
				csvWriter.write( new TypeApproveCEIRFileModel());
			}
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName("Manage Type Approval");
			auditTrail.setSubFeature("Export");
			auditTrail.setFeatureId( filterRequest.getFeatureId());
            if( Objects.nonNull(filterRequest.getUserId()) && filterRequest.getUserId() != 0 ) {
            	User user = userRepository.getByid( filterRequest.getUserId());
				auditTrail.setUserId( user.getId() );
				auditTrail.setUserName( user.getUsername());
            }else {
            	auditTrail.setUserName( "NA" );
            }
            if( Objects.nonNull(filterRequest.getUserType()) ) {
    			auditTrail.setUserType( filterRequest.getUserType());
    			auditTrail.setRoleType( filterRequest.getUserType() );
            }else {
            	auditTrail.setUserType("NA");
    			auditTrail.setRoleType("NA");
            }
            if( Objects.nonNull(filterRequest.getPublicIp()))
				auditTrail.setPublicIp(filterRequest.getPublicIp());
			if( Objects.nonNull(filterRequest.getBrowser()))
				auditTrail.setBrowser(filterRequest.getBrowser());
//            if( !Objects.nonNull(filterRequest.getTxnId()) || filterRequest.getTxnId().isEmpty())
            	auditTrail.setTxnId("NA");
			auditTrailRepository.save(auditTrail);
			return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("file.download-link").getValue().replace("$LOCAL_IP",
					propertiesReader.localIp)+fileName );
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			try {

				if( writer != null )
					writer.close();
			} catch (IOException e) {}
		}
	}
	
	
}



