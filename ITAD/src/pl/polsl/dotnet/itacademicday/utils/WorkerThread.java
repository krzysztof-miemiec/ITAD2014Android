package pl.polsl.dotnet.itacademicday.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkerThread extends Thread {
	private List<Runnable> tasks;
	private boolean shouldRun, notified;
	public boolean breakTask;

	public WorkerThread() {
		this(null, true);
	}

	public WorkerThread(String name, boolean start) {
		super();
		setPriority(NORM_PRIORITY);
		if (name == null)
			setName("XWorkerThread");
		else
			setName(name);
		shouldRun = true;
		tasks = Collections.synchronizedList(new ArrayList<Runnable>());
		if (start)
			start();
	}

	public WorkerThread(String name) {
		this(name, true);
	}

	public void addTask(Runnable r){
		if (Thread.currentThread() == this) {
			r.run();
			return;
		}
		synchronized (tasks) {
			tasks.add(r);
			tasks.notify();
			notified = true;
		}
	}

	public void clearTasks(){
		synchronized (tasks) {
			if (tasks.size() > 0) {
				tasks.clear();
			}
			tasks.notify();
			notified = true;
		}
	}

	public void abortAllTasks(){
		synchronized (tasks) {
			if (tasks.size() > 0) {
				breakTask = true;
				tasks.clear();
			}
			interrupt();
			tasks.notify();
			notified = true;
		}
	}

	public void abortTask(Runnable task){
		synchronized (tasks) {
			for (int i = 0; i < tasks.size(); i++) {
				if (tasks.get(i) == task) {
					if (i == 0) {
						breakTask = true;
						interrupt();
					}
					tasks.remove(i);
					break;
				}
			}
			tasks.notify();
			notified = true;
		}
	}

	public void justStop(){
		synchronized (tasks) {
			shouldRun = false;
			tasks.notify();
			notified = true;
		}
	}

	public void forceStop(){
		synchronized (tasks) {
			breakTask = true;
			shouldRun = false;
			tasks.notify();
			notified = true;
			interrupt();
		}
	}

	@Override
	public void run(){
		while (shouldRun) {
			try {
				breakTask = false;
				notified = false;
				if (tasks.size() > 0) {
					Runnable r = tasks.get(0);
					r.run();
					synchronized (tasks) {
						if (tasks.size() > 0)
							tasks.remove(r);
					}
				}
				synchronized (tasks) {
					if (tasks.size() == 0 && !notified) {
						tasks.wait();
					}
				}
			} catch (InterruptedException e) {
			}
		}
	}

	public boolean shouldRun(){
		return shouldRun;
	}
}