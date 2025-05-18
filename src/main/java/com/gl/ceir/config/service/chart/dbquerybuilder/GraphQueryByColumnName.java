package com.gl.ceir.config.service.chart.dbquerybuilder;

import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.model.app.*;
import com.gl.ceir.config.repository.app.GraphDbTablesRepository;
import com.gl.ceir.config.repository.app.ReportColumnDbRepository;
import com.gl.ceir.config.repository.app.ReportDbRepository;
import com.gl.ceir.config.repository.app.SystemConfigListRepository;
import com.gl.ceir.config.service.chart.chartInterface.GraphQueryInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GraphQueryByColumnName implements GraphQueryInterface {

    private static final Logger logger = LogManager.getLogger(GraphQueryByColumnName.class);

    @Autowired
    ReportDbRepository reportDbRepository;
    @Autowired
    SystemConfigListRepository systemConfigListRepository;
    @Autowired
    GraphDbTablesRepository graphDbTablesRepository;
    @Autowired
    ReportColumnDbRepository reportColumnDbRepository;

    @Autowired
    PropertiesReader propertiesReader;

    @Override
    public String graphQueryBuilder(TableFilterRequest filterRequest, List<ReportColumnDb> columnDetails) {
        List<SystemConfigListDb> typeFlags = null;
        List<String> columnsList = new ArrayList<String>();
        String reportTrend = null;
        ReportDb reportDb = null;
        String query = "";
        String where = "where";
        String columns = "";
        String order = null;
        long totalEle = 0l;
        Long rank = 5L;
        String creationValue = "";
        try {
            typeFlags = systemConfigListRepository.findByTag("Type_Flag", Sort.by("id"));
            if (Objects.nonNull(filterRequest.getTypeFlag()) && !filterRequest.getTypeFlag().equals(0))
                reportTrend = typeFlags.stream().filter(typeFlagDetails -> typeFlagDetails.getValue().equals(filterRequest.getTypeFlag())).findFirst().get().getInterpretation();
            reportDb = columnDetails.get(0).getReport();

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

            query = getSelectString(filterRequest, reportDb, reportTrend, columns);

            where = whereStringWithDateRange(filterRequest, where, reportTrend);

            where = whereStringForOtherModifiers(filterRequest, where, reportTrend, reportDb);

            query = query + " " + where;
            order = getOrderString(filterRequest, reportDb, reportTrend);
            query = query + " " + order;

            query = getTopRankString(filterRequest, query, reportTrend, reportDb);
            logger.info("FINAL QUERY [[" + query + "]]");
            return query;

        } catch (Exception e) {
            logger.error("[{(ERROR)}]" + e + "in [" + Arrays.stream(e.getStackTrace()).filter(ste -> ste.getClassName().equals(GraphQueryByColumnName.class.getName())).collect(Collectors.toList()).get(0) + "]");
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    private static String getTopRankString(TableFilterRequest filterRequest, String query, String reportTrend, ReportDb reportDb) {
        if ((!Objects.nonNull(reportDb.getReportDataQuery())) && Objects.nonNull(filterRequest.getTop())) {
            query += ") where rank <=" + filterRequest.getTop();
        }
        return query;
    }

    private static String getOrderString(TableFilterRequest filterRequest, ReportDb reportDb, String reportTrend) {
        String order;

        if (Objects.nonNull(reportDb.getReportDataQuery()) && Objects.nonNull(reportDb.getKeyColumn())) {
            order = " GROUP BY " + reportDb.getKeyColumn() + " order by " + reportDb.getKeyColumn();
        } else if (Objects.nonNull(reportDb.getOrderColumnName()))
            order = "order by " + reportDb.getOrderColumnName();
        else if (Objects.nonNull(reportDb.getOrderBy()))
            order = "order by " + reportDb.getOrderBy();
        else if (reportTrend.equalsIgnoreCase("yearly"))
            order = "order by  to_date(created_on,'YYYY')";
        else if (reportTrend.equalsIgnoreCase("quarterly"))
            order = "order by  to_date(created_on,'YYYY-MM')";
        else if (reportTrend.equalsIgnoreCase("monthly"))
            order = "order by  to_date(created_on,'MON YYYY')";
        else
            order = "order by created_on ";
        return order;
    }

    private static String getSelectString(TableFilterRequest filterRequest, ReportDb reportDb, String reportTrend, String columns) {
        String query;
        if (Objects.nonNull(reportDb.getReportDataQuery())) {
            if ((reportTrend.equalsIgnoreCase("daily") || reportTrend.equalsIgnoreCase("till date"))) {
                query = reportDb.getReportDataQuery().replace("$tableName", reportDb.getOutputTable());
            } else {
                query = reportDb.getReportDataQuery().replace("$tableName", reportDb.getOutputTable() + "_" + reportTrend.toLowerCase());
            }
            //    query = "SELECT " + columns + " FROM (" + reportDb.getReportDataQuery() + ") t1";
        } else {
            if (Objects.nonNull(reportTrend) && !(reportTrend.equalsIgnoreCase("daily")
                    || reportTrend.equalsIgnoreCase("till date"))) {
                query = "SELECT " + columns + " FROM " + reportDb.getOutputTable() + "_" + reportTrend.toLowerCase();
                if (Objects.nonNull(filterRequest.getTop())) {
                    query = "SELECT " + columns + " FROM (" +
                            "SELECT " + columns + ",RANK() OVER ( PARTITION BY created_on ORDER BY count DESC) AS rank  FROM "
                            + reportDb.getOutputTable() + "_" + reportTrend.toLowerCase();
                }
            } else {
                query = "SELECT " + columns + " FROM " + reportDb.getOutputTable();
                if (Objects.nonNull(filterRequest.getTop())) {
                    query = "SELECT " + columns + " FROM (" +
                            "SELECT " + columns + ",RANK() OVER ( PARTITION BY created_on ORDER BY count DESC) AS rank  FROM "
                            + reportDb.getOutputTable();
                }
            }
        }
        if (reportTrend.equalsIgnoreCase("till date")) {
            query = query.replace("PARTITION BY created_on", "");
        }
        return query;
    }

    public static String whereStringWithDateRange(TableFilterRequest filterRequest, String where, String reportTrend) {
        if (Objects.nonNull(filterRequest.getDateRange()) && (filterRequest.getDateRange().size() != 0)) {
            where = where + " created_on IS NOT NULL and ( ";
            for (DateRange dtrange : filterRequest.getDateRange()) {
                if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly"))
                    where = where + " to_char(to_date(created_on,'MON YYYY'),'YYYY-MM-DD') >= '" + dtrange.getStartDate() + "' and"
                            + " to_char(to_date(created_on,'MON YYYY'),'YYYY-MM-DD') <= '" + dtrange.getEndDate() + "' OR";
                else if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("quarterly"))   // optimise
                    where = where + "created_on >= to_char(to_date('" + dtrange.getStartDate() + "','YYYY-MM-DD' ),'YYYY-Q') and "
                            + "created_on <= to_char(to_date('" + dtrange.getEndDate() + "','YYYY-MM-DD' ),'YYYY-Q')   OR";
                    //  where = where + " to_char(to_date(created_on,'YYYY-Q'),'YYYY-MM-DD') >= '" + dtrange.getStartDate() + "' and" + " to_char(to_date(created_on,'YYYY-Q'),'YYYY-MM-DD') <= '" + dtrange.getEndDate() + "' OR";
                else if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("yearly"))
                    where = where + " to_char(to_date(created_on,'YYYY'),'YYYY-MM-DD') >= '" + dtrange.getStartDate() + "' and"
                            + " to_char(to_date(created_on,'YYYY'),'YYYY-MM-DD') <= '" + dtrange.getEndDate() + "' OR";
                else
                    // When created on is varchar ::
                    //   where = where + " to_char(to_date(created_on,'YYYY-MM-DD'),'YYYY-MM-DD') >= '" + dtrange.getStartDate() + "' and"
                    //          + " to_char(to_date(created_on,'YYYY-MM-DD'),'YYYY-MM-DD') <= '" + dtrange.getEndDate() + "' OR";

                    // When created on is timestamp ::
                    where = where + " to_char(created_on,'YYYY-MM-DD') >= '" + dtrange.getStartDate() + "' and"
                            + " to_char(created_on,'YYYY-MM-DD') <= '" + dtrange.getEndDate() + "' OR";

                //  where = where + "created_on  BETWEEN to_date('" + dtrange.getStartDate() + "','YYYY-MM-DD') AND  to_date( '" + dtrange.getEndDate() + "','YYYY-MM-DD') OR ";
//                    if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly"))
//                        where = where + " to_date(created_on,'MON YYYY')  between  to_Date('" + dtrange.getStartDate() + "','YYYY-MM-DD')  and  to_Date('" + dtrange.getStartDate() + "','YYYY-MM-DD')   OR";
//                    else if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("yearly"))
//                        where = where + " to_char(to_date(created_on,'YYYY'),'YYYY-MM-DD') >= '" + filterRequest.getStartDate() + "' and";
//                    else
//                        where = where + " to_char(created_on,'YYYY-MM-DD') >= '" + filterRequest.getStartDate() + "' and";
            }
            where = where.substring(0, where.lastIndexOf("OR"));
            where = where + " ) and";
        }
        return where;
    }

    private String whereStringForOtherModifiers(TableFilterRequest filterRequest, String where, String reportTrend, ReportDb reportDb) {

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

        if (where.equalsIgnoreCase("where"))
            return " ";
        else
            return where.substring(0, where.lastIndexOf("and"));

    }

}


//
//where = where + "  created_on BETWEEN  to_char(to_date(created_on,'MON YYYY'),'YYYY-MM-DD') >= '" + dtrange.getStartDate() + "' and"
//       + " to_char(to_date(created_on,'MON YYYY'),'YYYY-MM-DD') >= '" + dtrange.getEndDate() + "' and";


//            if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("yearly")) {
//                orderByColumn = orderByColumn + "to_date(created_on,'YYYY')";
//            } else if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly")) {
//                orderByColumn = orderByColumn + "to_date(created_on,'MON YYYY')";
//            } else {
//                orderByColumn = orderByColumn + "created_on";
//            }


//            if (propertiesReader.dialect.toLowerCase().contains("mysql"))
//                totalEle = graphDbTablesRepository.getTotalRows(query);
//            else totalEle = graphDbTablesRepository.getTotalRowsOracle(query);
//     var queryWithoutPagesize = query ;
//            if (propertiesReader.dialect.toLowerCase().contains("mysql")) {
//                query = query + " order by " + orderByColumn + " " + order + " limit " + (pageNumber * pageSize) + "," + ((pageNumber + 1) * pageSize);
//            } else {             query = "select * from ( select /*+ FIRST_ROWS(n) */ orderd1.*, ROWNUM rnum from (" + query + " order by " + orderByColumn + " " + order + ") orderd1"
//                        + ") orderd2 where rnum	> " + (pageNumber * pageSize) + " and rnum <=" + ((pageNumber + 1) * pageSize);         }
//  var response = graphDbTablesRepository.graphBuilder(queryWithoutPagesize, columnDetails);
//       return queryWithoutPagesize;


//            if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("yearly")) {
//                orderByColumn = orderByColumn + "to_date(created_on,'YYYY')";
//            } else if (Objects.nonNull(reportTrend) && reportTrend.equalsIgnoreCase("monthly")) {
//                orderByColumn = orderByColumn + "to_date(created_on,'MON YYYY')";
//            } else {
//                orderByColumn = orderByColumn + "created_on";
//            }