package com.ms.WebPlatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.ms.WebPlatform.model.MembershipUser;

public interface MembershipUserRepository extends JpaRepository<MembershipUser, Integer> {
	
	MembershipUser findByuserLogin(String login);
	MembershipUser findByuserEmail(String emailId);
	
	
}
