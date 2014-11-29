package pl.polsl.dotnet.itacademicday.views;

import java.util.GregorianCalendar;

import pl.polsl.dotnet.itacademicday.R;
import pl.polsl.dotnet.itacademicday.layouts.MainActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class CounterView extends View {

	private Thread mRefreshThread;
	private Paint mWhitePaint, mBackgroundPaint;
	private CounterArc mSeconds, mMinutes, mHours, mDays;
	private long mEndTime;
	private boolean mDone;
	private Handler mHandler;
	private Runnable mInvalidateRunnable = new Runnable() {
		@Override
		public void run(){
			invalidate();
		}
	};

	private class CounterArc {
		private Paint mPaint;
		private RectF mOval, mInnerOval;
		private float mAngle, mDiv, mScale;
		private int mValue;
		private String mText;
		private float mRadius, mCenterX, mCenterY;

		CounterArc(int color, float divider, float scale, String text) {
			mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaint.setColor(color);
			mOval = new RectF();
			mInnerOval = new RectF();
			mDiv = divider;
			mScale = scale;
			mText = text;
		}

		void refreshSize(float w, float h){
			mCenterX = w / 2;
			mCenterY = h / 2;
			if (w > h) {
				w = h;
			} else {
				h = w;
			}

			mRadius = mScale * w / 2;
			mOval.left = mCenterX - mRadius;
			mOval.top = mCenterY - mRadius;
			mOval.right = mCenterX + mRadius;
			mOval.bottom = mCenterY + mRadius;
			mInnerOval.left = mOval.left + THICKNESS;
			mInnerOval.top = mOval.top + THICKNESS;
			mInnerOval.right = mOval.right - THICKNESS;
			mInnerOval.bottom = mOval.bottom - THICKNESS;
		}

		void setValue(float value){
			mValue = (int) value;
			mAngle = 360 * value / mDiv;
		}

		void draw(Canvas c){
			if (!mDone) {
				c.drawArc(mOval, -90, mAngle, true, mPaint);
				c.drawOval(mInnerOval, mBackgroundPaint);
				float cx = (float) Math.sin(Math.PI * mAngle / 180) * (mRadius - THICKNESS / 2) + mCenterX;
				float cy = (float) -Math.cos(Math.PI * mAngle / 180) * (mRadius - THICKNESS / 2) + mCenterY;
				c.drawCircle(cx, cy, THICKNESS * 1.9f, mPaint);
				c.drawText(String.valueOf(mValue), cx, cy - THICKNESS * 0.1f, mWhitePaint);
				c.drawText(mText, cx, cy + THICKNESS * 1.6f, mWhitePaint);
			}
		}
	}

	private float THICKNESS = 10;

	public CounterView(Context c) {
		super(c);
		init();
	}

	public CounterView(Context c, AttributeSet attr) {
		super(c, attr);
		init();
	}

	public CounterView(Context c, AttributeSet attr, int defStyle) {
		super(c, attr, defStyle);
		init();
	}

	private void init(){
		setWillNotDraw(false);
		if (Build.VERSION.SDK_INT >= 11)
			setLayerType(LAYER_TYPE_SOFTWARE, null);
		mHandler = new Handler();
		THICKNESS = getContext().getResources().getDimension(R.dimen.counter_thickness);
		mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mWhitePaint.setColor(Color.WHITE);
		mWhitePaint.setTextAlign(Align.CENTER);
		mWhitePaint.setTextSize(THICKNESS * 2);
		mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		int bgcolor = MainActivity.getDrawableColor(getBackground());
		mBackgroundPaint.setColor(bgcolor);
		mSeconds = new CounterArc(0xFF2196F3, 60, 0.8f, "S");
		mMinutes = new CounterArc(0xFFF44336, 60, 0.6f, "M");
		mHours = new CounterArc(0xFF4CAF50, 24, 0.4f, "H");
		mDays = new CounterArc(0xFFFFC107, 7, 0.2f, "D");
		setupSizing(0, 0);
	}

	private void setupSizing(float w, float h){
		mSeconds.refreshSize(w, h);
		mMinutes.refreshSize(w, h);
		mHours.refreshSize(w, h);
		mDays.refreshSize(w, h);
		mHandler.post(new Runnable() {

			@Override
			public void run(){
				invalidate();
				requestLayout();
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setupSizing(getWidth(), getHeight());
	}

	public interface OnEndListener {
		public void onEnd();
	}

	private OnEndListener mOEL;

	public void setOnEndListner(OnEndListener oel){
		mOEL = oel;
	}

	public void start(){
		if (mRefreshThread != null) {
			stop();
		}
		mRefreshThread = new Thread("CounterThread") {
			@Override
			public void run(){
				try {
					double t, v;

					while (true) {
						t = (mEndTime - System.currentTimeMillis());
						if (t <= 0) {
							mSeconds.setValue(0);
							mMinutes.setValue(0);
							mHours.setValue(0);
							mDays.setValue(0);
							postInvalidate();
							if (mOEL != null)
								mOEL.onEnd();
							break;
						}
						v = t / (24 * 3600 * 1000);
						mDays.setValue((float) v);
						t -= ((int) v) * 24 * 3600 * 1000;
						v = t / (3600 * 1000);
						mHours.setValue((float) v);
						t -= ((int) v) * 3600 * 1000;
						v = t / (60 * 1000);
						mMinutes.setValue((float) v);
						t -= ((int) v) * (60 * 1000);
						v = t / 1000;
						mSeconds.setValue((float) v);

						mHandler.post(mInvalidateRunnable);
						Thread.sleep(40);
					}
				} catch (InterruptedException e) {
				}
			}
		};
		mRefreshThread.start();
	}

	public void stop(){
		if (mRefreshThread != null) {
			mRefreshThread.interrupt();
			mRefreshThread = null;
		}
	}

	public void setEndTime(int year, int month, int day, int hour, int minute, int second){
		GregorianCalendar gc = new GregorianCalendar(year, month - 1, day, hour, minute, second);
		mEndTime = gc.getTimeInMillis();
	}

	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		mSeconds.draw(canvas);
		mMinutes.draw(canvas);
		mHours.draw(canvas);
		mDays.draw(canvas);
	}
}