package pl.polsl.dotnet.itacademicday.layouts;

import java.util.ArrayList;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.core.DataFactory;
import pl.polsl.dotnet.itacademicday.core.entities.LecturesEntity;
import pl.polsl.dotnet.itacademicday.layouts.MainActivity.FontStyle;
import pl.polsl.dotnet.itacademicday.utils.Bitmaps;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class AgendaFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static AgendaFragment newInstance(int sectionNumber){
		AgendaFragment fragment = new AgendaFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public AgendaFragment() {
	}

	private LayoutInflater mInflater;
	private ListView mList;
	private AgendaAdapter mAdapter;
	private ProgressBar mProgressBar;

	public class AgendaAdapter extends BaseAdapter {
		private ArrayList<LecturesEntity> mAgendaList;

		private class LectureViewTag {
			ImageView iconView;
			TextView nameView, lecturerView;
		}

		public AgendaAdapter() {
			mAgendaList = new ArrayList<LecturesEntity>();
		}

		@Override
		public int getCount(){
			return mAgendaList.size();
		}

		@Override
		public LecturesEntity getItem(int position){
			return mAgendaList.get(position);
		}

		@Override
		public long getItemId(int position){
			return position;
		}

		public void setData(ArrayList<LecturesEntity> l){
			if (l != null) {
				mAgendaList = l;
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run(){
						notifyDataSetChanged();
					}
				});
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			LectureViewTag t;
			if (convertView == null) {
				ViewGroup v = (ViewGroup) mInflater.inflate(R.layout.lecture, mList, false);
				MainActivity.setFont(v, FontStyle.SEMILIGHT);
				t = new LectureViewTag();
				t.iconView = (ImageView) v.findViewById(R.id.icon);
				t.nameView = (TextView) v.findViewById(R.id.name);
				t.lecturerView = (TextView) v.findViewById(R.id.lecturer);
				v.setTag(t);
				convertView = v;
			} else {
				t = (LectureViewTag) convertView.getTag();
			}
			LecturesEntity l = getItem(position);
			Bitmaps.loadNetBitmapAsync(l.getIconURL(), t.iconView.getWidth(), t.iconView.getHeight())
					.result(t.iconView).start();
			t.nameView.setText(l.getName());
			t.lecturerView.setText(l.getLecturer());

			return convertView;
		}
	}

	private void setIsLoading(final boolean isLoading){
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run(){
				mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
			}
		});
	}

	private void errorToast(final Exception e){
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run(){
				Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		mInflater = inflater;
		View rootView = inflater.inflate(R.layout.agenda_fragment, container, false);
		MainActivity.setFont(rootView, FontStyle.REGULAR);
		mList = (ListView) rootView.findViewById(R.id.agenda_list);
		mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
		mAdapter = new AgendaAdapter();
		mList.setAdapter(mAdapter);
		Thread thread = new Thread(new Runnable() {

			@SuppressLint("NewApi")
			@Override
			public void run(){
				setIsLoading(true);
				try {
					if (android.os.Build.VERSION.SDK_INT > 9) {
						StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
						StrictMode.setThreadPolicy(policy);
					}
					mAdapter.setData(DataFactory.getLecturesData());
				} catch (Exception e) {
					e.printStackTrace();
					errorToast(e);
				}
				setIsLoading(false);
			}
		});
		thread.start();
		return rootView;
	}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}

	@Override
	public void onDetach(){
		super.onDetach();
	}
}