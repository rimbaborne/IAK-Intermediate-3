package iak.rimbaborne.intermediate3;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import iak.rimbaborne.intermediate3.adapter.RecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private final String API_Key = "a1f3faf95d3a6c30d3e3b20acfcdbeae";
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private String FilmCategory;
    private SearchView mnSearch;
    private ItemObject a;

    private ProgressDialog pDialog;

    private SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        requestJsonObject(0);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                adapter.itemList.clear();
                adapter.notifyDataSetChanged();
                requestJsonObject(0);
            }
        });

    }

    private void requestJsonObject(int i) {
        if (i == 0) {
            setTitle("Popular Movie");
            FilmCategory = "popular";
        } else if (i == 1) {
            setTitle("Top Rated Movie");
            FilmCategory = "top_rated";
        } else if (i == 2) {
            setTitle("Coming Soon");
            FilmCategory = "upcoming";
        }

        RequestParams params = new RequestParams();
        params.put("api_key",API_Key);
        String FullURL = "http://api.themoviedb.org/3/movie/"+FilmCategory;
        MyParsingGson(params, FullURL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnPopuler:
                requestJsonObject(0); //Popular
                break;
            case R.id.mnFavorit:
                requestJsonObject(1); //Top Rated
                break;
            case R.id.mnComing:
                requestJsonObject(2); //Up Coming
                break;
            case R.id.mnAbout:
                Toast.makeText(getApplicationContext().getApplicationContext(),
                        "Developer by Gagang R. B ",
                        Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void MyParsingGson(RequestParams params,String url) {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                pDialog.hide();
                try {
                    android.util.Log.d("Response: ", "> " + response);
                    try{
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();
                        a = mGson.fromJson(response, ItemObject.class);
                        adapter = new RecyclerViewAdapter(MainActivity.this, a.results);
                        recyclerView.setAdapter(adapter);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

                swipe.setRefreshing(false);
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,String content) {
                // Hide Progress Dialog
                pDialog.hide();
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();

                }
                swipe.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
