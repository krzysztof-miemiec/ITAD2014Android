package pl.polsl.dotnet.itacademicday.layouts;

import java.util.GregorianCalendar;

import pl.polsl.dotnet.itacademicday.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class CounterView extends View {

	private Thread mRefreshThread;
	private Paint mWhitePaint;
	private CounterArc mSeconds, mMinutes, mHours, mDays;
	private long mEndTime;
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
		private float mRadius, mCenter;

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
			if (w > h) {
				h = w;
			} else {
				w = h;
			}
			mCenter = w / 2;
			mRadius = mScale * w / 2;
			mOval.left = mCenter - mRadius;
			mOval.top = mCenter - mRadius;
			mOval.right = mCenter + mRadius;
			mOval.bottom = mCenter + mRadius;
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
			c.drawArc(mOval, -90, mAngle, true, mPaint);
			c.drawOval(mInnerOval, mWhitePaint);
			float cx = (float) Math.sin(Math.PI * mAngle / 180) * (mRadius - THICKNESS / 2) + mCenter;
			float cy = (float) -Math.cos(Math.PI * mAngle / 180) * (mRadius - THICKNESS / 2) + mCenter;
			c.drawCircle(cx, cy, THICKNESS * 1.7f, mPaint);
			c.drawText(String.valueOf(mValue), cx, cy - THICKNESS * 0.1f, mWhitePaint);
			c.drawText(mText, cx, cy + THICKNESS * 1.6f, mWhitePaint);
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
		setLayerType(LAYER_TYPE_SOFTWARE, null);
		mHandler = new Handler();
		THICKNESS = getContext().getResources().getDimension(R.dimen.counter_thickness);
		mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mWhitePaint.setColor(((ColorDrawable) getBackground()).getColor());
		mWhitePaint.setTextAlign(Align.CENTER);
		mWhitePaint.setTextSize(THICKNESS * 2);
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
		postInvalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setupSizing(getMeasuredWidth(), getMeasuredHeight());
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