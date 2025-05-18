package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gl.ceir.config.model.app.RequestCountAndQuantity;
import com.gl.ceir.config.model.app.ResponseCountAndQuantity;
import com.gl.ceir.config.model.app.StolenandRecoveryMgmt;

import io.lettuce.core.dynamic.annotation.Param;


public interface StolenAndRecoveryRepository extends JpaRepository<StolenandRecoveryMgmt, Long>,JpaSpecificationExecutor<StolenandRecoveryMgmt> {


	public StolenandRecoveryMgmt save(StolenandRecoveryMgmt stolenandRecoveryDetails);

	public List<StolenandRecoveryMgmt> findByUserIdAndRoleType(Long userId,String roleType);

	public void deleteByTxnId(String txnId);

	public StolenandRecoveryMgmt getByTxnId(String txnid);
	
	public StolenandRecoveryMgmt getById(Long id);

	public void deleteById(Long id);
	
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(srm.id) as count, coalesce(sum(srm.qty),0) as quantity) from StolenandRecoveryMgmt srm "
			+ "where srm.userId =:userId and srm.fileStatus in(:fileStatus) and srm.requestType in(:requestType)")
	public ResponseCountAndQuantity getStolenandRecoveryCount( @Param("userId") long userId, @Param("fileStatus") List<Integer> fileStatus,
			@Param("requestType") List<Integer> requestType);
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(srm.id) as count, coalesce(sum(srm.qty),0) as quantity) from StolenandRecoveryMgmt srm "
			+ "where srm.userId =:userId and srm.fileStatus in(:fileStatus) and srm.requestType in(:requestType)")
	public ResponseCountAndQuantity getBlockUnblockCount( @Param("userId") long userId, @Param("fileStatus") List<Integer> fileStatus,
			@Param("requestType") List<Integer> requestType);
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(srm.id) as count, coalesce(sum(srm.qty),0) as quantity) from StolenandRecoveryMgmt srm "
			+ "where srm.fileStatus in(:fileStatus) and srm.requestType in(:requestType)")
	public ResponseCountAndQuantity getStolenandRecoveryCount( @Param("fileStatus") List<Integer> fileStatus,
			@Param("requestType") List<Integer> requestType);
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(srm.id) as count, coalesce(sum(srm.qty),0) as quantity) from StolenandRecoveryMgmt srm "
			+ "where srm.fileStatus in(:fileStatus) and srm.requestType in(:requestType)")
	public ResponseCountAndQuantity getBlockUnblockCount( @Param("fileStatus") List<Integer> fileStatus,
			@Param("requestType") List<Integer> requestType);
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(srm.id) as count, coalesce(sum(srm.qty),0) as quantity) from StolenandRecoveryMgmt srm "
			+ "where srm.fileStatus in(:fileStatus) and srm.requestType in(:requestType) and srm.operatorTypeId =:operatorTypeId")
	public ResponseCountAndQuantity getBlockUnblockCount( @Param("fileStatus") List<Integer> fileStatus,
			@Param("requestType") List<Integer> requestType,@Param("operatorTypeId") Integer operatorTypeId);
}
