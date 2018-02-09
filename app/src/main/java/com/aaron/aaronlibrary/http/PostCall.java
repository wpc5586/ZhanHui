package com.aaron.aaronlibrary.http;

import android.app.Activity;
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
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostCall {

    public static final Handler handler = new Handler();

    public static <T> void post(Context mContext, final String url, HashMap<String, String> params, final PostResponse<T> response,
                                String[] toasts, boolean isShowDialog, final Class<T> clazz) {
        _postHttpUtils(mContext, url, params, null, response, toasts, clazz, null, null, null, isShowDialog, true);
    }

    public static <T> void postFile(Context mContext, final String url, File file, final PostResponse<T> response,
                                    String[] toasts, boolean isShowDialog, final Class<T> clazz) {
        _postHttpUtilsFile1(mContext, url, null, null, response, toasts, clazz, null, file, null, isShowDialog);
    }

    public static <T> void postFiles(Context mContext, final String url, HashMap<String, String> params, String filesKeyName, File[] files, final PostResponse<T> response,
                                    String[] toasts, boolean isShowDialog, final Class<T> clazz) {
        _postHttpUtilsFile2(mContext, url, params, null, response, toasts, clazz, filesKeyName, files, null, isShowDialog);
    }

    public static <T> void postJson(Context mContext, final String url, Object params, final PostResponse<T> response,
                                    String[] toasts, boolean isShowDialog, final Class<T> clazz) {
        _postHttpUtils(mContext, url, null, params, response, toasts, clazz, null, null, null, isShowDialog, true);
    }

    public static <T> void get(Context mContext, final String url, HashMap<String, String> params, final PostResponse<T> response,
                               String[] toasts, boolean isShowDialog, final Class<T> clazz) {
        _postHttpUtils(mContext, url, params, null, response, toasts, clazz, null, null, null, isShowDialog, false);
    }

    public static <T> void put(final Context mContext, final String url, final String content, final PostResponse<T> postResponse, final String[] toasts, final boolean isShowDialog, final Class<T> clazz) {
        _put(mContext, url, content, postResponse, toasts, isShowDialog, clazz, true);
    }

    public static <T> void putJson(final Context mContext, final String url, final Object params, final PostResponse<T> postResponse, final String[] toasts, final boolean isShowDialog, final Class<T> clazz) {
        _putJson(mContext, url, params, postResponse, toasts, isShowDialog, clazz, true);
    }

    public static <T> void delete(final Context mContext, final String url, final String content, final PostResponse<T> postResponse, final String[] toasts, final boolean isShowDialog, final Class<T> clazz) {
        _put(mContext, url, content, postResponse, toasts, isShowDialog, clazz, false);
    }

    public static <T> void deleteJson(final Context mContext, final String url, final Object params, final PostResponse<T> postResponse, final String[] toasts, final boolean isShowDialog, final Class<T> clazz) {
        _putJson(mContext, url, params, postResponse, toasts, isShowDialog, clazz, false);
    }

    public static <T> void _putJson(final Context mContext, final String url, final Object params, final PostResponse<T> postResponse, final String[] toasts, final boolean isShowDialog, final Class<T> clazz, final boolean isPut) {
        final String jsonContent = new Gson().toJson(params);
        Logger.dl("AsyncHttpClient.post()------", url + "?" + jsonContent);
        RequestCall call = isPut ?
                OkHttpUtils.put()
                        .url(url)
                        .requestBody(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonContent))
                        .build() :
                OkHttpUtils.delete()
                        .url(url)
                        .requestBody(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonContent))
                        .build();
        call.execute(new StringCallback() {
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
                Logger.dl("AsyncHttpClient.post()------", url + "?" + jsonContent + "    response:" + response);
                handleResponse(response, mContext, postResponse, toasts, clazz);
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

    public static <T> void _put(final Context mContext, final String url, final String content, final PostResponse<T> postResponse, final String[] toasts, final boolean isShowDialog, final Class<T> clazz, final boolean isPut) {
        Logger.dl("AsyncHttpClient.put()------", url + "?" + content);
        RequestCall call = isPut ?
                OkHttpUtils.put()
                        .url(url)
                        .requestBody(content)
                        .build() :
                OkHttpUtils.delete()
                        .url(url)
                        .requestBody(content)
                        .build();
        call.execute(new StringCallback() {
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
                Logger.dl("AsyncHttpClient.post()------", url + "?" + content + "    response:" + response);
                handleResponse(response, mContext, postResponse, toasts, clazz);
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

    private static <T> void _postHttpUtilsFile1(final Context mContext, final String url, final HashMap<String, String> params, final Object paramsT, final PostResponse<T> postResponse,
                                                final String[] toasts, final Class<T> clazz, String filesKeyName, final File file, final OkHttpClientManager.ProgressListener listener, final boolean isShowDialog) {
        Logger.dl("AsyncHttpClient.post()------", url + "?" + file.getAbsolutePath());
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "head_image", fileBody)
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        final OkHttpClient client = new OkHttpClient();
        final Response[] response = new Response[1];
        new Thread() {
            @Override
            public void run() {

                try {
                    response[0] = client.newCall(request).execute();
                    final String jsonString = response[0].body().string();
                    if (!response[0].isSuccessful()) {
                        if (postResponse != null) {
                            if (toasts.length > 1 && !TextUtils.isEmpty(toasts[1]))
                                ToastUtil.setErrorToast(mContext, toasts[1]);
                            postResponse.onFailure(-1, null);
                        }
                    } else {
                        Logger.dl("AsyncHttpClient.post()------", url + "?" + params + "    response:" + jsonString);
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleResponse(jsonString, mContext, postResponse, toasts, clazz);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static <T> void _postHttpUtilsFile2(final Context mContext, final String url, final HashMap<String, String> params, final Object paramsT, final PostResponse<T> postResponse,
                                                final String[] toasts, final Class<T> clazz, String filesKeyName, final File[] files, final OkHttpClientManager.ProgressListener listener, final boolean isShowDialog) {
        Logger.dl("AsyncHttpClient.post()------", url + "?" + files.length);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (String key : params.keySet()) {
            builder.addFormDataPart(key, params.get(key));
        }
        for (File file : files) {
            builder.addFormDataPart(filesKeyName, file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        }
        RequestBody requestBody = builder.build();
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        final OkHttpClient client = new OkHttpClient();
        final Response[] response = new Response[1];
        new Thread() {
            @Override
            public void run() {
                try {
                    response[0] = client.newCall(request).execute();
                    final String jsonString = response[0].body().string();
                    if (!response[0].isSuccessful()) {
                        if (postResponse != null) {
                            if (toasts.length > 1 && !TextUtils.isEmpty(toasts[1]))
                                ToastUtil.setErrorToast(mContext, toasts[1]);
                            postResponse.onFailure(-1, null);
                        }
                    } else {
                        Logger.dl("AsyncHttpClient.post()------", url + "?" + params + "    response:" + jsonString);
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleResponse(jsonString, mContext, postResponse, toasts, clazz);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static <T> void _postHttpUtilsFile(final Context mContext, final String url, final HashMap<String, String> params, final Object paramsT, final PostResponse<T> postResponse,
                                               final String[] toasts, final Class<T> clazz, String filesKeyName, File file, final OkHttpClientManager.ProgressListener listener, final boolean isShowDialog) {
        Logger.dl("AsyncHttpClient.post()------", url + "?" + file.getAbsolutePath());
//        save(params == null ? "null" : params.toString(), url, true);
        RequestCall requestCall = OkHttpUtils
                .postFile()
                .url(url)
                .file(file)
                .id(AppInfo.isNeedHttps() ? 101 : 100)
                .tag(mContext)
                .build();

        requestCall.execute(new StringCallback() {
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
                Logger.dl("AsyncHttpClient.post()------", url + "?" + params + "    response:" + response);
                handleResponse(response, mContext, postResponse, toasts, clazz);
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

    private static <T> void _postHttpUtils(final Context mContext, final String url, final HashMap<String, String> params, final Object paramsT, final PostResponse<T> postResponse,
                                           final String[] toasts, final Class<T> clazz, String filesKeyName, File[] files, final OkHttpClientManager.ProgressListener listener, final boolean isShowDialog, boolean isPost) {
        final String jsonContent = new Gson().toJson(paramsT);
        Logger.dl("AsyncHttpClient.post()------", url + "?" + (params == null ? jsonContent : params));
        save(params == null ? "null" : params.toString(), url, true);
        RequestCall requestCall = params != null ? (isPost ? OkHttpUtils
                .post()
                .url(url)
                .params(params)
                .id(AppInfo.isNeedHttps() ? 101 : 100)
                .tag(mContext)
                .build() : OkHttpUtils
                .get()
                .url(url)
                .params(params)
                .id(AppInfo.isNeedHttps() ? 101 : 100)
                .tag(mContext)
                .build()) : OkHttpUtils
                .postString()
                .url(url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(jsonContent)
                .id(AppInfo.isNeedHttps() ? 101 : 100)
                .tag(mContext)
                .build();

        requestCall.execute(new StringCallback() {
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
                Logger.dl("AsyncHttpClient.post()------", url + "?" + params + "    response:" + response);
                handleResponse(response, mContext, postResponse, toasts, clazz);
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

    private static <T> void handleResponse(String response, final Context mContext, final PostResponse<T> postResponse, final String[] toasts, final Class<T> clazz) {
        T bean = new Gson().fromJson(response, clazz);
        if (postResponse != null) {
            if (bean != null && bean instanceof BaseBean) {
                if (((BaseBean) bean).getStatusCode() == 0) {
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
