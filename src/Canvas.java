import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Canvas extends JPanel implements ModelListener {

	private ArrayList<DShape> shapes;
	private DShape selectedShape;
	private Point pressedLocation;
	private Point selectedShapeStartLocation;
	private Point selectedLinePointTwoLocation;
	
	private boolean movingKnob;
	private Point nonMovingPoint;
	private Point movingPoint;
	
	private Controls controls;
	private TCPServer server;

	public Canvas() {
		this.setPreferredSize(new Dimension(400, 400));
		this.setBackground(Color.WHITE);
		shapes = new ArrayList<>();
		pressedLocation = new Point();
		selectedShapeStartLocation = new Point();
		selectedLinePointTwoLocation = new Point();
		movingKnob = false;
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				pressedLocation = e.getPoint();
				selectedShape = findSelectedShape(e.getPoint());
				
				// if selected a shape
				if (selectedShape != null) {
					
					// if shape is a DLine keep track of old point 1 and point 2 values
					if (selectedShape instanceof DLine) {
						DLineModel tempModel = (DLineModel) selectedShape.getModel();
						selectedShapeStartLocation.x = tempModel.getP1().x;
						selectedShapeStartLocation.y = tempModel.getP1().y;
						selectedLinePointTwoLocation.x = tempModel.getP2().x;
						selectedLinePointTwoLocation.y = tempModel.getP2().y;
					} else {
						selectedShapeStartLocation.x = selectedShape.getBounds().x;
						selectedShapeStartLocation.y = selectedShape.getBounds().y;
					}
					
					// if selected a knob of a selected shape
					if(selectedShape.isKnob(pressedLocation)) {
						movingKnob = true;
						Rectangle anchorR = selectedShape.anchorPoint(e.getPoint());
						nonMovingPoint = new Point(anchorR.x, anchorR.y);
						
						// if selecting a line, only move one point when resizing
						if (selectedShape instanceof DLine) {
							movingPoint = ((DLine) selectedShape).movingPoint(pressedLocation);
						}
						
					// reset the points if no knobs selected
					} else {
						movingKnob = false;
						nonMovingPoint = null;
						movingPoint = null;
					}
					

				}
				// set controls based on if selected text
				checkIfText();
			}
		});
				
		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
		
				if (selectedShape != null) {
					if(movingKnob) {
						resizeShape(e.getPoint());
					} else {
						moveShape(e.getPoint());
					}
				}
			}
		});
	}
	
	public void moveShape(Point point) {
		if (selectedShape instanceof DLine) {
			DLineModel tempModel = (DLineModel) selectedShape.getModel();
			tempModel.setX(selectedShapeStartLocation.x + (point.x - pressedLocation.x), selectedLinePointTwoLocation.x + (point.x - pressedLocation.x));
			tempModel.setY(selectedShapeStartLocation.y + (point.y - pressedLocation.y), selectedLinePointTwoLocation.y + (point.y - pressedLocation.y));
		}

		selectedShape.getModel().setX(selectedShapeStartLocation.x + (point.x - pressedLocation.x));
		selectedShape.getModel().setY(selectedShapeStartLocation.y + (point.y - pressedLocation.y));	
	}
	
	private void resizeShape(Point point) {
		if (selectedShape instanceof DLine) { 
			movingPoint.x = point.x;
			movingPoint.y = point.y;
		} else {	
			int xDiff = Math.min(nonMovingPoint.x, point.x);
			int yDiff = Math.min(nonMovingPoint.y, point.y);
			int width = Math.abs(point.x - nonMovingPoint.x);
			int height = Math.abs(point.y - nonMovingPoint.y);
			Rectangle newRectangle = new Rectangle(xDiff, yDiff, width, height);
			selectedShape.getModel().setBounds(newRectangle);
		}
		selectedShape.getModel().notifyModelChanged("change");
	}

	public void addShape(DShape shape) {
		shapes.add(shape);
		shape.getModel().addListener(this);
		shape.getModel().addListener(server);
		shape.getModel().addListener(controls.getModelData());
		shape.getModel().notifyModelChanged("add");
	}

	// select the shape based on location
	// select in reverse order so to select the top shape first
	private DShape findSelectedShape(Point point) {
		for (int i = shapes.size()-1; i >= 0; i--) {
			if ((shapes.get(i).getModel().getBounds().contains(point)) 
					|| (shapes.get(i) == selectedShape && shapes.get(i).getModel().getBigBounds().contains(point))) {
				checkIfText();
				shapes.get(i).getModel().notifyModelChanged("change");
				return shapes.get(i);
			}
		}
		
		// did not select a shape return null and repaint to deselect shape.
		this.repaint();
		return null;
	}
	
	// select the shape based on ID
	public void findSelectedShape(int id) {
		for (DShape shape : shapes) {
			if (shape.getModel().getId() == id) {
				selectedShape = shape;
				return;
			}
		}
		System.out.println("Shape does not exist!");
	}

	// Special text methods	
	// update controls if selected shape is DText
	private void checkIfText() {
		if (selectedShape != null && selectedShape instanceof DText) {
			DText selectedText = ((DText) selectedShape);
			controls.selectedText(selectedText.getText(), selectedText.getFonttype());
		} else {
			controls.deselectedText();
		}
	}
	
	public void updateText(String text) {
		((DText) selectedShape).setText(text);
	}

	public void updateFontType(String fontName) {
		((DText) selectedShape).setFont(fontName);
	}

	public void changeTextSize() {
		((DText) selectedShape).changeSize();
	}
	// end special text methods
	
	public DShape getSelectedShape() {
		return selectedShape;
	}
	
	public void setColor(Color color) {
		if (selectedShape != null) {
			selectedShape.getModel().setColor(color);
		}
	}

	public void deleteSelected() {
		selectedShape.getModel().notifyModelChanged("remove");
		selectedShape.getModel().deleteAllListeners();
		shapes.remove(selectedShape);
		selectedShape = null;
		controls.deselectedText();
	}

	public void sendToFront() {
		int index = shapes.indexOf(selectedShape);
		shapes.remove(index);
		shapes.add(selectedShape);
		selectedShape.getModel().notifyModelChanged("front");
	}

	public void sendToBack() {
		int index = shapes.indexOf(selectedShape);
		shapes.remove(index);
		shapes.add(0, selectedShape);
		shapes.get(0).getModel().notifyModelChanged("back");
	}

	public DShapeModel[] getShapes() {
		DShapeModel[] shapeModels = new DShapeModel[shapes.size()];
		for (int i = 0; i < shapes.size(); i++) {
			shapeModels[i] = shapes.get(i).getModel();
		}
		return shapeModels;
	}

	public ArrayList<DShape> getShapesRef() {
		return shapes;
	}
	
	// clear all local shapes to make room for remote or file 
	public void flushShapes() {
		shapes = null;
		shapes = new ArrayList<DShape>();
		controls.setModelData(shapes);
	}

	public void deselectShape() {
		selectedShape = null;
	}

	// Remove the user's ability to click on a shape after setting to client mode
	public void setClientMode() {
		MouseListener[] mouseListener = this.getListeners(MouseListener.class);
		MouseMotionListener[] mouseMotionListener = this.getListeners(MouseMotionListener.class);
		this.removeMouseListener(mouseListener[0]);
		this.removeMouseMotionListener(mouseMotionListener[0]);
	}
	
	// Setters for controls and server used
	// since Canvas is instantiated first 
	public void setControls(Controls controls) {
		this.controls = controls;
	}
	
	public void setServer(TCPServer server) {
		this.server = server;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (DShape shape : shapes) {
			shape.draw(g);
			if (shape == selectedShape) {
				shape.drawKnobs(g);
			}
		}
	}
	
	@Override
	public void modelChanged(DShapeModel model, String action) {
		this.repaint();
	}
}