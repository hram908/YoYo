package com.asktun.mg.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.chen.jmvc.util.JsonReqUtil.GsonObj;
import com.google.gson.GsonBuilder;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;

public class JsonReqUtil {

	private int code = 0;
	private static String URL;
	private Context cxt;
	private Map<String, Object> params;
	private boolean netstate = false;
	private boolean isrespone = false;
	private String port;

	private HttpClient httpclient;
	private int timeoutConnection = 5000;
	private int timeoutSocket = 8000;

	private int reQuestiontime = 1000;

	private int retryCount = 0;
	private int maxtry = 3;

	public static int REQUEST_POST = 1;
	public static int REQUEST_GET = 2;

	private int requstType = REQUEST_POST;

	private static final String charset = "utf-8";

	private static ExecutorService executors = Executors.newCachedThreadPool();

	public JsonReqUtil(Context cxt) {
		this.cxt = cxt;
		if (URL == null) {
			URL = Util.getUrl(cxt);
		}
	}

	public void request(final String port, final Map<String, Object> params,
			final JsonCallBack callback) {
		executors.execute(new Runnable() {

			@Override
			public void run() {
				String obj = null;
				NetworkErrorException ex = null;
				try {
					obj = sendRequest(port, params);
				} catch (NetworkErrorException e) {
					e.printStackTrace();
					callback.handler(obj, e);
				}
				callback.handler(obj, ex);
			}
		});
	}

	public void request(final GsonObj gsonObj,
			final Map<String, Object> params, final JsonCallBack2 callback) {
		executors.execute(new Runnable() {

			@Override
			public void run() {
				String obj = null;
				Object o = null;
				NetworkErrorException ex = null;
				try {
					obj = sendRequest(gsonObj.getInterface(), params);
				} catch (NetworkErrorException e) {
					e.printStackTrace();
					callback.handler(o, e);
					return;
				}
				GsonBuilder builder = new GsonBuilder();
				builder.excludeFieldsWithoutExposeAnnotation();
				Type type = gsonObj.getTypeToken();
				try {
					o = builder.create().fromJson(obj, type);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("JsonReqUtil", "得到数据异常" + e.toString());
					callback.handler(o, e);
					return;
				}
				callback.handler(o, ex);
			}
		});
	}

	public Object request(final GsonObj gsonObj,
			final Map<String, Object> params) {
		String obj = null;
		Object o = null;
		NetworkErrorException ex = null;
		try {
			obj = sendRequest(gsonObj.getInterface(), params);
		} catch (NetworkErrorException e) {
			e.printStackTrace();
			return null;
		}
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		Type type = gsonObj.getTypeToken();
		try {
			o = builder.create().fromJson(obj, type);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("JsonReqUtil", "得到数据异常" + e.toString());
			return null;
		}
		return o;
	}

	public String sendRequest(String port, Map<String, Object> params)
			throws NetworkErrorException {
		if (port == null) {
			Log.e("JsonReqUtil", "rul is null");
			return null;
		}
		this.port = port;
		this.params = params;
		netstate = NetWorkState.getNetwork(cxt);
		if (!netstate) {
			throw new NetworkErrorException();
		}
		retryCount = 0;

		String obj = null;
		boolean flag = true;
		while (flag) {
			flag = false;
			try {
				retryCount++;
				obj = getJson();
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
				Log.e("JsonReqUtil",
						"UnsupportedEncodingException:" + e.getMessage());
				try {
					Thread.sleep(reQuestiontime);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (retryCount > maxtry) {
					Log.e("JsonReqUtil", "gt maxReTry count!!");
					return null;
				}
				flag = true;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				Log.e("JsonReqUtil",
						"ClientProtocolException:" + e.getMessage());
				try {
					Thread.sleep(reQuestiontime);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (retryCount > maxtry) {
					Log.e("JsonReqUtil", "gt maxReTry count!!");
					return null;
				}
				flag = true;
			} catch (ParseException e) {
				e.printStackTrace();
				Log.e("JsonReqUtil", "ParseException:" + e.getMessage());
				try {
					Thread.sleep(reQuestiontime);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (retryCount > maxtry) {
					Log.e("JsonReqUtil", "gt maxReTry count!!");
					return null;
				}
				flag = true;
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("JsonReqUtil", "IOException:" + e.getMessage());
				try {
					Thread.sleep(reQuestiontime);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (retryCount > maxtry) {
					Log.e("JsonReqUtil", "gt maxReTry count!!");
					return null;
				}
				flag = true;
			}
		}

		if (obj == null || obj.equals("")) {
			return null;
		}
		Log.d("JsonReqUtil", obj);
		return obj;
	}

	public int getResponeCode() {
		return this.code;
	}

	private String getJson() throws UnsupportedEncodingException,
			ClientProtocolException, ParseException, IOException {
		isrespone = false;
		StringBuffer sb = new StringBuffer();
		String url = URL + port;
		sb.append(url);
		sb.append("?");
		httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, timeoutConnection);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				timeoutSocket);

		HttpPost httpPost = new HttpPost(url);

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		Map<String, Object> map = this.params;
		if (map != null) {
			Set<String> set = map.keySet();
			for (Iterator iterator = set.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				String value = "";
				Object o = map.get(string);
				if (o != null) {
					value = o.toString();
				}
				params.add(new BasicNameValuePair(string, value));
				// Log.d("JsonReqUtil", string+"="+map.get(string));
				sb.append(string + "="
						+ URLEncoder.encode(map.get(string) + "", charset)
						+ "&");
			}
			HttpEntity httpEntity = new UrlEncodedFormEntity(params, charset);
			httpPost.setEntity(httpEntity);
		}
		Log.d("JsonReqUtil", sb.toString());
		long starttime = System.currentTimeMillis();
		HttpUriRequest request = httpPost;
		if (requstType == REQUEST_GET) {
			request = new HttpGet(URI.create(sb.toString()));
		} else {
			request = httpPost;
		}

		HttpResponse httpResponse = httpclient.execute(request);
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// 鍙栧緱杩斿洖鐨勫瓧绗︿覆
			String strResult = EntityUtils.toString(httpResponse.getEntity());
			long endtime = System.currentTimeMillis();
			Log.d("JsonReqUtil", "[" + this.port
					+ "] result return OK!!! consume time:"
					+ (endtime - starttime) / 1000.0);
			return strResult;
		} else {
			return "";
		}
	}

	public static String getURL() {
		return URL;
	}

	public static void setURL(String uRL) {
		URL = uRL;
	}

	public int getRequstType() {
		return requstType;
	}

	public void setRequstType(int requstType) {
		this.requstType = requstType;
	}

}
