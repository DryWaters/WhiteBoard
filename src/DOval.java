import java.awt.Graphics;

public class DOval extends DShape {

	public DOval(DOvalModel model) {
		super(model);
	}

	public void draw(Graphics g) {
		g.setColor(super.model.getColor());
		g.fillOval(super.model.getBounds().x, super.model.getBounds().y, super.model.getBounds().width,
				super.model.getBounds().height);
	}
}