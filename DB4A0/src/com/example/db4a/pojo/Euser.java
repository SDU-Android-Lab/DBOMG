package com.example.db4a.pojo;

import java.io.Serializable;

import org.db4a.annotation.Column;
import org.db4a.annotation.Id;
import org.db4a.annotation.Table;

@Table(name="Euser")
public class Euser implements Serializable{

	private static final long serialVersionUID = 6538222785549283466L;
	@Id(isAuto=true)
	@Column(name="Id")
	private Integer id;
	@Column(name="name")
	private String name;
	@Column(name="pwd")
	private String pwd;
	@Column(name="userType")
	private String userType;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	@Override
	public String toString() {
		return "Euser [id=" + id + ", name=" + name + ", pwd=" + pwd
				+ ", userType=" + userType + "]";
	}
}
