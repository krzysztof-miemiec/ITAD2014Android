package pl.polsl.dotnet.itacademicday.layouts;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

public abstract class Page extends FrameLayout {
	public Page(Context c) {
		super(c);
		LayoutInflater.from(getContext()).inflate(getLayoutResourceId(), this, true);
		onCreate();
	}

	protected abstract int getLayoutResourceId();

	protected abstract void onCreate();

	public void onShow(){
	}

	public void onHide(){
	}

	protected void onPortrait(){

	}

	protected void onLandscape(){

	}

	public boolean onBack(){
		return false;
	}

	public void onOrientationChange(int orientation){
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			onLandscape();
		} else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			onPortrait();
		}
	}
}