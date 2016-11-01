package com.example.user.test_ots;

import com.loopj.android.http.*;

public class FileDownloadClient {
    private static final String BASE_URL = "http://167.88.115.33/images/story_represent_image/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void download(String url, RequestParams params, BinaryHttpResponseHandler binaryHttpResponseHandler){
        client.get(getAbsoluteUrl(url), params, binaryHttpResponseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}