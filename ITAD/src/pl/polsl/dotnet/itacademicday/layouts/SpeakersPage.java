package pl.polsl.dotnet.itacademicday.layouts;

import java.util.ArrayList;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.core.DataFactory;
import pl.polsl.dotnet.itacademicday.core.entities.SpeakersEntity;
import pl.polsl.dotnet.itacademicday.layouts.MainActivity.FontStyle;
import pl.polsl.dotnet.itacademicday.utils.Bitmaps;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class SpeakersPage extends DynamicContentPage<SpeakersEntity> {

	public SpeakersPage(Context c) {
		super(c);
	}

	private class SpeakerViewTag {
		ImageView iconView;
		TextView nameView, companyView, descriptionView;
	}

	@Override
	protected View getView(SpeakersEntity l, LayoutInflater inflater, View convertView){
		final SpeakerViewTag t;
		if (convertView == null) {
			ViewGroup v = (ViewGroup) inflater.inflate(R.layout.speaker, getGridView(), false);
			MainActivity.setFont(v, FontStyle.SEMILIGHT);
			t = new SpeakerViewTag();
			t.iconView = (ImageView) v.findViewById(R.id.icon);
			t.nameView = (TextView) v.findViewById(R.id.name);
			t.companyView = (TextView) v.findViewById(R.id.company);
			t.descriptionView = (TextView) v.findViewById(R.id.description);
			v.setTag(t);
			convertView = v;
		} else {
			t = (SpeakerViewTag) convertView.getTag();
		}
		Bitmaps.loadNetBitmapAsync(l.getImageUrl(), t.iconView.getWidth(), t.iconView.getHeight(), true)
				.result(t.iconView).start();
		t.nameView.setText(l.getName());
		t.companyView.setText(l.getCompany());
		t.descriptionView.setText(l.getBio());

		return convertView;
	}

	@Override
	protected void onCreate(){
		MainActivity.setFont(this, FontStyle.REGULAR);
		MainActivity.setFont(findViewById(R.id.subtitle), FontStyle.LIGHT);
		setGridView((GridView) findViewById(R.id.lecturers_grid));
		setProgressBar((ProgressBar) findViewById(R.id.progress_bar));
		setSubheader((TextView) findViewById(R.id.subtitle));
	}

	@Override
	protected ArrayList<SpeakersEntity> getData(){
		return DataFactory.getSpeakersData();
	}

	@Override
	protected int getLayoutResourceId(){
		return R.layout.speakers_fragment;
	}

	@Override
	public boolean onBack(){
		((MainActivity) getContext()).setContentPage(new AboutPage(getContext()));
		return true;
	}

}