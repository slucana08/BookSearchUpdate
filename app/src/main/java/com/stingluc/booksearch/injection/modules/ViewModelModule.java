package com.stingluc.booksearch.injection.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.stingluc.booksearch.viewmodel.DataViewModel;
import com.stingluc.booksearch.viewmodel.StatusViewModel;
import com.stingluc.booksearch.viewmodel.ViewModelFactory;
import com.stingluc.booksearch.injection.annotations.ApplicationScope;
import com.stingluc.booksearch.injection.annotations.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @ApplicationScope
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
    //You are able to declare ViewModelProvider.Factory dependency in another module. For example in ApplicationModule.

    @Binds
    @IntoMap
    @ViewModelKey(DataViewModel.class)
    abstract ViewModel dataViewModel(DataViewModel dataViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(StatusViewModel.class)
    abstract ViewModel statusViewModel(StatusViewModel statusViewModel);
}
