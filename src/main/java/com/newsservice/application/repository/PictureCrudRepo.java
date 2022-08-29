package com.newsservice.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.newsservice.application.entity.Picture;


public interface PictureCrudRepo  extends JpaRepository<Picture, Integer>{

	@Query(value="SELECT * FROM newsservice1.picture WHERE news_id =:newsId", nativeQuery = true) 
	Picture findByNewsId(@Param("newsId") int newsId);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM newsservice1.picture WHERE news_id = :newsId", nativeQuery=true)
	void deletePictureBasedOnNewsId(@Param("newsId") int newsId);

	@Modifying
	@Transactional
	@Query(value="UPDATE newsservice1.picture "
			+ "SET picture_name=:pictureName "
			+ ", additional_picture_data =:additionalPictureData "
			+ ", metadata =:metadata "
			+ " WHERE news_id =:newsId", nativeQuery = true) 
	 void updatePictureDataBasedOnNewsId(int newsId, String pictureName, String additionalPictureData,
			String metadata);

}
