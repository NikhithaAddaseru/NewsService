package com.newsservice.application.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.newsservice.application.entity.Picture;
import com.newsservice.application.model.NewsModelObj;

public interface NewsService {

	NewsModelObj getNews(int newsId, String name);

	List<NewsModelObj> getNewsBasedOnRoleAndAccountId(String username);

	Picture getPicturesBasedOnNewsId(int newsId);

	void deleteArticleBasedOnNewdId(int newsId);

	void assignRoleToUser(String accountId, String roleToAssign);

	int addNewsRecord(NewsModelObj news, String username, String authorities);

	String editArticleBasedOnNewdId(int newsId, NewsModelObj news, String username, String role);

	void deleteArticlesBasedOnCurrentDate();

	void publishArticleBasedOnNewdId(int newsId, String username, String role);

	

}
