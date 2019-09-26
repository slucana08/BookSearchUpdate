package com.stingluc.booksearch.data.source.remote;

import com.stingluc.booksearch.data.pojos.Response;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface WebService {

    @GET(WSCalls.SEARCH_KIND)
    Observable<Response> getBooks(@QueryMap Map<String,String> searchParams);
}
