import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class DTextModel extends DShapeModel {

	private String textValue;
	private Font font;
	private String fontType;

	public DTextModel() {
		textValue = "Hello";
		fontType = "Dialog.plain";
		super.setWidth(80);
		super.setHeight(50);
	}

	public void computeFont(Graphics g) {
		double size = 1.0;
		font = new Font(fontType, Font.PLAIN, (int) size);
		FontMetrics metrics = g.getFontMetrics(font);
		while (metrics.getHeight() < super.getHeight()) {
			size = (size * 1.10) + 1;
			font = new Font(fontType, Font.PLAIN, (int) size);
			metrics = g.getFontMetrics(font);
		}
		size = (size / 1.10) - 1;
		font = new Font(fontType, Font.PLAIN, (int) size);
		g.setFont(font);
	}

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
		super.notifyModelChanged("change");
	}

	public String getFontType() {
		return fontType;
	}

	public void setFontType(String fontType) {
		this.fontType = fontType;
		super.notifyModelChanged("change");
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}
	
	public void mimic(DShapeModel other) {
		super.setX(other.getX());
		super.setY(other.getY());
		super.setColor(other.getColor());
		super.setHeight(other.getHeight());
		super.setWidth(other.getWidth());
		this.font = ((DTextModel) other).getFont();
		this.textValue = ((DTextModel) other).getTextValue();
	}
}