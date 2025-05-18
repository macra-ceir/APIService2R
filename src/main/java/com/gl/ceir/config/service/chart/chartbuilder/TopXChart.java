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
public class TopXChart implements GraphBuilderInterface {

    private static final Logger logger = LogManager.getLogger(TopXChart.class);

    @Autowired
    GraphDbTablesRepository graphDbTablesRepository;


    @Override
    public HighChartsObj createGraph(String query, List<ReportColumnDb> columnDetails, TableFilterRequest filterRequest) {  // NOT TO TOUCH
        logger.info("Final data query:::: [" + query + "]");
        var title = columnDetails.get(0).getReport().getReportName();
        var subtitle = columnDetails.get(0).getReport().getOutputTable();
        List<SeriesData> sData = new LinkedList<>();
        var categoryColum = columnDetails.stream()
                .filter(p -> p.getChartParam().trim().equalsIgnoreCase("xaxis")) //category
                .map(ReportColumnDb::getColumnName)
                .map(String::toLowerCase)
                .findFirst().get();
        var columnlist = columnDetails.stream()
                .filter(p -> p.getChartParam() != null)
                .filter(p -> p.getChartParam().trim().equalsIgnoreCase("yaxis"))  //series
                .map(ReportColumnDb::getColumnName)
                .map(p -> p.toLowerCase())
                .collect(Collectors.toList());
        var legendColumn = columnDetails.stream()
                .filter(p -> p.getChartParam().trim().equalsIgnoreCase("LegendValue")) //category
                .map(ReportColumnDb::getColumnName)
                .map(String::toLowerCase)
                .findFirst().get();

        String type = columnDetails.stream()
                .filter(p -> p.getChartParam() != null)
                .filter(p -> p.getChartParam().trim().equalsIgnoreCase("yaxis")) //category
                .map(ReportColumnDb::getChartType)
                .map(String::toLowerCase)
                .findFirst().get();

        logger.info("CategoryColumn:"+ categoryColum+",Columnlist:" + columnlist + ":type:" + type);
        try (Connection conn = graphDbTablesRepository.getConnection(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery(query);) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();// Create a map to store column names and their corresponding lists of values
            Map<String, List<String>> dataMap = new HashMap<>();
            List<ResponseDetailsForTop> responseDetailsForTop = new ArrayList<>();
            LinkedHashSet<String> dateSet = new LinkedHashSet<>();
            LinkedHashSet<String> typeXset = new LinkedHashSet<>();

            while (resultSet.next()) {
                logger.info("::::"+resultSet.getString(categoryColum)+ "::"+resultSet.getString(columnlist.get(0))+ "::"+resultSet.getString(legendColumn)+ "::");
                dateSet.add(resultSet.getString(1));   // createdOn
                typeXset.add(resultSet.getString(2));  // legend   // resultSet.getString(3); - legendValues
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
            var headerString ="";
            if (Objects.nonNull(filterRequest.getSearchString()))
                headerString =  filterRequest.getSearchString().toUpperCase()
                        .replace("'","");
            List<String> category = new LinkedList<>(dateSet);
            HighChartsObj highChartsObj = new HighChartsObj();
            highChartsObj.setTitle( title.replace("$title",headerString) );
            highChartsObj.setSubtitle(subtitle);
            highChartsObj.setyAxis("Imei Count"); // to be from legendValue's - headername  //also change
            highChartsObj.setSeriesData(sData);
            highChartsObj.setCatogery(category);
            logger.info(sData + ":::::::::::::::" + category);
            return highChartsObj;
        } catch (Exception e) {
            logger.error("[{(ERROR)}]" + e + "in [" + Arrays.stream(e.getStackTrace()).filter(ste -> ste.getClassName().equals(TopXChart.class.getName())).collect(Collectors.toList()).get(0) + "]");
            return null;
        }
    }

    private static String findCountValue(List<ResponseDetailsForTop> list, String date, String type) {
        for (ResponseDetailsForTop obj : list) {
            if (obj.getDateValue().equals(date) && obj.getTypeValue().equals(type)) {
                return obj.getCountValue();
            }
        }
        return "0"; // Indicates that no matching object was found
    }
}


class ResponseDetailsForTop {
    String dateValue;
    String typeValue;
    String countValue;


    public String getDateValue() {
        return dateValue;
    }

    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getCountValue() {
        return countValue;
    }

    public void setCountValue(String countValue) {
        this.countValue = countValue;
    }

    public ResponseDetailsForTop(String dateValue, String typeValue, String countValue) {
        this.dateValue = dateValue;
        this.typeValue = typeValue;
        this.countValue = countValue;
    }
}