package edu.raf.gef.editor.model.object.impl;

import java.awt.Color;
import java.awt.geom.Point2D;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.AnchorPointContainer;

public class DestinationAnchorPoint extends AnchorPoint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6339033486504339001L;
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
