package com.hombio.domain;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Pigsty {
	private IntegerProperty id;
	private StringProperty pigHouse;
	private IntegerProperty number;

	public Pigsty() {
		this.id = new SimpleIntegerProperty();
		this.pigHouse = new SimpleStringProperty();
		this.number = new SimpleIntegerProperty();
	}

	
	public Integer getId() {
		return id.get();
	}


	public void setId(Integer id) {
		this.id.set(id);
	}


	public String getPigHouse() {
		return pigHouse.get();
	}

	public void setPigHouse(String pigHouse) {
		this.pigHouse.set(pigHouse);
	}

	public Integer getNumber() {
		return number.get();
	}

	public void setNumber(Integer number) {
		this.number.set(number);
	}
	
	@Override
	public String toString() {
		return this.getPigHouse()+" "+"第"+this.getNumber()+"号猪栏";
	}
	
}
