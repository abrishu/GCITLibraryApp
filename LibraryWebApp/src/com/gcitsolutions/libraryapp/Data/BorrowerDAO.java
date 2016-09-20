package com.gcitsolutions.libraryapp.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcitsolutions.libraryapp.Entity.Borrower;

public class BorrowerDAO extends BaseDAO<Borrower> {

	public BorrowerDAO(Connection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create(Borrower bor) throws SQLException {
		String sql="insert into tbl_Borrower(name,address,phone) values (?,?,?)";
		save(sql,new Object[] {bor.getName(),bor.getAddress(),bor.getPhone()});
		
	}

	@Override
	public void update(Borrower bor) throws SQLException {
		String sql="update tbl_Borrower t set t.name=?,t.address=?,t.phone=? where t.cardNo=?";
		save(sql,new Object[] {bor.getName(),bor.getAddress(),bor.getPhone()});
	}

	@Override
	public void delete(Borrower bor) throws SQLException {
		String sql="delete from tbl_Borrower where t.cardNo=?";
		save(sql,new Object[] {bor.getCardNo()});
	}

	@Override
	public Borrower read(Integer[] pk) throws SQLException {
		// TODO Auto-generated method stub
		String sql="select * from tbl_Borrower t where t.cardNo=?";
		return read(sql,pk);
	}

	@Override
	public List<Borrower> readResult(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
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

	@Override
	public List<Borrower> readAll() throws SQLException {
		// TODO Auto-generated method stub
		String sql="select * from tbl_Borrower t ";
		return readAll(sql,new Integer[]{});
	}

}
