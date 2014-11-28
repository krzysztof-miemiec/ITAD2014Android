package pl.polsl.dotnet.itacademicday.layouts;

import java.util.ArrayList;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.core.DataFactory;
import pl.polsl.dotnet.itacademicday.core.entities.SponsorsEntity;
import pl.polsl.dotnet.itacademicday.layouts.MainActivity.FontStyle;
import pl.polsl.dotnet.itacademicday.utils.Bitmaps;
import android.content.Context;
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
public class SponsorsPage extends DynamicContentPage<SponsorsEntity> {

	public SponsorsPage(Context c) {
		super(c);
	}

	private class SponsorViewTag {
		ImageView iconView;
		TextView nameView, descriptionView;
	}

	@Override
	protected View getView(SponsorsEntity l, LayoutInflater inflater, View convertView){
		SponsorViewTag t;
		if (convertView == null) {
			ViewGroup v = (ViewGroup) inflater.inflate(R.layout.sponsor, getListView(), false);
			MainActivity.setFont(v, FontStyle.REGULAR);
			t = new SponsorViewTag();
			t.iconView = (ImageView) v.findViewById(R.id.icon);
			t.nameView = (TextView) v.findViewById(R.id.name);
			t.descriptionView = (TextView) v.findViewById(R.id.description);
			v.setTag(t);
			convertView = v;
		} else {
			t = (SponsorViewTag) convertView.getTag();
		}
		Bitmaps.loadNetBitmapAsync(l.getUrlLogo(), t.iconView.getWidth(), t.iconView.getHeight(), true)
				.result(t.iconView).start();
		t.nameView.setText(l.getName());
		t.descriptionView.setText(l.getAbout());

		return convertView;
	}

	@Override
	protected void onCreate(){
		MainActivity.setFont(this, FontStyle.REGULAR);
		MainActivity.setFont(findViewById(R.id.subtitle), FontStyle.LIGHT);
		setListView((PullToRefreshListView) findViewById(R.id.sponsors_list));
		setProgressBar((ProgressBar) findViewById(R.id.progress_bar));
	}

	@Override
	protected ArrayList<SponsorsEntity> getData(){
		return DataFactory.getSponsorsData();
	}

	@Override
	protected int getLayoutResourceId(){
		return R.layout.sponsors_fragment;
	}

	@Override
	public boolean onBack(){
		((MainActivity) getContext()).setContentPage(new AboutPage(getContext()));
		return true;
	}
}