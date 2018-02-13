import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class TCPClient extends Thread {
	
	private Socket socket;
	private String host;
	private int port;
	private Canvas canvas;
	private Importer importer;
	
	public TCPClient (Canvas canvas, Importer importer) {
		this.canvas = canvas;
		this.socket = null;
		this.host = null;
		this.port = 0;
		this.importer = importer;
	}
	
	@Override
	public void run() {
		canvas.setClientMode();
		canvas.flushShapes();
		try {
			socket = new Socket(host, port);
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			while(true) {
				readSocket(input);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String getIPPort(Canvas canvas) {
		String s = (String) JOptionPane.showInputDialog(canvas, "Please enter host:port (127.0.0.1:39587)  ", "Host location",
				JOptionPane.QUESTION_MESSAGE, null, null, "127.0.0.1:39587");
		if (s != null) {
			while (!validateIPAddress(s)) {
				JOptionPane.showMessageDialog(canvas, "INCORRECT URI FORMAT", "INCORRECT FORMAT", JOptionPane.WARNING_MESSAGE);
				s = (String) JOptionPane.showInputDialog(canvas, "Please enter host:port (127.0.0.1:39587)  ", "Host location",
						JOptionPane.QUESTION_MESSAGE, null, null, "127.0.0.1:39587");
			}
		}
		return s;
	}
	
	private boolean validateIPAddress(String ipAddress) {
		String ipWithoutPort = null;
		String port = null;
		
		// fail validation if does not contain ':'
		if (ipAddress.indexOf(":") < 0) {
			return false;
		}
		port = ipAddress.substring(ipAddress.indexOf(":")+1, ipAddress.length());
		int portNumber = Integer.parseInt(port);
		
		// fail validation if port number out of range
		if ((portNumber < 0) || (portNumber > 65535)) {
			return false;
		}
		ipWithoutPort = ipAddress.substring(0, ipAddress.indexOf(":"));
		
		// if is set to localhost pass validation
		if ("localhost".equals(ipWithoutPort)) {
			this.host = "127.0.0.1";
			this.port = portNumber;
			return true;
		}
		
		String[] tokens = ipWithoutPort.split("\\.");
		
		// return false if does not contain 4 octets
		if(tokens.length != 4) {
			return false;
		}
		
		// check each octet valid number from 0-255
		for (String str : tokens) {
			int i = Integer.parseInt(str);
			if ((i < 0) || (i>255)) {
				return false;
			}
		}
		
		this.host = ipWithoutPort;
		this.port = portNumber;
		return true;
	}
	
	public void readSocket(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        String xmlString = (String) stream.readObject();
        XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(xmlString.getBytes()));
		String actionString = (String) decoder.readObject();
		DShapeModel[] models = (DShapeModel[]) decoder.readObject();
		importer.decode(actionString, models);	
		decoder.close();
	}
}