package com.gcitsolutions.libraryapp.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcitsolutions.libraryapp.Entity.Author;
import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.Genre;
import com.gcitsolutions.libraryapp.Entity.Publisher;

public class BookDAO extends BaseDAO<Book> {
	
	//Creating book with Author
	public Integer create(Book book) throws SQLException {
		final String bookTitle=book.getTitle();
		Publisher pub=book.getPublisher();
		Integer pubId=null;
		if(pub!=null){
			pubId= pub.getId();
		}
		final Integer publisherId=pubId;
		final String bookSql="insert into tbl_book(title,pubId) values (?,?)";
		KeyHolder keyHolder=new GeneratedKeyHolder();
		template.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt=connection.prepareStatement(bookSql, new String[]{"bookId"});
				pstmt.setString(1, bookTitle);
				pstmt.setObject(2, publisherId);
				return pstmt;
			}
		},keyHolder);
		
		Integer bookIdCreated=keyHolder.getKey().intValue();
		return bookIdCreated;
	}
	
	public void createBookAuthor(Book book,Author auth){
		String sql="insert into tbl_book_authors(bookId,authorId) values (?,?)";
		template.update(sql,new Object[] {book.getId(),auth.getId()});
	}
	
	public void createBookGenres(Book book,Genre genre){
		String sql="insert into tbl_book_genres(bookId,genre_id) values (?,?)";
		template.update(sql,new Object[] {book.getId(),genre.getId()});
	}
	

	public void update(Book book) throws SQLException {
		
		String sql="update tbl_book t set t.title=?,t.pubId=? where t.bookId=?";
		template.update(sql,new Object[] {book.getTitle(),book.getPublisher().getId(),book.getId()});	
	}

	public void delete(Book book) throws SQLException {
		String sql="delete from tbl_book where bookId=?";
		template.update(sql,new Object[] {book.getId()});	
	}

	public Book read(Integer[] pk) throws SQLException {		
		String sql="select * from tbl_book where bookId=?";
		List<Book> bookList= template.query(sql,pk,this);
		if(!bookList.isEmpty()){
			return bookList.get(0);
		}else{
			return null;
		}
	}

	public List<Book> readAll() throws SQLException {
		String sql="select * from tbl_book";
		return template.query(sql,new Integer[]{},this);
	}
	
	public List<Book> readAll(Integer pageNo,Integer pageSize,String searchString) throws SQLException {
		setPageNo(pageNo);
		setPageSize(pageSize);	
		String sql="Select * from tbl_book t";
		if(searchString!=null && !searchString.equals("") && !searchString.equals("null")){
			searchString="%"+searchString+"%";
			sql=sql+" where t.title like ?";
			sql=frameLimitQuery(sql, pageNo, pageSize);
			return template.query(sql,new String[] {searchString},this);
		}else{
			sql=frameLimitQuery(sql, pageNo, pageSize);
			return template.query(sql,new String[] {},this);
		}
	}
	
	public void updateBookAuthorMappings(Book book,List<Author> originalValues) throws SQLException{
		List<Author> newSelections=book.getAuthorList();
		String strInsertSql="insert into tbl_book_authors(bookId,authorId) values(?,?)";
		String strDeleteSql="delete from tbl_book_authors where bookId=? and authorId=?";
		if(newSelections!=null){
			for(Author auth:newSelections){
				if(originalValues==null || !originalValues.contains(auth)){
					template.update(strInsertSql,new Object[]{book.getId(),auth.getId()});
				}else{
					originalValues.remove(auth);
				}
			}
		}
		if(originalValues!=null){
			for(Author auth:originalValues){
				template.update(strDeleteSql,new Object[]{book.getId(),auth.getId()});
			}
		}
	}
	
	public void updateBookGenreMappings(Book book,List<Genre> originalValues) throws SQLException{
		List<Genre> newSelections=book.getGenresList();
		String strInsertGenre="insert into tbl_book_genres(bookId,genre_id) values(?,?)";
		String strDeleteGenre="delete from tbl_book_genres where bookId=? and genre_id=?";
		if(newSelections!=null){
			for(Genre genre:newSelections){
				if( originalValues==null || !originalValues.contains(genre)){
					template.update(strInsertGenre,new Object[]{book.getId(),genre.getId()});
				}else{
					originalValues.remove(genre);
				}
			}
		}
		if(originalValues!=null){
			for(Genre genre:originalValues){
				template.update(strDeleteGenre,new Object[]{book.getId(),genre.getId()});
			}
		}
	}
	
	public List<Book> getBooksByAnAuthor(Author author) throws SQLException{
		String strSql="Select tb.* from tbl_book tb inner join tbl_book_authors tba on tb.bookId=tba.bookId where tba.authorId=?";
		return template.query(strSql,new Object[] {author.getId()},this);
	}
	

	public Integer getCountOfBooks(String searchString) throws SQLException  {
		String sql="Select count(*) from tbl_book t";
		if(searchString!=null && !searchString.equals("")){
			sql+=" where t.title like ?";
			return template.queryForObject(sql,new String[] {"%"+searchString+"%"},Integer.class);
		}else{
			return template.queryForObject(sql,new String[] {},Integer.class);
		}

	}


	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Book> bookList=null;
			while(rs.next()){
				if(bookList==null){
					bookList=new ArrayList<Book>();
				}
				Book book=new Book();
				book.setId(rs.getInt("bookId"));
				book.setTitle(rs.getString("title"));
				bookList.add(book);
			}
			return bookList;
	}
	

}
