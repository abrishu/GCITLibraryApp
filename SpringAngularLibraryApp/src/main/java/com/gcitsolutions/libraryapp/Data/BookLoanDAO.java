package com.gcitsolutions.libraryapp.Data;


import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.BookLoan;
import com.gcitsolutions.libraryapp.Entity.Borrower;
import com.gcitsolutions.libraryapp.Entity.LibraryBranch;

public class BookLoanDAO extends BaseDAO<BookLoan> {


	public void create(BookLoan bloan) throws SQLException {
		String sql="insert into tbl_book_loans(bookId,branchId,cardNo,dateOut,dueDate,dateIn) values (?,?,?,?,?,null)";
		Date today=getToday(new java.util.Date());
		Date dueDate=getSqlDateAfterAdding(new java.util.Date(),7);
		template.update(sql,new Object[] {bloan.getBook().getId(),bloan.getBranch().getId(),bloan.getBor().getCardNo(),today,dueDate});
	}

	public void delete(BookLoan bloan) throws SQLException {
		String sql="Delete from tbl_book_loans where bookId=? and branchId=? and cardNo=?";
		template.update(sql,new Object[]{bloan.getBook().getId(),bloan.getBranch().getId(),bloan.getBor().getCardNo()});
		
	}

	public BookLoan read(Integer[] pk) throws SQLException {
		String sql="select * from tbl_book_loans t where t.bookId=? and t.branchId=? and t.cardNo=?";
		List<BookLoan> blList= template.query(sql,pk,this);
		if(blList!=null){
			if(!blList.isEmpty()){
				return blList.get(0);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}

	public List<BookLoan> readAll() throws SQLException {
		String sql="select * from tbl_book_loans t";
		return template.query(sql,new Object[]{},this);
	}
	
	public void updateCheckInDate(BookLoan bloan) throws SQLException {
		String sql="update tbl_book_loans t set t.dateIn=curdate() where t.bookId=? and t.branchId=? and t.cardNo=?";
		template.update(sql,new Object[]{bloan.getBook().getId(),bloan.getBranch().getId(),bloan.getBor().getCardNo()});
	}
	
	public void updateDueDate(BookLoan bloan) throws SQLException {
		String sql="update tbl_book_loans t set t.dueDate=? where t.bookId=? and t.branchId=? and t.cardNo=?";
		java.sql.Date sqlDueDate=new Date(bloan.getDueDate().getTime());
		template.update(sql,new Object[]{sqlDueDate,bloan.getBook().getId(),bloan.getBranch().getId(),bloan.getBor().getCardNo()});	
	}
	
	public void updateCheckOutAndDueDate(BookLoan bloan) throws SQLException {
		Date today=getToday(new java.util.Date());
		Date dueDate=getSqlDateAfterAdding(new java.util.Date(),7);
		String sql="update tbl_book_loans t set t.dateIn=null, t.dateOut=?,t.dueDate=? where t.bookId=? and t.branchId=? and t.cardNo=?";
		template.update(sql,new Object[]{today,dueDate,bloan.getBook().getId(),bloan.getBranch().getId(),bloan.getBor().getCardNo()});	
	}

	public List<BookLoan> getCheckedOutBooks(Borrower bor){
		String strSql="Select * from tbl_book_loans t where t.cardNo=? and t.dateIn is null";
		return template.query(strSql,new Object[]{bor.getCardNo()},this);
	}
	
	public List<BookLoan> getAllCheckedOutBooks(){
		String strSql="Select * from tbl_book_loans t where t.dateIn is null";
		return template.query(strSql,new Object[]{},this);
	}
	
	public Date getSqlDateAfterAdding(java.util.Date date,int days){
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 7);
		return new Date(c.getTime().getTime());
	}
	
	public Date getToday(java.util.Date date){
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return new Date(c.getTime().getTime());
	}
	
	@Override
	public List<BookLoan> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<BookLoan> bookLoanList=null;
		BookLoan bookLoan=null;
		while(rs.next()){
			if(bookLoanList==null){
				bookLoanList=new ArrayList<BookLoan>();
			}
			bookLoan=new BookLoan();
			bookLoan.setBook(new Book(rs.getInt("bookId")));
			bookLoan.setBranch(new LibraryBranch(rs.getInt("branchId")));
			bookLoan.setBor(new Borrower(rs.getInt("cardNo")));
			bookLoan.setDateIn(rs.getDate("dateIn"));
			bookLoan.setDateOut(rs.getDate("dateOut"));
			bookLoan.setDueDate(rs.getDate("dueDate"));
			bookLoanList.add(bookLoan);
		}
		return bookLoanList;
	}
	

}
