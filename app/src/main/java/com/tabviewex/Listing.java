package com.tabviewex;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public  class Listing extends Fragment {


    private RecyclerView rv;
    private TextView emptyView;
    private List<Item> itemlist;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeContainer;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listing, container, false);
        rv=(RecyclerView)rootView.findViewById(R.id.rv);
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);



        final FloatingActionButton programFab1 = (FloatingActionButton) rootView.findViewById(R.id.menu_item1);
        final FloatingActionButton programFab2 = (FloatingActionButton) rootView.findViewById(R.id.menu_item2);


        programFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "delete call", Toast.LENGTH_SHORT).show();
            }
        });
        programFab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "view call", Toast.LENGTH_SHORT).show();
            }
        });




        // Swipe view init
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loaddata();
                swipeContainer.setRefreshing(false);
            }
        });


        loaddata();






        return rootView;
    }


    public void loaddata()
    {

        if (isNetworkAvailable(getActivity())) {

            new TestAsync().execute();

        } else {
            Toast.makeText(getActivity(),"Check your internet connection", 500).show();
            emptyView.setVisibility(View.VISIBLE);
        }


    }


    class TestAsync extends AsyncTask<Void, Integer, String>
    {

        protected void onPreExecute (){

          progressBar.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(Void...arg0) {

            String abc="";

            try {
                abc =  getResponseText("https://api.import.io/store/data/85f69b3f-b0a7-4f14-80e1-682ff325d4f2/_query?input/webpage/url=http://owlkingdom.com/&_user=a31791af-10fa-4234-9e23-508d6c7838e7&_apikey=EHJSRe1mjAgGkREpxEh4nTK385HVh1apflVdRjtwHW/7mIYy0a6meoIXt5MGQHQUFJxRBSORj8SnAWvoelhctw==");
                Log.e("CALL",abc);

            } catch (IOException e) {
                e.printStackTrace();
            }


            return abc;
        }



        protected void onPostExecute(String result) {

            itemlist = new ArrayList<>();
            String OutputData = "";
            JSONObject jsonResponse;

            try {

                /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                jsonResponse = new JSONObject(result);

                /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                /*******  Returns null otherwise.  *******/
                JSONArray jsonMainNode = jsonResponse.optJSONArray("results");

                /*********** Process each JSON Node ************/


                Log.i("JSON parse", jsonMainNode.toString());
                int lengthJsonArr = jsonMainNode.length();

                for(int i=0; i < lengthJsonArr; i++)
                {
                    /****** Get Object for each JSON node.***********/
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    /******* Fetch node values **********/
                    String song_id        = jsonChildNode.getString("image").toString();

                    itemlist.add(new Item(jsonChildNode.getString("price"), jsonChildNode.getString("image"), jsonChildNode.getString("name"), jsonChildNode.getString("description")));


                  //  Log.i("JSON parse", song_id);
                }

                initializeAdapter();
                progressBar.setVisibility(View.GONE);

            } catch (JSONException e) {

                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }




        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(itemlist,getActivity());
        rv.setAdapter(adapter);

        emptydata();


    }

    public void emptydata()
    {

        if (itemlist.isEmpty()) {
            rv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            rv.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }




    }
    private String getResponseText(String stringUrl) throws IOException
    {
        StringBuilder response  = new StringBuilder();

        URL url = new URL(stringUrl);
        HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
        if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()),8192);
            String strLine = null;
            while ((strLine = input.readLine()) != null)
            {
                response.append(strLine);
            }
            input.close();
        }
        return response.toString();
    }


}