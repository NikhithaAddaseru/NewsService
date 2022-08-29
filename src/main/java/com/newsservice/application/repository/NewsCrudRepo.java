package com.newsservice.application.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.newsservice.application.entity.News;

public interface NewsCrudRepo extends JpaRepository<News, Integer>{

	
//	@Query(value="SELECT n.news_id, n.title, n.text, n.creation_date, n.valid_from, n.valid_to, n.role_restriction "
//			+ "FROM newsservice1.news n, newsservice1.read_status r \r\n"
//			+ "WHERE  n.news_id=r.news_id AND r.account_id = :accountId \r\n"
//			+ "AND n.role_restriction = :role_restriction \r\n"
//			+ "AND r.read_date IS NULL \r\n"
//			+ "ORDER BY n.creation_date DESC LIMIT 10", nativeQuery = true) 
//	List<News> getBasedOnAccountIdAndRoleRestrictionDesc(@Param("accountId") int accountId, @Param("role_restriction") String role_restriction);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM newsservice1.news WHERE news_id = :newsId", nativeQuery=true)
	void deleteNewsBasedOnNewsId(@Param("newsId") int newsId);

	@Query(value = "SELECT news_id FROM newsservice1.read_status WHERE account_id = :accountId", nativeQuery=true)
	List<Integer> findNewsIdsBasedOnAccountId(@Param("accountId") int accountId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE newsservice1.news SET role_restriction =:roleToAssign WHERE news_id IN (:newsStr)", nativeQuery=true)
	void updateRoleForNewsIds(@Param("roleToAssign") String roleToAssign,@Param("newsStr") int newsStr);

	@Query(value = "SELECT news_id FROM newsservice1.news WHERE valid_to IS NOT NULL AND valid_to < :date", nativeQuery=true)
	List<Integer> getNewsIdsWithExpiredValidDate(@Param("date") Date date);

	@Modifying
	@Transactional
	@Query(value = "UPDATE newsservice1.news SET \r\n"
			+ " valid_from =:date WHERE news_id = :newsId", nativeQuery = true)
	void updateValidFrom(@Param("date")Date date, @Param("newsId") int newsId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE newsservice1.news SET \r\n"
			+ " valid_to = date_add(current_date(), INTERVAL 1 MONTH) WHERE news_id = :newsId", nativeQuery = true)
	void updateValidTo(@Param("newsId") int newsId);

	@Query(value = "SELECT * FROM newsservice1.news \r\n"
			+ "where news_id IN(\r\n"
			+ "Select news_id from newsservice1.read_status where account_id = :username and read_date IS NULL ) \r\n"
			+ "Order by valid_from DESC \r\n"
			+ "LIMIT 10;", nativeQuery=true)
	List<News> getNewsIdBasedonAccId(@Param("username") String username);

	@Query(value = "SELECT news_id FROM newsservice1.news WHERE valid_from IS NOT NULL", nativeQuery=true)
	List<Integer> getPublishedNewsIds();

}
