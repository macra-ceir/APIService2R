package com.gl.ceir.config.service.chart.chartbuilder;

import com.gl.ceir.config.model.app.*;
import com.gl.ceir.config.repository.app.GraphDbTablesRepository;
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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@Service

public class PieChart implements GraphBuilderInterface {

    private static final Logger logger = LogManager.getLogger(PieChart.class);

    @Autowired
    GraphDbTablesRepository graphDbTablesRepository;

    @Override
    public HighChartsObj createGraph(String query, List<ReportColumnDb> columnDetails, TableFilterRequest filterRequest) {  // NOT TO TOUCH
        logger.info("Final data query: [" + query + "]");
        var title = columnDetails.get(0).getReport().getReportName();
        var subtitle = columnDetails.get(0).getReport().getOutputTable();
        List<String> category = new LinkedList<>();

        List<SeriesData> sData = new LinkedList<>();
        var legend = columnDetails.stream()
                .filter(p -> p.getChartParam().equalsIgnoreCase("yaxis"))  //
                .map(ReportColumnDb::getColumnName)
                .map(String::toLowerCase)
                .findFirst().get();
        String chartType = columnDetails.stream()
                .filter(p -> p.getChartParam().trim().equalsIgnoreCase("yaxis")) //category
                .map(ReportColumnDb::getChartType)
                .map(String::toLowerCase)
                .findFirst().get();
        var legendValue = columnDetails.stream()
                .filter(p -> p.getChartParam().equalsIgnoreCase("LegendValue"))
                .map(ReportColumnDb::getColumnName)
                .map(p -> p.toLowerCase())
                .findFirst().get();
        try (Connection conn = graphDbTablesRepository.getConnection(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery(query);) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();// Create a map to store column names and their corresponding lists of values
//            for (int i = 1; i <= columnCount; i++) {
//                System.out.println(metaData.getColumnName(i));
//                if (! (metaData.getColumnName(i).equalsIgnoreCase(legend) || metaData.getColumnName(i).equalsIgnoreCase("created_on")) ){
//                    legendValue = metaData.getColumnName(i);
//                }
//            }
            logger.info("chartType" + chartType + ";;legend ->" + legend + ";;legendValue->" + legendValue);
            int i = 0;
            while (resultSet.next()) {
                var leg = resultSet.getString(legend);
                var legValue = resultSet.getString(legendValue);
                logger.info(i++ + "!!!!" + leg + ":::" + legValue);
                sData.add(new SeriesData(leg, chartType, List.of(legValue)));
            }
            HighChartsObj highChartsObj = new HighChartsObj();
            highChartsObj.setTitle(title);
            highChartsObj.setSubtitle(subtitle);
            highChartsObj.setChartType(chartType);
            highChartsObj.setyAxis("Imei Count");
            highChartsObj.setSeriesData(sData);
            highChartsObj.setCatogery(category);
            logger.info(sData + ":::::::::::::::" + category);
            return highChartsObj;
        } catch (Exception e) {
            logger.error(e + "in [" + Arrays.stream(e.getStackTrace()).filter(ste -> ste.getClassName().equals(PieChart.class.getName())).collect(Collectors.toList()).get(0) + "]");
            logger.info("Exception [" + e + "]");
            return null;
        }
    }

    // *************************** //
    public JSONObject getDataByQuery(String query) {
        logger.info("Final data query: [" + query + "]");
        try (Connection conn = graphDbTablesRepository.getConnection(); Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {
            while (resultSet.next()) {
              var value =  resultSet.getString("tagCounts");
              logger.info("---->"+value);
                return  (JSONObject) new JSONParser().parse( value);
            }
        } catch (Exception e) {
            logger.error(e + "in [" + Arrays.stream(e.getStackTrace()).filter(ste -> ste.getClassName().equals(PieChart.class.getName())).collect(Collectors.toList()).get(0) + "]");
            logger.info("Exception [" + e + "]");
        }
        return null;
    }
}

//                for (int i = 1; i <= columnCount; i++) {
//                    String columnName = metaData.getColumnName(i).toLowerCase();
//                    if (columnlist.contains(columnName)) {
//                        String columnValue = resultSet.getString(columnName);
//                        if (dataMap.containsKey(columnName)) {
//                            List<String> newList = new ArrayList<String>(dataMap.get(columnName));
//                            newList.add(columnValue.toString());
//                            dataMap.put(columnName, newList);
//                        } else {
//                            dataMap.put(columnName, List.of(columnValue));
//                        }
//                    }
//                }
//                category.add(resultSet.getString(categoryColumn));
//   }
//            for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
//                String columnName = entry.getKey();
//                List<String> columnValues = entry.getValue();
//                sData.add(new SeriesData(columnName, columnValues));
//