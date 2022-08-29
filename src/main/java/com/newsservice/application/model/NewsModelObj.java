package com.newsservice.application.model;


import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class NewsModelObj {

	private int newsId;
	private String title;
	private String text;
	private Date creationDate;
	private Date validFrom;
	private Date validTo;
	private String roleRestriction;
	private String author;
	
	private String pictureName;
	private String additionalPictureData;
	private String metadata;
	
	public int getNewsId() {
		return newsId;
	}
	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}
	public Date getValidTo() {
		return validTo;
	}
	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}
	public String getRoleRestriction() {
		return roleRestriction;
	}
	public void setRoleRestriction(String roleRestriction) {
		this.roleRestriction = roleRestriction;
	}
	public String getPictureName() {
		return pictureName;
	}
	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}
	public String getAdditionalPictureDate() {
		return additionalPictureData;
	}
	public void setAdditionalPictureDate(String additionalPictureDate) {
		this.additionalPictureData = additionalPictureDate;
	}
	public String getMetadata() {
		return metadata;
	}
	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	@Override
	public String toString() {
		return "NewsModelObj [newsId=" + newsId + ", title=" + title + ", text=" + text + ", creationDate="
				+ creationDate + ", validFrom=" + validFrom + ", validTo=" + validTo + ", roleRestriction="
				+ roleRestriction + ", author=" + author + ", pictureName=" + pictureName + ", additionalPictureDate="
				+ additionalPictureData + ", metadata=" + metadata + "]";
	}
	public NewsModelObj(int newsId, String title, String text, Date creationDate, Date validFrom, Date validTo,
			String roleRestriction, String author, String pictureName, String additionalPictureDate, String metadata) {
		super();
		this.newsId = newsId;
		this.title = title;
		this.text = text;
		this.creationDate = creationDate;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.roleRestriction = roleRestriction;
		this.author= author;
		this.pictureName = pictureName;
		this.additionalPictureData = additionalPictureDate;
		this.metadata = metadata;
	}
	public NewsModelObj() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
