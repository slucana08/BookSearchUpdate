package com.stingluc.booksearch.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class StatusViewModel extends ViewModel {

    private Context context;

    // 0 - app load // 1 - loading // 2 - loaded
    private int status = 0;
    private int type;
    private int pageIndex = 1;
    private int presenterID = 0;

    private boolean searching = false;
    private boolean settingsShowing = false;
    private boolean backOnline = false;

    @Inject
    public StatusViewModel() {}

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSearching() {
        return searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
    }

    @Override
    protected void onCleared() {
        status = 0;
        searching = false;
        super.onCleared();
    }

    public boolean isSettingsShowing() {
        return settingsShowing;
    }

    public void setSettingsShowing(boolean settingsShowing) {
        this.settingsShowing = settingsShowing;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public boolean isBackOnline() {
        return backOnline;
    }

    public void setBackOnline(boolean backOnline) {
        this.backOnline = backOnline;
    }

    public int getPresenterID() {
        return presenterID;
    }

    public void setPresenterID(int presenterID) {
        this.presenterID = presenterID;
    }
}
