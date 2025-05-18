package com.gl.ceir.config.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.model.app.FileDetails;
import com.gl.ceir.config.model.app.GenricResponse;
import com.gl.ceir.config.model.app.GrievanceGenricResponse;
import com.gl.ceir.config.model.app.TypeApproveFilter;
import com.gl.ceir.config.model.app.TypeApprovedDb;
import com.gl.ceir.config.service.impl.TypeApproveServiceImpl;
import com.gl.ceir.config.util.HttpResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping("/TypeApproved")
public class TypeApprovedController {

	private static final Logger log = LogManager.getLogger(TypeApprovedController.class);
	
	@Autowired
	TypeApproveServiceImpl  typeApproveService;
	
	@ApiOperation(value = "type approve data", response = TypeApprovedDb.class)
	@PostMapping("/view") 
	public MappingJacksonValue viewTypeApproveData(@RequestBody TypeApproveFilter filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file){
		MappingJacksonValue mapping=null;
		if( file == 0) {
			Page<TypeApprovedDb> typeApprovedData  = typeApproveService.viewTypeApprovdeData(filterRequest, pageNo, pageSize);
			mapping = new MappingJacksonValue(typeApprovedData);
		}
		else { 
			FileDetails fileDetails = null;
			if( filterRequest.getUserType().equalsIgnoreCase("CEIRAdmin"))
				fileDetails = typeApproveService.getFilterTACInFileCEIRAdmin(filterRequest, pageNo,pageSize);
			else
				fileDetails = typeApproveService.getFilterTACInFileV2(filterRequest, pageNo,pageSize);
			mapping = new MappingJacksonValue(fileDetails); 
		}
		 return mapping;
	}
	
	@PostMapping("/add")
	@ApiOperation( value = "Add type approve data",response = GenricResponse.class)
	public GenricResponse addTypeApproveData(@RequestBody TypeApprovedDb typeApprovedDb) {
		log.info("Going to add new TypeApprovedDb["+typeApprovedDb+"]");
		return typeApproveService.saveTypeApprove(typeApprovedDb);
	}

	@ApiOperation( value = "View Approve data by Id",response =HttpResponse.class)
	@PostMapping("viewById/{id}")
	public ResponseEntity<?> addTypeApproveData(@RequestParam(value = "id") long id,
			@RequestParam(value = "publicIp", defaultValue="NA") String publicIp,
			@RequestParam(value = "browser", defaultValue="NA") String browser,
			@RequestParam(value = "userId") long userId,
			@RequestParam(value = "userType") String userType) {
		return typeApproveService.viewTypeApproveById(id, userId, userType, publicIp, browser);
	}
	
	@PostMapping("/delete")
	@ApiOperation( value = "delete type approve data",response =HttpResponse.class)
	public HttpResponse deleteTypeApproveData(@RequestParam(required = false) Long id,
			@RequestParam long userId,
			@RequestParam String userType,
			@RequestParam(required = false) Integer deleteFlag,
			@RequestParam(required = false) String txnId,
			@RequestParam(required = false) String remark,
			@RequestParam(value = "publicIp", defaultValue="NA") String publicIp,
			@RequestParam(value = "browser", defaultValue="NA") String browser) {
		return typeApproveService.deleteTypeApprove( id, txnId, userId, userType, deleteFlag, remark, publicIp, browser );  
	}
	
	@PostMapping("/update")
	@ApiOperation( value = "update type approve data",response =GenricResponse.class)
	public GenricResponse  updateTypeApproveData(@RequestBody TypeApprovedDb typeApprovedDb) {
		log.info("Going to update TypeApprovedDb["+typeApprovedDb+"]");
		return typeApproveService.updateTypeApprove(typeApprovedDb);
	}
	
	@PostMapping("/approveReject")
	@ApiOperation( value = "Approve-Reject type approve data",response =GenricResponse.class)
	public GenricResponse  approveRejectTypeApprove(@RequestBody TypeApprovedDb typeApprovedDb) {
		log.info("Going to Approve-Reject TypeApproved["+typeApprovedDb+"]");
		return typeApproveService.updateTypeApproveRejectStatus(typeApprovedDb);
	}

	
//	@ApiOperation(value = "Download TAC File.", response = FileDetails.class)
//	@RequestMapping(path = "/downloadFile", method = RequestMethod.GET)
//	public MappingJacksonValue downloadOperatorFile(@RequestParam(value = "fileName") String fileName) {
//		log.info("Operator File DownloadRequest FileName="+fileName);
//		FileDetails fileDetails = typeApproveService.getFile(fileName);
//		return new MappingJacksonValue(fileDetails);
//	}
	
}
