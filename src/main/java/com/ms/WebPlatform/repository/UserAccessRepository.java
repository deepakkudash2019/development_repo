package com.ms.WebPlatform.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ms.WebPlatform.model.Depot;
import com.ms.WebPlatform.model.UserAccessPrevillage;

public interface UserAccessRepository extends CrudRepository<UserAccessPrevillage, Integer> {

	UserAccessPrevillage findByuserId(Integer divId);
	
}
