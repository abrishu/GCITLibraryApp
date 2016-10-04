package com.gcitsolutions.libraryapp.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.BookCopies;
import com.gcitsolutions.libraryapp.Entity.BookLoan;
import com.gcitsolutions.libraryapp.Entity.LibraryBranch;

public class BookCopiesDAO extends BaseDAO<BookCopies>{


	public void create(BookCopies bcop) throws SQLException {
		String sql="insert into tbl_book_copies(bookId,branchId,noOfCopies) values (?,?,?)";
		template.update(sql,new Object[] {bcop.getBook().getId(),bcop.getBranch().getId(),bcop.getNoOfCopies()});
	}


	public void update(BookCopies bcop) throws SQLException {
		String sql="update tbl_book_copies t set t.noOfCopies=? where t.bookId=? and t.branchId=?";
		template.update(sql,new Object[] {bcop.getNoOfCopies(),bcop.getBook().getId(),bcop.getBranch().getId()});
		
	}


	public void delete(BookCopies bcop) throws SQLException {
		String sql="Delete from tbl_book_copies where bookId=? and branchId=?";
		template.update(sql,new Object[] {bcop.getBook().getId(),bcop.getBranch().getId()});
	}


	public BookCopies read(Integer[] pk) throws SQLException {
		String sql="select * from tbl_book_copies t where t.bookId=? and t.branchId=?";
		List<BookCopies> bcopList= template.query(sql,pk,this);
		if(bcopList!=null){
			if(!bcopList.isEmpty()){
				return bcopList.get(0);
			}else{
				return null;
			}
		}
		else{
			return null;
		}
	}

	public void updateNoOfCopiesAfterCheckOut(BookLoan bloan) throws SQLException {
		String sql="update tbl_book_copies t set t.noOfCopies=t.noOfCopies-1 where t.bookId=? and t.branchId=?";
		template.update(sql,new Object[]{bloan.getBook().getId(),bloan.getBranch().getId()});
	}
	
	public void updateNoOfCopiesAfterReturn(BookLoan bloan) throws SQLException {
		String sql="update tbl_book_copies t set t.noOfCopies=t.noOfCopies+1 where t.bookId=? and t.branchId=?";
		template.update(sql,new Object[]{bloan.getBook().getId(),bloan.getBranch().getId()});
	}
	
	public List<BookCopies> getAvailableBookCopiesForABranch(LibraryBranch branch){
		String sql="select * from tbl_book_copies t where t.branchId=? and t.noOfCopies>0";
		List<BookCopies> bcoplist=template.query(sql,new Object[]{branch.getId()},this);
		return bcoplist;
	}
	
	public List<BookCopies> getBranchesContainingABook(Book book){
		String sql="select * from tbl_book_copies t where t.bookId=? and t.noOfCopies>0";
		List<BookCopies> bcoplist=template.query(sql,new Object[]{book.getId()},this);
		return bcoplist;
	}
	
	public List<BookCopies> readAll() throws SQLException {
		String sql="select * from tbl_book_copies t";
		return template.query(sql,new Object[]{},this);
	}
	
	
	public List<BookCopies> readAll(Integer pageNo,Integer pageSize,String searchStringBranch,String searchStringBook) throws SQLException {
		// TODO Auto-generated method stub
		String sql="Select tb.bookId,tb.title,tlb.branchId,tlb.branchName,tbc.noOfCopies from tbl_book_copies tbc inner join tbl_book tb on tbc.bookId=tb.bookId inner join tbl_library_branch tlb on tlb.branchId=tbc.branchId where 1=1 ";
		if(searchStringBranch!=null && !searchStringBranch.equals("")  && !searchStringBranch.equals("null") ){
			searchStringBranch="%"+searchStringBranch+"%";
			sql+=" and tlb.branchName like ?";
		}
		if(searchStringBook!=null && !searchStringBook.equals("") && !searchStringBook.equals("null") ){
			searchStringBook="%"+searchStringBook+"%";
			sql+=" and tb.title like ?";
		}
		
		sql=frameLimitQuery(sql, pageNo, pageSize);
		
		if(searchStringBranch!=null && !searchStringBranch.equals("") && !searchStringBranch.equals("null") ){
			if(searchStringBook!=null && !searchStringBook.equals("") && !searchStringBook.equals("null") ){
				return template.query(sql,new Object[]{searchStringBranch,searchStringBook},this);
			}else{
				return template.query(sql,new Object[]{searchStringBranch},this);
			}
		}else{
			if(searchStringBook!=null && !searchStringBook.equals("") &&  !searchStringBook.equals("null")){
				return template.query(sql,new Object[]{searchStringBook},this);
			}else{
				return template.query(sql,new Object[]{},this);
			}
		}
		
	}
	
	public boolean checkIfBookCheckedOutOrExistsInABranch(Book book) throws SQLException{
		String sql="Select sum(count) as count from ( select count(*) as count from tbl_book_copies where bookId=? and noOfCopies>0  union  select count(*) as count from tbl_book_loans where bookId=? and dateIn!=null) a; ";
		if(template.queryForObject(sql, new Object[]{book.getId(),book.getId()},Integer.class)>0){
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
		if(searchStringBranch!=null && !searchStringBranch.equals("null")){
			searchStringBranch="%"+searchStringBranch+"%";
			sql+=" and tlb.branchName like ?";
		}
		if(searchStringBook!=null && !searchStringBook.equals("null")){
			searchStringBook="%"+searchStringBook+"%";
			sql+=" and tb.title like ?";
		}
		if(searchStringBranch!=null && !searchStringBranch.equals("") && !searchStringBranch.equals("null") ){
			if(searchStringBook!=null && !searchStringBook.equals("")  && !searchStringBook.equals("null") ){
				return template.queryForObject(sql,new Object[]{searchStringBranch,searchStringBook},Integer.class);
			}else{
				return template.queryForObject(sql,new Object[]{searchStringBranch},Integer.class);
			}
		}else{
			if(searchStringBook!=null && !searchStringBook.equals("") && !searchStringBook.equals("null")){
				return template.queryForObject(sql,new Object[]{searchStringBook},Integer.class);
			}else{
				return template.queryForObject(sql,new Object[]{},Integer.class);
			}
		}
		
	}


	@Override
	public List<BookCopies> extractData(ResultSet rs) throws SQLException, DataAccessException {
		BookCopies bcop=null;
		List<BookCopies> bcopList=null;
		while(rs.next()){
			if(bcopList==null){
				bcopList=new ArrayList<BookCopies>();
			}
			bcop=new BookCopies();
			bcop.setBook(new Book(rs.getInt("bookId")));
			bcop.setBranch(new LibraryBranch(rs.getInt("branchId")));
			bcop.setNoOfCopies(rs.getInt("noOfCopies"));
			bcopList.add(bcop);
		}
		return bcopList;
	}

	
}
