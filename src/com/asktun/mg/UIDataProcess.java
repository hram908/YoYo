package com.asktun.mg;

import java.util.Map;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.asktun.mg.bean.UserMessage;
import com.asktun.mg.utils.Util;
import com.chen.jmvc.util.IResponseListener;
import com.chen.jmvc.util.JsonReqUtil;
import com.chen.jmvc.util.UIHelperUtil;

public class UIDataProcess {

	public static void reqData(@SuppressWarnings("rawtypes") final Class cls,
			Map<String, Object> params, final Object flg,
			IResponseListener listener) {

		final UIHelperUtil uhu = UIHelperUtil.getUIHelperUtil(listener);
		JsonReqUtil jru = new JsonReqUtil();
		if (!Util.isNetConnected(JsonReqUtil.cxt)) {
			new Handler(Looper.getMainLooper()).post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (!cls.equals(UserMessage.class))
						Toast.makeText(JsonReqUtil.cxt, "当前网络不可用，请检查你的网络设置",
								Toast.LENGTH_SHORT).show();
					uhu.sendFailureMessage(null);
				}
			});
			return;
		}
		jru.setRequstType(JsonReqUtil.REQUEST_GET);
		jru.setParams(params);
		if (flg != null) {
			jru.useFieldSetEnable = true;
			jru.obj = flg;
		}
		uhu.sendStartMessage();

		jru.request(cls, new JsonReqUtil.JsonCallBack() {

			@Override
			public void handler(Object jstr, Exception ex) {
				if (ex != null || jstr == null) {
					uhu.sendFailureMessage(ex);
				} else {
					uhu.sendSuccessMessage(jstr);
				}
				uhu.sendFinishMessage();
			}
		});

	}

}
