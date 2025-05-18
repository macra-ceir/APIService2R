package com.gl.ceir.config.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.configuration.FileStorageProperties;
import com.gl.ceir.config.model.app.RequestCountAndQuantity;
import com.gl.ceir.config.model.app.EndUserGrievance;
import com.gl.ceir.config.model.app.FileDetails;
import com.gl.ceir.config.model.app.Grievance;
import com.gl.ceir.config.model.app.GrievanceMsg;
import com.gl.ceir.config.model.app.GrievanceMsgWithUser;
import com.gl.ceir.config.model.app.GrievanceReply;
import com.gl.ceir.config.model.app.ResponseCountAndQuantity;
import com.gl.ceir.config.model.app.GrievanceGenricResponse;
import com.gl.ceir.config.model.app.GrievanceFilterRequest;
import com.gl.ceir.config.model.app.GrievanceHistory;
import com.gl.ceir.config.service.impl.GrievanceServiceImpl;
import com.gl.ceir.config.util.Utility;

import io.swagger.annotations.ApiOperation;

@RestController
public class GrievanceController {
	private static final Logger logger = LogManager.getLogger(GrievanceController.class);

	@Autowired
	GrievanceServiceImpl grievanceServiceImpl;
	@Autowired
	FileStorageProperties fileStorageProperties;
	@Autowired
	Utility utility;
	
	@ApiOperation(value = "Add new grievance.", response = GrievanceGenricResponse.class)
	@RequestMapping(path = "/grievance/save", method = {RequestMethod.POST})
	public GrievanceGenricResponse uploadFile(@RequestBody Grievance grievance) {
		logger.info("New Grievance Request="+grievance);
		GrievanceGenricResponse genricResponse = grievanceServiceImpl.save(grievance);
		logger.info("New Grievance Response="+genricResponse);
		return genricResponse;
	}
	
//	@ApiOperation(value = "View filtered grievance", response = Grievance.class)
//	@PostMapping("v1/filter/grievance")
//	public MappingJacksonValue filterGrievances(@RequestBody GrievanceFilterRequest filterRequest,
//			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
//			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
//
//		logger.info("Request to view filtered Grievances = " + filterRequest);
//		List<Grievance>  grievance =  grievanceServiceImpl.getFilterGrievances(filterRequest, pageNo, pageSize);
//		MappingJacksonValue mapping = new MappingJacksonValue(grievance);
//		logger.info("Response of view filtered Grievances ="+mapping);
//		return mapping;
//	}

	@ApiOperation(value = "pagination View filtered grievance", response = Grievance.class)
	@PostMapping("v2/filter/grievance")
	public MappingJacksonValue withPaginationGrievances(@RequestBody GrievanceFilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file) {
		MappingJacksonValue mapping = null;
		logger.info("Request to view filtered grievance = " + filterRequest+", pageNo:["+pageNo+"], pageSize:["+pageSize+"] and file:["+file+"]");
		if( file == 0) {
			Page<Grievance>  grievance =  grievanceServiceImpl.getFilterPaginationGrievances(filterRequest, pageNo, pageSize);
			mapping = new MappingJacksonValue(grievance);
		}else {
			FileDetails fileDetails = null;
			if( filterRequest.getUserType().equalsIgnoreCase("ceiradmin") )
				fileDetails = grievanceServiceImpl.getFilterGrievancesInFileCEIRAdmin(filterRequest, pageNo, pageSize);
			else if(filterRequest.getUserType().equalsIgnoreCase("Customer Care") )
				fileDetails = grievanceServiceImpl.getFilterGrievancesInFileCEIRAdmin(filterRequest, pageNo, pageSize);
			else
				fileDetails = grievanceServiceImpl.getFilterGrievancesInFileV2(filterRequest, pageNo, pageSize);
			mapping = new MappingJacksonValue(fileDetails);
		}
		logger.info("Response of view filtered Grievances ="+mapping);
		return mapping;
	}
	
	@ApiOperation(value = "Add new grievance Message", response = GrievanceGenricResponse.class)
	@RequestMapping(path = "/grievance/saveMessage", method = {RequestMethod.POST})
	public GrievanceGenricResponse saveGrievanceMessage(@RequestBody GrievanceReply grievanceReply) {
		logger.info("New Grievance message Request="+grievanceReply);
		GrievanceGenricResponse genricResponse = grievanceServiceImpl.saveGrievanceMsg(grievanceReply);
		logger.info("New Grievance Response="+genricResponse);
		return genricResponse;
	}
	
	/**This api will give all open grievances**/
	@ApiOperation(value = "View all the list of grievance messages", response = GrievanceMsgWithUser.class)
	@RequestMapping(path = "/grievance/msg", method = {RequestMethod.GET})
	public MappingJacksonValue getGrievanceMessagesByGrievanceId(@RequestParam("userId") Long userId,@RequestParam("grievanceId") String grievanceId
			,@RequestParam("recordLimit") Integer recordLimit,
			@RequestParam(value = "publicIp", defaultValue="NA") String publicIp,
			@RequestParam(value = "browser", defaultValue="NA") String browser,
			@RequestParam(value = "userType", defaultValue="NA") String userType,
			@RequestParam(value = "featureId", defaultValue="NA") Long featureId) {
		logger.info("Request to view all messages of grievance="+userId+", Grievance Id:["+grievanceId+"] and Record Limit:["+recordLimit+"]");
		List<GrievanceMsgWithUser>  msgs =  grievanceServiceImpl.getAllGrievanceMessagesByGrievanceId(grievanceId, recordLimit, userId,
				userType, publicIp, browser, featureId);
		MappingJacksonValue response = new MappingJacksonValue(msgs);
		logger.info("Response of view all messages of grievance ="+response);
		return response;
	}
	
	@ApiOperation(value = "pagination View filtered grievance history", response = Grievance.class)
	@PostMapping("v2/filter/grievanceHistory")
	public MappingJacksonValue withPaginationGrievanceHistory(@RequestBody GrievanceFilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

		logger.info("Request to view filtered grievance history = " + filterRequest);
		Page<GrievanceHistory>  grievance =  grievanceServiceImpl.getFilterPaginationGrievanceHistory(filterRequest, pageNo, pageSize);
		MappingJacksonValue mapping = new MappingJacksonValue(grievance);
		logger.info("Response of view filtered grievance history ="+mapping);
		return mapping;
	}
	
	@ApiOperation(value = "Add end user new grievance.", response = GrievanceGenricResponse.class)
	@RequestMapping(path = "/grievance/endUserSave", method = {RequestMethod.POST})
	public GrievanceGenricResponse saveEndUserGrievance(@RequestBody EndUserGrievance endUserGrievance) {
		logger.info("New End User Grievance Request="+endUserGrievance);
		GrievanceGenricResponse genricResponse = grievanceServiceImpl.saveEndUserGrievance( endUserGrievance );
		logger.info("New End User Grievance Response="+genricResponse);
		return genricResponse;
	}
	
}
