package com.gl.ceir.config.service.chart.chartbuilder;

import com.gl.ceir.config.model.app.HighChartsObj;
import com.gl.ceir.config.model.app.ReportColumnDb;
import com.gl.ceir.config.model.app.SeriesData;
import com.gl.ceir.config.model.app.TableFilterRequest;
import com.gl.ceir.config.repository.app.GraphDbTablesRepository;
import com.gl.ceir.config.request.model.ResponseDetailsForTop;
import com.gl.ceir.config.service.chart.chartInterface.GraphBuilderInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class DualAxisChart implements GraphBuilderInterface {

    private static final Logger logger = LogManager.getLogger(DualAxisChart.class);

    @Autowired
    GraphDbTablesRepository graphDbTablesRepository;

    @Override
    public HighChartsObj createGraph(String query, List<ReportColumnDb> columnDetails, TableFilterRequest filterRequest) {  // NOT TO TOUCH
        logger.info("Final data query:::: [" + query + "]");
        var title = columnDetails.get(0).getReport().getReportName();
        var subtitle = columnDetails.get(0).getReport().getOutputTable();
        List<SeriesData> sData = new LinkedList<>();
        String dualChartType = "", dualHeaderName = "", dualColumnName = "";
        String legendValueColumnName = "", legendValueChartType = "";
        String categoryColumn = "", type = "", headerName = "", xaxisChartType = "", yaxisColumnName = "";
        for (ReportColumnDb coulums : columnDetails) {
            if (coulums.getChartParam().trim().equalsIgnoreCase("xaxis")) {
                categoryColumn = coulums.getColumnName();
                coulums.getChartType();
            }
            if (coulums.getChartParam().trim().equalsIgnoreCase("yaxis")) {
                yaxisColumnName = coulums.getColumnName();
                type = coulums.getChartType();
            }
            if (coulums.getChartParam().trim().equalsIgnoreCase("LegendValue")) {
                legendValueColumnName = coulums.getColumnName();
                legendValueChartType = coulums.getChartType();
                headerName = coulums.getHeaderName();
            }
            if (coulums.getChartParam().trim().equalsIgnoreCase("LegendValue2")) {
                dualColumnName = coulums.getColumnName();
                dualHeaderName = coulums.getHeaderName();
                dualChartType = coulums.getChartType();
            }
        }
        logger.debug("xaxis::categoryColumn=" + categoryColumn + "; xaxisChartType=" + xaxisChartType);
        logger.debug("yaxis::yaxisColumnName=" + yaxisColumnName + "; (yaxisChartType)type=" + type);
        logger.debug("LegendValue::legendValueColumnName=" + legendValueColumnName + "; legendValueChartType=" + legendValueChartType + "; headerName=" + headerName);
        logger.debug("LegendValue2::dualColumnName=" + dualColumnName + "; ()dualHeaderName=" + dualHeaderName + "; dualChartType=" + dualChartType);

        try (Connection conn = graphDbTablesRepository.getConnections(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery(query);) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();// Create a map to store column names and their corresponding lists of values
            Map<String, String> seriesData = new HashMap<>();
            Map<String, List<String>> dataMap = new HashMap<>();
            List<ResponseDetailsForTop> responseDetailsForTop = new ArrayList<>();
            LinkedHashSet<String> dateSet = new LinkedHashSet();
            LinkedHashSet<String> typeXset = new LinkedHashSet();
            List<String> dualChartList = new ArrayList<>();
            while (resultSet.next()) {
                logger.info("::::" + resultSet.getString(1) + "::" + resultSet.getString(2) + "::" + resultSet.getString(3) + "::");
                dateSet.add(resultSet.getString(1));
                typeXset.add(resultSet.getString(2));
                seriesData.put("legend", resultSet.getString(3));  // legend   // resultSet.getString(3); - legendValues
                dualChartList.add(resultSet.getString(dualColumnName));
                responseDetailsForTop.add(new ResponseDetailsForTop(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)));
            }
            for (String typeX : typeXset) {
                for (String date : dateSet) {  //createdOn xAxis
                    var count = findCountValue(responseDetailsForTop, date, typeX);
                    if (dataMap.containsKey(typeX)) {
                        List<String> newList = new ArrayList<String>(dataMap.get(typeX));
                        newList.add(count);
                        dataMap.put(typeX, newList);
                    } else {
                        dataMap.put(typeX, List.of(count));
                    }
                }
            }

            for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
                String columnName = entry.getKey();
                List<String> columnValues = entry.getValue();
                sData.add(new SeriesData(columnName, type, columnValues));
            }
            sData.add(new SeriesData(dualColumnName, dualChartType, dualChartList));
            HighChartsObj highChartsObj = new HighChartsObj();
            var headerString = "";
            if (Objects.nonNull(filterRequest.getSearchString())) {
                for (String values : filterRequest.getSearchString().toUpperCase().split("AND ")) {
                    if (values.contains("MONTHS_BETWEEN")) {
                        headerString = headerString + " based on Second Hand Imei ";
                    } else {
                        headerString = headerString + " for " + values.toUpperCase().replace("'", "").replace("_", " ");
                    }
                }
                headerString.replace("md", "").replace("mobile_device_repository", "")
                        .replace("MOBILE_DEVICE_REPOSITORY", "");
            }

            highChartsObj.setTitle(title.replace("$title", headerString.isEmpty() ? "" : headerString));
            highChartsObj.setSubtitle(subtitle);
            highChartsObj.setyAxis(headerName + "," + dualHeaderName); //
            highChartsObj.setSeriesData(sData);
            highChartsObj.setCatogery(new LinkedList<>(dateSet));
            logger.debug(sData + ":::::::::::::::" + new LinkedList<>(dateSet));
            return highChartsObj;
        } catch (Exception e) {
            logger.error("[{(ERROR)}]" + e + "in [" + Arrays.stream(e.getStackTrace()).filter(ste -> ste.getClassName().equals(DualAxisChart.class.getName())).collect(Collectors.toList()).get(0) + "]");
            return null;
        }
    }

    private static String findCountValue(List<ResponseDetailsForTop> list, String date, String type) {
        return list.stream().
                filter(obj -> obj.getDateValue().equals(date) && obj.getTypeValue().equals(type))
                .map(obj -> obj.getCountValue())
                .findFirst().orElse("0");
    }

}
//    @Override
//    public HighChartsObj createGraph(String query, List<ReportColumnDb> columnDetails) {
//        logger.info("Final data query: [" + query + "]");
//        return null;
//    }

