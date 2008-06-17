package edu.raf.gef.editor.structure;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class SerializedDiagramObject {
	private String xml;

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public SerializedDiagramObject(String xml) {
		super();
		this.xml = xml;
	}

	/**
	 * By default deserialized via XStream.
	 * 
	 * @return New instance of this serialization
	 */
	public Object deserialize() {
		XStream xs = new XStream(new DomDriver());
		return xs.fromXML(xml);
	}

}
