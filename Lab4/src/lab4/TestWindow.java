package lab4;

import java.awt.event.*;
import java.io.File;

import javax.swing.*;

public class TestWindow {
	JFrame frame;
	
	public TestWindow() {
		frame = new JFrame("TEST_WINDOW");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void start() {
		JButton buttonLoad = new JButton("Load");
		JButton buttonSave = new JButton("Save");
		
		buttonLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//JFileChooser fileload = new JFileChooser();
				//int ret = fileload.showDialog(null, "Open file");                
				//if (ret == JFileChooser.APPROVE_OPTION) {
				//    File file = fileload.getSelectedFile();
				//	  model.setRecords(fileManager.read(file));
				//}
				
				MyFileChooser fileload = new MyFileChooser();
				int ret = fileload.showLoadDialog();
				if (ret == MyFileChooser.APPROVE_OPTION) {
					File file = fileload.getSelectedFile();
					System.out.println(file.getName());
				}
			}
		});
		
		buttonSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//JFileChooser filesave = new JFileChooser();
				//filesave.setDialogTitle("Save file");
				//int ret = filesave.showSaveDialog(null);               
				//if (ret == JFileChooser.APPROVE_OPTION) {
				//    File file = filesave.getSelectedFile();
				//    fileManager.write(file, getRecords());
				//}
				
				MyFileChooser myFileChooser = new MyFileChooser();
				int res = myFileChooser.showLoadDialog();
				System.out.println(res);
			}
		});
		
		JPanel mainPanel = new JPanel();
		mainPanel.add(buttonLoad);
		mainPanel.add(buttonSave);
		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true);
	}
}
