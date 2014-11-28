package pl.polsl.dotnet.itacademicday.utils;

import java.security.MessageDigest;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

class BitmapLruCache {

	private class BitmapLru extends LruCache<String, Bitmap> {
		public BitmapLru(int maxSize) {
			super(maxSize);
		}

		@Override
		protected void entryRemoved(boolean evicted, final String key,
				final Bitmap oldValue, Bitmap newValue) {
			if (evicted && !bitmapDiskCache.containsKey(key)) {
				workerThread.addTask(new Runnable() {
					@Override
					public void run() {
						bitmapDiskCache.put(key, oldValue);
					}
				});
			}
			super.entryRemoved(evicted, key, oldValue, newValue);
			// oldValue.recycle();
		}

		@Override
		protected int sizeOf(String key, Bitmap bitmap) {
			return BitmapLruCache.size(bitmap);
		}
	}

	private BitmapLru bitmapCache;
	private BitmapDiskCache bitmapDiskCache;
	private WorkerThread workerThread;

	private static int size(Bitmap bitmap) {
		if (bitmap != null) {
			final int pixels = bitmap.getWidth() * bitmap.getHeight();
			if (bitmap.getConfig() != null)
				switch (bitmap.getConfig()) {
				case ARGB_8888:
					return 4 * pixels;
				case RGB_565:
				case ARGB_4444:
					return 2 * pixels;
				default:
					return 4 * pixels;
				}
			return 4 * pixels;
		}
		return 0;
	}

	// private int maxSize;

	public BitmapLruCache(String name, int maxSize) {
		bitmapCache = new BitmapLru(maxSize);
		bitmapDiskCache = new BitmapDiskCache(name);
		workerThread = new WorkerThread("BitmapCacheWorker");
		// this.maxSize = maxSize;
	}

	public synchronized Bitmap put(String key, Bitmap bitmap) {
		key = normalizeKey(key);
		bitmapCache.put(key, bitmap);
		bitmapDiskCache.put(key, bitmap);
		return bitmapCache.get(key);
	}

	public synchronized Bitmap get(String key) {
		if (key != null) {
			key = normalizeKey(key);
			Bitmap bitmap = null;
			synchronized (bitmapCache) {
				bitmap = bitmapCache.get(key);
			}
			if (bitmap != null) {
				return bitmap;
			}
			bitmap = bitmapDiskCache.getBitmap(key);
			if (bitmap != null) {
				bitmapCache.put(key, bitmap);
				return bitmap;
			}
		}
		return null;
	}

	public synchronized void reduceAll() {
		bitmapCache.evictAll();
	}

	private static String normalizeKey(String key) {
		return md5(key);
	}

	private static final String md5(final String s) {
		final String MD5 = "MD5";
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuilder hexString = new StringBuilder();
			for (byte aMessageDigest : messageDigest) {
				String h = Integer.toHexString(0xFF & aMessageDigest);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (Exception e) {
		}
		return "";
	}

	public synchronized long size() {
		return bitmapCache.size();
	}
}