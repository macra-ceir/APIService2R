package com.gl.ceir.config.repository.app;

import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.model.app.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class DatabaseTablesRepository {
    private static final Logger logger = LogManager.getLogger(DatabaseTablesRepository.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    ReportColumnDbRepository reportColumnDbRepository;

    @Autowired
    ReportDbRepository reportDbRepository;

    @Autowired
    PropertiesReader propertiesReader;

    @Autowired
    SystemConfigListRepository systemConfigListRepository;

    @Autowired
    SystemConfigurationDbRepository systemConfigurationDb;

    public DBTableNames getTables(String dbName) {
//        SessionImplementor sessionImp = (org.hibernate.engine.spi.SessionImplementor) em.getDelegate();
        DatabaseMetaData metadata = null;
        DBTableNames dbTableNames = null;
        Connection conn = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
//            metadata = sessionImp.connection().getMetaData();
            metadata = conn.getMetaData();
            res = metadata.getTables(null, null, "%", new String[]{"BASE TABLE"});
            List<String> tables = new ArrayList<String>();
            while (res.next()) {
                tables.add(res.getString("TABLE_NAME"));
            }
            dbTableNames = new DBTableNames();
            dbTableNames.setDbName(dbName);
            Collections.sort(tables);
            dbTableNames.setTableNames(tables);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (res != null)
                try {
                    if (res != null)
                        res.close();
//					if( sessionImp != null )
//						sessionImp.close();
                    if (conn != null)
                        conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
        }
        return dbTableNames;
    }


    @Transactional
    public List<String> getDatabase() {
        List<String> dbList = new ArrayList<String>();
        try {

            String appDb = propertiesReader.appdbName;
            String auddbName = propertiesReader.auddbName;
            String repdbName = propertiesReader.repdbName;
            String oamdbName = propertiesReader.oamdbName;


            dbList.add(appDb);
            dbList.add(auddbName);
            dbList.add(repdbName);
            dbList.add(oamdbName);

            logger.info("all DB list " + dbList);

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return dbList;
    }


    @Transactional
    public DBTableNames getTablesV2(String dbName) {
//        SessionImplementor sessionImp = (org.hibernate.engine.spi.SessionImplementor) em.getDelegate();
        Connection conn = null;
        DBTableNames dbTableNames = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = null;
        List<String> dbList = new ArrayList<>();
        String appDb = propertiesReader.appdbName;
        String auddbName = propertiesReader.auddbName;
        String repdbName = propertiesReader.repdbName;
        String oamdbName = propertiesReader.oamdbName;


        dbList.add(appDb);
        dbList.add(auddbName);
        dbList.add(repdbName);
        dbList.add(oamdbName);

        logger.info("all DB list " + dbList);
        List<String> tables = new ArrayList<String>();
        try {

            logger.info("DB name: " + dbName);
/* 
        List<SystemConfigurationDb> aud=  systemConfigurationDb.getByTag1("dbName");
        List<String> tables = new ArrayList<String>();
        try {
        	conn = this.getConnection();
        	for(int i=0;i<aud.size();i++) {
          	  logger.info("DB name: "+aud.get(i).getValue());
*/

            if (propertiesReader.dialect.toLowerCase().contains("mysql")) {
                query = "SELECT table_name FROM information_schema.tables WHERE table_type = 'BASE TABLE' AND table_schema='" + dbName + "'";
            } else {
                query = "select table_name from user_tables";
            }
            logger.info("query for fetch tables in diffrent data bases " + query);
//            stmt = sessionImp.connection().createStatement();
            stmt = conn.createStatement();
            res = stmt.executeQuery(query);

            while (res.next()) {
                tables.add(res.getString("table_name"));

            }

            dbTableNames = new DBTableNames();
            dbTableNames.setDbName(dbName);
            Collections.sort(tables);
            dbTableNames.setTableNames(tables);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (res != null)
                    res.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return dbTableNames;
    }

    public TableColumnDetails getTableColumns(String dbName, String tableName) {
//        SessionImplementor sessionImp = (org.hibernate.engine.spi.SessionImplementor) em.getDelegate();
        TableColumnDetails tableColumnDetails = null;
        List<String> tempColumns = new ArrayList<String>();
        DatabaseMetaData metadata = null;
        Connection conn = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            metadata = conn.getMetaData();
            res = metadata.getColumns(null, null, tableName, null);
            while (res.next()) {
                tempColumns.add(res.getString("COLUMN_NAME"));
            }
            tableColumnDetails = new TableColumnDetails();
            tableColumnDetails.setDbName(dbName);
            tableColumnDetails.setTableName(tableName);
            // Collections.sort( columns );
            tableColumnDetails.setColumns(tempColumns);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (res != null)
                try {
                    if (res != null)
                        res.close();
                    if (conn != null)
                        conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
        }
        return tableColumnDetails;
    }

    @Transactional
    public TableColumnDetails getTableColumnsV2(String dbName, String tableName) {
        logger.info("inside get table method=");
        SessionImplementor sessionImp = (org.hibernate.engine.spi.SessionImplementor) em.getDelegate();

        TableColumnDetails tableColumnDetails = null;
        List<String> tempColumns = new ArrayList<String>();
        Connection conn = null;
        Statement stmt = null;
        String query = null;
        ResultSet res = null;
        try {
            logger.info("inside try block=");
            if (propertiesReader.dialect.toLowerCase().contains("mysql"))
                query = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS where table_name='" + tableName + "' and table_schema='" + dbName + "'";
            else
                query = "SELECT COLUMN_NAME from user_tab_columns where LOWER(table_name)=LOWER('" + tableName + "') order by column_id";
            logger.info("qurey for fetch tables column " + query);
            logger.info("sesssion connection value" + sessionImp.connection());
            stmt = sessionImp.connection().createStatement();
            logger.info("after session");
            res = stmt.executeQuery(query);
            logger.info("after execute");
 /*
        	conn = this.getConnection();
        	stmt = conn.createStatement();
            res = stmt.executeQuery(query);
> master
*/
            while (res.next()) {
                tempColumns.add(res.getString("COLUMN_NAME"));
            }
            tableColumnDetails = new TableColumnDetails();
            tableColumnDetails.setDbName(dbName);
            tableColumnDetails.setTableName(tableName);
            // Collections.sort( columns );
            tableColumnDetails.setColumns(tempColumns);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (res != null)
                try {
                    if (res != null)
                        res.close();
                    if (conn != null)
                        conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
        }
        return tableColumnDetails;
    }

    public TableData getTableData(TableFilterRequest filterRequest, int pageNumber, int pageSize) {
//        SessionImplementor sessionImp = (org.hibernate.engine.spi.SessionImplementor) em.getDelegate();
        List<Map<String, String>> resultData = null;
        List<String> resultColumns = null;
        Map<String, String> row = null;
        TableData tableData = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet res = null;
        String columns = "";
        String where = "where";
        String query = null;
        try {
            if (Objects.nonNull(filterRequest.getColumns())) {
                for (String column : filterRequest.getColumns()) {
                    columns += column + ",";
                }
                columns = columns.substring(0, columns.length() - 1);
            } else {
                columns = "*";
            }
            query = "SELECT " + columns + " FROM " + filterRequest.getTableName();
            if (Objects.nonNull(filterRequest.getStartDate())) {
                if (propertiesReader.dialect.toLowerCase().contains("mysql"))
                    where = where + " DATE(created_on) >= DATE('" + filterRequest.getStartDate() + "') and";
                else
                    where = where + " to_char(created_on,'YYYY-MM-DD') >= '" + filterRequest.getStartDate() + "' and";
            }
            if (Objects.nonNull(filterRequest.getEndDate())) {
                if (propertiesReader.dialect.toLowerCase().contains("mysql"))
                    where = where + " DATE(created_on) <= DATE('" + filterRequest.getEndDate() + "') and";
                else
                    where = where + " to_char(created_on,'YYYY-MM-DD') <= '" + filterRequest.getStartDate() + "' and";
            }

            if (!where.equals("where")) {
                query = query + " " + where.substring(0, where.lastIndexOf("and"));
            }
            if (propertiesReader.dialect.toLowerCase().contains("mysql")) {
                query = query + " order by created_on desc limit " + (pageNumber * pageSize) + "," + ((pageNumber + 1) * pageSize);
            } else {
//    			if( where.equals("where"))
//    				query = query+" where rownum > "+(pageNumber*pageSize)+" and rownum <= "+((pageNumber+1)*pageSize)+" order by created_on desc";
//    			else
//    				query = query+" and rownum > "+(pageNumber*pageSize)+" and rownum <= "+((pageNumber+1)*pageSize)+" order by created_on desc";
                query = "select * from( " + query + " order by created_on desc) where rownum > " + (pageNumber * pageSize) + " and rownum <= " + ((pageNumber + 1) * pageSize);
            }
            logger.info("Final query:[" + query + "]");
            conn = this.getConnection();
            stmt = conn.createStatement();
            res = stmt.executeQuery(query);
            tableData = new TableData();
            tableData.setDbName(filterRequest.getDbName());
            tableData.setTableName(filterRequest.getTableName());
            resultColumns = new ArrayList<String>();
            resultData = new ArrayList<Map<String, String>>();
            while (res.next()) {
                row = new LinkedHashMap<String, String>();
                for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
                    if (res.isFirst()) {
                        resultColumns.add(res.getMetaData().getColumnName(i));
                    }
                    if (Objects.nonNull(res.getString(i))) {
                        row.put(res.getMetaData().getColumnName(i), res.getString(i));
                    } else {
                        row.put(res.getMetaData().getColumnName(i), "NA");
                    }
                }
                resultData.add(row);
                row = null;
            }
            tableData.setColumns(resultColumns);
            tableData.setRowData(resultData);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (res != null)
                    res.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return tableData;
    }

    public TableData getTableDataV2(TableFilterRequest filterRequest, ReportDb reportDb, int pageNumber, int pageSize) {
//        SessionImplementor sessionImp = (org.hibernate.engine.spi.SessionImplementor) em.getDelegate();
        HashMap<String, String> colHeaderMapping = null;
        List<Map<String, String>> resultData = null;
        List<ReportColumnDb> orderedColumns = null;
        List<String> resultColumns = null;
        Map<String, String> row = null;
        TableData tableData = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = "";
        String where = "where";
        String columns = "";
        try {
            orderedColumns = reportColumnDbRepository.findByReportnameIdOrderByColumnOrderAsc(reportDb.getReportNameId());
            if (orderedColumns != null & orderedColumns.size() > 0) {
                colHeaderMapping = new HashMap<String, String>();
                for (ReportColumnDb column : orderedColumns) {
                    colHeaderMapping.put(column.getColumnName(),
                            reportColumnDbRepository.findByReportnameIdAndColumnName(reportDb.getReportNameId(), column.getColumnName()).getHeaderName());
                }
            }
            if (Objects.nonNull(reportDb.getReportDataQuery())) {
                query = reportDb.getReportDataQuery();
            } else {
                if (Objects.nonNull(filterRequest.getColumns())) {
                    for (String column : filterRequest.getColumns()) {
                        columns += column + ",";
                    }
                    columns = columns.substring(0, columns.length() - 1);
                } else {
                    columns = "*";
                }
//	        	query = "SELECT "+columns+" FROM "+filterRequest.getDbName()+"."+filterRequest.getTableName();
                query = "SELECT " + columns + " FROM " + filterRequest.getTableName();
            }
            if (Objects.nonNull(filterRequest.getStartDate())) {
                if (propertiesReader.dialect.toLowerCase().contains("mysql"))
                    where = where + " DATE(created_on) >= DATE('" + filterRequest.getStartDate() + "') and";
                else
                    where = where + " to_char(created_on,'YYYY-MM-DD') >= '" + filterRequest.getStartDate() + "' and";
            }
            if (Objects.nonNull(filterRequest.getEndDate())) {
                if (propertiesReader.dialect.toLowerCase().contains("mysql"))
                    where = where + " DATE(created_on) <= DATE('" + filterRequest.getEndDate() + "') and";
                else
                    where = where + " to_char(created_on,'YYYY-MM-DD') <= '" + filterRequest.getStartDate() + "' and";
            }
            if (Objects.nonNull(filterRequest.getTxnId()) && Objects.nonNull(reportDb.getTxnIdField())) {
                where = where + " " + reportDb.getTxnIdField() + "='" + filterRequest.getTxnId() + "' and";
            }

            if (!where.equals("where")) {
                query = query + " " + where.substring(0, where.lastIndexOf("and"));
            }
            query = query + " order by modified_on desc";
            logger.info("Final data query: [" + query + "]");
            conn = this.getConnection();
            stmt = conn.createStatement();
            res = stmt.executeQuery(query);
            tableData = new TableData();
            tableData.setDbName(filterRequest.getDbName());
            tableData.setTableName(filterRequest.getTableName());
            resultColumns = new ArrayList<String>();
            resultData = new ArrayList<Map<String, String>>();
            while (res.next()) {
                row = new LinkedHashMap<String, String>();
                if (!Objects.nonNull(filterRequest.getColumns()) && (orderedColumns != null & orderedColumns.size() > 0)) {
                    for (ReportColumnDb column : orderedColumns) {
                        if (res.isFirst()) {
                            resultColumns.add(column.getHeaderName());
                        }
                        if (Objects.nonNull(res.getString(column.getColumnName()))) {
                            row.put(column.getHeaderName(), res.getString(column.getColumnName()));
                        } else {
                            row.put(column.getHeaderName(), "NA");
                        }
                    }
                } else if (Objects.nonNull(filterRequest.getColumns()) && colHeaderMapping != null) {
                    for (String column : filterRequest.getColumns()) {

                        if (res.isFirst()) {
                            resultColumns.add(colHeaderMapping.get(column));
                        }
                        if (Objects.nonNull(res.getString(column))) {
                            row.put(colHeaderMapping.get(column), res.getString(column));
                        } else {
                            row.put(colHeaderMapping.get(column), "NA");
                        }
                    }
                } else if (Objects.nonNull(filterRequest.getColumns()) && Objects.isNull(colHeaderMapping)) {
                    //logger.info("Ordering by requested columns.");
                    for (String column : filterRequest.getColumns()) {
                        if (res.isFirst()) {
                            resultColumns.add(column);
                        }
                        if (Objects.nonNull(res.getString(column))) {
                            row.put(column, res.getString(column));
                        } else {
                            row.put(column, "NA");
                        }
                    }
                } else {
                    for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
                        if (res.isFirst()) {
                            resultColumns.add(res.getMetaData().getColumnName(i));
                        }
                        if (Objects.nonNull(res.getString(i))) {
                            row.put(res.getMetaData().getColumnName(i), res.getString(i));
                        } else {
                            row.put(res.getMetaData().getColumnName(i), "NA");
                        }
                    }
                }
                resultData.add(row);
                row = null;
            }
            logger.info("Result Columns:[" + resultColumns.toString() + "]");
            logger.info("Result Data:[" + resultData.toString() + "]");
            tableData.setColumns(resultColumns);
            tableData.setRowData(resultData);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (res != null)
                    res.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return tableData;
    }

    @Transactional
    public TableDataPageable getTableDataV3(TableFilterRequest filterRequest, ReportDb reportDb, int pageNumber, int pageSize) {
        TableColumnDetails tableColumnDetails = this.getTableColumnsV2(filterRequest.getDbName(), filterRequest.getTableName());
//        SessionImplementor sessionImp = (org.hibernate.engine.spi.SessionImplementor) em.getDelegate();
        List<Map<String, String>> resultData = null;
        List<ReportColumnDb> orderedColumns = null;
        List<String> resultColumns = null;
        TableDataPageable result = null;
        Map<String, String> row = null;
        String orderByColumn = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = "";
        String where = "where";
        String columns = "";
        int numberOfElem = 0;
        long totalEle = 0l;
        try {
            if (Objects.nonNull(tableColumnDetails)) {
                if (tableColumnDetails.getColumns().stream().anyMatch("modified_on"::equalsIgnoreCase))
                    orderByColumn = "modified_on";
                else if (tableColumnDetails.getColumns().stream().anyMatch("created_on"::equalsIgnoreCase))
                    orderByColumn = "created_on";
            }
            result = new TableDataPageable();
            result.setSort(new ReportDataSorting(true, false, false));
            result.setPageable(new DataPageable(new ReportDataSorting(true, false, false), pageSize, pageNumber, 0, false, true));
            if (reportDb != null && Objects.nonNull(reportDb)) {
                if (Objects.nonNull(reportDb.getReportDataQuery()))
                    query = reportDb.getReportDataQuery();
                orderedColumns = reportColumnDbRepository.findByReportnameIdOrderByColumnOrderAsc(reportDb.getReportNameId());
            }
            if (query.equals("")) {
                if (Objects.nonNull(filterRequest.getColumns())) {
                    for (String column : filterRequest.getColumns()) {
                        columns += column + ",";
                    }
                    columns = columns.substring(0, columns.length() - 1);
                } else {
                    columns = "*";
                }
                query = "SELECT " + columns + " FROM " + filterRequest.getDbName() + "." + filterRequest.getTableName();
                logger.info("count query with database: [" + query + "]");
            }
            if (Objects.nonNull(filterRequest.getStartDate()) && !filterRequest.getStartDate().equals("")) {
                if (propertiesReader.dialect.toLowerCase().contains("mysql"))
                    where = where + " DATE(created_on) >= DATE('" + filterRequest.getStartDate() + "') and";
                else
                    where = where + " to_char(created_on,'YYYY-MM-DD') >= '" + filterRequest.getStartDate() + "' and";
            }
            if (Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().equals("")) {
                if (propertiesReader.dialect.toLowerCase().contains("mysql"))
                    where = where + " DATE(created_on) <= DATE('" + filterRequest.getEndDate() + "') and";
                else
                    where = where + " to_char(created_on,'YYYY-MM-DD') <= '" + filterRequest.getEndDate() + "' and";
            }
            if (Objects.nonNull(filterRequest.getTxnId()) && Objects.nonNull(reportDb.getTxnIdField())) {
                where = where + " " + reportDb.getTxnIdField() + "='" + filterRequest.getTxnId() + "' and";
            }

            if (!where.equals("where")) {
                query = query + " " + where.substring(0, where.lastIndexOf("and"));
            }
            if (propertiesReader.dialect.toLowerCase().contains("mysql"))
//           		totalEle = this.getTotalRows( query, sessionImp );
                totalEle = this.getTotalRows(query);
            else
//           		totalEle = this.getTotalRowsOracle( query, sessionImp );
                totalEle = this.getTotalRowsOracle(query);
            if (totalEle > 0) {
                if (propertiesReader.dialect.toLowerCase().contains("mysql")) {
                    if (Objects.nonNull(orderByColumn))
                        query = query + " order by " + orderByColumn + " desc limit " + (pageNumber * pageSize) + "," + ((pageNumber + 1) * pageSize);
                    else
                        query = query + " limit " + (pageNumber * pageSize) + "," + ((pageNumber + 1) * pageSize);
                } else {
                    //    			if( where.equals("where"))
                    //    				query = query+" where rownum > "+(pageNumber*pageSize)+" and rownum <= "+((pageNumber+1)*pageSize)+" order by created_on desc";
                    //    			else
                    //    				query = query+" and rownum > "+(pageNumber*pageSize)+" and rownum <= "+((pageNumber+1)*pageSize)+" order by created_on desc";

//	    			query = "select * from( "+query+" order by created_on desc) where rn > "+(pageNumber*pageSize)+" and rn <= "+((pageNumber+1)*pageSize);
                    if (Objects.nonNull(orderByColumn)) {
                        query = "select * from ( select /*+ FIRST_ROWS(n) */ orderd1.*, ROWNUM rnum from (" + query + " order by " + orderByColumn + " desc) orderd1"
                                + ") orderd2 where rnum > " + (pageNumber * pageSize) + " and rnum <=" + ((pageNumber + 1) * pageSize);
                    } else {
                        query = "select * from ( select /*+ FIRST_ROWS(n) */ orderd1.*, ROWNUM rnum from (" + query + ") orderd1"
                                + ") orderd2 where rnum	> " + (pageNumber * pageSize) + " and rnum <=" + ((pageNumber + 1) * pageSize);
                    }
                }
                logger.info("Final data query: [" + query + "]");
                conn = this.getConnection();
                stmt = conn.createStatement();
                res = stmt.executeQuery(query);
                resultColumns = new ArrayList<String>();
                resultData = new ArrayList<Map<String, String>>();
                //            res.last();
                //            logger.info("Total row count is ["+res.getRow()+"]");
                //            res.beforeFirst();
                while (res.next()) {
                    numberOfElem++;
                    row = new LinkedHashMap<String, String>();
                    if (orderedColumns != null && orderedColumns.size() > 0) {
                        for (ReportColumnDb column : orderedColumns) {
                            if (res.isFirst()) {
                                resultColumns.add(column.getHeaderName());
                            }
                            if (Objects.nonNull(res.getString(column.getColumnName()))) {
                                row.put(column.getHeaderName(), res.getString(column.getColumnName()));
                            } else {
                                row.put(column.getHeaderName(), "NA");
                            }
                        }
                    } else {
                        if (Objects.nonNull(filterRequest.getColumns())) {
                            //logger.info("Ordering by requested columns.");
                            for (String column : filterRequest.getColumns()) {
                                if (res.isFirst()) {
                                    resultColumns.add(column);
                                }
                                if (Objects.nonNull(res.getString(column))) {
                                    row.put(column, res.getString(column));
                                } else {
                                    row.put(column, "NA");
                                }
                            }
                        } else {
                            for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
                                if (!res.getMetaData().getColumnName(i).equalsIgnoreCase("RNUM")) {
                                    if (res.isFirst()) {
                                        resultColumns.add(res.getMetaData().getColumnName(i));
                                    }
                                    if (Objects.nonNull(res.getString(i))) {
                                        row.put(res.getMetaData().getColumnName(i), res.getString(i));
                                    } else {
                                        row.put(res.getMetaData().getColumnName(i), "NA");
                                    }
                                }
                            }
                        }
                    }
                    resultData.add(row);
                    row = null;
                }
                logger.info("Total pages:[" + ((int) Math.ceil((totalEle / (float) pageSize))) + "] and devide:[" + (totalEle / (float) pageSize) + "]");
//	            result.setContent(new TableData( filterRequest.getTableName(), new ArrayList<String>(), resultData));
                result.setContent(new TableData(filterRequest.getTableName(), resultColumns, resultData));
                result.setTotalElements(totalEle);
                result.setTotalPages((int) Math.ceil((totalEle / (float) pageSize)));
                if (((int) Math.ceil((totalEle / (float) pageSize)) - 1) == pageNumber)
                    result.setLast(true);
                else
                    result.setLast(false);
                if (pageNumber == 0)
                    result.setFirst(true);
                else
                    result.setFirst(false);
                result.setNumberOfElements(numberOfElem);
                result.setSize(pageSize);
                result.setNumber(pageNumber);
                result.setEmpty(false);
            } else {
                result.setTotalElements(0);
                result.setTotalPages(0);
                result.setLast(true);
                result.setFirst(true);
                result.setNumberOfElements(0);
                result.setNumber(0);
                result.setSize(pageSize);
                result.setEmpty(true);
                result.setContent(new TableData(filterRequest.getTableName(), new ArrayList<String>(), new ArrayList<Map<String, String>>()));
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (res != null)
                    res.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return result;
    }

    public TableDataPageable getReportData(TableFilterRequest filterRequest, int pageNumber, int pageSize) {
//		SessionImplementor sessionImp = (org.hibernate.engine.spi.SessionImplementor) em.getDelegate();
        List<Map<String, String>> resultData = null;
        List<ReportColumnDb> columnDetails = null;
        TableDataPageable result = null;
        Map<String, String> row = null;
        String searchQuery = null;
        ReportDb reportDb = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = "";
        //  String where   = "where (created_on IS NOT NULL and created_on !='NA') and";
        String where = "where created_on IS NOT NULL and";
        String columns = "";
        int numberOfElem = 0;
        long totalEle = 0l;
        try {
            result = new TableDataPageable();
            result.setSort(new ReportDataSorting(true, false, false));
            result.setPageable(new DataPageable(new ReportDataSorting(true, false, false), pageSize, pageNumber, 0, false, true));
            if (Objects.nonNull(filterRequest.getReportnameId())) {
                reportDb = reportDbRepository.getOne(filterRequest.getReportnameId());
            } else {
                return new TableDataPageable();
            }
            if (Objects.nonNull(filterRequest.getColumns())) {
                for (String column : filterRequest.getColumns()) {
                    columns += column + ",";
                }
                columns = columns.substring(0, columns.length() - 1);
            } else {
                columnDetails = reportColumnDbRepository.findByReportnameIdOrderByColumnOrderAsc(reportDb.getReportNameId());
                for (ReportColumnDb column : columnDetails) {
                    if (propertiesReader.dialect.toLowerCase().contains("mysql")) {
                        columns += column.getColumnName() + " as `" + column.getHeaderName() + "`,";
                    } else {
                        columns += column.getColumnName() + " as \"" + column.getHeaderName() + "\",";
                    }
                }
                columns = columns.substring(0, columns.length() - 1);
            }
//        	if(!propertiesReader.dialect.toLowerCase().contains("mysql")) {
//        		if( columns.equals("*"))
//        			columns = reportDb.getOutputTable()+".*,rownum as rn";
//        		else
//        			columns += ",rownum as rn";
//        	}
            query = "SELECT " + columns + " FROM " + reportDb.getOutputTable();
            if (Objects.nonNull(filterRequest.getStartDate())) {
                if (propertiesReader.dialect.toLowerCase().contains("mysql"))
                    where = where + " DATE(created_on) >= DATE('" + filterRequest.getStartDate() + "') and";
                else
                    where = where + " to_char(created_on,'YYYY-MM-DD') >= '" + filterRequest.getStartDate() + "' and";
            }
            if (Objects.nonNull(filterRequest.getEndDate())) {
                if (propertiesReader.dialect.toLowerCase().contains("mysql"))
                    where = where + " DATE(created_on) <= DATE('" + filterRequest.getEndDate() + "') and";
                else
                    where = where + " to_char(created_on,'YYYY-MM-DD') <= '" + filterRequest.getStartDate() + "' and";
            }
            if (Objects.nonNull(filterRequest.getTxnId()) && Objects.nonNull(reportDb.getTxnIdField())) {
                where = where + " " + reportDb.getTxnIdField() + "='" + filterRequest.getTxnId() + "' and";
            }
            if (!where.equals("where")) {
                query = query + " " + where.substring(0, where.lastIndexOf("and"));
            }
            if (propertiesReader.dialect.toLowerCase().contains("mysql"))
//           		totalEle = this.getTotalRows( query, sessionImp );
                totalEle = this.getTotalRows(query);
            else
//           		totalEle = this.getTotalRowsOracle( query, sessionImp );
                totalEle = this.getTotalRowsOracle(query);
            if (totalEle > 0) {
                if (propertiesReader.dialect.toLowerCase().contains("mysql")) {
                    query = query + " order by created_on desc limit " + (pageNumber * pageSize) + "," + ((pageNumber + 1) * pageSize);
                } else {
//        			if( where.equals("where"))
//        				query = query+" where rownum > "+(pageNumber*pageSize)+" and rownum <= "+((pageNumber+1)*pageSize)+" order by created_on desc";
//        			else
//        				query = query+" and rownum > "+(pageNumber*pageSize)+" and rownum <= "+((pageNumber+1)*pageSize)+" order by created_on desc";

//        			query = "select * from( "+query+" order by created_on desc) where rn > "+(pageNumber*pageSize)+" and rn <= "+((pageNumber+1)*pageSize);
                    query = "select * from ( select /*+ FIRST_ROWS(n) */ orderd1.*, ROWNUM rnum from (" + query + " order by created_on desc) orderd1"
                            + ") orderd2 where rnum	> " + (pageNumber * pageSize) + " and rnum <=" + ((pageNumber + 1) * pageSize);
                }
                logger.info("Final data query: [" + query + "]");
                conn = this.getConnection();
                stmt = conn.createStatement();
                res = stmt.executeQuery(query);
                resultData = new ArrayList<Map<String, String>>();
//	            res.last();
//	            logger.info("Total row count is ["+res.getRow()+"]");
//	            res.beforeFirst();   
                while (res.next()) {
                    numberOfElem++;
                    row = new LinkedHashMap<String, String>();
                    for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
                        if (!res.getMetaData().getColumnName(i).equalsIgnoreCase("RNUM")) {
                            if (Objects.nonNull(res.getString(i))) {
                                row.put(res.getMetaData().getColumnName(i), res.getString(i));
                            } else {
                                row.put(res.getMetaData().getColumnName(i), "NA");
                            }
                        }
                    }
                    resultData.add(row);
                    row = null;
                }
//	            logger.info("Result Data:["+resultData.toString()+"]");
                logger.info("Total pages:[" + ((int) Math.ceil((totalEle / (float) pageSize))) + "] and devide:[" + (totalEle / (float) pageSize) + "]");
                result.setContent(resultData);
                result.setTotalElements(totalEle);
                result.setTotalPages((int) Math.ceil((totalEle / (float) pageSize)));
                if (((int) Math.ceil((totalEle / (float) pageSize)) - 1) == pageNumber)
                    result.setLast(true);
                else
                    result.setLast(false);
                if (pageNumber == 0)
                    result.setFirst(true);
                else
                    result.setFirst(false);
                result.setNumberOfElements(numberOfElem);
                result.setSize(pageSize);
                result.setNumber(pageNumber);
                result.setEmpty(false);
            } else {
                result.setTotalElements(0);
                result.setTotalPages(0);
                result.setLast(true);
                result.setFirst(true);
                result.setNumberOfElements(0);
                result.setNumber(0);
                result.setSize(pageSize);
                result.setEmpty(true);
                result.setContent(new ArrayList<Map<String, String>>());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (res != null)
                    res.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return result;
    }

    public TableDataPageable getReportDataV2(TableFilterRequest filterRequest, int pageNumber, int pageSize) {
//		SessionImplementor sessionImp = (org.hibernate.engine.spi.SessionImplementor) em.getDelegate();
        Connection conn = null;
        List<Map<String, String>> resultData = null;
        List<ReportColumnDb> columnDetails = null;
        List<SystemConfigListDb> typeFlags = null;
        List<String> columnsList = null;
        TableDataPageable result = null;
        Map<String, String> row = null;
        String orderByColumn = null;
        String searchQuery = null;
        String reportTrend = null;
        ReportDb reportDb = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = "";
        //String where   = "where (created_on IS NOT NULL and created_on !='NA') and";
        String where = "where created_on IS NOT NULL and";
        String columns = "";
        int numberOfElem = 0;
        String order = null;
        long totalEle = 0l;
        try {
            result = new TableDataPageable();
            result.setSort(new ReportDataSorting(true, false, false));
            result.setPageable(new DataPageable(new ReportDataSorting(true, false, false), pageSize, pageNumber, 0, false, true));
            typeFlags = systemConfigListRepository.findByTag("Type_Flag", Sort.by("id"));
            if (Objects.nonNull(filterRequest.getTypeFlag()) && !filterRequest.getTypeFlag().equals(0))
                reportTrend = typeFlags.stream().
                        filter(typeFlagDetails -> typeFlagDetails.getValue().equals(filterRequest.getTypeFlag())).findFirst().get().getInterpretation();
            if (Objects.nonNull(filterRequest.getReportnameId())) {
                reportDb = reportDbRepository.getOne(filterRequest.getReportnameId());
            } else {
                return new TableDataPageable();
            }
//        	logger.info(reportDb.toString());
            if (Objects.nonNull(reportDb.getOrderColumnName()))
                orderByColumn = reportDb.getOrderColumnName();
            else {
                if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly")) {
                    orderByColumn = "to_date(created_on,'MON YYYY')";
                } else {
                    orderByColumn = "created_on";
                }
            }
            if (Objects.nonNull(reportDb.getOrderBy()))
                order = reportDb.getOrderBy();
            else
                order = "desc";
            columnsList = new ArrayList<String>();
            if (Objects.nonNull(reportTrend)) {
                columnDetails = reportColumnDbRepository.findByReportnameIdAndTypeFlagOrderByColumnOrderAsc(reportDb.getReportNameId(),
                        filterRequest.getTypeFlag());
            } else {
                columnDetails = reportColumnDbRepository.findByReportnameIdOrderByColumnOrderAsc(reportDb.getReportNameId());
            }
            logger.info("Report column details:[" + columnDetails.toString() + "]");
            if (Objects.nonNull(filterRequest.getColumns())) {
                for (String col : filterRequest.getColumns()) {
                    for (ReportColumnDb column : columnDetails) {
                        if (column.getHeaderName().equalsIgnoreCase(col)) {
                            if (propertiesReader.dialect.toLowerCase().contains("mysql")) {
                                columns += column.getColumnName() + " as `" + column.getHeaderName() + "`,";
                            } else {
                                columns += column.getColumnName() + " as \"" + column.getHeaderName() + "\",";
                            }
                            columnsList.add(column.getHeaderName());
                            break;
                        }
                    }
                }
                columns = columns.substring(0, columns.length() - 1);
            } else {
                for (ReportColumnDb column : columnDetails) {
                    if (propertiesReader.dialect.toLowerCase().contains("mysql")) {
                        columns += column.getColumnName() + " as `" + column.getHeaderName() + "`,";
                    } else {
                        columns += column.getColumnName() + " as \"" + column.getHeaderName() + "\",";
                    }
                    columnsList.add(column.getHeaderName());
                }
                columns = columns.substring(0, columns.length() - 1);
            }
            logger.info("Report table columns list for query:[" + columns + "]");
            if (Objects.nonNull(reportDb.getReportDataQuery())) {
                query = "SELECT " + columns + " FROM (" + reportDb.getReportDataQuery() + ") t1";
            } else {
                if (Objects.nonNull(reportTrend) && !(reportTrend.equalsIgnoreCase("daily") || reportTrend.equalsIgnoreCase("till date")))
                    query = "SELECT " + columns + " FROM " + reportDb.getOutputTable() + "_" + reportTrend.toLowerCase();
                else
                    query = "SELECT " + columns + " FROM " + reportDb.getOutputTable();
            }
            if (Objects.nonNull(filterRequest.getStartDate()) && !filterRequest.getStartDate().equals("")) {
                if (propertiesReader.dialect.toLowerCase().contains("mysql"))
                    where = where + " DATE(created_on) >= DATE('" + filterRequest.getStartDate() + "') and";
                else {
                    if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly"))
                        where = where + " to_char(to_date(created_on,'MON YYYY'),'YYYY-MM-DD') >= '" + filterRequest.getStartDate() + "' and";
                    else if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("yearly"))
                        where = where + " to_char(to_date(created_on,'YYYY'),'YYYY-MM-DD') >= '" + filterRequest.getStartDate() + "' and";
                    else
                        where = where + " to_char(created_on,'YYYY-MM-DD') >= '" + filterRequest.getStartDate() + "' and";
                }
            }
            if (Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().equals("")) {
                if (propertiesReader.dialect.toLowerCase().contains("mysql"))
                    where = where + " DATE(created_on) <= DATE('" + filterRequest.getEndDate() + "') and";
                else {
                    if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly"))
                        where = where + " to_char(to_date(created_on,'MON YYYY'),'YYYY-MM-DD') <= '" + filterRequest.getEndDate() + "' and";
                    else if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("yearly"))
                        where = where + " to_char(to_date(created_on,'YYYY'),'YYYY-MM-DD') <= '" + filterRequest.getEndDate() + "' and";
                    else
                        where = where + " to_char(created_on,'YYYY-MM-DD') <= '" + filterRequest.getEndDate() + "' and";
                }
            }
            if (filterRequest.isLastDate()) {
                if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly"))
                    where = where + " created_on = (select to_char(max(to_date(created_on,'MON YYYY')),'MON YYYY')"
                            + " from " + reportDb.getOutputTable() + "_monthly) and";
                else if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("yearly"))
                    where = where + " created_on = (select max(created_on) from " + reportDb.getOutputTable() + "_yearly) and";
                else
                    where = where + " created_on = (select max(created_on) from " + reportDb.getOutputTable() + ") and";
            } else if (Objects.nonNull(filterRequest.getDayDataLimit())) {
                if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly"))
                    where = where + " created_on >= (select to_char(add_months(max(to_date(created_on,'MON YYYY')),-" + filterRequest.getDayDataLimit() + "),'MON YYYY')"
                            + " from " + reportDb.getOutputTable() + "_monthly) and";
                else if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("yearly"))
                    where = where + " created_on >= (select max(created_on)-" + filterRequest.getDayDataLimit() + " from " + reportDb.getOutputTable() + "_yearly) and";
                else
                    where = where + " created_on >= (select max(created_on)-" + filterRequest.getDayDataLimit() + " from " + reportDb.getOutputTable() + ") and";
            }

            if (Objects.nonNull(filterRequest.getTxnId()) && Objects.nonNull(reportDb.getTxnIdField())) {
                where = where + " " + reportDb.getTxnIdField() + "='" + filterRequest.getTxnId() + "' and";
            }

            if (Objects.nonNull(filterRequest.getSearchString())) {
                where = where + " " + filterRequest.getSearchString() + " and";
            }

            if (!where.equals("where")) {
                query = query + " " + where.substring(0, where.lastIndexOf("and"));
            }
            if (propertiesReader.dialect.toLowerCase().contains("mysql"))
//           		totalEle = this.getTotalRows( query, sessionImp );
                totalEle = this.getTotalRows(query);
            else
//           		totalEle = this.getTotalRowsOracle( query, sessionImp );
                totalEle = this.getTotalRowsOracle(query);
            if (totalEle > 0) {
                if (propertiesReader.dialect.toLowerCase().contains("mysql")) {
                    query = query + " order by " + orderByColumn + " " + order + " limit " + (pageNumber * pageSize) + "," + ((pageNumber + 1) * pageSize);
                } else {
                    query = "select * from ( select /*+ FIRST_ROWS(n) */ orderd1.*, ROWNUM rnum from (" + query + " order by " + orderByColumn + " " + order + ") orderd1"
                            + ") orderd2 where rnum	> " + (pageNumber * pageSize) + " and rnum <=" + ((pageNumber + 1) * pageSize);
                }
                logger.info("Final data query: [" + query + "]");
                conn = this.getConnection();
                stmt = conn.createStatement();
                res = stmt.executeQuery(query);
                resultData = new ArrayList<Map<String, String>>();
//	            res.last();
//	            logger.info("Total row count is ["+res.getRow()+"]");
//	            res.beforeFirst();   
                while (res.next()) {
                    numberOfElem++;
                    row = new LinkedHashMap<String, String>();
                    if (propertiesReader.dialect.toLowerCase().contains("mysql")) {
                        for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
                            if (!res.getMetaData().getColumnName(i).equalsIgnoreCase("RNUM")) {
                                if (Objects.nonNull(res.getString(i))) {
                                    if (res.getMetaData().getColumnTypeName(i).equalsIgnoreCase("date") || res.getMetaData().getColumnTypeName(i).equalsIgnoreCase("Timestamp"))
                                        row.put(columnDetails.get(i - 1).getHeaderName(), res.getString(i));
                                    else
                                        row.put(columnDetails.get(i - 1).getHeaderName(), res.getString(i));
                                } else {
                                    row.put(columnDetails.get(i - 1).getHeaderName(), "NA");
                                }
                            }
                        }
                    } else {
                        for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
                            if (!res.getMetaData().getColumnName(i).equalsIgnoreCase("RNUM")) {
                                if (Objects.nonNull(res.getString(i))) {
                                    if (res.getMetaData().getColumnTypeName(i).equalsIgnoreCase("date") || res.getMetaData().getColumnTypeName(i).equalsIgnoreCase("Timestamp"))
                                        row.put(res.getMetaData().getColumnName(i), this.formatedDate(res.getString(i)));
                                    else
                                        row.put(res.getMetaData().getColumnName(i), res.getString(i));
                                } else {
                                    row.put(res.getMetaData().getColumnName(i), "NA");
                                }
                            }
                        }
                    }
                    resultData.add(row);
                    row = null;
                }
//	            logger.info("Result Data:["+resultData.toString()+"]");
                logger.info("Total pages:[" + ((int) Math.ceil((totalEle / (float) pageSize))) + "] and devide:[" + (totalEle / (float) pageSize) + "]");
                result.setContent(new TableData(reportDb.getReportName(), columnsList, resultData));
                result.setTotalElements(totalEle);
                result.setTotalPages((int) Math.ceil((totalEle / (float) pageSize)));
                if (((int) Math.ceil((totalEle / (float) pageSize)) - 1) == pageNumber)
                    result.setLast(true);
                else
                    result.setLast(false);
                if (pageNumber == 0)
                    result.setFirst(true);
                else
                    result.setFirst(false);
                result.setNumberOfElements(numberOfElem);
                result.setSize(pageSize);
                result.setNumber(pageNumber);
                result.setEmpty(false);
//        		logger.info("Report data response:["+result.getContent().toString()+"]");
            } else {
                result.setTotalElements(0);
                result.setTotalPages(0);
                result.setLast(true);
                result.setFirst(true);
                result.setNumberOfElements(0);
                result.setNumber(0);
                result.setSize(pageSize);
                result.setEmpty(true);
                result.setContent(new TableData(reportDb.getReportName(), columnsList, new ArrayList<Map<String, String>>()));
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (res != null)
                    res.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return result;
    }

    public TableDataPageable getReportDataGroupBy(TableFilterRequest filterRequest, int pageNumber, int pageSize) {
//		SessionImplementor sessionImp = (org.hibernate.engine.spi.SessionImplementor) em.getDelegate();
        List<Map<String, String>> resultData = null;
        List<ReportColumnDb> columnDetails = null;
        List<SystemConfigListDb> typeFlags = null;
        List<String> uniqueValues = null;
        List<String> columnsList = null;
        TableDataPageable result = null;
        Map<String, String> row = null;
        String groupByColumn = null;
        String orderByColumn = null;
        String previousDate = null;
        String reportTrend = null;
        String dateColumn = null;
        ReportDb reportDb = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = "";
        //  String where   = "where (created_on IS NOT NULL and created_on !='NA') and";
        String where = "where created_on IS NOT NULL and";
        String columns = "";
        int numberOfElem = 0;
        String order = null;
        long totalEle = 0l;
        String temp = null;
        try {
            result = new TableDataPageable();
            result.setSort(new ReportDataSorting(true, false, false));
            result.setPageable(new DataPageable(new ReportDataSorting(true, false, false), pageSize, pageNumber, 0, false, true));
            typeFlags = systemConfigListRepository.findByTag("Type_Flag", Sort.by("id"));
            if (Objects.nonNull(filterRequest.getTypeFlag()) && !filterRequest.getTypeFlag().equals(0))
                reportTrend = typeFlags.stream().
                        filter(typeFlagDetails -> typeFlagDetails.getValue().equals(filterRequest.getTypeFlag())).findFirst().get().getInterpretation();
            if (Objects.nonNull(filterRequest.getReportnameId())) {
                reportDb = reportDbRepository.getOne(filterRequest.getReportnameId());
            } else {
                return new TableDataPageable();
            }
            logger.info(reportDb.toString());
            if (Objects.nonNull(reportDb.getOrderColumnName()))
                orderByColumn = reportDb.getOrderColumnName();
            else {
                if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly")) {
                    orderByColumn = "to_date(created_on,'MON YYYY')";
                } else {
                    orderByColumn = "created_on";
                }
            }
            if (Objects.nonNull(reportDb.getOrderBy()))
                order = reportDb.getOrderBy();
            else
                order = "desc";
            columnsList = new ArrayList<String>();
//        	columnDetails = reportColumnDbRepository.findByReportnameIdOrderByColumnOrderAsc( reportDb.getReportnameId() );
            if (Objects.nonNull(reportTrend)) {
                columnDetails = reportColumnDbRepository.findByReportnameIdAndTypeFlagOrderByColumnOrderAsc(reportDb.getReportNameId(),
                        filterRequest.getTypeFlag());
            } else {
                columnDetails = reportColumnDbRepository.findByReportnameIdOrderByColumnOrderAsc(reportDb.getReportNameId());
            }

            logger.info("columnDetails:::" + columnDetails);

            if (Objects.nonNull(filterRequest.getColumns())) {
                for (String col : filterRequest.getColumns()) {
                    for (ReportColumnDb column : columnDetails) {
                        if (column.getHeaderName().equalsIgnoreCase(col)) {
                            if (propertiesReader.dialect.toLowerCase().contains("mysql")) {
                                columns += column.getColumnName() + " as `" + column.getHeaderName() + "`,";
                            } else {
                                columns += column.getColumnName() + " as \"" + column.getHeaderName() + "\",";
                            }
                            columnsList.add(column.getHeaderName());
                            break;
                        }
                        if (column.getHeaderName().equalsIgnoreCase(filterRequest.getGroupBy()))  //Extra
                            groupByColumn = column.getColumnName();   //Extra
                    }
                }
                columns = columns.substring(0, columns.length() - 1);
            } else {
                for (ReportColumnDb column : columnDetails) {
                    if (column.getHeaderName().equalsIgnoreCase(filterRequest.getGroupBy()))//Extra
                        groupByColumn = column.getColumnName();//Extra
                    if (propertiesReader.dialect.toLowerCase().contains("mysql")) {
                        columns += column.getColumnName() + " as `" + column.getHeaderName() + "`,";
                    } else {
                        columns += column.getColumnName() + " as \"" + column.getHeaderName() + "\",";
                    }
                    columnsList.add(column.getHeaderName());
                }
                columns = columns.substring(0, columns.length() - 1);
            }
            if (groupByColumn != null) //Extra
                uniqueValues = this.getUniqueValuesOfColumn(reportDb.getOutputTable(), groupByColumn); //Extra
//        		uniqueValues = this.getUniqueValuesOfColumn( reportDb.getOutputTable(), groupByColumn, sessionImp);

            if (Objects.nonNull(reportDb.getReportDataQuery())) {
                query = "SELECT " + columns + " FROM (" + reportDb.getReportDataQuery() + ") t1";
            } else {
                if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly")) {
                    dateColumn = "Month";  //Extra
                    query = "SELECT " + columns + " FROM " + reportDb.getOutputTable() + "_monthly";
                } else if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("yearly")) {
                    dateColumn = "Year"; //Extra
                    query = "SELECT " + columns + " FROM " + reportDb.getOutputTable() + "_yearly";
                } else {
                    dateColumn = "Date"; //Extra
                    query = "SELECT " + columns + " FROM " + reportDb.getOutputTable();
                }
            }
            if (Objects.nonNull(filterRequest.getStartDate()) && !filterRequest.getStartDate().equals("")) {
                if (propertiesReader.dialect.toLowerCase().contains("mysql"))
                    where = where + " DATE(created_on) >= DATE('" + filterRequest.getStartDate() + "') and";
                else {
                    if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly"))
                        where = where + " to_char(to_date(created_on,'MON YYYY'),'YYYY-MM-DD') >= '" + filterRequest.getStartDate() + "' and";
                    else if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("yearly"))
                        where = where + " to_char(to_date(created_on,'YYYY'),'YYYY-MM-DD') >= '" + filterRequest.getStartDate() + "' and";
                    else
                        where = where + " to_char(created_on,'YYYY-MM-DD') >= '" + filterRequest.getStartDate() + "' and";
                }
            }
            if (Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().equals("")) {
                if (propertiesReader.dialect.toLowerCase().contains("mysql"))
                    where = where + " DATE(created_on) <= DATE('" + filterRequest.getEndDate() + "') and";
                else {
                    if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly"))
                        where = where + " to_char(to_date(created_on,'MON YYYY'),'YYYY-MM-DD') <= '" + filterRequest.getEndDate() + "' and";
                    else if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("yearly"))
                        where = where + " to_char(to_date(created_on,'YYYY'),'YYYY-MM-DD') <= '" + filterRequest.getEndDate() + "' and";
                    else
                        where = where + " to_char(created_on,'YYYY-MM-DD') <= '" + filterRequest.getEndDate() + "' and";
                }
            }
            if (filterRequest.isLastDate()) {
                if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly"))
                    where = where + " created_on = (select to_char(max(to_date(created_on,'MON YYYY')),'MON YYYY')"
                            + " from " + reportDb.getOutputTable() + "_monthly) and";
                else if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("yearly"))
                    where = where + " created_on = (select max(created_on) from " + reportDb.getOutputTable() + "_yearly) and";
                else
                    where = where + " created_on = (select max(created_on) from " + reportDb.getOutputTable() + ") and";
            } else if (Objects.nonNull(filterRequest.getDayDataLimit())) {
                if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly"))
                    where = where + " created_on >= (select to_char(add_months(max(to_date(created_on,'MON YYYY')),-" + filterRequest.getDayDataLimit() + "),'MON YYYY')"
                            + " from " + reportDb.getOutputTable() + "_monthly) and";
                else if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("yearly"))
                    where = where + " created_on >= (select max(created_on)-" + filterRequest.getDayDataLimit() + " from " + reportDb.getOutputTable() + "_yearly) and";
                else
                    where = where + " created_on >= (select max(created_on)-" + filterRequest.getDayDataLimit() + " from " + reportDb.getOutputTable() + ") and";
            }

            if (Objects.nonNull(filterRequest.getTxnId()) && Objects.nonNull(reportDb.getTxnIdField())) {
                where = where + " " + reportDb.getTxnIdField() + "='" + filterRequest.getTxnId() + "' and";
            }

            if (Objects.nonNull(filterRequest.getSearchString())) {
                where = where + " " + filterRequest.getSearchString() + " and";
            }
            if (!where.equals("where")) {
                query = query + " " + where.substring(0, where.lastIndexOf("and"));
            }
            if (propertiesReader.dialect.toLowerCase().contains("mysql"))
//           		totalEle = this.getTotalRows( query, sessionImp );
                totalEle = this.getTotalRows(query);
            else
//           		totalEle = this.getTotalRowsOracle( query, sessionImp );
                totalEle = this.getTotalRowsOracle(query);
            if (totalEle > 0) {
                if (propertiesReader.dialect.toLowerCase().contains("mysql")) {
                    query = query + " order by " + orderByColumn + " " + order + " limit " + (pageNumber * pageSize) + "," + ((pageNumber + 1) * pageSize);
                } else {
                    query = "select * from ( select /*+ FIRST_ROWS(n) */ orderd1.*, ROWNUM rnum from (" + query + " order by " + orderByColumn + " " + order + ") orderd1"
                            + ") orderd2 where rnum	> " + (pageNumber * pageSize) + " and rnum <=" + ((pageNumber + 1) * pageSize * uniqueValues.size());
                }
                logger.info("Final data query: [" + query + "]");
                conn = this.getConnection();
                stmt = conn.createStatement();
                res = stmt.executeQuery(query);
                resultData = new ArrayList<Map<String, String>>();
//	            res.last();
//	            logger.info("Total row count is ["+res.getRow()+"]");
//	            res.beforeFirst();   
                while (res.next()) {
                    numberOfElem++;
                    if (Objects.isNull(previousDate)) {
                        if (dateColumn.equalsIgnoreCase("date"))
                            previousDate = this.formatedDate(res.getString(dateColumn));
                        else
                            previousDate = res.getString(dateColumn);
                        row = new LinkedHashMap<String, String>();
//	            		row.put( dateColumn, this.formatedDate(res.getString(dateColumn)));
                        row.put(dateColumn, previousDate);
                    }
                    if (dateColumn.equalsIgnoreCase("date"))
                        temp = this.formatedDate(res.getString(dateColumn));
                    else
                        temp = res.getString(dateColumn);
                    if (!previousDate.equals(temp)) {
                        for (String col : uniqueValues) {
                            if (!row.containsKey(col))
                                row.put(col, "0");
                        }
                        resultData.add(row);
                        row = null;
                        row = new LinkedHashMap<String, String>();
                        if (dateColumn.equalsIgnoreCase("date"))
                            previousDate = this.formatedDate(res.getString(dateColumn));
                        else
                            previousDate = res.getString(dateColumn);
//	            		row.put( dateColumn, this.formatedDate(res.getString(dateColumn)));
                        row.put(dateColumn, previousDate);
                    }
                    for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
                        if (!res.getMetaData().getColumnName(i).equalsIgnoreCase("RNUM") &&
                                !res.getMetaData().getColumnTypeName(i).equalsIgnoreCase("date") &&
                                !res.getMetaData().getColumnName(i).equalsIgnoreCase(filterRequest.getGroupBy()) &&
                                !res.getMetaData().getColumnName(i).equalsIgnoreCase(dateColumn)) {
//			        		logger.info("Date:["+res.getString(dateColumn)+"],"+filterRequest.getGroupBy()+
//			        				":["+res.getString( filterRequest.getGroupBy() )+"],Count:["+res.getString(i)+"] ["+row.get( filterRequest.getGroupBy())+"]");
                            if (row.containsKey(res.getString(filterRequest.getGroupBy())) && Objects.nonNull(res.getString(filterRequest.getGroupBy())))
                                row.put(res.getString(filterRequest.getGroupBy()),
                                        String.valueOf(Long.valueOf(res.getString(i)) + Long.valueOf(row.get(res.getString(filterRequest.getGroupBy())))));
                            else
                                row.put(res.getString(filterRequest.getGroupBy()), res.getString(i));

                        }
                    }
//			        row = null;
                }
                if (resultData.isEmpty() && row != null)
                    resultData.add(row);
//	            logger.info("Result Data:["+resultData.toString()+"]");
                logger.info("Total pages:[" + ((int) Math.ceil((totalEle / (float) pageSize))) + "] and devide:[" + (totalEle / (float) pageSize) + "]");
                result.setContent(new TableData(reportDb.getReportName(), columnsList, resultData));
                result.setTotalElements(totalEle);
                result.setTotalPages((int) Math.ceil((totalEle / (float) pageSize)));
                if (((int) Math.ceil((totalEle / (float) pageSize)) - 1) == pageNumber)
                    result.setLast(true);
                else
                    result.setLast(false);
                if (pageNumber == 0)
                    result.setFirst(true);
                else
                    result.setFirst(false);
                result.setNumberOfElements(numberOfElem);
                result.setSize(pageSize);
                result.setNumber(pageNumber);
                result.setEmpty(false);
            } else {
                result.setTotalElements(0);
                result.setTotalPages(0);
                result.setLast(true);
                result.setFirst(true);
                result.setNumberOfElements(0);
                result.setNumber(0);
                result.setSize(pageSize);
                result.setEmpty(true);
                result.setContent(new TableData(reportDb.getReportName(), columnsList, new ArrayList<Map<String, String>>()));
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (res != null)
                    res.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return result;
    }

    //	public List< String > getUniqueValuesOfColumn( String tableName, String columnName,  SessionImplementor sessionImp ) {
    public List<String> getUniqueValuesOfColumn(String tableName, String columnName) {
        List<String> uniqueValues = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = null;
        try {
            query = "select distinct " + columnName + " as " + columnName + " from " + tableName + " countQuery";
            logger.info("Total row query:[" + query + "]");
            stmt = this.getConnection().createStatement();
            res = stmt.executeQuery(query);
            uniqueValues = new ArrayList<String>();
            while (res.next()) {
                uniqueValues.add(res.getString(columnName));
            }
            logger.info("Total  unique row values using groupby :[" + uniqueValues.size() + "]");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (res != null)
                    res.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return uniqueValues;
    }

    //	public long getTotalRows( String query,  SessionImplementor sessionImp ) {
    public long getTotalRows(String query) {
        long rows = 0l;
        Statement stmt = null;
        ResultSet res = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            stmt = conn.createStatement();
            logger.info("Total row query:[" + query + "]");
            res = stmt.executeQuery(query);
            res.last();
            rows = res.getRow();
            res.beforeFirst();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (res != null)
                    res.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return rows;
    }

    //	public long getTotalRowsOracle( String query,  SessionImplementor sessionImp ) {
    public long getTotalRowsOracle(String query) {
        long rows = 0l;
        Statement stmt = null;
        ResultSet res = null;
        Connection conn = null;
        try {
            query = "select count(*) from ( " + query + " ) countQuery";
            logger.info("Total row query:[" + query + "]");
            conn = this.getConnection();
            stmt = conn.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                rows = res.getLong(1);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (res != null)
                    res.close();
                if (stmt != null)
                    stmt.close();
//				if( sessionImp != null )
//					sessionImp.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return rows;
    }

    /*
     * public String formatedDate( String date ) { try { date =
     * date.substring(0,date.indexOf(".")); return LocalDateTime.parse(date,
     * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(DateTimeFormatter.
     * ofPattern("yyyy-MM-dd")); }catch (Exception ex) {
     * logger.error(ex.getMessage(), ex); return "NA"; } }
     */

    public String formatedDate(String date) {
        try {
            if (date.contains(".")) {
                date = date.substring(0, date.indexOf("."));
                return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else {
                return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return date;
        }
    }

    public Connection getConnection() {
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) em.getEntityManagerFactory();
        try {
            return info.getDataSource().getConnection();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
