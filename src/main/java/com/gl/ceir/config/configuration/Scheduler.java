//package com.gl.ceir.config.configuration;
//
//
//// Importing required classes
//
//import com.gl.ceir.config.service.chart.GraphService;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Component
//@Configuration
//@EnableScheduling
//public class Scheduler {
//
//    @Autowired
//    GraphService graphService;
//    private static final Logger logger = LogManager.getLogger(Scheduler.class);
//
//   @Scheduled(cron = "${pdfReportCronTime}")
//    public void scheduleTask() {
//        String strDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date());
//        logger.info("Scheduler start {}", strDate);
//        graphService.getReportExport();
//    }
//}