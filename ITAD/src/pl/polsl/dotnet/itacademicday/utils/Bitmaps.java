package pl.polsl.dotnet.itacademicday.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;

public class Bitmaps {
	public static final int BITMAP_ORIGINAL_SIZE = -1;
	/**
	 * Describes percent of heap memory that will be allocated for bitmap
	 * caching.
	 */
	public static final int BITMAP_CACHE_SIZE_PERCENT = 20;
	/**
	 * Describes the smallest amount of heap memory that will be allocated for
	 * bitmap caching. If {@link #BITMAP_CACHE_SIZE_PERCENT} *
	 * {@link #getFreeHeapMemory()} is smaller than this value, then the cache
	 * will use this value.
	 */
	public static final int BITMAP_CACHE_SIZE_MINIMUM = 3 * 1024 * 1024;

	static WorkerThread mWorkerThread;
	private static BitmapLruCache bitmapCache;

	public static AsyncBitmapRequest loadNetBitmapAsync(final String path,
			final int reqX, final int reqY, final boolean useCache) {
		return new AsyncBitmapRequest(new BitmapRequest() {
			@Override
			public Bitmap request() {
				return loadNetBitmap(path, reqX, reqY, useCache);
			}
		});
	}

	private static long getFreeHeapMemory() {
		long free = Runtime.getRuntime().maxMemory()
				+ Runtime.getRuntime().freeMemory()
				- Runtime.getRuntime().totalMemory();
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
				&& bitmapCache != null)
			free -= bitmapCache.size() * 1.3;
		return free;
	}

	public static void setup() {
		final long freeMemory = getFreeHeapMemory();
		final int cacheSize = Math.max(
				(int) (freeMemory * BITMAP_CACHE_SIZE_PERCENT),
				BITMAP_CACHE_SIZE_MINIMUM);
		bitmapCache = new BitmapLruCache("bmpcache", cacheSize);
	}

	public static final Bitmap loadNetBitmap(String netPath, int w, int h,
			boolean useCache) {
		if (netPath == null)
			return null;
		final Bitmap b = getCachedBitmap(netPath);
		if (b == null) {
			try {
				java.net.URL url = new java.net.URL(netPath);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap bm = BitmapFactory.decodeStream(input);
				int width = bm.getWidth();
				int height = bm.getHeight();
				Bitmap result;
				if (w != 0 && h != 0) {
					float scale = width > height ? (float) w / width
							: (float) h / height;
					Matrix matrix = new Matrix();
					matrix.postScale(scale, scale);
					result = Bitmap.createBitmap(bm, 0, 0, width, height,
							matrix, false);
					bm.recycle();
				} else {
					result = bm;
				}
				if (result != null) {
					return cacheBitmap(netPath, result);
				}
			} catch (Exception e) {
			}
		}
		return b;
	}

	public static Bitmap cacheBitmap(String key, Bitmap bitmap) {
		if (key != null && bitmap != null) {
			return bitmapCache.put(key, bitmap);
		}
		return null;
	}

	public static Bitmap getCachedBitmap(String key) {
		if (key == null)
			return null;
		return bitmapCache.get(key);
	}

	interface BitmapRequest {
		public Bitmap request();
	}

	public interface RequestResult {
		public void onBitmap(Bitmap b);
	}
}