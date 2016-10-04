package com.gcitsolutions.libraryapp.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.gcitsolutions.libraryapp.Entity.LibraryBranch;

public class LibraryBranchDAO extends BaseDAO<LibraryBranch> {


	
	public void create(LibraryBranch branch) throws SQLException {
		String sql="insert into tbl_library_branch(branchName,branchAddress) values (?,?)";
		template.update(sql,new Object[] {branch.getName(),branch.getAddress()});
	}


	public void update(LibraryBranch branch) throws SQLException {
		String sql="update tbl_library_branch t set t.branchName=?,t.branchAddress=? where t.branchId=?";
		template.update(sql,new Object[] {branch.getName(),branch.getAddress(),branch.getId()});
		
	}

	public void delete(LibraryBranch branch) throws SQLException {
		String sql="delete from tbl_library_branch where branchId=?";
		template.update(sql,new Object[] {branch.getId()});
	}

	public LibraryBranch read(Integer[] pk) throws SQLException {
		// TODO Auto-generated method stub
		String sql="select * from tbl_library_branch t where t.branchId=?";
		List<LibraryBranch> branchList=template.query(sql,pk,this);
		if(!branchList.isEmpty()){
			return branchList.get(0);
		}else{
			return null;
		}
	}

	public List<LibraryBranch> readAll() throws SQLException {
		// TODO Auto-generated method stub
		String sql="select * from tbl_library_branch t";
		return template.query(sql,new Integer[] {},this);
	}
	
	public List<LibraryBranch> readAll(Integer pageNo,Integer pageSize,String searchString) throws SQLException {
		// TODO Auto-generated method stub
		setPageNo(pageNo);
		setPageSize(pageSize);
		String sql="select * from tbl_library_branch t";
		if(searchString!=null && !searchString.equals("")){
			searchString="%"+searchString+"%";
			sql=sql+" where t.branchName like ?";
			sql=frameLimitQuery(sql, pageNo, pageSize);
			return template.query(sql,new String[] {searchString},this);
		}else{
			return template.query(sql,new Integer[] {},this);
		}
	}

	@Override
	public List<LibraryBranch> extractData(ResultSet rs) throws SQLException, DataAccessException {
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

}
