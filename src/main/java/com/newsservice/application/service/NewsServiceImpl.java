package com.newsservice.application.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newsservice.application.entity.News;
import com.newsservice.application.entity.Picture;
import com.newsservice.application.entity.ReadStatus;
import com.newsservice.application.entity.User;
import com.newsservice.application.exception.CustomExceptionHandler;
import com.newsservice.application.model.NewsModelObj;
import com.newsservice.application.repository.NewsCrudRepo;
import com.newsservice.application.repository.PictureCrudRepo;
import com.newsservice.application.repository.ReadStatusCrudRepo;
import com.newsservice.application.repository.UserRepository;

@Service
public class NewsServiceImpl implements NewsService{

	@Autowired
	private NewsCrudRepo newsrepo;
	
	@Autowired
	private ReadStatusCrudRepo readstatusrepo;
	
	@Autowired
	private PictureCrudRepo picturerepo;

	@Autowired
	private UserRepository userrepo;
	/*
	 * Insert record in News Table with NewsID as Auto Increment
	 * If any Picture related Info, insert record in Picture table
	 * Insert a record in ReadStatus with NewsId and UserName and no ReadDate
	 */
	@Override
	public int addNewsRecord(NewsModelObj news, String username, String role) {
		//try {
		News newsResp = new News();
			if(news.getTitle().isEmpty() || news.getTitle().length() ==0) {
				System.out.println("To Create a News Article, it must have title");
				throw new CustomExceptionHandler("601","Please enter proper News Title, It is Empty");
			}
			else {
				try {
				News newsObj = new News(news.getTitle(), news.getText(),new Date(), null, null, "PUBLISHER",username);
				newsResp = newsrepo.save(newsObj);
				
				System.out.println("New record inserted into News table at  :"+newsResp.getNewsId());

				if(newsResp.getNewsId() != 0) {
					//System.out.println("News Id generated after inserting record in News Table :"+newsResp.getNewsId());

					if(news.getPictureName() != null && news.getPictureName().length() !=0 && !news.getPictureName().isEmpty() ) {
						Picture picObj = new Picture(newsResp.getNewsId(), news.getPictureName(), news.getAdditionalPictureDate(), news.getMetadata());
						System.out.println("picObj :"+picObj.toString());
						Picture pictureResp = picturerepo.save(picObj);
						System.out.println("New record inserted into Pictures table at  : "+pictureResp.getTabId());
					}
					else {
						System.out.println("No record is required to insert in Picture table, as Picture name is Empty/Null");
					}
					//ReadStatus status = new ReadStatus(newsResp.getNewsId(), Integer.parseInt(username), null);
					//ReadStatus statusResp = readstatusrepo.save(status);
					//System.out.println("New record inserted into ReadStatus table at  : "+statusResp.getTabId());
				}
				else {
					throw new CustomExceptionHandler("601","Issue in inserting record in News Table");
				}
			}catch(ConstraintViolationException e) {
				throw new CustomExceptionHandler("627", "Please provide unique Title name");
			}
			}
//		}catch(IllegalArgumentException e) {
//			throw new CustomExceptionHandler("602", "Given News Article is Null "+e.getMessage());
//		}catch(Exception e) {
//			throw new CustomExceptionHandler("603", "Something went wrong in Service Layer "+e.getMessage());
//		}
			return newsResp.getNewsId();
	}

	/* Fetch News from News based on NewsId
	 * Insert a record in ReadStatus, if not already found else Fetch the record from ReadStatus and update with current Date
	 * Fetch News from Picture based on NewsId
	 */
	@Override
	public NewsModelObj getNews(int newsId, String username) {
		
		NewsModelObj resultObj = new NewsModelObj();
		
			if(newsId !=0) {
				News newsObj = newsrepo.findById(newsId).get();
				if(newsObj == null)
					throw new CustomExceptionHandler("625","No such NewsId exists");
				if(newsObj != null && newsObj.getValidFrom() == null)
					throw new CustomExceptionHandler("626","NewsId "+newsId+" is not yet published");
								
				System.out.println(newsObj.toString());
				ReadStatus statusObj = readstatusrepo.findByNewsIdAndAccountId(username, newsObj.getNewsId());
				System.out.println(statusObj.toString());
				if(statusObj.getAccountId() != null) {
					readstatusrepo.updateExistingWithNewDate(statusObj.getAccountId(), statusObj.getNewsId(), new Date());
				}else {
					statusObj.setNewsId(newsObj.getNewsId());
					statusObj.setAccountId(username);
					statusObj.setReadDate(new Date());
					readstatusrepo.save(statusObj);
				}
				System.out.println("OK, read status updated for AccountId :"+username);

				Picture picObj = picturerepo.findByNewsId(newsObj.getNewsId());
				if(picObj == null)
					resultObj = new NewsModelObj(newsObj.getNewsId(),newsObj.getTitle(), newsObj.getText(), newsObj.getCreationDate(), newsObj.getValidFrom(),
							newsObj.getValidTo(), newsObj.getRoleRestriction(), newsObj.getAuthor(), null, null, null);
				else {
				System.out.println(picObj.toString()); 

				resultObj = new NewsModelObj(newsObj.getNewsId(),newsObj.getTitle(), newsObj.getText(), newsObj.getCreationDate(), newsObj.getValidFrom(),
						newsObj.getValidTo(), newsObj.getRoleRestriction(), newsObj.getAuthor(), picObj.getPictureName(), picObj.getAdditionalPictureData(), picObj.getMetadata());
			
				}
			}
			else {
				throw new CustomExceptionHandler("604","Please verify NewsId");
			}

//		}catch(Exception e) {
//			throw new CustomExceptionHandler("605", "Something went wrong in Service layer while fetching a News Article "+e.getMessage());
//		}
		return resultObj;
	}

	/* Fetch Top 10 Unread News with the help of News and ReadStatus Tables
	 * Update read_date with current date for each record 10 unread newsId's 
	 * Fetch Pictures for the given newsId
	 * Assign Values to return Obj (News and Picture Data) and return as response
	 */
	@Override
	public List<NewsModelObj> getNewsBasedOnRoleAndAccountId(String username) {
		
		List<News> Obj = newsrepo.getNewsIdBasedonAccId(username);
		if(Obj.isEmpty())
			throw new CustomExceptionHandler("606","No unread Articles found for provided username");
		
		System.out.println("No. of record fetched from News Table :"+Obj.size());
		List<NewsModelObj> resultList = new ArrayList<NewsModelObj>();
		NewsModelObj resultObj = new NewsModelObj();
		try {
			for(News x : Obj) {
				if(x != null) {
					
					 readstatusrepo.updateExistingWithNewDate(username, x.getNewsId(), new Date());
					
					Picture picObj = picturerepo.findByNewsId(x.getNewsId());
					System.out.println(picObj.toString()); 
					if(picObj != null && picObj.getPictureName() != null) {
						resultObj = new NewsModelObj(x.getNewsId(),x.getTitle(), x.getText(), x.getCreationDate(), x.getValidFrom(),
								x.getValidTo(), x.getRoleRestriction(),x.getAuthor(), picObj.getPictureName(), picObj.getAdditionalPictureData(), picObj.getMetadata());
					}else 
					resultObj = new NewsModelObj(x.getNewsId(),x.getTitle(), x.getText(), x.getCreationDate(), x.getValidFrom(),
							x.getValidTo(), x.getRoleRestriction(),x.getAuthor(), null, null, null);
					
					resultList.add(resultObj);
				}
			}
//			}
			System.out.println(" Size of Result set: "+resultList.size()); 
		}catch(Exception e) {
			throw new CustomExceptionHandler("603", "Something went wrong in getTop10 unread news Service Layer "+e.getMessage());
		}
		return resultList;
	}

	@Override
	public Picture getPicturesBasedOnNewsId(int newsId) {
		Picture pic = new Picture();
		try {
			  pic =picturerepo.findByNewsId(newsId);
		}catch(IllegalArgumentException e){
			throw new CustomExceptionHandler("607", "Given newsId is null "+e.getMessage());
		}catch(NoSuchElementException e){
			throw new CustomExceptionHandler("608", "Given newsId do not have any records in Pictures Table "+e.getMessage());
		}catch(Exception e) {
			throw new CustomExceptionHandler("603", "Something went wrong in getPicture Service Layer "+e.getMessage());
		}
		return pic;
	}

	
	/* Delete from News
	 * Delete from Picture
	 * Delete accounts associated with newsId 
	 */
	@Override
	public void deleteArticleBasedOnNewdId(int newsId) {
		try {
			newsrepo.deleteNewsBasedOnNewsId(newsId);
			picturerepo.deletePictureBasedOnNewsId(newsId);
			readstatusrepo.deleteAccountsBasedOnNewsId(newsId);
		}catch(IllegalArgumentException e){
			throw new CustomExceptionHandler("609",e.getMessage());
		}catch(NoSuchElementException e){
			throw new CustomExceptionHandler("610", e.getMessage());
		}catch(Exception e) {
			throw new CustomExceptionHandler("603", "Something went wrong in deleteArticle Service Layer "+e.getMessage());
		}
		
	}

	/* Edit data of News Article for a particular NewsId
	 * i.e need an update in News Table
	 * Need an update in Pictures table
	 * does this need to mark article as unread in read_status, considering it as a new unread record??? 
	 */
	@Override
	public String editArticleBasedOnNewdId(int newsId, NewsModelObj news, String username, String role) {
		//try {
			if(newsId != 0) {
				
				News newsObj1 = newsrepo.findById(newsId).get();
				
				if(newsObj1 == null)
					throw new CustomExceptionHandler("620", "No Data available for given NewsId");
				if(newsObj1.getValidFrom() != null)
					throw new CustomExceptionHandler("628", "Article already Published, cannot edit");
					
				if(newsObj1.getAuthor().equalsIgnoreCase(username) || "ADMIN".equalsIgnoreCase(role)) {
					
				try {
				News newsObj = new News(newsId,news.getTitle(), news.getText(),newsObj1.getCreationDate(), newsObj1.getValidFrom(), newsObj1.getValidTo(), newsObj1.getRoleRestriction(),newsObj1.getAuthor());
				System.out.println("newsObj :"+newsObj.toString());
				News newsResp = newsrepo.save(newsObj);
				
					Picture picObj2 = picturerepo.findByNewsId(newsResp.getNewsId());
					 
					if(picObj2 != null && picObj2.getNewsId() != 0){
						System.out.println(picObj2.toString());
						 picturerepo.updatePictureDataBasedOnNewsId(picObj2.getNewsId(), news.getPictureName(), news.getAdditionalPictureDate(), news.getMetadata());
					}
					else {
						Picture picObj1 = new Picture(newsResp.getNewsId(), news.getPictureName(), news.getAdditionalPictureDate(), news.getMetadata());
						Picture pictureResp = picturerepo.save(picObj1);
					}
				
				}catch(Exception e) {
						throw new CustomExceptionHandler("603", "Something went wrong in EditArticle Service Layer "+e.getMessage());
					}
			}else
				 throw new CustomExceptionHandler("621", "Unauthorized to edit News");
			
//				ReadStatus statusObj = readstatusrepo.findByNewsIdAndAccountId(Integer.parseInt(username), newsObj.getNewsId());
//				System.out.println(statusObj.toString());
//				if(statusObj.getAccountId() !=0) {
//					readstatusrepo.updateExistingWithNewDate(statusObj.getAccountId(), statusObj.getNewsId(), new Date());
//				}else {
//					statusObj.setNewsId(newsObj.getNewsId());
//					statusObj.setAccountId(Integer.parseInt(username));
//					statusObj.setReadDate(new Date());
//					readstatusrepo.save(statusObj);
//				}
//				System.out.println("OK, read status updated");

			}else
				throw new CustomExceptionHandler("622","Provide correct NewsId");
//		}catch(IllegalArgumentException e){
//			throw new CustomExceptionHandler("609",e.getMessage());
//		}catch(NoSuchElementException e){
//			throw new CustomExceptionHandler("610", e.getMessage());
//		}catch(Exception e) {
//			throw new CustomExceptionHandler("603", "Something went wrong in EditArticle Service Layer "+e.getMessage());
//		}
		
		return "Successfully Updated the Article with given data";
	}

	/*
	 * Query ReadStatus and fetch NewsIds based on provided accountId 
	 * Query News, update role for which falls under given accountId
	 */
	@Override
	public void assignRoleToUser(String username, String roleToAssign) {
		int roleId =0;
		try {
			roleId = userrepo.findUserIdBasedOnUsername(username);
			System.out.println("role id: "+roleId);
			if(roleId == 0) {
				System.out.println("Exception Occured ");
				throw new CustomExceptionHandler("620","Invalid Username");
			}

		}catch(Exception e) {
			//System.out.println("Exception: " +e.getMessage());
			throw new CustomExceptionHandler("603", "Invalid inputs, please verify");
		}


		try {
			userrepo.updateRoleForRoleId(roleToAssign,roleId);

		}catch(Exception e) {
			System.out.println("Exception: " +e.getMessage());
			throw new CustomExceptionHandler("603", "Something went wrong in assignRole Service Layer "+e.getMessage());
		}
	}

	@Override
	public void deleteArticlesBasedOnCurrentDate() {
		
		List<Integer> newsIds =newsrepo.getNewsIdsWithExpiredValidDate(new Date());
		
		if(newsIds.isEmpty())
			throw new  CustomExceptionHandler("623","No invalid records to delete");
		try {
		newsIds.stream().forEach(x -> {
			newsrepo.deleteNewsBasedOnNewsId(x);
			picturerepo.deletePictureBasedOnNewsId(x);
			readstatusrepo.deleteAccountsBasedOnNewsId(x);
		});
		}catch(IllegalArgumentException e){
			throw new CustomExceptionHandler("609",e.getMessage());
		}catch(NoSuchElementException e){
			throw new CustomExceptionHandler("610", e.getMessage());
		}catch(Exception e) {
			throw new CustomExceptionHandler("603", "Something went wrong in deleteArticlesBasedOnCurrentDate Service Layer "+e.getMessage());
		}
	}

	@Override
	public void publishArticleBasedOnNewdId(int newsId, String username, String role) {
		
			News news = newsrepo.findById(newsId).get();
			System.out.println("Valid From :"+news.getValidFrom());
			if(news != null && news.getValidFrom() != null)
				throw new CustomExceptionHandler("624", "News with id: "+newsId+" is already published");
			
			if(news != null && (news.getAuthor().equalsIgnoreCase(username) ||role.equalsIgnoreCase("ADMIN"))){
				try {
					newsrepo.updateValidFrom(new Date(), newsId);
					newsrepo.updateValidTo(newsId);

					List<String> users =userrepo.getAllUsernames();

					users.stream().forEach(x -> {
						System.out.println("Each User: "+x);
						readstatusrepo.insertEntriesInStatus(x,newsId);
					});
				}catch(IllegalArgumentException e){
					throw new CustomExceptionHandler("609",e.getMessage());
				}catch(NoSuchElementException e){
					throw new CustomExceptionHandler("610", e.getMessage());
				}catch(Exception e) {
					throw new CustomExceptionHandler("603", "Something went wrong in publishArticle Service Layer "+e.getMessage());
				}
			}else
				throw new CustomExceptionHandler("625", "Unauthorized to publish");
	
		
	}

	
}
