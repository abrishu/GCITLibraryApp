package com.gcitsolutions.libraryapp.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcitsolutions.libraryapp.Entity.Author;
import com.gcitsolutions.libraryapp.Entity.Book;

public class AuthorDAO extends BaseDAO<Author> {

	public AuthorDAO(Connection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create(Author auth) throws SQLException {
		String sql="insert into tbl_author(authorName) values (?)";
		save(sql,new Object[] {auth.getName()});
	}

	@Override
	public void update(Author auth) throws SQLException {
		String sql="update tbl_author t set t.authorName=? where t.authorId=?";
		save(sql,new Object[] {auth.getName(),auth.getId()});
		
	}

	@Override
	public void delete(Author auth) throws SQLException {
		String sql="Delete from tbl_author where authorId=?";
		save(sql,new Object[] {auth.getId()});
	}

	@Override
	public Author read(Integer[] pk) throws SQLException {
		// TODO Auto-generated method stub
		String sql="Select * from tbl_Author t where t.authorId=?";
		return read(sql,pk);
	}

	@Override
	public List<Author> readResult(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
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

	
	public List<Author> readAll(Integer pageNo,Integer pageSize,String searchString) throws SQLException {
		// TODO Auto-generated method stub
		setPageNo(pageNo);
		setPageSize(pageSize);
		String sql="Select * from tbl_Author t";
		if(searchString!=null && !searchString.equals("")){
			searchString="%"+searchString+"%";
			sql=sql+" where t.authorName like ?";
			return readAll(sql,new String[] {searchString});
		}else{
			return readAll(sql,new Integer[] {});
		}
		
	}
	
	public List<Author> getAuthorsForABook(Book b) throws SQLException {
		// TODO Auto-generated method stub
		String sql="Select ta.* from tbl_Author ta inner join tbl_book_authors tba on ta.authorId=tba.authorId where tba.bookId=?";
		return readAll(sql,new Integer[] {b.getId()});
	}

	@Override
	public List<Author> readAll() throws SQLException {
		// TODO Auto-generated method stub
		String sql="Select * from tbl_Author t";
		return readAll(sql,new Integer[] {});
	}	
	
	public Integer getCountOfAuthors(String searchString) throws SQLException {
		String sql="Select count(*) from tbl_Author t";
		if(searchString!=null && !searchString.equals("")){
			sql+=" where t.authorName like ?";
			return getCount(sql,new String[] {"%"+searchString+"%"});
		}
		return getCount(sql,new Integer[] {});
	}
	
}
