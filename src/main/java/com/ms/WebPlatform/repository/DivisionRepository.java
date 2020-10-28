package com.ms.WebPlatform.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ms.WebPlatform.model.MsrtcDivisionMaster;

public interface DivisionRepository extends CrudRepository<MsrtcDivisionMaster, Integer> {
	
	MsrtcDivisionMaster findBydivisionCode(Integer divCode);
	MsrtcDivisionMaster findBydivisionName(String divisionName);
	 @Query(value=" select division_id,division_code,division_name from msrtc_division_master mdm inner JOIN msrtc_region_master mrm  on mdm.region_id = mrm.region_id where mrm.state_id = 1", nativeQuery = true)
	  Collection<MsrtcDivisionMaster> findAllDivision();
	
}
