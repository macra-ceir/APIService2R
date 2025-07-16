package com.gl.ceir.config.repository.app;

import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.model.app.DBTableNames;
import com.gl.ceir.config.model.app.HighChartsObj;
import com.gl.ceir.config.model.app.ReportColumnDb;
import com.gl.ceir.config.model.app.SeriesData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class GraphDbTablesRepository {
    private static final Logger logger = LogManager.getLogger(GraphDbTablesRepository.class);

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

    @Autowired
    private JdbcTemplate jdbcTemplate;


    //NOT TO TOUCH


    public HighChartsObj simpleGraphBuilder(String query, List<ReportColumnDb> columnDetails) {  // NOT TO TOUCH
        logger.info("Final data query: [" + query + "]");
        var title = columnDetails.get(0).getReport().getReportName();
        var subtitle = columnDetails.get(0).getReport().getOutputTable();
        List<String> category = new LinkedList<>();
        HighChartsObj highChartsObj = new HighChartsObj();
        highChartsObj.setTitle(title);
        highChartsObj.setSubtitle(subtitle);
        highChartsObj.setChartType("spline");
        List<SeriesData> sData = new LinkedList<>();
        var categoryColumn = columnDetails.stream()
                .filter(p -> p.getChartParam().equalsIgnoreCase("category"))
                .map(ReportColumnDb::getColumnName)
                .map(String::toLowerCase)
                .findFirst().get();
        logger.info("categoryColumn" + categoryColumn);
        var columnlist = columnDetails.stream()
                .filter(p -> p.getChartParam().equalsIgnoreCase("series"))
                .map(ReportColumnDb::getColumnName)
                .map(p -> p.toLowerCase())
                .collect(Collectors.toList());
        logger.info("columnlist::" + columnlist + ":::SIZE:" + columnlist.size());

        try (Connection conn = getConnections(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery(query);) {
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
                String type = columnDetails.stream()
                        .filter(p -> p.getColumnName().trim().equalsIgnoreCase(columnName)) //category
                        .map(ReportColumnDb::getChartType)
                        .map(String::toLowerCase)
                        .findFirst().get();
                List<String> columnValues = entry.getValue();
                sData.add(new SeriesData(columnName, type, columnValues));
            }
            highChartsObj.setSeriesData(sData);
            highChartsObj.setCatogery(category);
            logger.info(sData + ":::::::::::::::" + category);
            return highChartsObj;
        } catch (Exception e) {
            logger.error(e + "in [" + Arrays.stream(e.getStackTrace()).filter(ste -> ste.getClassName().equals(GraphDbTablesRepository.class.getName())).collect(Collectors.toList()).get(0) + "]");
            logger.info("Exception [" + e + "]");
            return null;
        }
    }

//    public HighChartsObj topXGraphBuilder(String query, List<ReportColumnDb> columnDetails) {  // NOT TO TOUCH
//        var title = columnDetails.get(0).getReport().getReportName();
//        var subtitle = columnDetails.get(0).getReport().getOutputTable();
//
//        List<SeriesData> sData = new LinkedList<>();
//        var categoryColumn = columnDetails.stream()
//                .filter(p -> p.getChartParam().trim().equalsIgnoreCase("xaxis")) //category
//                .map(ReportColumnDb::getColumnName)
//                .map(String::toLowerCase)
//                .findFirst().get();
//        logger.info("categoryColumn" + categoryColumn);
//        var columnlist = columnDetails.stream()
//                .filter(p -> p.getChartParam().trim().equalsIgnoreCase("yaxis"))  //series
//                .map(ReportColumnDb::getColumnName)
//                .map(p -> p.toLowerCase())
//                .collect(Collectors.toList());
//
//        logger.info("columnlist::" + columnlist + ":::SIZE:" + columnlist.size());
//        logger.info("Final data query:::: [" + query + "]");
//        try (Statement stmt = this.getC onnection().createStatement(); ResultSet resultSet = stmt.executeQuery(query);) {
//            ResultSetMetaData metaData = resultSet.getMetaData();
//            int columnCount = metaData.getColumnCount();// Create a map to store column names and their corresponding lists of values
//            Map<String, List<String>> dataMap = new HashMap<>();
//            List<ResponseDetailsForTop> responseDetailsForTop = new ArrayList<>();
//            LinkedHashSet<String> dateSet = new LinkedHashSet();
//            LinkedHashSet<String> typeXset = new LinkedHashSet();
//            while (resultSet.next()) {
//                dateSet.add(resultSet.getString(1));
//                typeXset.add(resultSet.getString(2));
//                responseDetailsForTop.add(new ResponseDetailsForTop(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)));
//            }
//            responseDetailsForTop.forEach(System.out::println);
//            for (String typeX : typeXset) {
//                for (String date : dateSet) {
//                    var count = findCountValue(responseDetailsForTop, date, typeX);
//                    if (dataMap.containsKey(typeX)) {
//                        List<String> newList = new ArrayList<String>(dataMap.get(typeX));
//                        newList.add(count);
//                        dataMap.put(typeX, newList);
//                    } else {
//                        dataMap.put(typeX, List.of(count));
//                    }
//                }
//            }
//
//            String type  = columnDetails.stream()
//                    .filter(p -> p.getChartParam().trim().equalsIgnoreCase("yaxis")) //category
//                    .map(ReportColumnDb::getChartType)
//                    .map(String::toLowerCase)
//                    .findFirst().get();
//            for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
//                String columnName = entry.getKey();
//                List<String> columnValues = entry.getValue();
//                sData.add(new SeriesData(columnName, type, columnValues));
//            }
//            List<String> category = new LinkedList<>(dateSet);
//            HighChartsObj highChartsObj = new HighChartsObj();
//            highChartsObj.setTitle(title);
//            highChartsObj.setSubtitle(subtitle);
//            highChartsObj.setyAxis("COUNTS");
//            highChartsObj.setSeriesData(sData);
//            highChartsObj.setCatogery(category);
//            logger.info(sData + ":::::::::::::::" + category);
//            return highChartsObj;
//        } catch (Exception e) {
//            logger.error("[{(ERROR)}]" + e + "in [" + Arrays.stream(e.getStackTrace()).filter(ste -> ste.getClassName().equals(GraphDbTablesRepository.class.getName())).collect(Collectors.toList()).get(0) + "]");
//             return null;
//        }
//    }


//    public HighChartsObj simplePieGraphBuilder(String query, List<ReportColumnDb> columnDetails) {  // NOT TO TOUCH
//        logger.info("Final data query: [" + query + "]");
//        var title = columnDetails.get(0).getReport().getReportName();
//        var subtitle = columnDetails.get(0).getReport().getOutputTable();
//        List<String> category = new LinkedList<>();
//        HighChartsObj highChartsObj = new HighChartsObj();
//        highChartsObj.setTitle(title);
//        highChartsObj.setSubtitle(subtitle);
//        highChartsObj.setChartType("spline");
//        List<SeriesData> sData = new LinkedList<>();
//        var categoryColumn = columnDetails.stream()
//                .filter(p -> p.getChartParam().equalsIgnoreCase("category"))
//                .map(ReportColumnDb::getColumnName)
//                .map(String::toLowerCase)
//                .findFirst().get();
//        logger.info("categoryColumn" + categoryColumn);
//        var columnlist = columnDetails.stream()
//                .filter(p -> p.getChartParam().equalsIgnoreCase("series"))
//                .map(ReportColumnDb::getColumnName)
//                .map(p -> p.toLowerCase())
//                .collect(Collectors.toList());
//        logger.info("columnlist::" + columnlist + ":::SIZE:" + columnlist.size());
//
//        try (Statement stmt = this.get Connection().createStatement(); ResultSet resultSet = stmt.executeQuery(query);) {
//            ResultSetMetaData metaData = resultSet.getMetaData();
//            int columnCount = metaData.getColumnCount();// Create a map to store column names and their corresponding lists of values
//            Map<String, List<String>> dataMap = new HashMap<>();//        while (res.next()) {/     category.add(res.getString(String.valueOf(categoryColumn)));
//            while (resultSet.next()) {
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
//            }
//            for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
//                String columnName = entry.getKey();
//                List<String> columnValues = entry.getValue();
//                sData.add(new SeriesData(columnName, columnValues));
//            }
//            highChartsObj.setSeriesData(sData);
//            highChartsObj.setCatogery(category);
//            logger.info(sData + ":::::::::::::::" + category);
//            return highChartsObj;
//        } catch (Exception e) {
//            logger.error(e + "in [" + Arrays.stream(e.getStackTrace()).filter(ste -> ste.getClassName().equals(GraphDbTablesRepository.class.getName())).collect(Collectors.toList()).get(0) + "]");
//            logger.info("Exception [" + e + "]");
//            return null;
//        }
//    }


    public DBTableNames getTables(String dbName) {
        DatabaseMetaData metadata = null;
        DBTableNames dbTableNames = null;
        Connection conn = null;
        ResultSet res = null;
        try {
            conn = this.getConnections();
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


//    private static String findCountValue(List<ResponseDetailsForTop> list, String date, String type) {
//        for (ResponseDetailsForTop obj : list) {
//            if (obj.getDateValue().equals(date) && obj.getTypeValue().equals(type)) {
//                return obj.getCountValue();
//            }
//        }
//        return "0"; // Indicates that no matching object was found
//    }

    //	public long getTotalRows( String query,  SessionImplementor sessionImp ) {
    public long getTotalRows(String query) {
        long rows = 0l;
        Statement stmt = null;
        ResultSet res = null;
        Connection conn = null;
        try {
            conn = getConnections();
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
            conn = getConnections();
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
                if (conn != null)
                    conn.close();
//				if( sessionImp != null )
//					sessionImp.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return rows;
    }


//    public Connection getConnection() {
//        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) em.getEntityManagerFactory();
//        try {
//            var ds =  info.getDataSource();
//            if (ds instanceof HikariDataSource) {
//                HikariDataSource hds = (HikariDataSource) ds;
//                logger.info("HikariCP PoolSize {} ,ConnTimiOut {} ,Max Lifetime{} " , hds.getMaximumPoolSize(), hds.getConnectionTimeout(),hds.getMaxLifetime());
//            }
//            return info.getDataSource().getConnection();
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return null;
//        }
//    }


    public Connection getConnections() {
        try {
            DataSource dataSource = jdbcTemplate.getDataSource();
            if (dataSource != null) {
                return dataSource.getConnection(); //  Don't forget to close it
            } else {
                throw new IllegalStateException("No DataSource found in JdbcTemplate");
            }
        } catch (SQLException e) {
            logger.error("Error while getting connection from JdbcTemplate: {}", e.getMessage(), e);
            return null;
        }
    }


}


//
//class ResponseDetailsForTop {
//    String dateValue;
//    String typeValue;
//    String countValue;
//
//
//    public String getDateValue() {
//        return dateValue;
//    }
//
//    public void setDateValue(String dateValue) {
//        this.dateValue = dateValue;
//    }
//
//    public String getTypeValue() {
//        return typeValue;
//    }
//
//    public void setTypeValue(String typeValue) {
//        this.typeValue = typeValue;
//    }
//
//    public String getCountValue() {
//        return countValue;
//    }
//
//    public void setCountValue(String countValue) {
//        this.countValue = countValue;
//    }
//
//    public ResponseDetailsForTop(String dateValue, String typeValue, String countValue) {
//        this.dateValue = dateValue;
//        this.typeValue = typeValue;
//        this.countValue = countValue;
//    }
//}