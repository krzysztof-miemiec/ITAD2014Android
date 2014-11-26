package pl.polsl.dotnet.itacademicday.core;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import pl.polsl.dotnet.itacademicday.core.entities.LecturesEntity;
import pl.polsl.dotnet.itacademicday.core.entities.SponsorsEntity;

public class DataFactory {
	
	protected static String LECTURES_LINK = "http://itadpolsl.pl/lectures/Json";
	
	protected static String SPONSORS_LINK = "http://itadpolsl.pl/Sponsors/Json";
	
	protected static JSONParser parser = new JSONParser();
	
	public static ArrayList<LecturesEntity> getLecturesData(){
		JSONArray jsonArray = null;
		try {
			jsonArray = parser.getJSONFromUrl(LECTURES_LINK);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<LecturesEntity> lectures= new ArrayList<LecturesEntity>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {	
				lectures.add(new LecturesEntity(jsonArray.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return lectures;
	}
	
	public static ArrayList<SponsorsEntity> getSponsorsData(){
		JSONArray jsonArray = null;
		try {
			jsonArray = parser.getJSONFromUrl(SPONSORS_LINK);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<SponsorsEntity> sponsors= new ArrayList<SponsorsEntity>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {	
				sponsors.add(new SponsorsEntity(jsonArray.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return sponsors;
	}

}
