package com.gcitsolutions.libraryapp.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gcitsolutions.libraryapp.Data.AuthorDAO;
import com.gcitsolutions.libraryapp.Data.BookCopiesDAO;
import com.gcitsolutions.libraryapp.Data.BookDAO;
import com.gcitsolutions.libraryapp.Data.BookLoanDAO;
import com.gcitsolutions.libraryapp.Data.BorrowerDAO;
import com.gcitsolutions.libraryapp.Data.GenreDAO;
import com.gcitsolutions.libraryapp.Data.LibraryBranchDAO;
import com.gcitsolutions.libraryapp.Data.PublisherDAO;
import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.BookCopies;
import com.gcitsolutions.libraryapp.Entity.BookLoan;
import com.gcitsolutions.libraryapp.Entity.Borrower;
import com.gcitsolutions.libraryapp.Entity.LibraryBranch;
import com.gcitsolutions.libraryapp.Entity.Message;
import com.gcitsolutions.libraryapp.LibraryException.LibraryException;

public class BorrowerService {

private static BorrowerService borrowerInstance=null;
@Autowired
AuthorDAO adao;
@Autowired
BookDAO bdao;
@Autowired
PublisherDAO pubdao;
@Autowired 
LibraryBranchDAO libdao;
@Autowired
BookLoanDAO bldao;
@Autowired
BookCopiesDAO bcopdao;
@Autowired
GenreDAO genredao;
@Autowired
BorrowerDAO bordao;
@Autowired
Message message;
	
	public BorrowerService() {
	}
	
	public static BorrowerService getInstance(){
		if(borrowerInstance==null){
			synchronized(Administrator.class){
				if(borrowerInstance==null){
					return new BorrowerService();
				}
			}
		}
		return borrowerInstance;
	}
	
	public List<LibraryBranch> getAllBranches() throws Exception{
		List<LibraryBranch> libList=libdao.readAll();
		if(libList!=null){
			if(!libList.isEmpty()){
				return libList;
			}
		}
		return null;
	}
	
	public List<Book> getAvailableBooksForABranch(LibraryBranch branch) throws Exception{
		
		List<Book> bookList=null;
		List<BookCopies> bookCopies=null;
		bookCopies= bcopdao.getAvailableBookCopiesForABranch(branch);
		if(bookCopies!=null){
			for(BookCopies temp:bookCopies){
				if(bookList==null){
					bookList=new ArrayList<Book>();
				}
				bookList.add(bdao.read(new Integer[]{temp.getBook().getId()}));
			}
		}	
		return bookList;
	}
	
	public List<LibraryBranch> getBranchesContainingABook(Book book) throws Exception{
		List<LibraryBranch> branchList=null;
		List<BookCopies> bookCopies=null;
		bookCopies= bcopdao.getBranchesContainingABook(book);
		if(bookCopies!=null){
			for(BookCopies temp:bookCopies){
				if(branchList==null){
					branchList=new ArrayList<LibraryBranch>();
				}
				branchList.add(libdao.read(new Integer[]{temp.getBranch().getId()}));
			}
		}	
		return branchList;
	}
	
	public Borrower validateBorrower(Borrower borrower) throws Exception{
			borrower=bordao.read(new Integer[]{borrower.getCardNo()});
			if(borrower!=null){
				return borrower;
			}else{
				return null;
			}
			
	}
		
	@Transactional
	public Message performCheckOut(BookLoan bl) throws Exception{
			BookLoan blExists=null;
			blExists=bldao.read(new Integer[]{bl.getBook().getId(),bl.getBranch().getId(),bl.getBor().getCardNo()});
			if(blExists==null){
				bldao.create(bl);
			}
			else if(blExists.getDateIn()!=null){
				bldao.updateCheckOutAndDueDate(blExists);
			}else{
				return new Message(false,"You have already checked out this book");
			}
			bcopdao.updateNoOfCopiesAfterCheckOut(bl);
			return new Message(true,"Book Successfully checked out");
	}
	
	@Transactional
	public void performReturn(BookLoan bl) throws Exception{
		bldao.updateCheckInDate(bl);
		bcopdao.updateNoOfCopiesAfterReturn(bl);
	}
	
	public List<BookLoan> getCheckedOutBooks(Borrower bor) throws Exception{
		List<BookLoan> blList= bldao.getCheckedOutBooks(bor);
		if(blList!=null){
			if(!blList.isEmpty()){
				for(BookLoan bl:blList){
					bl.setBook(bdao.read(new Integer[]{bl.getBook().getId()}));
					bl.setBranch(libdao.read(new Integer[]{bl.getBranch().getId()}));
					bl.setBor(bordao.read(new Integer[]{bl.getBor().getCardNo()}));
				}
			}
		}
		return blList;
	}
	
	public List<BookLoan> getAllCheckedOutBooks() throws Exception{
		List<BookLoan> blList= bldao.getAllCheckedOutBooks();
		if(blList!=null){
			if(!blList.isEmpty()){
				for(BookLoan bl:blList){
					bl.setBook(bdao.read(new Integer[]{bl.getBook().getId()}));
					bl.setBranch(libdao.read(new Integer[]{bl.getBranch().getId()}));
					bl.setBor(bordao.read(new Integer[]{bl.getBor().getCardNo()}));
				}
			}
		}
		return blList;
	}
	
	
	public BookLoan getBookLoanInfo(BookLoan bl) throws Exception{
		return bldao.read(new Integer[]{bl.getBook().getId(),bl.getBranch().getId(),bl.getBor().getCardNo()});
	}
	
		
		
}
