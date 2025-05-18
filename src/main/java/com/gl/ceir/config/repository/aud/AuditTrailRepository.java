package com.gl.ceir.config.repository.aud;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.aud.AuditTrail;

public interface AuditTrailRepository extends JpaRepository<AuditTrail, Long> {

}
