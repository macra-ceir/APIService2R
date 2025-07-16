package com.gl.ceir.config.service.impl;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.app.DBTableNames;
import com.gl.ceir.config.model.app.FileDetails;
import com.gl.ceir.config.model.app.ReportDb;
import com.gl.ceir.config.model.app.TableColumnDetails;
import com.gl.ceir.config.model.app.TableData;
import com.gl.ceir.config.model.app.TableDataPageable;
import com.gl.ceir.config.model.app.TableFilterRequest;
import com.gl.ceir.config.model.app.User;
import com.gl.ceir.config.model.app.AuditTrail;
import com.gl.ceir.config.repository.app.DatabaseTablesRepository;
import com.gl.ceir.config.repository.app.ReportDbRepository;
import com.gl.ceir.config.repository.app.StakeholderFeatureRepository;
import com.gl.ceir.config.repository.app.SystemConfigListRepository;
import com.gl.ceir.config.repository.app.SystemConfigurationDbRepository;
import com.gl.ceir.config.repository.app.UserRepository;
import com.gl.ceir.config.repository.app.AuditTrailRepository;
import com.opencsv.CSVWriter;

@Service
public class DatabaseTablesServiceImpl {
	private static final Logger logger = LogManager.getLogger(DatabaseTablesServiceImpl.class);
	
	@Autowired
	DatabaseTablesRepository databaseTablesRepository;
	@Autowired
	ReportDbRepository reportDbRepository;
	@Autowired
	SystemConfigListRepository systemConfigListRepository;
	@Autowired
	SystemConfigurationDbRepository systemConfigurationDbRepository;
	@Autowired
	ReportServiceImpl reportServiceImpl;
	@Autowired
	PropertiesReader propertiesReader;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AuditTrailRepository auditTrailRepository;
	@Autowired
	StakeholderFeatureRepository stakeholderFeatureRepository;
	
	public DBTableNames getTableNames( String dbName, Long userId, Long featureId, String userType,
			String publicIp, String browser ){
		try {
			User user = userRepository.getByid( userId );
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName("DB Tables");
			auditTrail.setSubFeature("ViewList");
			auditTrail.setFeatureId( featureId );
			auditTrail.setUserId( userId );
			auditTrail.setUserName( user.getUsername());
			auditTrail.setUserType( userType );
			auditTrail.setRoleType( userType );
			auditTrail.setTxnId( "NA" );
			auditTrail.setPublicIp(publicIp);
			auditTrail.setBrowser(browser);
			auditTrailRepository.save(auditTrail);
			logger.info("coming in service 2");
			return databaseTablesRepository.getTablesV2(dbName);
		}catch( Exception e ) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public DBTableNames getTableNamesV2( String dbName ){
		List<String> reportNames = null;
		List<ReportDb> reportList = null;
		DBTableNames dbTableNames = new DBTableNames();
		try {
			reportList = reportDbRepository.findAll();
			dbTableNames.setDbName(dbName);
			reportNames = new ArrayList<String>();
			for( ReportDb reportDb : reportList ) {
				reportNames.add( reportDb.getReportName());
			}
			dbTableNames.setTableNames(reportNames);
			return dbTableNames;
		}catch( Exception e ) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public TableColumnDetails getTableColumnNames( String dbName, String tableName ){
		ReportDb reportDb = null;
		try {
			reportDb = reportDbRepository.findByReportName( tableName );
			if( reportDb != null )
				 {logger.info("report db is not null "+reportDb);
				return reportServiceImpl.getColumns(reportDb.getReportNameId());
				 }
				else {
					logger.info("report db is  null "+reportDb);
					return databaseTablesRepository.getTableColumnsV2( dbName, tableName );
				}
		}catch( Exception e ) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public List<String> getDBList(){
		try {
			logger.info("going to get db details"); 
			List<String> dbList=databaseTablesRepository.getDatabase();
			logger.info("DB list"+dbList);
			 return dbList;
				
		}catch( Exception e ) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public TableData getTableData( TableFilterRequest filterRequest, int pageNumber, int pageSize ){
		try {
			return databaseTablesRepository.getTableData( filterRequest, pageNumber,  pageSize);
		}catch( Exception e ) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public TableData getTableDataV2( TableFilterRequest filterRequest, int pageNumber, int pageSize){
		ReportDb reportDb = null;
		try {
			reportDb = reportDbRepository.findByReportName( filterRequest.getTableName());
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setSubFeature("History");
			auditTrail.setFeatureName(stakeholderFeatureRepository.findById(filterRequest.getFeatureId()).get().getName());
			auditTrail.setFeatureId( filterRequest.getFeatureId() );
			auditTrail.setUserId( filterRequest.getUserId() );
			if( Objects.nonNull( filterRequest.getUserId() )) {
				User user = userRepository.getByid( filterRequest.getUserId() );
				auditTrail.setUserName( user.getUsername());
			}else {
				auditTrail.setUserName( "NA");
			}
			auditTrail.setUserType( filterRequest.getUserType() );
			auditTrail.setRoleType( filterRequest.getUserType() );
			auditTrail.setTxnId( filterRequest.getTxnId());
			auditTrail.setPublicIp(filterRequest.getPublicIp());
			auditTrail.setBrowser(filterRequest.getBrowser());
			if(auditTrail.getSubFeature().equalsIgnoreCase("History") && auditTrail.getUserType().equalsIgnoreCase("CEIRAdmin") ) {
				auditTrail.setTxnId( filterRequest.getNewTxnID());
			}
			auditTrailRepository.save(auditTrail);
			if( reportDb != null ) {
				return databaseTablesRepository.getTableDataV2(filterRequest, reportDb , pageNumber, pageSize);
			}else {
				return databaseTablesRepository.getTableData( filterRequest, pageNumber,  pageSize);
			}
		}catch( Exception e ) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public TableDataPageable getTableDataV3( TableFilterRequest filterRequest, int pageNumber, int pageSize){
		ReportDb reportDb = null;
		try {
			reportDb = reportDbRepository.findByReportName( filterRequest.getTableName());
			User user = userRepository.getByid( filterRequest.getUserId() );
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName("DB Tables");
			if( !this.tableDefaultFilter(filterRequest))
				auditTrail.setSubFeature("Filter");
			else
				auditTrail.setSubFeature("View All");
			auditTrail.setFeatureId( filterRequest.getFeatureId() );
			auditTrail.setUserId( filterRequest.getUserId() );
			auditTrail.setUserName( user.getUsername());
			auditTrail.setUserType( filterRequest.getUserType() );
			auditTrail.setRoleType( filterRequest.getUserType() );
			auditTrail.setTxnId( "NA" );
			auditTrail.setPublicIp(filterRequest.getPublicIp());
			auditTrail.setBrowser(filterRequest.getBrowser());
			auditTrailRepository.save(auditTrail);
			return databaseTablesRepository.getTableDataV3(filterRequest, reportDb , pageNumber, pageSize);
		}catch( Exception e ) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	@Transactional
	public FileDetails getTableDataInFile( TableFilterRequest filterRequest, int pageNumber, int pageSize){
		int i = 0;
		String url = null;
		String fileName = null;
		Writer writer   = null;
		String[] columns = null;
		String[] rowData = null;
		ReportDb reportDb = null;
		TableData tableData = null;
		CSVWriter csvWriter = null;
		TableDataPageable tableDataPageable = null;
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		String filePath = systemConfigurationDbRepository.getByTag("file.download-dir").getValue();
		try {
			pageNumber = 0;
			url = systemConfigurationDbRepository.getByTag("file.download-link").getValue();
			User user = userRepository.getByid( filterRequest.getUserId() );
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName("DB Tables");
			auditTrail.setSubFeature("Export");
			auditTrail.setFeatureId( filterRequest.getFeatureId() );
			auditTrail.setUserId( filterRequest.getUserId() );
			auditTrail.setUserName( user.getUsername());
			auditTrail.setUserType( filterRequest.getUserType() );
			auditTrail.setRoleType( filterRequest.getUserType() );
			auditTrail.setTxnId( "NA" );
			auditTrail.setPublicIp(filterRequest.getPublicIp());
			auditTrail.setBrowser(filterRequest.getBrowser());
			auditTrailRepository.save(auditTrail);
			reportDb = reportDbRepository.findByReportName( filterRequest.getTableName());
			pageSize  = Integer.valueOf(systemConfigurationDbRepository.getByTag("file.max-file-record").getValue());
			tableDataPageable = databaseTablesRepository.getTableDataV3(filterRequest, reportDb , pageNumber, pageSize);
			if( Objects.nonNull(tableDataPageable.getContent())) {
				tableData = (TableData)tableDataPageable.getContent();
				fileName  = (LocalDateTime.now().format(dtf2)+"_"+tableData.getTableName()+".csv").replace(" ", "_");
				writer    = Files.newBufferedWriter(Paths.get(filePath+fileName));
				csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
						CSVWriter.DEFAULT_LINE_END);
//				csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
//						CSVWriter.DEFAULT_LINE_END);
				columns = tableData.getColumns().toArray(new String[tableData.getColumns().size()]);
				csvWriter.writeNext(columns);
				for( Map<String, String> temp : tableData.getRowData()) {
					rowData = new String[columns.length];
					i = 0;
					for( String col : columns ) {
						rowData[i] = temp.get(col);
						i++;
					}
					csvWriter.writeNext(rowData);
					rowData = null;
				}
				csvWriter.flush();
			}
			return new FileDetails( fileName, filePath, url.replace("$LOCAL_IP", propertiesReader.localIp)+fileName );
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			try {
				if(writer != null )
					writer.close();
				if( csvWriter != null )
					csvWriter.close();
			}catch( Exception ex) {}
		}
	}
	
	public boolean tableDefaultFilter( TableFilterRequest filterRequest ) {
		try {
			if(Objects.nonNull(filterRequest.getStartDate()) && !filterRequest.getStartDate().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().equals(""))
				return Boolean.FALSE;
			if(Objects.nonNull(filterRequest.getTxnId()) && !filterRequest.getTxnId().equals(""))
				return Boolean.FALSE;
		}catch( Exception ex) {
			logger.error(ex.getMessage(), ex);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
}
