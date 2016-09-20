package com.gcitsolutions.libraryapp.Data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcitsolutions.libraryapp.Entity.Book;
import com.gcitsolutions.libraryapp.Entity.BookLoan;
import com.gcitsolutions.libraryapp.Entity.Borrower;
import com.gcitsolutions.libraryapp.Entity.LibraryBranch;

public class BookLoanDAO extends BaseDAO<BookLoan> {

	public BookLoanDAO(Connection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create(BookLoan bloan) throws SQLException {
		String sql="insert into tbl_book_loans(bookId,branchId,cardNo,dateOut,dueDate,dateIn) values (?,?,?,curdate(),cast(curdate()+7 as datetime),null)";
		save(sql,new Object[] {bloan.getBook().getId(),bloan.getBranch().getId(),bloan.getBor().getCardNo()});
	}

	@Override
	public void update(BookLoan bloan) throws SQLException {
		
	}

	
	@Override
	public void delete(BookLoan bloan) throws SQLException {
		String sql="Delete from tbl_book_loans where bookId=? and branchId=? and cardNo=?";
		save(sql,new Object[]{bloan.getBook().getId(),bloan.getBranch().getId(),bloan.getBor().getCardNo()});
		
	}

	@Override
	public BookLoan read(Integer[] pk) throws SQLException {
		// TODO Auto-generated method stub
		String sql="select * from tbl_book_loans t where t.bookId=? and t.branchId=? and t.cardNo=?";
		return read(sql,pk);
	}

	@Override
	public List<BookLoan> readResult(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<BookLoan> bookLoanList=null;
		BookLoan bookLoan=null;
		Connection conn=null;
		while(rs.next()){
			if(bookLoanList==null){
				bookLoanList=new ArrayList<BookLoan>();
			}
			bookLoan=new BookLoan();
			conn=ConnectionUtils.getConnection();
			
			BookDAO bdao=new BookDAO(conn);
			bookLoan.setBook(bdao.read(new Integer[]{rs.getInt("bookId")}));
			
			LibraryBranchDAO libdao=new LibraryBranchDAO(conn);
			bookLoan.setBranch(libdao.read(new Integer[] {rs.getInt("branchId")}));
			
			BorrowerDAO bordao=new BorrowerDAO(conn);
			bookLoan.setBor(bordao.read(new Integer[] {rs.getInt("cardNo")}));

			bookLoan.setDateIn(rs.getDate("dateIn"));
			bookLoan.setDateOut(rs.getDate("dateOut"));
			bookLoan.setDueDate(rs.getDate("dueDate"));
			
			bookLoanList.add(bookLoan);
		}
		return bookLoanList;
	}

	@Override
	public List<BookLoan> readAll() throws SQLException {
		String sql="select * from tbl_book_loans t";
		return readAll(sql,new Integer[]{});
	}
	
	public void updateCheckInDate(BookLoan bloan) throws SQLException {
		String sql="update tbl_book_loans t set t.dateIn=curdate() where t.bookId=? and t.branchId=? and t.cardNo=?";
		save(sql,new Object[]{bloan.getBook().getId(),bloan.getBranch().getId(),bloan.getBor().getCardNo()});
	}
	
	public void updateDueDate(BookLoan bloan) throws SQLException {
		String sql="update tbl_book_loans t set t.dueDate=? where t.bookId=? and t.branchId=? and t.cardNo=?";
		java.sql.Date sqlDueDate=new Date(bloan.getDueDate().getTime());
		save(sql,new Object[]{sqlDueDate,bloan.getBook().getId(),bloan.getBranch().getId(),bloan.getBor().getCardNo()});	
	}
	
	public void updateCheckOutAndDueDate(BookLoan bloan) throws SQLException {
		String sql="update tbl_book_loans t set t.dateIn=null, t.dateOut=curdate(),t.dueDate=cast(curdate()+7 as datetime) where t.bookId=? and t.branchId=? and t.cardNo=?";
		save(sql,new Object[]{bloan.getBook().getId(),bloan.getBranch().getId(),bloan.getBor().getCardNo()});	
	}
	

}
