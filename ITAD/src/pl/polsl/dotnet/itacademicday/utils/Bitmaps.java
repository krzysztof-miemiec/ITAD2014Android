package pl.polsl.dotnet.itacademicday.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class Bitmaps {
	public static final int BITMAP_ORIGINAL_SIZE = -1;
	/** Describes percent of heap memory that will be allocated for bitmap caching. */
	public static final int BITMAP_CACHE_SIZE_PERCENT = 20;
	/**
	 * Describes the smallest amount of heap memory that will be allocated for bitmap caching. If {@link #BITMAP_CACHE_SIZE_PERCENT} *
	 * {@link #getFreeHeapMemory()} is smaller than this value, then the cache will use this value.
	 */
	public static final int BITMAP_CACHE_SIZE_MINIMUM = 3 * 1024 * 1024;

	static WorkerThread mWorkerThread;

	public static AsyncBitmapRequest loadNetBitmapAsync(final String path, final int reqX, final int reqY){
		return new AsyncBitmapRequest(new BitmapRequest() {
			@Override
			public Bitmap request(){
				return loadNetBitmap(path, reqX, reqY);
			}
		});
	}

	public static final Bitmap loadNetBitmap(String netPath, int w, int h){
		try {
			java.net.URL url = new java.net.URL(netPath);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap bm = BitmapFactory.decodeStream(input);
			int width = bm.getWidth();
			int height = bm.getHeight();
			float scaleWidth = ((float) w) / width;
			float scaleHeight = ((float) h) / height;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
			bm.recycle();
			return resizedBitmap;
		} catch (Exception e) {
			return null;
		}
	}

	interface BitmapRequest {
		public Bitmap request();
	}

	public interface RequestResult {
		public void onBitmap(Bitmap b);
	}
}