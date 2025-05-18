package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.app.DashboardUsersFeatureStateMap;

public interface DashboardUsersFeatureStateMapRepository extends JpaRepository< DashboardUsersFeatureStateMap, Long>{
	public List<DashboardUsersFeatureStateMap> findByUserTypeIdAndFeatureId( Long userTypeId, Long featureId );
}
