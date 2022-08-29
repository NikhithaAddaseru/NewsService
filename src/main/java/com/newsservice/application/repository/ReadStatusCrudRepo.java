package com.newsservice.application.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.newsservice.application.entity.ReadStatus;


public interface ReadStatusCrudRepo  extends JpaRepository<ReadStatus, Integer>{

	@Query(value="SELECT * FROM newsservice1.read_status WHERE account_id =:accountId AND news_id =:newsId", nativeQuery = true) 
	ReadStatus findByNewsIdAndAccountId(@Param("accountId") String accountId, @Param("newsId") int newsId);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM newsservice1.read_status WHERE news_id = :newsId", nativeQuery=true)
	void deleteAccountsBasedOnNewsId(@Param("newsId") int newsId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE newsservice1.read_status SET read_date = :date WHERE news_id = :newsId AND account_id = :accountId", nativeQuery=true)
	void updateExistingWithNewDate(@Param("accountId") String accountId, @Param("newsId") int newsId,@Param("date") Date date);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO newsservice1.read_status (account_id,news_id,read_date) VALUES (:accountId,:newsId,null)", nativeQuery=true)
	void insertEntriesInStatus(@Param("accountId") String accountId, @Param("newsId") int newsId);

//	@Query(value="SELECT newsID FROM newsservice.read_status WHERE accountID =:accountID", nativeQuery = true) 
//	String findNewsIdBasedOnAccId(@Param("accountId") int accountID);

}
