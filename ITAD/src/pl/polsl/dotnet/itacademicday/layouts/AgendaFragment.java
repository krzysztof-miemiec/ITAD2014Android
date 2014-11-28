package pl.polsl.dotnet.itacademicday.layouts;

import java.util.ArrayList;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.core.DataFactory;
import pl.polsl.dotnet.itacademicday.core.entities.LecturesEntity;
import pl.polsl.dotnet.itacademicday.layouts.MainActivity.FontStyle;
import pl.polsl.dotnet.itacademicday.utils.Bitmaps;
import pl.polsl.dotnet.itacademicday.utils.Bitmaps.RequestResult;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class AgendaFragment extends NetFragment<LecturesEntity> {

	@Override
	public View getView(LecturesEntity l, LayoutInflater inflater,
			View convertView) {
		LectureViewTag t;
		if (convertView == null) {
			ViewGroup v = (ViewGroup) inflater.inflate(R.layout.lecture,
					getListView(), false);
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
		final ImageView iv = t.iconView;
		Bitmaps.loadNetBitmapAsync(l.getIconURL(), t.iconView.getWidth(),
				t.iconView.getHeight(), true).result(new RequestResult() {
			@Override
			public void onBitmap(final Bitmap b) {
				iv.post(new Runnable() {
					@Override
					public void run() {
						iv.setImageBitmap(b);
						iv.invalidate();
					}
				});
			}
		}).start();
		t.nameView.setText(l.getName());
		t.lecturerView.setText(l.getLecturer());
		t.timeView.setText(l.getStartTimeText() + " - " + l.getEndTimeText());

		return convertView;
	}

	@Override
	protected ArrayList<LecturesEntity> getData() {
		return DataFactory.getLecturesData();
	}

	@Override
	protected View onCreate(LayoutInflater inflater, ViewGroup container) {
		View rootView = inflater.inflate(R.layout.agenda_fragment, container,
				false);
		MainActivity.setFont(rootView, FontStyle.REGULAR);
		MainActivity.setFont(rootView.findViewById(R.id.subtitle),
				FontStyle.LIGHT);
		setListView((PullToRefreshListView) rootView
				.findViewById(R.id.agenda_list));
		setProgressBar((ProgressBar) rootView.findViewById(R.id.progress_bar));
		return rootView;
	}

}