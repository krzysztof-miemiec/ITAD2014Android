package pl.polsl.dotnet.itacademicday.core;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import pl.polsl.dotnet.itacademicday.core.entities.LecturesEntity;
import pl.polsl.dotnet.itacademicday.core.entities.SpeakersEntity;
import pl.polsl.dotnet.itacademicday.core.entities.SponsorsEntity;

@SuppressLint("NewApi")
public class DataFactory {

	protected static String LECTURES_LINK = "http://itadpolsl.pl/lectures/Json";

	protected static String SPONSORS_LINK = "http://itadpolsl.pl/Sponsors/Json";

	protected static String SPEAKERS_LINK = "http://itadpolsl.pl/Speakers/Json";

	protected static JSONParser parser = new JSONParser();

	protected static SharedPreferences sharedPreferences = null;

	public static ArrayList<LecturesEntity> getLecturesData() {
		JSONArray jsonArray = null;
		try {
			jsonArray = parser.getJSONFromUrl(LECTURES_LINK);
			sharedPreferences.edit()
					.putString(LECTURES_LINK, jsonArray.toString()).apply();

		} catch (JSONException e) {
			try {
				jsonArray = new JSONArray(sharedPreferences.getString(
						LECTURES_LINK, "[]"));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		ArrayList<LecturesEntity> lectures = new ArrayList<LecturesEntity>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				lectures.add(new LecturesEntity(jsonArray.getJSONObject(i)));
			} catch (JSONException e) {
				try {
					jsonArray = new JSONArray(sharedPreferences.getString(
							LECTURES_LINK, "[]"));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return lectures;
	}

	public static ArrayList<SponsorsEntity> getSponsorsData() {
		JSONArray jsonArray = null;
		try {
			jsonArray = parser.getJSONFromUrl(SPONSORS_LINK);
			sharedPreferences.edit()
					.putString(SPONSORS_LINK, jsonArray.toString()).apply();
		} catch (JSONException e) {
			try {
				jsonArray = new JSONArray(sharedPreferences.getString(
						SPONSORS_LINK, "[]"));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		ArrayList<SponsorsEntity> sponsors = new ArrayList<SponsorsEntity>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				sponsors.add(new SponsorsEntity(jsonArray.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return sponsors;
	}

	public static ArrayList<SpeakersEntity> getSpeakersData() {
		JSONArray jsonArray = null;
		try {
			jsonArray = parser.getJSONFromUrl(SPEAKERS_LINK);
			sharedPreferences.edit()
					.putString(SPEAKERS_LINK, jsonArray.toString()).apply();
		} catch (JSONException e) {
			try {
				jsonArray = new JSONArray(sharedPreferences.getString(
						SPONSORS_LINK, "[]"));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		ArrayList<SpeakersEntity> speakers = new ArrayList<SpeakersEntity>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				speakers.add(new SpeakersEntity(jsonArray.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return speakers;
	}

	protected static SharedPreferences getSharedPreferences() {
		return sharedPreferences;
	}

	public static void setSharedPreferences(SharedPreferences sharedPreferences) {
		DataFactory.sharedPreferences = sharedPreferences;
	}

}
