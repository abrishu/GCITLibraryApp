package com.gcitsolutions.libraryapp.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gcitsolutions.libraryapp.Data.AuthorDAO;
import com.gcitsolutions.libraryapp.Data.BookCopiesDAO;
import com.gcitsolutions.libraryapp.Data.BookDAO;
import com.gcitsolutions.libraryapp.Data.BookLoanDAO;
import com.gcitsolutions.libraryapp.Data.BorrowerDAO;
import com.gcitsolutions.libraryapp.Data.ConnectionUtils;
import com.gcitsolutions.libraryapp.Data.GenreDAO;
import com.gcitsolutions.libraryapp.Data.LibraryBranchDAO;
import com.gcitsolutions.libraryapp.Data.PublisherDAO;
import com.gcitsolutions.libraryapp.Entity.Author;
import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.BookLoan;
import com.gcitsolutions.libraryapp.Entity.Genre;
import com.gcitsolutions.libraryapp.Entity.LibraryBranch;
import com.gcitsolutions.libraryapp.Entity.Message;
import com.gcitsolutions.libraryapp.Entity.Publisher;
import com.gcitsolutions.libraryapp.LibraryException.LibraryException;

public class Administrator {

	private static Administrator adminInstance=null;
	
	
	private Administrator() {
	}
	
	public static Administrator getInstance(){
		if(adminInstance==null){
			synchronized(Administrator.class){
				if(adminInstance==null){
					return new Administrator();
				}
			}
		}
		return adminInstance;
	}
	
	
	//List of Create Objects for Administrator
	
	public  void addBook(Book book) throws Exception{
		Connection conn=null;
		
		//Business Validations
		if(book==null || book.getTitle()==null || book.getTitle().trim().length()==0){
			throw new LibraryException("The book title cannot be null or blank");
		}else if(book.getTitle().length()>45){
			throw new LibraryException("The book title cannot be more than 45 characters");
		}
		
		//Data Access Actions
		try{
			conn=ConnectionUtils.getConnection();
			BookDAO bdao=new BookDAO(conn);
			bdao.create(book);
			conn.commit();
		}catch(Exception ex){
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	public void addAuthor(Author author) throws Exception{
		Connection conn=null;
		AuthorDAO adao=null;
		
		//Business Validations
		if(author==null || author.getName()==null || author.getName().trim().length()==0){
			throw new LibraryException("The Author name cannot be null or blank");
		}else if(author.getName().length()>45){
			throw new LibraryException("The Author name cannot be more than 45 characters");
		}
		
		//Data Access Actions
		try{
			conn=ConnectionUtils.getConnection();
			adao=new AuthorDAO(conn);
			adao.create(author);
			conn.commit();
		}catch(Exception ex){
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}

	public void addPublisher(Publisher pub) throws Exception{
		Connection conn=null;
		PublisherDAO pubdao=null;
		try{
			conn=ConnectionUtils.getConnection();
			pubdao=new PublisherDAO(conn);
			pubdao.create(pub);
			conn.commit();
		}catch(Exception ex){
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	public void addLibraryBranch(LibraryBranch branch) throws Exception{
		Connection conn=null;
		LibraryBranchDAO libdao=null;
		try{
			conn=ConnectionUtils.getConnection();
			libdao=new LibraryBranchDAO(conn);
			libdao.create(branch);
			conn.commit();
		}catch(Exception ex){
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	
	public Message deleteBook(Book book) throws Exception{
		Message message=new Message();
		String strMessage="Deleted successfully";
		Connection conn=null;
		try{
			conn=ConnectionUtils.getConnection();
			BookDAO bdao=new BookDAO(conn);
			BookCopiesDAO bcopdao=new BookCopiesDAO(conn);
			if(bdao.read(new Integer[]{book.getId()})==null){
				message.setSuccess(false);
				message.setMessage("The Book does not exist"); 
			}else{
				if(bcopdao.checkIfBookCheckedOutOrExistsInABranch(book)){
					message.setSuccess(false);
					message.setMessage("The Book you are trying to delete still has copies available in library branche(s) or have active checkouts"); 
				}else{
					bdao.delete(book);
					message.setMessage(strMessage);
				}
			}
			//Check if the book has been checked out
			
			conn.commit();
		}catch(Exception ex){
			conn.rollback();
			strMessage="Delete Operation failed:"+ex.getMessage();
		}
		return message;
		
	}
	
	//Adding Borrower
	
	public void addBorrowerEntity(com.gcitsolutions.libraryapp.Entity.Borrower borrower) throws Exception{
		
		//Business Validations
		if(borrower==null || borrower.getName()==null || borrower.getName().trim().length()==0){
			throw new LibraryException("The book title cannot be null or blank");
		}else if(borrower.getName().length()>45){
			throw new LibraryException("The book title cannot be more than 45 characters");
		}
				
		Connection conn=null;
		BorrowerDAO bordao=null;
		try{
			conn=ConnectionUtils.getConnection();
			bordao=new BorrowerDAO(conn);
			bordao.create(borrower);
			conn.commit();
		}catch(Exception ex){
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	
	//Adding Book Genre
	
	public void addGenre(Genre genre) throws Exception{
		Connection conn=null;
		GenreDAO genredao=null;
		try{
			conn=ConnectionUtils.getConnection();
			genredao=new GenreDAO(conn);
			genredao.create(genre);
			conn.commit();
		}catch(Exception ex){
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	
	
	//Adding Book Loan

	public void addBookLoan(BookLoan bloan) throws Exception{
		if(bloan!=null){
			if(bloan.getBook()==null || bloan.getBook().getId()==0){
				throw new LibraryException("Book information needs to be setup");
			}else if(bloan.getBranch()==null || bloan.getBranch().getId()==0){
				throw new LibraryException("Branch information needs to be setup");
			}else if(bloan.getBor()==null || bloan.getBor().getCardNo()==0){
				throw new LibraryException("Borrower information needs to be setup");
			}
		}else{
			throw new LibraryException("Book, Branch and Card Number need to be setup");
		}
		
		Connection conn=null;
		BookLoanDAO bloanDAO=null;
		try{
			conn=ConnectionUtils.getConnection();
			bloanDAO=new BookLoanDAO(conn);
			bloanDAO.create(bloan);
			
			conn.commit();
		}catch(Exception ex){
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	
	public List<Author> getAllAuthors(Integer pageNo,Integer pageSize,String searchString) throws LibraryException, SQLException{
		Connection conn=null;
		AuthorDAO adao=null;
		try{
			conn=ConnectionUtils.getConnection();
			adao=new AuthorDAO(conn);
			return adao.readAll(pageNo,pageSize, searchString);
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
	
	public List<Author> getAllAuthors() throws LibraryException, SQLException{
		Connection conn=null;
		AuthorDAO adao=null;
		try{
			conn=ConnectionUtils.getConnection();
			adao=new AuthorDAO(conn);
			return adao.readAll();
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
	
	public List<Book> getAllBooks() throws LibraryException, SQLException{
		Connection conn=null;
		BookDAO bdao=null;
		try{
			conn=ConnectionUtils.getConnection();
			bdao=new BookDAO(conn);
			return bdao.readAll();
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
	
	public List<Publisher> getAllPublishers() throws SQLException,LibraryException{
		Connection conn=null;
		PublisherDAO pubdao=null;
		try{
			conn=ConnectionUtils.getConnection();
			pubdao=new PublisherDAO(conn);
			return pubdao.readAll();
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
	
	//Updating Book Information
	
	public void updateBookInfo(Book book) throws Exception{
		Connection conn=null;
		BookDAO bdao=null;
		try{
			conn=ConnectionUtils.getConnection();
			bdao=new BookDAO(conn);
			bdao.update(book);
			conn.commit();
		}catch(Exception ex){
			conn.rollback();
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	public Book getPublisherForABook(Book book)throws Exception{
		Connection conn=null;
		Publisher pub=null;
		try{
			conn=ConnectionUtils.getConnection();
			PublisherDAO pubdao=new PublisherDAO(conn);
			book.setPublisher(pubdao.getPublisherForABook(book));
		}catch(Exception ex){
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
		return book;
	}
	
	public Book getAuthorsForABook(Book book) throws LibraryException, SQLException{
		Connection conn=null;
		Author aut=null;
		try{
			conn=ConnectionUtils.getConnection();
			AuthorDAO autdao=new AuthorDAO(conn);
			book.setAuthorList(autdao.getAuthorsForABook(book));
		}catch(Exception ex){
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
		return book;
	}
	
	
	public Book getBookInfo(Book book)throws Exception{
		Connection conn=null;
		BookDAO bdao=null;
		
		try{
			conn=ConnectionUtils.getConnection();
			bdao=new BookDAO(conn);
			book = bdao.read(new Integer[]{book.getId()});
			PublisherDAO pub=new PublisherDAO(conn);
			book.setPublisher(pub.getPublisherForABook(book));
			AuthorDAO adao=new AuthorDAO(conn);
			book.setAuthorList(adao.getAuthorsForABook(book));
		}catch(Exception ex){
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
		return book;
	}
	
	public List<LibraryBranch> getAllLibraryBranch()throws Exception{
		Connection conn=null;
		LibraryBranchDAO lbdao=null;
		try{
			conn=ConnectionUtils.getConnection();
			lbdao=new LibraryBranchDAO(conn);
			return lbdao.readAll();
		}catch(Exception ex){
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	public List<LibraryBranch> getAllLibraryBranch(Integer pageNo,Integer pageSize,String searchString)throws Exception{
		Connection conn=null;
		LibraryBranchDAO lbdao=null;
		try{
			conn=ConnectionUtils.getConnection();
			lbdao=new LibraryBranchDAO(conn);
			return lbdao.readAll(pageNo,pageSize,searchString);
		}catch(Exception ex){
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	public LibraryBranch getBranchInfo(LibraryBranch branch)throws Exception{
		Connection conn=null;
		LibraryBranchDAO lbdao=null;
		LibraryBranch br=new LibraryBranch(branch.getId());
		try{
			conn=ConnectionUtils.getConnection();
			lbdao=new LibraryBranchDAO(conn);
			return lbdao.read(new Integer[]{branch.getId()});
		}catch(Exception ex){
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	public Publisher getPublisherInfo(Publisher pub)throws Exception{
		Connection conn=null;
		PublisherDAO pubdao=null;
		try{
			conn=ConnectionUtils.getConnection();
			pubdao=new PublisherDAO(conn);
			pub=pubdao.read(new Integer[]{pub.getId()});
			return pub;
		}catch(Exception ex){
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	
	public void updateBranchInfo(LibraryBranch branch)throws Exception{
		Connection conn=null;
		LibraryBranchDAO lbdao=null;
		try{
			conn=ConnectionUtils.getConnection();
			lbdao=new LibraryBranchDAO(conn);
			lbdao.update(branch);
			conn.commit();
		}catch(Exception ex){
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	public void updateAuthor(Author author)throws Exception{
		Connection conn=null;
		AuthorDAO dao=null;
		try{
			conn=ConnectionUtils.getConnection();
			dao=new AuthorDAO(conn);
			dao.update(author);
			conn.commit();
		}catch(Exception ex){
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	public void updatePublisher(Publisher pub)throws Exception{
		Connection conn=null;
		PublisherDAO pubdao=null;
		try{
			conn=ConnectionUtils.getConnection();
			pubdao=new PublisherDAO(conn);
			pubdao.update(pub);
			conn.commit();
		}catch(Exception ex){
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	public void overrideBookLoan(BookLoan bl) throws Exception{
		Connection conn=null;
		BookLoanDAO bldao=null;
		try{
			conn=ConnectionUtils.getConnection();
			bldao=new BookLoanDAO(conn);
			bldao.updateDueDate(bl);
			conn.commit();
		}catch(Exception ex){
			throw new LibraryException("The transaction failed. Failure Details:"+ex.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	
	public Author getAuthorById(Author author) throws Exception{
		Connection conn=null;
		try{
			conn=ConnectionUtils.getConnection();
			AuthorDAO adao=new AuthorDAO(conn);
			author= adao.read(new Integer[]{author.getId()});
		}
		finally{
			conn.close();
		}
		return author;
	}
	
	
	public Integer getCountOfAuthors(String searchString) throws Exception{
		Connection conn=null;
		try{
			conn=ConnectionUtils.getConnection();
			AuthorDAO adao=new AuthorDAO(conn);
			return adao.getCountOfAuthors(searchString);
		}
		finally{
			conn.close();
		}
	}

	public Integer getCountOfBooks(String searchString) throws Exception {
		// TODO Auto-generated method stub
		Connection conn=null;
		try{
			conn=ConnectionUtils.getConnection();
			BookDAO bdao=new BookDAO(conn);
			return bdao.getCountOfBooks(searchString);
		}
		finally{
			conn.close();
		}
	}

	public List<Book> getAllBooks(Integer pageNo, Integer pageSize, String searchString) throws LibraryException,SQLException {
		// TODO Auto-generated method stub
		Connection conn=null;
		BookDAO bdao=null;
		try{
			conn=ConnectionUtils.getConnection();
			bdao=new BookDAO(conn);
			return bdao.readAll(pageNo,pageSize, searchString);
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
	

}
