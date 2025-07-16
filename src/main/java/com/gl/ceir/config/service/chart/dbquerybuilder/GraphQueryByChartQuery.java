package com.gl.ceir.config.service.chart.dbquerybuilder;

import com.gl.ceir.config.model.app.*;
import com.gl.ceir.config.repository.app.*;
import com.gl.ceir.config.service.chart.chartInterface.GraphQueryInterface;
import com.gl.ceir.config.service.chart.chartbuilder.TopXChart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GraphQueryByChartQuery implements GraphQueryInterface {

    private static final Logger logger = LogManager.getLogger(GraphQueryByChartQuery.class);

    @Autowired
    ReportDbRepository reportDbRepository;
    @Autowired
    SystemConfigListRepository systemConfigListRepository;


    @Autowired
    TopXChart topXChart;


    @Override
    public String graphQueryBuilder(TableFilterRequest filterRequest, List<ReportColumnDb> columnDetails) {
        logger.info("START QUERY BUILDER   ");
        List<SystemConfigListDb> typeFlags = null;
        List<String> columnsList = null;
        String orderByColumn = null;
        String reportTrend = null;
        ReportDb reportDb = null;
        String query = "";
        String where = "where";
        String columns = "";
        String order = null;
        long totalEle = 0l;
        String creationValue = "";
        String chartQuery = columnDetails.get(0).getReport().getChartQuery();
        Long rank = 5L;
        try {
            typeFlags = systemConfigListRepository.findByTag("Type_Flag", Sort.by("id"));
            if (filterRequest.getTypeFlag() == null) {
                filterRequest.setTypeFlag(0);
            }


            reportTrend = typeFlags.stream().filter(typeFlagDetails -> typeFlagDetails.getValue().equals(filterRequest.getTypeFlag())).findFirst().get().getInterpretation();
            logger.debug("TYpe FLag Trend" + reportTrend);

            {   //where Block
                where = whereStringWithDateRange(filterRequest, where, reportTrend);

                reportDb = columnDetails.get(0).getReport();
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
                } else {
                    where = " ";
                }

            }   // Where clause ends

            String tableName = reportDb.getOutputTable();
            if (Objects.nonNull(reportTrend)) {
                if (reportTrend.equalsIgnoreCase("monthly")) {  //2
                    creationValue = " TO_CHAR(created_on, 'YYYY-MM'),TO_CHAR(created_on, 'MON-YYYY') ";
                    tableName = reportDb.getOutputTable() + "_" + reportTrend.toLowerCase();
                } else if (reportTrend.equalsIgnoreCase("yearly")) { // 3
                    creationValue = " to_char(created_on,'YYYY') ";
                    tableName = reportDb.getOutputTable() + "_" + reportTrend.toLowerCase();
                } else if (reportTrend.equalsIgnoreCase("quarterly")) {  //5
                    creationValue = " to_char(created_on,'YYYY-Q') ";
                    tableName = reportDb.getOutputTable() + "_" + reportTrend.toLowerCase();
                } else if (reportTrend.equalsIgnoreCase("daily")) {   //1
                    creationValue = " to_char(created_on,'YYYY-MM-DD') ";
                } else if (reportTrend.equalsIgnoreCase("Till Date")) {  //0
                    creationValue = "";  //----- remove  as created_on , created_on,   PARTITION BY ,creationValue
                    chartQuery = chartQuery.replace("as created_on,", "")
                            .replace("created_on,", "")
                            .replace("PARTITION BY", "")
                            .replace("$creationValue,", "");
                }
            }

            if (Objects.nonNull(filterRequest.getTop()) && !(filterRequest.getTop() == 0L)) {
                rank = filterRequest.getTop();
            }
            query = chartQuery  //$tableName
                    .replace("$startDate", "'2020-01-01'")
                    .replace("$endDate", " CURRENT_TIMESTAMP ")
                    .replace("$creationValue", creationValue)
                    .replace("$tableName", tableName)
                    .replace("$where", where)
                    .replace("$rank", String.valueOf(rank));
            logger.info("FINAL QUERY [[" + query + "]]");
            return query;
        } catch (Exception e) {
            logger.error("[{(ERROR)}]" + e + "in [" + Arrays.stream(e.getStackTrace()).filter(ste -> ste.getClassName().equals(GraphQueryByChartQuery.class.getName())).collect(Collectors.toList()).get(0) + "]");
            return null;
        }
    }


    // Note this will work with direct table having created on in timestamp , not work for created on in JAN 2025 or YYYY Q etc. For that need to check whereStringWithDateRange on different class
    public static String whereStringWithDateRange(TableFilterRequest filterRequest, String where, String reportTrend) {
        if (Objects.nonNull(filterRequest.getDateRange()) && (filterRequest.getDateRange().size() != 0)) {
            where = where + " created_on IS NOT NULL and ( ";
            for (DateRange dtrange : filterRequest.getDateRange()) {
                if (Objects.nonNull(reportTrend))
                    where = where + " to_char(created_on,'YYYY-MM-DD') >= '" + dtrange.getStartDate() + "' and"
                            + " to_char(created_on,'YYYY-MM-DD') <= '" + dtrange.getEndDate() + "' OR";
            }
            where = where.substring(0, where.lastIndexOf("OR"));
            where = where + " ) and";
        }
        return where;
    }

}

