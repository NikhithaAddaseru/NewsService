package com.newsservice.application.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import io.swagger.annotations.ApiModelProperty;

//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;

@Entity
@Table(name="news")
//@Getter
//@Setter
//@ToString
public class News {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "news_id")
	private int newsId;
	
	@NonNull
	@ApiModelProperty(example = "USER")
	private String title;
	
	private String text;
	
	@NonNull
	//@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
//	
//	@PrePersist
//	private void onCreate() {
//		creation_date = new Date();
//	}
	@Column(name = "valid_from")
	private Date validFrom;
	
	@Column(name = "valid_to")
	private Date validTo;
	
	@NonNull
	@Column(name = "role_restriction")
	@ApiModelProperty(example = "PUBLISHER")
	private String roleRestriction;
		
	@NonNull
	@Column(name = "author")
	private String author;
	

	public News(String title, String text, Date creationDate, Date validFrom, Date validTo,
			String roleRestriction,String author) {
		super();
		this.title = title;
		this.text = text;
		this.creationDate = creationDate;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.roleRestriction = roleRestriction;
		this.author = author;
	}
	
	

	public News(int newsId, String title, String text, Date creationDate, Date validFrom, Date validTo,
			String roleRestriction,String author) {
		super();
		this.newsId = newsId;
		this.title = title;
		this.text = text;
		this.creationDate = creationDate;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.roleRestriction = roleRestriction;
		this.author = author;
	}



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

	
	@Override
	public String toString() {
		return "News [newsId=" + newsId + ", title=" + title + ", text=" + text + ", creationDate=" + creationDate
				+ ", validFrom=" + validFrom + ", validTo=" + validTo + ", roleRestriction=" + roleRestriction
				+ ", author=" + author + "]";
	}



	public String getAuthor() {
		return author;
	}



	public void setAuthor(String author) {
		this.author = author;
	}



	public News() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
}
