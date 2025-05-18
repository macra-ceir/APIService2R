package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gl.ceir.config.model.app.GrievanceMsg;


public interface GrievanceMsgRepository extends JpaRepository<GrievanceMsg, Long>, JpaSpecificationExecutor<GrievanceMsg> {
	public GrievanceMsg save( GrievanceMsg grievanceMsg );
	public List<GrievanceMsg> getGrievanceMsgByGrievanceId( String grievanceId );
	public List<GrievanceMsg> getGrievanceMsgByGrievanceIdOrderByIdDesc( String grievanceId);
	public List<GrievanceMsg> getGrievanceMsgByGrievanceIdOrderByModifiedOnDesc( String grievanceId);
	//@Query(value="SELECT gm FROM GrievanceMsg gm WHERE gm.grievanceId =:grievanceId ORDER BY gm.id DESC LIMIT recordLimit")
	public List<GrievanceMsg> getGrievanceMsgByGrievanceIdOrderByIdDesc( String grievanceId, Pageable pageable);
}
