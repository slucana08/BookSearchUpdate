package com.stingluc.booksearch.data.source;

import com.stingluc.booksearch.data.entities.Book;
import com.stingluc.booksearch.data.source.local.DBBookSearch;
import com.stingluc.booksearch.data.source.local.dao.DaoBook;
import com.stingluc.booksearch.injection.annotations.ApplicationScope;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;

@ApplicationScope
public class DataSourceLocal {

    private DBBookSearch dbBookSearch;

    private DaoBook daoBook;

    private ThreadPoolExecutor threadPoolExecutor =  new ThreadPoolExecutor(4,4,20000,
            TimeUnit.MILLISECONDS,new LinkedBlockingDeque<>(10));

    @Inject
    public DataSourceLocal(DBBookSearch dbBookSearch){
        this.dbBookSearch = dbBookSearch;
        daoBook = dbBookSearch.daoBook();
    }

    public void insertAll(List<Book> entities){
        threadPoolExecutor.execute(() -> {
            daoBook.deleteAll();
            daoBook.insertAll(entities);
        });
    }

    public Observable<List<Book>> getAllBooks(){
        return daoBook.getAllBooks().toObservable();
    }
}
