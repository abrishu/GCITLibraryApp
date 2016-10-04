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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.Message;
import com.gcitsolutions.libraryapp.LibraryException.LibraryException;
import com.gcitsolutions.libraryapp.Service.Administrator;

@RestController
public class BookControllerRest {

	@Autowired
	Administrator adminService;
	@Autowired
	Message message;
	ObjectMapper obj=new ObjectMapper();
	
	public BookControllerRest() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(value = {"/paginationBooks/{searchString}"}, method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String paginationBooks(@PathVariable("searchString") String searchString,Model model) {
			
			try {
					Integer pageCount=0;
					Integer noOfRecords=0;
					if(searchString!=null && !searchString.equals("") && !searchString.equals("null")){
						noOfRecords=adminService.getCountOfBooks(searchString);
					}else{
						noOfRecords=adminService.getCountOfBooks("");
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

	
	@RequestMapping(value = {"/searchBooksRest/{searchString}/{pageNo}"},
			method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public List<Book> searchBooksRest(@PathVariable String searchString,@PathVariable Integer pageNo,
			Model model) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		List<Book> books = new ArrayList<Book>();
		books = adminService.getAllBooks(pageNo, 10, searchString);
		return books;
	}
	
	@RequestMapping(value = "/addBookRest", method = RequestMethod.POST,consumes = "application/json",produces="application/json")
	public String addBook(@RequestBody Book book) 
			throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			adminService.addBook(book);
			return obj.writeValueAsString(new Message(true,"Book " +book.getTitle()+" created successfully"));
		}catch(Exception ex){
			return obj.writeValueAsString(new Message(false,"Error during book creation !!!"));
		}	
	}
	
	@RequestMapping(value = "/deleteBookRest", method = RequestMethod.POST,consumes="application/json; charset=utf-8",produces = "application/json; charset=utf-8")
	public Message deleteBookRest(
			@RequestBody Book book,Model model) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			message = adminService.deleteBook(book);
			return message;
		}catch(Exception ex){
			message.setSuccess(false);
			message.setMessage("Failure during delete operation: Details:"+ex.getMessage());
			return message;
		}
	}
	
	@RequestMapping(value = "/updateBookRest", method = RequestMethod.POST,consumes = "application/json",produces = "application/json; charset=utf-8")
	@ResponseBody
	public String updateBookRest(@RequestBody Book book
			) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			adminService.updateBookInfo(book);
			message.setSuccess(true);
			message.setMessage("Details were updated successfully");
			return obj.writeValueAsString(message);
		}catch(Exception ex){
			message.setSuccess(false);
			message.setMessage("Failure during update operation: Details:"+ex.getMessage());
			return obj.writeValueAsString(message);
		}	
	}
	
	
	@RequestMapping( value = "/getBookById/{bookId}",method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public Book getAuthorById(@PathVariable("bookId") Integer bookId, Locale locale, Model model) {
		try{
			Book book= adminService.getBookInfo(new Book(bookId));
			return book;
		}catch(Exception ex){
			return null;
		}
	}
	
	@RequestMapping( value = "/getAllBooksRest",method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public List<Book> getAllBooksRest() {
		try{
			return adminService.getAllBooks();
		}catch(Exception ex){
			return null;
		}
	}
}
