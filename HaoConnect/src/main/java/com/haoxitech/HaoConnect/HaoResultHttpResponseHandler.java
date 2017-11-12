package com.haoxitech.HaoConnect;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * HaoResultHttpResponseHandler
 */
public abstract class HaoResultHttpResponseHandler extends TextHttpResponseHandler {

    private static final String LOG_TAG = "HaoHttpRH";

    public abstract void onSuccess(HaoResult result);

    public void onFail(HaoResult result) {
        AsyncHttpClient.log.w(LOG_TAG, "onFailure(int, Header[], String, Throwable) was not overriden, but callback was received", null);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        HaoResult haoResult = (HaoResult) HaoResult.instanceModel(null, -1, responseString, null);
        onFail(haoResult);
    }

    @Override
    public final void onSuccess(int statusCode, Header[] headers, String responseString) {

        try {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);
            Log.d(LOG_TAG, "response\n");
            Log.d(LOG_TAG, responseString);

            HaoResult haoResult = (HaoResult) HaoResult.instanceModel(jsonObject.get("results"), jsonObject.get("errorCode").getAsInt(), jsonObject.get("errorStr").getAsString(), jsonObject.get("extraInfo"));
            if (haoResult.isResultsOK()) {
                onSuccess(haoResult);
            } else {
                onFail(haoResult);
            }
        } catch (Exception e) {
            HaoResult haoResult = (HaoResult) HaoResult.instanceModel(null, -1, e.toString(), null);
            onFail(haoResult);
        }
    }

    @Override
    public final void onSuccess(int statusCode, Header[] headers, byte[] responseBytes) {
        super.onSuccess(statusCode, headers, responseBytes);
    }
}