package de.skaliant.wax.util;

/**
 * 
 * 
 * @author Udo Kastilan
 */
public class AccessibleBean {
	private String readonly = null;
	@SuppressWarnings("unused")
	private String writeonly = null;
	private String readwrite = null;


	public String getReadwrite() {
		return readwrite;
	}


	public void setReadwrite(String readwrite) {
		this.readwrite = readwrite;
	}


	public String getReadonly() {
		return readonly;
	}


	public void setWriteonly(String writeonly) {
		this.writeonly = writeonly;
	}
}
