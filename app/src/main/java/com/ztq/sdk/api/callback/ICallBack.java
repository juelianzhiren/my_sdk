package com.ztq.sdk.api.callback;


import com.ztq.sdk.exception.AppException;

/**
 * create at 2020-3-20 下午03:15:49
 */
public interface ICallBack<T> {
	/**
	 * 请求前
	 */
	public void prepare();
	
	/**
	 * 请求成功
	 */
	public void success(T t);

	/**
	 * 请求失败
	 */
	public void failure(AppException e);
	
	/**
	 * 请求结束(无论是请求成功， 还是请求失败)
	 */
	public void end();
}