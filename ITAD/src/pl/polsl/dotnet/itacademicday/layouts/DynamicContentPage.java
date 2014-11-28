package pl.polsl.dotnet.itacademicday.layouts;

import java.util.ArrayList;

import pl.polsl.dotnet.itacademicday.utils.WorkerThread;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
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

public abstract class DynamicContentPage<T> extends Page {

	public DynamicContentPage(Context c) {
		super(c);

		mList.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView){
				loadData();
			}
		});
		mAdapter = new GenericAdapter();
		mList.setAdapter(mAdapter);
		loadData();
	}

	private PullToRefreshListView mList;
	private GenericAdapter mAdapter;
	private ProgressBar mProgressBar;

	protected abstract View getView(T data, LayoutInflater inflater, View view);

	protected T getItem(int position){
		return mAdapter.mGenericList.get(position);
	}

	public class GenericAdapter extends BaseAdapter {
		private ArrayList<T> mGenericList;

		public GenericAdapter() {
			mGenericList = new ArrayList<T>();
		}

		@Override
		public int getCount(){
			return mGenericList.size();
		}

		@Override
		public T getItem(int position){
			return mGenericList.get(position);
		}

		@Override
		public long getItemId(int position){
			return position;
		}

		public void setData(ArrayList<T> l){
			if (l != null) {
				mGenericList = l;
				post(new Runnable() {
					@Override
					public void run(){
						notifyDataSetChanged();
					}
				});
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			return DynamicContentPage.this.getView(getItem(position), LayoutInflater.from(getContext()), convertView);
		}
	}

	private void setIsLoading(final boolean isLoading){
		post(new Runnable() {
			@Override
			public void run(){
				mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
				mList.setRefreshing(isLoading);
				if (!isLoading) {
					mAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	private void errorToast(final Exception e){
		post(new Runnable() {
			@Override
			public void run(){
				Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
			}
		});
	}

	protected abstract ArrayList<T> getData();

	protected void setListView(PullToRefreshListView ptrlv){
		mList = ptrlv;
	}

	protected PullToRefreshListView getListView(){
		return mList;
	}

	protected void setProgressBar(ProgressBar pb){
		mProgressBar = pb;
	}

	protected ProgressBar getProgressBar(){
		return mProgressBar;
	}

	private Runnable mLoadDataRunnable = new Runnable() {
		@SuppressLint("NewApi")
		@Override
		public void run(){
			setIsLoading(true);
			try {
				if (android.os.Build.VERSION.SDK_INT > 9) {
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
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

	private void loadData(){
		WorkerThread worker = MainActivity.getWorker();
		worker.abortAllTasks();
		worker.addTask(mLoadDataRunnable);
	}

}