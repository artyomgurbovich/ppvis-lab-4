package lab4.controller;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import lab4.MyFileChooser;
import lab4.model.Drive;
import lab4.model.FileDescription;
import lab4.view.MainWindow;

public class Controller {
	
	private final int DRIVE_LETTER_OFFSET = 2;
	public static final String LOAD_TEXT = "Load";
	public static final String SAVE_TEXT = "Save";
	public static final int TABLE_MODE_INDEX = 0;
	public static final int LIST_MODE_INDEX = 1;
	public static final int ICONS_MODE_INDEX = 2;
	public static final String FOLDER_TYPE = "Folder";
	
	private List<Drive> drives;
	private List<FileDescription> filesDescriptions;
	private String currentCatalogPath;
	private File selectedFile;
	private String currentFilter = MainWindow.ALL_FILES;
	
	public Controller() {
		drives = loadDrives();
	}
	
	public List<FileDescription> getFilesDescriptions() {
		return filesDescriptions;
	}
	
	public void setFilesDescriptions(List<FileDescription> filesDescriptions) {
		this.filesDescriptions = filesDescriptions;
	}
	
	public List<Drive> getDrives() {
		return drives;
	}
	
	public String getCurrentCatalogPath() {
		return currentCatalogPath;
	}
	
	public void clear() {
		selectedFile = null;
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
	
	public void setSelectedFile(File file) {
		selectedFile = file;
	}
	
	private List<Drive> loadDrives() {
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
	
	public List<FileDescription> loadFilesDescriptions(String path) {
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

	public void setCurrentCatalogPath(String currentCatalogPath) {
		this.currentCatalogPath = currentCatalogPath;
	}
}
