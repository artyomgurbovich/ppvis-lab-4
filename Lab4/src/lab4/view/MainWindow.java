package lab4.view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import lab4.controller.Controller;
import lab4.model.FileDescription;
import lab4.view.icons.CatalogIconsRenderer;
import lab4.view.list.CatalogListPanel;
import lab4.view.list.CatalogListRenderer;
import lab4.view.table.CatalogTableModel;
import lab4.view.table.DrivesTableModel;

public class MainWindow {
	
	private final int FRAME_SIZE_X = 1000;
	private final int FRAME_SIZE_Y = 600;
	private final int LEFT_PANEL_SIZE_X = 300;
	private final int RIGHT_PANEL_SIZE_X = 700;
	private final int DRIVE_TABLEL_SIZE_Y = 100;
	private final int TEXT_FIELD_SIZE_X = 400;
	private final int TEXT_FIELD_SIZE_Y = 30;
	private final int HORIZONTAL_STRUT_SIZE = 100;
	private final String CURRENT_PATH_LABEL_TEXT = "Current path: ";
	public static final String ALL_FILES = "All files";
	public static String[] DEFAULT_FILTER = {ALL_FILES};
	
	private CatalogsPanel catalogsPanel;
	private DrivesTableModel drivesTableModel;
	private CatalogTableModel catalogTableModel;
	private CatalogListPanel catalogListPanel;
	private CatalogListPanel catalogIconsPanel;
	private JTable drivesTable;
	private JTable catalogTable;
	private ListSelectionModel listSelectionModel;
	private JButton homeButton;
	private JButton fileButton;
	private JLabel currentPathLabel;
	private JTextField fileField;
	private JButton tableModeButton;
	private JButton listModeButton;
	private JButton iconsModeButton;
	private JScrollPane tableScrollPane;
	private JScrollPane listScrollPane;
	private JScrollPane iconsScrollPane;
	private JComboBox<String> filtersBox;
	private JDialog dialog;
	
	private JMenuItem tableMode;
	private JMenuItem listMode;
	private JMenuItem iconsMode;
	private JMenuItem home;
	
	/////
	
	private Controller controller;
	
	public MainWindow(Controller controller) {
		catalogsPanel = new CatalogsPanel();
		drivesTableModel = new DrivesTableModel();
		drivesTable = new JTable(drivesTableModel);
		catalogTableModel = new CatalogTableModel();
		catalogTable = new JTable(catalogTableModel);
		catalogTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listSelectionModel = drivesTable.getSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		homeButton = new JButton("Home directory");
		currentPathLabel = new JLabel(CURRENT_PATH_LABEL_TEXT);
		fileButton = new JButton("Save/Load");
		fileField = new JTextField();
		tableModeButton = new JButton("Table mode");
		listModeButton = new JButton("List mode");
		iconsModeButton = new JButton("Icons mode");
		catalogListPanel = new CatalogListPanel(new CatalogListRenderer(), JList.VERTICAL);
		catalogIconsPanel = new CatalogListPanel(new CatalogIconsRenderer(), JList.VERTICAL_WRAP);
		tableScrollPane = new JScrollPane(catalogTable);
		listScrollPane = new JScrollPane(catalogListPanel);
		iconsScrollPane = new JScrollPane(catalogIconsPanel);
		filtersBox = new JComboBox<String>(DEFAULT_FILTER);
		
		tableMode = new JMenuItem("Table mode");
		listMode = new JMenuItem("List mode");
		iconsMode = new JMenuItem("Icons mode");
		home = new JMenuItem("Home catalog");
		
		dialog = new JDialog();
		dialog.setModal(true);
		
		/////
		
		this.controller = controller;
		
		catalogTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	        public void valueChanged(ListSelectionEvent e) {
	        	if (!e.getValueIsAdjusting()) {
	                ListSelectionModel source = (ListSelectionModel) e.getSource();
	                if (!source.isSelectionEmpty()) {
	                	int selectedRow = source.getMinSelectionIndex();
	                	FileDescription fileDescription = controller.getFilesDescriptions().get(selectedRow);
	                	if (!controller.getFilesDescriptions().get(selectedRow).isFolder()) {
	                		setFileFieldText(fileDescription.getName());
	                	}
	                }
	            }
	        }
		});
		
		catalogTable.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        JTable table =(JTable) mouseEvent.getSource();
		        Point point = mouseEvent.getPoint();
		        int row = table.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
		        	String selectedFileType = (String) table.getValueAt(row, 1);
		        	if (selectedFileType.equals(Controller.FOLDER_TYPE)) {
		        		openFolder((String) table.getValueAt(row, 0));
		        	}
		        }
		    }
		});
		
		MouseAdapter listMouseListener = new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		    	@SuppressWarnings("unchecked")
				JList<FileDescription> list = (JList<FileDescription>) mouseEvent.getSource();
	        	FileDescription selectedFile = list.getSelectedValue();
		        if (mouseEvent.getClickCount() == 2 && selectedFile.isFolder()) {
		        	openFolder(selectedFile.getName());
		        }
		    }
		};
		
		catalogListPanel.addListMouseListener(listMouseListener);
		catalogIconsPanel.addListMouseListener(listMouseListener);
		
		ListSelectionListener listSelectionListener = new ListSelectionListener() {
	        public void valueChanged(ListSelectionEvent e) {
	        	if (!e.getValueIsAdjusting()) {
	                @SuppressWarnings("unchecked")
					JList<FileDescription> source = (JList<FileDescription>) e.getSource();
	                FileDescription fileDescription = source.getSelectedValue();
	                if (fileDescription != null) {
	                    setFileFieldText(fileDescription.getName());
	                }
	            }
	        }
		};
		
		catalogListPanel.addListSelectionListener(listSelectionListener);
		catalogIconsPanel.addListSelectionListener(listSelectionListener);

		listSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel listSelectionModel = (ListSelectionModel)e.getSource();
				if (!e.getValueIsAdjusting()) {
					int currentDriveIndex = listSelectionModel.getMinSelectionIndex();
			        File fileRoot = new File(controller.getDrives().get(currentDriveIndex).getLetter() + ":");
			        updateCatalogs(fileRoot);
				}
			}
		});
		
		catalogsPanel.setTreeSelectionListener(new TreeSelectionListener() {  
		    public void valueChanged(TreeSelectionEvent e) { 
		    	String currentCatalogPath = "";
		    	TreePath treePath = e.getPath();
		    	if (treePath != null) {
		    		Object[] elements = treePath.getPath();
		            for (Object element: elements) {
		            	currentCatalogPath += element + "\\";
		            }
		    	}
		    	controller.setCurrentCatalogPath(currentCatalogPath);
		    	setCurrentPathText(currentCatalogPath);
		    	controller.setFilesDescriptions(controller.loadFilesDescriptions(currentCatalogPath));
		    	updateCurrentCatalog(controller.getFilesDescriptions());
		    	updateCatalogChild(new File(currentCatalogPath));
		    }
		});
		
		fileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setSelectedFile(new File(controller.getCurrentCatalogPath() + getFileFieldText()));
				if (controller.getSelectedFile().isDirectory()) {
					controller.setSelectedFile(null);
					JOptionPane.showMessageDialog(new JFrame(), "Folder selected!", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					dispose();
				}
			}
		});
		
		filtersBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (controller.getFilesDescriptions() != null) {
					controller.setFilesDescriptions(controller.loadFilesDescriptions(controller.getCurrentCatalogPath()));
					updateCurrentCatalog(controller.getFilesDescriptions());
				}
			}
		});
		
		ActionListener listModeButtonListner = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeMode(Controller.LIST_MODE_INDEX);
			}
		};
		
		ActionListener tableModeButtonListner = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeMode(Controller.TABLE_MODE_INDEX);
			}
		};
		
		ActionListener iconsModeButtonListner = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeMode(Controller.ICONS_MODE_INDEX);
			}
		};
		
		listModeButton.addActionListener(listModeButtonListner);
		listMode.addActionListener(listModeButtonListner);
		tableModeButton.addActionListener(tableModeButtonListner);
		tableMode.addActionListener(tableModeButtonListner);	
		iconsModeButton.addActionListener(iconsModeButtonListner);
		iconsMode.addActionListener(iconsModeButtonListner);
		
		ActionListener homeButtonListner = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openHomeDirectory();
			}
		};
		
		homeButton.addActionListener(homeButtonListner);
		home.addActionListener(homeButtonListner);
	}
	
	private JMenu createMenu() {
		JMenu modeMenu = new JMenu("Menu");
		modeMenu.add(tableMode);
		modeMenu.add(listMode);
		modeMenu.add(iconsMode);
		modeMenu.add(home);
		return modeMenu;
	}
	
	public void start() {
		drivesTableModel.updateData(controller.getDrives());
		
		JMenuBar menuBar = new JMenuBar(); 
        menuBar.add(createMenu());
		
		dialog.setSize(new Dimension(FRAME_SIZE_X, FRAME_SIZE_Y));
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setMaximumSize(new Dimension(LEFT_PANEL_SIZE_X, FRAME_SIZE_Y));
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setMaximumSize(new Dimension(RIGHT_PANEL_SIZE_X, FRAME_SIZE_Y));
		
		drivesTable.setRowSelectionInterval(0, 0);
		JScrollPane drivesScrollPane = new JScrollPane(drivesTable);
		drivesScrollPane.setMaximumSize(new Dimension(LEFT_PANEL_SIZE_X, DRIVE_TABLEL_SIZE_Y));
		drivesScrollPane.setPreferredSize(new Dimension(LEFT_PANEL_SIZE_X, DRIVE_TABLEL_SIZE_Y));
		
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        
        menuPanel.add(tableModeButton);
        menuPanel.add(listModeButton);
        menuPanel.add(iconsModeButton);
        menuPanel.add(Box.createHorizontalStrut(HORIZONTAL_STRUT_SIZE));
        menuPanel.add(homeButton);
        
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.X_AXIS));
        
        fileField.setMaximumSize(new Dimension(TEXT_FIELD_SIZE_X, TEXT_FIELD_SIZE_Y));
        filtersBox.setMaximumSize(new Dimension(HORIZONTAL_STRUT_SIZE, TEXT_FIELD_SIZE_Y));
        filePanel.add(fileField);
        filePanel.add(filtersBox);
        filePanel.add(fileButton);
		
		leftPanel.add(drivesScrollPane);
		leftPanel.add(new JScrollPane(catalogsPanel));
		
		tableScrollPane.setMaximumSize(new Dimension(RIGHT_PANEL_SIZE_X, FRAME_SIZE_Y));
		tableScrollPane.setPreferredSize(new Dimension(RIGHT_PANEL_SIZE_X, FRAME_SIZE_Y));
		
		listScrollPane.setMaximumSize(new Dimension(RIGHT_PANEL_SIZE_X, FRAME_SIZE_Y));
		listScrollPane.setPreferredSize(new Dimension(RIGHT_PANEL_SIZE_X, FRAME_SIZE_Y));
		listScrollPane.setVisible(false);
		
		iconsScrollPane.setMaximumSize(new Dimension(RIGHT_PANEL_SIZE_X, FRAME_SIZE_Y));
		iconsScrollPane.setPreferredSize(new Dimension(RIGHT_PANEL_SIZE_X, FRAME_SIZE_Y));
		iconsScrollPane.setVisible(false);
		
		rightPanel.add(currentPathLabel);
		rightPanel.add(tableScrollPane);
		rightPanel.add(listScrollPane);
		rightPanel.add(iconsScrollPane);
		rightPanel.add(menuPanel);
		rightPanel.add(filePanel);
		
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
        dialog.setJMenuBar(menuBar);
		dialog.add(mainPanel);
        dialog.setVisible(true);
	}
	
	public void dispose() {
		dialog.dispose();
	}

	public void setFilters(String[] filters) {
		filtersBox.removeAllItems();
		for (String filter: filters) {
			filtersBox.addItem(filter);
		}
	}
	
	public void setModeText(String modeText) {
		dialog.setTitle(modeText);
		fileButton.setText(modeText);
	}
	
	public void setFileFieldText(String text) {
		fileField.setText(text);
	}
	
	public String getFileFieldText() {
		return fileField.getText();
	}
	
	public void setCurrentPathText(String currentPath) {
		currentPathLabel.setText(CURRENT_PATH_LABEL_TEXT + currentPath);
	}
	
	public void updateCatalogs(File fileRoot) {
		catalogsPanel.updateRootNode(fileRoot);
	}
	
	public void changeMode(int modeIndex) {
		if (modeIndex == Controller.TABLE_MODE_INDEX) {
			tableScrollPane.setVisible(true);
			listScrollPane.setVisible(false);
			iconsScrollPane.setVisible(false);
		} else if (modeIndex == Controller.LIST_MODE_INDEX) {
			tableScrollPane.setVisible(false);
			listScrollPane.setVisible(true);
			iconsScrollPane.setVisible(false);
		} else if (modeIndex == Controller.ICONS_MODE_INDEX) {
			listScrollPane.setVisible(false);
			tableScrollPane.setVisible(false);
			iconsScrollPane.setVisible(true);
		}
		dialog.setVisible(true);
	}
	
	public void updateCurrentCatalog(List<FileDescription> filesDescriptions) {
		catalogTableModel.updateData(filesDescriptions);
		catalogListPanel.updateData(filesDescriptions);
		catalogIconsPanel.updateData(filesDescriptions);
	}
	
	public void setHomeListner(ActionListener homeListener) {
		homeButton.addActionListener(homeListener);
		home.addActionListener(homeListener);
	}
	
	public void openHomeDirectory() {
		drivesTable.setRowSelectionInterval(0, 0);
		catalogsPanel.openHomeDirectory();
	}

	public void updateCatalogChild(File childFile) {
		catalogsPanel.updateChildNode(childFile);
	}
	
	public void openFolder(String folderName) {
		catalogsPanel.openFolder(folderName);
	}
}
