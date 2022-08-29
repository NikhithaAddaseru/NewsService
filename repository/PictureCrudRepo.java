package com.newsservice.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsservice.application.entity.Picture;


public interface PictureCrudRepo  extends JpaRepository<Picture, Integer>{

}
