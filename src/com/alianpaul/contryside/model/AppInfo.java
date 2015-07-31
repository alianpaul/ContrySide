package com.alianpaul.contryside.model;

public class AppInfo {
	private int imageResourceID;
	private String tittle;
	private String packageName;
	private boolean selected;
	private int type;
	
	public AppInfo(int imageResourceID, String tittle, String packageName, boolean selected, int type) {
		this.setImageResourceID(imageResourceID);
		this.setTittle(tittle);
		this.setPackageName(packageName);
		this.setSelected(selected);
		this.setType(type);
	}

	public int getImageResourceID() {
		return imageResourceID;
	}

	public void setImageResourceID(int imageResourceID) {
		this.imageResourceID = imageResourceID;
	}

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
