package com.gcitsolutions.libraryapp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.BookCopies;
import com.gcitsolutions.libraryapp.Entity.BookLoan;
import com.gcitsolutions.libraryapp.Entity.Borrower;
import com.gcitsolutions.libraryapp.Entity.LibraryBranch;
import com.gcitsolutions.libraryapp.Entity.Message;
import com.gcitsolutions.libraryapp.LibraryException.LibraryException;
import com.gcitsolutions.libraryapp.Service.Administrator;
import com.gcitsolutions.libraryapp.Service.BorrowerService;
import com.gcitsolutions.libraryapp.Service.Librarian;

@RestController

public class BookLoanController {

	@Autowired
	Administrator adminService;
	@Autowired
	BorrowerService borService;
	@Autowired
	Librarian libService;
	@Autowired
	Message message;
	
	
	public BookLoanController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value = "/validateBorrowerRest/{cardNum}", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public Borrower validateBorrowerRest(
			@PathVariable Integer cardNum) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			Borrower borrower = borService.validateBorrower(new Borrower(cardNum));
			return borrower;
		}catch(Exception ex){
			return null;
		}
	}
	
	
	@RequestMapping(value = "/paginationBookLoans/{searchStringBranch}/{searchStringBook}", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public String paginationBookCopies(@PathVariable("searchStringBranch") String searchStringBranch,
			@PathVariable("searchStringBook") String searchStringBook,
			Model model) {
			
			try {
					Integer pageCount=0;
					Integer noOfRecords=0;
					noOfRecords = libService.getCountOfBookCopies(searchStringBranch, searchStringBook);
					System.out.println(noOfRecords);
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
	
	@RequestMapping(value = "/searchBookCopies/{searchStringBranch}/{searchStringBook}/{pageNo}", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public String searchBookCopies(@PathVariable String searchStringBranch,
			@PathVariable String searchStringBook,
			@PathVariable Integer pageNo,
			Model model) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			List<BookCopies> bcopList = libService.getAllBookCopies(pageNo, 10, searchStringBranch, searchStringBook);
			ObjectMapper mapper=new ObjectMapper();
			String jsonBookCopiesList=mapper.writeValueAsString(bcopList);
			return jsonBookCopiesList;
		}catch(Exception ex){
			ex.printStackTrace();
			return ex.getMessage();
		}
	}
	
	@RequestMapping(value = "/getReturnInfo/{cardNum}", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public List<BookLoan> getReturnInfo(
			@PathVariable Integer cardNum,
			Model model) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			return borService.getCheckedOutBooks(new Borrower(cardNum));
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/getAllBranches", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public List<LibraryBranch> getAllBranches() throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			return adminService.getAllLibraryBranch();
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}	
	
	
	@RequestMapping(value = "/getBookCopiesInfo/{bookId}/{branchId}", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public BookCopies getBookCopiesInfo(@PathVariable Integer bookId,@PathVariable Integer branchId) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			return libService.getBookCopiesForABook(new BookCopies(new Book(bookId),new LibraryBranch(branchId)));
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/performCheckOut", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
	public Message performCheckOut(@RequestBody BookLoan bLoan) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			Message msg=borService.performCheckOut(bLoan);
			return msg;
		}catch(Exception ex){
			ex.printStackTrace();
			return new Message(false,"Failure happened during checkout");
		}
	}
	
	
	
	@RequestMapping(value = "/performReturn", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
	public Message performReturn(
			@RequestBody BookLoan bLoan) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			borService.performReturn(bLoan);
			return new Message(true,"Book successfully returned");
		}catch(Exception ex){
			ex.printStackTrace();
			return new Message(false,"Failure happened during returned");
		}
	}
	
	
	@RequestMapping(value = "/addBookCopiesToBranch", method = RequestMethod.POST,consumes="application/json; charset=utf-8", produces = "application/json; charset=utf-8")
	public Message addBookCopiesToBranch(@RequestBody BookCopies bcop) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			libService.addBookCopiesToBranch(bcop);
			message.setSuccess(true);
			message.setMessage("Details were updated successfully");
			return message;
		}catch(Exception ex){
			ex.printStackTrace();
			message.setSuccess(false);
			message.setMessage("Failure in the operation:"+ex.getMessage());
			return message;
		}
	}
	
	@RequestMapping(value = "/updateBranchDetails", method = RequestMethod.POST,consumes="application/json; charset=utf-8",produces = "application/json; charset=utf-8")
	public Message updateBranchDetails(@RequestBody LibraryBranch branch) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			libService.updateLibraryBranch(branch);
			message.setSuccess(true);
			message.setMessage("Details were updated successfully");
			return message;
		}catch(Exception ex){
			ex.printStackTrace();
			message.setSuccess(false);
			message.setMessage("Failure in the operation:"+ex.getMessage());
			return message;
		}
	}

	
	@RequestMapping(value = "/overrideBookLoan", method = RequestMethod.POST,consumes="application/json; charset=utf-8",produces = "application/json; charset=utf-8")
	public Message overrideBookLoan(@RequestBody BookLoan bloan) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			adminService.overrideBookLoan(bloan);
			message.setSuccess(true);
			message.setMessage("Details were updated successfully");
			return message;
		}catch(Exception ex){
			ex.printStackTrace();
			message.setSuccess(false);
			message.setMessage("Failure in the operation:"+ex.getMessage());
			return message;
		}
	}
	
	

	@RequestMapping(value = "/updateBorrowerInfo", method = RequestMethod.POST,consumes="application/json; charset=utf-8",produces = "application/json; charset=utf-8")
	public Message updateBorrowerInfo(@RequestBody Borrower borrower) throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			adminService.updateBorrower(borrower);
			message.setSuccess(true);
			message.setMessage("Borrrower details were updated successfully");
			return message;
		}catch(Exception ex){
			ex.printStackTrace();
			message.setSuccess(false);
			message.setMessage("Failure in the operation:"+ex.getMessage());
			return message;
		}
	}

	
	
	@RequestMapping(value = "/getAllCheckedOutBooks", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public List<BookLoan> getAllCheckedOutBooks() throws LibraryException, SQLException, JsonGenerationException, JsonMappingException, IOException {
		try{
			return borService.getAllCheckedOutBooks();
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	
	
}
