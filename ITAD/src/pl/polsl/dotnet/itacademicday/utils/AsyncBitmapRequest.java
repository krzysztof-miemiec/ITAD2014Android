package pl.polsl.dotnet.itacademicday.utils;

import java.lang.ref.WeakReference;

import pl.polsl.dotnet.itacademicday.utils.Bitmaps.BitmapRequest;
import pl.polsl.dotnet.itacademicday.utils.Bitmaps.RequestResult;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class AsyncBitmapRequest {
	private static WorkerThread mWorkerThread;
	private BitmapRequest bitmapLoad;
	private Runnable task;
	private RequestResult reqResult;
	private WeakReference<ImageView> imgView;
	private int errorResource;

	AsyncBitmapRequest(BitmapRequest bitmapLoad) {
		this.bitmapLoad = bitmapLoad;
	}

	/** Result will be passed to RequestResult for custom further processing. It does not exclude setting imageView */
	public AsyncBitmapRequest result(RequestResult requestResult){
		reqResult = requestResult;
		return this;
	}

	/** Result will be passed as Drawable to specified ImageView. */
	public AsyncBitmapRequest result(ImageView imageView){
		imgView = new WeakReference<ImageView>(imageView);
		return this;
	}

	public AsyncBitmapRequest errorImageResource(int resource){
		errorResource = resource;
		return this;
	}

	/**Queues bitmap load task and starts it as soon as possible.*/
	public AsyncBitmapRequest start(){
		task = new Runnable() {
			@Override
			public void run(){
				final Bitmap b = bitmapLoad.request();
				if (imgView != null) {
					final ImageView iv = imgView.get();
					if (iv != null) {
						iv.post(new Runnable() {
							@Override
							public void run(){
								if (b != null) {
									iv.setImageBitmap(b);
								} else {
									if (errorResource != 0)
										iv.setImageResource(errorResource);
								}
								iv.invalidate();
							}
						});
					}
				}
				if (reqResult != null) {
					reqResult.onBitmap(b);
				}
			}
		};
		if (mWorkerThread == null) {
			mWorkerThread = new WorkerThread("AsyncBitmapLoader");
		}
		mWorkerThread.addTask(task);
		return this;
	}

	/** Removes task from queue or tries to abort it if it's already running. */
	public AsyncBitmapRequest cancel(){
		reqResult = null;
		imgView = null;
		if (mWorkerThread == null) {
			mWorkerThread = new WorkerThread("AsyncBitmapLoader");
		}
		mWorkerThread.abortTask(task);
		return this;
	}
}