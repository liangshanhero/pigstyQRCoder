package com.hombio.dao;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import com.mchange.v2.c3p0.ComboPooledDataSource;



public class PigDAO {

	private static ComboPooledDataSource ds = new ComboPooledDataSource();
	 static {
   	 Properties properties = new Properties();
   	 try {
			properties.load(PigDAO.class.getResourceAsStream("dao.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	 try {
			ds.setDriverClass(properties.getProperty("driverClass"));
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	 
        ds.setJdbcUrl(properties.getProperty("jdbcUrl"));
        ds.setUser(properties.getProperty("user"));
        ds.setPassword(properties.getProperty("password"));
        ds.setMaxPoolSize(new Integer(properties.getProperty("maxPoolSize")));
        ds.setMinPoolSize(new Integer(properties.getProperty("minPoolSize")));
        ds.setInitialPoolSize(new Integer(properties.getProperty("initialPoolSize")));
        ds.setMaxStatements(new Integer(properties.getProperty("maxPoolSize")));
    }
	 
	public PigDAO() {
		
	}
	 
     
	 //正常的话会返回一个属性为id，pigHouse，number的CachedRowSet
	 //供离线操作
     public  CachedRowSet findAllPigHouses() {
    	String sql = "select ph.Id,pht.name as pigHouse,ph.Number "
    					 + "from pig_house as ph, pig_house_type  as pht "
    					 + "where ph.pigHouseTypeId=pht.Id "
    					 + "order by pigHouse,ph.Number";
		try {
				Connection conn = ds.getConnection();
				Statement statement = conn.createStatement();
				ResultSet resultSet = statement.executeQuery(sql);
				RowSetFactory rowSetFactory = RowSetProvider.newFactory();
				CachedRowSet cachedRowSet = rowSetFactory.createCachedRowSet();
				cachedRowSet.populate(resultSet);
				resultSet.close();
				statement.close();
				conn.close();
				return cachedRowSet;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 return null;
     }
     
     public  CachedRowSet findAllPigstyByPigHouseId(int pigHouseId) {
     	String sql = "select id,number from pigsty where pigHouseId = ? order by number";
 		try {
 				Connection conn = ds.getConnection();
 				PreparedStatement preparedStatement = conn.prepareStatement(sql);
 				preparedStatement.setInt(1, pigHouseId);
 				ResultSet resultSet = preparedStatement.executeQuery();
 				RowSetFactory rowSetFactory = RowSetProvider.newFactory();
 				CachedRowSet cachedRowSet = rowSetFactory.createCachedRowSet();
 				cachedRowSet.populate(resultSet);
 				resultSet.close();
 				preparedStatement.close();
 				conn.close();
 				return cachedRowSet;
 		} catch (SQLException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
     	 return null;
      }
     
     public CachedRowSet findAllEmpoyees() {
    	 String sql = "select name,number,memo from employee where logOn=1";
    	 try {
    		    Connection conn = ds.getConnection();
				Statement statement = conn.createStatement();
				ResultSet resultSet = statement.executeQuery(sql);
				RowSetFactory rowSetFactory = RowSetProvider.newFactory();
				CachedRowSet cachedRowSet = rowSetFactory.createCachedRowSet();
				cachedRowSet.populate(resultSet);
				resultSet.close();
				statement.close(); 
				conn.close();
				return cachedRowSet;
    	 }catch (SQLException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
      	 return null;
     }
}
