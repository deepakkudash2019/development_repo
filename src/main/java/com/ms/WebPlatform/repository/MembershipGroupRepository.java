package com.ms.WebPlatform.repository;

import org.springframework.data.repository.CrudRepository;

import com.ms.WebPlatform.model.MembershipGroup;

public interface MembershipGroupRepository extends CrudRepository<MembershipGroup, Integer>{
	
	
	MembershipGroup findBygroupName(String groupName);
	
}
