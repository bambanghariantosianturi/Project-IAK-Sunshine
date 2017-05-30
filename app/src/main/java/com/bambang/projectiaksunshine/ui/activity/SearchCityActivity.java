package com.bambang.projectiaksunshine.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bambang.projectiaksunshine.R;
import com.bambang.projectiaksunshine.data.local.WeatherCache;
import com.bambang.projectiaksunshine.data.local.remote.WeatherService;
import com.bambang.projectiaksunshine.data.local.repository.WeatherRepository;
import com.bambang.projectiaksunshine.data.local.repository.impl.WeatherRepositoryImpl;
import com.bambang.projectiaksunshine.model.CityWeatherCondition;
import com.bambang.projectiaksunshine.ui.adapter.CityListAdapter;
import com.bambang.projectiaksunshine.ui.contract.SearchCityContract;
import com.bambang.projectiaksunshine.ui.event.RecyclerViewItemClickListener;
import com.bambang.projectiaksunshine.ui.presenter.SearchCityPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bambanghs on 5/24/2017.
 */

public class SearchCityActivity extends AppCompatActivity implements SearchCityContract.View {

    @BindView(R.id.successContainer)
    protected RelativeLayout successContainer;

    @BindView(R.id.errorContainer)
    protected LinearLayout errorContainer;

    @BindView(R.id.loadingContainer)
    protected LinearLayout loadingContainer;

    @BindView(R.id.emptyContainer)
    protected LinearLayout emptyContainer;

    @BindView(R.id.citiesRecyclerView)
    protected RecyclerView citiesRecyclerView;

    private SearchCityContract.Presenter presenter;

    private List<CityWeatherCondition> cities;

    private CityListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);

        ButterKnife.bind(this);
        dependecyInjection();
        initialize(savedInstanceState);

        presenter.handleIntent(getIntent());
    }

    private void dependecyInjection() {
        WeatherService service = WeatherService.Builder.build();
        WeatherCache cache = new WeatherCache();
        WeatherRepository repository = new WeatherRepositoryImpl(service, cache);
        presenter = new SearchCityPresenter(this, repository);
    }

    private void initializeToolbar() {
        try {
            getSupportActionBar().setTitle(getString(R.string.empty));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (NullPointerException e) {
            showToast(getString(R.string.error_during_init));
        }
    }

    private void initialize(Bundle bundle) {
        initializeToolbar();
        initializeRecyclerView();
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        presenter.handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_city, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);

        return true;
    }

    public void initializeRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        citiesRecyclerView.setLayoutManager(layoutManager);
        cities = new ArrayList<>();
        adapter = new CityListAdapter(this, cities);
        citiesRecyclerView.setAdapter(adapter);

        citiesRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CityWeatherCondition city = cities.get(position);
                presenter.addCity(city);
            }
        }));
    }

    @Override
    public void setupCurrentWeather(CityWeatherCondition cityWeatherCondition) {
        cities.add(cityWeatherCondition);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (query != null && !"".equals(query.trim())) {
                presenter.loadCity(query);
            }
        }
    }

    @Override
    public void showLoadingLayout() {
        successContainer.setVisibility(View.GONE);
        errorContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.VISIBLE);
        emptyContainer.setVisibility(View.GONE);
    }

    @Override
    public void showErrorLayout() {
        successContainer.setVisibility(View.GONE);
        errorContainer.setVisibility(View.VISIBLE);
        loadingContainer.setVisibility(View.GONE);
        emptyContainer.setVisibility(View.GONE);
    }

    @Override
    public void showSuccessLayout() {
        successContainer.setVisibility(View.VISIBLE);
        errorContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.GONE);
        emptyContainer.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyLayout() {
        successContainer.setVisibility(View.GONE);
        errorContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.GONE);
        emptyContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void goBackToWeatherList() {
        finish();
    }
}
