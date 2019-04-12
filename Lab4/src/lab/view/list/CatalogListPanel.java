package lab.view.list;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionListener;

import lab.model.FileDescription;

@SuppressWarnings("serial")
public class CatalogListPanel extends JPanel {
	
	private DefaultListModel<FileDescription> listModel;
	private JList<FileDescription> jList;

	public CatalogListPanel(ListCellRenderer<FileDescription> listCellRenderer, int layoutOrientation) {
        listModel = new DefaultListModel<FileDescription>();
        jList = new JList<>(listModel);
        add(jList);
        jList.setLayoutOrientation(layoutOrientation);
        jList.setCellRenderer(listCellRenderer);
	}
	
	public void updateData(List<FileDescription> filesDescriptions) {
		listModel = new DefaultListModel<FileDescription>();
		for (FileDescription fileDescription: filesDescriptions) {
			listModel.addElement(fileDescription);
		}
		jList.setModel(listModel);
	}
	
	public void addListSelectionListener(ListSelectionListener listSelectionListener) {
		jList.addListSelectionListener(listSelectionListener);
	}
}
