package com.newsservice.application.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.newsservice.application.config.LoggedInUser;
import com.newsservice.application.entity.Picture;
import com.newsservice.application.entity.User;
import com.newsservice.application.exception.ControllerCustomException;
import com.newsservice.application.exception.CustomExceptionHandler;
import com.newsservice.application.model.AppUser;
import com.newsservice.application.model.NewsModelObj;
import com.newsservice.application.repository.UserRepository;
import com.newsservice.application.service.NewsService;
import com.newsservice.application.service.UserService;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/news")
public class NewsServiceController {

	@Autowired
	private NewsService service;
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "To add News article", notes = "To create News Article, Publisher/Admin works on it before publishing")
	@PostMapping("/createArticle")
	public ResponseEntity<?> createNews(@RequestBody NewsModelObj news, @ApiIgnore @LoggedInUser AppUser appUser){
		int newsId = 0;
		try
		{
			System.out.println("<Starting><EndPoint : createArticle><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Inputs: "+news.toString()+">");
			if(news.getNewsId() != 0) {
				ControllerCustomException ex = new ControllerCustomException("616", "Do not provide NewsId to create a Article");
				return new ResponseEntity<ControllerCustomException>(ex,HttpStatus.BAD_REQUEST);
			}
			Collection<GrantedAuthority> credentials = (Collection<GrantedAuthority>) appUser.getAuthorities();
			String role = credentials.stream().map(x -> x.getAuthority()).collect(Collectors.joining());
			System.out.println("Role of loggedIn user"+role);
			System.out.println("loggedIn user"+appUser.getUsername());
			newsId =service.addNewsRecord(news, appUser.getUsername(), role);

		}catch(CustomExceptionHandler e) {
			ControllerCustomException ex = new ControllerCustomException(e.getErrorCode(), e.getErrorMessage());
			System.out.println("<Ending><EndPoint : createArticle><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Exception Occured> ");
			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			ControllerCustomException ex = new ControllerCustomException("611", "Something went wrong in CreateArticle Controller "+e.getMessage());
			System.out.println("<Ending><EndPoint : createArticle><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Exception Occured>"+e.getMessage());
			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
		}
		System.out.println("<Ending><EndPoint : createArticle><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+">");
		return new ResponseEntity<String>("Succusfully created the Article with NewsId: "+newsId,HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "To retrive a News article", notes = "To retrieve a News Article based on NewsId")
	@PostMapping("/viewArticle/{newsId}")
	public ResponseEntity<?> RetriveNewsBasedonId(@PathVariable int newsId,@ApiIgnore @LoggedInUser AppUser appUser){
		NewsModelObj result = new NewsModelObj();
		try {
			System.out.println("<Starting><EndPoint : viewArticle/"+newsId+"><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Inputs: NewsId :"+newsId+">");
			
			result =service.getNews(newsId, appUser.getUsername());
		}catch(CustomExceptionHandler e) {
			ControllerCustomException ex = new ControllerCustomException(e.getErrorCode(), e.getErrorMessage());
			System.out.println("<Ending><EndPoint : viewArticle><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Exception Occured>");
			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			ControllerCustomException ex = new ControllerCustomException("612", e.getMessage());
			System.out.println("<Ending><EndPoint : viewArticle><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Exception Occured>");
			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
		}
		System.out.println("<Ending><EndPoint : viewArticle><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+">");
		return new ResponseEntity<NewsModelObj>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "To retrive Top 10 Unread News article", notes = "To retrieve top 10 unread news articles based on AccountId and Role")
	@PostMapping("/viewTop10Article")
	public ResponseEntity<?> RetriveTop10UnreadNewsBasedonIdAndRole(@ApiIgnore @LoggedInUser AppUser appUser){
		List<NewsModelObj> resultList= new ArrayList<NewsModelObj>();
		try {
			System.out.println("<Starting><EndPoint : viewTop10Article><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+">");
			resultList = service.getNewsBasedOnRoleAndAccountId(appUser.getUsername());
		}catch(CustomExceptionHandler e) {
			ControllerCustomException ex = new ControllerCustomException(e.getErrorCode(), e.getErrorMessage());
			System.out.println("<Ending><EndPoint : viewTop10Article><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Exception Occured>");
			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			ControllerCustomException ex = new ControllerCustomException("613",e.getMessage());
			System.out.println("<Ending><EndPoint : viewTop10Article><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Exception Occured>");
			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
		}
		System.out.println("<Ending><EndPoint : viewTop10Article><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+">");
		return new ResponseEntity<List<NewsModelObj>>(resultList, HttpStatus.OK);
		
	}
	
//	@ApiOperation(value = "To retrive Picture of particular News article", notes = "To retrieve Pictures based on NewsId")
//	@PostMapping("/viewPicture")
//	public ResponseEntity<?> viewPictureBasedOnNewsId(@PathVariable int newsId,Principal princi){
//		Picture pic = new Picture();
//		try{
//			System.out.println("<Starting><EndPoint : viewPicture><CurrentUser: "+princi.getName()+"><Inputs: NewsId :"+newsId+">");
//			pic = service.getPicturesBasedOnNewsId(newsId);
//		}catch(CustomExceptionHandler e) {
//			ControllerCustomException ex = new ControllerCustomException(e.getErrorCode(), e.getErrorMessage());
//			System.out.println("<Ending><EndPoint : viewPicture><CurrentUser: "+princi.getName()+"><Exception Occured>");
//			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
//		}catch(Exception e) {
//			ControllerCustomException ex = new ControllerCustomException("614", "Something went wrong in viewPicture Controller "+e.getMessage());
//			System.out.println("<Ending><EndPoint : viewPicture><CurrentUser: "+princi.getName()+"><Exception Occured>");
//			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
//		}
//		System.out.println("<Ending><EndPoint : viewPicture><CurrentUser: "+princi.getName()+">");
//		return new ResponseEntity<Picture>(pic, HttpStatus.OK);
//		
//	}
		
	@ApiOperation(value = "To delete a News article", notes = "To delete a particular News Article based on NewsId")
	@PostMapping("/deleteArticle/{newsId}")
	public ResponseEntity<?> DeleteNewsBasedonId(@PathVariable int newsId, Principal princi){
		try {
			System.out.println("<Starting><EndPoint : deleteArticle/"+newsId+"><CurrentUser: "+princi.getName()+"><Inputs: NewsId :"+newsId+">");
			service.deleteArticleBasedOnNewdId(newsId);
		}catch(CustomExceptionHandler e) {
			ControllerCustomException ex = new ControllerCustomException(e.getErrorCode(), e.getErrorMessage());
			System.out.println("<Ending><EndPoint : deleteArticle><CurrentUser: "+princi.getName()+"><Exception Occured>");
			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			ControllerCustomException ex = new ControllerCustomException("615", e.getMessage());
			System.out.println("<Ending><EndPoint : deleteArticle><CurrentUser: "+princi.getName()+"><Exception Occured>");
			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
		}
		System.out.println("<Ending><EndPoint : deleteArticle><CurrentUser: "+princi.getName()+">");
		return new ResponseEntity<Void>(HttpStatus.OK);
		
	}
	
	@ApiOperation(value = "To delete Expired articles", notes = "To delete all Articles with expired date")
	@PostMapping("/deleteInValidArticles")
	public ResponseEntity<?> DeleteInValidNews(@ApiIgnore @LoggedInUser AppUser appUser){
		try {
			System.out.println("<Starting><EndPoint : deleteInValidArticles><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+">");
			service.deleteArticlesBasedOnCurrentDate();
		}catch(CustomExceptionHandler e) {
			ControllerCustomException ex = new ControllerCustomException(e.getErrorCode(), e.getErrorMessage());
			System.out.println("<Ending><EndPoint : deleteInValidArticles><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Exception Occured>");
			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			ControllerCustomException ex = new ControllerCustomException("615", e.getMessage());
			System.out.println("<Ending><EndPoint : deleteInValidArticles><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Exception Occured>");
			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
		}
		System.out.println("<Ending><EndPoint : deleteInValidArticles><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+">");
		return new ResponseEntity<Void>(HttpStatus.OK);
		
	}
	
	@ApiOperation(value = "To edit a News article", notes = "To edit a particular News Article based on NewsId")
	@PostMapping("/editArticle/{newsId}")
	public ResponseEntity<?> EditNewsBasedonId(@PathVariable int newsId, @RequestBody NewsModelObj news, @ApiIgnore @LoggedInUser AppUser appUser){
		try {
		System.out.println("<Starting><EndPoint : editArticle/"+newsId+"><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Inputs: NewsId :"+newsId+" and "+news.toString()+">");
		Collection<GrantedAuthority> credentials = (Collection<GrantedAuthority>) appUser.getAuthorities();
		String role = credentials.stream().map(x -> x.getAuthority()).collect(Collectors.joining());
		System.out.println("Role "+role);
		System.out.println("userName: "+appUser.getUsername());
		service.editArticleBasedOnNewdId(newsId,news, appUser.getUsername(), role);
		}catch(CustomExceptionHandler e) {
			ControllerCustomException ex = new ControllerCustomException(e.getErrorCode(), e.getErrorMessage());
			System.out.println("<Ending><EndPoint : editArticle><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Exception Occured>");
			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			ControllerCustomException ex = new ControllerCustomException("616", e.getMessage());
			System.out.println("<Ending><EndPoint : editArticle><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Exception Occured>");
			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
		}
		System.out.println("<Ending><EndPoint : editArticle><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+">");
		return new ResponseEntity<String>("News Article with Id"+newsId+" is succesfully updated",HttpStatus.OK);
		
	}
	
	@ApiOperation(value = "To publish a News article", notes = "To edit a particular News Article based on NewsId")
	@PostMapping("/publishArticle/{newsId}")
	public ResponseEntity<?> PublishNewsBasedonId(@PathVariable int newsId, @ApiIgnore @LoggedInUser AppUser appUser){
		try {
		System.out.println("<Starting><EndPoint : publishArticle/"+newsId+"><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Inputs: NewsId :"+newsId+">");
		Collection<GrantedAuthority> credentials = (Collection<GrantedAuthority>) appUser.getAuthorities();
		String role = credentials.stream().map(x -> x.getAuthority()).collect(Collectors.joining());
		System.out.println("Role "+role);
		service.publishArticleBasedOnNewdId(newsId, appUser.getUsername(), role);
		}catch(CustomExceptionHandler e) {
			ControllerCustomException ex = new ControllerCustomException(e.getErrorCode(), e.getErrorMessage());
			System.out.println("<Ending><EndPoint : editArticle><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Exception Occured>");
			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			ControllerCustomException ex = new ControllerCustomException("616", e.getMessage());
			System.out.println("<Ending><EndPoint : editArticle><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Exception Occured>");
			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
		}
		System.out.println("<Ending><EndPoint : editArticle><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+">");
		return new ResponseEntity<String>("News Article with Id: "+newsId+" is succesfully published",HttpStatus.OK);
		
	}
	
	@ApiOperation(value = "To assign role to an user", notes = "To assign/modify role to an user based on username")
	@PostMapping("/assignRole")
	public ResponseEntity<?> AssignRolesToAnUser(@RequestParam String username, @RequestParam String roleToAssign, @ApiIgnore @LoggedInUser AppUser appUser){
		try {
		System.out.println("<Starting><EndPoint : assignRole><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Inputs: UserName :"+username+", roleToAssign: "+roleToAssign+">");
		service.assignRoleToUser(username, roleToAssign);
		}catch(CustomExceptionHandler e) {
			ControllerCustomException ex = new ControllerCustomException(e.getErrorCode(), e.getErrorMessage());
			System.out.println("<Ending><EndPoint : assignRole><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Exception Occured>");
			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			ControllerCustomException ex = new ControllerCustomException("617",e.getMessage());
			System.out.println("<Ending><EndPoint : assignRole><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Exception Occured>");
			return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
		}
		System.out.println("<Ending><EndPoint : assignRole><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+">");
		return new ResponseEntity<String>("Assigned role for user: "+username,HttpStatus.OK);
		
	}
	
	@PostMapping("/createUser")
	public ResponseEntity<?> createUser(@RequestBody User user, @ApiIgnore @LoggedInUser AppUser appUser) {
		User userResp = new User();
		try {
			System.out.println("<Starting><EndPoint : createUser><CurrentUser: "+appUser.getUsername()+", Role: "+appUser.getAuthorities()+"><Inputs: User :"+user.toString()+">");
			userResp = userService.save(user);
			}catch(CustomExceptionHandler e) {
				ControllerCustomException ex = new ControllerCustomException(e.getErrorCode(), e.getErrorMessage());
				System.out.println("<Ending><EndPoint : assignRole><CurrentUser: "+appUser.getUsername()+"><Exception Occured>");
				return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
			}catch(Exception e) {
				ControllerCustomException ex = new ControllerCustomException("617", "Something went wrong in createUser Controller "+e.getMessage());
				System.out.println("<Ending><EndPoint : assignRole><CurrentUser: "+appUser.getUsername()+"><Exception Occured>");
				return new ResponseEntity<ControllerCustomException>(ex, HttpStatus.BAD_REQUEST);
			}
			System.out.println("<Ending><EndPoint : assignRole><CurrentUser: "+appUser.getUsername()+">");
			return new ResponseEntity<User>(userResp,HttpStatus.OK);
	}
	
}
