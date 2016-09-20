package com.gcitsolutions.libraryapp.Entity;

import java.util.ArrayList;
import java.util.List;

public class Book implements BaseEntity {

	private int id;
	private List<Genre> genresList;

	public Book(int id) {
		super();
		this.id = id;
	}

	public Book(String title, List<Author> authorList, Publisher publisher) {
		super();
		this.title = title;
		this.authorList = authorList;
		this.publisher = publisher;
	}

	private String title;
	private List<Author> authorList;

	public Book(int id, String title, Publisher publisher) {
		super();
		this.id = id;
		this.title = title;
		this.publisher = publisher;
	}

	private Publisher publisher;

	public Book(int id, String title) {
		this.id = id;
		this.title = title;
	}

	public Book(int id, String title, List<Author> authorList, Publisher publisher) {
		this.id = id;
		this.title = title;
		this.authorList = authorList;
		this.publisher = publisher;
	}

	public Book() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Author> getAuthorList() {
		return authorList;
	}

	public void setAuthorList(List<Author> authorList) {
		this.authorList = authorList;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public void addAuthor(Author aut) {
		if (authorList == null) {
			this.authorList = new ArrayList<Author>();
		}
		this.authorList.add(aut);
	}

	public String toString() {
		return this.title + " by " + this.authorList + " ;published by:" + this.getPublisher().toString();
	}

	public List<Genre> getGenresList() {
		return genresList;
	}

	public void setGenresList(List<Genre> genresList) {
		this.genresList = genresList;
	}
}
