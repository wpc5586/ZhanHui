package com.aaron.aaronlibrary.http;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.aaron.aaronlibrary.base.activity.BaseActivity;
import com.aaron.aaronlibrary.base.utils.Logger;
import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.ToastUtil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

public class PostCall {

    public static final Handler handler = new Handler();

    public static <T> void post(Context mContext, final String url, HashMap<String, String> params, final PostResponse<T> response,
                                String[] toasts, boolean isShowDialog, final Class<T> clazz) {
        _postHttpUtils(mContext, url, params, response, toasts, clazz, null, null, null, isShowDialog);
    }

    private static <T> void _postHttpUtils(final Context mContext, final String url, final HashMap<String, String> params, final PostResponse<T> postResponse,
                                           final String[] toasts, final Class<T> clazz, String filesKeyName, File[] files, final OkHttpClientManager.ProgressListener listener, final boolean isShowDialog) {
        Logger.dl("AsyncHttpClient.post()------", url + "?" + params);
        save(params == null ? "null" : params.toString(), url, true);
        OkHttpUtils
                .post()
                .url(url)
                .params(params)
                .id(AppInfo.isNeedHttps() ? 101 : 100)
                .tag(mContext)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logger.dl("AsyncHttpClient.post()------", "onError message = " + e.getMessage());
                        if (postResponse != null) {
                            if (toasts.length > 1 && !TextUtils.isEmpty(toasts[1]))
                                ToastUtil.setErrorToast(mContext, toasts[1]);
                            postResponse.onFailure(-1, null);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        Logger.dl("AsyncHttpClient.post()------", url + "?" + params + "    response:" + response);
//                        save(response, url, false);
//                        if (!(url.contains(ServerUrl.getVersion()))) {
//                            Gson gson = new Gson();
//                            Map<String, Object> map = new HashMap<>();
//                            map = gson.fromJson(response, map.getClass());
//                            Number code = (Number) map.get("statusCode");
//                            map.put("statusCode", code.intValue());
//                            map.put("message", AES.decrypt((String) map.get("message")));
//                            if (map.get("obj") instanceof String)
//                                map.put("obj", gson.fromJson(AES.decrypt((String) map.get("obj")), map.getClass()));
//                            response = gson.toJson(map);
//                        }
                        T bean = new Gson().fromJson(response, clazz);
                        Logger.dl("AsyncHttpClient.post()------", url + "?" + params + "    response:" + response + ",  bean = " + bean);
                        if (postResponse != null) {
                            if (bean != null && bean instanceof BaseBean) {
                                if (((BaseBean) bean).getStatusCode() == 200 || ((BaseBean) bean).getStatusCode() == 10) {
                                    if (toasts.length > 0 && !TextUtils.isEmpty(toasts[0]))
                                        ToastUtil.show(mContext, toasts[0]);
                                    postResponse.onSuccess(0, null, bean);
                                } else {
                                    postResponse.onFailure(-1, null);
                                    Toast.makeText(mContext,
                                            ((BaseBean) bean).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                postResponse.onFailure(-1, null);
                                Toast.makeText(mContext, "bean is null!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        if (mContext instanceof BaseActivity && isShowDialog)
                            ((BaseActivity) mContext).showProgressDialog("加载中");
                    }

                    @Override
                    public void onAfter(int id) {
                        if (mContext instanceof BaseActivity && isShowDialog)
                            ((BaseActivity) mContext).dismissProgressDialog();
                    }
                });
    }

    @SuppressWarnings("rawtypes")
    private static String getJsonString(HashMap<String, String> params) {
        final JSONObject object = new JSONObject();
        try {
            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey().toString();
                String val = (String) entry.getValue().toString();

                object.put(key, val);

            }
        } catch (JSONException e1) {
            e1.printStackTrace();
            return "";
        } catch (Exception e1) {
            e1.printStackTrace();
            return "";
        }
        return object.toString();
    }

    public interface PostResponse<T> {
        public void onSuccess(int statusCode, byte[] responseBody, T bean);

        public void onFailure(int statusCode, byte[] responseBody);
    }

    public interface PostProgress {
        public void onProgress(long bytesWritten, long totalSize);
    }

    public static void save(String content, String url, boolean isRequest) {
//        String time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
//        String fileName = "crash-" + (isRequest ? "request-" : "response-") + url.replace(ServerUrl.getService() + "", "").replace("mobile/", "").replace(".do", "") + time + ".log";
//        if (Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            try {
//                File dir = new File(DataPath.getDirectory(DataPath.DATA_PATH_CRASH));
////                LogUtil.e("CrashHandler", dir.toString());
//                if (!dir.exists())
//                    dir.mkdir();
//                FileOutputStream fos = new FileOutputStream(new File(dir,
//                        fileName));
//                fos.write(content.getBytes());
//                fos.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
