import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class DShape {

	public static final int KNOB_SIZE = 9;
	protected DShapeModel model;

	public DShape(DShapeModel model) {
		this.model = model;
	}

	public abstract void draw(Graphics g);

	public ArrayList<Rectangle> getKnobs() {
		ArrayList<Rectangle> knobs = new ArrayList<>();
		knobs.add(new Rectangle(model.getX() - KNOB_SIZE / 2, model.getY() - KNOB_SIZE / 2, KNOB_SIZE, KNOB_SIZE));
		knobs.add(new Rectangle(model.getX() + model.getWidth() - KNOB_SIZE / 2, model.getY() - KNOB_SIZE / 2,
				KNOB_SIZE, KNOB_SIZE));
		knobs.add(new Rectangle(model.getX() + model.getWidth() - KNOB_SIZE / 2,
				model.getY() + model.getHeight() - KNOB_SIZE / 2, KNOB_SIZE, KNOB_SIZE));
		knobs.add(new Rectangle(model.getX() - KNOB_SIZE / 2, model.getY() + model.getHeight() - KNOB_SIZE / 2,
				KNOB_SIZE, KNOB_SIZE));
		return knobs;
	}

	public void drawKnobs(Graphics g) {
		Color tempColor = g.getColor();
		g.setColor(Color.BLACK);
		ArrayList<Rectangle> knobs = getKnobs();
		for (Rectangle rect : knobs) {
			g.fillRect(rect.x, rect.y, rect.width, rect.height);
		}
		g.setColor(tempColor);
	}

	public Rectangle getBounds() {
		return model.getBounds();
	}

	public DShapeModel getModel() {
		return model;
	}

	public boolean isKnob(Point point) {
		boolean isKnob = false;
		ArrayList<Rectangle> knobs = getKnobs();
		for (Rectangle rect : knobs) {
			if (rect.contains(point)) {
				isKnob = true;
			}
		}
		return isKnob;
	}
	
	public Rectangle anchorPoint(Point movingP) {

		ArrayList<Rectangle> knobs = getKnobs();
		for (int i = 0; i < knobs.size(); i++) {
			if (knobs.get(i).contains(movingP)) {
				return (knobs.get((i+2) % knobs.size()));
			}
		}
		return null;
	}
}