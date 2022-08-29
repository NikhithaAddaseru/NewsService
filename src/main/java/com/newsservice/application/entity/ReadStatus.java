package com.newsservice.application.entity;


import java.util.Date;

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
@Table(name="read_status")
//@Getter
//@Setter
//@ToString
public class ReadStatus {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "status_id")
	private int tabId;

	@NonNull
	@Column(name = "news_id")
	private int newsId;
	
	@NonNull
	@Column(name = "account_id")
	private String accountId;
	
	@Column(name = "read_date")
	private Date readDate;

	
	public ReadStatus(int newsId, String accountId, Date readDate) {
		super();
		this.newsId = newsId;
		this.accountId = accountId;
		this.readDate = readDate;
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

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Date getReadDate() {
		return readDate;
	}

	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}

	@Override
	public String toString() {
		return "ReadStatus [tabId=" + tabId + ", newsId=" + newsId + ", accountId=" + accountId + ", readDate="
				+ readDate + "]";
	}

	public ReadStatus() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	
}
