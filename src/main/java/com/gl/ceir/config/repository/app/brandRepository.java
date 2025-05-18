package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gl.ceir.config.model.app.ConsignmentMgmt;
import com.gl.ceir.config.model.app.brandRepoModel;

public interface brandRepository  extends JpaRepository<brandRepoModel, Long>, JpaSpecificationExecutor<brandRepoModel>  {

	
}
