package com.gcitsolutions.libraryapp.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcitsolutions.libraryapp.Entity.Author;
import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.BookCopies;
import com.gcitsolutions.libraryapp.Entity.BookLoan;
import com.gcitsolutions.libraryapp.Entity.LibraryBranch;

public class BookCopiesDAO extends BaseDAO<BookCopies>{

	public BookCopiesDAO(Connection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create(BookCopies bcop) throws SQLException {
		String sql="insert into tbl_book_copies(bookId,branchId,noOfCopies) values (?,?,?)";
		save(sql,new Object[] {bcop.getBook().getId(),bcop.getBranch().getId(),bcop.getNoOfCopies()});
	}

	@Override
	public void update(BookCopies bcop) throws SQLException {
		String sql="update tbl_book_copies t set t.noOfCopies=t.noOfCopies+? where t.bookId=? and t.branchId=?";
		save(sql,new Object[] {bcop.getNoOfCopies(),bcop.getBook().getId(),bcop.getBranch().getId()});
		
	}

	@Override
	public void delete(BookCopies bcop) throws SQLException {
		String sql="Delete from tbl_book_copies where bookId=? and branchId=?";
		save(sql,new Object[] {bcop.getBook().getId(),bcop.getBranch().getId()});
	}

	@Override
	public BookCopies read(Integer[] pk) throws SQLException {
		String sql="select * from tbl_book_copies t where t.bookId=? and t.branchId=?";
		return read(sql,pk);
	}

	public void updateNoOfCopiesAfterCheckOut(BookLoan bloan) throws SQLException {
		String sql="update tbl_book_copies t set t.noOfCopies=t.noOfCopies-1 where t.bookId=? and t.branchId=?";
		save(sql,new Object[]{bloan.getBook().getId(),bloan.getBranch().getId()});
	}
	
	public void updateNoOfCopiesAfterReturn(BookLoan bloan) throws SQLException {
		String sql="update tbl_book_copies t set t.noOfCopies=t.noOfCopies+1 where t.bookId=? and t.branchId=?";
		save(sql,new Object[]{bloan.getBook().getId(),bloan.getBranch().getId()});
	}
	
	@Override
	public List<BookCopies> readResult(ResultSet rs) throws SQLException {
		BookCopies bcop=null;
		List<BookCopies> bcopList=null;
		Connection conn=null;
		while(rs.next()){
			conn=ConnectionUtils.getConnection();
			BookDAO bdao=new BookDAO(conn);
			LibraryBranchDAO libdao=new LibraryBranchDAO(conn);
			if(bcopList==null){
				bcopList=new ArrayList<BookCopies>();
			}
			bcop=new BookCopies();
			bcop.setBook(bdao.read(new Integer[]{rs.getInt("bookId")}));
			bcop.setBranch(libdao.read(new Integer[] {rs.getInt("branchId")}));
			bcop.setNoOfCopies(rs.getInt("noOfCopies"));
			bcopList.add(bcop);
		}
		return bcopList;
	}

	@Override
	public List<BookCopies> readAll() throws SQLException {
		// TODO Auto-generated method stub
		String sql="select * from tbl_book_copies t";
		return readAll(sql,new Integer[]{});
	}
	
	public List<BookCopies> readAll(Integer pageNo,Integer pageSize,String searchStringBranch,String searchStringBook) throws SQLException {
		// TODO Auto-generated method stub
		setPageNo(pageNo);
		setPageSize(pageSize);
		String sql="Select tb.bookId,tb.title,tlb.branchId,tlb.branchName,tbc.noOfCopies from tbl_book_copies tbc inner join tbl_book tb on tbc.bookId=tb.bookId inner join tbl_library_branch tlb on tlb.branchId=tbc.branchId where 1=1 ";
		if(searchStringBranch!=null){
			searchStringBranch="%"+searchStringBranch+"%";
			sql+=" and tlb.branchName like ?";
		}
		if(searchStringBook!=null && !searchStringBook.equals("")){
			searchStringBook="%"+searchStringBook+"%";
			sql+=" and tb.title like ?";
		}
		
		if(searchStringBranch!=null && !searchStringBranch.equals("")){
			if(searchStringBook!=null && !searchStringBook.equals("")){
				return readAll(sql,new Object[]{searchStringBranch,searchStringBook});
			}else{
				return readAll(sql,new Object[]{searchStringBranch});
			}
		}else{
			if(searchStringBook!=null && !searchStringBook.equals("")){
				return readAll(sql,new Object[]{searchStringBook});
			}else{
				return readAll(sql,new Object[]{});
			}
		}
		
	}
	
	
	
	public boolean checkIfBookCheckedOutOrExistsInABranch(Book book) throws SQLException{
		String sql="Select sum(count) as count from ( select count(*) as count from tbl_book_copies where bookId=? and noOfCopies>0  union  select count(*) as count from tbl_book_loans where bookId=? and dateIn!=null) a; ";
		if(getCount(sql, new Object[]{book.getId(),book.getId()})>0){
			return true;
		}else{
			return false;
		}
	}
	
	public Integer getCountOfBookCopies(String searchStringBranch, String searchStringBook) throws SQLException  {
		// TODO Auto-generated method stub
		String sql="Select count(*) from tbl_book_copies tbc"
				+ " inner join tbl_book tb on tbc.bookId=tb.bookId "
				+ "inner join tbl_library_branch tlb on tlb.branchId=tbc.branchId "
				+ "where 1=1 ";
		if(searchStringBranch!=null){
			searchStringBranch="%"+searchStringBranch+"%";
			sql+=" and tlb.branchName like ?";
		}
		if(searchStringBook!=null){
			searchStringBook="%"+searchStringBook+"%";
			sql+=" and tb.title like ?";
		}
		
		if(searchStringBranch!=null && !searchStringBranch.equals("")){
			if(searchStringBook!=null && !searchStringBook.equals("")){
				return getCount(sql,new Object[]{searchStringBranch,searchStringBook});
			}else{
				return getCount(sql,new Object[]{searchStringBranch});
			}
		}else{
			if(searchStringBook!=null && !searchStringBook.equals("")){
				return getCount(sql,new Object[]{searchStringBook});
			}else{
				return getCount(sql,new Object[]{});
			}
		}
		
	}

	
}
