package com.gl.ceir.config.repository.app;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gl.ceir.config.model.app.Usertype;

public interface UsertypeRepo extends JpaRepository<Usertype, Long>{
	public List<Usertype> findAll();
	public Usertype findById(long id); 
	public Usertype findByUserTypeName(String usertype);
}
