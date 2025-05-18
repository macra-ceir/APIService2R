package com.gl.ceir.config.repository.app;

import com.gl.ceir.config.model.app.SystemConfigListDb;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SystemConfigListRepository extends CrudRepository<SystemConfigListDb, Long>, 
JpaRepository<SystemConfigListDb, Long>, JpaSpecificationExecutor<SystemConfigListDb>{
	
	public List<SystemConfigListDb> findByTag(String tag, Sort sort);

	public SystemConfigListDb findByTagAndValue(String tag, int value);


	@Query("SELECT DISTINCT a.tag FROM SystemConfigListDb a")
	List<String> findDistinctTags();
	
	@Query("SELECT NEW com.gl.ceir.config.model.app.SystemConfigListDb(a.tag, a.description, a.displayName) FROM SystemConfigListDb a group by a.tag, a.description, a.displayName")
	List<SystemConfigListDb> findDistinctTagsWithDescription();
	
	public SystemConfigListDb getById(long id);
	
}
