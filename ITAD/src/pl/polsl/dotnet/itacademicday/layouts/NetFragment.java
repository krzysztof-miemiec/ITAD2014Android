package pl.polsl.dotnet.itacademicday.layouts;

import java.util.ArrayList;

import pl.polsl.dotnet.itacademicday.utils.WorkerThread;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * A placeholder fragment containing a simple view.
 */
public abstract class NetFragment<T> extends Fragment {
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

	public NetFragment() {
	}

	private LayoutInflater mInflater;
	private PullToRefreshListView mList;
	private GenericAdapter mAdapter;
	private ProgressBar mProgressBar;

	public abstract View getView(T data, LayoutInflater inflater, View view);

	public class GenericAdapter extends BaseAdapter {
		private ArrayList<T> mGenericList;

		public GenericAdapter() {
			mGenericList = new ArrayList<T>();
		}

		@Override
		public int getCount() {
			return mGenericList.size();
		}

		@Override
		public T getItem(int position) {
			return mGenericList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void setData(ArrayList<T> l) {
			if (l != null) {
				mGenericList = l;
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
			return NetFragment.this.getView(getItem(position), mInflater,
					convertView);
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
		View rootView = onCreate(inflater, container);

		mList.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData();
				Toast.makeText(mList.getContext(), "d", Toast.LENGTH_LONG)
						.show();
			}
		});
		mAdapter = new GenericAdapter();
		mList.setAdapter(mAdapter);
		loadData();
		return rootView;
	}

	protected abstract ArrayList<T> getData();

	protected void setListView(PullToRefreshListView ptrlv) {
		mList = ptrlv;
	}

	protected PullToRefreshListView getListView() {
		return mList;
	}

	protected void setProgressBar(ProgressBar pb) {
		mProgressBar = pb;
	}

	protected ProgressBar getProgressBar() {
		return mProgressBar;
	}

	protected abstract View onCreate(LayoutInflater inflater,
			ViewGroup container);

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
				mAdapter.setData(getData());
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