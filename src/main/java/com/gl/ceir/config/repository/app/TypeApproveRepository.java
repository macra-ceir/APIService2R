package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import com.gl.ceir.config.model.app.ResponseCountAndQuantity;
import com.gl.ceir.config.model.app.TypeApprovedDb;

@EnableJpaAuditing
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public interface TypeApproveRepository extends JpaRepository<TypeApprovedDb, Long>, JpaSpecificationExecutor<TypeApprovedDb>{

	public TypeApprovedDb save(TypeApprovedDb typeApprovedDb);
	public TypeApprovedDb findById(long id);
	public TypeApprovedDb getByTxnId(String txnId);
	public TypeApprovedDb getByTacAndUserId( String tac, Long userId);
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(t.id) as count) from TypeApprovedDb t "
			+ "where t.approveStatus in (:approveStatus) and t.userId =:userId and t.featureId =:featureId")
	public ResponseCountAndQuantity getTypeApproveCount( @Param("approveStatus")List<Integer> approveStatus, @Param("userId")Long userId,
			@Param("featureId")long featureId);
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(t.id) as count) from TypeApprovedDb t "
			+ "where t.adminApproveStatus IS NULL and t.featureId =:featureId")
	public ResponseCountAndQuantity getAdminTypeApproveCount( @Param("featureId")long featureId);
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(t.id) as count) from TypeApprovedDb t "
			+ "where t.approveStatus in (:approveStatus) and t.featureId =:featureId")
	public ResponseCountAndQuantity getAdminTypeApproveCount( @Param("approveStatus")List<Integer> approveStatus, @Param("featureId")long featureId);
	
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(t.id) as count) from TypeApprovedDb t "
			+ "where t.approveStatus in (:approveStatus) and t.featureId =:featureId and t.userType =:userType")
	public ResponseCountAndQuantity getAdminTypeApproveCount( @Param("approveStatus")List<Integer> approveStatus, @Param("featureId")long featureId,
			@Param("userType")String userType);
}
