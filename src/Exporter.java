import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Exporter {
	
	private Canvas canvas;
	
	public Exporter(Canvas canvas) {
		this.canvas = canvas;
	}

	public File getFile() {
		String s = (String) JOptionPane.showInputDialog(canvas, "Please enter the filename to save:  ", "File Name",
				JOptionPane.QUESTION_MESSAGE, null, null, "shapes.xml");
		return new File(s);
	}
		
	public void writeObject(ObjectOutputStream stream, String action, DShapeModel[] dShapeModels) throws IOException {
        OutputStream memStream = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(memStream);
        encoder.writeObject(action);
        encoder.writeObject(dShapeModels);
        encoder.close();
        String xmlString = memStream.toString();
        stream.writeObject(xmlString);
		stream.flush();
	}
	
	public void writeFile(OutputStream stream, DShapeModel[] dShapeModels) throws IOException {
		XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(stream));
		encoder.writeObject("add");
		encoder.writeObject(dShapeModels);
		encoder.close();
	}

	public void savePNG() {
		String s = (String) JOptionPane.showInputDialog(canvas, "Please enter the filename to save:  ", "File Name",
				JOptionPane.QUESTION_MESSAGE, null, null, "shapes.png");
		BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = image.createGraphics();
		canvas.paint(g);
		g.dispose();
		try {
			ImageIO.write(image, "png", new File(s));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}