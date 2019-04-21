package lab4.view.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import lab4.model.Drive;

@SuppressWarnings("serial")
public class DrivesTableModel extends AbstractTableModel {
	
	private final int NAME_COLUMN_INDEX = 0;
	private final int LETTER_COLUMN_INDEX = 1;
	private final int COLUMN_COUNT = 2;
	
	private List<Drive> drives;
		
	public DrivesTableModel() {
	    super();
	    this.drives = new ArrayList<Drive>();
	}
	
	public void updateData(List<Drive> drives) {
		this.drives = drives;
		fireTableDataChanged();
	}
	
    @Override
    public int getRowCount() {
        return drives.size();
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
            case LETTER_COLUMN_INDEX:
                result = "Letter";
                break;
        }
        return result;
    }
    
    @Override
    public Object getValueAt(int r, int c) {
        switch (c) {
            case NAME_COLUMN_INDEX:
                return drives.get(r).getName();
            case LETTER_COLUMN_INDEX:
            	return drives.get(r).getLetter();
            default:
                return "";
        }
    }
}