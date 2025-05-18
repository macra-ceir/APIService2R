package com.gl.ceir.config.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.model.app.GenricResponse;
import com.gl.ceir.config.model.app.UploadedFileDB;
import com.gl.ceir.config.model.constants.CopyStatus;
import com.gl.ceir.config.repository.app.UploadedFileDBRepository;

@Service
public class UploadedFileDBServiceImpl {
	
	private static final Logger logger = LogManager.getLogger(UploadedFileDBServiceImpl.class);
	
	@Autowired
	UploadedFileDBRepository uploadedFileDBRepository;
	
	public GenricResponse saveUploadedFileEntry( UploadedFileDB uploadedFileDB ) {
		try {
			logger.info("Make uploaded file entry: "+uploadedFileDB.toString());
			uploadedFileDB.setCopyStatus(CopyStatus.NEW);
			uploadedFileDB = uploadedFileDBRepository.save(uploadedFileDB);
			if( uploadedFileDB != null)
				return new GenricResponse(200,"Uploaded file entry sucessfully saved",uploadedFileDB.getTxnId(),"FILE_UPLOAD_ENTRY_SUCCESS");
			else
				return new GenricResponse(500,"Uploaded file entry failed to saved","0","FILE_UPLOAD_ENTRY_FAIL");
		}catch(Exception e) {
			logger.info("Exception found ="+e.getMessage());
			logger.error(e.getMessage(), e);
			return new GenricResponse(409,"Oops something wrong happened","0","FILE_UPLOAD_ENTRY_ERROR");
		}
	}
}
