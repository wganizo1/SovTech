package ganizo.w;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Categories extends AppCompatActivity {

    String[] categoryTitle;
    final String URL_GET_DATA = "https://api.chucknorris.io/jokes/categories";
    private ProgressDialog loading;
    private JSONArray cats;
    private SimpleAdapter simpleAdapter;
    private ListView simpleListview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);
        simpleListview = findViewById(R.id.list_view);
        if (isConnected()) {
            getCategories();
        }
        else{
            alert(getString(R.string.no_internet));
        }

        simpleListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ActivityJoke.class);
                i.putExtra("category", categoryTitle[position].toLowerCase());
                startActivity(i);
            }
        });
    }
    private void getCategories() {
        show_dialog(getString(R.string.loading));
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GET_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            cats = new JSONArray(response);
                            categoryTitle=new String[cats.length()];
                            for(int i = 0; i<cats.length(); i++){
                                categoryTitle[i]=cats.getString(i);
                            }
                            categoriesAdapter();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            show_toast(e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        show_toast(getString(R.string.comm_error)+error);
                        loading.dismiss();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void categoriesAdapter(){
        List<HashMap<String,String>> aList = new ArrayList<>();
        for (int x = 0; x < categoryTitle.length; x++){
            HashMap<String, String> hm = new HashMap<>();
            hm.put("categoryTitle", categoryTitle[x].toUpperCase());
            aList.add(hm);
        }

        String[] from = {
                "categoryTitle"
        };
        int[] to = {
                R.id.Title
        };
        simpleAdapter = new SimpleAdapter(getBaseContext(),aList, R.layout.listview_items,from,to);
        simpleListview.setAdapter(simpleAdapter);
    }
    public void show_toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public void show_dialog(String ltxt) {
        try {
            loading = ProgressDialog.show(this, ltxt, getString(R.string.please_wait), false, false);
        } catch (Exception e) {
            loading.dismiss();
        }
    }
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {

        }
        return connected;
    }
    public  void alert(String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        startActivity(getIntent());
                    }
                });

        builder1.setNegativeButton(
                "Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}