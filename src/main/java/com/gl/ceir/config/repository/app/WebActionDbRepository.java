package com.gl.ceir.config.repository.app;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.app.WebActionDb;

public interface WebActionDbRepository extends JpaRepository<WebActionDb, Long> {

	@SuppressWarnings("unchecked")
	public WebActionDb save(WebActionDb webActionDb);

	public WebActionDb getByState(int state);

	public WebActionDb findFirstByState(int state);
	
}
