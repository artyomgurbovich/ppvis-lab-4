package lab.view.icons;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.metal.MetalIconFactory;
import lab.model.FileDescription;

@SuppressWarnings("serial")
public class CatalogIconsRenderer extends JLabel implements ListCellRenderer<FileDescription> {
	
	private final int BORDER_SIZE = 30;
 
    public CatalogIconsRenderer() {
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
        setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.BOTTOM);
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