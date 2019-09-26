package com.stingluc.booksearch.data.source;

import com.stingluc.booksearch.data.entities.Book;
import com.stingluc.booksearch.data.pojos.Response;
import com.stingluc.booksearch.injection.annotations.ApplicationScope;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@ApplicationScope
public class DataSourceRepository {

    private DataSourceRemote remote;
    private DataSourceLocal local;

    @Inject
    public DataSourceRepository(DataSourceRemote dataSourceRemote, DataSourceLocal dataSourceLocal){
        this.remote = dataSourceRemote;
        this.local = dataSourceLocal;
    }

    public void insertAll(List<Book> entities){
        local.insertAll(entities);
    }

    public Observable<List<Book>> getAllBooks(){
        return local.getAllBooks();
    }

    public Observable<Response> getBooks(Map<String,String> searchParams){
        return remote.getBooks(searchParams);
    }
}
