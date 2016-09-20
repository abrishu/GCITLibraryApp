package com.gcitsolutions.libraryapp.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcitsolutions.libraryapp.Entity.Genre;

public class GenreDAO extends BaseDAO<Genre> {

	public GenreDAO(Connection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create(Genre genre) throws SQLException {
		String sql="insert into tbl_genre(genre_name) values (?)";
		save(sql,new Object[] {genre.getName()});
	}

	@Override
	public void update(Genre genre) throws SQLException {
		String sql="update tbl_genre tg set tg.genre_name=? where tg.genre_id=?";
		save(sql,new Object[] {genre.getName(),genre.getId()});
		
	}

	@Override
	public void delete(Genre genre) throws SQLException {
		String sql="delete from tbl_genre where genre_id=?";
		save(sql,new Object[] {genre.getId()});
	}

	@Override
	public Genre read(Integer[] pk) throws SQLException {
		// TODO Auto-generated method stub
		return read("Select * from tbl_genre tg where tg.genre_id=",pk);
	}

	@Override
	public List<Genre> readResult(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<Genre> genreList=null;
		Genre genre=null;
		while(rs.next()){
			if(genreList==null){
				genreList=new ArrayList<Genre>();
			}
			genre=new Genre();
			genre.setName(rs.getString("genre_name"));
			genreList.add(genre);
		}
		return genreList;
	}

	@Override
	public List<Genre> readAll() throws SQLException {
		// TODO Auto-generated method stub
		return readAll("Select * from tbl_genre tg",new Integer[]{});
	}

}
