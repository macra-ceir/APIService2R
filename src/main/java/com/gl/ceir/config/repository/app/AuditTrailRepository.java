package com.gl.ceir.config.repository.app;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.app.AuditTrail;

public interface AuditTrailRepository extends JpaRepository<AuditTrail, Long> {

}
