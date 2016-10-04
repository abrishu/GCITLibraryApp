package com.gcitsolutions.libraryapp.Service;

import java.sql.SQLException;
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
import com.gcitsolutions.libraryapp.Entity.Author;
import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.BookLoan;
import com.gcitsolutions.libraryapp.Entity.Borrower;
import com.gcitsolutions.libraryapp.Entity.Genre;
import com.gcitsolutions.libraryapp.Entity.LibraryBranch;
import com.gcitsolutions.libraryapp.Entity.Message;
import com.gcitsolutions.libraryapp.Entity.Publisher;
import com.gcitsolutions.libraryapp.LibraryException.LibraryException;

public class Administrator {

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
	
	private static Administrator adminInstance=null;
	
	
	public Administrator() {
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
	
	@Transactional
	public  void addBook(Book book) throws Exception{
			Integer newCreatedBookId=bdao.create(book);
			book.setId(newCreatedBookId);
			if(book.getAuthorList()!=null && book.getAuthorList().size()>0){
					for(Author auth:book.getAuthorList()){
						bdao.createBookAuthor(book, new Author(auth.getId()));
					}
			}
			if(book.getGenresList()!=null){
				for(Genre genre:book.getGenresList()){
					bdao.createBookGenres(book, new Genre(genre.getId()));
				}
			}
	}
	@Transactional
	public void addAuthor(Author author) throws Exception{
			adao.create(author);
	}
	
	public void addAuthorMongo(Author author) throws Exception{
		adao.createMongo(author);
	}
	
	@Transactional
	public void addPublisher(Publisher pub) throws Exception{
			pubdao.create(pub);
	}
	@Transactional
	public void addLibraryBranch(LibraryBranch branch) throws Exception{
			libdao.create(branch);
	}
	
	@Transactional
	public Message deleteBook(Book book) throws Exception{

			if(bdao.read(new Integer[]{book.getId()})==null){
				message.setSuccess(false);
				message.setMessage("The Book does not exist"); 
			}else{
				if(bcopdao.checkIfBookCheckedOutOrExistsInABranch(book)){
					message.setSuccess(false);
					message.setMessage("The Book you are trying to delete still has copies available in library branche(s) or have active checkouts"); 
				}else{
					bdao.delete(book);
					message.setSuccess(true);
					message.setMessage("Book Deleted Successfully");
				}
			}
		return message;
		
	}
	
	@Transactional
	public Message deleteAuthor(Author author) throws Exception{
				author=adao.read(new Integer[]{author.getId()});
				if(author==null){
					message.setSuccess(false);
					message.setMessage("The Author does not exist"); 
				}else{
						adao.delete(author);
						message.setSuccess(true);
						message.setMessage("Author '" + author.getName() +"' has been successfully deleted."); 
				}
				return message;	
	}
	
	@Transactional
	public Message deletePublisher(Publisher pub) throws Exception{

				if(pubdao.read(new Integer[]{pub.getId()})==null){
					message.setSuccess(false);
					message.setMessage("The Publisher does not exist"); 
				}else{
						pubdao.delete(pub);
						message.setSuccess(true);
						message.setMessage("Publisher has been successfully deleted."); 
				}
				return message;	
	}
		
	
	//Adding Borrower
	
	public void addBorrowerEntity(com.gcitsolutions.libraryapp.Entity.Borrower borrower) throws Exception{
			bordao.create(borrower);
	}
	
	
	//Adding Book Genre
	
	public void addGenre(Genre genre) throws Exception{
			genredao.create(genre);
	}
	
	
	
	//Adding Book Loan

	public void addBookLoan(BookLoan bloan) throws Exception{
			bldao.create(bloan);
	}
	
	public List<Author> getAllAuthors(Integer pageNo,Integer pageSize,String searchString) throws LibraryException, SQLException{
			return adao.readAll(pageNo,pageSize, searchString);
	}
	
	public List<Publisher> getAllPublishers(Integer pageNo,Integer pageSize,String searchString) throws LibraryException, SQLException{
		return pubdao.readAll(pageNo,pageSize, searchString);
	}

	
	public List<Author> getAllAuthors() throws LibraryException, SQLException{
			return adao.readAll();
	}
	
	public List<Book> getAllBooks() throws LibraryException, SQLException{
			List<Book> bookList=bdao.readAll();
			List<Author> authorList=null;
			List<Genre> genresList=null;
			if(bookList!=null){
				//Populate Author and Publisher Details
				for(Book book:bookList){
					authorList=adao.getAuthorsForABook(book);
					genresList=genredao.getGenresForABook(book);
					book.setGenresList(genresList);
					book.setAuthorList(authorList);
					book.setPublisher(pubdao.getPublisherForABook(book));
				}
			}
			return bookList;
	}
	
	public List<Publisher> getAllPublishers() throws SQLException,LibraryException{
			return pubdao.readAll();
	}
	
	//Updating Book Information
	
	public void updateBookInfo(Book book) throws Exception{
		//Update Title and Publisher
			if(book.getPublisher()==null){
				book.setPublisher(new Publisher(null));
			}
			bdao.update(book);
		//Update Authors
			List<Author> authorList=adao.getAuthorsForABook(book);
			bdao.updateBookAuthorMappings(book, authorList);
		//Update Genres
			List<Genre> genreList=genredao.getGenresForABook(book);
			bdao.updateBookGenreMappings(book, genreList);
	}
	
	public Book getPublisherForABook(Book book)throws Exception{
		book.setPublisher(pubdao.getPublisherForABook(book));
		return book;
	}
	
	public Book getAuthorsForABook(Book book) throws LibraryException, SQLException{
			book.setAuthorList(adao.getAuthorsForABook(book));
		return book;
	}
	
	
	public Book getBookInfo(Book book)throws Exception{
			book = bdao.read(new Integer[]{book.getId()});
			book.setPublisher(pubdao.getPublisherForABook(book));
			book.setAuthorList(adao.getAuthorsForABook(book));
			book.setGenresList(genredao.getGenresForABook(book));
			return book;
	}
	
	public List<LibraryBranch> getAllLibraryBranch()throws Exception{
			return libdao.readAll();
	}
	
	public List<LibraryBranch> getAllLibraryBranch(Integer pageNo,Integer pageSize,String searchString)throws Exception{
			return libdao.readAll(pageNo,pageSize,searchString);
	}
	
	public LibraryBranch getBranchInfo(LibraryBranch branch)throws Exception{
			return libdao.read(new Integer[]{branch.getId()});
	}
	
	public Publisher getPublisherInfo(Publisher pub)throws Exception{
			return pubdao.read(new Integer[]{pub.getId()});
	}
	
	
	public void updateBranchInfo(LibraryBranch branch)throws Exception{
			libdao.update(branch);
	}
	
	public void updateAuthor(Author author)throws Exception{
			adao.update(author);
	}
	
	public void updatePublisher(Publisher pub)throws Exception{
			pubdao.update(pub);
	}
	
	public void overrideBookLoan(BookLoan bl) throws Exception{
			bldao.updateDueDate(bl);
	}
	
	
	public Author getAuthorById(Author author) throws Exception{
			return adao.read(new Integer[]{author.getId()});
	}
	
	public Genre getGenreById(Genre genre) throws Exception{
		return genredao.read(new Integer[]{genre.getId()});
	}

	public Integer getCountOfAuthors(String searchString) throws Exception{
			System.out.println(adao.getCountOfAuthors(""));
			return adao.getCountOfAuthors(searchString);
	}
	
	public Integer getCountOfPublishers(String searchString) throws Exception{
		return pubdao.getCountOfPublishers(searchString);
	}

	public Integer getCountOfBooks(String searchString) throws Exception {
			return bdao.getCountOfBooks(searchString);
	}
	
	public Integer getCountOfgenres(String searchString) throws Exception {
			return genredao.getCountOfGenres(searchString);
	}

	public List<Book> getAllBooks(Integer pageNo, Integer pageSize, String searchString) throws LibraryException,SQLException {
		List<Book> bookList=  bdao.readAll(pageNo,pageSize, searchString);
		List<Author> authorList=null;
		List<Genre> genresList=null;
		if(bookList!=null){
			//Populate Author and Publisher Details
			for(Book book:bookList){
				authorList=adao.getAuthorsForABook(book);
				genresList=genredao.getGenresForABook(book);
				book.setGenresList(genresList);
				book.setAuthorList(authorList);
				book.setPublisher(pubdao.getPublisherForABook(book));
			}
		}
		return bookList;
	}

	public List<Genre> getAllgenres(Integer pageNo, int pageSize, String searchString) throws SQLException {
		// TODO Auto-generated method stub
		return genredao.readAll(pageNo, pageSize, searchString);
	}

	public Message deleteGenre(Genre genre) throws SQLException {
		if(genredao.read(new Integer[]{genre.getId()})==null){
			message.setSuccess(false);
			message.setMessage("The Genre does not exist"); 
		}else{
				genredao.delete(genre);
				message.setSuccess(true);
				message.setMessage("Genre has been successfully deleted."); 
		}
		return message;		}

	public void updateGenre(Genre genre) throws SQLException {
		genredao.update(genre);
	}

	public List<Genre> getAllGenres() throws SQLException {
		// TODO Auto-generated method stub
		return genredao.readAll();
	}

	public void updateBorrower(Borrower borrower) throws SQLException {
		bordao.update(borrower);
	}

	
	

}
