package com.ms.WebPlatform.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.ms.WebPlatform.model.MsrtcDeviceLog;

public interface deviceLogRepository extends CrudRepository<MsrtcDeviceLog, Integer> {

	List<MsrtcDeviceLog> findBydeviceImei(String imeiNo,Pageable page);
	
	
	// List<Route> findBydivisionId(Integer divId ,Pageable page);
}
