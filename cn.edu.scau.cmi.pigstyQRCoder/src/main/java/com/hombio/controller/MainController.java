package com.hombio.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import com.hombio.domain.Employee;
import com.hombio.domain.PigHouse;
import com.hombio.domain.Pigsty;
import com.hombio.service.PigService;

import cn.edu.scau.cmi.pigstyQRCoder.CoderUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.ComboBox;
import javafx.event.Event;
import javafx.scene.control.Tab;

public class MainController implements Initializable {
	private PigService pigService;
	//猪舍的表格
	@FXML TableColumn<PigHouse,String> pigHouseCol;
	@FXML TableColumn<PigHouse,Integer> numberCol;
	@FXML TableColumn<PigHouse,String> pigstyCol;
	@FXML TableView<PigHouse> pigHouseTableView;
	@FXML TableColumn<PigHouse,Boolean> pigHousechoiceCol;
	
	//猪栏的表格
	@FXML TableView<Pigsty> pigstyTableView;
	@FXML TableColumn<Pigsty,Boolean> pigstyChoiceCol;
	@FXML TableColumn<Pigsty,String> pigHouseCols;
	@FXML TableColumn<Pigsty,Integer> pigstyNumCol;
	@FXML Button refreshOrBack;
	
	//右侧操作控件
	@FXML Button selectNoneBtn;
	@FXML Button selectAllBtn;
	@FXML TextField targetFileTextFiled;
	@FXML ImageView logoImageView;
	@FXML ComboBox<String> imageFormatComboBox;
	
	//员工表格
	@FXML TableView<Employee> employeeTableView;
	@FXML TableColumn<Employee,String> employeeNameCol;
	@FXML TableColumn<Employee,String> telephoneCol;
	@FXML TableColumn<Employee,Boolean> employeeChoiceCol;
	@FXML TableColumn<Employee,String> employeeMemoCol;
	@FXML Tab employeeTab;
	@FXML Tab pigTab;

	
	private Set<Integer> selectedindexSet;
	private List<Pigsty> pigstyList;
	private List<Employee> employeeList;
	private List<PigHouse> pigHouseList;
	private String imageFormat;
	
	public void initialize(URL location, ResourceBundle resources) {
		pigService = new PigService();
		selectedindexSet = new LinkedHashSet<>();
		initPigHouseTableView();
		String[] imageFormats = new String[]{
			"jpg",
    		"jpeg",
    		"png",
    		"bmp"};
		imageFormatComboBox.getItems().addAll(FXCollections.observableArrayList(imageFormats));
		imageFormatComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				imageFormat = newValue;
			}
		});
		
	}
	
     private void initEmployeeTableView() {
    	 try {
 			employeeList = pigService.getAllEmployees();
 		} catch (SQLException e) {
 			showMessage("连接数据库失败");
 		}
    	 employeeTableView.setItems(FXCollections.observableArrayList(employeeList));
    	 employeeNameCol.setCellValueFactory(new PropertyValueFactory<Employee,String>("name"));
    	 telephoneCol.setCellValueFactory(new PropertyValueFactory<Employee,String>("telephone"));
    	 employeeMemoCol.setCellValueFactory(new PropertyValueFactory<Employee,String>("memo"));
    	 selectAll(false);
    }
	
	private void initPigHouseTableView() {
		try {
			pigHouseList = pigService.getAllPigHouse();
		} catch (SQLException e1) {
			showMessage("连接数据库失败");
		}
		pigstyTableView.setVisible(false);
		pigHouseTableView.setVisible(true);
		refreshOrBack.setText("刷新");
		pigHouseTableView.setItems(FXCollections.observableArrayList(pigHouseList));
		pigHouseCol.setCellValueFactory(new PropertyValueFactory<PigHouse, String>("name"));
		numberCol.setCellValueFactory(new PropertyValueFactory<PigHouse, Integer>("number"));
		selectAll(false);
		pigstyCol.setCellFactory((col) -> {
            TableCell<PigHouse, String> cell = new TableCell<PigHouse, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    if (!empty) {
                        Button see = new Button("查看");
                        this.setGraphic(see);
                        see.setOnMouseClicked((me) -> {
                        	initPigstyTableView(pigHouseList.get(this.getIndex()));
                        	selectedindexSet.clear();
                        });
                    }
                }
            };
            return cell;
        });
	}
	
	private void initPigstyTableView(PigHouse pigHouse) {
		try {
			pigstyList = pigService.getAllPigstiesByPigHouse(pigHouse);
		} catch (SQLException e) {
			showMessage("连接数据库失败");
		}
		pigstyTableView.setVisible(true);
		pigHouseTableView.setVisible(false);
		refreshOrBack.setText("返回猪舍");
		pigstyTableView.setItems(FXCollections.observableArrayList(pigstyList));
		pigHouseCols.setCellValueFactory(new PropertyValueFactory<Pigsty, String>("pigHouse"));
		pigstyNumCol.setCellValueFactory(new PropertyValueFactory<Pigsty, Integer>("number"));
		selectAll(false);
	}

	
	private void selectAll(boolean isSelectAll ) {
		if(employeeTab.isSelected()) {
			employeeChoiceCol.setCellFactory((col) -> {
	            TableCell<Employee, Boolean> cell = new TableCell<Employee, Boolean>() {
	                @Override
	                public void updateItem(Boolean item, boolean empty) {
	                    if (!empty) {
	                        CheckBox checkBox = new CheckBox();
	                        this.setGraphic(checkBox);
	                        checkBox.selectedProperty().addListener((obVal, oldVal, newVal) -> {
	                            if (newVal) {
	                                selectedindexSet.add(this.getIndex());
	                  
	                            }
	                            if(oldVal) {
	                            	selectedindexSet.remove(new Integer(this.getIndex()));
	                
	                            }
	                        });
	                        checkBox.setSelected(isSelectAll);
	                    }
	                }
	            };
	            return cell;
	        });
		}else {
			if(pigHouseTableView.isVisible()) {
				pigHousechoiceCol.setCellFactory((col) -> {
		            TableCell<PigHouse, Boolean> cell = new TableCell<PigHouse, Boolean>() {
		                @Override
		                public void updateItem(Boolean item, boolean empty) {
		                    if (!empty) {
		                        CheckBox checkBox = new CheckBox();
		                        this.setGraphic(checkBox);
		                        checkBox.selectedProperty().addListener((obVal, oldVal, newVal) -> {
		                            if (newVal) {
		                                selectedindexSet.add(this.getIndex());
		                      
		                            }
		                            if(oldVal) {
		                            	selectedindexSet.remove(new Integer(this.getIndex()));
		                      
		                            }
		                        });
		                        checkBox.setSelected(isSelectAll);
		                    }
		                }
		            };
		            return cell;
		        });
			}else {
				pigstyChoiceCol.setCellFactory((col) -> {
		            TableCell<Pigsty, Boolean> cell = new TableCell<Pigsty, Boolean>() {
		                @Override
		                public void updateItem(Boolean item, boolean empty) {
		                    if (!empty) {
		                        CheckBox checkBox = new CheckBox();
		                        this.setGraphic(checkBox);
		                        checkBox.selectedProperty().addListener((obVal, oldVal, newVal) -> {
		                        	if (newVal) {
		                                selectedindexSet.add(this.getIndex());
		              
		                            }
		                            if(oldVal) {
		                            	selectedindexSet.remove(new Integer(this.getIndex()));
		                  
		                            }
		                        });
		                        checkBox.setSelected(isSelectAll);
		                    }
		                }
		            };
		            return cell;
		        });
			}
		}
		
	}
	
	//刷新
	@FXML 
	public void onClickPigHouse(ActionEvent event) {
		if(employeeTab.isSelected()) {
			initEmployeeTableView();
		}else {
			initPigHouseTableView();
		}
		selectedindexSet.clear();
	}



	@FXML 
	public void onClickSelectNone(ActionEvent event) {
		selectAll(false);
		selectedindexSet.clear();
	}



	@FXML 
	public void onClickSelectAll(ActionEvent event) {
		selectAll(true);
		if(employeeTab.isSelected()) {
			for(int i=0;i<employeeList.size();i++) {
				selectedindexSet.add(i);
			}
		}else {
			if(pigHouseTableView.isVisible()) {
				for(int i=0;i<pigHouseList.size();i++) {
					selectedindexSet.add(i);
				}
			}else {
				for(int i=0;i<pigstyList.size();i++) {
					selectedindexSet.add(i);
				}
			}
		}
		
	}





	private File historyChooseImageDir = null;
	private File logoFile = null;
	@FXML 
	public void onClickChooseLogo(ActionEvent event) throws FileNotFoundException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("选择Logo");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("图片", "*.jpg","*.jpeg","*.bmp","*.png"));
		fileChooser.setInitialDirectory(historyChooseImageDir);
		logoFile = fileChooser.showOpenDialog(new Stage());
		if(logoFile != null) {
			historyChooseImageDir = new File(logoFile.getAbsolutePath().substring(0, logoFile.getAbsolutePath().lastIndexOf('\\')));
			logoImageView.setFitWidth(170);
			logoImageView.setFitHeight(170);
			logoImageView.setImage(new Image(new FileInputStream(logoFile)));
			
		}else {
			logoImageView.setImage(null);
		}
	}



	@FXML 
	public void onClickClearLogo(ActionEvent event) {
		logoImageView.setImage(null);
		logoFile = null;
	}


    private void formAction(boolean codeFormat) {
    	if(targetFileTextFiled.getText().length()==0) {
			showMessage("一/二维码存放文件夹不能为空");
			return ;
		}
		if(selectedindexSet.size()==0) {
			showMessage("未选择猪舍或者猪栏或者员工");
			return ;
		}
		if(imageFormat==null) {
			showMessage("请选择生成图片格式");
			return ;
		}
		String type;
		if(codeFormat) {
			type="二维码";
		}else {
			type="条形码";
		}
    	
		for(int index : selectedindexSet) {
			String bottomString;
			String content;
			String targetPath ;
			String targertDir = null;
	    	if(employeeTab.isSelected()) {
	    		Employee employee = employeeList.get(index);
	    		bottomString = employee.toString();
	    		content = "employee"+employee.getTelephone();
	    		targertDir =  targetFileTextFiled.getText()+"\\"+"员工"+type;
	    		
	    	}else {
	    		if(pigHouseTableView.isVisible()) {
					PigHouse pigHouse = pigHouseList.get(index);
				    bottomString = pigHouse.toString();
					content = "pigHouse"+pigHouse.getId();
					targertDir =  targetFileTextFiled.getText()+"\\"+bottomString;
				}else {
					Pigsty pigsty = pigstyList.get(index);
					bottomString = pigsty.toString();
					content = "pigsty"+pigsty.getId();
					targertDir =  targetFileTextFiled.getText()+"\\"+pigsty.getPigHouse()+"\\"+"猪栏"+type;
					
				}
	    	}
	    	File targertDirFile = new File(targertDir);
	    	if(!targertDirFile.exists()){
	    		targertDirFile.mkdirs();
	    	}
	    	targetPath =  targertDir+"\\"+bottomString+type+"."+imageFormat;
			try {
				CoderUtil.writeImage(content, logoFile, bottomString,new File(targetPath), imageFormat, codeFormat);
			} catch (Exception e) {
				showMessage("生成维码图片失败");
			}
		}
		showMessage("维码图片生成完毕");
		selectAll(false);
		selectedindexSet.clear();
    }
    
	@FXML 
	private void formQRCode(ActionEvent event) {
		formAction(true);
	}



	@FXML 
	public void formBarcode(ActionEvent event) {
		formAction(false);
	}


	private File historychooseDirect;
	@FXML 
	public void onClickChooseFile(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
    	directoryChooser.setTitle("选择图片存放目录");
    	directoryChooser.setInitialDirectory(historychooseDirect);
    	historychooseDirect = directoryChooser.showDialog(new Stage());
    	if(historychooseDirect!=null) {
    		targetFileTextFiled.setText(historychooseDirect.getAbsolutePath());
    	}
	}


	private void showMessage(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
    	alert.show();
	}



	@FXML 
	public void onSelectEmployeeTab(Event event) {
	
		initEmployeeTableView();
	}

	@FXML 
	public void onSelectPigTab(Event event) {
		if(pigService==null) {
			return;
		}
		initPigHouseTableView();
	}



	
	
}
