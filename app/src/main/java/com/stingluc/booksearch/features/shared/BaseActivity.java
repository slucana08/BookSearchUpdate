package com.stingluc.booksearch.features.shared;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.stingluc.booksearch.App;
import com.stingluc.booksearch.injection.AppComponent;
import com.stingluc.booksearch.utils.internetconnection.InternetReceiver;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    private AppComponent appComponent;

    private InternetReceiver internetReceiver = new InternetReceiver();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        ButterKnife.bind(this);

        setAppComponent(App.getInstance().getAppComponent());

        internetReceiver.setConnectivity(this::connectivityChange);
    }

    @Override
    protected void onResume() {
        registerReceiver(internetReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }

    public abstract int getLayoutId();

    @Override
    protected void onPause() {
        unregisterReceiver(internetReceiver);
        super.onPause();
    }

    public abstract void connectivityChange(boolean isConnected);

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public void setAppComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }
}
