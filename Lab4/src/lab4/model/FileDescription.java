package lab4.model;

public class FileDescription {
	
	private String name;
	private String extension;
	private long size;
	private String date;
	private boolean folder;
	
	public FileDescription(String name, String extension, long size, String date, boolean folder) {
		this.name = name;
		this.extension = extension; 
		this.size = size;
		this.date = date; 
		this.folder = folder;
	}
	
	public String getName() {
		return name;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public long getSize() {
		return size;
	}
	
	public String getDate() {
		return date;
	}
	
	public boolean isFolder() {
		return folder;
	}
}
