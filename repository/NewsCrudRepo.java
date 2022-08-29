package com.newsservice.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.newsservice.application.entity.News;

public interface NewsCrudRepo extends JpaRepository<News, Integer>{

	
	@Query(value="SELECT n.newsID, n.title, n.text, n.creation_date, n.valid_from, n.valid_to, n.role_restriction "
			+ "FROM newsservice.news n, newsservice.read_status r \r\n"
			+ "WHERE n.newsID=r.newsID\r\n"
			+ "AND r.accountID = :accountId \r\n"
			+ "AND n.role_restriction = :role_restriction \r\n"
			+ "AND r.read_date IS NULL \r\n"
			+ "ORDER BY n.creation_date DESC", nativeQuery = true) 
	List<News> getBasedOnAccountIdAndRoleRestrictionDesc(@Param("accountId") int accountId, @Param("role_restriction") String role_restriction);

	@Query(value="SELECT n.* "
			+ "FROM newsservice.news n, newsservice.read_status r \r\n"
			+ "WHERE n.newsID=r.newsID \r\n"
			+ "AND r.read_date IS NULL \r\n"
			+ "ORDER BY n.creation_date DESC", nativeQuery = true) 
	List<News> getNewsForAdmins();

}
