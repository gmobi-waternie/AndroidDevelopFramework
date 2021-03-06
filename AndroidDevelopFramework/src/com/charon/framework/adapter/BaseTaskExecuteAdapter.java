package com.charon.framework.adapter;

import com.charon.framework.BaseAsyncTask.TaskExecuteListener;

/**
 * TaskExecuteListener的适配器类，对改接口中的方法进行了默认的空实现
 * 
 * @param Result
 *            后台任务执行时返回的结果类型
 * @see TaskExecuteListener
 */
public abstract class BaseTaskExecuteAdapter<Result> implements
		TaskExecuteListener<Result> {

	@Override
	public void onPreExecute() {

	}

	@Override
	public abstract void onPostExecute(Result result);

	@Override
	public void onCancelled() {

	}
}
