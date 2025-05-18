package com.gl.ceir.config.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.configuration.FileStorageProperties;
import com.gl.ceir.config.model.app.FileDetails;
import com.gl.ceir.config.model.app.FileDumpMgmt;
import com.gl.ceir.config.model.app.FileDumpFilterRequest;
import com.gl.ceir.config.service.impl.ListFileDetailsImpl;

import io.swagger.annotations.ApiOperation;

@RestController
public class FileDumpController {
	@Autowired
	ListFileDetailsImpl  listFileDetailsImpl;

	@Autowired
	FileStorageProperties fileStorageProperties;

	private static final Logger logger = LogManager.getLogger(FileDumpController.class);



	@ApiOperation(value = "View blackList and Grey List FileDetails.", response = FileDumpMgmt.class)
	@RequestMapping(path = "/filedump/filter", method = RequestMethod.POST)

	public MappingJacksonValue getFileDumpData(@RequestBody FileDumpFilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file) {

		logger.info("File dump View Details Request =["+filterRequest.toString()+"], pageNo:["+pageNo+"], pageSize:["+pageSize+"] and file:["+file+"]");
		MappingJacksonValue mapping = null;
		//List<FileDumpMgmt> fileDetails =	listFileDetailsImpl.getByListType(listType);
		if( file.equals(0)) {
			Page<FileDumpMgmt> fileDetails = listFileDetailsImpl.getFilterPagination(filterRequest, pageNo, pageSize);
			mapping = new MappingJacksonValue(fileDetails);
		}else {
			FileDetails fileDetails = listFileDetailsImpl.getFilterInFileV2(filterRequest, pageNo, pageSize);
			mapping = new MappingJacksonValue(fileDetails);
		}

		return mapping;
	}



	@ApiOperation(value = "Download FileDump File.", response = FileDetails.class)
	@RequestMapping(path = "/filedump/downloadFile", method = RequestMethod.GET)
	public MappingJacksonValue downloadFileDumpFile(@RequestParam(value = "fileName") String fileName) {

		logger.info("FileDump File DownloadRequest FileName="+fileName);

		FileDetails fileDetails = listFileDetailsImpl.getFile(fileName);
		
		return new MappingJacksonValue(fileDetails);
	}
}
