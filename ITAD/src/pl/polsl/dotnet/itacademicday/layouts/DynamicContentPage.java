package pl.polsl.dotnet.itacademicday.layouts;

import java.util.ArrayList;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.utils.WorkerThread;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public abstract class DynamicContentPage<T> extends Page {

	public DynamicContentPage(Context c) {
		super(c);

		mAdapter = new GenericAdapter();
		mGrid.setAdapter(mAdapter);
		mGrid.invalidateViews();
		mAdapter.notifyDataSetChanged();
		loadData();
		onOrientationChange(getResources().getConfiguration().orientation);
	}

	private GridView mGrid;
	private GenericAdapter mAdapter;
	private ProgressBar mProgressBar;
	private TextView mSubheader;

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
			View v = DynamicContentPage.this.getView(getItem(position), LayoutInflater.from(getContext()), convertView);
			adjustViewHeight(v);
			return v;
		}
	}

	private void setIsLoading(final boolean isLoading){
		post(new Runnable() {
			@Override
			public void run(){
				mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
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

	protected void setGridView(GridView ptrlv){
		mGrid = ptrlv;
	}

	protected GridView getGridView(){
		return mGrid;
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

	protected void setSubheader(TextView subheader){
		mSubheader = subheader;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
		GridView gv = getGridView();
		if (gv != null) {
			int columns = (int) Math.floor(Math.max(1, w / getResources().getDimension(R.dimen.column_width)));
			gv.setNumColumns(columns);
			for (int i = 0; i < gv.getChildCount(); i++) {
				adjustViewHeight(getChildAt(i));
			}
			mAdapter.notifyDataSetChanged();
			gv.invalidateViews();
		}
	}

	private void adjustViewHeight(View v){
		GridView.LayoutParams lp = (GridView.LayoutParams) v.getLayoutParams();
		if (getNumColumnsCompat() == 1) {
			if (lp.height != LayoutParams.WRAP_CONTENT) {
				lp.height = LayoutParams.WRAP_CONTENT;
				v.setLayoutParams(lp);
			}
		} else {
			int gh = getGridMaxItemHeight();
			if (lp.height != gh) {
				lp.height = gh;
				v.setLayoutParams(lp);
			}
		}

	}

	@Override
	protected void onPortrait(){
		if (mSubheader != null) {
			mSubheader.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.subtitle_font));
			int p = (int) getResources().getDimension(R.dimen.subtitle_padding);
			mSubheader.setPadding(p, p, p, p);
		}
	}

	protected int getGridMaxItemHeight(){
		return (int) getResources().getDimension(R.dimen.max_grid_item_height);
	}

	@Override
	protected void onLandscape(){
		if (mSubheader != null) {
			mSubheader
					.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.subtitle_font_small));
			int p = (int) getResources().getDimension(R.dimen.subtitle_padding_small);
			mSubheader.setPadding(p, p, p, p);
		}
	}

	private int getNumColumnsCompat(){
		if (Build.VERSION.SDK_INT >= 11) {
			return mGrid.getNumColumns();

		} else {
			int columns = 0;
			int children = mGrid.getChildCount();
			if (children > 0) {
				int width = mGrid.getChildAt(0).getMeasuredWidth();
				if (width > 0) {
					columns = mGrid.getWidth() / width;
				}
			}
			return columns > 0 ? columns : GridView.AUTO_FIT;
		}
	}
}