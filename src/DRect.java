import java.awt.Graphics;

public class DRect extends DShape {

	public DRect(DRectModel model) {
		super(model);
	}

	public void draw(Graphics g) {
		g.setColor(super.model.getColor());
		g.fillRect(super.model.getBounds().x, super.model.getBounds().y, super.model.getBounds().width,
				super.model.getBounds().height);
	}
}