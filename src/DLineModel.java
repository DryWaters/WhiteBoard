import java.awt.Rectangle;
import java.awt.Point;

public class DLineModel extends DShapeModel {

	private Point p1;
	private Point p2;
	
	public DLineModel(){
		super();
		p1 = new Point(10, 10);
		p2 = new Point(30, 30);
	}

	public Rectangle getBounds() {
		return new Rectangle(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.abs(p2.x - p1.x), Math.abs(p2.y - p1.y));
	}

	public Rectangle getBigBounds() {
		return new Rectangle(Math.min(p1.x, p2.x) - DShape.KNOB_SIZE / 2, Math.min(p1.y, p2.y) - DShape.KNOB_SIZE / 2, Math.abs(p2.x - p1.x) + DShape.KNOB_SIZE,
				Math.abs(p2.y - p1.y) + DShape.KNOB_SIZE);
	}
		
	public int getWidth() {
		return Math.abs(p2.x - p1.x);
	}
	
	public int getHeight() {
		return Math.abs(p2.y-p1.y);
	}
	
	public Point getP1() {
		return p1;
	}
	
	public Point getP2() {
		return p2;
	}
	
	public void setP1(Point pointOne) {
		this.p1.x = pointOne.x;
		this.p1.y = pointOne.y;
		super.notifyModelChanged("change");
	}
	
	public void setP2(Point pointTwo) {
		this.p2.x = pointTwo.x;
		this.p2.y = pointTwo.y;
		super.notifyModelChanged("change");
	}
	
	public void setX(int x1, int x2) {
		this.p1.x = x1;
		this.p2.x = x2;
		super.notifyModelChanged("change");
	}
	
	public void setY(int y1, int y2) {
		this.p1.y = y1;
		this.p2.y = y2;
		super.notifyModelChanged("change");
	}
	
	public void mimic(DShapeModel other) {
		DLineModel tempModel = (DLineModel) other;
		this.p1.x = tempModel.getP1().x;
		this.p1.y = tempModel.getP1().y;
		this.p2.x = tempModel.getP2().x;
		this.p2.y = tempModel.getP2().y;
		super.setColor(other.getColor());
		super.setHeight(other.getHeight());
		super.setWidth(other.getWidth());
	}

}