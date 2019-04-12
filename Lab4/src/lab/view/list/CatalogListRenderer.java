package lab.view.list;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.metal.MetalIconFactory;
import lab.model.FileDescription;

@SuppressWarnings("serial")
public class CatalogListRenderer extends JLabel implements ListCellRenderer<FileDescription> {
 
    public CatalogListRenderer() {
        setOpaque(true);
    }
 
    @Override
    public Component getListCellRendererComponent(JList<? extends FileDescription> list, FileDescription fileDescription, int index, boolean isSelected, boolean cellHasFocus) {
        boolean folder = fileDescription.isFolder();
        if (folder) {
            setIcon(MetalIconFactory.getTreeFolderIcon());
        } else {
        	setIcon(MetalIconFactory.getTreeLeafIcon());
        }
        setText(fileDescription.getName());
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}