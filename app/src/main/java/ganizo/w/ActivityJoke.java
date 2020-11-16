package ganizo.w;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityJoke extends AppCompatActivity {
    private ProgressDialog loading;
    final String URL_GET_DATA = "https://alterbliss.co.za/sovtech.php?opt=random&category=";
    RecyclerView recyclerView;
    Adapter adapter;
    List<SovTechAPI> heroList;
    ImageView refreshActivity;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bundle = getIntent().getExtras();

        refreshActivity = findViewById(R.id.refreshActivity);
        recyclerView = findViewById(R.id.recyclerViewJoke);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        heroList = new ArrayList<>();
        loadJoke();
        refreshActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });
    }

    private void loadJoke() {
        show_dialog(getString(R.string.loading));
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GET_DATA+bundle.getString("category"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = "["+response+"]";
                        loading.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                SovTechAPI hero = new SovTechAPI(
                                        obj.getString(getString(R.string.value)),
                                        obj.getString(getString(R.string.icon_url)),
                                        obj.getString(getString(R.string.created_at))
                                );

                                heroList.add(hero);
                            }

                            adapter = new Adapter(heroList, getApplicationContext());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.dismiss();
                            show_toast(getString(R.string.comm_error));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        show_toast(getString(R.string.comm_error));
                        loading.dismiss();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
}