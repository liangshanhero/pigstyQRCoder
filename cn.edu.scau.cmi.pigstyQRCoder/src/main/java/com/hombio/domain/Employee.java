package com.hombio.domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Employee {
	
	private StringProperty name;
	private StringProperty telephone;
	private StringProperty memo;
	
	public Employee() {
		this.name = new SimpleStringProperty();
		this.telephone = new SimpleStringProperty();
		this.memo =  new SimpleStringProperty();
	}

	
	public String getMemo() {
		return memo.get();
	}


	public void setMemo(String memo) {
		this.memo.set(memo);
	}


	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getTelephone() {
		return telephone.get();
	}

	public void setTelephone(String telephone) {
		this.telephone.set(telephone);
	}
	
	@Override
	public String toString() {
		return this.getName();
	}

}
