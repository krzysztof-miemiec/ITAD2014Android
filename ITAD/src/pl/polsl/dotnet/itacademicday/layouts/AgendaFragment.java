package pl.polsl.dotnet.itacademicday.layouts;

import java.util.ArrayList;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.core.DataFactory;
import pl.polsl.dotnet.itacademicday.core.entities.LecturesEntity;
import pl.polsl.dotnet.itacademicday.layouts.MainActivity.FontStyle;
import pl.polsl.dotnet.itacademicday.utils.Bitmaps;
import pl.polsl.dotnet.itacademicday.utils.Bitmaps.RequestResult;
import pl.polsl.dotnet.itacademicday.utils.WorkerThread;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class AgendaFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static AgendaFragment newInstance(int sectionNumber) {
		AgendaFragment fragment = new AgendaFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public AgendaFragment() {
	}

	private LayoutInflater mInflater;
	private PullToRefreshListView mList;
	private AgendaAdapter mAdapter;
	private ProgressBar mProgressBar;

	public class AgendaAdapter extends BaseAdapter {
		private ArrayList<LecturesEntity> mAgendaList;

		private class LectureViewTag {
			ImageView iconView;
			TextView nameView, lecturerView, timeView;
		}

		public AgendaAdapter() {
			mAgendaList = new ArrayList<LecturesEntity>();
		}

		@Override
		public int getCount() {
			return mAgendaList.size();
		}

		@Override
		public LecturesEntity getItem(int position) {
			return mAgendaList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void setData(ArrayList<LecturesEntity> l) {
			if (l != null) {
				mAgendaList = l;
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						notifyDataSetChanged();
					}
				});
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LectureViewTag t;
			if (convertView == null) {
				ViewGroup v = (ViewGroup) mInflater.inflate(R.layout.lecture,
						mList, false);
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
			LecturesEntity l = getItem(position);
			final ImageView iv = t.iconView;
			Bitmaps.loadNetBitmapAsync(l.getIconURL(), t.iconView.getWidth(),
					t.iconView.getHeight(), true).result(new RequestResult() {
				@Override
				public void onBitmap(final Bitmap b) {
					mList.post(new Runnable() {
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
			t.timeView.setText(l.getStartTimeText() + " - "
					+ l.getEndTimeText());

			return convertView;
		}
	}

	private void setIsLoading(final boolean isLoading) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mProgressBar
						.setVisibility(isLoading ? View.VISIBLE : View.GONE);
				mList.setRefreshing(isLoading);
				if (!isLoading) {
					mAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	private void errorToast(final Exception e) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getActivity(), "Error: " + e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		View rootView = inflater.inflate(R.layout.agenda_fragment, container,
				false);
		MainActivity.setFont(rootView, FontStyle.REGULAR);
		MainActivity.setFont(rootView.findViewById(R.id.subtitle),
				FontStyle.LIGHT);

		mList = (PullToRefreshListView) rootView.findViewById(R.id.agenda_list);
		mList.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData();
				Toast.makeText(mList.getContext(), "d", Toast.LENGTH_LONG)
						.show();
			}
		});
		mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
		mAdapter = new AgendaAdapter();
		mList.setAdapter(mAdapter);
		loadData();
		return rootView;
	}

	private Runnable mLoadDataRunnable = new Runnable() {
		@SuppressLint("NewApi")
		@Override
		public void run() {
			setIsLoading(true);
			try {
				if (android.os.Build.VERSION.SDK_INT > 9) {
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
							.permitAll().build();
					StrictMode.setThreadPolicy(policy);
				}
				mAdapter.setData(DataFactory.getLecturesData());
			} catch (Exception e) {
				e.printStackTrace();
				errorToast(e);
			}
			setIsLoading(false);
		}
	};

	private void loadData() {
		WorkerThread worker = MainActivity.getWorker();
		worker.abortAllTasks();
		worker.addTask(mLoadDataRunnable);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
}