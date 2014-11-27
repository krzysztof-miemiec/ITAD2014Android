package pl.polsl.dotnet.itacademicday;

import pl.polsl.dotnet.itacademicday.layouts.AboutFragment;
import pl.polsl.dotnet.itacademicday.layouts.AgendaFragment;
import pl.polsl.dotnet.itacademicday.layouts.NavigationDrawerFragment;
import pl.polsl.dotnet.itacademicday.layouts.SponsorsFragment;
import pl.polsl.dotnet.itacademicday.layouts.WallFragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private int mColor;

	private static ConnectivityManager mConManager;

	public static boolean hasAccessToNetwork(){

		final NetworkInfo activeNetwork = mConManager.getActiveNetworkInfo();
		return (activeNetwork != null && activeNetwork.isConnected());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mConManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(
				R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	protected void onResume(){
		super.onResume();
		if (mConManager == null)
			mConManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position){
		FragmentManager fragmentManager = getSupportFragmentManager();
		position++;
		Fragment f = null;
		switch (position) {
			case 1:
				f = AboutFragment.newInstance(position);
				break;
			case 2:
				f = AgendaFragment.newInstance(position);
				break;
			case 3:
				f = WallFragment.newInstance(position);
				break;
			case 4:
				f = SponsorsFragment.newInstance(position);
				break;
			case 5:
				break;
		}
		fragmentManager.beginTransaction().replace(R.id.container, f).commit();
	}

	public void onSectionAttached(int number){
		switch (number) {
			case 1:
				mTitle = getString(R.string.title_section1);
				mColor = getResources().getColor(R.color.section1);
				break;
			case 2:
				mTitle = getString(R.string.title_section2);
				mColor = getResources().getColor(R.color.section2);
				break;
			case 3:
				mTitle = getString(R.string.title_section3);
				mColor = getResources().getColor(R.color.section3);
				break;
			case 4:
				mTitle = getString(R.string.title_section4);
				mColor = getResources().getColor(R.color.section4);
				break;
		}
	}

	@SuppressWarnings("deprecation")
	public void restoreActionBar(){
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
		actionBar.setBackgroundDrawable(new ColorDrawable(mColor));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

}
