package com.gl.ceir.config.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.model.app.DashboardConfDb;
import com.gl.ceir.config.model.app.ResponseCountAndQuantity;
import com.gl.ceir.config.service.impl.DashboardConfServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
public class DashboardController {
	private static final Logger logger = LogManager.getLogger(GrievanceController.class);

	@Autowired
	DashboardConfServiceImpl dashboardConfServiceImpl;
	
	@ApiOperation(value = "Get total count.", response = ResponseCountAndQuantity.class)
	@RequestMapping(path = "/grievance/count", method = RequestMethod.GET)
	public MappingJacksonValue getgrievanceCount(@RequestParam(value = "userId") Long userId,
			@RequestParam(value = "userTypeId") Long userTypeId,
			@RequestParam(value = "userType") String userType,
			@RequestParam(value = "featureId") Long featureId) {
		ResponseCountAndQuantity response = dashboardConfServiceImpl.getGrievanceCount(userId, userTypeId, featureId, userType);
		return new MappingJacksonValue(response);
	}
	
	@ApiOperation(value = "Get total count.", response = ResponseCountAndQuantity.class)
	@RequestMapping(path = "/consignment/countAndQuantity", method = RequestMethod.GET)
	public MappingJacksonValue getConsignmentCountAndQuantity( @RequestParam(value = "userId") Integer userId,
			@RequestParam(value = "userTypeId") Long userTypeId,
			@RequestParam(value = "userType") String userType,
			@RequestParam(value = "featureId") Long featureId ) {
		ResponseCountAndQuantity response = dashboardConfServiceImpl.getConsignmentCountAndQuantity(userId, userTypeId, featureId, userType);
		return new MappingJacksonValue(response);
	}
	
	@ApiOperation(value = "Get total count and quantity.", response = ResponseCountAndQuantity.class)
	@RequestMapping(path = "/stock/countAndQuantity", method = RequestMethod.GET)
	public MappingJacksonValue getStockCountAndQuantity( @RequestParam(value = "userId") long userId,
			@RequestParam(value = "userTypeId") Long userTypeId,
			@RequestParam(value = "userType") String userType,
			@RequestParam(value = "featureId") Long featureId ) {
		//hh
		ResponseCountAndQuantity response = dashboardConfServiceImpl.getStockCountAndQuantity( userId, userTypeId, featureId, userType );
		return new MappingJacksonValue(response);
	}
	
	@ApiOperation(value = "Get total count.", response = ResponseCountAndQuantity.class)
	@RequestMapping(path = "/stakeholder/count", method = RequestMethod.GET)
	public MappingJacksonValue getStolenAndRecoveryCount( @RequestParam(value = "userId") long userId,
			@RequestParam(value = "userTypeId") Long userTypeId,
			@RequestParam(value = "featureId") Long featureId,
			@RequestParam(value = "userType") String userType,
			@RequestParam(value = "requestType") List<Integer> requestType) {
		ResponseCountAndQuantity response = dashboardConfServiceImpl.getStolenAndRecoveryCount( userId, userTypeId, featureId, requestType, userType);
		return new MappingJacksonValue(response);
	}
	
	@ApiOperation(value = "Get total count.", response = ResponseCountAndQuantity.class)
	@RequestMapping(path = "/filedump/count", method = RequestMethod.GET)
	public MappingJacksonValue getOperatorCount( @RequestParam(value = "userId") Integer userId,
			@RequestParam(value = "userTypeId") Long userTypeId,
			@RequestParam(value = "featureId") Long featureId,
			@RequestParam(value = "userType") String userType,
			@RequestParam(value = "serviceDump") Integer serviceDump) {
		ResponseCountAndQuantity response = dashboardConfServiceImpl.getFileDumpCount(serviceDump);
		return new MappingJacksonValue(response);
	}
	
	@ApiOperation(value = "Get total count.", response = ResponseCountAndQuantity.class)
	@RequestMapping(path = "/TypeApproved/count", method = RequestMethod.GET)
	public MappingJacksonValue getTACCount( @RequestParam(value = "userId") Long userId,
			@RequestParam(value = "userTypeId") Long userTypeId,
			@RequestParam(value = "featureId") Long featureId,
			@RequestParam(value = "userType") String userType) {
		ResponseCountAndQuantity response = dashboardConfServiceImpl.getTACCount(userId, userTypeId, featureId, userType);
		return new MappingJacksonValue(response);
	}
	
	@ApiOperation(value = "Get total count.", response = DashboardConfDb.class)
	@RequestMapping(path = "/dashboard/dbConf", method = RequestMethod.GET)
	public MappingJacksonValue getDashboardConf( @RequestParam(value = "userTypeId") Integer userTypeId) {
		List<DashboardConfDb> response = dashboardConfServiceImpl.getDashboardConfig(userTypeId);
		return new MappingJacksonValue(response);
	}
	
	@ApiOperation(value = "Get total count.", response = DashboardConfDb.class)
	@RequestMapping(path = "/stakeholder/blockUnblockCount", method = RequestMethod.GET)
	public MappingJacksonValue getBlockUnblockCount( @RequestParam(value = "userId") long userId,
			@RequestParam(value = "userTypeId") Long userTypeId,
			@RequestParam(value = "featureId") Long featureId,
			@RequestParam(value = "userType") String userType,
			@RequestParam(value = "operatorId" , required=false ) Integer operatorId,
			@RequestParam(value = "requestType") List<Integer> requestType) {
		ResponseCountAndQuantity response = dashboardConfServiceImpl.getBlockUnblockCount(userId, userTypeId, featureId, requestType, userType, operatorId);
		return new MappingJacksonValue(response);
	}
	
	@ApiOperation(value = "Get total count.", response = DashboardConfDb.class)
	@RequestMapping(path = "/users/pendingCount", method = RequestMethod.GET)
	public MappingJacksonValue getPendingUsersCount( @RequestParam(value = "userId") long userId,
			@RequestParam(value = "userTypeId") Long userTypeId,
			@RequestParam(value = "featureId") Long featureId,
			@RequestParam(value = "userType") String userType) {
		ResponseCountAndQuantity response = dashboardConfServiceImpl.getPendingUsersCount(userTypeId, featureId);
		return new MappingJacksonValue(response);
	}
	
	@ApiOperation(value = "Get total count.", response = DashboardConfDb.class)
	@RequestMapping(path = "/device/countAndQuantity", method = RequestMethod.GET)
	public MappingJacksonValue getDeviceRequestCountAndQuantity( @RequestParam(value = "userId") long userId,
			@RequestParam(value = "userTypeId") Long userTypeId,
			@RequestParam(value = "featureId") Long featureId,
			@RequestParam(value = "userType") String userType) {
		ResponseCountAndQuantity response = dashboardConfServiceImpl.getDeviceRequestCountAndQuantity(userId, userTypeId, featureId, userType);
		return new MappingJacksonValue(response);
	}
	
	@ApiOperation(value = "Get total count.", response = DashboardConfDb.class)
	@RequestMapping(path = "/updateVisa/countAndQuantity", method = RequestMethod.GET)
	public MappingJacksonValue getVisaUpdateRequestCount( @RequestParam(value = "userId") long userId,
			@RequestParam(value = "userTypeId") Long userTypeId,
			@RequestParam(value = "featureId") Long featureId,
			@RequestParam(value = "userType") String userType) {
		ResponseCountAndQuantity response = dashboardConfServiceImpl.getVisaUpdateRequestCount(userId, userTypeId, featureId, userType);
		return new MappingJacksonValue(response);
	}
	
}
