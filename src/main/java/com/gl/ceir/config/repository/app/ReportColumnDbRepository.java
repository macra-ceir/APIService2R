package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.app.ReportColumnDb;

@Repository
public interface ReportColumnDbRepository extends JpaRepository< ReportColumnDb, Long>{
	
	public List<ReportColumnDb> findByReportnameIdOrderByColumnOrderAsc( Long reportnameId);
	
	public List<ReportColumnDb> findByReportnameIdAndTypeFlagOrderByColumnOrderAsc( Long reportnameId, Integer typeFlag);
	
	public ReportColumnDb findByReportnameIdAndColumnName( Long reportnameId, String columnName);
	
}
