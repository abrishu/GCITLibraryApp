package com.gcitsolutions.libraryapp.Entity;

public class Genre implements BaseEntity{

	private int id;
	public Genre(int id) {
		super();
		this.id = id;
	}
	private String name;
	public Genre() {
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

	public String toString() {
		return this.name;
	}

	//
	@Override
	public int hashCode() {
		int hash = 3;
		hash = 53 * hash + (this.getName() != null ? this.name.hashCode() : 0);
		hash = 53 * hash + this.getId();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {

		return (this.getId() == ((Genre) obj).getId());

	}
}
