import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class TCPServer extends Thread implements ModelListener {
	
	private int port;
	private List<ObjectOutputStream> clientStreams;
	private Canvas canvas;
	private Exporter exporter;
	
	public TCPServer(Canvas canvas, Exporter exporter) {
		clientStreams = new ArrayList<>();
		this.canvas = canvas;
		this.exporter = exporter;
	}

	public String getPort(Canvas canvas) {
		String s = (String) JOptionPane.showInputDialog(canvas, "Please enter port number:(0-65535)  ", "Port Number",
				JOptionPane.QUESTION_MESSAGE, null, null, "39587");

		// if user does not hit cancel
		if (s != null) {
			while (!validatePort(s)) {
				JOptionPane.showMessageDialog(canvas, "Incorrect Port Number!", "INCORRECT FORMAT", JOptionPane.WARNING_MESSAGE);
				s = (String) JOptionPane.showInputDialog(canvas, "Please enter port number:(0-65535)  ", "Port Number",
						JOptionPane.QUESTION_MESSAGE, null, null, "39587");
			}
			this.port = Integer.parseInt(s);
		}
		return s;
	}
	
	private boolean validatePort(String port) {

		int portNumber = Integer.parseInt(port);
		
		// fail validation if port number out of range
		if ((portNumber < 0) || (portNumber > 65535)) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public void run() {
		ServerSocket server = null;
		try {
			System.out.println("Port number is:  " + port);
			server = new ServerSocket(port);	
			while (true) {
				System.out.println("Waiting for connection");
				Socket client = server.accept();
				clientStreams.add(new ObjectOutputStream(client.getOutputStream()));
				System.out.println("Client connected");
				System.out.println("Total clients is " + clientStreams.size());
				addAllShapes();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void addAllShapes() {
		DShapeModel[] models = canvas.getShapes();
		for (ObjectOutputStream stream : clientStreams) {	
			try {
				exporter.writeObject(stream, "add", models);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	public void changeOneShape(DShapeModel model, String action) {
		DShapeModel[] models = new DShapeModel[1];
		models[0] = model;
		for (ObjectOutputStream stream : clientStreams) {	
			try {
				exporter.writeObject(stream, action, models);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void modelChanged(DShapeModel model, String action) {
		changeOneShape(model, action);
	}
}