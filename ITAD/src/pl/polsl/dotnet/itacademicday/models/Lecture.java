package pl.polsl.dotnet.itacademicday.models;

import android.graphics.Bitmap;

public class Lecture {
	private Bitmap image;
	private String name, lecturer;
	private String startTime, endTime;

	public Lecture(String name, String lecturer, String startTime, String endTime, String description) {
		this.name = name;
		this.lecturer = lecturer;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getName(){
		return name;
	}

	public String getLecturer(){
		return lecturer;
	}

	public Bitmap getIcon(){
		return image;
	}

	public String getTime(){
		//SimpleDateFormat f = new SimpleDateFormat("HH:mm", Locale.getDefault());
		//return f.format(startTime) + " - " + f.format(endTime);
		return startTime + " - " + endTime;
	}
}