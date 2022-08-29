package com.newsservice.application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;

@Entity
@Table(name="picture")
//@Getter
//@Setter
//@ToString
public class Picture {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tab_id")
	private int tabId;
	
	@NonNull
	@Column(name = "news_id")
	private int newsId;
	
	@NonNull
	@Column(name = "picture_name")
	private String pictureName;
	
	@Column(name = "additional_picture_data")
	private String additionalPictureData;
	
	private String metadata;
	

	public Picture(int newsId, String pictureName, String additionalPictureData, String metadata) {
		super();
		this.newsId = newsId;
		this.pictureName = pictureName;
		this.additionalPictureData = additionalPictureData;
		this.metadata = metadata;
	}

	public int getTabId() {
		return tabId;
	}

	public void setTabId(int tabId) {
		this.tabId = tabId;
	}

	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

	public String getAdditionalPictureData() {
		return additionalPictureData;
	}

	public void setAdditionalPictureData(String additionalPictureData) {
		this.additionalPictureData = additionalPictureData;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	@Override
	public String toString() {
		return "Picture [tabId=" + tabId + ", newsId=" + newsId + ", pictureName=" + pictureName
				+ ", additionalPictureData=" + additionalPictureData + ", metadata=" + metadata + "]";
	}

	public Picture() {
		super();
		// TODO Auto-generated constructor stub
	}

	
		
}
