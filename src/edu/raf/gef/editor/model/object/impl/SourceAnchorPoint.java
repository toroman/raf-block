package edu.raf.gef.editor.model.object.impl;

import java.awt.Color;
import java.awt.geom.Point2D;

import edu.raf.gef.editor.model.object.AnchorPointContainer;

public class SourceAnchorPoint extends AnchorPoint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3758853163847711397L;
	private static final Color SOURCE_COLOR = new Color(153, 217, 234);

	public SourceAnchorPoint(AnchorPointContainer parent, Point2D initLocation) {
		super(parent, initLocation);
		setColor(SOURCE_COLOR);
	}

	@Override
	public boolean willAcceptLinkAsDestination(Link link) {
		return false;
	}
}
