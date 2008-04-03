package edu.raf.flowchart.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import edu.raf.flowchart.diagram.Diagram;

/**
 * Ovo je za sada suvišna klasa. Kada se bude ubacilo više prozora, tada će ovo imati smisla. Svaki
 * od tih prozora treba da sadrži pod jedan od ovih panela. Ovde ćemo ubaciti alatke specifične za
 * svaki view (zoom, na primer, koji je lokalni za svaki prozor). Zasad, neka samo iscrtava ovaj
 * bedan dijagram.
 * 
 * @author Boca
 * 
 */

@SuppressWarnings("serial")
public class DiagramPanel extends JPanel {
	private Diagram diagram;

	public DiagramPanel() {
		this.diagram = new Diagram(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		diagram.getView().drawDiagram(g2d, getSize());
	}

	public Diagram getDiagram() {
		return diagram;
	}
}
