package com.gl.ceir.config.service.impl;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.EmailSender.EmailUtil;
import com.gl.ceir.config.EmailSender.MailSubjects;
import com.gl.ceir.config.configuration.FileStorageProperties;
import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.feign.UserFeignClient;
import com.gl.ceir.config.model.app.AttachedFileInfo;
import com.gl.ceir.config.model.app.DashboardUsersFeatureStateMap;
import com.gl.ceir.config.model.app.EndUserGrievance;
import com.gl.ceir.config.model.app.FileDetails;
import com.gl.ceir.config.model.app.Grievance;
import com.gl.ceir.config.model.file.GrievanceFileModel;
import com.gl.ceir.config.model.file.GrievanceCEIRFileModel;
import com.gl.ceir.config.model.app.GrievanceFilterRequest;
import com.gl.ceir.config.model.app.GrievanceGenricResponse;
import com.gl.ceir.config.model.app.GrievanceHistory;
import com.gl.ceir.config.model.app.GrievanceMsg;
import com.gl.ceir.config.model.app.GrievanceMsgWithUser;
import com.gl.ceir.config.model.app.GrievanceReply;
import com.gl.ceir.config.model.app.SearchCriteria;
import com.gl.ceir.config.model.app.StatesInterpretationDb;
import com.gl.ceir.config.model.app.SystemConfigListDb;
import com.gl.ceir.config.model.app.User;
import com.gl.ceir.config.model.app.UserProfile;
import com.gl.ceir.config.model.app.Usertype;
import com.gl.ceir.config.model.aud.AuditTrail;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.Features;
import com.gl.ceir.config.model.constants.GrievanceOrderColumnMapping;
import com.gl.ceir.config.model.constants.GrievanceStatus;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.model.constants.Tags;
import com.gl.ceir.config.repository.app.AttachedFileInfoRepository;
import com.gl.ceir.config.repository.app.DashboardUsersFeatureStateMapRepository;
import com.gl.ceir.config.repository.app.GrievanceHistoryRepository;
import com.gl.ceir.config.repository.app.GrievanceMsgRepository;
import com.gl.ceir.config.repository.app.GrievanceRepository;
import com.gl.ceir.config.repository.app.StatesInterpretaionRepository;
import com.gl.ceir.config.repository.app.SystemConfigurationDbRepository;
import com.gl.ceir.config.repository.app.UserProfileRepository;
import com.gl.ceir.config.repository.app.UserRepository;
import com.gl.ceir.config.repository.app.UsertypeRepo;
import com.gl.ceir.config.repository.app.WebActionDbRepository;
import com.gl.ceir.config.repository.aud.AuditTrailRepository;
import com.gl.ceir.config.request.model.Generic_Response_Notification;
import com.gl.ceir.config.request.model.RegisterationUser;
import com.gl.ceir.config.specificationsbuilder.GenericSpecificationBuilder;
import com.gl.ceir.config.specificationsbuilder.GrievanceHistorySpecificationBuilder;
import com.gl.ceir.config.specificationsbuilder.GrievanceSpecificationBuilder;
import com.gl.ceir.config.util.CustomMappingStrategy;

import com.opencsv.CSVWriter;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class GrievanceServiceImpl{

	private static final Logger logger = LogManager.getLogger(GrievanceServiceImpl.class);
	@Autowired
	GrievanceRepository grievanceRepository;
	@Autowired
	GrievanceMsgRepository grievanceMsgRepository;
	@Autowired
	GrievanceHistoryRepository grievanceHistoryRepository;
	@Autowired
	WebActionDbRepository webActionDbRepository;
	@Autowired
	PropertiesReader propertiesReader;
	@Autowired
	FileStorageProperties fileStorageProperties;
	@Autowired
	ConfigurationManagementServiceImpl configurationManagementServiceImpl;
	@Autowired	
	EmailUtil emailUtil;
	@Autowired
	SystemConfigurationDbRepository systemConfigurationDbRepository;
	@Autowired
	AttachedFileInfoRepository attachedFileInfoRepository;
	@Autowired
	AuditTrailRepository auditTrailRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	StatesInterpretaionRepository statesInterpretaionRepository;
	@Autowired
	UsertypeRepo usertypeRepo;
	@Autowired
	DashboardUsersFeatureStateMapRepository dashboardUsersFeatureStateMapRepository;
	@Autowired
	UserFeignClient userFeignClient;
	@Autowired
	UserProfileRepository userProfileRepository;
	
	@Transactional
	public GrievanceGenricResponse save(Grievance grievance) {
		Map<String, String> placeholders = new HashMap<String, String>();
		List<StatesInterpretationDb> states = null;
		UserProfile userProfile = null;
		User user  = null;
		int status = 0;
		try {
			states = statesInterpretaionRepository.findByFeatureId( grievance.getFeatureId() );
            for( StatesInterpretationDb state : states ) {
            	if( state.getInterpretation().trim().equalsIgnoreCase(Tags.NEW))
            		status = state.getState();
            }
			grievance.setGrievanceStatus( status );
			if( Objects.isNull(grievance.getTxnId()) || grievance.getTxnId().isEmpty())
				grievance.setTxnId("NA");
			logger.info("New Grievance:["+grievance.toString()+"]");
			Grievance newGrievance = grievanceRepository.save(grievance);
			GrievanceMsg grievanceMsg = new GrievanceMsg();
			grievanceMsg.setGrievanceId( grievance.getGrievanceId() );
			grievanceMsg.setUserId( grievance.getUserId() );
			grievanceMsg.setUserType( grievance.getUserType());
			grievanceMsg.setReply( grievance.getRemarks());
			for( AttachedFileInfo fileInfo : grievance.getAttachedFiles() ) {
				fileInfo.setGrievanceId( grievanceMsg.getGrievanceId() );
			}
			grievanceMsg.setAttachedFiles(grievance.getAttachedFiles());
			logger.info("before saving grievance---------"+grievanceMsg.toString());
			grievanceMsg = grievanceMsgRepository.save(grievanceMsg);
			logger.info("after save grievance");
			logger.error("Grievance message ID="+grievanceMsg.getId()+" and Grievance Id ="+grievanceMsg.getGrievanceId());
			/**Email Notification Start **/
			placeholders.put( "<txn_id>", grievance.getGrievanceId());
			placeholders.put( "<Txn_Id>", grievance.getGrievanceId());
			Generic_Response_Notification genericResponseNotification = userFeignClient.ceirInfoByUserTypeId(8);
			logger.info("generic_Response_Notification::::::::"+genericResponseNotification);
			List<RegisterationUser> registerationUserList = genericResponseNotification.getData();
			for(RegisterationUser registerationUser :registerationUserList) {
				userProfile = userProfileRepository.getByUserId(registerationUser.getId());
				placeholders.put( "<User first name>", userProfile.getFirstName());
				emailUtil.saveNotificationV2("New_Grievance", 
					    userProfile, 
						grievance.getFeatureId(),
						Features.GRIEVANCE,
						SubFeatures.REPLY,
						grievance.getGrievanceId(),
						MailSubjects.SUBJECT,
						placeholders,
						"CEIRAdmin",
						"CEIRAdmin", "USERS");
				logger.info("Notfication have been saved for CEIR Admin.");
			}
			/**Email Notification End**/
			user = userRepository.getByid( grievance.getUserId());
			logger.info("user details --------"+user);
			logger.info("user id+++++++++++++"+user.getId());
			placeholders.put( "<User first name>", user.getUserProfile().getFirstName());
			emailUtil.saveNotificationV2("New_Grievance_User", 
					user.getUserProfile(), 
					grievance.getFeatureId(),
					Features.GRIEVANCE,
					SubFeatures.REPLY,
					grievance.getGrievanceId(),
					MailSubjects.SUBJECT,
					placeholders,
					grievance.getUserType(),
					grievance.getUserType(), "USERS");
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName("Grievance Management");
			auditTrail.setSubFeature("Created");
			auditTrail.setFeatureId( grievance.getFeatureId());
			if( Objects.nonNull(grievance.getPublicIp()))
				logger.info("111111");
				auditTrail.setPublicIp(grievance.getPublicIp());
			if( Objects.nonNull(grievance.getBrowser()))
				logger.info("222222");
				auditTrail.setBrowser(grievance.getBrowser());
			if( Objects.nonNull( grievance.getRaisedByUserId() )) {
				logger.info("333333");
				user = userRepository.getByid( grievance.getRaisedByUserId());
				auditTrail.setUserType( grievance.getRaisedByUserType());
				auditTrail.setRoleType( grievance.getRaisedByUserType() );
			}else {
				logger.info("44444");
				user = grievance.getUser();
				logger.info("@@@@@@@@@@@@@@@"+grievance.getUser());
				auditTrail.setUserType( grievance.getUserType());
				auditTrail.setRoleType( grievance.getUserType() );
			}
			logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11"+user);
			logger.info("user id========"+user.getId());
			auditTrail.setUserId(user.getId());
			auditTrail.setUserName(user.getUsername());
			auditTrail.setTxnId(grievance.getGrievanceId());
			logger.info("Audit trail:["+auditTrail.toString()+"]");
			auditTrailRepository.save(auditTrail);
			return new GrievanceGenricResponse(0,"Grievance registered successfuly",grievance.getGrievanceId());

		}catch (Exception e) {
			logger.error("Grievance Registration failed="+e.getMessage());
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	
	@Transactional
	public GrievanceGenricResponse saveEndUserGrievance(EndUserGrievance endUserGrievance) {
		Map< String, String > placeholders = new HashMap< String, String >();
		List<StatesInterpretationDb> states = null;
		UserProfile userProfile = null;
		int status = 0;
		try {
			
			states = statesInterpretaionRepository.findByFeatureId( endUserGrievance.getFeatureId() );
            for( StatesInterpretationDb state : states ) {
            	if( state.getInterpretation().trim().equalsIgnoreCase(Tags.NEW))
            		status = state.getState();
            }
			Usertype usertype = usertypeRepo.findByUserTypeName( endUserGrievance.getUserType());
			User user = User.getDefaultUser();
			user.setUsertype(usertype);
			userProfile = UserProfile.getDefaultUserProfile();
			userProfile.setFirstName( endUserGrievance.getFirstName());
			userProfile.setMiddleName( endUserGrievance.getMiddleName() );
			userProfile.setLastName( endUserGrievance.getLastName() );
			userProfile.setPhoneNo( endUserGrievance.getPhoneNo() );
			userProfile.setEmail(endUserGrievance.getEmail());
			userProfile.setDistrict("NA");
			userProfile.setCommune("NA");
			userProfile.setVillage("NA");
			userProfile.setUser(user);
			user.setUserProfile(userProfile);
			user = userRepository.save(user);
			
			logger.info("User Id:"+user.getId());
			

			Grievance grievance = new Grievance();
			grievance.setGrievanceStatus( status );
			grievance.setCategoryId( endUserGrievance.getCategoryId() );
			grievance.setTxnId( endUserGrievance.getTxnId() );
			grievance.setGrievanceId( endUserGrievance.getGrievanceId() );
			grievance.setUserId( user.getId() );
			grievance.setUserType( endUserGrievance.getUserType() );
			grievance.setAttachedFiles( endUserGrievance.getAttachedFiles() );
			grievance.setRemarks( endUserGrievance.getRemarks() );
			grievance.setRaisedBy( endUserGrievance.getRaisedBy() );
			grievance.setRaisedByUserId( endUserGrievance.getRaisedByUserId());
			grievance.setRaisedByUserType( endUserGrievance.getRaisedByUserType());
			logger.info("New Grievance:["+grievance.toString()+"]");
			grievance = grievanceRepository.save(grievance);
			
			
//			webActionDbRepository.save(webActionDb);
			GrievanceMsg grievanceMsg = new GrievanceMsg();
			grievanceMsg.setGrievanceId( grievance.getGrievanceId() );
			grievanceMsg.setUserId( grievance.getUserId() );
			grievanceMsg.setUserType( grievance.getUserType());
			grievanceMsg.setReply( grievance.getRemarks());
			grievanceMsg.setAttachedFiles(endUserGrievance.getAttachedFiles());
			grievanceMsg = grievanceMsgRepository.save(grievanceMsg);
			logger.error("Grievance message ID="+grievanceMsg.getId()+" and Grievance Id ="+grievanceMsg.getGrievanceId());
	
			/**Email Notification Start **/
			placeholders.put( "<User first name>", user.getUserProfile().getFirstName());
			placeholders.put( "<txn_id>", grievance.getGrievanceId());
			placeholders.put( "<Txn_Id>", grievance.getGrievanceId());
			emailUtil.saveNotificationV2("New_Grievance_User", 
					user.getUserProfile(), 
					grievance.getFeatureId(),
					Features.GRIEVANCE,
					SubFeatures.REPLY,
					grievance.getGrievanceId(),
					MailSubjects.SUBJECT,
					placeholders,
					grievance.getUserType(),
					grievance.getUserType(),"USERS");
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName("Grievance Management");
			auditTrail.setSubFeature("Created");
			auditTrail.setFeatureId( grievance.getFeatureId());
			if( Objects.nonNull(endUserGrievance.getPublicIp()))
				auditTrail.setPublicIp(endUserGrievance.getPublicIp());
			if( Objects.nonNull(endUserGrievance.getBrowser()))
				auditTrail.setBrowser(endUserGrievance.getBrowser());
			if( Objects.nonNull( endUserGrievance.getRaisedByUserId() )) {
				user = userRepository.getByid( endUserGrievance.getRaisedByUserId());
				auditTrail.setUserType( endUserGrievance.getRaisedByUserType());
				auditTrail.setRoleType( endUserGrievance.getRaisedByUserType() );
			}else {
				auditTrail.setUserType( grievance.getUserType());
				auditTrail.setRoleType( grievance.getUserType() );
			}
			auditTrail.setUserId( user.getId());
			auditTrail.setUserName( user.getUsername());
			auditTrail.setTxnId( grievance.getGrievanceId());
			logger.info("Audit trail:["+auditTrail.toString()+"]");
			auditTrailRepository.save(auditTrail);
			Generic_Response_Notification genericResponseNotification = userFeignClient.ceirInfoByUserTypeId(8);
			logger.info("generic_Response_Notification::::::::"+genericResponseNotification);
			List<RegisterationUser> registerationUserList = genericResponseNotification.getData();
			for(RegisterationUser registerationUser :registerationUserList) {
				userProfile = userProfileRepository.getByUserId(registerationUser.getId());
				placeholders.put( "<User first name>", userProfile.getFirstName());	
				emailUtil.saveNotificationV2("New_Grievance", 
						userProfile, 
						endUserGrievance.getFeatureId(),
						Features.GRIEVANCE,
						SubFeatures.REPLY,
						grievance.getGrievanceId(),
						MailSubjects.SUBJECT,
						placeholders,
						"CEIRAdmin",
						"CEIRAdmin","USERS");
				logger.info("Notfication have been saved for CEIR Admin.");
			}
			
			/**Email Notification End**/
			return new GrievanceGenricResponse(0,"Grievance registered successfuly",grievance.getGrievanceId());

		}catch (Exception e) {
			logger.error("Grievance Registration failed="+e.getMessage());
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public List<Grievance> getGrievanceByUserId(Integer userId) {
		try {
			logger.info("Going to get All grievances List ");
			return grievanceRepository.getGrievanceByUserId(userId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public List<Grievance> getAllGrievanceStatusNotClosed(Long userId) {
		try {
			logger.info("Going to get All grievances List ");
			return grievanceRepository.getAllGrievanceStatusNotClosed(userId, GrievanceStatus.Closed.getCode());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public List<Grievance> getAllGrievanceStatusNotClosedForAdmin() {
		try {
			logger.info("Going to get All grievances List ");
			return grievanceRepository.getAllGrievanceStatusNotClosedForAdmin(GrievanceStatus.Closed.getCode());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public Page<Grievance> getFilterPaginationGrievances(GrievanceFilterRequest grievance, Integer pageNo, Integer pageSize) {
		boolean isDefaultFilter = this.grievanceDefaultFilter(grievance);
		Pageable pageable = null;
		Page<Grievance> results = null;
		List<StatesInterpretationDb> states = null;
		try {
			AuditTrail auditTrail = new AuditTrail();
			Sort.Direction direction;
			if ( Objects.nonNull(grievance.getOrder()) && grievance.getOrder().equalsIgnoreCase("asc") ) {
				direction = Sort.Direction.ASC;
			} else {
				direction = Sort.Direction.DESC;				
			}
			if( Objects.nonNull( grievance.getOrderColumnName()) && 
					Objects.nonNull(GrievanceOrderColumnMapping.getColumnMapping(grievance.getOrderColumnName()))) {
				pageable = PageRequest.of(pageNo, pageSize,
						new Sort(direction, GrievanceOrderColumnMapping.getColumnMapping(grievance.getOrderColumnName()).name()));
			} else {
				pageable = PageRequest.of(pageNo, pageSize, new Sort(direction, "modifiedOn"));
			}
			GenericSpecificationBuilder<Grievance> gsb = new GenericSpecificationBuilder<Grievance>(propertiesReader.dialect);
			states = statesInterpretaionRepository.findByFeatureId( grievance.getFeatureId() );
			
			if(Objects.nonNull(grievance.getUserId()) && (grievance.getUserId() != -1 && grievance.getUserId() != 0)
					&& !grievance.getUserType().equalsIgnoreCase("ceiradmin") && !grievance.getUserType().equalsIgnoreCase("Customer Care"))
				gsb.with(new SearchCriteria("userId", grievance.getUserId(), SearchOperation.EQUALITY, Datatype.LONG));
			
			if(Objects.nonNull(grievance.getStartDate()) && !grievance.getStartDate().equals(""))
				gsb.with(new SearchCriteria("createdOn", grievance.getStartDate() , SearchOperation.GREATER_THAN, Datatype.DATE));
			
			if(Objects.nonNull(grievance.getEndDate()) && !grievance.getEndDate().equals(""))
				gsb.with(new SearchCriteria("createdOn",grievance.getEndDate() , SearchOperation.LESS_THAN, Datatype.DATE));
			
			if(Objects.nonNull(grievance.getModifiedOn()) && !grievance.getModifiedOn().equals(""))
				gsb.with(new SearchCriteria("modifiedOn",grievance.getModifiedOn() , SearchOperation.EQUALITY, Datatype.DATE));
			
			if(Objects.nonNull(grievance.getGrievanceStatus()) && grievance.getGrievanceStatus() != -1)
				gsb.with(new SearchCriteria("grievanceStatus", grievance.getGrievanceStatus(), SearchOperation.EQUALITY, Datatype.INT));
			else if( isDefaultFilter ) {
				List<DashboardUsersFeatureStateMap> userDefaultStates = dashboardUsersFeatureStateMapRepository.
						findByUserTypeIdAndFeatureId(grievance.getUserTypeId(), grievance.getFeatureId());
				List<Integer> allStates = new ArrayList<Integer>();
				for( DashboardUsersFeatureStateMap state: userDefaultStates) {
					allStates.add( state.getState());
				}
				gsb.addSpecification( gsb.in( "grievanceStatus", allStates));
				
			}
			
			if( Objects.nonNull( grievance.getRaisedBy()) && !grievance.getRaisedBy().equals("")) {
				gsb.with(new SearchCriteria("raisedBy", grievance.getRaisedBy(), SearchOperation.LIKE, Datatype.STRING));
			}
			
			if(Objects.nonNull(grievance.getGrievanceId()) && !grievance.getGrievanceId().equals("")) {
				gsb.with(new SearchCriteria("grievanceId", grievance.getGrievanceId(), SearchOperation.LIKE, Datatype.STRING));
			}
			
			if(Objects.nonNull(grievance.getTxnId()) && !grievance.getTxnId().equals("")) {
				gsb.with(new SearchCriteria("txnId", grievance.getTxnId(), SearchOperation.LIKE, Datatype.STRING));
				auditTrail.setTxnId( grievance.getTxnId() );
			}
			
			if(Objects.nonNull(grievance.getFilterUserName()) && !grievance.getFilterUserName().equals("")) {
				gsb.with(new SearchCriteria("user-username", grievance.getFilterUserName(), SearchOperation.LIKE, Datatype.STRING));
			}
				
			if(Objects.nonNull(grievance.getFilterUserType()) && !grievance.getFilterUserType().equals(""))
				gsb.with(new SearchCriteria("userType", grievance.getFilterUserType(), SearchOperation.EQUALITY_CASE_INSENSITIVE, Datatype.STRING));

			auditTrail.setFeatureName("Grievance Management");
			if( !isDefaultFilter )
				auditTrail.setSubFeature("Filter");
			else
				auditTrail.setSubFeature("View All");
			auditTrail.setFeatureId( (long)grievance.getFeatureId());
			if( Objects.nonNull(grievance.getPublicIp()))
				auditTrail.setPublicIp(grievance.getPublicIp());
			if( Objects.nonNull(grievance.getBrowser()))
				auditTrail.setBrowser(grievance.getBrowser());
			if( Objects.nonNull(grievance.getUserId()) ) {
				User user = userRepository.getByid( grievance.getUserId());
				auditTrail.setUserId( grievance.getUserId() );
				auditTrail.setUserName( user.getUsername());
			}else {
				auditTrail.setUserName( "NA");
			}
			if( Objects.nonNull(grievance.getUserType()) ) {
				auditTrail.setUserType( grievance.getUserType());
				auditTrail.setRoleType( grievance.getUserType() );
			}else {
				auditTrail.setUserType( "NA" );
				auditTrail.setRoleType( "NA" );
			}
			auditTrail.setTxnId("NA");
			auditTrailRepository.save(auditTrail);
			results =  grievanceRepository.findAll(gsb.build(), pageable);
			for( Grievance result : results) {
	            for( StatesInterpretationDb state : states ) {
	            	if( state.getState().equals( result.getGrievanceStatus()))
	            		result.setStateInterp( state.getInterpretation());
	            }
	            result.setUserDisplayName( result.getUser().getUsername());
			}
			return results;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}
	
	public boolean grievanceDefaultFilter( GrievanceFilterRequest grievance ) {
		try {
			if(Objects.nonNull(grievance.getStartDate()) && !grievance.getStartDate().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(grievance.getEndDate()) && !grievance.getEndDate().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(grievance.getModifiedOn()) && !grievance.getModifiedOn().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(grievance.getGrievanceStatus()) && grievance.getGrievanceStatus() != -1)
				return Boolean.FALSE;
			if( Objects.nonNull( grievance.getRaisedBy()) && !grievance.getRaisedBy().equals("")) 
				return Boolean.FALSE;
			if(Objects.nonNull(grievance.getGrievanceId()) && !grievance.getGrievanceId().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(grievance.getTxnId()) && !grievance.getTxnId().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(grievance.getFilterUserName()) && !grievance.getFilterUserName().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(grievance.getFilterUserType()) && !grievance.getFilterUserType().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(grievance.getSearchString()) && !grievance.getSearchString().equals(""))
				return Boolean.FALSE;
		}catch( Exception ex) {
			logger.error(ex.getMessage(), ex);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	public FileDetails getFilterGrievancesInFile(GrievanceFilterRequest grievance, Integer pageNo, Integer pageSize) {
		String fileName = null;
		String attachedFiles   = "";
		Writer writer   = null;
		GrievanceFileModel gfm = null;
		DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter dtf2  = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		String filePath  = systemConfigurationDbRepository.getByTag("file.download-dir").getValue();
		StatefulBeanToCsvBuilder<GrievanceFileModel> builder = null;
		StatefulBeanToCsv<GrievanceFileModel> csvWriter      = null;
		List< GrievanceFileModel> fileRecords       = null;
		HeaderColumnNameTranslateMappingStrategy<GrievanceFileModel> mapStrategy = null;
		try {
			pageNo = 0;
			pageSize = Integer.valueOf(systemConfigurationDbRepository.getByTag("file.max-file-record").getValue());
			logger.info("Grievance data download page size:["+pageSize+"]");
			List<Grievance> grievances = this.getFilterPaginationGrievances(grievance, pageNo, pageSize).getContent();
			fileName = LocalDateTime.now().format(dtf2).replace(" ", "_")+"_Grievances.csv";
			writer = Files.newBufferedWriter(Paths.get(filePath+fileName));
			builder = new StatefulBeanToCsvBuilder<GrievanceFileModel>(writer);
			csvWriter = builder.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
			if( grievances.size() > 0 ) {
//				List<SystemConfigListDb> systemConfigListDbs = configurationManagementServiceImpl.getSystemConfigListByTag("GRIEVANCE_CATEGORY");
				fileRecords = new ArrayList<GrievanceFileModel>(); 
				for( Grievance gr : grievances ) {
					gfm = new GrievanceFileModel();
					gfm.setGrievanceId( gr.getGrievanceId() );
					gfm.setGrievanceStatus( gr.getStateInterp());
//					for( SystemConfigListDb config : systemConfigListDbs ) {
//						if( config.getValue().equals( (Integer)gr.getCategoryId()) ) {
//							gfm.setCategoryId( config.getInterp());
//						}
//					}
					if( gr.getCreatedOn() != null)
						gfm.setCreatedOn(gr.getCreatedOn().format(dtf));
					if( gr.getModifiedOn() != null)
						gfm.setModifiedOn( gr.getModifiedOn().format(dtf));
					gfm.setRemarks( gr.getRemarks());
					System.out.println(gfm.toString());
					fileRecords.add(gfm);
				}
				csvWriter.write(fileRecords);
			}
			User user = userRepository.getByid( grievance.getUserId());
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName("File Download");
			auditTrail.setSubFeature("InputFile");
			auditTrail.setUserId( (long)grievance.getUserId());
			auditTrail.setUserName( user.getUsername());
			auditTrail.setUserType( grievance.getUserType());
			auditTrailRepository.save(auditTrail);
			return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("file.download-link").getValue().replace("$LOCAL_IP",
					propertiesReader.localIp)+fileName );
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			try {

				if( writer != null )
					writer.close();
			} catch (IOException e) {}
		}

	}
	
	public FileDetails getFilterGrievancesInFileV2(GrievanceFilterRequest grievance, Integer pageNo, Integer pageSize) {
		String fileName = null;
		String attachedFiles   = "";
		Writer writer   = null;
		GrievanceFileModel gfm = null;
		DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter dtf2  = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		String filePath  = systemConfigurationDbRepository.getByTag("file.download-dir").getValue();
		StatefulBeanToCsvBuilder<GrievanceFileModel> builder = null;
		StatefulBeanToCsv<GrievanceFileModel> csvWriter      = null;
		List< GrievanceFileModel> fileRecords                = null;
		CustomMappingStrategy<GrievanceFileModel> mappingStrategy = new CustomMappingStrategy<GrievanceFileModel>();
		try {
			pageNo = 0;
			pageSize = Integer.valueOf(systemConfigurationDbRepository.getByTag("file.max-file-record").getValue());
			List<Grievance> grievances = this.getFilterPaginationGrievances(grievance, pageNo, pageSize).getContent();
			fileName = LocalDateTime.now().format(dtf2).replace(" ", "_")+"_Grievances.csv";
			writer = Files.newBufferedWriter(Paths.get(filePath+fileName));
			mappingStrategy.setType(GrievanceFileModel.class);
			builder = new StatefulBeanToCsvBuilder<GrievanceFileModel>(writer);
//			csvWriter = builder.withMappingStrategy(mappingStrategy).withSeparator(',').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
			csvWriter = builder.withMappingStrategy(mappingStrategy).withSeparator(',').withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER).build();
			if( grievances.size() > 0 ) {
				fileRecords = new ArrayList<GrievanceFileModel>();
//				List<SystemConfigListDb> systemConfigListDbs = configurationManagementServiceImpl.getSystemConfigListByTag("GRIEVANCE_CATEGORY");
				for( Grievance gr : grievances ) {
					gfm = new GrievanceFileModel();
					gfm.setGrievanceId( gr.getGrievanceId() );
					gfm.setGrievanceStatus( gr.getStateInterp());
//					for( SystemConfigListDb config : systemConfigListDbs ) {
//						if( config.getValue().equals( (Integer)gr.getCategoryId()) ) {
//							gfm.setCategoryId( config.getInterp());
//						}
//					}
					if( gr.getCreatedOn() != null)
						gfm.setCreatedOn(gr.getCreatedOn().format(dtf));
					if( gr.getModifiedOn() != null)
						gfm.setModifiedOn( gr.getModifiedOn().format(dtf));
					attachedFiles   = "";
					for( AttachedFileInfo attachedFileInfo : gr.getAttachedFiles()) {
						attachedFiles += attachedFileInfo.getFileName()+"|";
					}
					if( !attachedFiles.equals("") )
						attachedFiles = attachedFiles.substring( 0, attachedFiles.length() - 1);
					gfm.setFileName( attachedFiles );
					gfm.setRemarks( gr.getRemarks());
					if( Objects.nonNull( gr.getTxnId() ) )
						gfm.setTxnId( gr.getTxnId() );
					else
						gfm.setTxnId( "NA" );
					//System.out.println(gfm.toString());
					fileRecords.add(gfm);
				}
				csvWriter.write(fileRecords);
			}else {
				csvWriter.write( new GrievanceFileModel());
			}
			//return new FileDetails( fileName, filePath, fileStorageProperties.getGrievanceDownloadLink()+fileName );
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName("Grievance Management");
			auditTrail.setSubFeature("Export");
			auditTrail.setFeatureId( (long)grievance.getFeatureId());
			if( Objects.nonNull(grievance.getPublicIp()))
				auditTrail.setPublicIp(grievance.getPublicIp());
			if( Objects.nonNull(grievance.getBrowser()))
				auditTrail.setBrowser(grievance.getBrowser());
			if( Objects.nonNull(grievance.getUserId()) ) {
				User user = userRepository.getByid( grievance.getUserId());
				auditTrail.setUserId( grievance.getUserId() );
				auditTrail.setUserName( user.getUsername());
			}else {
				auditTrail.setUserName( "NA");
			}
			if( Objects.nonNull(grievance.getUserType()) ) {
				auditTrail.setUserType( grievance.getUserType());
				auditTrail.setRoleType( grievance.getUserType() );
			}else {
				auditTrail.setUserType( "NA" );
				auditTrail.setRoleType( "NA" );
			}
//			if( !Objects.nonNull( grievance.getTxnId() )  || grievance.getTxnId().isEmpty() ) {
				auditTrail.setTxnId("NA");
//			}
			auditTrailRepository.save(auditTrail);
//			return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("file.download-link").getValue()+fileName );
			return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("file.download-link").getValue().replace("$LOCAL_IP",
					propertiesReader.localIp)+fileName );
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			try {

				if( writer != null )
					writer.close();
			} catch (IOException e) {}
		}
	}
	
	
	public FileDetails getFilterGrievancesInFileCEIRAdmin(GrievanceFilterRequest grievance, Integer pageNo, Integer pageSize) {
		String fileName = null;
		String attachedFiles   = "";
		Writer writer   = null;
		GrievanceCEIRFileModel gfm = null;
		DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter dtf2  = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		String filePath  = systemConfigurationDbRepository.getByTag("file.download-dir").getValue();
		StatefulBeanToCsvBuilder<GrievanceCEIRFileModel> builder = null;
		StatefulBeanToCsv<GrievanceCEIRFileModel> csvWriter      = null;
		List< GrievanceCEIRFileModel> fileRecords                = null;
		CustomMappingStrategy<GrievanceCEIRFileModel> mappingStrategy = new CustomMappingStrategy<GrievanceCEIRFileModel>();
		try {
			pageNo = 0;
			pageSize = Integer.valueOf(systemConfigurationDbRepository.getByTag("file.max-file-record").getValue());
			List<Grievance> grievances = this.getFilterPaginationGrievances(grievance, pageNo, pageSize).getContent();
			fileName = LocalDateTime.now().format(dtf2).replace(" ", "_")+"_Grievances.csv";
			writer = Files.newBufferedWriter(Paths.get(filePath+fileName));
			mappingStrategy.setType(GrievanceCEIRFileModel.class);
			builder = new StatefulBeanToCsvBuilder<GrievanceCEIRFileModel>(writer);
			csvWriter = builder.withMappingStrategy(mappingStrategy).withSeparator(',').withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER).build();
			if( grievances.size() > 0 ) {
				fileRecords = new ArrayList<GrievanceCEIRFileModel>();
				List<SystemConfigListDb> systemConfigListDbs = configurationManagementServiceImpl.getSystemConfigListByTag("GRIEVANCE_CATEGORY");
				for( Grievance gr : grievances ) {
					gfm = new GrievanceCEIRFileModel();
					if( gr.getCreatedOn() != null)
						gfm.setCreatedOn(gr.getCreatedOn().format(dtf));
					
					if( gr.getModifiedOn() != null)
						gfm.setModifiedOn( gr.getModifiedOn().format(dtf));
					
					if( Objects.nonNull( gr.getTxnId() ) )
						gfm.setTxnId( gr.getTxnId() );
					else
						gfm.setTxnId( "NA" );
					
					gfm.setGrievanceId( gr.getGrievanceId() );
					gfm.setUserId( gr.getUser().getUsername() );
					gfm.setUserType( gr.getUserType() );
					gfm.setRaisedBy( gr.getRaisedBy() );
					
					gfm.setGrievanceStatus( gr.getStateInterp());
					attachedFiles   = "";
					for( AttachedFileInfo attachedFileInfo : gr.getAttachedFiles()) {
						attachedFiles += attachedFileInfo.getFileName()+"|";
					}
					if( !attachedFiles.equals("") )
						attachedFiles = attachedFiles.substring( 0, attachedFiles.length() - 1);
					gfm.setFileName( attachedFiles );
					gfm.setRemarks( gr.getRemarks());
					
					fileRecords.add(gfm);
				}
				csvWriter.write(fileRecords);
			}else {
				csvWriter.write( new GrievanceCEIRFileModel());
			}
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName("Grievance Management");
			auditTrail.setSubFeature("Export");
			auditTrail.setFeatureId( (long)grievance.getFeatureId());
			if( Objects.nonNull(grievance.getPublicIp()))
				auditTrail.setPublicIp(grievance.getPublicIp());
			if( Objects.nonNull(grievance.getBrowser()))
				auditTrail.setBrowser(grievance.getBrowser());
			if( Objects.nonNull(grievance.getUserId()) ) {
				User user = userRepository.getByid( grievance.getUserId());
				auditTrail.setUserId( grievance.getUserId() );
				auditTrail.setUserName( user.getUsername());
			}else {
				auditTrail.setUserName( "NA");
			}
			if( Objects.nonNull(grievance.getUserType()) ) {
				auditTrail.setUserType( grievance.getUserType());
				auditTrail.setRoleType( grievance.getUserType() );
			}else {
				auditTrail.setUserType( "NA" );
				auditTrail.setRoleType( "NA" );
			}
			if( !Objects.nonNull( grievance.getTxnId() )  || grievance.getTxnId().isEmpty() ) {
				auditTrail.setTxnId("NA");
			}
			auditTrailRepository.save(auditTrail);
			return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("file.download-link").getValue().replace("$LOCAL_IP",
					propertiesReader.localIp)+fileName );
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			try {

				if( writer != null )
					writer.close();
			} catch (IOException e) {}
		}

	}
	
	
	@Transactional
	public GrievanceGenricResponse saveGrievanceMsg(GrievanceReply grievanceReply) {
		Map< String, String > placeholders = new HashMap< String, String >();
		List<StatesInterpretationDb> states = null;
		GrievanceHistory grievanceHistory = null;
		GrievanceMsg grievanceMsg = null;
		UserProfile userProfile = null;
		int pendingWithAdmin = 0;
		int pendingCode = 0;
		int closeCode = 0;
		try {
			states = statesInterpretaionRepository.findByFeatureId( grievanceReply.getFeatureId() );
			for( StatesInterpretationDb state : states ) {
            	if( state.getInterpretation().trim().equalsIgnoreCase(Tags.CLOSED))
            		closeCode = state.getState();
            	else if( state.getInterpretation().trim().equalsIgnoreCase(Tags.Pending_With_User)) {
            		pendingCode = state.getState();
				}else if( state.getInterpretation().trim().equalsIgnoreCase(Tags.Pending_With_Admin)) {
					pendingWithAdmin = state.getState();
				}
            }
			Grievance grievance = grievanceRepository.getBygrievanceId( grievanceReply.getGrievanceId() );
			/*****Grievance Message new object*****/
			grievanceMsg = new GrievanceMsg();
			grievanceMsg.setGrievanceId( grievance.getGrievanceId() );
			grievanceMsg.setUserId( grievanceReply.getUserId() );
			grievanceMsg.setUserType( grievanceReply.getUserType());
			grievanceMsg.setReply( grievanceReply.getReply());
			grievanceMsg.setAttachedFiles( grievanceReply.getAttachedFiles() );
			/*****Grievance Message new object all parameters set*****/
			/**Grievance status update**/
			placeholders.put( "<txn_id>", grievance.getGrievanceId());
			placeholders.put( "<Txn_Id>", grievance.getGrievanceId());
			if( !grievanceReply.getGrievanceStatus().equals( closeCode ) ) {
				if( grievanceReply.getUserType().equalsIgnoreCase("ceiradmin") ) {
					grievance.setGrievanceStatus( pendingCode );
					/**Email Notification Start **/
					placeholders.put( "<User first name>", grievance.getUser().getUserProfile().getFirstName());
					emailUtil.saveNotificationV2("Grievance_Pending_With_User", 
							grievance.getUser().getUserProfile(), 
							grievanceReply.getFeatureId(),
							Features.GRIEVANCE,
							SubFeatures.REPLY,
							grievance.getGrievanceId(),
							MailSubjects.SUBJECT,
							placeholders,
							grievance.getUserType(),
							grievance.getUserType(),"USERS");
					/**Email Notification End**/
				}else{
					grievance.setGrievanceStatus( pendingWithAdmin );
					/**Email Notification Start **/
					Generic_Response_Notification genericResponseNotification = userFeignClient.ceirInfoByUserTypeId(8);
					logger.info("generic_Response_Notification::::::::"+genericResponseNotification);
					List<RegisterationUser> registerationUserList = genericResponseNotification.getData();
					for(RegisterationUser registerationUser :registerationUserList) {
						userProfile = userProfileRepository.getByUserId(registerationUser.getId());
						placeholders.put( "<User first name>", userProfile.getFirstName());
						emailUtil.saveNotificationV2("Grievance_Pending_With_Admin", 
								userProfile, 
								grievanceReply.getFeatureId(),
								Features.GRIEVANCE,
								SubFeatures.REPLY,
								grievance.getGrievanceId(),
								MailSubjects.SUBJECT,
								placeholders,
								grievanceReply.getUserType(),
								"CEIRAdmin","USERS");
						logger.info("Notfication have been saved for CEIR Admin.");
					}
					/**Email Notification End**/
				}
			}else{
				/**Grievance History object**/
				grievanceHistory = new GrievanceHistory();
				grievanceHistory.setGrievanceId( grievance.getGrievanceId());
				grievanceHistory.setCategoryId( grievance.getCategoryId());
				grievanceHistory.setUserId( grievance.getUserId());
				grievanceHistory.setUserType( grievance.getUserType() );
				grievanceHistory.setCategoryId( grievance.getCategoryId());

//				webActionDb.setState( closeCode );
				grievance.setGrievanceStatus( closeCode );
				grievanceHistory.setGrievanceStatus( closeCode );
				grievanceHistory.setCreatedOn(grievance.getCreatedOn());
				grievanceHistory.setTxnId( grievance.getTxnId() );
				grievanceHistory.setRemarks( grievance.getRemarks() );
				grievanceHistory.setClosedByUserId( grievanceReply.getUserId());
				grievanceHistory.setClosedByUserType( grievanceReply.getUserType() );
				/**Grievance History object all parameters set**/
				/**Email Notification Start **/
				placeholders.put( "<User first name>", grievance.getUser().getUserProfile().getFirstName());
				emailUtil.saveNotificationV2("Grievance_Closed", 
						grievance.getUser().getUserProfile(), 
						grievanceReply.getFeatureId(),
						Features.GRIEVANCE,
						SubFeatures.REPLY,
						grievance.getGrievanceId(),
						MailSubjects.SUBJECT,
						placeholders,
						grievance.getUserType(),
						grievance.getUserType(),"USERS");
				/**Email Notification End**/
			}
			/**Grievance status update end**/
			grievanceRepository.save(grievance);
			grievanceMsg = grievanceMsgRepository.save(grievanceMsg);
			if( grievanceHistory != null )
				grievanceHistoryRepository.save(grievanceHistory);

			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName("Grievance Management");
			auditTrail.setSubFeature("Reply");
			auditTrail.setFeatureId( grievance.getFeatureId());
			if( Objects.nonNull(grievanceReply.getPublicIp()))
					auditTrail.setPublicIp(grievanceReply.getPublicIp());
			if( Objects.nonNull(grievanceReply.getBrowser()))
				auditTrail.setBrowser(grievanceReply.getBrowser());
			User user = null;
			if( Objects.nonNull( grievanceReply.getRaisedByUserId() )) {
				user = userRepository.getByid( grievanceReply.getRaisedByUserId());
				auditTrail.setUserType( grievanceReply.getRaisedByUserType());
				auditTrail.setRoleType( grievanceReply.getRaisedByUserType() );
			}else if( Objects.nonNull( grievanceReply.getUserId() )) {
				user = userRepository.getByid( grievanceReply.getUserId());
				auditTrail.setUserType( grievanceReply.getUserType());
				auditTrail.setRoleType( grievanceReply.getUserType() );
			}else {
				user = grievanceMsg.getUser();
				auditTrail.setUserType( grievanceMsg.getUserType());
				auditTrail.setRoleType( grievanceMsg.getUserType() );
			}
			auditTrail.setUserId( user.getId());
			auditTrail.setUserName( user.getUsername());
			auditTrail.setTxnId( grievance.getGrievanceId());
			auditTrailRepository.save(auditTrail);
			return new GrievanceGenricResponse(0,"Grievance Message saved successfuly.",grievance.getGrievanceId());

		}catch (Exception e) {
			logger.error("Grievance Message update failed"+e.getMessage());
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public List<GrievanceMsgWithUser> getAllGrievanceMessagesByGrievanceId( String grievanceId, Integer recordLimit, Long userId,
			String userType, String publicIp, String browser, Long featureId){
		List<GrievanceMsg> messages = null;
		List<GrievanceMsgWithUser> messagesWithUser = null;
		List<SystemConfigListDb> docTypes = null;
		try {
			logger.info("Going to get All grievance Messages List ");
			docTypes = configurationManagementServiceImpl.getSystemConfigListByTag("DOC_TYPE");
			if( recordLimit == -1) {
//				messages = grievanceMsgRepository.getGrievanceMsgByGrievanceIdOrderByIdDesc(grievanceId );
				messages = grievanceMsgRepository.getGrievanceMsgByGrievanceIdOrderByModifiedOnDesc(grievanceId);
			}else {
				messages = grievanceMsgRepository.getGrievanceMsgByGrievanceIdOrderByIdDesc(grievanceId, new PageRequest(0, recordLimit) );
			}
			messagesWithUser = new ArrayList<GrievanceMsgWithUser>();
			if( messages.size() > 0 ) {
				GrievanceMsgWithUser msgWithUser = null;
				for( GrievanceMsg msg : messages ) {
					msgWithUser = new GrievanceMsgWithUser();
					msgWithUser.setId(msg.getId());
					msgWithUser.setGrievance( msg.getGrievance());
					msgWithUser.setGrievanceId(msg.getGrievanceId());
					msgWithUser.setCreatedOn(msg.getCreatedOn());
					msgWithUser.setModifiedOn(msg.getModifiedOn());
					msgWithUser.setReply(msg.getReply());
					msgWithUser.setUserId(msg.getUserId());
					msgWithUser.setUserType(msg.getUserType());
					if( msg.getUserType().equalsIgnoreCase("ceiradmin") ) {
						if( !msg.getGrievance().getUserId().equals( userId ) ) {
							msgWithUser.setUserDisplayName(msg.getUser().getUserProfile().getDisplayName());
							msgWithUser.setUsername(msg.getUser().getUsername());
						}else {
							msgWithUser.setUserDisplayName("Admin");
						}
					}else {
						if( msg.getUserId().equals(userId ) )
							msgWithUser.setUserDisplayName("You");
						else
							msgWithUser.setUserDisplayName("User");
					}
					logger.info("msg.getAttachedFiles()===="+msg.getAttachedFiles());
					for( AttachedFileInfo fileInfo : msg.getAttachedFiles()) {
						for( SystemConfigListDb docType : docTypes) {
							if( docType.getValue().equals( fileInfo.getDocType() ))
								fileInfo.setDocTypeInterp( docType.getInterpretation() );
						}
					}
					msgWithUser.setAttachedFiles( msg.getAttachedFiles() );
					messagesWithUser.add(msgWithUser);
				}
			}
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName("Grievance Management");
			auditTrail.setSubFeature("View");
			auditTrail.setFeatureId( featureId);
			auditTrail.setPublicIp(publicIp);
			auditTrail.setBrowser(browser);
			User user = userRepository.getByid( userId );
			auditTrail.setUserType(userType);
			auditTrail.setRoleType(userType);
			auditTrail.setUserId( user.getId());
			auditTrail.setUserName( user.getUsername());
			auditTrail.setTxnId( grievanceId);
			auditTrailRepository.save(auditTrail);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
		return messagesWithUser;
	}
	
	public Page<GrievanceHistory> getFilterPaginationGrievanceHistory(GrievanceFilterRequest grievance, Integer pageNo, Integer pageSize) {
		try {
			Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(Sort.Direction.DESC, "id"));
			GrievanceHistorySpecificationBuilder gsb = new GrievanceHistorySpecificationBuilder(propertiesReader.dialect);
			
			if(Objects.nonNull(grievance.getUserId()) && (grievance.getUserId() != -1 && grievance.getUserId() != 0))
				gsb.with(new SearchCriteria("userId", grievance.getUserId(), SearchOperation.EQUALITY, Datatype.LONG));
			
			if(Objects.nonNull(grievance.getStartDate()) && !grievance.getStartDate().equals(""))
				gsb.with(new SearchCriteria("createdOn", grievance.getStartDate() , SearchOperation.GREATER_THAN, Datatype.DATE));
			
			if(Objects.nonNull(grievance.getEndDate()) && !grievance.getEndDate().equals(""))
				gsb.with(new SearchCriteria("createdOn",grievance.getEndDate() , SearchOperation.LESS_THAN, Datatype.DATE));
			
			if(Objects.nonNull(grievance.getGrievanceId()) && !grievance.getGrievanceId().equals(""))
				gsb.with(new SearchCriteria("grievanceId", grievance.getGrievanceId(), SearchOperation.EQUALITY, Datatype.STRING));
			
			if(Objects.nonNull(grievance.getTxnId()) && !grievance.getTxnId().equals(""))
				gsb.with(new SearchCriteria("txnId", grievance.getTxnId(), SearchOperation.EQUALITY, Datatype.STRING));
			
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName("Grievance Management");
			auditTrail.setSubFeature("History");
			auditTrail.setFeatureId( grievance.getFeatureId());
			auditTrail.setPublicIp(grievance.getPublicIp());
			auditTrail.setBrowser(grievance.getBrowser());
			User user = userRepository.getByid( grievance.getUserId() );
			auditTrail.setUserType(grievance.getUserType());
			auditTrail.setRoleType(grievance.getUserType());
			auditTrail.setUserId( user.getId());
			auditTrail.setUserName( user.getUsername());
			auditTrail.setTxnId( grievance.getGrievanceId());
			auditTrailRepository.save(auditTrail);
			return grievanceHistoryRepository.findAll(gsb.build(), pageable);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}

}
