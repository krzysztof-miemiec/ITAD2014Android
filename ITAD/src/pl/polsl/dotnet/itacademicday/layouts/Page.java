package pl.polsl.dotnet.itacademicday.layouts;

import android.content.Context;
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

	public boolean onBack(){
		return false;
	}
}