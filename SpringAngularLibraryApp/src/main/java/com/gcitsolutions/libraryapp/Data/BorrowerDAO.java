package com.gcitsolutions.libraryapp.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.gcitsolutions.libraryapp.Entity.Borrower;

public class BorrowerDAO extends BaseDAO<Borrower> {

	
	public void create(Borrower bor) throws SQLException {
		String sql="insert into tbl_Borrower(name,address,phone) values (?,?,?)";
		template.update(sql,new Object[] {bor.getName(),bor.getAddress(),bor.getPhone()});
		
	}

	public void update(Borrower bor) throws SQLException {
		String sql="update tbl_Borrower t set t.name=?,t.address=?,t.phone=? where t.cardNo=?";
		template.update(sql,new Object[] {bor.getName(),bor.getAddress(),bor.getPhone(),bor.getCardNo()});
	}

	public void delete(Borrower bor) throws SQLException {
		String sql="delete from tbl_Borrower where t.cardNo=?";
		template.update(sql,new Object[] {bor.getCardNo()});
	}

	public Borrower read(Integer[] pk) throws SQLException {
		String sql="select * from tbl_Borrower t where t.cardNo=?";
		List<Borrower> borList=null;
		borList=template.query(sql,pk,this);
		if(borList!=null){
			if(!borList.isEmpty()){
				return borList.get(0);
			}else{
				return null;
			}
		}else{
			return null;
		}
		
	}
	
	public List<Borrower> readAll() throws SQLException {
		String sql="select * from tbl_Borrower t ";
		return template.query(sql,new Integer[]{},this);
	}

	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Borrower> borList=null;
		Borrower bor=null;
		while(rs.next()){
			if(borList==null){
				borList=new ArrayList<Borrower>();
			}
			bor=new Borrower();
			bor.setCardNo(rs.getInt("cardNo"));
			bor.setName(rs.getString("name"));
			bor.setAddress(rs.getString("address"));
			bor.setPhone(rs.getString("phone"));
			borList.add(bor);
		}
		return borList;
	}

}
