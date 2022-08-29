package com.newsservice.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.newsservice.application.entity.ReadStatus;


public interface ReadStatusCrudRepo  extends JpaRepository<ReadStatus, Integer>{

	@Query(value="SELECT newsID FROM newsservice.read_status WHERE accountID =:accountID", nativeQuery = true) 
	String findNewsIdBasedOnAccId(@Param("accountId") int accountID);

}
