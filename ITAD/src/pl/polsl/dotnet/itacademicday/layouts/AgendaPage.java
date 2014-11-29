package pl.polsl.dotnet.itacademicday.layouts;

import java.util.ArrayList;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.core.DataFactory;
import pl.polsl.dotnet.itacademicday.core.entities.LecturesEntity;
import pl.polsl.dotnet.itacademicday.layouts.MainActivity.FontStyle;
import pl.polsl.dotnet.itacademicday.utils.Bitmaps;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class AgendaPage extends DynamicContentPage<LecturesEntity> {

	public AgendaPage(Context c) {
		super(c);
	}

	private class LectureViewTag {
		ImageView iconView;
		TextView nameView, companyView, lecturerView, timeView;
	}

	@Override
	protected View getView(LecturesEntity l, LayoutInflater inflater, View convertView){
		LectureViewTag t;
		if (convertView == null) {
			ViewGroup v = (ViewGroup) inflater.inflate(R.layout.lecture, getGridView(), false);
			MainActivity.setFont(v, FontStyle.SEMILIGHT);
			t = new LectureViewTag();
			t.iconView = (ImageView) v.findViewById(R.id.icon);
			t.nameView = (TextView) v.findViewById(R.id.name);
			t.companyView = (TextView) v.findViewById(R.id.company);
			t.lecturerView = (TextView) v.findViewById(R.id.lecturer);
			t.timeView = (TextView) v.findViewById(R.id.time);
			v.setTag(t);
			convertView = v;
		} else {
			t = (LectureViewTag) convertView.getTag();
		}
		Bitmaps.loadNetBitmapAsync(l.getIconURL(), t.iconView.getWidth(), t.iconView.getHeight(), true)
				.result(t.iconView).start();
		MainActivity.setString(t.nameView, l.getName());
		MainActivity.setString(t.companyView, l.getSponsor());
		MainActivity.setString(t.lecturerView, l.getLecturer());
		t.timeView.setText(l.getStartTimeText() + " - " + l.getEndTimeText());

		return convertView;
	}

	@Override
	protected ArrayList<LecturesEntity> getData(){
		return DataFactory.getLecturesData();
	}

	@Override
	protected int getLayoutResourceId(){
		return R.layout.agenda_fragment;
	}

	@Override
	public boolean onBack(){
		((MainActivity) getContext()).setContentPage(new AboutPage(getContext()));
		return true;
	}

	@Override
	protected void onCreate(){
		MainActivity.setFont(this, FontStyle.REGULAR);
		MainActivity.setFont(findViewById(R.id.subtitle), FontStyle.LIGHT);
		setGridView((GridView) findViewById(R.id.agenda_grid));
		getGridView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				LecturePage lp = new LecturePage(getContext());
				lp.setLecture(getItem((int) id));
				((MainActivity) getContext()).setContentPage(lp);
			}

		});
		setSubheader((TextView) findViewById(R.id.subtitle));
		setProgressBar((ProgressBar) findViewById(R.id.progress_bar));
	}

}