package lab4.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;

import lab4.MyFileChooser;
import lab4.model.Drive;
import lab4.model.FileDescription;
import lab4.view.MainWindow;

public class Controller {
	
	private final int DRIVE_LETTER_OFFSET = 2;
	private final String LOAD_TEXT = "Load";
	private final String SAVE_TEXT = "Save";
	public static final int TABLE_MODE_INDEX = 0;
	public static final int LIST_MODE_INDEX = 1;
	public static final int ICONS_MODE_INDEX = 2;
	private final String FOLDER_TYPE = "Folder";
	
	private MainWindow mainWindow;
	private List<Drive> drives;
	private List<FileDescription> filesDescriptions;
	private String currentCatalogPath;
	private File selectedFile;
	private String currentFilter = MainWindow.ALL_FILES;
	
	public Controller(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		drives = getDrives();
		mainWindow.updateDrives(drives);
		mainWindow.setListSelectionListener(listSelectionListener);
		mainWindow.setTreeSelectionListener(treeSelectionListener);
		mainWindow.setHomeListner(homeListener);
		mainWindow.setTableModeListner(tableModeListener);
		mainWindow.setListModeListner(listModeListener);
		mainWindow.setIconsModeListner(iconsModeListener);
		mainWindow.setCatalogTableListener(catalogTableListener);
		mainWindow.setCatalogListListener(catalogListListener);
		mainWindow.setCatalogIconsListener(catalogListListener);
		mainWindow.setFileButtonListener(fileButtonListener);
		mainWindow.setFiltersBoxListener(filtersBoxListener);
		mainWindow.setCatalogTableMouseListener(tableMouseListener);
		mainWindow.setCatalogListMouseListener(listMouseListener);
		mainWindow.setCatalogIconsMouseListener(listMouseListener);
	}
	
	public void clear() {
		selectedFile = null;
	}
	
	public void setLoadModeText() {
		mainWindow.setModeText(LOAD_TEXT);
	}
	
	public void setSaveModeText() {
		mainWindow.setModeText(SAVE_TEXT);
	}
	
	public int checkFileOnLoadMode() {
		if (selectedFile != null) {
			if (selectedFile.exists()) {
				return MyFileChooser.APPROVE_OPTION;
			} else {
				JOptionPane.showMessageDialog(new JFrame(), "File not exist!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		return MyFileChooser.ERROR_OPTION;
	}
	
	public int checkFileOnSaveMode() {
		if (selectedFile != null) {
			return MyFileChooser.APPROVE_OPTION;
		} else {
			return MyFileChooser.ERROR_OPTION;
		}
	}
	
	public File getSelectedFile() {
		return selectedFile;
	}
    
	public void setFilters(String[] filters) {
		mainWindow.setFilters(filters);
	}
	
	private List<Drive> getDrives() {
		File[] rootDrive = File.listRoots();
		List<Drive> drives = new ArrayList<Drive>();
		String driveSystemDisplayName;
		String driveName;
		String driveLetter;
		FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		for(File drive: rootDrive) {
			driveSystemDisplayName = fileSystemView.getSystemDisplayName(drive);
			if (!driveSystemDisplayName.equals("")) {
				driveName = driveSystemDisplayName.substring(0, driveSystemDisplayName.lastIndexOf(' '));
				driveLetter = driveSystemDisplayName.substring(driveSystemDisplayName.lastIndexOf(' ') + DRIVE_LETTER_OFFSET, driveSystemDisplayName.length() - DRIVE_LETTER_OFFSET);
				drives.add(new Drive(driveName, driveLetter));
			}
		}
		return drives;
	}
	
	private ListSelectionListener catalogListListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
        	if (!e.getValueIsAdjusting()) {
                @SuppressWarnings("unchecked")
				JList<FileDescription> source = (JList<FileDescription>) e.getSource();
                FileDescription fileDescription = source.getSelectedValue();
                if (fileDescription != null) {
                    mainWindow.setFileFieldText(fileDescription.getName());
                }
            }
        }
	};
	
	private ListSelectionListener catalogTableListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
        	if (!e.getValueIsAdjusting()) {
                ListSelectionModel source = (ListSelectionModel) e.getSource();
                if (!source.isSelectionEmpty()) {
                	int selectedRow = source.getMinSelectionIndex();
                	FileDescription fileDescription = filesDescriptions.get(selectedRow);
                	if (!filesDescriptions.get(selectedRow).isFolder()) {
                		mainWindow.setFileFieldText(fileDescription.getName());
                	}
                }
            }
        }
	};
	
	private ActionListener filtersBoxListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			@SuppressWarnings("unchecked")
			JComboBox<String> box = (JComboBox<String>)e.getSource();
			currentFilter = (String)box.getSelectedItem();
			if (filesDescriptions != null) {
				filesDescriptions = getFilesDescriptions(currentCatalogPath);
				mainWindow.updateCurrentCatalog(filesDescriptions);
			}
		}
	};
	
	private ActionListener fileButtonListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			selectedFile = new File(currentCatalogPath + mainWindow.getFileFieldText());
			if (selectedFile.isDirectory()) {
				selectedFile = null;
				JOptionPane.showMessageDialog(new JFrame(), "Folder selected!", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				mainWindow.dispose();
			}
		}
	};
	
	private ActionListener homeListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
	    	mainWindow.openHomeDirectory();
		}
	};
	
	private ActionListener tableModeListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			mainWindow.changeMode(TABLE_MODE_INDEX);
		}
	};
	
	private ActionListener listModeListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			mainWindow.changeMode(LIST_MODE_INDEX);
		}
	};
	
	private ActionListener iconsModeListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			mainWindow.changeMode(ICONS_MODE_INDEX);
		}
	};
	
	private ListSelectionListener listSelectionListener = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel listSelectionModel = (ListSelectionModel)e.getSource();
			if (!e.getValueIsAdjusting()) {
				int currentDriveIndex = listSelectionModel.getMinSelectionIndex();
		        File fileRoot = new File(drives.get(currentDriveIndex).getLetter() + ":");
		        mainWindow.updateCatalogs(fileRoot);
			}
		}
	};
	
	private TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {  
	    public void valueChanged(TreeSelectionEvent e) { 
	    	currentCatalogPath = "";
	    	TreePath treePath = e.getPath();
	    	if (treePath != null) {
	    		Object[] elements = treePath.getPath();
	            for (Object element: elements) {
	            	currentCatalogPath += element + "\\";
	            }
	    	}
	    	filesDescriptions = getFilesDescriptions(currentCatalogPath);
	    	mainWindow.setCurrentPathText(currentCatalogPath);
	    	mainWindow.updateCurrentCatalog(filesDescriptions);
	    	mainWindow.updateCatalogChild(new File(currentCatalogPath));
	    }
	};
	
	private List<FileDescription> getFilesDescriptions(String path) {
		List<FileDescription> result = new ArrayList<FileDescription>();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		FileDescription fileDescription;
		if (listOfFiles != null) {
			for (File file: listOfFiles) {
				fileDescription = new FileDescription(file.getName(),
						                              getFileExtension(file),
						                              getFileSize(file),
						                              getLastModifiedDate(file),
						                              file.isDirectory());
				if (currentFilter.equals(MainWindow.ALL_FILES)) {
					result.add(fileDescription);
				} else if (currentFilter.equals(fileDescription.getExtension())) {
					result.add(fileDescription);
				}
			}
		}
		return result;
	}
	
	private String getFileExtension(File file) {
		if (file.isDirectory()) {
			return FOLDER_TYPE;
		} else {
			String extension = "";
			String fileName = file.getName();
			int index = fileName.lastIndexOf('.');
			extension = fileName.substring(index+1);
			return extension;
		}
	}
	
	private long getFileSize(File file) {
		return file.length() / 1024;
	}
	
	private String getLastModifiedDate(File file) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.mm.yyyy");
		return simpleDateFormat.format(file.lastModified());
	}
	
	MouseListener listMouseListener = new MouseAdapter() {
	    public void mousePressed(MouseEvent mouseEvent) {
	    	@SuppressWarnings("unchecked")
			JList<FileDescription> list = (JList<FileDescription>) mouseEvent.getSource();
        	FileDescription selectedFile = list.getSelectedValue();
	        if (mouseEvent.getClickCount() == 2 && selectedFile.isFolder()) {
	        	mainWindow.openFolder(selectedFile.getName());
	        }
	    }
	};
	
	MouseListener tableMouseListener = new MouseAdapter() {
	    public void mousePressed(MouseEvent mouseEvent) {
	        JTable table =(JTable) mouseEvent.getSource();
	        Point point = mouseEvent.getPoint();
	        int row = table.rowAtPoint(point);
	        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
	        	String selectedFileType = (String) table.getValueAt(row, 1);
	        	if (selectedFileType.equals(FOLDER_TYPE)) {
	        		mainWindow.openFolder((String) table.getValueAt(row, 0));
	        	}
	        }
	    }
	};
}
