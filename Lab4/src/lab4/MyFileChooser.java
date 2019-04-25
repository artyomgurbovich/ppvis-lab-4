package lab4;

import java.io.File;

import lab4.controller.Controller;
import lab4.view.MainWindow;

public class MyFileChooser {
	
	public static final int APPROVE_OPTION = 0;
	public static final int ERROR_OPTION = 1;
	
	private Controller controller;
	private MainWindow mainWindow;
	
	public MyFileChooser() {
		mainWindow = new MainWindow();
		controller = new Controller(mainWindow);
	}
	
	public int showLoadDialog() {
		controller.clear();
		controller.setLoadModeText();
		mainWindow.start();
		return controller.checkFileOnLoadMode();
	}
	
	public int showSaveDialog() {
		controller.clear();
		controller.setSaveModeText();
		mainWindow.start();
		return controller.checkFileOnSaveMode();
	}
	
	public File getSelectedFile() {
		return controller.getSelectedFile();
	}
	
	public void setFilters(String[] filters) {
		controller.setFilters(filters);
	}
}
