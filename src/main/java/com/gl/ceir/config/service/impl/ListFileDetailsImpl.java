package com.gl.ceir.config.service.impl;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.configuration.FileStorageProperties;
import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.app.FileDetails;
import com.gl.ceir.config.model.app.FileDumpMgmt;
import com.gl.ceir.config.model.app.FileListFileModel;
import com.gl.ceir.config.model.app.Grievance;
import com.gl.ceir.config.model.app.FileDumpFilterRequest;
import com.gl.ceir.config.model.app.SearchCriteria;
import com.gl.ceir.config.model.app.User;
import com.gl.ceir.config.model.aud.AuditTrail;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.FileDumpOrderColumnMapping;
import com.gl.ceir.config.model.constants.FileDumpType;
import com.gl.ceir.config.model.constants.FileType;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.repository.app.FileDumpMgmtRepository;
import com.gl.ceir.config.repository.app.StakeholderFeatureRepository;
import com.gl.ceir.config.repository.app.SystemConfigurationDbRepository;
import com.gl.ceir.config.repository.app.UserRepository;
import com.gl.ceir.config.repository.aud.AuditTrailRepository;
import com.gl.ceir.config.specificationsbuilder.FileDumpSpecificationBuilder;
import com.gl.ceir.config.specificationsbuilder.GenericSpecificationBuilder;
import com.gl.ceir.config.util.CustomMappingStrategy;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Service
public class ListFileDetailsImpl {

	private static final Logger logger = LogManager.getLogger(ListFileDetailsImpl.class);


	@Autowired
	FileDumpMgmtRepository fileDumpMgmtRepository;
	@Autowired
	PropertiesReader propertiesReader;
	@Autowired
	FileStorageProperties fileStorageProperties;
	@Autowired
	SystemConfigurationDbRepository systemConfigurationDbRepository;
	@Autowired
	AuditTrailRepository auditTrailRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	StakeholderFeatureRepository stakeholderFeatureRepository;

	public List<FileDumpMgmt> getByListType(String listType){
		try {

			return fileDumpMgmtRepository.getByServiceDump(listType);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	
	public Page<FileDumpMgmt> getFilterPagination( FileDumpFilterRequest filterRequest, Integer pageNo, Integer pageSize) {
		boolean isDefaultFilter = true;
		Pageable pageable = null;
		try {
			Sort.Direction direction;
			if ( Objects.nonNull(filterRequest.getOrder()) && filterRequest.getOrder().equalsIgnoreCase("asc") ) {
				direction = Sort.Direction.ASC;
			} else {
				direction = Sort.Direction.DESC;				
			}
			if( Objects.nonNull( filterRequest.getOrderColumnName()) && 
					Objects.nonNull(FileDumpOrderColumnMapping.getColumnMapping(filterRequest.getOrderColumnName()))) {
				pageable = PageRequest.of(pageNo, pageSize,
						new Sort(direction, FileDumpOrderColumnMapping.getColumnMapping(filterRequest.getOrderColumnName()).name()));
			} else {
				pageable = PageRequest.of(pageNo, pageSize, new Sort(direction, "modifiedOn"));
			}
//			Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(Sort.Direction.DESC, "modifiedOn"));
			GenericSpecificationBuilder<FileDumpMgmt> osb = new GenericSpecificationBuilder<FileDumpMgmt>(propertiesReader.dialect);
			if(Objects.nonNull(filterRequest.getStartDate()) && !filterRequest.getStartDate().equals("")) {
				isDefaultFilter = false;
				osb.with(new SearchCriteria("createdOn", filterRequest.getStartDate() , SearchOperation.GREATER_THAN, Datatype.DATE));
			}
			
			if(Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().equals("")) {
				isDefaultFilter = false;
				osb.with(new SearchCriteria("createdOn",filterRequest.getEndDate() , SearchOperation.LESS_THAN, Datatype.DATE));
			}
			
			if(Objects.nonNull(filterRequest.getFileType()) && !filterRequest.getFileType().equals(-1)) {
				isDefaultFilter = false;
				osb.with(new SearchCriteria("fileType", filterRequest.getFileType(), SearchOperation.EQUALITY, Datatype.INT));
			}
			
			if(Objects.nonNull(filterRequest.getFileName()) && !filterRequest.getFileName().equals("")) {
				isDefaultFilter = false;
				osb.with(new SearchCriteria("fileName", filterRequest.getFileName(), SearchOperation.LIKE, Datatype.STRING));
			}
			
			if(Objects.nonNull(filterRequest.getServiceDump()) && !filterRequest.getServiceDump().equals(-1))
				osb.with(new SearchCriteria("serviceDump", filterRequest.getServiceDump(), SearchOperation.EQUALITY, Datatype.INT));

			if(Objects.nonNull(filterRequest.getSearchString()) && !filterRequest.getSearchString().equals("")){
				if( filterRequest.getFileType() == null || filterRequest.getFileType().equals(-1) ) {
					for( FileType fileType : FileType.values()) {
						if( fileType.toString().toLowerCase().contains( filterRequest.getSearchString().toLowerCase() )) {
							osb.orSearch(new SearchCriteria("fileType", fileType.getCode(), SearchOperation.EQUALITY, Datatype.INT));
						}
					}
				}
				osb.orSearch(new SearchCriteria("fileName", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			}
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName(stakeholderFeatureRepository.findById(filterRequest.getFeatureId()).get().getName());
			if( !isDefaultFilter )
				auditTrail.setSubFeature("Filter");
			else
				auditTrail.setSubFeature("View All");
			auditTrail.setFeatureId(filterRequest.getFeatureId());
			if( Objects.nonNull(filterRequest.getPublicIp()))
				auditTrail.setPublicIp(filterRequest.getPublicIp());
			if( Objects.nonNull(filterRequest.getBrowser()))
				auditTrail.setBrowser(filterRequest.getBrowser());
			if( Objects.nonNull(filterRequest.getUserId()) ) {
				User user = userRepository.getByid( filterRequest.getUserId());
				auditTrail.setUserId( filterRequest.getUserId() );
				auditTrail.setUserName( user.getUsername());
			}else {
				auditTrail.setUserName( "NA");
			}
			if( Objects.nonNull(filterRequest.getUserType()) ) {
				auditTrail.setUserType( filterRequest.getUserType());
				auditTrail.setRoleType( filterRequest.getUserType() );
			}else {
				auditTrail.setUserType( "NA" );
				auditTrail.setRoleType( "NA" );
			}
			auditTrail.setTxnId("NA");
			auditTrailRepository.save(auditTrail);
			return fileDumpMgmtRepository.findAll(osb.build(), pageable);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}

	
	public FileDetails getFilterInFileV2(FileDumpFilterRequest filterRequest, Integer pageNo, Integer pageSize) {
		String fileName = null;
		Writer writer   = null;
		FileListFileModel flm = null;
		DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String filePath  = systemConfigurationDbRepository.getByTag("file.download-dir").getValue();
		StatefulBeanToCsvBuilder<FileListFileModel> builder = null;
		StatefulBeanToCsv<FileListFileModel> csvWriter      = null;
		List< FileDumpMgmt> fileRecords = null;
		List<FileListFileModel> fileDetails  = null;
		CustomMappingStrategy<FileListFileModel> mappingStrategy = new CustomMappingStrategy<FileListFileModel>();
		try {
			pageNo = 0;
			pageSize = Integer.valueOf(systemConfigurationDbRepository.getByTag("file.max-file-record").getValue());
			fileRecords = this.getFilterPagination(filterRequest, pageNo, pageSize).getContent();
			if( FileDumpType.getActionNames( filterRequest.getServiceDump() ).toString().equalsIgnoreCase("black") )
				fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")).replace(" ", "_")+"_Black_List.csv";
			else
				fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")).replace(" ", "_")+"_Gray_List.csv";
			writer = Files.newBufferedWriter(Paths.get(filePath+fileName));
			mappingStrategy.setType(FileListFileModel.class);
			builder = new StatefulBeanToCsvBuilder<FileListFileModel>(writer);
			csvWriter = builder.withMappingStrategy(mappingStrategy).withSeparator(',').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
			if( fileRecords.size() > 0 ) {
				fileDetails = new ArrayList<FileListFileModel>();
				for( FileDumpMgmt fdm : fileRecords ) {
					flm = new FileListFileModel();
					flm.setCreatedOn( fdm.getCreatedOn().format(dtf));
					flm.setFileName( fdm.getFileName() );
					flm.setFileType( fdm.getFileTypeInterp() );
					fileDetails.add(flm);
				}
				csvWriter.write(fileDetails);
			}else {
				csvWriter.write( new FileListFileModel());
			}
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName(stakeholderFeatureRepository.findById(filterRequest.getFeatureId()).get().getName());
			auditTrail.setSubFeature("Export");
			auditTrail.setFeatureId(filterRequest.getFeatureId());
			if( Objects.nonNull(filterRequest.getPublicIp()))
				auditTrail.setPublicIp(filterRequest.getPublicIp());
			if( Objects.nonNull(filterRequest.getBrowser()))
				auditTrail.setBrowser(filterRequest.getBrowser());
			if( Objects.nonNull(filterRequest.getUserId()) ) {
				User user = userRepository.getByid( filterRequest.getUserId());
				auditTrail.setUserId( filterRequest.getUserId() );
				auditTrail.setUserName( user.getUsername());
			}else {
				auditTrail.setUserName( "NA");
			}
			if( Objects.nonNull(filterRequest.getUserType()) ) {
				auditTrail.setUserType( filterRequest.getUserType());
				auditTrail.setRoleType( filterRequest.getUserType() );
			}else {
				auditTrail.setUserType( "NA" );
				auditTrail.setRoleType( "NA" );
			}
			auditTrail.setTxnId("NA");
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

	public FileDetails getFile(String fileName) {
		//String filePath  = fileStorageProperties.getFileDumpDownloadDir();
		
		try {
			//return new FileDetails( fileName, filePath, fileStorageProperties.getFileDumpDownloadLink()+fileName );
			if( fileName.toLowerCase().contains("black")) {
				String filePath  = systemConfigurationDbRepository.getByTag("BLACKLIST_FILEPATH").getValue();
				return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("BLACKLIST_DOWNLOAD_LINK").getValue().replace("$LOCAL_IP",
						propertiesReader.localIp)+fileName );
			}else {
//				return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("GREYLIST_DOWNLOAD_LINK").getValue()+fileName );
				String filePath  = systemConfigurationDbRepository.getByTag("GREYLIST_FILEPATH").getValue();
				return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("GREYLIST_DOWNLOAD_LINK").getValue().replace("$LOCAL_IP",
						propertiesReader.localIp)+fileName );
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	

}
