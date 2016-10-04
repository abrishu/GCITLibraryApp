package com.gcitsolutions.libraryapp.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.Genre;

public class GenreDAO extends BaseDAO<Genre> {

	public void create(Genre genre) throws SQLException {
		String sql="insert into tbl_genre(genre_name) values (?)";
		template.update(sql,new Object[] {genre.getName()});
	}


	public void update(Genre genre) throws SQLException {
		String sql="update tbl_genre tg set tg.genre_name=? where tg.genre_id=?";
		template.update(sql,new Object[] {genre.getName(),genre.getId()});
		
	}


	public void delete(Genre genre) throws SQLException {
		String sql="delete from tbl_genre where genre_id=?";
		template.update(sql,new Object[] {genre.getId()});
	}

	public Genre read(Integer[] pk) throws SQLException {
		String sql="Select * from tbl_genre tg where tg.genre_id=?";
		List<Genre> genreList=template.query(sql,pk,this);
		if(!genreList.isEmpty()){
			return genreList.get(0);
		}else{
			return null;
		}
	}
	
	public List<Genre> getGenresForABook(Book b) throws SQLException {
		String sql="Select tg.* from tbl_genre tg inner join tbl_book_genres tbg on tg.genre_id=tbg.genre_id where tbg.bookId=?";
		return template.query(sql,new Integer[]{b.getId()},this);
	}

	public List<Genre> readAll() throws SQLException {
		String sql="Select * from tbl_genre tg";
		return template.query(sql,new Integer[]{},this);
	}

	public Integer getCountOfGenres(String searchString) throws SQLException {
		String sql="Select count(*) from tbl_genre t";
		if(searchString!=null && !searchString.equals("")){
			sql+=" where t.genre_name like ?";
			return template.queryForObject(sql,new String[] {"%"+searchString+"%"},Integer.class);
		}else{
			return template.queryForObject(sql,new String[] {},Integer.class);
		}
	}
	
	public List<Genre> readAll(Integer pageNo,Integer pageSize,String searchString) throws SQLException {
		String sql="Select * from tbl_genre t";
		
		if(searchString!=null && !searchString.equals("")){
			searchString="%"+searchString+"%";
			sql=sql+" where t.genre_name like ?";
			sql=frameLimitQuery(sql, pageNo, pageSize);
			return template.query(sql,new String[] {searchString},this);
		}else{
			sql=frameLimitQuery(sql, pageNo, pageSize);
			return template.query(sql,new String[] {},this);
		}
	}
	
	
	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Genre> genreList=null;
		Genre genre=null;
		while(rs.next()){
			if(genreList==null){
				genreList=new ArrayList<Genre>();
			}
			genre=new Genre();
			genre.setName(rs.getString("genre_name"));
			genre.setId(rs.getInt("genre_id"));
			genreList.add(genre);
		}
		return genreList;
	}

}
