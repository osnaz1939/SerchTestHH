package com.act_ex.a1221.serchtesthh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        Callback<BasePaginationDto<VacancyDto>>, SwipeRefreshLayout.OnRefreshListener {

    public final int offset = 30;
    private int page = 0;


    private RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean loadingMore = false;
    ApiInterface service;
    Call<BasePaginationDto<VacancyDto>> call;
    int itemsCountLocal;
    ContentValues[] cvArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int itemsCountLocal = getItemsCountLocal();
        if (isOnline(this)) {
            this.getContentResolver().delete(RequestProvider.BASE_CONTENT_URI, null, null);
            service = ApiFactory.getVacanciesService();
            call = service.searchVacancy("android",20,getItemsCountLocal());
            call.enqueue(this);
        }else{
            if (getItemsCountLocal()>0) {
                getSupportLoaderManager().restartLoader(0, null, this);
            }else{
                Toast.makeText(this, "CONNECTION ERROR,LIST IS NULL", Toast.LENGTH_SHORT);
            }
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        CustomCursorRecyclerViewAdapter mAdapter = new CustomCursorRecyclerViewAdapter(this, null);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    @Override
    public void onResponse(Call<BasePaginationDto<VacancyDto>> call, Response<BasePaginationDto<VacancyDto>> response) {
        if (response.isSuccessful()) {
            BasePaginationDto<VacancyDto> page = response.body();
            List<VacancyDto> test = page.getItems();
            int size = test.size();
            int i = 0;
            cvArray = new ContentValues[size];
            for (VacancyDto dtoItem : test) {
                ContentValues cv = new ContentValues();
                cv.put(TableItems.TEXT, (dtoItem.getName()));
                Log.e("TAG", String.valueOf(cv));
                cvArray[i] = cv;
                i++;
            }
            getContentResolver().bulkInsert(RequestProvider.urlForItems(getItemsCountLocal()), cvArray);
            getSupportLoaderManager().restartLoader(0, null, this);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onFailure(Call<BasePaginationDto<VacancyDto>> call, Throwable t) {

    }


    private int getItemsCountLocal() {
        int itemsCount = 0;

        Cursor query = getContentResolver().query(RequestProvider.urlForItems(0), null, null, null, null);
        if (query != null) {
            itemsCount = query.getCount();
            query.close();
        }
        return itemsCount;
    }

    /*loader*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 0:
                return new CursorLoader(this, RequestProvider.urlForItems(offset * page), null, null, null, null);
            default:
                throw new IllegalArgumentException("no id handled!");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case 0:
                Log.d(TAG, "onLoadFinished: loading MORE");
                //   shortToast.setText("loading MORE " + page);
                //   shortToast.show();

                Cursor cursor = ((CustomCursorRecyclerViewAdapter) mRecyclerView.getAdapter()).getCursor();

                //fill all exisitng in adapter
                MatrixCursor mx = new MatrixCursor(TableItems.Columns);
                fillMx(cursor, mx);

                //fill with additional result
                fillMx(data, mx);

                ((CustomCursorRecyclerViewAdapter) mRecyclerView.getAdapter()).swapCursor(mx);


                handlerToWait.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingMore = false;
                    }
                }, 2000);

                break;
            default:
                throw new IllegalArgumentException("no loader id handled!");
        }
    }

    private Handler handlerToWait = new Handler();

    private void fillMx(Cursor data, MatrixCursor mx) {
        if (data == null)
            return;

        data.moveToPosition(-1);
        while (data.moveToNext()) {
            mx.addRow(new Object[]{
                    data.getString(data.getColumnIndex(TableItems._ID)),
                    data.getString(data.getColumnIndex(TableItems.TEXT))
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // TODO: 2016-10-13
    }

    //

    private static final String TAG = "MainActivity";

    @Override
    public void onRefresh() {
           if (isOnline(this)) {
           mSwipeRefreshLayout.setRefreshing(true);
            call = service.searchVacancy("android",20,itemsCountLocal);
            call.enqueue(this);
         }

    }
}

