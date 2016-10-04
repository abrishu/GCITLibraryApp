package com.gcitsolutions.libraryapp.Entity;

public class BookCopies {

	private Book book;
	private LibraryBranch branch;
	private int noOfCopies;
	
	
	public BookCopies(Book book, LibraryBranch branch, int noOfCopies) {
		super();
		this.book = book;
		this.branch = branch;
		this.noOfCopies = noOfCopies;
	}
	public BookCopies(Book book, LibraryBranch branch) {
		super();
		this.book = book;
		this.branch = branch;
	}
	public BookCopies() {
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
	public int getNoOfCopies() {
		return noOfCopies;
	}
	public void setNoOfCopies(int noOfCopies) {
		this.noOfCopies = noOfCopies;
	}

}
