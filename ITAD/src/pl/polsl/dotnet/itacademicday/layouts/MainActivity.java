package pl.polsl.dotnet.itacademicday.layouts;

import java.io.File;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.core.DataFactory;
import pl.polsl.dotnet.itacademicday.utils.Bitmaps;
import pl.polsl.dotnet.itacademicday.utils.WorkerThread;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #refreshHeader()}.
	 */
	private static int mColor;

	private static ConnectivityManager mConManager;
	private static Typeface regular, light, semilight;
	private ViewGroup mHeader;
	private TextView mTitleView;
	private static WorkerThread mWorker;
	private static File mCacheDir;

	public static WorkerThread getWorker() {
		if (mWorker == null) {
			mWorker = new WorkerThread("DataWorkerThread");
		}
		return mWorker;
	}

	public static int getCurrentColor() {
		return mColor;
	}

	public static boolean hasAccessToNetwork() {
		final NetworkInfo activeNetwork = mConManager.getActiveNetworkInfo();
		return (activeNetwork != null && activeNetwork.isConnected());
	}

	public static File getDiskCacheDir() {
		return mCacheDir;
	}

	@SuppressLint({ "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DataFactory.setSharedPreferences(getPreferences(0));
		final String cachePath = ((Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED || !Environment
				.isExternalStorageRemovable()) && getExternalCacheDir() != null) ? getExternalCacheDir()
				.getPath() : getCacheDir().getPath();
		mCacheDir = new File(cachePath);
		Bitmaps.setup();

		if (Build.VERSION.SDK_INT < 16) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			View decorView = getWindow().getDecorView();
			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		}
		setContentView(R.layout.activity_main);
		ActionBar ab = getSupportActionBar();
		if (ab != null)
			ab.hide();
		mHeader = (ViewGroup) findViewById(R.id.action_bar_layout);
		mTitleView = (TextView) mHeader.findViewById(R.id.action_bar_title);
		mTitleView.setTypeface(light);
		final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		ImageButton drawerButton = (ImageButton) mHeader
				.findViewById(R.id.action_bar_drawer_button);
		drawerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				drawerLayout.openDrawer(Gravity.LEFT);
			}
		});

		mConManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		regular = Typeface.createFromAsset(getAssets(), "Segoe.ttf");
		light = Typeface.createFromAsset(getAssets(), "SegoeLight.ttf");
		semilight = Typeface.createFromAsset(getAssets(), "SegoeSemilight.ttf");
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mConManager == null)
			mConManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	@Override
	protected void onStop() {
		if (mWorker != null) {
			mWorker.abortAllTasks();
			mWorker.interrupt();
			mWorker = null;
		}
		super.onStop();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		position++;
		Fragment f = null;
		switch (position) {
		case 1:
			f = AboutFragment.newInstance(position);
			mColor = getResources().getColor(R.color.section1);
			break;
		case 2:
			f = AgendaFragment.newInstance(position);
			mColor = getResources().getColor(R.color.section2);
			break;
		case 3:
			f = WallFragment.newInstance(position);
			mColor = getResources().getColor(R.color.section3);
			break;
		case 4:
			f = SponsorsFragment.newInstance(position);
			mColor = getResources().getColor(R.color.section4);
			break;
		case 5:
			break;
		}
		refreshHeader();
		fragmentManager.beginTransaction().replace(R.id.container, f).commit();
	}

	public void refreshHeader() {
		if (mHeader != null) {
			mHeader.setBackgroundColor(mColor);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			refreshHeader();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	public enum FontStyle {
		REGULAR, LIGHT, SEMILIGHT
	};

	public static void setFont(View v, FontStyle style) {
		if (v instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) v;
			int numChildren = vg.getChildCount();
			for (int i = 0; i < numChildren; i++) {
				setFont(vg.getChildAt(i), style);
			}
		} else if (v instanceof TextView) {
			TextView tv = (TextView) v;
			switch (style) {
			case LIGHT:
				tv.setTypeface(light);
				break;
			case SEMILIGHT:
				tv.setTypeface(semilight);
				break;
			case REGULAR:
			default:
				tv.setTypeface(regular);
				break;
			}

		}
	}
}
