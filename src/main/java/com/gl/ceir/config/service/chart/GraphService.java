package com.gl.ceir.config.service.chart;

import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.app.HighChartsObj;
import com.gl.ceir.config.model.app.ReportColumnDb;
import com.gl.ceir.config.model.app.SeriesData;
import com.gl.ceir.config.model.app.TableFilterRequest;
import com.gl.ceir.config.repository.app.ReportColumnDbRepository;
import com.gl.ceir.config.repository.app.ReportDbRepository;
import com.gl.ceir.config.repository.app.SystemConfigurationDbRepository;
import com.gl.ceir.config.request.model.HighCharts.*;
import com.gl.ceir.config.service.chart.chartbuilder.DualAxisChart;
import com.gl.ceir.config.service.chart.chartbuilder.PieChart;
import com.gl.ceir.config.service.chart.chartbuilder.TopXChart;
import com.gl.ceir.config.service.chart.dbquerybuilder.GraphQueryByChartQuery;
import com.gl.ceir.config.service.chart.dbquerybuilder.GraphQueryByColumnName;
import com.gl.ceir.config.service.chart.dbquerybuilder.GraphQueryByReportDataQuery;
import com.gl.ceir.config.util.CallHttpServiceForMail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Service

public class GraphService {
    private static final Logger logger = LogManager.getLogger(GraphService.class);

    @Autowired
    ReportDbRepository reportDbRepository;
    @Autowired
    GraphQueryByChartQuery graphQueryByChartQuery;
    @Autowired
    GraphQueryByColumnName graphQueryByColumnName;
    @Autowired
    ReportColumnDbRepository reportColumnDbRepository;

    @Autowired
    DualAxisChart dualAxisChart;
    @Autowired
    PieChart pieChart;
    @Autowired
    TopXChart topXChart;

    @Autowired
    GraphQueryByReportDataQuery graphQueryByReportDataQuery;

    @Autowired
    CallHttpServiceForMail callHttpServiceForMail;

    @Value("${pdfFilePath}")
    String pdffilepath;

    @Autowired
    SystemConfigurationDbRepository sysCon;
/******/
    public HighChartsObj getReportData(TableFilterRequest filterRequest) {
        try {
            var columnDetails = reportColumnDbRepository.findByReportnameIdOrderByColumnOrderAsc(filterRequest.getReportnameId());
            String query = query(filterRequest, columnDetails);
            return charts(filterRequest, columnDetails, query);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
        }
    }

    private String query(TableFilterRequest filterRequest, List<ReportColumnDb> columnDetails) {

        if (Objects.nonNull(columnDetails.get(0).getReport().getReportDataQuery())) {  // simple query, usually for till date
            return graphQueryByReportDataQuery.graphQueryBuilder(filterRequest, columnDetails);
        } else if (!(Objects.nonNull(columnDetails.get(0).getReport().getChartQuery()))   // optimise
                || columnDetails.get(0).getReport().getChartQuery().equalsIgnoreCase("")) {
            return graphQueryByColumnName.graphQueryBuilder(filterRequest, columnDetails);
        } else {
            return graphQueryByChartQuery.graphQueryBuilder(filterRequest, columnDetails);
        }
    }

    private HighChartsObj charts(TableFilterRequest filterRequest, List<ReportColumnDb> columnDetails, String query) {

        if (columnDetails.stream().filter(p -> p.getChartParam() != null).anyMatch(p -> p.getChartParam().equalsIgnoreCase("LegendValue2")))
            return dualAxisChart.createGraph(query, columnDetails, filterRequest);
        if (filterRequest.getTypeFlag() == 1 || columnDetails.stream().filter(p -> p.getChartType() != null).anyMatch(p -> p.chartType.startsWith("pie"))) {
            return pieChart.createGraph(query, columnDetails, filterRequest);
        } else if (!(Objects.nonNull(columnDetails.get(0).getReport().getChartQuery()))   // optimise
                || columnDetails.get(0).getReport().getChartQuery().equalsIgnoreCase("")) {
            //  return columnNameAsLegendChart.createGraph(query, columnDetails);
            return topXChart.createGraph(query, columnDetails, filterRequest);
        } else {
            return topXChart.createGraph(query, columnDetails, filterRequest);                //  return graphDbTablesRepository.topXGraphBuilder(query, columnDetails);
        }
    }

    public Object getdashboardCounterData(TableFilterRequest filterRequest) {
        var reportQuery = reportDbRepository.findByReportnameId(filterRequest.getReportnameId())
                .getReportDataQuery();
        return  pieChart.getDataByQuery(reportQuery);
    }

    //*******************************************************************


    public Object getReportExport() {
        try {
            String param = null;
            String reqId = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            var filepath = pdffilepath + "/" + reqId + "/";
            Files.createDirectories(Paths.get(filepath + "pdf/"));
            logger.debug("DIRECTORY CRAETED " + filepath);

            int[] a = {180, 181, 182, 183};
            for (int reportnameId : a) {
                var filterRequest = new TableFilterRequest((long) reportnameId, 5L, 1);
                HighChartsObj response = getReportData(filterRequest);
                param = transformHighChart(response);
                // logger.info("HighChartOutput Response:[" + param + "]");
                try (FileWriter writer = new FileWriter(filepath + reportnameId + ".json")) {
                    writer.write(param);
                }
                Thread.sleep(1000);
                executeHighChartExport(filepath, reqId, reportnameId);
            }
            logger.debug("EXPORT DONE  ");
            var fullFileName = mergePdf(filepath, reqId, a);
            logger.info("MERGE PDF DONE" + fullFileName);
            if (fullFileName == null)
                return new MappingJacksonValue(false);
            else
                addToNotification(fullFileName, reqId);
            return new MappingJacksonValue(true);
        } catch (Exception e) {
            logger.error("Chart Response:[" + e + "]");
            return new MappingJacksonValue(false);
        }
    }

    private void addToNotification(String fullFileName, String reqId) {
        try {
            String mess = sysCon.getByTag("pdfReportingEMailMessage").getValue();
            var subject = sysCon.getByTag("pdfReportingEMailSubject").getValue();
            var email = sysCon.getByTag("pdfReportingEMailId").getValue();
            var map = Map.of("message", mess, "subject", subject, "email", email, "txnId", reqId, "file", fullFileName);
            callHttpServiceForMail.callEmailApi(map);
        } catch (Exception e) {
            logger.error("Not able to send notification" + e);
        }
    }

    public String transformHighChart(HighChartsObj inputChart) {
        if (inputChart.getChartType().equalsIgnoreCase("PIE")) {
            return transformHighChartPie(inputChart).toString();
        } else {
            return transformHighChartGeneral(inputChart).toString();
        }
    }

    private HighChartOutputPie transformHighChartPie(HighChartsObj inputChart) {
        HighChartOutputPie outputChart = new HighChartOutputPie();
        Chart chart = new Chart();
        chart.setType(inputChart.getChartType());
        outputChart.setChart(chart);
        Title title = new Title();
        title.setText(inputChart.getTitle());
        outputChart.setTitle(title);
        XAxis xAxis = new XAxis();
        xAxis.setCategories(inputChart.getCatogery());
        outputChart.setxAxis(xAxis);
        // Set yAxis title
        YAxis yAxis = new YAxis();
        Title yAxisTitle = new Title();
        yAxisTitle.setText(inputChart.getyAxis());
        yAxis.setTitle(yAxisTitle);
        outputChart.setyAxis(yAxis);

        List<PieSeriesData> data = new ArrayList<>();
        // Set series data
        for (SeriesData sd : inputChart.getSeriesData()) {
            data.add(new PieSeriesData(sd.getName(), Integer.parseInt((String) sd.getData().get(0))));
        }
        List<PieSeries> seriesList = new ArrayList<>();
        seriesList.add(new PieSeries(inputChart.getyAxis(), data));

        outputChart.setSeries(seriesList);
        return outputChart;
    }


    public HighChartOutput transformHighChartGeneral(HighChartsObj inputChart) {
        HighChartOutput outputChart = new HighChartOutput();

        // Set chart type
        Chart chart = new Chart();
        chart.setType(inputChart.getChartType());

        outputChart.setChart(chart);

        // Set title
        Title title = new Title();
        title.setText(inputChart.getTitle());
        outputChart.setTitle(title);

        // Set xAxis categories
        XAxis xAxis = new XAxis();
        xAxis.setCategories(inputChart.getCatogery());
        outputChart.setxAxis(xAxis);

        // Set yAxis title
        YAxis yAxis = new YAxis();
        Title yAxisTitle = new Title();
        yAxisTitle.setText(inputChart.getyAxis());
        yAxis.setTitle(yAxisTitle);
        outputChart.setyAxis(yAxis);

        // Set series data
        List<Series> seriesList = new ArrayList<>();

        for (SeriesData sd : inputChart.getSeriesData()) {
            seriesList.add(new Series(sd.getName(), sd.getData()));
        }
        outputChart.setSeries(seriesList);
        return outputChart;
    }

    public void executeHighChartExport(String filepathWithReqId, String reqId, int reportnameId) {
        String a = "highcharts-export-server -infile  " + filepathWithReqId + "/" + reportnameId + ".json -outfile " + filepathWithReqId + "/pdf/" + reportnameId + ".pdf --allowCodeExecution true ";
        //   a = pdffilepath + "/start.sh " + reqId + " " + reportnameId;
        logger.info(a);
        try {
//            try {
//                ProcessBuilder pb = new ProcessBuilder(a);
//                Process p = pb.start();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//                String line = null;
//                String response = null;
//                while ((line = reader.readLine()) != null) {
//                    response += line;
//                }
//                logger.info("Response1:: " + response);
//            } catch (Exception e) {
//                logger.error("EXP :: " + e);
//            }
//            try {
//                ProcessBuilder pb1 = new ProcessBuilder(a);
//                Process p1 = pb1.start();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(p1.getInputStream()));
//                String line = null;
//                String response = null;
//                while ((line = reader.readLine()) != null) {
//                    response += line;
//                }
//                logger.info("Response2 :: " + response);
//            } catch (Exception e) {
//                logger.error("EXP :: " + e);
//            }
//            try {
//                String[] cmd = {"bash", "-c", a};
//                Process p = Runtime.getRuntime().exec(cmd);
//                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//                String line = null;
//                String response = null;
//                while ((line = reader.readLine()) != null) {
//                    response += line;
//                }
//                logger.info("Response3 :: " + response);
//            } catch (Exception e) {
//                logger.error("EXP :: " + e);
//            }
            try {
                Process p = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", a});
                int exitCode = p.waitFor();
                logger.info("ExitCode :: {} ", exitCode);
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                String response = null;
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                logger.info("Response :: " + response);
            } catch (Exception ex) {
                logger.error("Not able to execute : " + ex.getLocalizedMessage() + "");
            }
        } catch (Exception e) {
            logger.error("EXP :: " + e);
        }
    }

    private String mergePdf(String filepath, String reqId, int[] a) {
        try {
            PDFMergerUtility ut = new PDFMergerUtility();
            for (int i = 0; i < a.length; i++) {
                File file = new File(filepath + "pdf/" + a[i] + ".pdf");
                ut.addSource(file);
            }
            var fullFilePath = filepath + "/" + reqId + "_combined.pdf";
            ut.setDestinationFileName(fullFilePath);
            ut.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
            return fullFilePath;
        } catch (Exception e) {
            logger.error("error :: " + e);
            return null;
        }
    }


}


//            if (Objects.nonNull(filterRequest.getTypeFlag()) && !filterRequest.getTypeFlag().equals(0)) {
//                columnDetails = columnDetails.stream()
//                        .filter(p -> p.getTypeFlag().equals(filterRequest.getTypeFlag()))
//                        .collect(Collectors.toList());
//            }
// dualAxis chartBuilder . Single and dual are almost same ,
// but note dual has 2 type of response .
// 1. smart count in line, metfone count in column , cellcard in bar ( bar column can not come at same time).
// 2. Dual axis :
// 3. Pie
//      if (query == null) {
//        return new HighChartsObj();
//    }

//   return   graphDbTablesRepository.  getReportDataV2( filterRequest,  pageNumber,  pageSize);
//	saveAuditTrail(filterRequest);
//			if( Objects.nonNull( filterRequest.getGroupBy()))
//				return databaseTablesRepository.getReportDataGroupBy(filterRequest, pageNumber, pageSize);
//			else

//    var seriesdata = JSON.parse(result).seriesData;
//    var xaxis = {type: 'category'};
//var dataobj = [];
//var series = [];
//        for (let i = 0; i < seriesdata.length; i++) {
//        const object = {};
//object.name = seriesdata[i].name;
//object.y = parseInt(seriesdata[i].data[0]);
//        dataobj.push(object);
//    }
//var seriesObject = {
//        name: yaxix,
//colorByPoint: true,
//data: dataobj
//    };


//        var dataobj = [];
//        var series = [];
//        for (let i = 0; i < seriesdata.length; i++) {
//        const object = {};
//            object.name = seriesdata[i].name;
//            object.y = parseInt(seriesdata[i].data[0]);
//            dataobj.push(object);
//        }
//        var seriesObject = {
//                name:yaxix,
//                colorByPoint:true,
//                data:dataobj
//    };
//        series.push(seriesObject);
//***************************************

