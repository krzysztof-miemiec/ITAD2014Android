package pl.polsl.dotnet.itacademicday.layouts;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.layouts.MainActivity.FontStyle;
import pl.polsl.dotnet.itacademicday.views.CounterView;
import android.content.Context;
import android.text.Html;
import android.widget.TextView;

public class AboutPage extends Page {

	public AboutPage(Context c) {
		super(c);
	}

	private CounterView mITADCounter;

	@Override
	protected int getLayoutResourceId(){
		return R.layout.about_fragment;
	}

	@Override
	protected void onCreate(){
		MainActivity.setFont(this, FontStyle.SEMILIGHT);
		mITADCounter = (CounterView) findViewById(R.id.itad_countdown);
		TextView aboutText = (TextView) findViewById(R.id.about_text);
		aboutText.setText(Html.fromHtml(getResources().getString(R.string.about_text)));
		mITADCounter.setEndTime(2014, 12, 2, 8, 0, 0);
		mITADCounter.start();
	}

	@Override
	public void onShow(){
		if (mITADCounter != null) {
			mITADCounter.start();
		}
	}

	@Override
	public void onHide(){
		if (mITADCounter != null)
			mITADCounter.stop();
	}
}