package info.santhosh.omdbsearchclient.omdbApiRetrofitService;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;

public class RetrofitLoader extends AsyncTaskLoader<searchService.ResultWithDetail> {

    private static final String LOG_TAG = "RetrofitLoader";

    private final String mTitle;

    private searchService.ResultWithDetail mData;

    public RetrofitLoader(Context context, String title) {
        super(context);
        mTitle = title;
    }

    @Override
    public searchService.ResultWithDetail loadInBackground() {
        // obtem resultado da chamada da API
        try {
            searchService.Result result =  searchService.performSearch(mTitle);
            searchService.ResultWithDetail resultWithDetail = new searchService.ResultWithDetail(result);
            if(result.Search != null) {
                for(searchService.Movie movie: result.Search) {
                    resultWithDetail.addToList(searchService.getDetail(movie.imdbID));
                }
            }
            return  resultWithDetail;
        } catch(final IOException e) {
            Log.e(LOG_TAG, "Error from api access", e);
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        } else {
            forceLoad();
        }
    }


    @Override
    protected void onReset() {
        Log.d(LOG_TAG, "onReset");
        super.onReset();
        mData = null;
    }

    @Override
    public void deliverResult(searchService.ResultWithDetail data) {
        if (isReset()) {
            return;
        }

        searchService.ResultWithDetail oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

    }
}
