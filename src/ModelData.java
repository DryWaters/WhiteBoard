import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

public class ModelData extends AbstractTableModel implements ModelListener{
	
	private String[] colNames = { "X", "Y", "WIDTH", "HEIGHT" };
	private ArrayList<DShape> data;

	public ModelData(ArrayList<DShape> shapes) {
		data = shapes;
	}
	
	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}
	
	@Override
	public String getColumnName(int c) {
		return colNames[c];	
	}

	@Override
	public String getValueAt(int rowIndex, int col) {
		if (data.get(rowIndex) instanceof DLine) {
			if (col == 0)
				return "Start X " + ((DLineModel) data.get(rowIndex).getModel()).getP1().x;
			else if (col == 1)
				return "Start Y " + ((DLineModel) data.get(rowIndex).getModel()).getP1().y;
			else if (col == 2)
				return "End X " + ((DLineModel) data.get(rowIndex).getModel()).getP2().x;
			else if (col == 3)
				return "End Y " + ((DLineModel) data.get(rowIndex).getModel()).getP2().y;
			else 
				return null;	
		} else {
			if (col == 0)
				return "" + data.get(rowIndex).getModel().getX();
			else if (col == 1)
				return "" + data.get(rowIndex).getModel().getY();
			else if (col == 2)
				return "" + data.get(rowIndex).getModel().getWidth();
			else if (col == 3)
				return "" + data.get(rowIndex).getModel().getHeight();
			else 
				return null;	
		}
	}
	
	@Override
	public void modelChanged(DShapeModel model, String str) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if ("change".equals(str)) {
					@SuppressWarnings("unlikely-arg-type")
					int i = data.indexOf(model);
					fireTableRowsUpdated(i, i);
				} else {
					fireTableDataChanged();
				}
			}
		});
	}
	
	public void setShapes(ArrayList<DShape> shapes) {
		this.data = shapes;
	}
}
