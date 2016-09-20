package com.gcitsolutions.libraryapp.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcitsolutions.libraryapp.Entity.Author;
import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.Genre;
import com.gcitsolutions.libraryapp.Entity.Publisher;

public class BookDAO extends BaseDAO<Book> {

	public BookDAO(Connection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	
	//Creating book with Author
	@Override
	public void create(Book book) throws SQLException {
		List<Author> authList=book.getAuthorList();
		List<Genre> genreList=book.getGenresList();
		String bookSql="insert into tbl_book(title,pubId) values (?,?)";
		int bookIdCreated=saveAndGetId(bookSql,new Object[] {book.getTitle(),book.getPublisher().getId()});
		book.setId(bookIdCreated);
		
		//Book Author Mapping
		if(authList!=null){
			if(authList.size()>0){
				for(Author auth:authList){
					String sql="insert into tbl_book_authors(bookId,authorId) values (?,?)";
					save(sql,new Object[] {book.getId(),auth.getId()});
				}
			}
		}
		//Book-Genre Mapping
		if(genreList!=null){
			if(genreList.size()>0){
				for(Genre genre:genreList){
					String sql="insert into tbl_book_genres(genre_id,book_id) values (?,?)";
					save(sql,new Object[] {genre.getId(),book.getId()});
				}
			}
		}
		
	}
	
	
	@Override
	public void update(Book book) throws SQLException {
		
		String sql="update tbl_book t set t.title=?,t.pubId=? where t.bookId=?";
		save(sql,new Object[] {book.getTitle(),book.getPublisher().getId(),book.getId()});
		updateBookAuthorMappings(book);
		if(book.getGenresList()!=null){
			updateBookGenreMappings(book);
		}
		
	}

	@Override
	public void delete(Book book) throws SQLException {
		String sql="delete from tbl_book where bookId=?";
		save(sql,new Object[] {book.getId()});	
	}

	@Override
	public Book read(Integer[] pk) throws SQLException {
		// TODO Auto-generated method stub
	/*String sql="select t.*,pub.publisherName,ta.authorId,ta.authorName from tbl_book t"
				+" left join tbl_book_authors tba on t.bookId=tba.bookId "
				+" left join tbl_author ta on ta.authorId=tba.authorId"
				+" left join tbl_Publisher pub on pub.publisherId=t.pubId where t.bookId=?";*/
		
		String sql="select * from tbl_book where bookId=?";
		return read(sql, pk);
	}

	@Override
	public List<Book> readAll() throws SQLException {
		// TODO Auto-generated method stub
		String sql="select * from tbl_book";
		/*
		String sql="select t.*,pub.publisherName,ta.authorId,ta.authorName from tbl_book t"
		+" inner join tbl_book_authors tba on t.bookId=tba.bookId "
		+" inner join tbl_author ta on ta.authorId=tba.authorId"
		+" inner join tbl_Publisher pub on pub.publisherId=t.pubId";*/
		
		return readAll(sql,new Integer[]{});
	}
	
	public List<Book> readAll(Integer pageNo,Integer pageSize,String searchString) throws SQLException {
		// TODO Auto-generated method stub
		setPageNo(pageNo);
		setPageSize(pageSize);
		String sql="Select * from tbl_book t";
		if(searchString!=null && !searchString.equals("")){
			searchString="%"+searchString+"%";
			sql=sql+" where t.title like ?";
			return readAll(sql,new String[] {searchString});
		}else{
			return readAll(sql,new Integer[] {});
		}
		
	}
	
	public void updateBookAuthorMappings(Book book) throws SQLException{
		List<Author> originalValues=getAuthorsForABook(book);
		List<Author> newSelections=book.getAuthorList();
		//Inserting new authors into tbl Book Authors
		if(newSelections!=null){
			for(Author auth:newSelections){
				if(originalValues==null || !originalValues.contains(auth)){
					save("insert into tbl_book_authors(bookId,authorId) values(?,?)",new Integer[]{book.getId(),auth.getId()});
				}else{
					originalValues.remove(auth);
				}
			}
		}
		//Delete old entries from tbl Book Authors that are not selected
		if(originalValues!=null){
			for(Author auth:originalValues){
				save("delete from tbl_book_authors where bookId=? and authorId=?",new Integer[]{book.getId(),auth.getId()});
			}
		}
	}
	
	public void updateBookGenreMappings(Book book) throws SQLException{
		List<Genre> originalValues=getGenresForABook(book);
		List<Genre> newSelections=book.getGenresList();
		for(Genre genre:newSelections){
			if( originalValues==null || !originalValues.contains(genre)){
				save("insert into tbl_book_genres(bookId,genre_id) values(?,?)",new Integer[]{book.getId(),genre.getId()});
			}else{
				originalValues.remove(genre);
			}
		}
		
		if(originalValues!=null){
			for(Genre genre:originalValues){
				save("delete from tbl_book_genres where bookId=? and genre_id=?",new Integer[]{book.getId(),genre.getId()});
			}
		}
	}

	public List<Genre> getGenresForABook(Book book) throws SQLException{
		String sql="select tg.genre_id,tg.genre_name from  tbl_book_genres tbg inner join tbl_genre tg on tbg.genre_id=tg.genre_id where tbg.bookId=?";
		Connection conn=ConnectionUtils.getConnection();
		try{
			GenreDAO gdao=new GenreDAO(conn);
			return gdao.readAll(sql, new Integer[]{book.getId()});
		}finally{
			conn.close();
		}
		
	}
	
	public List<Author> getAuthorsForABook(Book book) throws SQLException{
		String sql="select ta.authorId,ta.authorName from  tbl_book_authors tba inner join tbl_author ta on ta.authorId=tba.authorId where tba.bookId=?";
		Connection conn=ConnectionUtils.getConnection();
		try{
			AuthorDAO adao=new AuthorDAO(conn);
			return adao.readAll(sql, new Integer[]{book.getId()});
		}finally{
			conn.close();
		}
		
	}
	
	
	@Override
	public List<Book> readResult(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<Book> bookList=null;
		Book book=null;
		Connection conn=ConnectionUtils.getConnection();
		try{
			while(rs.next()){
				if(bookList==null){
					bookList=new ArrayList<Book>();
				}
				book=new Book();
				book.setId(rs.getInt("bookId"));
				book.setTitle(rs.getString("title"));
			
				PublisherDAO pubDAO=new PublisherDAO(conn);
				Publisher pub=pubDAO.read(new Integer[]{rs.getInt("pubId")});
				if(pub!=null){
					book.setPublisher(pub);	
				}
				List<Author> authorList=getAuthorsForABook(book);
				if(authorList!=null){
					book.setAuthorList(authorList);
				}
				bookList.add(book);
			}
			return bookList;
		}finally{
			conn.close();
		}
		
	}


	public Integer getCountOfBooks(String searchString) throws SQLException  {
		// TODO Auto-generated method stub
		String sql="Select count(*) from tbl_book t";
		if(searchString!=null && !searchString.equals("")){
			sql+=" where t.title like ?";
			return getCount(sql,new String[] {"%"+searchString+"%"});
		}
		return getCount(sql,new Integer[] {});

	}
	

}
