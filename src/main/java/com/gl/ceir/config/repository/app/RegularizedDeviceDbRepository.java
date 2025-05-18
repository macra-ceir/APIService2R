package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

//import com.gl.ceir.config.factory.CustomerCareRepo;
import com.gl.ceir.config.model.app.RegularizeDeviceDb;
import com.gl.ceir.config.model.app.ResponseCountAndQuantity;

import io.lettuce.core.dynamic.annotation.Param;

public interface RegularizedDeviceDbRepository extends JpaRepository<RegularizeDeviceDb, Long>, 
JpaSpecificationExecutor<RegularizeDeviceDb	> {
//, CustomerCareRepo<RegularizeDeviceDb>
	public RegularizeDeviceDb getByDeviceSerialNumber(String serialNumber);

	public void deleteByDeviceSerialNumber(String serialNumber);

	public List<RegularizeDeviceDb> getByNid(String nid);

	public RegularizeDeviceDb getByFirstImei(String imei1);

	public Long countByNid(String nid);

	public RegularizeDeviceDb getByTxnId(String txnid);

	@Query("SELECT r FROM RegularizeDeviceDb r WHERE firstImei = :imei OR secondImei = :imei OR thirdImei = :imei OR fourthImei = :imei") 
	public RegularizeDeviceDb getByImei(String imei);

	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(g.id) as count, coalesce((sum(CASE WHEN g.firstImei IS NULL OR g.firstImei ='' THEN 0 ELSE 1 END)+"
			+ "sum(CASE WHEN g.secondImei IS NULL OR g.secondImei ='' THEN 0 ELSE 1 END)+sum(CASE WHEN g.thirdImei IS NULL OR g.thirdImei ='' THEN 0 ELSE 1 END)+"
			+ "sum(CASE WHEN g.fourthImei IS NULL OR g.fourthImei ='' THEN 0 ELSE 1 END)),0) as quantity) from RegularizeDeviceDb g "
			+ "where g.creatorUserId =:creatorUserId and g.status in(:status)")
	public ResponseCountAndQuantity getCountAndQuantity( @Param("creatorUserId") long creatorUserId, @Param("status") List<Integer> status );
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(g.id) as count, coalesce((sum(CASE WHEN g.firstImei IS NULL OR g.firstImei ='' THEN 0 ELSE 1 END)+"
			+ "sum(CASE WHEN g.secondImei IS NULL OR g.secondImei ='' THEN 0 ELSE 1 END)+sum(CASE WHEN g.thirdImei IS NULL OR g.thirdImei ='' THEN 0 ELSE 1 END)+"
			+ "sum(CASE WHEN g.fourthImei IS NULL OR g.fourthImei ='' THEN 0 ELSE 1 END)),0) as quantity) from RegularizeDeviceDb g "
			+ "where g.status in(:status)")
	public ResponseCountAndQuantity getCountAndQuantity( @Param("status") List<Integer> status );
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(g.id) as count, coalesce((sum(CASE WHEN g.firstImei IS NULL OR g.firstImei ='' THEN 0 ELSE 1 END)+"
			+ "sum(CASE WHEN g.secondImei IS NULL OR g.secondImei ='' THEN 0 ELSE 1 END)+sum(CASE WHEN g.thirdImei IS NULL OR g.thirdImei ='' THEN 0 ELSE 1 END)+"
			+ "sum(CASE WHEN g.fourthImei IS NULL OR g.fourthImei ='' THEN 0 ELSE 1 END)),0) as quantity) from RegularizeDeviceDb g "
			+ "where g.status in(:status) and g.origin =:origin")
	public ResponseCountAndQuantity getCountAndQuantity( @Param("status") List<Integer> status,  String origin);
	
}
