package com.example.sarahz.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.sarahz.nytimessearch.EndlessScrollListener;
import com.example.sarahz.nytimessearch.FilterSearch;
import com.example.sarahz.nytimessearch.ItemClickSupport;
import com.example.sarahz.nytimessearch.R;
import com.example.sarahz.nytimessearch.adaptors.ArticleArrayAdapter;
import com.example.sarahz.nytimessearch.modals.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class SearchActivity extends AppCompatActivity implements FilterSearch.FilterSearchDialogListener {
    private RecyclerView gvResults;
    private String searchQuery;
    private String beginDate;
    private String sortOrder;
    private boolean isArts;
    private boolean isFashionAndStyle;
    private boolean isSports;

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }

    public void setupViews() {
        gvResults = (RecyclerView) findViewById(R.id.gvResults);
        gvResults.setItemAnimator(new SlideInUpAnimator());

        articles = new ArrayList<>();

        // Create adapter passing in the sample user data
        adapter = new ArticleArrayAdapter(articles, this);
        // Attach the adapter to the recyclerview to populate items
        gvResults.setAdapter(adapter);

        int columns = 4;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(columns,
                StaggeredGridLayoutManager.VERTICAL);
        gvResults.setLayoutManager(layoutManager);

        gvResults.addOnScrollListener(new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                FetchApi(searchQuery, page);
            }
        });

        // hook up listener for grid click
        ItemClickSupport.addTo(gvResults).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                        Article article = articles.get(position);
                        intent.putExtra("article", article);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;

                int currentSize = articles.size();
                articles.clear();
                adapter.notifyItemRangeRemoved(0, currentSize);

                FetchApi(searchQuery, 0);

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // add setting
        MenuItem settingsItem = menu.findItem(R.id.action_settings);
        settingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FilterSearch filterSearch = FilterSearch.newInstance(
                        getString(R.string.filter_search_fragment_title), beginDate, sortOrder, isArts,
                        isFashionAndStyle, isSports);
                filterSearch.show(fragmentManager, "filter_search");
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFinishFilterSearchDialog(String beginDate, String sortOrder, boolean isArts,
                                           boolean isFashionAndStyle, boolean isSports) {
        this.beginDate = beginDate;
        this.sortOrder = sortOrder;
        this.isArts = isArts;
        this.isFashionAndStyle = isFashionAndStyle;
        this.isSports = isSports;
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

    public void FetchApi(String query, int page) {

        //Toast.makeText(this, "Searching for " + query, Toast.LENGTH_LONG).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", "a3deb2c966b9414d98b1f20fd2f659c1");
        params.put("page", page);
        params.put("q", query);

        if (beginDate != null && !beginDate.isEmpty()) {
            params.put("begin_date", beginDate);
        }
        if (sortOrder != null) {
            params.put("sort", sortOrder.toLowerCase());
        }
        if (!newsDeskParameter().isEmpty()) {
            params.put("fq", newsDeskParameter());
        }

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResult = null;

                Log.d("DEBUG", url + " " + params.toString());

                try {
                    articleJsonResult = response.getJSONObject("response").getJSONArray("docs");
                    int currentSize = articles.size();
                    articles.addAll(Article.fromJSONArray(articleJsonResult));
                    adapter.notifyItemRangeInserted(currentSize, articles.size());
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Snackbar.make(gvResults, "API is not responding",
                        Snackbar.LENGTH_LONG).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private String newsDeskParameter() {
        StringBuilder newsDeskParam = new StringBuilder("news_desk:(");
        if (!isArts && !isFashionAndStyle && !isSports) {
            return "";
        }
        if (isArts) {
            newsDeskParam.append("\"Arts\"");
        }
        if (isFashionAndStyle) {
            if (newsDeskParam.length() > 11) {
                newsDeskParam.append(",");
            }
            newsDeskParam.append("\"Fashion & Style\"");
        }
        if (isSports) {
            if (newsDeskParam.length() > 11) {
                newsDeskParam.append(",");
            }
            newsDeskParam.append("\"Sports\"");
        }
        newsDeskParam.append(")");
        return newsDeskParam.toString();
    }

    private ArrayList<Article> getSampleArrayList() {
        return articles;
    }
}
