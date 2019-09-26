package com.stingluc.booksearch.data.source;

import com.stingluc.booksearch.data.pojos.Response;
import com.stingluc.booksearch.data.source.remote.WebService;
import com.stingluc.booksearch.injection.annotations.ApplicationScope;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@ApplicationScope
public class DataSourceRemote {

    private WebService webService;

    @Inject
    public DataSourceRemote(WebService webService){
        this.webService = webService;
    }

    public Observable<Response> getBooks(Map<String,String> searchParams){
        return webService.getBooks(searchParams);
    }
}
