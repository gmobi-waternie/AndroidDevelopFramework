package com.charon.framework.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.charon.framework.util.IOUtil;

public class SimpleHttpConnection {
	private static final int connectTimeout = 3000;

	/**
	 * 进行网络连接，并且以String方式返回该返回值
	 * 
	 * @param url
	 *            url地址
	 * @return String
	 * @throws IOException
	 */
	public static String getResponse(String url) {
		InputStream is = getResponseStream(url);
		if (is != null) {
			try {
				return IOUtil.toString(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 进行网络连接，并且以String方式返回该返回值
	 * 
	 * @param url
	 *            url地址
	 * @return String
	 * @throws IOException
	 */
	public static InputStream getResponseStream(String url) {
		try {
			URL mURL = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) mURL
					.openConnection();
			connection.setConnectTimeout(connectTimeout);
			connection.setRequestMethod("GET");
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream is = connection.getInputStream();
				return is;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
