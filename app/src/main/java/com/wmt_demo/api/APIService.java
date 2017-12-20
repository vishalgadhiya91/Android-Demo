package com.wmt_demo.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public abstract class APIService {

    private static final String TAG = APIService.class.getSimpleName();

    public static final String BASE_URL = "http://appsdata2.cloudapp.net/demo/androidApi/";


    public interface Success<T> {
        public void onSuccess(T response);
    }

    protected static void handleError(final Context context, VolleyError error) {
        Log.d(TAG, "Error :: " + error);

        if (error instanceof NoConnectionError) {

            Toast.makeText(context, "Please check internet connection !", Toast.LENGTH_LONG).show();

        } else if (error.networkResponse != null) {

            Log.d(TAG, "CODE =" + error.networkResponse.statusCode);
            Log.d(TAG, new String(error.networkResponse.data));
        }
    }

    private static String getErrorMessage(byte[] responseData) {
        String message = null;
        String code = null;
        try {
            JSONObject jsonObject = new JSONObject(new String(responseData));
            JSONObject subJsonObj = jsonObject.getJSONObject("error");
            code = subJsonObj.getString("code");
            message = subJsonObj.getString("message");
        } catch (Exception e) {
            Log.d(TAG, "getErrorMessage" + e.getMessage());
            message = "Something is wrong,Please try again";
        }
        return message;
    }

}
