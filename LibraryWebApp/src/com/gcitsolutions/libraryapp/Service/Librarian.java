package com.gcitsolutions.libraryapp.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gcitsolutions.libraryapp.Data.AuthorDAO;
import com.gcitsolutions.libraryapp.Data.BookCopiesDAO;
import com.gcitsolutions.libraryapp.Data.BookDAO;
import com.gcitsolutions.libraryapp.Data.ConnectionUtils;
import com.gcitsolutions.libraryapp.Data.LibraryBranchDAO;
import com.gcitsolutions.libraryapp.Entity.BookCopies;
import com.gcitsolutions.libraryapp.Entity.LibraryBranch;
import com.gcitsolutions.libraryapp.LibraryException.LibraryException;

public class Librarian {

private static Librarian libInstance=null;
	
	
	private Librarian() {
	}
	
	public static Librarian getInstance(){
		if(libInstance==null){
			synchronized(Librarian.class){
				if(libInstance==null){
					return new Librarian();
				}
			}
		}
		return libInstance;
	}
	
	public List<BookCopies> getAllBookCopies(Integer pageNo,Integer pageSize,String searchStringBranch,String searchStringBook) throws Exception{
		
		Connection conn=null;
		BookCopiesDAO bcopdao=null;
		try{
			conn=ConnectionUtils.getConnection();
			bcopdao=new BookCopiesDAO(conn);
			return bcopdao.readAll(pageNo,pageSize, searchStringBranch,searchStringBook);
		}
		catch(Exception ex){
			throw new LibraryException(ex.getMessage());
		}
		finally{
			if(conn!=null){
				conn.close();	
			}
			
		}
	}

	
	
	public void updateLibraryBranch(LibraryBranch branch) throws Exception{
		Connection conn=null;
		//Business Validations
		if(branch==null || branch.getName()==null || branch.getName().trim().length()==0){
			throw new LibraryException("The branch cannot be null or blank");
		}else if(branch.getName().length()>45){
			throw new LibraryException("The branch cannot be more than 45 characters");
		}
		//Data Access Actions
		try{
			conn=ConnectionUtils.getConnection();
			LibraryBranchDAO branchdao=new LibraryBranchDAO(conn);
			branchdao.update(branch);
			conn.commit();
		}catch(SQLException ex){
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	public BookCopies getBookCopiesForABook(BookCopies bcop) throws Exception {
		Connection conn=null;
		try{
			conn=ConnectionUtils.getConnection();
			BookCopiesDAO bcopdao=new BookCopiesDAO(conn);
			bcop=bcopdao.read(new Integer[]{bcop.getBook().getId(),bcop.getBranch().getId()});
		}catch(SQLException ex){
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		return bcop;
	}
	
	public void addBookCopiesToBranch(BookCopies bcop) throws Exception{
		Connection conn=null;
		
		//Business Validations
		if(bcop==null || bcop.getBranch()==null){
			throw new LibraryException("The branch cannot be null or blank");
		}else if(bcop.getBook()==null){
			throw new LibraryException("The book cannot be blank");
		}
		
		//Data Access Actions
		
		try{
			conn=ConnectionUtils.getConnection();
			BookCopiesDAO bcopdao=new BookCopiesDAO(conn);
			if(bcopdao.read(new Integer[]{bcop.getBook().getId(),bcop.getBranch().getId()})!=null){
				bcopdao.update(bcop);
			}else{
				bcopdao.create(bcop);
			}	
			conn.commit();
		}catch(SQLException ex){
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	public Integer getCountOfBookCopies(String searchStringBranch,String searchStringBook) throws Exception {
		// TODO Auto-generated method stub
		Connection conn=null;
		try{
			conn=ConnectionUtils.getConnection();
			BookCopiesDAO bcopdao=new BookCopiesDAO(conn);
			return bcopdao.getCountOfBookCopies(searchStringBranch, searchStringBook);
		}
		finally{
			conn.close();
		}
	}
	
	

}
