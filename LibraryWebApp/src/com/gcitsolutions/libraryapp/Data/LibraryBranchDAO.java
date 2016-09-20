package com.gcitsolutions.libraryapp.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.gcitsolutions.libraryapp.Entity.LibraryBranch;

public class LibraryBranchDAO extends BaseDAO<LibraryBranch> {

	public LibraryBranchDAO(Connection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create(LibraryBranch branch) throws SQLException {
		String sql="insert into tbl_library_branch(branchName,branchAddress) values (?,?)";
		save(sql,new Object[] {branch.getName(),branch.getAddress()});
	}

	@Override
	public void update(LibraryBranch branch) throws SQLException {
		String sql="update tbl_library_branch t set t.branchName=?,t.branchAddress=? where t.branchId=?";
		save(sql,new Object[] {branch.getName(),branch.getAddress(),branch.getId()});
		
	}

	@Override
	public void delete(LibraryBranch branch) throws SQLException {
		String sql="delete from tbl_library_branch where branchId=?";
		save(sql,new Object[] {branch.getId()});
	}

	@Override
	public LibraryBranch read(Integer[] pk) throws SQLException {
		// TODO Auto-generated method stub
		String sql="select * from tbl_library_branch t where t.branchId=?";
		return read(sql,pk);
	}

	@Override
	public List<LibraryBranch> readResult(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<LibraryBranch> branchList=null;
		LibraryBranch branch=null;
		while(rs.next()){
			if(branchList==null){
				branchList=new ArrayList<LibraryBranch>();
			}
			branch=new LibraryBranch();
			branch.setId(rs.getInt("branchId"));
			branch.setName(rs.getString("branchName"));
			branch.setAddress(rs.getString("branchAddress"));
			branchList.add(branch);
		}
		return branchList;
	}

	@Override
	public List<LibraryBranch> readAll() throws SQLException {
		// TODO Auto-generated method stub
		String sql="select * from tbl_library_branch t";
		return readAll(sql,new Integer[] {});
	}
	
	public List<LibraryBranch> readAll(Integer pageNo,Integer pageSize,String searchString) throws SQLException {
		// TODO Auto-generated method stub
		setPageNo(pageNo);
		setPageSize(pageSize);
		String sql="select * from tbl_library_branch t";
		if(searchString!=null && !searchString.equals("")){
			searchString="%"+searchString+"%";
			sql=sql+" where t.branchName like ?";
			return readAll(sql,new String[] {searchString});
		}else{
			return readAll(sql,new Integer[] {});
		}
	}

}
