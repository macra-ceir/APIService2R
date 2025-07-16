package com.gl.ceir.config.service.impl;

import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.app.*;
import com.gl.ceir.config.model.app.AuditTrail;
import com.gl.ceir.config.repository.app.*;
import com.gl.ceir.config.repository.app.AuditTrailRepository;
import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl {

	private static final Logger logger = LogManager.getLogger(ReportServiceImpl.class);
	
	@Autowired
	ReportDbRepository reportDbRepository;
	@Autowired
	SystemConfigListRepository systemConfigListRepository;
	@Autowired
	DatabaseTablesRepository databaseTablesRepository;
	@Autowired
	ReportColumnDbRepository reportColumnDbRepository;
	@Autowired
	SystemConfigurationDbRepository systemConfigurationDbRepository;
	@Autowired
	PropertiesReader propertiesReader;
	@Autowired
	ReportFreqDbRepository reportFreqDbRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AuditTrailRepository auditTrailRepository;
	@Autowired
	StakeholderFeatureRepository stakeholderFeatureRepository;
	
	
	public List<ReportDb> getAllReport( Integer reportCategory, Long userId,
			String userType, Long featureId, String publicIp, String browser){
		List<SystemConfigListDb> viewFlags = null;
		List<SystemConfigListDb> typeFlags = null;
		List<ReportFreqDb> reportsFreqTemp = null;
		List<ReportFreqDb> reportsFreq = null;
		List<ReportDb> reportList = null;
		List<ReportTrend> trends = null;
		Integer viewStatus = 1;
		try {
			typeFlags = systemConfigListRepository.findByTag("Type_Flag", Sort.by( "id"));
			reportsFreq = reportFreqDbRepository.findAll();
			for( SystemConfigListDb viewFlag : viewFlags ) {
				if( viewFlag.getInterpretation().equalsIgnoreCase("Yes")) {
					viewStatus = viewFlag.getValue();
				}
			}
			reportList = reportDbRepository.findByViewFlagAndReportCategoryOrderByReportOrder(viewStatus, reportCategory);
			logger.info("Report details:["+reportList.toString()+"]");
			for( ReportDb reportDb : reportList ) {
				reportsFreqTemp = reportsFreq.stream().filter( reportFreq -> reportFreq.getReportnameId().equals( reportDb.getReportNameId() ))
						.collect(Collectors.toList());
				trends = new ArrayList<ReportTrend>();
				for( ReportFreqDb reportFreq : reportsFreqTemp ) {
					trends.add( new ReportTrend( reportFreq.getTypeFlag(), typeFlags.stream().
							filter( typeFlagDetails -> typeFlagDetails.getValue().equals(reportFreq.getTypeFlag())).findFirst().get().getInterpretation()));
				}
				reportDb.setReportTrends(trends);
			}
			User user = userRepository.getByid( userId );
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName(stakeholderFeatureRepository.findById(featureId).get().getName());
			auditTrail.setSubFeature("View Report List");
			auditTrail.setFeatureId( featureId );
			auditTrail.setUserId( userId );
			auditTrail.setUserName( user.getUsername());
			auditTrail.setUserType( userType );
			auditTrail.setRoleType( userType );
			auditTrail.setTxnId( "NA" );
			auditTrail.setPublicIp(publicIp);
			auditTrail.setBrowser(browser);
			auditTrailRepository.save(auditTrail);
			return reportList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public ReportDb getSingleReportDetail( Long reportNameId ){
		List<SystemConfigListDb> typeFlags = null;
		List<ReportFreqDb> reportsFreq = null;
		List<ReportTrend> trends = null;
		Map<String,String> reportColumns= null;
		ReportDb reportDb = null;
		try {
			typeFlags = systemConfigListRepository.findByTag("Type_Flag", Sort.by( "id"));
			reportsFreq = reportFreqDbRepository.findByReportnameIdOrderByTypeFlagAsc( reportNameId );
			reportDb  = reportDbRepository.findByReportnameId( reportNameId );
;
			trends = new ArrayList<ReportTrend>();
			for( ReportFreqDb reportFreq : reportsFreq ) {
				trends.add( new ReportTrend( reportFreq.getTypeFlag(), typeFlags.stream().
						filter( typeFlagDetails -> typeFlagDetails.getValue().equals(reportFreq.getTypeFlag())).findFirst().get().getInterpretation()));
			}

			reportDb.setReportTrends(trends);
			reportDb.setReportColumns(getColumNameHeaderName(reportNameId));
			return reportDb;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public TableColumnDetails getColumns( Long reportnameId ){
		List<ReportColumnDb> reportColumns = null;
		TableColumnDetails tcd = new TableColumnDetails();
		List<String> columnList = null;
		try {
			reportColumns = reportColumnDbRepository.findByReportnameIdOrderByColumnOrderAsc(reportnameId);
			columnList = new ArrayList<String>();
			if( reportColumns != null ) {
				for( ReportColumnDb column : reportColumns ) {
					columnList.add( column.getHeaderName());
				}
				tcd.setColumns(columnList);
			}
			return tcd;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}


	public Map<String,String> getColumNameHeaderName( Long reportnameId ){
	//	List<ReportColumnDb> reportColumns = null;
		Map<String,String> columnmap = new HashMap<>();
		try {
		var	reportColumns = reportColumnDbRepository.findByReportnameIdOrderByColumnOrderAsc(reportnameId);
			if( reportColumns != null ) {
				for( ReportColumnDb column : reportColumns ) {
					columnmap.put(column.getHeaderName(),column.getColumnName());
				}
			}
			return columnmap;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public TableColumnDetails getColumns( Long reportnameId, Integer typeFlag ){
		List<ReportColumnDb> reportColumns = null;
		TableColumnDetails tcd = new TableColumnDetails();
		List<String> columnList = null;
		try {
//			reportColumns = reportColumnDbRepository.findByReportnameIdOrderByColumnOrderAsc(reportnameId);
			reportColumns = reportColumnDbRepository.findByReportnameIdAndTypeFlagOrderByColumnOrderAsc( reportnameId, typeFlag );
			columnList = new ArrayList<String>();
			if( reportColumns != null ) {
				for( ReportColumnDb column : reportColumns ) {
					columnList.add( column.getHeaderName());
				}
				tcd.setColumns(columnList);
			}
			return tcd;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public TableDataPageable getReportData( TableFilterRequest filterRequest, int pageNumber, int pageSize ){
		try {
			User user = userRepository.getByid( filterRequest.getUserId() );
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName(stakeholderFeatureRepository.findById(filterRequest.getFeatureId()).get().getName());
			if( !this.reportDefaultFilter(filterRequest))
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
			if( Objects.nonNull( filterRequest.getGroupBy()))
				return databaseTablesRepository.getReportDataGroupBy(filterRequest, pageNumber, pageSize);
			else
				return databaseTablesRepository.getReportDataV2(filterRequest, pageNumber, pageSize);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public FileDetails downloadReportData( TableFilterRequest filterRequest, int pageNumber, int pageSize ){
		int i = 0;
		String url = null;
		String fileName = null;
		Writer writer   = null;
		String[] columns = null;
		String[] rowData = null;
		TableData tableData = null;
		CSVWriter csvWriter = null;
		TableDataPageable tableDataPageable = null;
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		String filePath = systemConfigurationDbRepository.getByTag("file.download-dir").getValue();
		try {
			User user = userRepository.getByid( filterRequest.getUserId() );
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName(stakeholderFeatureRepository.findById(filterRequest.getFeatureId()).get().getName());
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
			pageNumber = 0;
			url = systemConfigurationDbRepository.getByTag("file.download-link").getValue();
			pageSize  = Integer.valueOf(systemConfigurationDbRepository.getByTag("file.max-file-record").getValue());
			tableDataPageable = databaseTablesRepository.getReportDataV2(filterRequest, pageNumber, pageSize );
			if( Objects.nonNull(tableDataPageable.getContent())) {
				tableData = (TableData)tableDataPageable.getContent();
				fileName  = (LocalDateTime.now().format(dtf2)+"_"+tableData.getTableName()+".csv").replace(" ", "_");
				writer    = Files.newBufferedWriter(Paths.get(filePath+fileName));
//				csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
//						CSVWriter.DEFAULT_LINE_END);
				csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
						CSVWriter.DEFAULT_LINE_END);
				columns = tableData.getColumns().toArray(new String[tableData.getColumns().size()]);
				csvWriter.writeNext(columns);
				for( Map<String, String> temp : tableData.getRowData()) {
//					logger.info("Data to write:["+temp.toString()+"] and columns:["+tableData.getColumns()+"]");
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
//			return new FileDetails( fileName, filePath, url+fileName );
			
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
	
	public boolean reportDefaultFilter( TableFilterRequest filterRequest ) {
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
