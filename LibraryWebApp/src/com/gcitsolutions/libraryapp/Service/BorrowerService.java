package com.gcitsolutions.libraryapp.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.gcitsolutions.libraryapp.Data.BookCopiesDAO;
import com.gcitsolutions.libraryapp.Data.BookLoanDAO;
import com.gcitsolutions.libraryapp.Data.BorrowerDAO;
import com.gcitsolutions.libraryapp.Data.ConnectionUtils;
import com.gcitsolutions.libraryapp.Data.LibraryBranchDAO;
import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.BookCopies;
import com.gcitsolutions.libraryapp.Entity.BookLoan;
import com.gcitsolutions.libraryapp.Entity.Borrower;
import com.gcitsolutions.libraryapp.Entity.LibraryBranch;
import com.gcitsolutions.libraryapp.LibraryException.LibraryException;

public class BorrowerService {

private static BorrowerService borrowerInstance=null;
	
	
	private BorrowerService() {
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
		Connection conn=null;
		List<LibraryBranch> libList=null;
		try{
			conn=ConnectionUtils.getConnection();
			LibraryBranchDAO libdao=new LibraryBranchDAO(conn);
			libList=libdao.readAll();
		}catch(Exception ex){
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
		return libList;
	}
	
	
	public List<Book> getAvailableBooksForABranch(LibraryBranch branch) throws Exception{
		
		Connection conn=null;
		List<Book> bookList=null;
		List<BookCopies> bookcopies=null;
		//Data Access Actions
		try{
			conn=ConnectionUtils.getConnection();
			BookCopiesDAO bcopdao=new BookCopiesDAO(conn);
			bookcopies= bcopdao.readAll("select * from tbl_book_copies t where t.branchId=? and t.noOfCopies>0 ", new Integer[]{branch.getId()});
			if(bookcopies!=null){
				for(BookCopies temp:bookcopies){
					if(bookList==null){
						bookList=new ArrayList<Book>();
					}
					bookList.add(temp.getBook());
				}
			}	
		}catch(Exception ex){
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
		return bookList;
	}
	
	public Borrower validateBorrower(Borrower borrower) throws Exception{
		Connection conn=null;
		//Business Validations
		if(borrower==null || borrower.getCardNo()==0){
			throw new LibraryException("Please enter a valid card number");
		}
		//Data Access Actions
		try{
			conn=ConnectionUtils.getConnection();
			BorrowerDAO bdao=new BorrowerDAO(conn);
			borrower=bdao.read(new Integer[]{borrower.getCardNo()});
		}catch(Exception ex){
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
		return borrower;
	}
		
	
	public void performCheckOut(BookLoan bl) throws Exception{
		
		Connection conn=null;
		//Data Access Actions
		try{
			conn=ConnectionUtils.getConnection();
			BookLoanDAO bldao=new BookLoanDAO(conn);
			BookLoan blExists=null;
			blExists=bldao.read(new Integer[]{bl.getBook().getId(),bl.getBranch().getId(),bl.getBor().getCardNo()});
			if(blExists==null){
				bldao.create(bl);
			}
			else if(blExists.getDateIn()!=null){
				bldao.updateCheckOutAndDueDate(blExists);
			}else{
				conn.rollback();
				throw new LibraryException("You have already checked out this book");
			}
			
			BookCopiesDAO bcopdao=new BookCopiesDAO(conn);
			bcopdao.updateNoOfCopiesAfterCheckOut(bl);
			conn.commit();
			}
		catch(Exception ex){
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	public boolean performReturn(BookLoan bl) throws Exception{
		
		Connection conn=null;
		boolean success=false;
		//Data Access Actions
		try{
				conn=ConnectionUtils.getConnection();
				BookLoanDAO bldao=new BookLoanDAO(conn);
				bldao.updateCheckInDate(bl);
				BookCopiesDAO bcopdao=new BookCopiesDAO(conn);
				bcopdao.updateNoOfCopiesAfterReturn(bl);
				conn.commit();
				success=true;
			}
		catch(Exception ex){
			success=false;
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
		return success;
	}
	
	
	public List<BookLoan> getCheckedOutBooks(Borrower bor) throws Exception{
		Connection conn=null;
		BookLoanDAO bldao=null;
		List<BookLoan> loanList=null;
		//Data Access Actions
		try{
			conn=ConnectionUtils.getConnection();
			bldao=new BookLoanDAO(conn);
			loanList=bldao.readAll("Select * from tbl_book_loans t where t.cardNo=? and t.dateIn is null", new Integer[]{bor.getCardNo()});
		}
		catch(Exception ex){
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
		return loanList;
	}
	
		
		
}
