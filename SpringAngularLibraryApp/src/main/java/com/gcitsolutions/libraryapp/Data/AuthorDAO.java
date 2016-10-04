package com.gcitsolutions.libraryapp.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.gcitsolutions.libraryapp.Entity.Author;
import com.gcitsolutions.libraryapp.Entity.Book;


public class AuthorDAO extends BaseDAO<Author> {

	final String AUTH_COLLECTION="Authors";
	
	@Transactional
	public void create(Author auth) throws SQLException {
		String sql="insert into tbl_author(authorName) values (?)";
		template.update(sql,new Object[] {auth.getName()});
	}
	
	public void createMongo(Author auth) throws SQLException {
		mongoTemplate.save(auth,AUTH_COLLECTION);
	}

	@Transactional
	public void update(Author auth) throws SQLException {
		String sql="update tbl_author t set t.authorName=? where t.authorId=?";
		template.update(sql,new Object[] {auth.getName(),auth.getId()});
		
	}
	
	
	@Transactional
	public void delete(Author auth) throws SQLException {
		String sql="Delete from tbl_author where authorId=?";
		template.update(sql,new Object[] {auth.getId()});
	}
	
	
	public Author read(Integer[] pk) throws SQLException {
		String sql="Select * from tbl_Author t where t.authorId=?";
		List<Author> authList=template.query(sql,pk,this);
		if(!authList.isEmpty()){
			return authList.get(0);
		}else{
			return null;
		}
	}
	
	public List<Author> readAll(Integer pageNo,Integer pageSize,String searchString) throws SQLException {
		String sql="Select * from tbl_Author t";
		
		if(searchString!=null && !searchString.equals("")){
			searchString="%"+searchString+"%";
			sql=sql+" where t.authorName like ?";
			sql=frameLimitQuery(sql, pageNo, pageSize);
			return template.query(sql,new String[] {searchString},this);
		}else{
			sql=frameLimitQuery(sql, pageNo, pageSize);
			return template.query(sql,new String[] {},this);
		}
	}
	
	public List<Author> getAuthorsForABook(Book b) throws SQLException {
		String sql="Select ta.* from tbl_Author ta inner join tbl_book_authors tba on ta.authorId=tba.authorId where tba.bookId=?";
		return template.query(sql,new Integer[] {b.getId()},this);
	}

	public List<Author> readAll() throws SQLException {
		String sql="Select * from tbl_Author t";
		return template.query(sql,new String[] {},this);
	}	
	
	public List<Author> readAllMongo() throws SQLException {
		return mongoTemplate.findAll(Author.class);
	}	
	
	public Integer getCountOfAuthors(String searchString) throws SQLException {
		String sql="Select count(*) from tbl_Author t";
		if(searchString!=null && !searchString.equals("")){
			sql+=" where t.authorName like ?";
			return template.queryForObject(sql,new String[] {"%"+searchString+"%"},Integer.class);
		}else{
			return template.queryForObject(sql,new String[] {},Integer.class);
		}
	}


	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Author auth=null;
		List<Author> authList=null;
		while(rs.next()){
			if(authList==null){
				authList=new ArrayList<Author>();
			}
			auth=new Author();
			auth.setId(rs.getInt("authorId"));
		auth.setName(rs.getString("authorName"));
			authList.add(auth);
		}
		return authList;
	}
	
}
