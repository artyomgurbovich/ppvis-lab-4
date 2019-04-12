package lab.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;
import lab.model.Drive;
import lab.model.FileDescription;
import lab.view.MainWindow;

public class Controller {
	
	private final String HOME_DIRECTORY_PATH = "C:\\Windows\\";
	public static final int TABLE_MODE_INDEX = 0;
	public static final int LIST_MODE_INDEX = 1;
	public static final int ICONS_MODE_INDEX = 2;
	private final int SLEEP_TIME = 10;
	
	private final String LOAD_TEXT = "Load";
	private final String SAVE_TEXT = "Save";
	private final int LOAD_INDEX = 0;
	private final int SAVE_INDEX = 1;
	
	private MainWindow mainWindow;
	private List<Drive> drives;
	private List<FileDescription> filesDescriptions;
	private DefaultMutableTreeNode root;
	private String currentCatalogPath;
	private File selectedFile;
	private boolean isPressed;
	private boolean isWindowClosed;
	private String currentFilter = MainWindow.ALL_FILES;
	
	public Controller(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		drives = getDrives();
		mainWindow.updateDrives(drives);
		mainWindow.setListSelectionListener(listSelectionListener);
		mainWindow.setTreeSelectionListener(treeSelectionListener);
		mainWindow.setHomeButtonListner(homeButtonListener);
		mainWindow.setTableModeButtonListner(tableModeButtonListener);
		mainWindow.setListModeButtonListner(listModeButtonListener);
		mainWindow.setIconsModeButtonListner(iconsModeButtonListener);
		mainWindow.setCatalogTableListener(catalogTableListener);
		mainWindow.setCatalogListListener(catalogListListener);
		mainWindow.setCatalogIconsListener(catalogListListener);
		mainWindow.setFileButtonListener(fileButtonListener);
		mainWindow.setWindowListener(windowAdapter);
		mainWindow.setFiltersBoxListener(filtersBoxListener);
	}
	
	public int setMode(int modeIndex) {
		if (modeIndex == LOAD_INDEX) {
			mainWindow.setModeText(LOAD_TEXT);
		} else if (modeIndex == SAVE_INDEX) {
			mainWindow.setModeText(SAVE_TEXT);
		}
		while (!isPressed && !isWindowClosed) {
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		mainWindow.dispose();
		if (selectedFile != null) {
			if (modeIndex == LOAD_INDEX &&  selectedFile.exists()) {
				return 0;
			} else if (modeIndex == SAVE_INDEX) { 
				return 0;
			}
		}
		return 1;
	}
	
	public File getSelectedFile() {
		return selectedFile;
	}
	
	private WindowAdapter windowAdapter = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
        	isWindowClosed = true;
        }
    };
    
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
				driveName = driveSystemDisplayName.split(" ")[0];
				driveLetter = driveSystemDisplayName.split(" ")[1].replace("(", "").replace(":)", "");
				drives.add(new Drive(driveName, driveLetter));
			}
		}
		return drives;
	}
	
	private ListSelectionListener catalogListListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
        	if (!e.getValueIsAdjusting()){
                @SuppressWarnings("unchecked")
				JList<FileDescription> source = (JList<FileDescription>) e.getSource();
                mainWindow.setFileFieldText(source.getSelectedValue().getName());
            }
        }
	};
	
	private ListSelectionListener catalogTableListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
        	if (!e.getValueIsAdjusting()){
                ListSelectionModel source = (ListSelectionModel) e.getSource();
                if (!source.isSelectionEmpty()) {
                	int selectedRow = source.getMinSelectionIndex();
                	mainWindow.setFileFieldText(filesDescriptions.get(selectedRow).getName());
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
			isPressed = true;
		}
	};
	
	private ActionListener homeButtonListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
	    	filesDescriptions = getFilesDescriptions(HOME_DIRECTORY_PATH);
			mainWindow.setCurrentPathText(HOME_DIRECTORY_PATH);
	    	mainWindow.updateCurrentCatalog(filesDescriptions);
		}
	};
	
	private ActionListener tableModeButtonListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			mainWindow.changeMode(TABLE_MODE_INDEX);
		}
	};
	
	private ActionListener listModeButtonListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			mainWindow.changeMode(LIST_MODE_INDEX);
		}
	};
	
	private ActionListener iconsModeButtonListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			mainWindow.changeMode(ICONS_MODE_INDEX);
		}
	};
	
	private ListSelectionListener listSelectionListener = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel listSelectionModel = (ListSelectionModel)e.getSource();
			if (!e.getValueIsAdjusting()) {
		        File fileRoot = new File(drives.get(listSelectionModel.getMinSelectionIndex()).getLetter() + ":");
		        System.out.println(fileRoot.getName());
		        root = new DefaultMutableTreeNode(fileRoot, true);
		        mainWindow.updateCatalogs(root);
				ChildNodeManager childNodeManager = new ChildNodeManager(root, fileRoot);
		        new Thread(childNodeManager).start();
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
			return "Folder";
		} else {
			String extension = "";
			String fileName = file.getName();
			int i = fileName.lastIndexOf('.');
			extension = fileName.substring(i+1);
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
	
	class ChildNodeManager implements Runnable {
	    private DefaultMutableTreeNode node;
	    private File fileRoot;

	    public ChildNodeManager(DefaultMutableTreeNode node, File fileRoot) {
	        this.fileRoot = fileRoot;
	        this.node = node;
	    }

	    @Override
	    public void run() {
	    	getCatalog(node, fileRoot);
	    }

		private void getCatalog(DefaultMutableTreeNode node, File file) {
			file = new File(file.getPath() + "\\");
			File filesList[] = file.listFiles();
			if (filesList != null) {
	            for(File child: filesList)
	            {
	                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child.getName());
	                if(child.isDirectory())
	                {
	                	node.add(childNode);
	                    getCatalog(childNode, child);
	                }
	            }
			}
	    }
	}
}
