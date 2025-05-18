package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gl.ceir.config.model.app.ResponseCountAndQuantity;
import com.gl.ceir.config.model.app.SingleImeiDetails;

import io.lettuce.core.dynamic.annotation.Param;

public interface ImmegreationImeiDetailsRepository extends JpaRepository<SingleImeiDetails, Long> {

	public SingleImeiDetails save(SingleImeiDetails immegreationImeiDetails);
	public SingleImeiDetails getByTxnId( String txnId );
	public List<SingleImeiDetails> getAllByTxnId( String txnId );

	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(g.id) as count, coalesce((sum(CASE g.firstImei WHEN NULL THEN 0 WHEN 0 THEN 0 ELSE 1 END)+"
			+ "sum(CASE g.secondImei WHEN NULL THEN 0 WHEN 0 THEN 0 ELSE 1 END)+sum(CASE g.thirdImei WHEN NULL THEN 0 WHEN 0 THEN 0 ELSE 1 END)+"
			+ "sum(CASE g.fourthImei WHEN NULL THEN 0 WHEN 0 THEN 0 ELSE 1 END)),0) as quantity) from SingleImeiDetails g "
			+ "where g.userId =:userId and g.requestType in (:requestType) and g.processState in(:processState)")
	public ResponseCountAndQuantity getBlockUnblockCount( @Param("userId") long userId, @Param("requestType") List<Integer> requestType,
			@Param("processState") List<Integer> processState);
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(g.id) as count, coalesce((sum(CASE g.firstImei WHEN NULL THEN 0 WHEN 0 THEN 0 ELSE 1 END) +" 
			+ "sum(CASE g.secondImei WHEN NULL THEN 0 WHEN 0 THEN 0 ELSE 1 END)+sum(CASE g.thirdImei WHEN NULL THEN 0 WHEN 0 THEN 0 ELSE 1 END)+"
			+ "sum(CASE g.fourthImei WHEN NULL THEN 0 WHEN 0 THEN 0 ELSE 1 END)),0) as quantity) from SingleImeiDetails g "
			+ "where g.requestType in (:requestType) and g.processState in(:processState)")
	public ResponseCountAndQuantity getBlockUnblockCount( @Param("requestType") List<Integer> requestType, @Param("processState") List<Integer> processState);
	
}
