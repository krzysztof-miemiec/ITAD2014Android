package pl.polsl.dotnet.itacademicday.models;

import android.graphics.Bitmap;

public class Sponsor {
	private Bitmap image;
	private String name, description;

	public Sponsor(String name, String description, Bitmap image) {
		this.name = name;
		this.description = description;
		this.image = image;
	}

	public String getName(){
		return name;
	}

	public String getDescription(){
		return description;
	}

	public Bitmap getImage(){
		return image;
	}

}