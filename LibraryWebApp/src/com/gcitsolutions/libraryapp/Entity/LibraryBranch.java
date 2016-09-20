package com.gcitsolutions.libraryapp.Entity;

public class LibraryBranch implements BaseEntity {

	private int id;
	private String name;
	public LibraryBranch(String name, String address) {
		super();
		this.name = name;
		this.address = address;
	}

	public LibraryBranch(int id) {
		super();
		this.id = id;
	}

	public LibraryBranch(int id, String name, String address) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
	}

	private String address;
	
	public LibraryBranch() {
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
