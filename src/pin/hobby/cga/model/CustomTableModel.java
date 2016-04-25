package pin.hobby.cga.model;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

/**
 * Created by pin-mint on 16. 4. 21.
 */
public class CustomTableModel extends DefaultTableModel{

    public CustomTableModel() {
    }

    public CustomTableModel(int rowCount, int columnCount) {
        super(rowCount, columnCount);
    }

    public CustomTableModel(Vector columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    public CustomTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    public CustomTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    public CustomTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
//        return super.isCellEditable(row, column);
        return false;
    }
}
