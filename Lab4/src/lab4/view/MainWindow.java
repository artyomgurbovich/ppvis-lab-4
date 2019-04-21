package lab4.view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import lab4.controller.Controller;
import lab4.model.Drive;
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
	
	public MainWindow() {
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
	
	public void setFiltersBoxListener(ActionListener filtersBoxListener) {
		filtersBox.addActionListener(filtersBoxListener);
	}
	
	public void setFilters(String[] filters) {
		filtersBox.removeAllItems();
		for (String filter: filters) {
			filtersBox.addItem(filter);
		}
	}
	
	public void setFileButtonListener(ActionListener fileButtonListener) {
		fileButton.addActionListener(fileButtonListener);
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
	
	public void setCatalogTableListener(ListSelectionListener catalogTableListener) {
		catalogTable.getSelectionModel().addListSelectionListener(catalogTableListener);
	}
	
	public void setCatalogListListener(ListSelectionListener catalogListListener) {
		catalogListPanel.addListSelectionListener(catalogListListener);
	}
	
	public void setCatalogIconsListener(ListSelectionListener catalogIconsListener) {
		catalogIconsPanel.addListSelectionListener(catalogIconsListener);
	}
	
	public void setTableModeListner(ActionListener tableModeListener) {
		tableModeButton.addActionListener(tableModeListener);
		tableMode.addActionListener(tableModeListener);
	}
	
	public void setListModeListner(ActionListener listModeListener) {
		listModeButton.addActionListener(listModeListener);
		listMode.addActionListener(listModeListener);
	}
	
	public void setIconsModeListner(ActionListener iconsModeListener) {
		iconsModeButton.addActionListener(iconsModeListener);
		iconsMode.addActionListener(iconsModeListener);
	}
	
	public void setCurrentPathText(String currentPath) {
		currentPathLabel.setText(CURRENT_PATH_LABEL_TEXT + currentPath);
	}
	
	public void updateDrives(List<Drive> drives) {
		drivesTableModel.updateData(drives);
	}
	
	public void updateCatalogs(DefaultMutableTreeNode root) {
		catalogsPanel.updateRootNode(root);
	}
	
	public void updateCatalogsNode(DefaultMutableTreeNode node) {
		catalogsPanel.updateChildNode(node);
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
	
	public void setListSelectionListener(ListSelectionListener listSelectionListener) {
		listSelectionModel.addListSelectionListener(listSelectionListener);
	}
	
	public void setTreeSelectionListener(TreeSelectionListener treeSelectionListener) {
		catalogsPanel.setTreeSelectionListener(treeSelectionListener);
	}
}
