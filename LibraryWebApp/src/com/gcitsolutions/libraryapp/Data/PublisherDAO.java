package com.gcitsolutions.libraryapp.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.Publisher;

public class PublisherDAO extends BaseDAO<Publisher> {

	public PublisherDAO(Connection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create(Publisher pub) throws SQLException {
		String sql="insert into tbl_publisher(publisherName,publisherAddress,publisherPhone) values (?,?,?)";
		save(sql,new Object[]{pub.getName(),pub.getAddress(),pub.getPhone()});
		
	}
	
	public Publisher getPublisherForABook(Book book) throws SQLException {
		String sql="select * from tbl_publisher tp inner join tbl_book tb on tp.publisherId=tb.pubId where tb.bookId=?";
		return read(sql,new Integer[]{book.getId()});
		
	}

	@Override
	public void update(Publisher pub) throws SQLException {
		String sql ="update tbl_publisher t set t.publisherName=?,t.publisherAddress=?,t.publisherPhone=? where t.publisherId=?";
		save(sql,new Object[]{pub.getName(),pub.getAddress(),pub.getPhone(),pub.getId()});
		
	}

	@Override
	public void delete(Publisher pub) throws SQLException {
		String sql="Delete from tbl_Publisher where authorId=?";
		save(sql,new Object[] {pub.getId()});
	}

	@Override
	public Publisher read(Integer[] pk) throws SQLException {
		// TODO Auto-generated method stub
		String sql="Select * from tbl_Publisher t where t.publisherId=?";
		return read(sql,pk);
	}

	@Override
	public List<Publisher> readResult(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
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

	@Override
	public List<Publisher> readAll() throws SQLException {
		// TODO Auto-generated method stub
		String sql="Select * from tbl_Publisher t";
		return readAll(sql,new Integer[] {});
	}

}
