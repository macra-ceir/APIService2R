package com.gl.ceir.config.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.configuration.FileStorageProperties;
import com.gl.ceir.config.model.app.GenricResponse;
import com.gl.ceir.config.model.app.SingleImeiDetails;
import com.gl.ceir.config.service.impl.StolenAndRecoveryServiceImpl;
import com.gl.ceir.config.util.Utility;

import io.swagger.annotations.ApiOperation;

@RestController
public class StolenAndRecoveryController {

	private static final Logger logger = LogManager.getLogger(StolenAndRecoveryController.class);

	@Autowired
	StolenAndRecoveryServiceImpl stolenAndRecoveryServiceImpl;

	@Autowired
	FileStorageProperties fileStorageProperties;

	@Autowired
	Utility utility;
	
	@ApiOperation(value = "Report Single Block Details.", response = GenricResponse.class)
	@RequestMapping(path = "/stakeholder/uploadSingle/block", method = RequestMethod.POST)
	public GenricResponse uploadSingleReportBlock(@RequestBody SingleImeiDetails singleImeiDetails)
	{
		logger.info("Single Block Upload Request="+singleImeiDetails.toString());

		GenricResponse genricResponse =	stolenAndRecoveryServiceImpl.blockSigleImei(singleImeiDetails);
		logger.info("Single Block Upload Response ="+genricResponse);

		return genricResponse;
	}
	
	@ApiOperation(value = "Update Single Block Details.", response = GenricResponse.class)
	@RequestMapping(path = "/stakeholder/updateSingle/blockUnblock", method = RequestMethod.POST)
	public GenricResponse updateSingleBlockUnblock(@RequestBody SingleImeiDetails singleImeiDetails)
	{
		logger.info("Single Block Update Request="+singleImeiDetails);

		GenricResponse genricResponse =	stolenAndRecoveryServiceImpl.updateSigleBlockImei(singleImeiDetails);
		logger.info("Single Block Update Response ="+genricResponse);

		return genricResponse;
	}
	
	@ApiOperation(value = "Single Block/Unblock Details..", response = SingleImeiDetails.class)
	@RequestMapping(path = "/stakeholder/view/singleImei", method = RequestMethod.POST)
	public MappingJacksonValue getAllBlockUnblockByTxnId(@RequestParam(value = "txnId") String txnId) {

		MappingJacksonValue mapping = null;

		logger.info("View request to Block/Unblock Device By TxnId = " +  txnId);
		mapping = new MappingJacksonValue(stolenAndRecoveryServiceImpl.getBlockUnblockDetailsByTxnId(txnId));
		logger.info("Record Response of Stolen And Recovery Info="+mapping);

		return mapping;
	}
}