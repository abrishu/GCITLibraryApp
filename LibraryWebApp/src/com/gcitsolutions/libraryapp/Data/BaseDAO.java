package com.gcitsolutions.libraryapp.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.Statement;

public abstract class BaseDAO<BaseEntity> {
	
	
	private Integer pageSize;
	private Integer pageNo;
	private Connection conn = null;
	
	public BaseDAO(Connection conn) {
		this.conn = conn;
	}

	public abstract void create(BaseEntity be) throws SQLException;
	
	public abstract void update(BaseEntity be) throws SQLException;

	public abstract void delete(BaseEntity be) throws SQLException;

	public abstract BaseEntity read(Integer[] pk) throws SQLException;
	
	public abstract List<BaseEntity> readResult(ResultSet rs) throws SQLException;
	
	public BaseEntity read(String sql,Integer[] vals) throws SQLException{
		
		ResultSet rs=null;
		try{
			rs = executeStatement(sql, vals);
			List<BaseEntity> beList=readResult(rs);
			if(beList==null || beList.isEmpty()){
				return null;
			}else{
				return beList.get(0);
			}
		}
		finally{
		}
		
	}
	
	public List<BaseEntity> readAll(String sql,Object[] vals) throws SQLException{
		
		pageNo=getPageNo();
		pageSize=getPageSize();
		if(pageNo!=null){
			if(Integer.valueOf(pageNo)>0){
				int index=(pageNo-1)*pageSize;
				sql+=" LIMIT "+ index +","+pageSize;
			}
		}
		ResultSet rs =executeStatement(sql, vals);
		return readResult(rs);
}
	
	/*
	public List<BaseEntity> readAll(String sql,Integer[] vals) throws SQLException{
		
			pageNo=getPageNo();
			pageSize=getPageSize();
			if(pageNo!=null){
				if(Integer.valueOf(pageNo)>0){
					int index=(pageNo-1)*pageSize;
					sql+=" LIMIT "+ index +","+pageSize;
				}
			}
			ResultSet rs =executeStatement(sql, vals);
			return readResult(rs);
	}*/
	
	

	public Integer getCount(String sql,Object[] vals) throws SQLException{
		ResultSet rs=executeStatement(sql,vals);
		while(rs.next()){
			return rs.getInt(1);
		}
		return -1;
	}
	
	private ResultSet executeStatement(String sql, Object[] vals) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try{
			stmt = conn.prepareStatement(sql);
			int idx = 1;
			for(Object o : vals) {
					stmt.setObject(idx, o);
				idx++;
			}
			rs=stmt.executeQuery();
		}finally{
		}
		return rs;
	}
	
	public abstract List<BaseEntity> readAll() throws SQLException;
	
	protected void save(String sql, Object[] vals) throws SQLException {
 
		PreparedStatement stmt = conn.prepareStatement(sql);
		int idx = 1;
		for(Object o : vals) {
			stmt.setObject(idx, o);
			idx++;
		}

		stmt.executeUpdate();
	}

	protected int saveAndGetId(String sql, Object[] vals) throws SQLException {
		PreparedStatement stmt=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		ResultSet rs=null;
		int lastInsertId=-1;
		int idx = 1;
		for(Object o : vals) {
			stmt.setObject(idx, o);
			idx++;
		}
		stmt.executeUpdate();
		rs=stmt.getGeneratedKeys();
		
		if (rs.next()) {
			lastInsertId = rs.getInt(1);
		} 
		return lastInsertId;
		
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

}
