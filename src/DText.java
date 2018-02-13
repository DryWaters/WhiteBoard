import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Shape;

public class DText extends DShape {

	private DTextModel model;

	public DText(DTextModel model) {
		super(model);
		this.model = model;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(super.model.getColor());
		model.computeFont(g);
		FontMetrics metrics = g.getFontMetrics();

		Shape clip = g.getClip();
		g.setClip(clip.getBounds().createIntersection(getBounds()));
		g.drawString(model.getTextValue(), model.getX(), model.getY() + model.getHeight() - metrics.getDescent());
		g.setClip(clip);
	}

	public void changeSize() {
		model.setHeight((int) (Math.random() * 200));
		model.setWidth((int) (Math.random() * 200));
	}

	public void setFont(String fontName) {
		model.setFontType(fontName);
	}

	public void setText(String text) {
		model.setTextValue(text);
	}

	public String getText() {
		return model.getTextValue();
	}

	public String getFonttype() {
		return model.getFontType();
	}
}