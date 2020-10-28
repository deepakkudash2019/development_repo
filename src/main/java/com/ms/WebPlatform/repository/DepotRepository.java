package com.ms.WebPlatform.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ms.WebPlatform.model.Depot;

public interface DepotRepository extends CrudRepository<Depot, Integer> {

	List<Depot> findBydivisionId(Integer divId);
	List<Depot> findBydepotCode(String depotCode);
	
	
}
