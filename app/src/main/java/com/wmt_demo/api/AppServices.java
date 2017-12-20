package com.wmt_demo.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wmt_demo.R;
import com.wmt_demo.utility.DialogUtility;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Vishal on 19-11-2016.
 */

public class AppServices extends APIService {

    private static final String TAG = AppServices.class.getSimpleName();

    private static final String URL_INSERT_DATA = APIService.BASE_URL+"insert.php";

    private static final String URL_FETCH_ALL_DATA = APIService.BASE_URL+"list.php";

    private static final String URL_FETCH_SINGLE_DATA = APIService.BASE_URL+"single.php";

    // Insert data
    public static void insertData(final Context context, final HashMap<String, String> paramMap, final Success<String> successListener) {
        final ProgressDialog progressDialog = DialogUtility.getProgressDialog(context, context.getString(R.string.dlg_msg_wait_while_loading), false);
        progressDialog.show();

        logDebug("URL_INSERT_DATA >> " + URL_INSERT_DATA);
        logDebug("Parameter Map >> " + paramMap);

        StringRequest insertRequest = new StringRequest(Request.Method.POST, URL_INSERT_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DialogUtility.dismissProgressDialog(progressDialog);
                logDebug("Insert Response >> " + response);
                try {
                    successListener.onSuccess(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogUtility.dismissProgressDialog(progressDialog);
                logDebug("****** Insert data error  ****** \n " + error.getMessage());
                Toast.makeText(context, context.getString(R.string.toast_msg_something_is_wrong), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paramMap;
            }
        };

        APIController.getInstance(context).addRequest(insertRequest, TAG);
    }


    // Fetch all data
    public static void fetchAllData(final Context context,  final Success<String> successListener) {
        final ProgressDialog progressDialog = DialogUtility.getProgressDialog(context, context.getString(R.string.dlg_msg_wait_while_loading), false);
        progressDialog.show();

        logDebug("URL_FETCH_ALL_DATA >> " + URL_FETCH_ALL_DATA);

        StringRequest fetchAllDataRequest = new StringRequest(Request.Method.POST, URL_FETCH_ALL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DialogUtility.dismissProgressDialog(progressDialog);
                logDebug("Fetch all data Response >> " + response);
                try {
                    successListener.onSuccess(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogUtility.dismissProgressDialog(progressDialog);
                logDebug("****** fetch all data error  ****** \n " + error.getMessage());
                Toast.makeText(context, context.getString(R.string.toast_msg_something_is_wrong), Toast.LENGTH_SHORT).show();
            }
        });

        APIController.getInstance(context).addRequest(fetchAllDataRequest, TAG);
    }

    // Fetch single data
    public static void fetchSingleData(final Context context, final HashMap<String, String> paramMap, final Success<String> successListener) {
        final ProgressDialog progressDialog = DialogUtility.getProgressDialog(context, context.getString(R.string.dlg_msg_wait_while_loading), false);
        progressDialog.show();

        logDebug("URL_FETCH_SINGLE_DATA >> " + URL_FETCH_SINGLE_DATA);
        logDebug("Parameter Map >> " + paramMap);

        StringRequest fetchAllDataRequest = new StringRequest(Request.Method.POST, URL_FETCH_SINGLE_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DialogUtility.dismissProgressDialog(progressDialog);
                logDebug("Fetch single data Response >> " + response);
                try {
                    successListener.onSuccess(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogUtility.dismissProgressDialog(progressDialog);
                logDebug("****** fetch single data error  ****** \n " + error.getMessage());
                Toast.makeText(context, context.getString(R.string.toast_msg_something_is_wrong), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paramMap;
            }
        };

        APIController.getInstance(context).addRequest(fetchAllDataRequest, TAG);
    }

    private static void logDebug(String msg)
    {
        Log.d(TAG,msg);
    }
}
