package pl.polsl.dotnet.itacademicday.layouts;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.views.CounterView;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class AboutFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static AboutFragment newInstance(int sectionNumber){
		AboutFragment fragment = new AboutFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public AboutFragment() {
	}

	private CounterView mITADCounter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.about_fragment, container, false);
		mITADCounter = (CounterView) rootView.findViewById(R.id.itad_countdown);
		TextView aboutText = (TextView) rootView.findViewById(R.id.about_text);
		aboutText.setText(Html.fromHtml(getResources().getString(R.string.about_text)));
		mITADCounter.setEndTime(2014, 12, 1, 8, 0, 0);
		mITADCounter.start();
		return rootView;
	}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
		if (mITADCounter != null) {
			mITADCounter.start();
		}
	}

	@Override
	public void onDetach(){
		mITADCounter.stop();
		super.onDetach();
	}
}