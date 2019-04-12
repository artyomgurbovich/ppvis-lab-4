package lab;

import lab.controller.Controller;
import lab.view.MainWindow;

public class MyFileChooser {
	
	public static final int APPROVE = 0;
	public static final int ERROR = 1;
	
	private Controller controller;
	private MainWindow mainWindow;
	private String[] filters = MainWindow.DEFAULT_FILTER;
	
	private void update() {
		mainWindow = new MainWindow();
		controller = new Controller(mainWindow);
		controller.setFilters(filters);
		mainWindow.start();
	}
	
	public int showLoadDialog() {
		update();
		return controller.setMode(0);
	}
	
	public int showSaveDialog() {
		update();
		return controller.setMode(1);
	}
	
	public void setFilters(String[] filters) {
		this.filters = filters;
	}
}
