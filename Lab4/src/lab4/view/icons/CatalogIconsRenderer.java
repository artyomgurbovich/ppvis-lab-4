package lab4.view.icons;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import lab4.model.FileDescription;

@SuppressWarnings("serial")
public class CatalogIconsRenderer extends JLabel implements ListCellRenderer<FileDescription> {
	
	private final int BORDER_SIZE = 30;
 
    public CatalogIconsRenderer() {
        setOpaque(true);
    }
 
    @Override
    public Component getListCellRendererComponent(JList<? extends FileDescription> list, FileDescription fileDescription, int index, boolean isSelected, boolean cellHasFocus) {
        boolean isFolder = fileDescription.isFolder();
        if (isFolder) {
            setIcon(new ImageIcon("image\\folderIcon.png"));
        } else {
        	setIcon(new ImageIcon("image\\fileIcon.png"));
        }
        setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.BOTTOM);
        setText(fileDescription.getName());
        setMinimumSize(new Dimension(300, 300));
        setPreferredSize(new Dimension(300, 300));
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