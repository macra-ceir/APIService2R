package com.gl.ceir.config.service.chart.dbquerybuilder;

import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.model.app.ReportColumnDb;
import com.gl.ceir.config.model.app.ReportDb;
import com.gl.ceir.config.model.app.SystemConfigListDb;
import com.gl.ceir.config.model.app.TableFilterRequest;
import com.gl.ceir.config.repository.app.*;
import com.gl.ceir.config.repository.aud.AuditTrailRepository;
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

import static com.gl.ceir.config.service.chart.dbquerybuilder.GraphQueryByColumnName.whereStringWithDateRange;

@Service
public class GraphQueryByReportDataQuery implements GraphQueryInterface {
    private static final Logger logger = LogManager.getLogger(GraphQueryByReportDataQuery.class);

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

    @Override
    public String graphQueryBuilder(TableFilterRequest filterRequest, List<ReportColumnDb> columnDetails) {
        logger.info("START QUERY BUILDER   ");
        List<SystemConfigListDb> typeFlags = null;
        List<String> columnsList = new ArrayList<String>();
        String reportTrend = null;
        String query = "";
        String columns = "";
        String order = null;
        long totalEle = 0l;
        Long rank = 5L;
        String creationValue = "";
        try {
            ReportDb reportDb = columnDetails.get(0).getReport();
            String reportQuery = reportDb.getReportDataQuery();

            logger.info("Report Query" + reportQuery);
            typeFlags = systemConfigListRepository.findByTag("Type_Flag", Sort.by("id"));
            if (Objects.nonNull(filterRequest.getTypeFlag()) && !filterRequest.getTypeFlag().equals(0))
                reportTrend = typeFlags.stream().filter(typeFlagDetails -> typeFlagDetails.getValue().equals(filterRequest.getTypeFlag())).findFirst().get().getInterpretation();
            if ((reportTrend.equalsIgnoreCase("daily") || reportTrend.equalsIgnoreCase("till date"))) {
                query = reportDb.getReportDataQuery().replace("$tableName", reportDb.getOutputTable());
            } else {
                query = reportDb.getReportDataQuery().replace("$tableName", reportDb.getOutputTable() + "_" + reportTrend.toLowerCase());
            }
            if (Objects.nonNull(filterRequest.getTop()) && !(filterRequest.getTop() == 0L)) {
                rank = filterRequest.getTop();
            }
            String where = " and";
             where = whereStringWithDateRange(filterRequest, where, reportTrend);
            if (Objects.nonNull(filterRequest.getSearchString())) {
                where = where + " " + filterRequest.getSearchString() + " and";
             }

            query = query.replace("$rank", String.valueOf(rank))
                    .replace("$order", getOrderString(filterRequest, reportDb, reportTrend))
                    .replace("$where", where.substring(0, where.lastIndexOf("and")));

            logger.info("FINAL QUERY [[" + query + "]]");
            return query;
        } catch (Exception e) {
            logger.error("[{(ERROR)}]" + e + "in [" + Arrays.stream(e.getStackTrace()).filter(ste -> ste.getClassName().equals(GraphQueryByReportDataQuery.class.getName())).collect(Collectors.toList()).get(0) + "]");
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    private static String getOrderString(TableFilterRequest filterRequest, ReportDb reportDb, String reportTrend) {
        String order;

        if (reportTrend.equalsIgnoreCase("yearly"))
            order = "order by to_date(created_on,'YYYY') ";
        else if (reportTrend.equalsIgnoreCase("quarterly"))
            order = "order by  to_date(created_on,'YYYY-MM') ";
        else if (reportTrend.equalsIgnoreCase("monthly"))
            order = "order by  to_date(created_on,'MON YYYY') ";
        else
            order = "order by created_on ";
        return order;
    }

}
//   SELECT reason_for_invalid_imei ,count FROM (SELECT reason_for_invalid_imei , sum(count) as count,RANK() OVER (ORDER BY sum(count)  DESC) AS rank FROM  rep.invalid_reason_imei_count   GROUP BY reason_for_invalid_imei )  where rank <=5
