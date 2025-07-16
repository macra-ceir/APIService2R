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
public class GraphServiceImplOld {

    private static final Logger logger = LogManager.getLogger(GraphServiceImplOld.class);

    @Autowired
    ReportDbRepository reportDbRepository;
    @Autowired
    SystemConfigListRepository systemConfigListRepository;
    @Autowired
    GraphDbTablesRepository graphDbTablesRepository;
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


    public List<ReportDb> getAllReport(Integer reportCategory, Long userId, String userType, Long featureId, String publicIp, String browser) {
        List<SystemConfigListDb> viewFlags = null;
        List<SystemConfigListDb> typeFlags = null;
        List<ReportFreqDb> reportsFreqTemp = null;
        List<ReportFreqDb> reportsFreq = null;
        List<ReportDb> reportList = null;
        List<ReportTrend> trends = null;
        Integer viewStatus = 1;
        try {
            viewFlags = systemConfigListRepository.findByTag("View_Flag", Sort.by("id"));
            typeFlags = systemConfigListRepository.findByTag("Type_Flag", Sort.by("id"));
            reportsFreq = reportFreqDbRepository.findAll();
            for (SystemConfigListDb viewFlag : viewFlags) {
                if (viewFlag.getInterpretation().equalsIgnoreCase("Yes")) {
                    viewStatus = viewFlag.getValue();
                }
            }
            reportList = reportDbRepository.findByViewFlagAndReportCategoryOrderByReportOrder(viewStatus, reportCategory);
            logger.info("Report details:[" + reportList.toString() + "]");
            for (ReportDb reportDb : reportList) {
                reportsFreqTemp = reportsFreq.stream()
                        .filter(reportFreq -> reportFreq.getReportnameId().equals(reportDb.getReportNameId()))
                        .collect(Collectors.toList());
                trends = new ArrayList<ReportTrend>();
                for (ReportFreqDb reportFreq : reportsFreqTemp) {
                    trends.add(new ReportTrend(reportFreq.getTypeFlag(), typeFlags.stream().
                            filter(typeFlagDetails -> typeFlagDetails.getValue().equals(reportFreq.getTypeFlag())).findFirst().get().getInterpretation()));
                }
                reportDb.setReportTrends(trends);
            }
            User user = userRepository.getByid(userId);
//            AuditTrail auditTrail = new AuditTrail();
//            auditTrail.setFeatureName(stakeholderFeatureRepository.findById(featureId).get().getName());
//            auditTrail.setSubFeature("View Report List");
//            auditTrail.setFeatureId(featureId);
//            auditTrail.setUserId(userId);
//            auditTrail.setUserName(user.getUsername());
//            auditTrail.setUserType(userType);
//            auditTrail.setRoleType(userType);
//            auditTrail.setTxnId("NA");
//            auditTrail.setPublicIp(publicIp);
//            auditTrail.setBrowser(browser);
       ///     auditTrailRepository.save(auditTrail);
            return reportList;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
        }
    }

    public ReportDb getSingleReportDetail(Long reportNameId) {
        List<SystemConfigListDb> typeFlags = null;
        List<ReportFreqDb> reportsFreq = null;
        List<ReportTrend> trends = null;
        Map<String, String> reportColumns = null;
        ReportDb reportDb = null;
        try {
            typeFlags = systemConfigListRepository.findByTag("Type_Flag", Sort.by("id"));
            reportsFreq = reportFreqDbRepository.findByReportnameIdOrderByTypeFlagAsc(reportNameId);
            reportDb = reportDbRepository.findByReportnameId(reportNameId);
            trends = new ArrayList<ReportTrend>();
            for (ReportFreqDb reportFreq : reportsFreq) {
                trends.add(new ReportTrend(reportFreq.getTypeFlag(), typeFlags.stream().
                        filter(typeFlagDetails -> typeFlagDetails.getValue().equals(reportFreq.getTypeFlag())).findFirst().get().getInterpretation()));
            }
            reportDb.setReportTrends(trends);
            reportDb.setReportColumns(getColumNameHeaderName(reportNameId));
            return reportDb;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
        }
    }

    public TableColumnDetails getColumns(Long reportnameId) {
        List<ReportColumnDb> reportColumns = null;
        TableColumnDetails tcd = new TableColumnDetails();
        List<String> columnList = null;
        try {
            reportColumns = reportColumnDbRepository.findByReportnameIdOrderByColumnOrderAsc(reportnameId);
            columnList = new ArrayList<String>();
            if (reportColumns != null) {
                for (ReportColumnDb column : reportColumns) {
                    columnList.add(column.getHeaderName());
                }
                tcd.setColumns(columnList);
            }
            return tcd;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
        }
    }


    public Map<String, String> getColumNameHeaderName(Long reportnameId) {
        //	List<ReportColumnDb> reportColumns = null;
        Map<String, String> columnmap = new HashMap<>();
        try {
            var reportColumns = reportColumnDbRepository.findByReportnameIdOrderByColumnOrderAsc(reportnameId);
            if (reportColumns != null) {
                for (ReportColumnDb column : reportColumns) {
                    columnmap.put(column.getHeaderName(), column.getColumnName());
                }
            }
            return columnmap;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
        }
    }

    public TableColumnDetails getColumns(Long reportnameId, Integer typeFlag) {
        List<ReportColumnDb> reportColumns = null;
        TableColumnDetails tcd = new TableColumnDetails();
        List<String> columnList = null;
        try {
//			reportColumns = reportColumnDbRepository.findByReportnameIdOrderByColumnOrderAsc(reportnameId);
            reportColumns = reportColumnDbRepository.findByReportnameIdAndTypeFlagOrderByColumnOrderAsc(reportnameId, typeFlag);
            columnList = new ArrayList<String>();
            if (reportColumns != null) {
                for (ReportColumnDb column : reportColumns) {
                    columnList.add(column.getHeaderName());
                }
                tcd.setColumns(columnList);
            }
            return tcd;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
        }
    }


    public FileDetails downloadReportData(TableFilterRequest filterRequest, int pageNumber, int pageSize) {
        int i = 0;
        String url = null;
        String fileName = null;
        Writer writer = null;
        String[] columns = null;
        String[] rowData = null;
        TableData tableData = null;
        CSVWriter csvWriter = null;
        TableDataPageable tableDataPageable = null;
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String filePath = systemConfigurationDbRepository.getByTag("file.download-dir").getValue();
        try {
            User user = userRepository.getByid(filterRequest.getUserId());
            AuditTrail auditTrail = new AuditTrail();
            auditTrail.setFeatureName(stakeholderFeatureRepository.findById(filterRequest.getFeatureId()).get().getName());
            auditTrail.setSubFeature("Export");
            auditTrail.setFeatureId(filterRequest.getFeatureId());
            auditTrail.setUserId(filterRequest.getUserId());
            auditTrail.setUserName(user.getUsername());
            auditTrail.setUserType(filterRequest.getUserType());
            auditTrail.setRoleType(filterRequest.getUserType());
            auditTrail.setTxnId("NA");
            auditTrail.setPublicIp(filterRequest.getPublicIp());
            auditTrail.setBrowser(filterRequest.getBrowser());
            auditTrailRepository.save(auditTrail);
            pageNumber = 0;
            url = systemConfigurationDbRepository.getByTag("file.download-link").getValue();
            pageSize = Integer.valueOf(systemConfigurationDbRepository.getByTag("file.max-file-record").getValue());

            //	tableDataPageable = databaseTablesRepository.getReportDataV2(filterRequest, pageNumber, pageSize );
            if (Objects.nonNull(tableDataPageable.getContent())) {
                tableData = (TableData) tableDataPageable.getContent();
                fileName = (LocalDateTime.now().format(dtf2) + "_" + tableData.getTableName() + ".csv").replace(" ", "_");
                writer = Files.newBufferedWriter(Paths.get(filePath + fileName));
//				csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
//						CSVWriter.DEFAULT_LINE_END);
                csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);
                columns = tableData.getColumns().toArray(new String[tableData.getColumns().size()]);
                csvWriter.writeNext(columns);
                for (Map<String, String> temp : tableData.getRowData()) {
//					logger.info("Data to write:["+temp.toString()+"] and columns:["+tableData.getColumns()+"]");
                    rowData = new String[columns.length];
                    i = 0;
                    for (String col : columns) {
                        rowData[i] = temp.get(col);
                        i++;
                    }
                    csvWriter.writeNext(rowData);
                    rowData = null;
                }
                csvWriter.flush();
            }
//			return new FileDetails( fileName, filePath, url+fileName );

            return new FileDetails(fileName, filePath, url.replace("$LOCAL_IP", propertiesReader.localIp) + fileName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
        } finally {
            try {
                if (writer != null)
                    writer.close();
                if (csvWriter != null)
                    csvWriter.close();
            } catch (Exception ex) {
            }
        }
    }

    public boolean reportDefaultFilter(TableFilterRequest filterRequest) {
        try {
            if (Objects.nonNull(filterRequest.getStartDate()) && !filterRequest.getStartDate().equals(""))
                return Boolean.FALSE;
            if (Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().equals(""))
                return Boolean.FALSE;
            if (Objects.nonNull(filterRequest.getTxnId()) && !filterRequest.getTxnId().equals(""))
                return Boolean.FALSE;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    private void saveAuditTrail(FilterRequest filterRequest) {
        // getReportDataV2
        //	User user = userRepository.getByid( filterRequest.getUserId() );
//			AuditTrail auditTrail = new AuditTrail();
//			auditTrail.setFeatureName(stakeholderFeatureRepository.findById(filterRequest.getFeatureId()).get().getName());
//			if( !this.reportDefaultFilter(filterRequest))
//				auditTrail.setSubFeature("Filter");
//			else
//				auditTrail.setSubFeature("View All");
//			auditTrail.setFeatureId( filterRequest.getFeatureId() );
//			auditTrail.setUserId( filterRequest.getUserId() );
//			auditTrail.setUserName( user.getUsername());
//			auditTrail.setUserType( filterRequest.getUserType() );
//			auditTrail.setRoleType( filterRequest.getUserType() );
//			auditTrail.setTxnId( "NA" );
//			auditTrail.setPublicIp(filterRequest.getPublicIp());
//			auditTrail.setBrowser(filterRequest.getBrowser());
//			auditTrailRepository.save(auditTrail);
    }


//    public HighChartsObj getReportData(TableFilterRequest filterRequest, int pageNumber, int pageSize) {
//        //   return   graphDbTablesRepository.  getReportDataV2( filterRequest,  pageNumber,  pageSize);
//        //	saveAuditTrail(filterRequest);
////			if( Objects.nonNull( filterRequest.getGroupBy()))
////				return databaseTablesRepository.getReportDataGroupBy(filterRequest, pageNumber, pageSize);
////			else
//        try {
//            String query = null;
//            var columnDetails = reportColumnDbRepository.findByReportnameIdOrderByColumnOrderAsc(filterRequest.getReportnameId());
//            if (Objects.nonNull(filterRequest.getTypeFlag()) && !filterRequest.getTypeFlag().equals(0)) {
//                columnDetails = columnDetails.stream()
//                        .filter(p -> p.getTypeFlag().equals(filterRequest.getTypeFlag()))
//                        .collect(Collectors.toList());
//            }
//            logger.info("Report details:[" + columnDetails.toString() + "]");
//            if (!(Objects.nonNull(columnDetails.get(0).getReport().getChartQuery()))   // optimise
//                    || columnDetails.get(0).getReport().getChartQuery().equalsIgnoreCase("")) {// Query not present , create query
//                query = createSelectQueryBuilder(filterRequest, pageNumber, pageSize, columnDetails);
//            } else {
//                query = createQueryByChartQuery(filterRequest, pageNumber, pageSize, columnDetails);
//            }  // dualAxis Query
//            if (query == null) {
//                return new HighChartsObj();
//            }
//            // dualAxis chartBuilder . Single and dual are almost same ,
//            // but note dual has 2 type of response .
//            // 1. smart count in line, metfone count in column , cellcard in bar ( bar column can not come at same time).
//            // 2. Dual axis :
//            // 3. Pie
//            var response = graphDbTablesRepository.graphBuilder(query, columnDetails);
//            logger.info("GRAPH:::::" + response);
//            return response;
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
//        }
//    }


    public String createSelectQueryBuilder(TableFilterRequest filterRequest, int pageNumber, int pageSize, List<ReportColumnDb> columnDetails) {
        List<SystemConfigListDb> typeFlags = null;
        List<String> columnsList = null;
        String orderByColumn = null;
        String searchQuery = null;
        String reportTrend = null;
        ReportDb reportDb = null;
        String query = "";
        String where = "where created_on IS NOT NULL and";
        String columns = "";
        String order = null;
        long totalEle = 0l;
        try {
            typeFlags = systemConfigListRepository.findByTag("Type_Flag", Sort.by("id"));
            if (Objects.nonNull(filterRequest.getTypeFlag()) && !filterRequest.getTypeFlag().equals(0))
                reportTrend = typeFlags.stream().
                        filter(typeFlagDetails -> typeFlagDetails.getValue().equals(filterRequest.getTypeFlag())).findFirst().get().getInterpretation();

//            if (Objects.nonNull(filterRequest.getReportnameId())) {
//                reportDb = reportDbRepository.getOne(filterRequest.getReportnameId());
//            } else {
//                return null;
//            }
            reportDb = columnDetails.get(0).getReport();

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
//            if (Objects.nonNull(reportTrend)) { // present in groupBy check in normal reportV2. ** its already done
//                columnDetails = reportColumnDbRepository.findByReportnameIdAndTypeFlagOrderByColumnOrderAsc(reportDb.getReportNameId(), filterRequest.getTypeFlag());
//            } else {
//                columnDetails = reportColumnDbRepository.findByReportnameIdOrderByColumnOrderAsc(reportDb.getReportNameId());
//            }
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
//                    if (propertiesReader.dialect.toLowerCase().contains("mysql")) {
//                        columns += column.getColumnName() + " as `" + column.getHeaderName() + "`,";
//                    } else {
//                        columns += column.getColumnName() + " as \"" + column.getHeaderName() + "\",";
//                    }
                    columns += column.getColumnName() + " ,";
                    columnsList.add(column.getHeaderName());
                }
                columns = columns.substring(0, columns.length() - 1);
            }
            logger.info("Report table columns list for query:[" + columns + "]");


             {
                if (Objects.nonNull(reportDb.getReportDataQuery())) {
                    query = "SELECT " + columns + " FROM (" + reportDb.getReportDataQuery() + ") t1";
                } else {
                    if (Objects.nonNull(reportTrend) && !(reportTrend.equalsIgnoreCase("daily")
                            || reportTrend.equalsIgnoreCase("till date")))
                        query = "SELECT " + columns + " FROM " + reportDb.getOutputTable() + "_" + reportTrend.toLowerCase();
                    else
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
                totalEle = graphDbTablesRepository.getTotalRows(query);
            else
                totalEle = graphDbTablesRepository.getTotalRowsOracle(query);
            var queryWithoutPagesize = query;

            if (propertiesReader.dialect.toLowerCase().contains("mysql")) {
                query = query + " order by " + orderByColumn + " " + order + " limit " + (pageNumber * pageSize) + "," + ((pageNumber + 1) * pageSize);
            } else {
                query = "select * from ( select /*+ FIRST_ROWS(n) */ orderd1.*, ROWNUM rnum from (" + query + " order by " + orderByColumn + " " + order + ") orderd1"
                        + ") orderd2 where rnum	> " + (pageNumber * pageSize) + " and rnum <=" + ((pageNumber + 1) * pageSize);
            }
            //  var response = graphDbTablesRepository.graphBuilder(queryWithoutPagesize, columnDetails);
            //   logger.info("GRAPH:::::" + response);
            return queryWithoutPagesize;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    public String createQueryByChartQuery(TableFilterRequest filterRequest, int pageNumber, int pageSize, List<ReportColumnDb> columnDetails) {
logger.info("START QUERY BUILDER   " );
        List<SystemConfigListDb> typeFlags = null;
        List<String> columnsList = null;
        String orderByColumn = null;
        String searchQuery = null;
        String reportTrend = null;
        ReportDb reportDb = null;
        String query = "";
        String where = "and created_on IS NOT NULL and";
        String columns = "";
        String order = null;
        long totalEle = 0l;
        try {
            String startDate = "'2020-01-01'";
            String endDate = "'2024-12-30'";
            Long rank = 1000L;
            String creationValue = " to_char(created_on,'YYYY-MM-DD') ";

            typeFlags = systemConfigListRepository.findByTag("Type_Flag", Sort.by("id"));

            if (Objects.nonNull(filterRequest.getStartDate()) && !filterRequest.getStartDate().equals("")) {
                startDate = "'" + filterRequest.getStartDate() + "'";
            }
            if (Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().equals("")) {
                endDate = "'" + filterRequest.getEndDate() + "'";
            }
            if (Objects.nonNull(filterRequest.getTop()) && !(filterRequest.getTop() == 0L)) {
                rank = filterRequest.getTop();
            }
            if (Objects.nonNull(reportTrend)) {
                if (reportTrend.equalsIgnoreCase("monthly")) {
                    creationValue = " to_char(created_on,'YYYY-MM') ";
                }
                if (reportTrend.equalsIgnoreCase("yearly")) {
                    creationValue = " to_char(created_on,'YYYY') ";
                }
            }

            {   //where Block
                reportDb =columnDetails.get(0).getReport();
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
                    where = " " + where.substring(0, where.lastIndexOf("and"));
                }
            }
            String chartQuery = columnDetails.get(0).getReport().getChartQuery();
            query = chartQuery
                    .replace("$startDate", startDate)
                    .replace("$endDate", endDate)
                    .replace("$creationValue", creationValue)
                    .replace("$where", where)
                    .replace("$rank", String.valueOf(rank));

            logger.info("QUERY:::::::::: " +query);

            return query;


//            if (Objects.nonNull(filterRequest.getTypeFlag()) && !filterRequest.getTypeFlag().equals(0))
//                reportTrend = typeFlags.stream().filter(typeFlagDetails -> typeFlagDetails.getValue().equals(filterRequest.getTypeFlag())).findFirst().get().getInterpretation();
//
//            reportDb = columnDetails.get(0).getReport();
//            if (Objects.nonNull(reportDb.getOrderColumnName()))
//                orderByColumn = reportDb.getOrderColumnName();
//            else {
//                if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly")) {
//                    orderByColumn = "to_date(created_on,'MON YYYY')";
//                } else {
//                    orderByColumn = "created_on";
//                }
//            }
//            if (Objects.nonNull(reportDb.getOrderBy()))
//                order = reportDb.getOrderBy();
//            else order = "desc";

//            if (Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().equals("")) {
//                if (propertiesReader.dialect.toLowerCase().contains("mysql"))
//                    where = where + " DATE(created_on) <= DATE('" + filterRequest.getEndDate() + "') and";
//                else {
//                    if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly"))
//                        where = where + " to_char(to_date(created_on,'MON YYYY'),'YYYY-MM-DD') <= '" + filterRequest.getEndDate() + "' and";
//                    else if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("yearly"))
//                        where = where + " to_char(to_date(created_on,'YYYY'),'YYYY-MM-DD') <= '" + filterRequest.getEndDate() + "' and";
//                    else
//                        where = where + " to_char(created_on,'YYYY-MM-DD') <= '" + filterRequest.getEndDate() + "' and";
//                }
//            }

//            if (propertiesReader.dialect.toLowerCase().contains("mysql")) {
//                query = query + " order by " + orderByColumn + " " + order + " limit " + (pageNumber * pageSize) + "," + ((pageNumber + 1) * pageSize);
//            } else {
//                query = "select * from ( select /*+ FIRST_ROWS(n) */ orderd1.*, ROWNUM rnum from (" + query + " order by " + orderByColumn + " " + order + ") orderd1"
//                        + ") orderd2 where rnum	> " + (pageNumber * pageSize) + " and rnum <=" + ((pageNumber + 1) * pageSize);
//            }


            //  var response = graphDbTablesRepository.graphBuilder(queryWithoutPagesize, columnDetails);
            //   logger.info("GRAPH:::::" + response);

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }
}


//String finalQueryWithGroupBy = " select  trunc(created_on, 'DD')   as mon,  model_name ,sum(COUNT) from device_type_active_report1  where \n" +
//        " CREATED_ON >=   TO_DATE('2021-01-01', 'YYYY-MM-DD')   AND  CREATED_ON <=   TO_DATE('2021-02-28', 'YYYY-MM-DD') \n" +
//        " group by   trunc(created_on, 'DD') , model_name  order by model_name ";
//
//String finalQueryWithGroupByForMoth = " select  trunc(created_on, 'MON')   as mon,  model_name ,sum(COUNT) from device_type_active_report1  where \n" +
//        " CREATED_ON >=   TO_DATE('2021-01-01', 'YYYY-MM-DD')   AND  CREATED_ON <=   TO_DATE('2021-02-28', 'YYYY-MM-DD') \n" +
//        " group by   trunc(created_on, 'MON') , model_name  order by model_name ";
//