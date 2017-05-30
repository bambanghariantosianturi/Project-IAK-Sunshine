package com.bambang.projectiaksunshine.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.bambang.projectiaksunshine.Constants;
import com.bambang.projectiaksunshine.R;
import com.bambang.projectiaksunshine.core.aggregation.WeatherAggregation;
import com.bambang.projectiaksunshine.data.local.WeatherCache;
import com.bambang.projectiaksunshine.data.local.remote.WeatherService;
import com.bambang.projectiaksunshine.data.local.repository.WeatherRepository;
import com.bambang.projectiaksunshine.data.local.repository.impl.WeatherRepositoryImpl;
import com.bambang.projectiaksunshine.model.CityWeatherCondition;
import com.bambang.projectiaksunshine.ui.adapter.WeatherListAdapter;
import com.bambang.projectiaksunshine.ui.contract.WeatherListContract;
import com.bambang.projectiaksunshine.ui.presenter.WeatherListPresenter;
import com.bambang.projectiaksunshine.ui.event.RecyclerViewItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by bambanghs on 5/24/2017.
 */

public class WeatherListActivity extends AppCompatActivity implements WeatherListContract.View {

    @BindView(R.id.swipeLayout)
    protected SwipeRefreshLayout swipeLayout;

    @BindView(R.id.weatherRecyclerView)
    protected RecyclerView weatherRecyclerView;

    @BindView(R.id.successContainer)
    protected LinearLayout successContainer;

    @BindView(R.id.errorContainer)
    protected LinearLayout errorContainer;

    @BindView(R.id.loadingContainer)
    protected LinearLayout loadingContainer;

    @BindView(R.id.emptyContainer)
    protected LinearLayout emptyContainer;

    @BindView(R.id.fab)
    protected FloatingActionButton fab;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private WeatherListAdapter adapter;

    private WeatherListContract.Presenter presenter;

    @OnClick(R.id.fab)
    protected void fabClick() {
        findCity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_list);

        ButterKnife.bind(this);
        dependecyInjection();
        initialize(savedInstanceState);

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadCities();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dependecyInjection() {
        WeatherService service = WeatherService.Builder.build();
        WeatherCache cache = new WeatherCache();
        WeatherRepository repository = new WeatherRepositoryImpl(service, cache);
        presenter = new WeatherListPresenter(this, repository);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constants.WEATHER_KEY, presenter.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    private void initialize(Bundle bundle) {
        if (bundle != null && bundle.containsKey(Constants.WEATHER_KEY)) {
            WeatherAggregation aggregation = (WeatherAggregation) bundle.getSerializable(Constants.WEATHER_KEY);
            presenter.onLoadInstanceState(aggregation);
            presenter.refreshUi();
        }
    }

    private void addSwipeToDeleteInRecyclerView() {
        final Paint paint = new Paint();

        ItemTouchHelper swipeToDeleteTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                adapter.removeItem(position);
                presenter.removeCity(position);
            }

            @Override
            public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon = null;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX < 0) {
                        paint.setColor(getResources().getColor(R.color.red));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        canvas.drawRect(background,paint);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_black_24dp);
                        RectF iconDest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        canvas.drawBitmap(icon,null,iconDest, paint);
                    }
                }
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        swipeToDeleteTouchHelper.attachToRecyclerView(weatherRecyclerView);
    }

    @OnClick(R.id.retryButton)
    void retryCitiesWeather() {
        presenter.retryButtonClick();
    }

    @Override
    public void setupWeatherList(List<CityWeatherCondition> cities) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new WeatherListAdapter(this, cities);
        weatherRecyclerView.setLayoutManager(manager);
        weatherRecyclerView.setAdapter(adapter);
        weatherRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                presenter.weatherItemClick(position);
            }
        }));

        // add swipe to delete function in RecyclerView
        addSwipeToDeleteInRecyclerView();

        // updates weather information
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                presenter.loadCities();
            }
        });
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
    public void checkSwipeRefreshing() {
        if (swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void showWeatherForecast(CityWeatherCondition city) {
        Intent intentWeatherForecast = new Intent(this, WeatherForecastActivity.class);
        intentWeatherForecast.putExtra(Constants.CITY_ID_EXTRA, city.getId());
        startActivity(intentWeatherForecast);
    }

    @Override
    public void findCity() {
        startActivity(new Intent(this, SearchCityActivity.class));
    }
}
