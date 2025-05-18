package com.gl.ceir.config.controller;

import com.gl.ceir.config.model.app.DBTableNames;
import com.gl.ceir.config.model.app.ReportTrend;
import com.gl.ceir.config.model.app.TableFilterRequest;
import com.gl.ceir.config.service.chart.GraphService;
import com.gl.ceir.config.service.impl.ReportServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
public class ReportController {

    private static final Logger logger = LogManager.getLogger(ReportController.class);

    @Autowired
    ReportServiceImpl reportServiceImpl;


    @Autowired
    GraphService graphService;

    @ApiOperation(value = "Get all tables.", response = DBTableNames.class)
    @RequestMapping(path = "/report/list", method = {RequestMethod.POST})
    public MappingJacksonValue getAllReport(@RequestParam(value = "reportCategory", defaultValue = "0") Integer reportCategory,
                                            @RequestParam(value = "userId") Long userId,
                                            @RequestParam(value = "userType") String userType,
                                            @RequestParam(value = "featureId") Long featureId,
                                            @RequestParam(value = "publicIp", defaultValue = "NA") String publicIp,
                                            @RequestParam(value = "browser", defaultValue = "NA") String browser) {
        return new MappingJacksonValue(reportServiceImpl.getAllReport(reportCategory, userId, userType, featureId,
                publicIp, browser));
    }

    @ApiOperation(value = "Get all table columns.", response = DBTableNames.class)
    @RequestMapping(path = "/report/columnList", method = {RequestMethod.POST})
    public MappingJacksonValue getAllColumns(@RequestParam(value = "reportnameId") Long reportnameId,
                                             @RequestParam(value = "typeFlag", defaultValue = "2", required = false) Integer typeFlag) {
        if (Objects.nonNull(typeFlag) && !typeFlag.equals(0))
            return new MappingJacksonValue(reportServiceImpl.getColumns(reportnameId, typeFlag));
        else
            return new MappingJacksonValue(reportServiceImpl.getColumns(reportnameId));
    }

    @ApiOperation(value = "Get report trend list.", response = ReportTrend.class)
    @RequestMapping(path = "/report/details", method = {RequestMethod.POST})
    public MappingJacksonValue getTrendList(@RequestParam(value = "reportnameId") Long reportnameId) {
        return new MappingJacksonValue(reportServiceImpl.getSingleReportDetail(reportnameId));
    }


    @ApiOperation(value = "Get report data.")
    @RequestMapping(path = "/report/data", method = {RequestMethod.POST})
    public MappingJacksonValue getTableData(@RequestBody TableFilterRequest filterRequest,
                                            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(value = "file", defaultValue = "0", required = false) int file) {
        logger.info("Report filter request:[" + filterRequest.toString() + "]");
        if (file == 0)
            return new MappingJacksonValue(reportServiceImpl.getReportData(filterRequest, pageNumber, pageSize));
        else
            return new MappingJacksonValue(reportServiceImpl.downloadReportData(filterRequest, pageNumber, pageSize));
    }


    @ApiOperation(value = "Get report data.")
    @RequestMapping(path = "/graph/data", method = {RequestMethod.POST})
    public MappingJacksonValue getGraphData(@RequestBody TableFilterRequest filterRequest,
                                            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(value = "file", defaultValue = "0", required = false) int file) {
        logger.info("Chart request:[" + filterRequest.toString() + "]");
        //if( file == 0 )
        var response = graphService.getReportData(filterRequest);
        logger.info("Chart Response:[" + response + "]");
        return new MappingJacksonValue(response);
    }


    @ApiOperation(value = "Get report DashboardCounter Data.")
    @RequestMapping(path = "/graph/dashboardCounter", method = {RequestMethod.POST})
    public MappingJacksonValue dashboardCounter(@RequestBody TableFilterRequest filterRequest) {
        logger.info("Chart request:[" + filterRequest.toString() + "]");
        var response = graphService.getdashboardCounterData(filterRequest);
        logger.info("Chart Response:[" + response + "]");
        return new MappingJacksonValue(response);
    }

    @ApiOperation(value = "Get Report Pdf Data.")
    @RequestMapping(path = "/graph/pdfadaptor", method = {RequestMethod.GET})
    public MappingJacksonValue getReportExport() {
        return new MappingJacksonValue(graphService.getReportExport());
    }
}


// mkdir folder abc
//for 0-n   //graphs
//     HighChartsObj response  = graphService.getReportData(filterRequest);
//    logger.info("Chart Response:[" + response + "]");
// change response to new resposne
// add new response to json file
//  // call exportserver using cmd line // it creates pdf at certain locaion,
// get all pdfs and merge (OPTIONAL)
//  add in notificTION . attach in mail (we are not sending mail having attachment) send mail

//