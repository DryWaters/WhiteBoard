import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class DLine extends DShape {
	
	private DLineModel model;

	public DLine(DLineModel model) {
		super(model);
		this.model = model;
	}

	public void draw(Graphics g) {
		g.setColor(super.model.getColor());
		g.drawLine(model.getP1().x, model.getP1().y, model.getP2().x, model.getP2().y);
	}
	
	public ArrayList<Rectangle> getKnobs() {
		ArrayList<Rectangle> knobs = new ArrayList<>();
		knobs.add(new Rectangle(model.getP1().x - KNOB_SIZE / 2, model.getP1().y - KNOB_SIZE / 2, KNOB_SIZE, KNOB_SIZE));
		knobs.add(new Rectangle(model.getP2().x - KNOB_SIZE / 2, model.getP2().y - KNOB_SIZE / 2, KNOB_SIZE, KNOB_SIZE));
		return knobs;
	}
	
	public Point movingPoint(Point movingPoint) {
		ArrayList<Rectangle> knobs = getKnobs();
		if (knobs.get(0).contains(movingPoint))
			return model.getP1();
		else 
			return model.getP2();
	}
	
}