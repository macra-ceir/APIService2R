package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gl.ceir.config.model.app.SystemConfigurationDb;

public interface SystemConfigurationDbRepository extends JpaRepository<SystemConfigurationDb, Long>, JpaSpecificationExecutor<SystemConfigurationDb> {


	public SystemConfigurationDb getByTag(String tag);
	
	 @Query("SELECT NEW com.gl.ceir.config.model.app.SystemConfigurationDb (a.value) FROM SystemConfigurationDb  a where a.tag='dbName' ")
	 public List<SystemConfigurationDb> getByTag1(String tag);
	
	 public SystemConfigurationDb getById(Long id);


}
