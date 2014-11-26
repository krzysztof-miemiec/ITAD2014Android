package pl.polsl.dotnet.itacademicday.core.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class SponsorsEntity {
	
	public int Id = 0;
	
	public String Name = null;
	
	public String About = null;
	
	public String UrlLogo = null;
	
	public SponsorsEntity(){
		
	}
	
	public SponsorsEntity(JSONObject object){
		try {
			this.setId(object.getInt("Id"));
			this.setName(object.getString("Name"));
			this.setAbout(object.getString("About"));
			this.setUrlLogo(object.getString("UrlLogo"));	
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

	public String getAbout() {
		return About;
	}

	public void setAbout(String about) {
		About = about;
	}

	public String getUrlLogo() {
		return UrlLogo;
	}

	public void setUrlLogo(String urlLogo) {
		UrlLogo = urlLogo;
	}
	
}
