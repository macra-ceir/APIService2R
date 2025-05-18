package com.gl.ceir.config.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

//import com.gl.ceir.config.genericresponse.GenricResponse_Class;
//import com.gl.ceir.config.model.FeatureValidateReq;
import com.gl.ceir.config.request.model.Generic_Response_Notification;
//import com.gl.ceir.config.request.model.Port;
//import com.gl.ceir.config.util.HttpResponse;
@Component
@Service
@FeignClient(url = "http://139.59.74.2:9508/CEIRCode", value = "dsj" )
public interface UserFeignClient {

//	@PostMapping(value="/periodValidate")
//	public HttpResponse validatePeriod(FeatureValidateReq featureValidateReq) ;
//
//	@PostMapping(value="/usertypeStatus/{usertypeId}")
//	public GenricResponse_Class usertypeStatus(@PathVariable("usertypeId") Integer usertypeId) ;
//
//
//	@PostMapping("/nameById/{id}")
//	public GenricResponse_Class nameById(@PathVariable("id") Integer id);
//
//	@PostMapping("/portAddress/viewById/{id}")
//	public GenricResponse_Class portAddressInterp(@PathVariable("id") Integer id);
//
//
//	@PostMapping("/userMgmt/DataByPortAndUserType/") 
//	public Generic_Response_Notification getUserIDByPortAddress(Port registeration);

	@PostMapping("/userMgmt/databyUserTypeId/{usertypeId}") 
	public Generic_Response_Notification ceirInfoByUserTypeId(@PathVariable("usertypeId") Integer usertypeId);



}