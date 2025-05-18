package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.app.ReportDb;

@Repository
public interface ReportDbRepository extends JpaRepository< ReportDb, Long>{
	
	public ReportDb findByReportName( String reportName );
	
	//public ReportDb findByReportNameId( Long reportnameId );
	
	public ReportDb findByReportnameId( Long reportnameId );
	
	public List<ReportDb> findByStatus( Integer status );
	
	public List<ReportDb> findByViewFlagOrderByReportOrder( Integer viewflag );
	
	public List<ReportDb> findByViewFlagAndReportCategoryOrderByReportOrder( Integer viewflag, Integer reportCategory );
}
