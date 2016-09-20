package com.gcitsolutions.libraryapp.Entity;

import java.util.Date;

public class BookLoan implements BaseEntity{

	private Book book;
	private LibraryBranch branch;
	private Borrower bor;
	public BookLoan(Book book, LibraryBranch branch, Borrower bor) {
		super();
		this.book = book;
		this.branch = branch;
		this.bor = bor;
	}

	private Date dateOut;
	private Date dueDate;
	private Date dateIn;
	
	public BookLoan() {
		// TODO Auto-generated constructor stub
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public LibraryBranch getBranch() {
		return branch;
	}

	public void setBranch(LibraryBranch branch) {
		this.branch = branch;
	}

	public Borrower getBor() {
		return bor;
	}

	public void setBor(Borrower bor) {
		this.bor = bor;
	}

	public Date getDateOut() {
		return dateOut;
	}

	public void setDateOut(Date dateOut) {
		this.dateOut = dateOut;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getDateIn() {
		return dateIn;
	}

	public void setDateIn(Date dateIn) {
		this.dateIn = dateIn;
	}

}
