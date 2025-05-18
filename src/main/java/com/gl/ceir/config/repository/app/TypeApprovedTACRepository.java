package com.gl.ceir.config.repository.app;

import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.gl.ceir.config.model.app.TypeApprovedTAC;

@EnableJpaAuditing
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public interface TypeApprovedTACRepository  extends JpaRepository<TypeApprovedTAC, Long>, JpaSpecificationExecutor<TypeApprovedTAC>{
	
	public TypeApprovedTAC save(TypeApprovedTAC typeApprovedTAC);
	public TypeApprovedTAC getByTac( String tac);
	public TypeApprovedTAC getByTacAndUserId( String tac, Long userId);
	
}
