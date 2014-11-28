package pl.polsl.dotnet.itacademicday.layouts;

import java.io.File;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.core.DataFactory;
import pl.polsl.dotnet.itacademicday.utils.Bitmaps;
import pl.polsl.dotnet.itacademicday.utils.WorkerThread;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

public class MainActivity extends ActionBarActivity implements NavigationView.NavigationDrawerCallbacks {

	private static NavigationView mNavigationDrawer;

	private static int mColor;

	private static ConnectivityManager mConManager;
	private static Typeface regular, light, semilight;
	private ViewGroup mHeader;
	private TextView mTitleView;
	private Page mContentView;
	private FrameLayout mContentHolder;
	private static WorkerThread mWorker;
	private static File mCacheDir;

	public static WorkerThread getWorker(){
		if (mWorker == null) {
			mWorker = new WorkerThread("DataWorkerThread");
		}
		return mWorker;
	}

	public static int getCurrentColor(){
		return mColor;
	}

	public static boolean hasAccessToNetwork(){
		final NetworkInfo activeNetwork = mConManager.getActiveNetworkInfo();
		return (activeNetwork != null && activeNetwork.isConnected());
	}

	public static File getDiskCacheDir(){
		return mCacheDir;
	}

	@SuppressLint({ "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		DataFactory.setSharedPreferences(getPreferences(0));
		mCacheDir = new File(getCacheDir().getPath());
		Bitmaps.setup();

		setContentView(R.layout.activity_main);
		ActionBar ab = getSupportActionBar();
		if (ab != null)
			ab.hide();
		mHeader = (ViewGroup) findViewById(R.id.action_bar_layout);
		mTitleView = (TextView) mHeader.findViewById(R.id.action_bar_title);
		mTitleView.setTypeface(light);
		final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		ImageButton drawerButton = (ImageButton) mHeader.findViewById(R.id.action_bar_drawer_button);
		drawerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				drawerLayout.openDrawer(Gravity.LEFT);
			}
		});

		mConManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		regular = Typeface.createFromAsset(getAssets(), "Segoe.ttf");
		light = Typeface.createFromAsset(getAssets(), "SegoeLight.ttf");
		semilight = Typeface.createFromAsset(getAssets(), "SegoeSemilight.ttf");
		mNavigationDrawer = new NavigationView(this, (ListView) findViewById(R.id.navigation_drawer),
				(DrawerLayout) findViewById(R.id.drawer_layout));
		mNavigationDrawer.setCallbacks(this);
		onNavigationDrawerItemSelected(0);
	}

	@Override
	protected void onResume(){
		super.onResume();
		if (mConManager == null)
			mConManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	@SuppressLint("InlinedApi")
	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			if (Build.VERSION.SDK_INT < 16) {
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
			} else {
				getWindow().getDecorView().setSystemUiVisibility(
						View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
								| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
								| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
			}
		}
	}

	@Override
	protected void onStop(){
		if (mWorker != null) {
			mWorker.abortAllTasks();
			mWorker.interrupt();
			mWorker = null;
		}
		super.onStop();
	}

	@Override
	public void onBackPressed(){
		if (mContentView != null) {
			if (!mContentView.onBack()) {
				super.onBackPressed();
			}
		}

	}

	private static final FrameLayout.LayoutParams contentLayoutParams = new FrameLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

	@Override
	public void onNavigationDrawerItemSelected(int position){
		if (mContentHolder == null)
			mContentHolder = (FrameLayout) findViewById(R.id.container);
		position++;
		Page v = null;
		switch (position) {
			case 1:
				v = new AboutPage(this);
				mColor = getResources().getColor(R.color.section1);
				break;
			case 2:
				v = new AgendaPage(this);
				mColor = getResources().getColor(R.color.section2);
				break;
			case 3:
				v = new WallPage(this);
				mColor = getResources().getColor(R.color.section3);
				break;
			case 4:
				v = new SponsorsPage(this);
				mColor = getResources().getColor(R.color.section4);
				break;
		}
		setContentPage(v);
	}

	public void setContentPage(final Page newPage){
		refreshColors();
		mContentHolder.addView(newPage, contentLayoutParams);
		if (mContentView != null) {
			Animation anim = AnimationUtils.loadAnimation(mContentHolder.getContext(), R.anim.abc_slide_in_bottom);
			anim.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation){
					mContentView.onShow();
				}

				@Override
				public void onAnimationRepeat(Animation animation){
				}

				@Override
				public void onAnimationEnd(Animation animation){
					mContentView.onHide();
					mContentHolder.removeView(mContentView);
					mContentView = newPage;
				}
			});
			newPage.startAnimation(anim);
		} else {
			mContentView = newPage;
			mContentView.onShow();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
	}

	public void refreshColors(){
		if (mHeader != null && mNavigationDrawer != null) {
			int startColor;
			try {
				startColor = ((ColorDrawable) mHeader.getBackground()).getColor();
			} catch (Exception e) {
				startColor = Color.WHITE;
			}
			mNavigationDrawer.setBackgroundColor(mColor);
			ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, mColor);
			colorAnimation.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animator){
					mHeader.setBackgroundColor((Integer) animator.getAnimatedValue());
				}
			});
			colorAnimation.setDuration(400);
			colorAnimation.start();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		if (!mNavigationDrawer.isDrawerOpen()) {
			refreshColors();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	public enum FontStyle {
		REGULAR, LIGHT, SEMILIGHT
	};

	public static void setFont(View v, FontStyle style){
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

	public static void tintView(View v, int color){
		if (v instanceof ImageButton) {
			ImageButton ib = (ImageButton) v;
			Drawable d = ib.getDrawable();
			d.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
		} else if (v instanceof TextView) {
			TextView tv = (TextView) v;
			Drawable d[] = tv.getCompoundDrawables();
			for (int i = 0; i < d.length; i++) {
				if (d[i] != null)
					d[i].setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
			}
		} else if (v instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) v;
			for (int i = 0; i < vg.getChildCount(); i++) {
				tintView(vg.getChildAt(i), color);
			}
		}
	}
}
