package com.gl.ceir.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.model.app.GenricResponse;
import com.gl.ceir.config.model.app.UploadedFileDB;
import com.gl.ceir.config.service.impl.UploadedFileDBServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
public class UploadedFileDBController {
	
	@Autowired
	UploadedFileDBServiceImpl uploadedFileDBServiceImpl;
	
	@ApiOperation(value = "Make entry uploaded file.", response = GenricResponse.class)
	@RequestMapping(path = "/uploadedFile/save", method = {RequestMethod.POST})
	public GenricResponse saveUploadedFileEntry( @RequestBody UploadedFileDB uploadedFileDB) {
		return uploadedFileDBServiceImpl.saveUploadedFileEntry(uploadedFileDB);
	}
	
}
