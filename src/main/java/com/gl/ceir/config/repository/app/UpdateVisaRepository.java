package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gl.ceir.config.model.app.ResponseCountAndQuantity;
import com.gl.ceir.config.model.app.VisaUpdateDb;

import io.lettuce.core.dynamic.annotation.Param;

public interface UpdateVisaRepository extends JpaRepository<VisaUpdateDb, Long>,JpaSpecificationExecutor<VisaUpdateDb>{


	public VisaUpdateDb save(VisaUpdateDb visa);
	public VisaUpdateDb findByEndUserDBData_Id(long id);
	public VisaUpdateDb getById(long id);
	public VisaUpdateDb getByEndUserDBData_Id(long id);
	public VisaUpdateDb getByTxnId(String txnId);
//	
//	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(g.id) as count) from VisaUpdateDb g "
//			+ "where g.userId =:userId and g.status in(:status)")
//	public ResponseCountAndQuantity getCountAndQuantity( @Param("userId") long userId, @Param("status") List<Integer> status );
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(g.id) as count) from VisaUpdateDb g "
			+ "where g.status in(:status)")
	public ResponseCountAndQuantity getCountAndQuantity( @Param("status") List<Integer> status );
	
}