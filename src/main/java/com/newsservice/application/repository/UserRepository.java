package com.newsservice.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.newsservice.application.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	@Query(value="SELECT * FROM newsservice1.users WHERE username = :username", nativeQuery = true)
    public User getUserByUsername(@Param("username") String username);

	@Query(value="SELECT distinct username FROM newsservice1.users", nativeQuery = true)
	public List<String> getAllUsernames();

	@Query(value="SELECT ur.role_id FROM newsservice1.users u, newsservice1.users_roles ur WHERE u.username=:username AND u.user_id=ur.user_id", nativeQuery = true)
	public int findUserIdBasedOnUsername(String username);

	@Modifying
	@Transactional
	@Query(value = "UPDATE newsservice1.roles SET role_name =:roleToAssign WHERE role_id =:roleId", nativeQuery=true)
	public void updateRoleForRoleId(String roleToAssign, int roleId);

}
