package edu.raf.gef.editor.model.object;

public interface ResizePoint {
	public double getX();

	public double getY();

	public void setLocation(double x, double y);

	public final String LOCATION_PROPERTY = "location";
}
