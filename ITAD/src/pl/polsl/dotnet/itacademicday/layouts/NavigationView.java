package pl.polsl.dotnet.itacademicday.layouts;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.layouts.MainActivity.FontStyle;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NavigationView {

	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
	//private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
	private NavigationDrawerCallbacks mCallbacks;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	private Context mContext;

	private int mCurrentSelectedPosition = 0;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;

	private class NavigationAdapter extends BaseAdapter {
		private String[] array;

		public NavigationAdapter(String[] array) {
			this.array = array;
		}

		@Override
		public int getCount(){
			return array.length;
		}

		@Override
		public String getItem(int position){
			return array[position];
		}

		@Override
		public long getItemId(int position){
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			TextView view;
			if (convertView == null) {
				view = new TextView(mContext);
				view.setTextColor(Color.WHITE);
				int s = (int) mContext.getResources().getDimension(R.dimen.app_title_font);
				view.setTextSize(TypedValue.COMPLEX_UNIT_PX, s);
				view.setPadding(s / 2, s / 2, s / 2, s / 2);
				MainActivity.setFont(view, FontStyle.SEMILIGHT);
			} else {
				view = (TextView) convertView;
			}
			view.setText(getItem(position));
			return view;
		}
	}

	public NavigationView(Context c, ListView list, DrawerLayout drawerLayout) {
		mContext = c;
		mDrawerListView = list;
		MainActivity.setFont(mDrawerListView, FontStyle.LIGHT);
		mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				selectItem(position);
			}
		});
		mDrawerListView.setAdapter(new NavigationAdapter(new String[] { c.getString(R.string.title_section1),
				c.getString(R.string.title_section2), c.getString(R.string.title_section5),
				c.getString(R.string.title_section3), c.getString(R.string.title_section4) }));
		mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
		mDrawerListView.setBackgroundColor(MainActivity.getCurrentColor());
		configureDrawerLayout(drawerLayout);
	}

	public boolean isDrawerOpen(){
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mDrawerListView);
	}

	public void setDrawerOpen(boolean open){
		if (mDrawerLayout != null) {
			if (open) {
				mDrawerLayout.openDrawer(GravityCompat.START);
			} else {
				mDrawerLayout.closeDrawers();
			}
		}
	}

	/**
	 * Users of this fragment must call this method to set up the navigation drawer interactions.
	 *
	 * @param fragmentId   The android:id of this fragment in its activity's layout.
	 * @param drawerLayout The DrawerLayout containing this fragment's UI.
	 */
	public void configureDrawerLayout(DrawerLayout drawerLayout){
		mDrawerLayout = drawerLayout;
		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mDrawerListView);
		}
	}

	private void selectItem(int position){
		mCurrentSelectedPosition = position;
		if (mDrawerListView != null) {
			mDrawerListView.setItemChecked(position, true);
		}
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mDrawerListView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}

	public void setCallbacks(NavigationDrawerCallbacks c){
		mCallbacks = c;
	}

	public void saveState(Bundle outState){
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	public void loadState(Bundle inState){
		selectItem(inState.getInt(STATE_SELECTED_POSITION, 0));
	}

	/**
	 * Callbacks interface that all activities using this fragment must implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int position);
	}

	public void setBackgroundColor(int color){
		mDrawerListView.setBackgroundColor(color);
	}

	public int getBackgroundColor(){
		return ((ColorDrawable) mDrawerListView.getBackground()).getColor();
	}
}
