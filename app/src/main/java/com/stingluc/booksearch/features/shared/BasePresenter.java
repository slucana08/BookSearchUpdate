package com.stingluc.booksearch.features.shared;

public interface BasePresenter<T extends BaseContractView> {

    void attachView(T view);

    void detachView();

    T getView();
}
