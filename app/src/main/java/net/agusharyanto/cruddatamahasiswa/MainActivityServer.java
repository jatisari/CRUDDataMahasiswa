package net.agusharyanto.cruddatamahasiswa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.agusharyanto.cruddatamahasiswa.adapter.MahasiswaAdapter;
import net.agusharyanto.cruddatamahasiswa.model.Mahasiswa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivityServer extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MahasiswaAdapter rvAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context = MainActivityServer.this;

    private static  final int REQUEST_CODE_ADD =1;
    private static  final int REQUEST_CODE_EDIT =2;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityServer.this, MahasiswaActivityServer.class);
                // intent.putExtra("action", REQUEST_CODE_ADD);
                startActivityForResult(intent, REQUEST_CODE_ADD);

            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
       // initializeData();
        //gambarDatakeRecyclerView();
        pDialog = new ProgressDialog(this);
        loadDataServerVolley();

    }

    private void gambarDatakeRecyclerView(){
        rvAdapter = new MahasiswaAdapter(mahasiswaList);
        mRecyclerView.setAdapter(rvAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Mahasiswa mahasiswa = rvAdapter.getItem(position);
                        //Toast.makeText(context, "Name :" + mahasiswa.getNama(), Toast.LENGTH_SHORT).show();
                        // selectedPosition = position;
                        Intent intent = new Intent(MainActivityServer.this, MahasiswaActivityServer.class);
                        intent.putExtra("mahasiswa", mahasiswa);
                        startActivityForResult(intent, REQUEST_CODE_EDIT);
                    }
                })
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ADD: {
                if (resultCode == RESULT_OK && null != data) {
                    if (data.getStringExtra("refreshflag").equals("1")) {
                       loadDataServerVolley();
                    }
                }
                break;
            }
            case REQUEST_CODE_EDIT: {
                if (resultCode == RESULT_OK && null != data) {
                    if (data.getStringExtra("refreshflag").equals("1")) {
                       loadDataServerVolley();
                    }
                }
                break;
            }
        }
    }






    private List<Mahasiswa> mahasiswaList = new ArrayList<Mahasiswa>();
    // This method creates an ArrayList that has three Fruit objects

    //ambil data sever volley
    private void loadDataServerVolley(){

//        mahasiswaList.clear();
        String url = GlobalVar.IP_SERVER+"/mahasiswa/listdata.php";
        pDialog.setMessage("Retieve Data Mahasiswa...");
        showDialog();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("tag","response:"+response);
                        hideDialog();
                        processResponse(response);
                        gambarDatakeRecyclerView();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                //params.put("id","1");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    private void processResponse(String response){

        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            Log.d("TAG", "data length: " + jsonArray.length());
            Mahasiswa objectmahasiswa = null;
            mahasiswaList.clear();
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                objectmahasiswa= new Mahasiswa();
                objectmahasiswa.setId(obj.getString("id"));

                objectmahasiswa.setNama(obj.getString("nama"));
                objectmahasiswa.setNim(obj.getString("nim"));
                objectmahasiswa.setJurusan(obj.getString("jurusan"));


                mahasiswaList.add(objectmahasiswa);
            }

        } catch (JSONException e) {
            Log.d("MainActivity", "errorJSON");
        }

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
