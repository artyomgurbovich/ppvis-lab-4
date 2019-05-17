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
		controller = new Controller();
		mainWindow = new MainWindow(controller);
	}
	
	public int showLoadDialog() {
		controller.clear();
		mainWindow.setModeText(Controller.LOAD_TEXT);
		mainWindow.start();
		return controller.checkFileOnLoadMode();
	}
	
	public int showSaveDialog() {
		controller.clear();
		mainWindow.setModeText(Controller.SAVE_TEXT);
		mainWindow.start();
		return controller.checkFileOnSaveMode();
	}
	
	public File getSelectedFile() {
		return controller.getSelectedFile();
	}
	
	public void setFilters(String[] filters) {
		mainWindow.setFilters(filters);
	}
}
