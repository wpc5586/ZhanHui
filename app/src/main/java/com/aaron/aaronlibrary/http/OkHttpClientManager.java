package com.aaron.aaronlibrary.http;

public class OkHttpClientManager {

//    private static OkHttpClientManager mInstance;
//
//    private OkHttpClient mOkHttpClient;
//    private Handler mDelivery;
//
//    private OkHttpClientManager() {
//        mOkHttpClient = new OkHttpClient();
//        // mOkHttpClient.cookieJar();
//        mDelivery = new Handler(Looper.getMainLooper());
//    }
//
//    private OkHttpClientManager(InputStream... certificates) {
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.sslSocketFactory(setCertificates(certificates));
//        mOkHttpClient = builder.build();
//        mDelivery = new Handler(Looper.getMainLooper());
//    }
//
//    public static OkHttpClientManager getInstance() {
//        if (mInstance == null) {
//            synchronized (OkHttpClientManager.class) {
//                if (mInstance == null) {
//                    if (AppInfo.isNeedHttps())
//                        try {
////                            mInstance = new OkHttpClientManager(CrashApplication.APP.getAssets().open("srca.cer"));
//                            System.out.println("~!~ https");
//                            mInstance = new OkHttpClientManager(CrashApplication.APP.getAssets().open("root.crt"));
//                        } catch (IOException e) {
//                            System.out.println("~!~ http");
//                            mInstance = new OkHttpClientManager();
//                            e.printStackTrace();
//                        }
//                    else
//                        mInstance = new OkHttpClientManager();
//                }
//            }
//        }
//        return mInstance;
//    }
//
//    public void setHttpsClient(InputStream... certificates) {
//        try {
//            mInstance = new OkHttpClientManager(certificates);
//            System.out.println("~!~ https success");
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("~!~ https error");
//        }
//    }
//
//    public static void get(String url, Callback callback) {
//        getInstance()._get(url, callback);
//    }
//
//    public static void post(String url, final Callback callback, Param... params) {
//        getInstance()._post(url, callback, params);
//    }
//
//    public static void post(String url, Map<String, String> params, final Callback callback) {
//        getInstance()._post(url, callback, params);
//    }
//
//    public static void post(String url, Callback callback, File[] files, String fileKeys,
//            ProgressListener progressListener, HashMap<String, String> params) {
//        if (files != null && fileKeys != null) {
//            String[] ss = new String[files.length];
//            for (int i = 0; i < ss.length; i++) {
//                ss[i] = fileKeys;
//            }
//            getInstance()._post(url, callback, files, ss, progressListener, params);
//            return;
//        }
//
//        getInstance()._post(url, callback, params);
//    }
//
//    public static void post(String url, Callback callback, File[] files, String fileKeys,
//                            ProgressListener progressListener, Param... params) {
//        if (files != null && fileKeys != null) {
//            String[] ss = new String[files.length];
//            for (int i = 0; i < ss.length; i++) {
//                ss[i] = fileKeys;
//            }
//            getInstance()._post(url, callback, files, ss, progressListener, params);
//            return;
//        }
//
//        getInstance()._post(url, callback, params);
//    }
//
//    public static void post(String url, Callback callback, File[] files, String[] fileKeys,
//            ProgressListener progressListener, HashMap<String, String> params) {
//        getInstance()._post(url, callback, files, fileKeys, progressListener, params);
//    }
//
//    public static void post(String url, Callback callback, File[] files, String[] fileKeys,
//                            ProgressListener progressListener, Param... params) {
//        getInstance()._post(url, callback, files, fileKeys, progressListener, params);
//    }
//
//    public static void post(String url, Callback callback, File file, String fileKey, ProgressListener progressListener)
//            throws IOException {
//        getInstance()._post(url, callback, file, fileKey, progressListener);
//    }
//
//    public static void post(String url, Callback callback, File file, String fileKey,
//            ProgressListener progressListener, Param... params) throws IOException {
//        getInstance()._post(url, callback, file, fileKey, progressListener, params);
//    }
//
//    private void _get(String url, final Callback callback) {
//        final Request request = new Request.Builder().url(url).build();
//        _call(callback, request);
//    }
//
//    private void _post(String url, final Callback callback, Param... params) {
//        Request request = buildPostRequest(url, params);
//        _call(callback, request);
//    }
//
//    private void _post(String url, final Callback callback, Map<String, String> params) {
//        Param[] paramsArr = map2Params(params);
//        Request request = buildPostRequest(url, paramsArr);
//        _call(callback, request);
//    }
//
//    private void _post(String url, Callback callback, File[] files, String[] fileKeys,
//                       ProgressListener progressListener, Param... params) {
//        Request request = buildMultipartFormRequest(url, files, fileKeys, params, progressListener);
//        _call(callback, request);
//    }
//
//    private void _post(String url, Callback callback, File[] files, String[] fileKeys,
//            ProgressListener progressListener, HashMap<String, String> params) {
//        Request request = buildMultipartFormRequest(url, files, fileKeys, map2Params(params), progressListener);
//        _call(callback, request);
//    }
//
//    private void _post(String url, Callback callback, File file, String fileKey, ProgressListener progressListener)
//            throws IOException {
//        Request request = buildMultipartFormRequest(url, new File[] { file }, new String[] { fileKey }, null,
//                progressListener);
//        _call(callback, request);
//    }
//
//    private void _post(String url, Callback callback, File file, String fileKey, ProgressListener progressListener,
//            Param... params) throws IOException {
//        Request request = buildMultipartFormRequest(url, new File[] { file }, new String[] { fileKey }, params,
//                progressListener);
//        _call(callback, request);
//    }
//
//    public String getFileName(String path) {
//        int separatorIndex = path.lastIndexOf("/");
//        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
//    }
//
//    private Request buildMultipartFormRequest(String url, File[] files, String[] fileKeys, Param[] params,
//            ProgressListener progressListener) {
//        params = validateParam(params);
//
//        // MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
//        MultipartBody.Builder builder = new MultipartBody.Builder();
//        builder.setType(MultipartBody.FORM);
//
//        for (Param param : params) {
//            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
//                    RequestBody.create(null, param.value));
//        }
//        if (files != null) {
//            RequestBody fileBody = null;
//            for (int i = 0; i < files.length; i++) {
//                File file = files[i];
//                String fileName = file.getName();
//                // fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
//                fileBody = new CountingFileRequestBody(file, guessMimeType(fileName), progressListener);
//
//                // 根据文件名设置contentType
//                builder.addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"" + fileKeys[i] + "\"; filename=\""
//                                + fileName + "\""), fileBody);
//            }
//        }
//
//        RequestBody requestBody = builder.build();
//        return new Request.Builder().url(url).post(requestBody).build();
//    }
//
//    private String guessMimeType(String path) {
//        FileNameMap fileNameMap = URLConnection.getFileNameMap();
//        String contentTypeFor = fileNameMap.getContentTypeFor(path);
//        if (contentTypeFor == null) {
//            contentTypeFor = "application/octet-stream";
//        }
//        return contentTypeFor;
//    }
//
//    private Param[] validateParam(Param[] params) {
//        if (params == null)
//            return new Param[0];
//        else
//            return params;
//    }
//
//    private Param[] map2Params(Map<String, String> params) {
//        if (params == null)
//            return new Param[0];
//        int size = params.size();
//        Param[] res = new Param[size];
//        Set<Map.Entry<String, String>> entries = params.entrySet();
//        int i = 0;
//        for (Map.Entry<String, String> entry : entries) {
//            res[i++] = new Param(entry.getKey(), entry.getValue());
//        }
//        return res;
//    }
//
//    private void _call(final Callback callback, Request request) {
//        // 设置超时时间（读文件：100秒   写文件：60秒   连接超时：60秒）
//        mOkHttpClient = new OkHttpClient.Builder()
//                .readTimeout(100, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .build();
//
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(final Call call, final IOException ioexception) {
//                if (callback != null)
//                    callback.onFailure(call, ioexception);
//            }
//
//            @Override
//            public void onResponse(final Call call, final Response response) {
//                if (callback != null)
//                    try {
//                        callback.onResponse(call, response);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//            }
//        });
//    }
//
//    private Request buildPostRequest(String url, Param[] params) {
//        if (params == null) {
//            params = new Param[0];
//        }
//
//        FormBody.Builder builder = new FormBody.Builder();
//        for (Param param : params) {
//            builder.add(param.key, param.value);
//        }
//        RequestBody requestBody = builder.build();
//        return new Request.Builder().url(url).post(requestBody).build();
//    }
//
//    public static abstract class ResultCallback<T> {
//        public abstract void onFailure(byte[] responseBody, Request request, Exception e);
//
//        public abstract void onSuccess(byte[] responseBody, T response);
//    }
//
//    public static class Param {
//        public Param() {
//        }
//
//        public Param(String key, String value) {
//            this.key = key;
//            this.value = value;
//        }
//
//        String key;
//        String value;
//    }
//
//    public class CountingFileRequestBody extends RequestBody {
//
//        private static final int SEGMENT_SIZE = 2048; // okio.Segment.SIZE
//
//        private final File file;
//        private final ProgressListener listener;
//        private final String contentType;
//        private long filesize;
//        private String filename;
//        long total = 0;
//
//        public CountingFileRequestBody(File file, String contentType, ProgressListener listener) {
//            this.file = file;
//            filesize = file.length();
//            filename = file.getName();
//            this.contentType = contentType;
//            this.listener = listener;
//        }
//
//        @Override
//        public long contentLength() {
//            return file.length();
//        }
//
//        @Override
//        public MediaType contentType() {
//            return MediaType.parse(contentType);
//        }
//
//        @Override
//        public void writeTo(BufferedSink sink) throws IOException {
//            Source source = null;
//            try {
//
//                source = Okio.source(file);
//
//                total = 0;
//                long read;
//
//                while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
//                    total += read;
//                    sink.flush();
//                    if (this.listener != null) {
//                        mDelivery.post(new Runnable() {
//                            public void run() {
//                                listener.transferred(filename, filesize, total);
//                            }
//                        });
//
//                    }
//
//                }
//            } finally {
//                Util.closeQuietly(source);
//            }
//        }
//    }
//
    public interface ProgressListener {
        void transferred(String filename, long size, long num);
    }
//
//    public SSLSocketFactory setCertificates(InputStream... certificates) {
//        final String pwd = "bonc";
//        try {
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            KeyManagerFactory keyManager = KeyManagerFactory.getInstance("X509");
//            TrustManagerFactory trustManager = TrustManagerFactory.getInstance("X509");
//            KeyStore kks = KeyStore.getInstance("PKCS12");
//            KeyStore tks = KeyStore.getInstance("BKS");
//            tks.load(CrashApplication.APP
//                    .getResources()
//                    .openRawResource(R.raw.serverb), pwd.toCharArray());
//            kks.load(CrashApplication.APP
//                    .getResources()
//                    .openRawResource(R.raw.server), pwd.toCharArray());
//            keyManager.init(kks, pwd.toCharArray());
//            trustManager.init(tks);
//            sslContext.init(keyManager.getKeyManagers(),
//                    trustManager.getTrustManagers(), null);
//            return sslContext.getSocketFactory();
////            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
////            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
////            keyStore.load(null);
////            int index = 0;
////            for (InputStream certificate : certificates) {
////                String certificateAlias = Integer.toString(index++);
////                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
////
////                try {
////                    if (certificate != null)
////                        certificate.close();
////                } catch (IOException e) {
////                }
////            }
////
////            SSLContext sslContext = SSLContext.getInstance("TLS");
////
////            TrustManagerFactory trustManagerFactory =
////                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
////
////            trustManagerFactory.init(keyStore);
////            sslContext.init (
////                            null,
////                            trustManagerFactory.getTrustManagers(),
////                            new SecureRandom());
////            return sslContext.getSocketFactory();
//        } catch (Exception e) {
//            System.out.println("~!~ failed https");
//            e.printStackTrace();
//        }
//        return null;
//    }

}