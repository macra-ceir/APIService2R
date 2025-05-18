package com.gl.ceir.config.repository.app;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.app.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {


	public UserProfile getByUserId(long userid);

}
