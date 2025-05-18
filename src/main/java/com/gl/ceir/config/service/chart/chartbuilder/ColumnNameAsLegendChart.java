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


@Service

public class ColumnNameAsLegendChart implements GraphBuilderInterface {

    private static final Logger logger = LogManager.getLogger(ColumnNameAsLegendChart.class);

    @Autowired
    GraphDbTablesRepository graphDbTablesRepository;

    @Override
    public HighChartsObj createGraph(String query, List<ReportColumnDb> columnDetails, TableFilterRequest filterRequest) {  // NOT TO TOUCH
        logger.info("Final data query: [" + query + "]");
        var title = columnDetails.get(0).getReport().getReportName();
        var subtitle = columnDetails.get(0).getReport().getOutputTable();
        List<String> category = new LinkedList<>();
        List<SeriesData> sData = new LinkedList<>();
        var categoryColumn = columnDetails.stream()
                .filter(p -> p.getChartParam().equalsIgnoreCase("xaxis"))
                .map(ReportColumnDb::getColumnName)
                .map(String::toLowerCase)
                .findFirst().get();
        logger.info("categoryColumn" + categoryColumn);
        var columnlist = columnDetails.stream()
                .filter(p -> p.getChartParam().equalsIgnoreCase("yaxis"))
                .map(ReportColumnDb::getColumnName)
                .map(p -> p.toLowerCase())
                .collect(Collectors.toList());
        logger.info("columnlist::" + columnlist + ":::SIZE:" + columnlist.size());
        logger.info("Final data query: [" + query + "]");
        try (Connection conn = graphDbTablesRepository.getConnection(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery(query);) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();// Create a map to store column names and their corresponding lists of values
            Map<String, List<String>> dataMap = new HashMap<>();//        while (res.next()) {/     category.add(res.getString(String.valueOf(categoryColumn)));

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i).toLowerCase();
                    if (columnlist.contains(columnName)) {
                        String columnValue = resultSet.getString(columnName);
                        if (dataMap.containsKey(columnName)) {
                            List<String> newList = new ArrayList<String>(dataMap.get(columnName));
                            newList.add(columnValue.toString());
                            dataMap.put(columnName, newList);
                        } else {
                            dataMap.put(columnName, List.of(columnValue));
                        }
                    }
                }
                category.add(resultSet.getString(categoryColumn));
            }
            for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
                String columnName = entry.getKey();
                List<String> columnValues = entry.getValue();
                sData.add(new SeriesData(columnName, columnValues));
            }
            HighChartsObj highChartsObj = new HighChartsObj();
            highChartsObj.setTitle(title);
            highChartsObj.setSubtitle(subtitle);
            highChartsObj.setSeriesData(sData);
            highChartsObj.setCatogery(category);
            highChartsObj.setyAxis("Imei Count");  //   // count and percentage
            logger.info(sData + "::::::::::::::::::::::::::::::" + category);
            return highChartsObj;
        } catch (Exception e) {
            logger.info("Exception [" + e + "]");
            logger.error("Exception [" + e + "]]in [" + Arrays.stream(e.getStackTrace()).filter(ste -> ste.getClassName().equals(ColumnNameAsLegendChart.class.getName())).collect(Collectors.toList()).get(0) + "]");
            return null;
        }
    }
}
