package pl.polsl.dotnet.itacademicday.layouts;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.core.entities.LecturesEntity;
import pl.polsl.dotnet.itacademicday.layouts.MainActivity.FontStyle;
import pl.polsl.dotnet.itacademicday.utils.Bitmaps;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class LecturePage extends Page {

	private TextView nameView, timeView, sponsorView, lecturerView, descriptionView;
	private ImageView imageView;

	private LecturesEntity mLecture;

	public LecturePage(Context c) {
		super(c);
	}

	@Override
	protected int getLayoutResourceId(){
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return R.layout.lecture_expanded_horizontal;
		}
		return R.layout.lecture_expanded;
	}

	@Override
	public boolean onBack(){
		((MainActivity) getContext()).setContentPage(new AgendaPage(getContext()));
		return true;
	}

	@Override
	protected void onCreate(){
		MainActivity.setFont(this, FontStyle.LIGHT);
		nameView = (TextView) findViewById(R.id.name);
		timeView = (TextView) findViewById(R.id.time);
		lecturerView = (TextView) findViewById(R.id.lecturer);
		sponsorView = (TextView) findViewById(R.id.sponsor);
		imageView = (ImageView) findViewById(R.id.image);
		descriptionView = (TextView) findViewById(R.id.description);
		ImageButton back = (ImageButton) findViewById(R.id.back);
		MainActivity.tintView(this, Color.WHITE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){
				((MainActivity) getContext()).setContentPage(new AgendaPage(getContext()));
			}
		});
	}

	public void setLecture(LecturesEntity lecture){
		mLecture = lecture;
		MainActivity.setString(nameView, lecture.getName());
		MainActivity.setString(lecturerView, lecture.getLecturer());
		MainActivity.setString(sponsorView, lecture.getSponsor());
		timeView.setText(lecture.getStartTimeText() + " - " + lecture.getEndTimeText());
		MainActivity.setString(descriptionView, lecture.getAbout());
		Bitmaps.loadNetBitmapAsync(lecture.getIconURL(), imageView.getWidth(), imageView.getHeight(), true)
				.result(imageView).start();
	}

	@Override
	public void onOrientationChange(int orientation){
		super.onOrientationChange(orientation);
		removeViewAt(0);
		LayoutInflater.from(getContext()).inflate(getLayoutResourceId(), this, true);
		onCreate();
		setLecture(mLecture);
	}
}