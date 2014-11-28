package pl.polsl.dotnet.itacademicday.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pl.polsl.dotnet.itacademicday.layouts.MainActivity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import com.jakewharton.disklrucache.DiskLruCache;

public class BitmapDiskCache {
	private String name;
	private com.jakewharton.disklrucache.DiskLruCache mDiskCache;
	private CompressFormat mCompressFormat = CompressFormat.PNG;
	private int mCompressQuality = 100;
	private static final int BUFFER_SIZE = 8192;
	private static final int CACHE_SIZE_MB = 10;
	private static final int VALUE_COUNT = 1;

	public BitmapDiskCache(String name) {
		this.name = name;
		start();
	}

	private void start() {
		try {
			final File diskCacheDir = new File(MainActivity.getDiskCacheDir(),
					name);
			mDiskCache = DiskLruCache.open(diskCacheDir, 1, VALUE_COUNT,
					CACHE_SIZE_MB * 1048576);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor)
			throws IOException, FileNotFoundException {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(editor.newOutputStream(0),
					BUFFER_SIZE);
			return bitmap.compress(mCompressFormat, mCompressQuality, out);
		} catch (IllegalStateException e) {
			if (out != null) {
				out.close();
			}
			return false;
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public void put(String key, Bitmap data) {
		if (mDiskCache == null) {
			start();
			if (mDiskCache == null)
				return;
		}
		DiskLruCache.Editor editor = null;
		try {
			editor = mDiskCache.edit(key);
			if (editor == null) {
				return;
			}

			if (writeBitmapToFile(data, editor)) {
				mDiskCache.flush();
				editor.commit();
			} else {
				editor.abort();
			}
		} catch (IOException e) {
			try {
				if (editor != null) {
					editor.abort();
				}
			} catch (IOException ignored) {
			}
		}

	}

	public Bitmap getBitmap(String key) {
		if (mDiskCache == null) {
			start();
			if (mDiskCache == null)
				return null;
		}
		Bitmap bitmap = null;
		DiskLruCache.Snapshot snapshot = null;
		try {
			snapshot = mDiskCache.get(key);
			if (snapshot == null) {
				return null;
			}
			final InputStream in = snapshot.getInputStream(0);
			if (in != null) {
				final BufferedInputStream buffIn = new BufferedInputStream(in,
						BUFFER_SIZE);
				try {
					bitmap = BitmapFactory.decodeStream(buffIn);
				} catch (OutOfMemoryError oome) {
					bitmap = null;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (snapshot != null) {
				snapshot.close();
			}
		}
		return bitmap;

	}

	public boolean containsKey(String key) {

		boolean contained = false;
		DiskLruCache.Snapshot snapshot = null;
		try {
			snapshot = mDiskCache.get(key);
			contained = snapshot != null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (snapshot != null) {
				snapshot.close();
			}
		}

		return contained;

	}

	public void clearCache() {
		try {
			mDiskCache.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getCacheFolder() {
		return mDiskCache.getDirectory();
	}

	@Override
	public void finalize() {
		try {
			mDiskCache.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}