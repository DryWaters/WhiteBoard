import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class DShapeModel {

	private static int numberOfShapes = 0;
	private int x;
	private int y;
	private int width;
	private int height;
	private Color color;
	private int id;

	private ArrayList<ModelListener> listeners;

	public DShapeModel() {
		x = 10;
		y = 10;
		width = 20;
		height = 20;
		color = Color.GRAY;
		listeners = new ArrayList<>();
		id = numberOfShapes++;
	}

	public void notifyModelChanged(String action) {
		for (ModelListener listener : listeners) {
			listener.modelChanged(this, action);
		}
	}

	public void addListener(ModelListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ModelListener listener) {
		listeners.remove(listener);
	}

	public void deleteAllListeners() {
		listeners.clear();
	}
	
	public void setBounds(Rectangle rectangle) {
		this.x = rectangle.x;
		this.y = rectangle.y;
		this.width = rectangle.width;
		this.height = rectangle.height;
		notifyModelChanged("change");
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public Rectangle getBigBounds() {
		return new Rectangle(x - DShape.KNOB_SIZE / 2, y - DShape.KNOB_SIZE / 2, width + DShape.KNOB_SIZE,
				height + DShape.KNOB_SIZE);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		notifyModelChanged("change");
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		notifyModelChanged("change");
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		notifyModelChanged("change");
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		notifyModelChanged("change");
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		notifyModelChanged("change");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void mimic(DShapeModel other) {
		this.x = other.x;
		this.y = other.y;
		this.color = other.color;
		this.height = other.height;
		this.setWidth(other.width);
	}
}