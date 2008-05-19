package edu.raf.gef.editor.control.state;

import edu.raf.gef.editor.BlockDiagram;
import edu.raf.gef.editor.control.state.util.TotalMouseAdapter;

/**
 * Ovde bi trebalo ubaciti skrolovanje koristeći right-click, zoom preko točka i skrolovanja kada se
 * pri dragu priđe ivici panela. Ostala ponašanja treba da naslede ove mogućnosti, samo ubace neke
 * dodatne, svoje.
 * 
 * @author Boca
 * 
 */
public class DefaultMouseBehaviour extends TotalMouseAdapter {
	protected BlockDiagram diagram;

	public DefaultMouseBehaviour (BlockDiagram diagram) {
		this.diagram = diagram;
	}
}
