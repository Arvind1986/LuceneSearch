package com;

public class Hotel {
	public int id;
	public String name;
	public String city;
	public String description;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
public Hotel(String name,String city,String deString){
	this.name =name;
	this.city =city;
	this.description = deString;
}
}
