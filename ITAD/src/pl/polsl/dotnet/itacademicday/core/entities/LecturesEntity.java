package pl.polsl.dotnet.itacademicday.core.entities;

import android.annotation.SuppressLint;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class LecturesEntity {

	protected int Id = 0;

	protected String Speaker = null;

	protected String Lecturer = null;

	protected String Name = null;

	protected String Sponsor = null;

	protected String StartTimeText = null;

	protected String EndTimeText = null;

	protected boolean Ongoing = false;

	protected String About = null;

	protected Date StartHour = null;

	protected Date EndHour = null;

	protected String IconURL = null;

	public LecturesEntity() {

	}

	public LecturesEntity(JSONObject object) {
		try {
			this.setId(object.getInt("Id"));
			this.setSpeaker(object.getString("Speaker"));
			this.setSponsor(object.getString("Sponsor"));
			this.setLecturer(object.getString("Lecturer"));
			this.setName(object.getString("Name"));
			this.setStartTimeText(object.getString("StartTimeText"));
			this.setEndTimeText(object.getString("EndTimeText"));
			this.setOngoing(object.getBoolean("Ongoing"));
			this.setAbout(object.getString("About"));
			this.setIconURL(object.getString("IconUrl"));
			this.setStartHour(this.getDateJsonFromString(object
					.getString("StartHour")));
			this.setEndHour(this.getDateJsonFromString(object
					.getString("EndHour")));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@SuppressLint("UseValueOf") 
	private Date getDateJsonFromString(String jsonString) {
		jsonString = jsonString.substring(6, jsonString.length() - 2);
		Long l = Long.getLong(jsonString);
		if (l == null)
			l = new Long(0);
		return new Date(l);
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getSpeaker() {
		return Speaker;
	}

	public void setSpeaker(String speaker) {
		Speaker = speaker;
	}

	public String getLecturer() {
		return Lecturer;
	}

	public void setLecturer(String lecturer) {
		Lecturer = lecturer;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getStartTimeText() {
		return StartTimeText;
	}

	public void setStartTimeText(String startTimeText) {
		StartTimeText = startTimeText;
	}

	public String getEndTimeText() {
		return EndTimeText;
	}

	public void setEndTimeText(String endTimeText) {
		EndTimeText = endTimeText;
	}

	public boolean isOngoing() {
		return Ongoing;
	}

	public void setOngoing(boolean ongoing) {
		Ongoing = ongoing;
	}

	public String getAbout() {
		return About;
	}

	public void setAbout(String about) {
		About = about;
	}

	public Date getStartHour() {
		return StartHour;
	}

	public void setStartHour(Date startHour) {
		StartHour = startHour;
	}

	public Date getEndHour() {
		return EndHour;
	}

	public void setEndHour(Date endHour) {
		EndHour = endHour;
	}

	public String getIconURL() {
		return IconURL;
	}

	public void setIconURL(String iconURL) {
		IconURL = iconURL;
	}

	public String getSponsor() {
		return Sponsor;
	}

	public void setSponsor(String sponsor) {
		Sponsor = sponsor;
	}
}
