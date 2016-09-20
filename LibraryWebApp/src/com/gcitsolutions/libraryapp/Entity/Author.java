package com.gcitsolutions.libraryapp.Entity;

import java.util.ArrayList;
import java.util.List;

//Java Persistence API

public class Author implements BaseEntity {
	private int id;
	public Author(int id) {
		super();
		this.id = id;
	}

	private String name;
	private List<Book> books;
	
	public Author(int id, String name, List<Book> books) {
		super();
		this.id = id;
		this.name = name;
		this.books = books;
	}

	public Author(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Author() {
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
	
	
	public void addBook(Book book){
		if(books==null){
			this.books=new ArrayList<Book>();
		}
		this.books.add(book);
	}
	
	public String toString(){
		return this.name;
	}
	
	@Override
	public int hashCode() {
	    int hash = 3;
	    hash = 53 * hash + (this.getName() != null ? this.name.hashCode() : 0);
	    hash = 53 * hash + this.getId();
	    return hash;
	}

	
	@Override
	public boolean equals(Object obj){
		
		return (this.getId()==((Author)obj).getId());
		
	}

}
