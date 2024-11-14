package model;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String label;
	
	private Object data;

	public Message(String label, Object data) {
		super();
		this.label = label;
		this.data = data;
	}
	
	
	

	public Message(String label) {
		super();
		this.label = label;
	}
//////////////////////////



	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	

	
	
	
	

}
