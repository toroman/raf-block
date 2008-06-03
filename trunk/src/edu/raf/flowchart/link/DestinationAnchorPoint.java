package edu.raf.flowchart.link;

import java.awt.Color;
import java.awt.geom.Point2D;

import edu.raf.gef.editor.model.object.AnchorPointContainer;
import edu.raf.gef.editor.model.object.impl.AnchorPoint;
import edu.raf.gef.editor.model.object.impl.Link;

public class DestinationAnchorPoint extends AnchorPoint {

	private static final Color destinationColor = new Color (255, 249, 189);
	
	public DestinationAnchorPoint(AnchorPointContainer parent, Point2D initLocation) {
		super(parent, initLocation);
		setColor(destinationColor);
	}
	
	@Override
	public boolean willAcceptLinkAsSource(Link link) {
		return false;
	}
}
