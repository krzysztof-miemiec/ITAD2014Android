package pl.polsl.dotnet.itacademicday.core.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class SpeakersEntity {

	public int Id = 0;

	public String Name = null;

	public String Company = null;

	public String Bio = null;

	public String ImageUrl = null;

	public SpeakersEntity() {

	}

	public SpeakersEntity(JSONObject object) {
		try {
			this.setId(object.getInt("Id"));
			this.setName(object.getString("Name"));
			this.setCompany(object.getString("Company"));
			this.setImageUrl(object.getString("ImageUrl"));
			this.setBio(object.getString("Bio"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getCompany() {
		return Company;
	}

	public void setCompany(String company) {
		Company = company;
	}

	public String getBio() {
		return Bio;
	}

	public void setBio(String bio) {
		Bio = bio;
	}

	public String getImageUrl() {
		return ImageUrl;
	}

	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}

}
