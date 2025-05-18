package com.gl.ceir.config.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.model.app.SystemConfigListDb;
import com.gl.ceir.config.service.impl.ConfigurationManagementServiceImpl;

@Component
public final class InterpSetter {
	
	@Autowired
	ConfigurationManagementServiceImpl configurationManagementServiceImpl;

	public String setInterp(String tag, int value) {
		List<SystemConfigListDb> systemConfigListDbs = configurationManagementServiceImpl.getSystemConfigListByTag(tag);
		
		return systemConfigListDbs.stream().filter(o -> o.getValue() == value).findAny().get().getInterpretation();
	}
	
	public String setConfigInterp(String tag, int value) {
		List<SystemConfigListDb> systemConfigListDbs = configurationManagementServiceImpl.getSystemConfigListByTag(tag);
		
		return systemConfigListDbs.stream().filter(o -> o.getValue() == value).findAny().get().getInterpretation();
	}
}
