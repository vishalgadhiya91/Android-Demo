package com.wmt_demo.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wmt_demo.R;
import com.wmt_demo.adapter.BrandAdapter;
import com.wmt_demo.api.APIService;
import com.wmt_demo.api.AppServices;
import com.wmt_demo.db.DbHandler;
import com.wmt_demo.model.Brand;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DbHandler dbHandler;
    private List<Brand> localBrandList;
    private List<Brand> remoteBrandList;
    private List<Brand> originalRemoteBrandList;
    private List<Brand> syncedBrandList;
    RecyclerView rvList;
    BrandAdapter brandAdapter;
    BrandAdapter brandAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolBar();

        floatingInertButton();

        idFinder();

        objectCreation();
        // get all brand
        localBrandList = dbHandler.getAllBrand();

        debugLog("localBrandList.size() >> " + localBrandList.size());

        brandAdapter = new BrandAdapter(MainActivity.this, localBrandList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvList.setLayoutManager(mLayoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setAdapter(brandAdapter);


    }

    private void idFinder() {
        rvList = (RecyclerView) findViewById(R.id.rvList);
    }

    private void objectCreation() {
        dbHandler = new DbHandler(MainActivity.this);
        syncedBrandList = new ArrayList<Brand>();
        localBrandList = new ArrayList<Brand>();
    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void floatingInertButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddBrandDialog();
            }
        });
    }

    public void openAddBrandDialog() {

        final LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dailog_brand_input, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setCancelable(false);

        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();

        final EditText edtBrandName = (EditText) mView.findViewById(R.id.edtBrandName);
        final EditText edtBrandDescription = (EditText) mView.findViewById(R.id.edtBrandDescription);
        final Button btnAdd = (Button) mView.findViewById(R.id.btnAdd);
        final Button btnCancel = (Button) mView.findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nm = edtBrandName.getText().toString().trim();
                String desc = edtBrandDescription.getText().toString().trim();
                if (nm == null || nm.length() <= 0) {
                    edtBrandName.setError("Please enter brand name");
                    edtBrandName.setFocusable(true);
                } else if (desc == null || desc.length() <= 0) {
                    edtBrandDescription.setError("Please enter description");
                } else {
                    Brand brand = new Brand();
                    brand.setName(nm);
                    brand.setDescription(desc);
                    brand.setSyncStatus("0");
                    dbHandler.addBrand(brand);
                    alertDialogAndroid.cancel();
                    localBrandList.add(0, brand);
                    syncedBrandList.add(0, brand);

                    if (brandAdapter != null)
                        brandAdapter.notifyDataSetChanged();

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogAndroid.cancel();
            }
        });


        alertDialogAndroid.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync) {
            syncData();
        }

        return super.onOptionsItemSelected(item);
    }

    private void syncData() {
        // Get local un sync data
        List<Brand> unSyncBrandList = dbHandler.getAllUnSyncBrand();
        debugLog("unSyncBrandList.size() >> " + unSyncBrandList.size());

        // Add new recode on remote
        insertNewLocalDataToRemote(createJsonOfListData(unSyncBrandList));
    }
    private String createJsonOfListData(List<Brand> localBrandList) {
        String strJson = null;
        if (localBrandList != null && localBrandList.size() > 0) {
            try {
                JSONObject jsonObject = new JSONObject();

                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < localBrandList.size(); i++) {
                    JSONObject jo = new JSONObject();
                    jo.put("name", localBrandList.get(i).getName());
                    jo.put("description", localBrandList.get(i).getDescription());
                    jsonArray.put(jo);
                }
                strJson = jsonObject.put("brand", jsonArray).toString();
                debugLog("Created json " + strJson);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return strJson;
    }

    private void insertNewLocalDataToRemote(String strJson) {
        if (strJson != null && strJson.length() > 0) {
            HashMap map = new HashMap();
            map.put("data", strJson);
            AppServices.insertData(MainActivity.this, map, new APIService.Success<String>() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("error_code").equals("1")) {
                            Toast.makeText(MainActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT);
                            // Delete all local data
                            // dbHandler.deleteAllBrand();

                            new AsyncTask<Void, Void, Void>() {

                                @Override
                                protected Void doInBackground(Void... voids) {
                                    // Delete all local data
                                    dbHandler.deleteAllBrand();

                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    getAllRemoteBrandsAndAddInLocal();
                                    super.onPostExecute(aVoid);
                                }
                            }.execute();
                        } else {
                            Toast.makeText(MainActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            getAllRemoteBrandsAndAddInLocal();

        }
    }
    private List<Brand> parseDate(String response) {
        if (response != null && response.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("error_code").equals("1")) {
                    remoteBrandList = new ArrayList<Brand>();
                    JSONArray jsonArray = jsonObject.getJSONArray("brand_list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        Brand brand = new Brand();
                        brand.setName(jo.optString("name"));
                        brand.setDescription(jo.optString("description"));
                        brand.setCreatedAt(jo.optString("created_at"));
                        brand.setSyncStatus("1");
                        remoteBrandList.add(brand);
                    }
                } else {
                    Toast.makeText(MainActivity.this, jsonObject.optString("message").toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return remoteBrandList;
    }

    // Get remote data
    private void getAllRemoteBrandsAndAddInLocal() {

        AppServices.fetchAllData(MainActivity.this, new APIService.Success<String>() {
            @Override
            public void onSuccess(String response) {
                final List<Brand> temRemoteList = parseDate(response);
                debugLog("temRemoteList.size() >> " + temRemoteList.size());

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        localBrandList.clear();
                        for (int i = 0; i < temRemoteList.size(); i++) {
                            dbHandler.addBrand(temRemoteList.get(i));
                        }

                        localBrandList = dbHandler.getAllBrand();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        debugLog("localBrandList.size() >> " + localBrandList.size());
                        // brandAdapter.notifyDataSetChanged();
                        brandAdapter = new BrandAdapter(MainActivity.this, localBrandList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rvList.setLayoutManager(mLayoutManager);
                        rvList.setItemAnimator(new DefaultItemAnimator());
                        rvList.setAdapter(brandAdapter);
                        //super.onPostExecute(aVoid);
                    }
                }.execute();

            }
        });
    }


    private void debugLog(String message) {
        Log.d(TAG, message);
    }
}
