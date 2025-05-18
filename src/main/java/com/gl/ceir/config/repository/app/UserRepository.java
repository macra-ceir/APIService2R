package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gl.ceir.config.model.app.ResponseCountAndQuantity;
import com.gl.ceir.config.model.app.User;

import io.lettuce.core.dynamic.annotation.Param;

public interface UserRepository extends JpaRepository<User, Long>{
	
	public User getByUsername(String userName);
	public User getByid(Long id);
	public User getById(long id);
    public List<User> getByUsertype_Id(long id);
	@Query(value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(u.id) as count) from User u "
			+ "where u.currentStatus in (:currentStatus)")
	public ResponseCountAndQuantity getPendingUsersCount( @Param("currentStatus") List< Integer > currentStatus);
	
}
