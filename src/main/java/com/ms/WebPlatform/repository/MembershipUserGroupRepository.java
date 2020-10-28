package com.ms.WebPlatform.repository;

import org.springframework.data.repository.CrudRepository;

import com.ms.WebPlatform.model.MembershipUserGroup;

public interface MembershipUserGroupRepository extends CrudRepository<MembershipUserGroup, Integer>{

	MembershipUserGroup findByuserId(Integer userId);
	
	
	
}
