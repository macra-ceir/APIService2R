package com.gl.ceir.config.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.app.DashboardConfDb;
import com.gl.ceir.config.model.app.DashboardUsersFeatureStateMap;
import com.gl.ceir.config.model.app.ResponseCountAndQuantity;
import com.gl.ceir.config.model.app.UserProfile;
import com.gl.ceir.config.repository.app.ConsignmentRepository;
import com.gl.ceir.config.repository.app.DashboardConfDbRepository;
import com.gl.ceir.config.repository.app.DashboardUsersFeatureStateMapRepository;
import com.gl.ceir.config.repository.app.FileDumpMgmtRepository;
import com.gl.ceir.config.repository.app.GrievanceRepository;
import com.gl.ceir.config.repository.app.ImmegreationImeiDetailsRepository;
import com.gl.ceir.config.repository.app.RegularizedDeviceDbRepository;
import com.gl.ceir.config.repository.app.StockManagementRepository;
import com.gl.ceir.config.repository.app.StolenAndRecoveryRepository;
import com.gl.ceir.config.repository.app.TypeApproveRepository;
import com.gl.ceir.config.repository.app.UpdateVisaRepository;
import com.gl.ceir.config.repository.app.UserRepository;

@Service
public class DashboardConfServiceImpl {
	private static final Logger logger = LogManager.getLogger(DashboardConfServiceImpl.class);
	
	@Autowired
	DashboardConfDbRepository dashboardConfDbRepository;
	@Autowired
	GrievanceRepository grievanceRepository;
	@Autowired
	ConsignmentRepository consignmentRepository;
	@Autowired
	StockManagementRepository stockManagementRepository;
	@Autowired
	StolenAndRecoveryRepository stolenAndRecoveryRepository;
	@Autowired
	FileDumpMgmtRepository fileDumpMgmtRepository;
	@Autowired
	TypeApproveRepository typeApproveRepo;
	@Autowired
	ImmegreationImeiDetailsRepository immegreationImeiDetailsRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	DashboardUsersFeatureStateMapRepository featureStateMapRepository;
	@Autowired
	RegularizedDeviceDbRepository regularizedDeviceDbRepository;
	@Autowired
	UpdateVisaRepository updateVisaRepository;
	
	public List< DashboardConfDb> getDashboardConfig( Integer userTypeId ){
		try {
			logger.info("Going to get All dashboard config List for userTypeId:["+userTypeId+"]");
			return dashboardConfDbRepository.findByUserTypeId(userTypeId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public ResponseCountAndQuantity getGrievanceCount( Long userId, Long userTypeId, Long featureId, String userType ) {
		List<DashboardUsersFeatureStateMap> stateFeatureMap = null;
		List<Integer> status = new ArrayList<Integer>();
		try {
			logger.info("Going to get Grievance count.");
			stateFeatureMap = featureStateMapRepository.findByUserTypeIdAndFeatureId(userTypeId, featureId);
			for( DashboardUsersFeatureStateMap map : stateFeatureMap) {
				status.add( map.getState() );
			}
			if( !userType.equalsIgnoreCase("ceiradmin"))
				return grievanceRepository.getGrievanceCount( userId, status);
			else
				return grievanceRepository.getGrievanceCount( status );
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseCountAndQuantity(0,0);
		}
	}
	
	public ResponseCountAndQuantity getConsignmentCountAndQuantity( Integer userId, Long userTypeId, Long featureId, String userType ) {
		List<DashboardUsersFeatureStateMap> stateFeatureMap = null;
		List<Integer> status = new ArrayList<Integer>();
		try {
			logger.info("Going to get  Cosignment count and quantity.");
			stateFeatureMap = featureStateMapRepository.findByUserTypeIdAndFeatureId(userTypeId, featureId);
			for( DashboardUsersFeatureStateMap map : stateFeatureMap) {
				status.add( map.getState() );
			}
			logger.info("All consignment states for userType["+userType+"] and states:["+status.toString()+"]");
			if( !(userType.equalsIgnoreCase("ceiradmin") || userType.equalsIgnoreCase("Custom"))) {
				return consignmentRepository.getConsignmentCountAndQuantity( userId, status);
			}else if(userType.equalsIgnoreCase("Custom")) {
				UserProfile userProfile = userRepository.getByid( (long)userId).getUserProfile();
				return consignmentRepository.getConsignmentCountAndQuantity( status, userProfile.getPortAddress());
			}else {
				return consignmentRepository.getConsignmentCountAndQuantity( status );
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseCountAndQuantity(0,0);
		}
	}
	

	public ResponseCountAndQuantity getStockCountAndQuantity( long userId, Long userTypeId, Long featureId, String userType ) {
		List<DashboardUsersFeatureStateMap> stateFeatureMap = null;
		List<Integer> status = new ArrayList<Integer>();
		try {
			logger.info("Going to get  stock count and quantity.");
			stateFeatureMap = featureStateMapRepository.findByUserTypeIdAndFeatureId(userTypeId, featureId);
			for( DashboardUsersFeatureStateMap map : stateFeatureMap) {
				status.add( map.getState() );
			}
			if( userType.equalsIgnoreCase("ceiradmin") )
				return stockManagementRepository.getStockCountAndQuantity( status );
			else if( userType.equalsIgnoreCase("Custom") ) {
				UserProfile userProfile = userRepository.getByid( (long)userId).getUserProfile();
				return stockManagementRepository.getStockCountAndQuantityCustom( userProfile.getPortAddress(), status );
			}else
				return stockManagementRepository.getStockCountAndQuantity( userId, status );
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseCountAndQuantity(0,0);
		}
	}
	
	public ResponseCountAndQuantity getStolenAndRecoveryCount( long userId, Long userTypeId, Long featureId, List<Integer> requestType, String userType ) {
		List<DashboardUsersFeatureStateMap> stateFeatureMap = null;
		List<Integer> status = new ArrayList<Integer>();
		try {
			stateFeatureMap = featureStateMapRepository.findByUserTypeIdAndFeatureId(userTypeId, featureId);
			for( DashboardUsersFeatureStateMap map : stateFeatureMap) {
				status.add( map.getState() );
			}
			logger.info("Going to get StolenAndRecovery count. Status:["+status.toString()+"] and requestType:["+requestType+"]");
			if( !userType.equalsIgnoreCase("ceiradmin") && !userType.equalsIgnoreCase("Lawful Agency") )
				return stolenAndRecoveryRepository.getStolenandRecoveryCount( userId, status, requestType);
			else
				return stolenAndRecoveryRepository.getStolenandRecoveryCount( status, requestType);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseCountAndQuantity(0,0);
		}
	}
	
	public ResponseCountAndQuantity getFileDumpCount( Integer serviceDump ) {
		try {
			logger.info("Going to get FileDump count for serviceDump["+serviceDump+"].");
			return fileDumpMgmtRepository.getFileDumpCount(serviceDump);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseCountAndQuantity(0,0);
		}
	}
	

	public ResponseCountAndQuantity getTACCount( Long userId, Long userTypeId, Long featureId, String userType ) {
		List<DashboardUsersFeatureStateMap> stateFeatureMap = null;
		List<Integer> status = new ArrayList<Integer>();
		try {
			stateFeatureMap = featureStateMapRepository.findByUserTypeIdAndFeatureId(userTypeId, featureId);
			for( DashboardUsersFeatureStateMap map : stateFeatureMap) {
				status.add( map.getState() );
			}
			logger.info("Going to get TAC count for userId["+userId+"].");
			if( userType.equalsIgnoreCase("CEIRAdmin") && status.size() > 0) {
				return typeApproveRepo.getAdminTypeApproveCount(status, featureId);
			}else if( userType.equalsIgnoreCase("CEIRAdmin") && status.size() == 0 ) {
				return typeApproveRepo.getAdminTypeApproveCount(featureId);
			}else if( userType.equalsIgnoreCase("TRC") ) {
				return typeApproveRepo.getAdminTypeApproveCount( status, featureId, userType);
			}else {
				return typeApproveRepo.getTypeApproveCount( status, userId, featureId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseCountAndQuantity(0,0);
		}
	}
	
	public ResponseCountAndQuantity getBlockUnblockCount( long userId, Long userTypeId, Long featureId, List<Integer> requestType,
			String userType, Integer operatorId ) {
		List<DashboardUsersFeatureStateMap> stateFeatureMap = null;
		ResponseCountAndQuantity stolenAndRecovery = null;
		List<Integer> status = new ArrayList<Integer>();
		try {
			stateFeatureMap = featureStateMapRepository.findByUserTypeIdAndFeatureId(userTypeId, featureId);
			for( DashboardUsersFeatureStateMap map : stateFeatureMap) {
				status.add( map.getState() );
			}
			logger.info("Going to get StolenAndRecovery count.Request Type:["+requestType.toString()+"] and status:["+status.toString()+"] "
					+ "and Usertype:["+userType+"] and operatorId:["+operatorId+"]");
			if( userType.equalsIgnoreCase("ceiradmin") && !userType.equalsIgnoreCase("Lawful Agency") ) {
				stolenAndRecovery = stolenAndRecoveryRepository.getBlockUnblockCount( status, requestType);
			}else if( userType.equalsIgnoreCase("Operator") ) {
				stolenAndRecovery = stolenAndRecoveryRepository.getBlockUnblockCount( status, requestType, operatorId);
			}else {
				stolenAndRecovery = stolenAndRecoveryRepository.getBlockUnblockCount( userId, status, requestType);
			}
			return new ResponseCountAndQuantity( stolenAndRecovery.getCount(),
					stolenAndRecovery.getQuantity() );
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseCountAndQuantity(0,0);
		}
	}
	
	public ResponseCountAndQuantity getPendingUsersCount( Long userTypeId, Long featureId ) {
		List<DashboardUsersFeatureStateMap> stateFeatureMap = null;
		List<Integer> status = new ArrayList<Integer>();
		try {
			stateFeatureMap = featureStateMapRepository.findByUserTypeIdAndFeatureId(userTypeId, featureId);
			for( DashboardUsersFeatureStateMap map : stateFeatureMap) {
				status.add( map.getState() );
			}
			logger.info("Going to get pending users count.");
			return userRepository.getPendingUsersCount(status);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseCountAndQuantity(0,0);
		}
	}
	
	public ResponseCountAndQuantity getDeviceRequestCountAndQuantity( long userId, Long userTypeId, Long featureId, String userType ) {
		List<DashboardUsersFeatureStateMap> stateFeatureMap = null;
		List<Integer> status = new ArrayList<Integer>();
		try {
			logger.info("Going to get register device request count.");
			stateFeatureMap = featureStateMapRepository.findByUserTypeIdAndFeatureId(userTypeId, featureId);
			for( DashboardUsersFeatureStateMap map : stateFeatureMap) {
				status.add( map.getState() );
			}
			if( !userType.equalsIgnoreCase("ceiradmin"))
				return regularizedDeviceDbRepository.getCountAndQuantity( status, userType );
			else
				return regularizedDeviceDbRepository.getCountAndQuantity( status );
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseCountAndQuantity(0,0);
		}
	}
	
	public ResponseCountAndQuantity getVisaUpdateRequestCount( long userId, Long userTypeId, Long featureId, String userType ) {
		List<DashboardUsersFeatureStateMap> stateFeatureMap = null;
		List<Integer> status = new ArrayList<Integer>();
		try {
			logger.info("Going to get register device request count.");
			stateFeatureMap = featureStateMapRepository.findByUserTypeIdAndFeatureId(userTypeId, featureId);
			for( DashboardUsersFeatureStateMap map : stateFeatureMap) {
				status.add( map.getState() );
			}
//			if( !userType.equalsIgnoreCase("ceiradmin"))
//				return updateVisaRepository.getCountAndQuantity( userId, status );
//			else
			return updateVisaRepository.getCountAndQuantity( status );
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseCountAndQuantity(0,0);
		}
	}
}
