import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Controls extends JPanel {

	private Importer importer;
	private Exporter exporter;
	private TCPClient tcpClient;
	private TCPServer tcpServer;
	private JPanel addShapesPnl;
	private JPanel textBoxPnl;
	private JPanel shapeColorPnl;
	private JPanel editShapesPnl;
	private JPanel editSaveContentPnl;
	private JPanel networkingPnl;
	private JPanel tablePnl;
	private Canvas canvas;
	private JLabel networkStatus;
	private ModelData model;

	public Controls(Canvas canvas) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setPreferredSize(new Dimension(500, 400));
		this.canvas = canvas;
		importer = new Importer(canvas);
		exporter = new Exporter(canvas);
		tcpClient = new TCPClient(canvas, importer);
		tcpServer = new TCPServer(canvas, exporter);		
		canvas.setServer(tcpServer);
		
		// Methods to add GUI elementals to JPanel
		addShapesBox();
		textBox();
		shapeColorBox();
		editShapesBox();
		editSaveContentBox();
		networkingBox();
		table();
	}

	private void addShapesBox() {
		addShapesPnl = new JPanel();
		addShapesPnl.setLayout(new BoxLayout(addShapesPnl, BoxLayout.X_AXIS));
		addShapesPnl.setBorder(BorderFactory.createMatteBorder(3, 10, 3, 10, new Color(238, 238, 238)));
		addShapesPnl.setAlignmentX(Box.LEFT_ALIGNMENT);
		addShapesPnl.add(new JLabel("Add Shapes: ")).setFont(new Font("Serif", Font.ITALIC, 16));
		
		addShapesPnl.add(Box.createRigidArea(new Dimension(10, 0)));
		JButton newRect = new JButton("Rect");
		newRect.addActionListener((e) -> canvas.addShape(new DRect(new DRectModel())));
		addShapesPnl.add(newRect);

		addShapesPnl.add(Box.createRigidArea(new Dimension(10, 0)));
		JButton newOval = new JButton("Oval");
		newOval.addActionListener((e) -> canvas.addShape(new DOval(new DOvalModel())));
		addShapesPnl.add(newOval);

		addShapesPnl.add(Box.createRigidArea(new Dimension(10, 0)));
		JButton newLine = new JButton("Line");
		newLine.addActionListener((e) -> canvas.addShape(new DLine(new DLineModel())));
		addShapesPnl.add(newLine);

		addShapesPnl.add(Box.createRigidArea(new Dimension(10, 0)));
		JButton newText = new JButton("Text");
		newText.addActionListener((e) -> canvas.addShape(new DText(new DTextModel())));
		
		addShapesPnl.add(newText);

		addShapesPnl.add(Box.createHorizontalGlue());
		this.add(addShapesPnl);
	}

	private void textBox() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = ge.getAllFonts();

		textBoxPnl = new JPanel();
		textBoxPnl.setLayout(new BoxLayout(textBoxPnl, BoxLayout.X_AXIS));
		textBoxPnl.setBorder(BorderFactory.createMatteBorder(3, 10, 3, 10, new Color(238, 238, 238)));
		textBoxPnl.setAlignmentX(Box.LEFT_ALIGNMENT);
		textBoxPnl.add(new JLabel("Edit Text: ")).setFont(new Font("Serif", Font.ITALIC, 16));
		
		JTextField textField = new JTextField(25);
		textField.setMaximumSize(new Dimension(textField.getPreferredSize().width, 20));
		textBoxPnl.add(textField);
		textField.setEnabled(false);
		textField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				canvas.updateText(textField.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				canvas.updateText(textField.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				canvas.updateText(textField.getText());
			}
		});
		
		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.setMaximumRowCount(8);
		comboBox.setMaximumSize(new Dimension(comboBox.getPreferredSize().width, 20));
		for (Font font : fonts) {
			comboBox.addItem(font.getFontName());
		}
		comboBox.addActionListener((e) -> {
			String value = comboBox.getSelectedItem().toString();
			canvas.updateFontType(value);
		});
		comboBox.setEnabled(false);
		textBoxPnl.add(Box.createHorizontalGlue());
		textBoxPnl.add(comboBox);
		this.add(textBoxPnl);
	}

	private void shapeColorBox() {
		shapeColorPnl = new JPanel();
		shapeColorPnl.setLayout(new BoxLayout(shapeColorPnl, BoxLayout.X_AXIS));
		shapeColorPnl.setBorder(BorderFactory.createMatteBorder(3, 10, 3, 10, new Color(238, 238, 238)));
		shapeColorPnl.setAlignmentX(Box.LEFT_ALIGNMENT);
		shapeColorPnl.add(new JLabel("Set Shape Color: ")).setFont(new Font("Serif", Font.ITALIC, 16));
		shapeColorPnl.add(Box.createRigidArea(new Dimension(10, 0)));

		JButton setColorBtn = new JButton("Set Color");
		setColorBtn.addActionListener(
				(e) -> canvas.setColor(JColorChooser.showDialog(canvas, "Choose Color", Color.GRAY)));
		shapeColorPnl.add(setColorBtn);
		shapeColorPnl.add(Box.createHorizontalGlue());

		this.add(shapeColorPnl);
	}

	private void editShapesBox() {
		editShapesPnl = new JPanel();
		editShapesPnl.setLayout(new BoxLayout(editShapesPnl, BoxLayout.X_AXIS));
		editShapesPnl.setBorder(BorderFactory.createMatteBorder(3, 10, 3, 10, new Color(238, 238, 238)));
		editShapesPnl.setAlignmentX(Box.LEFT_ALIGNMENT);
		editShapesPnl.add(new JLabel("Edit Shapes: ")).setFont(new Font("Serif", Font.ITALIC, 16));
		
		editShapesPnl.add(Box.createRigidArea(new Dimension(10, 0)));
		JButton sendToFront = new JButton("Send to Front");
		sendToFront.addActionListener((e) -> canvas.sendToFront());
		editShapesPnl.add(sendToFront);

		editShapesPnl.add(Box.createRigidArea(new Dimension(10, 0)));
		JButton sendToBack = new JButton("Send to Back");
		sendToBack.addActionListener((e) -> canvas.sendToBack());
		editShapesPnl.add(sendToBack);

		editShapesPnl.add(Box.createRigidArea(new Dimension(10, 0)));
		JButton removeShape = new JButton("Remove");
		removeShape.addActionListener((e) -> canvas.deleteSelected());
		editShapesPnl.add(removeShape);
		editShapesPnl.add(Box.createHorizontalGlue());

		this.add(editShapesPnl);
	}

	private void editSaveContentBox() {
		editSaveContentPnl = new JPanel();
		editSaveContentPnl.setLayout(new BoxLayout(editSaveContentPnl, BoxLayout.X_AXIS));
		editSaveContentPnl.setBorder(BorderFactory.createMatteBorder(3, 10, 3, 10, new Color(238, 238, 238)));
		editSaveContentPnl.setAlignmentX(Box.LEFT_ALIGNMENT);
		editSaveContentPnl.add(new JLabel("Edit/Save Content: ")).setFont(new Font("Serif", Font.ITALIC, 16));
		
		editSaveContentPnl.add(Box.createRigidArea(new Dimension(10, 0)));
		JButton save = new JButton("Save");
		save.addActionListener((e) -> {
			File fileName = exporter.getFile();
			FileOutputStream fileStream = null;
			try {
				fileStream = new FileOutputStream(fileName);
				exporter.writeFile(fileStream, canvas.getShapes());	
			} catch (IOException e1) {
				System.out.println("Invalid file name!");
				e1.printStackTrace();
			}
		});
		editSaveContentPnl.add(save);
		
		editSaveContentPnl.add(Box.createRigidArea(new Dimension(10, 0)));
		JButton load = new JButton("Load");
		load.addActionListener((e) -> {
			File fileName = importer.getFile();
			FileInputStream fileStream = null;
			try {
				canvas.flushShapes();
				fileStream = new FileInputStream(fileName);
				importer.readObject(fileStream);
			} catch (FileNotFoundException e1) {
				System.out.println("Invalid file name!");
				e1.printStackTrace();
			}
		});
		editSaveContentPnl.add(load);
		
		editSaveContentPnl.add(Box.createRigidArea(new Dimension(10, 0)));
		JButton savePNG = new JButton("Save as PNG");
		savePNG.addActionListener((e) -> {
			canvas.deselectShape();
			canvas.repaint();
			exporter.savePNG();
		});
		editSaveContentPnl.add(savePNG);
		editSaveContentPnl.add(Box.createHorizontalGlue());

		this.add(editSaveContentPnl);
	}

	private void networkingBox() {
		networkingPnl = new JPanel();
		networkingPnl.setLayout(new BoxLayout(networkingPnl, BoxLayout.X_AXIS));
		networkingPnl.setBorder(BorderFactory.createMatteBorder(3, 10, 3, 10, new Color(238, 238, 238)));
		networkingPnl.setAlignmentX(Box.LEFT_ALIGNMENT);
		networkingPnl.add(new JLabel("Networking: ")).setFont(new Font("Serif", Font.ITALIC, 16));
		
		networkingPnl.add(Box.createRigidArea(new Dimension(10, 0)));
		JButton startServer = new JButton("Start Server");
		JButton startClient = new JButton("Start Client");
		startServer.addActionListener((e)-> {
			String portNumber = tcpServer.getPort(canvas);
			if (portNumber != null) {
				startServer.setEnabled(false);
				startClient.setEnabled(false);
				networkStatus.setText("Server Mode on port:  " + portNumber);
				tcpServer.start();
			}
		});
		networkingPnl.add(startServer);
		
		networkingPnl.add(Box.createRigidArea(new Dimension(10, 0)));
		startClient.addActionListener((e)-> {
			
			// remove the focus from startClient button (for esthetics)
			this.requestFocus();
			String hostLocation = tcpClient.getIPPort(canvas);
			if (hostLocation != null) {
				disableAllButtons();
				networkStatus.setText("Client: " + hostLocation);
				tcpClient.start();	
			}
		});
		networkingPnl.add(startClient);
		
		networkingPnl.add(Box.createHorizontalGlue());
		networkStatus = new JLabel("");
		networkingPnl.add(networkStatus);

		this.add(networkingPnl);
	}

	private void table() {		
		tablePnl = new JPanel();
		tablePnl.setLayout(new BoxLayout(tablePnl, BoxLayout.X_AXIS));
		tablePnl.setBorder(BorderFactory.createMatteBorder(3, 10, 3, 10, new Color(238, 238, 238)));
		tablePnl.setAlignmentX(Box.LEFT_ALIGNMENT);

		model = new ModelData(canvas.getShapesRef());
		JTable table = new JTable(model);

		table.getTableHeader().setReorderingAllowed(false);
		table.setEnabled(false);
		tablePnl.add(new JScrollPane(table));  

		this.add(tablePnl);
	}

	public void selectedText(String textField, String fontName) {
		for (Component textBoxComp : textBoxPnl.getComponents()) {
			if (textBoxComp instanceof JTextField) {
				((JTextField) textBoxComp).setText(textField);
			}
			if (textBoxComp instanceof JComboBox) {
				((JComboBox<?>) textBoxComp).setSelectedItem(fontName);
			}
			textBoxComp.setEnabled(true);
		}
	}

	public void deselectedText() {
		for (Component textBoxComp : textBoxPnl.getComponents()) {
			if (textBoxComp instanceof JTextField || textBoxComp instanceof JComboBox) {
				textBoxComp.setEnabled(false);
			}
		}
	}
	
	private void disableAllButtons() {
		for (Component component : this.getComponents()) {
			if (component instanceof JPanel) {
				for (Component panelComponents : ((JPanel) component).getComponents()) {
					panelComponents.setEnabled(false);
				}
			}
		}
		networkStatus.setEnabled(true);
	}

	public ModelData getModelData() {
		return model;
	}
	
	public void setModelData(ArrayList<DShape> shapes) {
		model.setShapes(shapes);
	}
	

}