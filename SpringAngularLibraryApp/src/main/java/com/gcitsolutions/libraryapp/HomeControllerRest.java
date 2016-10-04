package com.gcitsolutions.libraryapp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.gcitsolutions.libraryapp.Entity.Author;
import com.gcitsolutions.libraryapp.Entity.Genre;
import com.gcitsolutions.libraryapp.Entity.Message;
import com.gcitsolutions.libraryapp.Entity.Publisher;
import com.gcitsolutions.libraryapp.LibraryException.LibraryException;
import com.gcitsolutions.libraryapp.Service.Administrator;

/**
 * Handles requests for the application home page.
 */
@RestController
public class HomeControllerRest {
	
	@Autowired
	Administrator adminService;
	@Autowired
	Message message;
	
	ObjectMapper obj=new ObjectMapper();

	
	
	@RequestMapping(value = {"/paginationAuthors/{searchString}"}, method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String paginationAuthors(@PathVariable("searchString") String searchString,Model model) {
			
			try {
					Integer pageCount=0;
					Integer noOfRecords=0;
					if(searchString!=null && !searchString.equals("")){
						noOfRecords=adminService.getCountOfAuthors(searchString);
					}else{
						noOfRecords=adminService.getCountOfAuthors("");
					}
					Integer pageSize=10;
					if(noOfRecords%10==0){
						if((noOfRecords/pageSize)==0){
							pageCount=1;
						}else{
							pageCount=(noOfRecords/pageSize);	
						}
					}else{
						pageCount=(noOfRecords/pageSize)+1;
					}	
					return pageCount.toString();
					
			} catch (Exception e) {
				e.printStackTrace();
				return "0";
			}
			
	}
	
	@RequestMapping(value = {"/paginationAuthors"}, method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String paginationAuthors() {
			
			try {
					Integer pageCount=0;
					Integer noOfRecords=0;
					noOfRecords=adminService.getCountOfAuthors("");
					Integer pageSize=10;
					if(noOfRecords%10==0){
						if((noOfRecords/pageSize)==0){
							pageCount=1;
						}else{
							pageCount=(noOfRecords/pageSize);	
						}
					}else{
						pageCount=(noOfRecords/pageSize)+1;
					}	
					return pageCount.toString();
					
			} catch (Exception e) {
				e.printStackTrace();
				return "0";
			}
			
	}
	
	//,"/searchAuthors/{pageNo}","/searchAuthors"
	@RequestMapping(value = {"/searchAuthors/{searchString}/{pageNo}"},
			method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public List<Author> searchAuthorsRest(@PathVariable String searchString,@PathVariable Integer pageNo,
			Model model) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		System.out.println(searchString+" - "+pageNo);
		List<Author> authors = new ArrayList<Author>();
		authors = adminService.getAllAuthors(pageNo, 10, searchString);
		return authors;
	}
	
	@RequestMapping(value = {"/searchAuthors/{pageNo}"},
			method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public List<Author> searchAuthorsRest(@PathVariable Integer pageNo,
			Model model) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		List<Author> authors = new ArrayList<Author>();
		authors = adminService.getAllAuthors(pageNo, 10, null);
		return authors;
	}
	
	
	@RequestMapping(value = "/addAuthorRest", method = RequestMethod.POST,consumes = "application/json",produces="application/json")
	public String addAuthor(@RequestBody Author author) 
			throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			adminService.addAuthor(author);
			return obj.writeValueAsString(new Message(true,"Author " +author.getName()+" created successfully"));
		}catch(Exception ex){
			return obj.writeValueAsString(new Message(false,"Error during author creation !!!"));
		}	
	}
	
	@RequestMapping(value = "/addAuthorMongo/{authorName}", method = RequestMethod.POST)
	public String addAuthorMongo(@PathVariable("authorName") String authorName,
			ModelMap model) 
			throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			Author author = new Author();
			author.setName(HtmlUtils.htmlEscape(authorName));
			adminService.addAuthorMongo(author);
			model.addAttribute("success","Author "+ HtmlUtils.htmlEscape(authorName) + " added successfully");
			return "viewAuthors";
		}catch(Exception ex){
			model.addAttribute( "error","Failure during operation: Details:"+ HtmlUtils.htmlEscape(ex.getMessage()));
			return "viewAuthors";
		}	
	}

	
	@RequestMapping(value = "/deleteAuthorRest", method = RequestMethod.POST,consumes="application/json; charset=utf-8",produces = "application/json; charset=utf-8")
	public String deleteAuthor(
			@RequestBody Author author,Model model) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			message = adminService.deleteAuthor(author);
			return obj.writeValueAsString(message);
		}catch(Exception ex){
			message.setSuccess(false);
			message.setMessage("Failure during delete operation: Details:"+ex.getMessage());
			return obj.writeValueAsString(message);
		}
	}
	
	
	@RequestMapping(value = "/updateAuthorRest", method = RequestMethod.POST,consumes = "application/json",produces = "application/json; charset=utf-8")
	@ResponseBody
	public String updateAuthorRest(@RequestBody Author author
			) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			adminService.updateAuthor(author);
			message.setSuccess(true);
			message.setMessage("Details were updated successfully");
			return obj.writeValueAsString(message);
		}catch(Exception ex){
			message.setSuccess(false);
			message.setMessage("Failure during update operation: Details:"+ex.getMessage());
			return obj.writeValueAsString(message);
		}	
	}
	
	@RequestMapping( value = "/getAllAuthorsRest",method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getAllAuthors(Locale locale, Model model) {
		try{
			List<Author> authorList= adminService.getAllAuthors();
			return obj.writeValueAsString(authorList);
		}catch(Exception ex){
			return null;
		}
	}
	
	@RequestMapping( value = "/getAllPublishers",method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<Publisher> getAllPublishers(Locale locale, Model model) {
		try{
			List<Publisher> pubList= adminService.getAllPublishers();
			return pubList;
		}catch(Exception ex){
			return null;
		}
	}
	
	@RequestMapping( value = "/getAllGenres",method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<Genre> getAllGenres(Locale locale, Model model) {
		try{
			List<Genre> genreList= adminService.getAllGenres();
			return genreList;
		}catch(Exception ex){
			return null;
		}
	}
	
	@RequestMapping( value = "/getAllAuthorsMongo",method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getAllAuthorsMongo(Locale locale, Model model) {
		try{
			List<Author> authorList= adminService.getAllAuthors();
			return obj.writeValueAsString(authorList);
		}catch(Exception ex){
			return null;
		}
	}
	
	@RequestMapping( value = "/getAuthorById/{authorId}",method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public Author getAuthorById(@PathVariable("authorId") Integer authorId, Locale locale, Model model) {
		try{
			Author author= adminService.getAuthorById(new Author(authorId));
			return author;
		}catch(Exception ex){
			return null;
		}
	}
	
	
}
