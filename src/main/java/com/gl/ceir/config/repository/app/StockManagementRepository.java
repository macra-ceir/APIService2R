package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gl.ceir.config.model.app.ResponseCountAndQuantity;
import com.gl.ceir.config.model.app.StockMgmt;

public interface StockManagementRepository extends JpaRepository<StockMgmt, Long>, JpaSpecificationExecutor<StockMgmt> {

	public StockMgmt save(StockMgmt distributerManagement);
	
	public StockMgmt getByTxnId(String txnId);

	public List<StockMgmt> findByRoleTypeAndUserId(String moduleType, Long userId);

	public StockMgmt findByRoleTypeAndTxnId(String moduleType, String txnId);

	public void deleteByTxnId(String txnId);

	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(sm.id) as count, coalesce(sum(sm.quantity),0) as quantity) from StockMgmt sm "
			+ "where sm.userId =:userId and sm.stockStatus in(:stockStatus)")
	public ResponseCountAndQuantity getStockCountAndQuantity( @Param("userId") long userId, @Param("stockStatus") List<Integer> stockStatus);
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(sm.id) as count, coalesce(sum(sm.quantity),0) as quantity) from StockMgmt sm "
			+ "where sm.assignerId =:assignerId and sm.stockStatus in(:stockStatus)")
	public ResponseCountAndQuantity getStockCountAndQuantityCustom( @Param("assignerId") long assignerId, @Param("stockStatus") List<Integer> stockStatus);
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(sm.id) as count, coalesce(sum(sm.quantity),0) as quantity) from StockMgmt sm "
			+ "where sm.portAddress =:portAddress and sm.stockStatus in(:stockStatus)")
	public ResponseCountAndQuantity getStockCountAndQuantityCustom( @Param("portAddress") Integer portAddress , @Param("stockStatus") List<Integer> stockStatus);
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(sm.id) as count, coalesce(sum(sm.quantity),0) as quantity) from StockMgmt sm "
			+ "where sm.stockStatus in(:stockStatus)")
	public ResponseCountAndQuantity getStockCountAndQuantity(@Param("stockStatus") List<Integer> stockStatus);
	
}
