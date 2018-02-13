import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

import javax.swing.JOptionPane;

public class Importer {
	
	private Canvas canvas;
		
	public Importer(Canvas canvas) {
		this.canvas = canvas;
	}
	
	public File getFile() {
		String s = (String) JOptionPane.showInputDialog(canvas, "Please enter the filename to load:  ", "File Name",
				JOptionPane.QUESTION_MESSAGE, null, null, "shapes.xml");
		return new File(s);
	}
	
	public void readObject(InputStream stream) {
		DShapeModel[] models = null;
		XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(stream));
		String action = (String) decoder.readObject();
		models = (DShapeModel[]) decoder.readObject();
		decode(action, models);
		decoder.close();
	}
	
	public void decode(String action, DShapeModel[] models) {
		if ("add".equals(action)) {
			for (DShapeModel model : models) {
				whichShapeToAdd(model);
			}
		} else if ("remove".equals(action)) {
			canvas.findSelectedShape(models[0].getId());
			canvas.deleteSelected();
		} else if ("front".equals(action)) {
			canvas.findSelectedShape(models[0].getId());
			canvas.sendToFront();
		} else if ("back".equals(action)) {
			canvas.findSelectedShape(models[0].getId());
			canvas.sendToBack();
		} else {
			canvas.findSelectedShape(models[0].getId());
			canvas.getSelectedShape().getModel().mimic(models[0]);
		}
	}
	
	private void whichShapeToAdd(DShapeModel model) {
		if (model instanceof DRectModel) {
			canvas.addShape(new DRect((DRectModel) model));
		} else if (model instanceof DOvalModel) {
			canvas.addShape(new DOval((DOvalModel) model));
		} else if (model instanceof DTextModel) {
			canvas.addShape(new DText((DTextModel) model));
		} else {
			canvas.addShape(new DLine((DLineModel) model));
		}
	}
}