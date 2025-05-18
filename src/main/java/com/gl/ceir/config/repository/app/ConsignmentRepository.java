package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gl.ceir.config.model.app.ConsignmentMgmt;
import com.gl.ceir.config.model.app.ResponseCountAndQuantity;

import io.lettuce.core.dynamic.annotation.Param;

public interface ConsignmentRepository extends JpaRepository<ConsignmentMgmt, Long>, JpaSpecificationExecutor<ConsignmentMgmt> {

	public ConsignmentMgmt save(ConsignmentMgmt consignment);

	public ConsignmentMgmt getByConsignmentNumber(String consignmEntNumber);

	public List<ConsignmentMgmt> getByUserIdOrderByIdDesc(Long userId);

	public ConsignmentMgmt getByTxnId(String txnId);

	public List<ConsignmentMgmt> findByUser_id(int id);

	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(c.id) as count, coalesce(sum(c.quantity),0) as quantity) from ConsignmentMgmt c "
			+ "where c.userId =:userId and c.consignmentStatus in (:consignmentStatus)")
	public ResponseCountAndQuantity getConsignmentCountAndQuantity( @Param("userId") Integer userId, @Param("consignmentStatus") List< Integer > consignmentStatus);
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(c.id) as count, coalesce(sum(c.quantity),0) as quantity) from ConsignmentMgmt c "
			+ "where c.consignmentStatus in (:consignmentStatus)")
	public ResponseCountAndQuantity getConsignmentCountAndQuantity( @Param("consignmentStatus") List< Integer > consignmentStatus);
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(c.id) as count, coalesce(sum(c.quantity),0) as quantity) from ConsignmentMgmt c "
			+ "where c.consignmentStatus in (:consignmentStatus) and c.portAddress =:portAddress")
	public ResponseCountAndQuantity getConsignmentCountAndQuantity( @Param("consignmentStatus") List< Integer > consignmentStatus,
			@Param("portAddress") Integer portAddress);

}
