package cn.edu.scau.cmi.hombio.domain;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PigHouse {

	private IntegerProperty id;
	private StringProperty name;
	private IntegerProperty number;
	
	
	public PigHouse(){
		this.id = new SimpleIntegerProperty();
		this.name = new SimpleStringProperty();
		this.number = new SimpleIntegerProperty();
	}
	public Integer getId() {
		return id.get();
	}
	public void setId(Integer id) {
		this.id.set(id);
	}
	public String getName() {
		return name.get();
	}
	public void setName(String name) {
		this.name.set(name);
	}
	public Integer getNumber() {
		return number.get();
	}
	public void setNumber(Integer number) {
		this.number.set(number);
	}
	@Override
	public String toString() {
		return this.getName()+this.getNumber()+"号";
	}
	
}
