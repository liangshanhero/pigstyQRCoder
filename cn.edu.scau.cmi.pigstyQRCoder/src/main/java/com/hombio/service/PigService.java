package com.hombio.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import com.hombio.dao.PigDAO;
import com.hombio.domain.Employee;
import com.hombio.domain.PigHouse;
import com.hombio.domain.Pigsty;

public class PigService {
	private PigDAO pigDAO;
	
	public PigService() {
		pigDAO = new PigDAO();
	}
	
	public List<PigHouse> getAllPigHouse() throws SQLException{
		CachedRowSet cachedRowSet = pigDAO.findAllPigHouses();
		if(cachedRowSet == null){
			return null;
		}
		List<PigHouse> pigHouseList = new ArrayList<PigHouse>();
		while(cachedRowSet.next()) {
			PigHouse pigHouse = new PigHouse();
			pigHouse.setId(cachedRowSet.getInt(1));
			pigHouse.setName(cachedRowSet.getString(2));
			pigHouse.setNumber(cachedRowSet.getInt(3));
			pigHouseList.add(pigHouse);
		}
		return pigHouseList;
	}
	
	public List<Pigsty> getAllPigstiesByPigHouse(PigHouse pigHouse) throws SQLException{
		CachedRowSet cachedRowSet = pigDAO.findAllPigstyByPigHouseId(pigHouse.getId());
		if(cachedRowSet==null) {
			return null;
		}
		List<Pigsty> pigstyList = new ArrayList<>();
		while(cachedRowSet.next()) {
			Pigsty pigsty = new Pigsty();
			pigsty.setPigHouse(pigHouse.toString());
			pigsty.setNumber(cachedRowSet.getInt(2));
			pigsty.setId(cachedRowSet.getInt(1));
			pigstyList.add(pigsty);
		}
		return pigstyList;
	}
	
	public List<Employee> getAllEmployees() throws SQLException{
		CachedRowSet cachedRowSet = pigDAO.findAllEmpoyees();
		if(cachedRowSet==null) {
			return null;
		}
		List<Employee> employeeList = new ArrayList<>();
		while(cachedRowSet.next()) {
			Employee employee = new Employee();
			employee.setName(cachedRowSet.getString(1));
			employee.setTelephone(cachedRowSet.getString(2));
			employee.setMemo(cachedRowSet.getString(3));
			employeeList.add(employee);
		}
		return employeeList;
	}
	
}
