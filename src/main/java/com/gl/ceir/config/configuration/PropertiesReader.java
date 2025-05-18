package com.gl.ceir.config.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
    @PropertySource(value = {"file:../../../../../../resources/applicationMysql.properties"}, ignoreResourceNotFound = true),
    @PropertySource(value = {"file:configuration.properties"}, ignoreResourceNotFound = true)
})

public class PropertiesReader {

	@Value("${spring.jpa.properties.hibernate.dialect}")
	public String dialect;
	
	@Value("${date.view.format}")
	public String dateViewFormat;
	
	@Value("${local-ip}")
	public String localIp;
	
	@Value("${appdbName}")
	public String appdbName;
	
	@Value("${repdbName}")
	public String repdbName;
	
	@Value("${auddbName}")
	public String auddbName;
	
	@Value("${oamdbName}")
	public String oamdbName;
}
