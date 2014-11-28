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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class AgendaPage extends DynamicContentPage<LecturesEntity> {

	public AgendaPage(Context c) {
		super(c);
	}

	private class LectureViewTag {
		ImageView iconView;
		TextView nameView, lecturerView, timeView;
	}

	@Override
	protected View getView(LecturesEntity l, LayoutInflater inflater, View convertView){
		LectureViewTag t;
		if (convertView == null) {
			ViewGroup v = (ViewGroup) inflater.inflate(R.layout.lecture, getListView(), false);
			MainActivity.setFont(v, FontStyle.SEMILIGHT);
			t = new LectureViewTag();
			t.iconView = (ImageView) v.findViewById(R.id.icon);
			t.nameView = (TextView) v.findViewById(R.id.name);
			t.lecturerView = (TextView) v.findViewById(R.id.lecturer);
			t.timeView = (TextView) v.findViewById(R.id.time);
			v.setTag(t);
			convertView = v;
		} else {
			t = (LectureViewTag) convertView.getTag();
		}
		Bitmaps.loadNetBitmapAsync(l.getIconURL(), t.iconView.getWidth(), t.iconView.getHeight(), true)
				.result(t.iconView).start();
		t.nameView.setText(l.getName());
		t.lecturerView.setText(l.getLecturer());
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
		setListView((PullToRefreshListView) findViewById(R.id.agenda_list));
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				LecturePage lp = new LecturePage(getContext());
				lp.setLecture(getItem(position));
				((MainActivity) getContext()).setContentPage(lp);
			}

		});
		setProgressBar((ProgressBar) findViewById(R.id.progress_bar));
	}

}