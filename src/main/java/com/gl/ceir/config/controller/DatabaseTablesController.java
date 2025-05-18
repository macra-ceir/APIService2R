package com.gl.ceir.config.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.model.app.DBTableNames;
import com.gl.ceir.config.model.app.TableColumnDetails;
import com.gl.ceir.config.model.app.TableData;
import com.gl.ceir.config.model.app.TableFilterRequest;
import com.gl.ceir.config.service.impl.DatabaseTablesServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
public class DatabaseTablesController {
	
	private static final Logger logger = LogManager.getLogger(DatabaseTablesController.class);
	
	@Autowired
	DatabaseTablesServiceImpl databaseTablesServiceImpl;
	
	@ApiOperation(value = "Get all tables.", response = DBTableNames.class)
	@RequestMapping(path = "/db/tables", method = {RequestMethod.POST})
	public MappingJacksonValue getAllTables(@RequestParam(value = "dbName") String dbName, 
			@RequestParam(value = "userId") Long userId,
			@RequestParam(value = "userType") String userType,
			@RequestParam(value = "featureId") Long featureId,
			@RequestParam(value = "publicIp", defaultValue="NA") String publicIp,
			@RequestParam(value = "browser", defaultValue="NA") String browser) {
		return new MappingJacksonValue( databaseTablesServiceImpl.getTableNames( dbName, userId, featureId, userType, publicIp, browser ) );
	}
	
	@ApiOperation(value = "Get all tables.", response = DBTableNames.class)
	@RequestMapping(path = "/db/tables/V2", method = {RequestMethod.POST})
	public MappingJacksonValue getAllTablesV2(@RequestParam(value = "dbName") String dbName,
			@RequestParam(value = "reportStatus") Integer reportStatus) {
		return new MappingJacksonValue( databaseTablesServiceImpl.getTableNamesV2( dbName) );
	}
	
	@ApiOperation(value = "Get all tables.", response = TableColumnDetails.class)
	@RequestMapping(path = "/db/table/details", method = {RequestMethod.POST})
	public MappingJacksonValue getTableColumns(@RequestParam(value = "dbName") String dbName,
												@RequestParam(value = "tableName") String tableName) {
		return new MappingJacksonValue( databaseTablesServiceImpl.getTableColumnNames(dbName, tableName) );
	}
	@ApiOperation(value = "Get all DB.", response = TableColumnDetails.class)
	@RequestMapping(path = "/db/table/db", method = {RequestMethod.POST})
	public MappingJacksonValue getDBList() {
		List<String> dbList=databaseTablesServiceImpl.getDBList();
		return new MappingJacksonValue(dbList);
	}
	
	@ApiOperation(value = "Get table data.", response = TableData.class)
	@RequestMapping(path = "/db/table/data", method = {RequestMethod.POST})
	public MappingJacksonValue getTableData(@RequestBody TableFilterRequest filterRequest,
												@RequestParam(value = "pageNumber", defaultValue="0") int pageNumber,
												@RequestParam(value = "pageSize", defaultValue="10") int pageSize) {
		return new MappingJacksonValue( databaseTablesServiceImpl.getTableData(filterRequest, pageNumber, pageSize) );
	}
	
	@ApiOperation(value = "Get table data.", response = TableData.class)
	@RequestMapping(path = "/db/table/data/V2", method = {RequestMethod.POST})
	public MappingJacksonValue getTableDataV2(@RequestBody TableFilterRequest filterRequest,
												@RequestParam(value = "pageNumber", defaultValue="0") int pageNumber,
												@RequestParam(value = "pageSize", defaultValue="10") int pageSize) {
		logger.info("Datatable filter request:["+filterRequest.toString()+"]");
		return new MappingJacksonValue( databaseTablesServiceImpl.getTableDataV2(filterRequest, pageNumber, pageSize) );
	}
	
	@ApiOperation(value = "Get table data.", response = TableData.class)
	@RequestMapping(path = "/db/table/data/V3", method = {RequestMethod.POST})
	public MappingJacksonValue getTableDataV3(@RequestBody TableFilterRequest filterRequest,
												@RequestParam(value = "pageNumber", defaultValue="0") int pageNumber,
												@RequestParam(value = "pageSize", defaultValue="10") int pageSize,
												@RequestParam(value = "file", defaultValue="0", required=false) int file) {
		logger.info("Datatable filter request:["+filterRequest.toString()+"]");
		if( file == 0 )
			return new MappingJacksonValue( databaseTablesServiceImpl.getTableDataV3(filterRequest, pageNumber, pageSize) );
		else
			return new MappingJacksonValue( databaseTablesServiceImpl.getTableDataInFile(filterRequest, pageNumber, pageSize) );
	}
	
}
