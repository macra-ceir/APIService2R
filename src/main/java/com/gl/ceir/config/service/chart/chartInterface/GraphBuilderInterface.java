package com.gl.ceir.config.service.chart.chartInterface;

import com.gl.ceir.config.model.app.HighChartsObj;
import com.gl.ceir.config.model.app.ReportColumnDb;
import com.gl.ceir.config.model.app.TableFilterRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface GraphBuilderInterface  {
    HighChartsObj createGraph(String query, List<ReportColumnDb> columnDetails, TableFilterRequest filterRequest);
}
