package edu.raf.gef.editor.control;

import edu.raf.gef.editor.model.object.Focusable;

public class GefFocusEvent {
	public static final int FOCUS_GIVEN = 1;
	public static final int FOCUS_LOST = 2;
	public static final int FOCUS_CLEARED = 4;
	
	public final DiagramController source;
	public final Focusable focused;
	public final int type;

	public GefFocusEvent(DiagramController source, Focusable focused, int type) {
		this.source = source;
		this.focused = focused;
		this.type = type;
	}

}
