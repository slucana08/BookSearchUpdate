package com.stingluc.booksearch.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.stingluc.booksearch.data.entities.Book;
import com.stingluc.booksearch.data.pojos.Response;
import com.stingluc.booksearch.data.source.DataSourceRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DataViewModel extends ViewModel {

    private DataSourceRepository dataSourceRepository;
    private MutableLiveData<Response> responseData = new MutableLiveData<>();
    private MutableLiveData<List<Book>> storedBooks = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public DataViewModel(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    public void setBooks(Map<String,String> searchParams) {
        disposable.add(dataSourceRepository.getBooks(searchParams)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> responseData.setValue(response),
                        throwable -> responseData.setValue(null)));
    }

    public void clearData(){
        Response mockResponse = new Response();
        mockResponse.setTotalItems(-1);
        responseData.setValue(mockResponse);
    }

    public MutableLiveData<Response> getBooks(){
        return responseData;
    }

    public MutableLiveData<List<Book>> getStoredBooks() {
        return storedBooks;
    }

    public void setStoredBooks() {
        disposable.add(dataSourceRepository.getAllBooks()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> storedBooks.setValue(books),
                        throwable -> storedBooks.setValue(null)));
    }

    @Override
    protected void onCleared() {
        disposable.clear();
    }
}
