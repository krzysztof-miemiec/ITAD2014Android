package pl.polsl.dotnet.itacademicday.layouts;

import java.util.ArrayList;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.core.DataFactory;
import pl.polsl.dotnet.itacademicday.core.entities.SponsorsEntity;
import pl.polsl.dotnet.itacademicday.layouts.MainActivity.FontStyle;
import pl.polsl.dotnet.itacademicday.utils.Bitmaps;
import pl.polsl.dotnet.itacademicday.utils.Bitmaps.RequestResult;
import android.content.Context;
import android.graphics.Bitmap;
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
public class SponsorsPage extends DynamicContentPage<SponsorsEntity> {

	public SponsorsPage(Context c) {
		super(c);
	}

	private class SponsorViewTag {
		ImageView iconView;
		TextView nameView;
	}

	@Override
	protected View getView(SponsorsEntity l, LayoutInflater inflater, View convertView){
		final SponsorViewTag t;
		if (convertView == null) {
			ViewGroup v = (ViewGroup) inflater.inflate(R.layout.sponsor, getGridView(), false);
			MainActivity.setFont(v, FontStyle.SEMILIGHT);
			t = new SponsorViewTag();
			t.iconView = (ImageView) v.findViewById(R.id.icon);
			t.nameView = (TextView) v.findViewById(R.id.name);
			v.setTag(t);
			convertView = v;
		} else {
			t = (SponsorViewTag) convertView.getTag();
		}
		t.nameView.setVisibility(VISIBLE);
		t.nameView.setText(l.getName());
		t.iconView.setImageBitmap(null);
		Bitmaps.loadNetBitmapAsync(l.getUrlLogo(), t.iconView.getWidth(), t.iconView.getHeight(), true)
				.result(t.iconView).result(new RequestResult() {
					@Override
					public void onBitmap(Bitmap b){
						if (b != null)
							((MainActivity) getContext()).runOnUiThread(new Runnable() {
								@Override
								public void run(){
									t.nameView.setVisibility(GONE);
								}
							});
					}
				}).start();

		return convertView;
	}

	@Override
	protected void onCreate(){
		MainActivity.setFont(this, FontStyle.REGULAR);
		MainActivity.setFont(findViewById(R.id.subtitle), FontStyle.LIGHT);
		setGridView((GridView) findViewById(R.id.sponsors_grid));
		setProgressBar((ProgressBar) findViewById(R.id.progress_bar));
		setSubheader((TextView) findViewById(R.id.subtitle));
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
	protected int getGridMaxItemHeight(){
		return LayoutParams.WRAP_CONTENT;
	}

	@Override
	public boolean onBack(){
		((MainActivity) getContext()).setContentPage(new AboutPage(getContext()));
		return true;
	}
}