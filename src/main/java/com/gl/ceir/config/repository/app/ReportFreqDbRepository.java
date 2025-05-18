package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.app.ReportFreqDb;

public interface ReportFreqDbRepository  extends JpaRepository< ReportFreqDb, Long>{
	
	public List<ReportFreqDb> findByReportnameIdOrderByTypeFlagAsc( Long reportnameId );
	
}
