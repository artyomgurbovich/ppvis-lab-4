package lab4.view.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import lab4.model.FileDescription;

@SuppressWarnings("serial")
public class CatalogTableModel extends AbstractTableModel {
	
	private final int NAME_COLUMN_INDEX = 0;
	private final int EXTENSION_COLUMN_INDEX = 1;
	private final int SIZE_COLUMN_INDEX = 2;
	private final int DATE_COLUMN_INDEX = 3;
	private final int COLUMN_COUNT = 4;
	
	private List<FileDescription> filesDescriptions;
		
	public CatalogTableModel() {
	    super();
	    this.filesDescriptions = new ArrayList<FileDescription>();
	}
	
	public void updateData(List<FileDescription> filesDescriptions) {
		this.filesDescriptions = filesDescriptions;
		fireTableDataChanged();
	}
	
    @Override
    public int getRowCount() {
        return filesDescriptions.size();
    }
    
    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }
    
    @Override
    public String getColumnName(int c) {
        String result = "";
        switch (c) {
            case NAME_COLUMN_INDEX:
                result = "Name";
                break;
            case EXTENSION_COLUMN_INDEX:
                result = "Extension";
                break;
            case SIZE_COLUMN_INDEX:
                result = "Size (KB)";
                break;
            case DATE_COLUMN_INDEX:
                result = "Date";
                break;
        }
        return result;
    }
    
    @Override
    public Object getValueAt(int r, int c) {
        switch (c) {
            case NAME_COLUMN_INDEX:
                return filesDescriptions.get(r).getName();
            case EXTENSION_COLUMN_INDEX:
            	return filesDescriptions.get(r).getExtension();
            case SIZE_COLUMN_INDEX:
            	return filesDescriptions.get(r).getSize();
            case DATE_COLUMN_INDEX:
            	return filesDescriptions.get(r).getDate();
            default:
                return "";
        }
    }
}
