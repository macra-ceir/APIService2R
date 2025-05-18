package com.gl.ceir.config.service.chart.chartInterface;

import com.gl.ceir.config.model.app.ReportColumnDb;
import com.gl.ceir.config.model.app.TableFilterRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GraphQueryInterface {
    public String graphQueryBuilder(TableFilterRequest filterRequest, List<ReportColumnDb> columnDetails);
}
