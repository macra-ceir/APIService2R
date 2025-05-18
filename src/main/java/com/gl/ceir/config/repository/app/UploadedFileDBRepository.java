package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.model.app.UploadedFileDB;
import com.gl.ceir.config.model.constants.CopyStatus;

@Component
public interface UploadedFileDBRepository extends JpaRepository<UploadedFileDB, Long> {
	public UploadedFileDB save(UploadedFileDB uploadedFileDB);
	public List<UploadedFileDB> getByCopyStatus(CopyStatus copyStatus);
}
