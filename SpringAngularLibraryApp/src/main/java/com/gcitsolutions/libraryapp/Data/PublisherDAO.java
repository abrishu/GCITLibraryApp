package com.gcitsolutions.libraryapp.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.Publisher;

public class PublisherDAO extends BaseDAO<Publisher> {


	public void create(Publisher pub) throws SQLException {
		String sql="insert into tbl_publisher(publisherName,publisherAddress,publisherPhone) values (?,?,?)";
		template.update(sql,new Object[]{pub.getName(),pub.getAddress(),pub.getPhone()});
		
	}
	
	public Publisher getPublisherForABook(Book book) throws SQLException {
		String sql="select tp.* from tbl_publisher tp inner join tbl_book tb on tp.publisherId=tb.pubId where tb.bookId=?";
		List<Publisher> pubList= template.query(sql,new Integer[]{book.getId()},this);
		if(pubList!=null && !pubList.isEmpty()){
			return pubList.get(0);
		}else{
			return null;
		}
		
	}

	public void update(Publisher pub) throws SQLException {
		String sql ="update tbl_publisher t set t.publisherName=?,t.publisherAddress=?,t.publisherPhone=? where t.publisherId=?";
		template.update(sql,new Object[]{pub.getName(),pub.getAddress(),pub.getPhone(),pub.getId()});
		
	}

	public void delete(Publisher pub) throws SQLException {
		String sql="Delete from tbl_Publisher where publisherId=?";
		template.update(sql,new Object[] {pub.getId()});
	}

	public Publisher read(Integer[] pk) throws SQLException {
		// TODO Auto-generated method stub
		String sql="Select * from tbl_Publisher t where t.publisherId=?";
		List<Publisher> pubList= template.query(sql,pk,this);
		if(!pubList.isEmpty()){
			return pubList.get(0);
		}else{
			return null;
		}
	}

	
	public Integer getCountOfPublishers(String searchString) throws SQLException {
		String sql="Select count(*) from tbl_Publisher t";
		if(searchString!=null && !searchString.equals("")){
			sql+=" where t.publisherName like ?";
			return template.queryForObject(sql,new String[] {"%"+searchString+"%"},Integer.class);
		}else{
			return template.queryForObject(sql,new String[] {},Integer.class);
		}
	}

	public List<Publisher> readAll() throws SQLException {
		String sql="Select * from tbl_Publisher t";
		return template.query(sql,new Integer[] {},this);
	}
	
	public List<Publisher> readAll(Integer pageNo,Integer pageSize,String searchString) throws SQLException {
		String sql="Select * from tbl_Publisher t";
		
		if(searchString!=null && !searchString.equals("")){
			searchString="%"+searchString+"%";
			sql=sql+" where t.publisherName like ?";
			sql=frameLimitQuery(sql, pageNo, pageSize);
			return template.query(sql,new String[] {searchString},this);
		}else{
			sql=frameLimitQuery(sql, pageNo, pageSize);
			return template.query(sql,new String[] {},this);
		}
	}
	

	@Override
	public List<Publisher> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Publisher pub=null;
		List<Publisher> pubList=null;
		while(rs.next()){
			if(pubList==null){
				pubList=new ArrayList<Publisher>();
			}
			pub=new Publisher();
			pub.setId(rs.getInt("publisherId"));
			pub.setName(rs.getString("publisherName"));
			pub.setAddress(rs.getString("publisherAddress"));
			pub.setPhone(rs.getString("publisherPhone"));
			pubList.add(pub);
		}
		return pubList;
	}

}
