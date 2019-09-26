package com.stingluc.booksearch.features.search;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.stingluc.booksearch.R;
import com.stingluc.booksearch.data.entities.Book;
import com.stingluc.booksearch.features.shared.BaseActivity;
import com.stingluc.booksearch.utils.SimpleDividerItemDecoration;
import com.stingluc.booksearch.utils.UtilMethods;
import com.stingluc.booksearch.viewmodel.DataViewModel;
import com.stingluc.booksearch.viewmodel.StatusViewModel;
import com.stingluc.booksearch.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class SearchActivity extends BaseActivity implements SearchContract.View, View.OnClickListener, TextView.OnEditorActionListener {

    @BindView(R.id.setting_scroll_view)
    ScrollView settingScrollView;
    @BindView(R.id.title_check_box)
    AppCompatCheckBox titleCheckBox;
    @BindView(R.id.author_check_box)
    AppCompatCheckBox authorCheckBox;
    @BindView(R.id.author_edit_text)
    EditText authorEditText;
    @BindView(R.id.books_radio_button)
    AppCompatRadioButton booksRadioButton;
    @BindView(R.id.magazine_radio_button)
    AppCompatRadioButton magazineRadioButton;
    @BindView(R.id.all_radio_button)
    AppCompatRadioButton allRadioButton;
    @BindView(R.id.results_count_spinner)
    AppCompatSpinner resultsCountSpinner;
    @BindView(R.id.empty_image_view)
    AppCompatImageView emptyImageView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.books_recycler_view)
    RecyclerView booksRecyclerView;
    @BindView(R.id.pages_linear_layout)
    LinearLayout pagesLinearLayout;
    @BindView(R.id.previous_button)
    MaterialButton previousButton;
    @BindView(R.id.page_number_text_view)
    TextView pageNumberTextView;
    @BindView(R.id.next_button)
    MaterialButton nextButton;
    @BindView(R.id.internet_text_view)
    TextView internetTextView;
    @BindView(R.id.retry_button)
    MaterialButton retryButton;

    private EditText bookSearchEditText;

    private String queryText,authorText;
    private boolean inTitle,inAuthor, isConnected, firstLoad;

    private DataViewModel dataViewModel;
    private StatusViewModel statusViewModel;

    @Inject
    SearchPresenter presenter;

    @Inject
    SearchAdapter adapter;

    @Inject
    ViewModelFactory viewModelFactory;

    // Set of colors for CheckBoxes and RadioButtons
    ColorStateList colorStateList = new ColorStateList(
            new int[][]{
                    new int[]{-android.R.attr.state_checked},
                    new int[]{android.R.attr.state_checked}
            },
            new int[]{
                    Color.rgb(255, 255, 255),
                    Color.rgb(0, 0, 0)
            }
    );

    // Transformation object that allows picasso to resize image according to size of container
    // view
    final Transformation transformation = new Transformation() {
        @Override
        public Bitmap transform(Bitmap source) {

            int targetWidth = emptyImageView.getWidth();
            int targetHeight = emptyImageView.getHeight();

            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight,
                    false);
            if (result != source) {
                // Same bitmap is returned if sizes are the same
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "transformation";
        }
    };

    private Handler handler = new Handler();
    private Runnable internetRunnable = new Runnable() {
        @Override
        public void run() {
            internetTextView.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAppComponent().inject(this);
        dataViewModel = ViewModelProviders.of(this,viewModelFactory).get(DataViewModel.class);
        statusViewModel = ViewModelProviders.of(this,viewModelFactory).get(StatusViewModel.class);
        presenter.attachView(this);
        presenter.setDataViewModel(dataViewModel);
        presenter.setStatusViewModel(statusViewModel);
        presenter.setUpViews();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void connectivityChange(boolean isConnected) {
        this.isConnected = isConnected;
        if (isConnected) {
            presenter.stopObservingBD();
            if (presenter.isBackOnline()) {
                internetTextView.setText(getString(R.string.back_online));
                internetTextView.setVisibility(View.VISIBLE);
                internetTextView.setBackgroundColor(ContextCompat.getColor(SearchActivity.this, R.color.green));
                handler.postDelayed(internetRunnable, 3000);
                if (presenter.isSearching()) {
                    checkQueries(presenter.getType());
                    presenter.setSearching(false);
                }
                presenter.setBackOnline(false);
            }
        } else {
            presenter.setBackOnline(true);
            handler.removeCallbacks(internetRunnable);
            internetTextView.setText(getString(R.string.disconnected));
            internetTextView.setVisibility(View.VISIBLE);
            internetTextView.setBackgroundColor(ContextCompat.getColor(SearchActivity.this, R.color.red));
        }

        if (firstLoad) {
            emptyImageView.setVisibility(View.VISIBLE);
            settingScrollView.setVisibility(View.GONE);
            settingScrollView.setVisibility(presenter.isSettingsShowing() ? View.VISIBLE : View.GONE);
        } else {
            switch (presenter.getStatus()) {
                case 0:
                    handler.postDelayed(() -> SearchActivity.this.checkQueries(3), 200);
                    settingScrollView.setVisibility(View.VISIBLE);
                    presenter.setSettingsShowing(true);
                    break;
                case 1:
                    emptyImageView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    settingScrollView.setVisibility(presenter.isSettingsShowing() ? View.VISIBLE : View.GONE);
                    break;
                case 2:
                    emptyImageView.setVisibility(emptyImageView.getVisibility() == View.VISIBLE ? View.VISIBLE : View.GONE);
                    settingScrollView.setVisibility(presenter.isSettingsShowing() ? View.VISIBLE : View.GONE);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setColorStateList(){
        titleCheckBox.setSupportButtonTintList(colorStateList);
        authorCheckBox.setSupportButtonTintList(colorStateList);
        booksRadioButton.setSupportButtonTintList(colorStateList);
        magazineRadioButton.setSupportButtonTintList(colorStateList);
        allRadioButton.setSupportButtonTintList(colorStateList);
    }

    @Override
    public void setUpActionBar() {
        // Getting action bar to set custom view, allows user to search for a book with the given
        // input
        ActionBar actionBar = getSupportActionBar();
        View customView = getLayoutInflater().inflate(R.layout.search_action_bar, null);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(customView);

        // Creating a toolbar to eliminate unnecessary spacing on the sides of customView
        Toolbar parent = (Toolbar) customView.getParent();
        parent.setPadding(0, 0, 0, 0);
        parent.setContentInsetsAbsolute(0, 0);

        ImageView settingsImageView = actionBar.getCustomView().findViewById(R.id.settings_image_view);
        settingsImageView.setOnClickListener(view -> {
            settingScrollView.setVisibility(settingScrollView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            presenter.setSettingsShowing(settingScrollView.getVisibility() == View.VISIBLE);
        });

        bookSearchEditText = actionBar.getCustomView().findViewById(R.id.book_search_edit_text);
        bookSearchEditText.setOnEditorActionListener(this);

        ImageView searchImageView = actionBar.getCustomView().findViewById(R.id.search_image_view);
        searchImageView.setOnClickListener(view -> {
            UtilMethods.hideKeyboard(SearchActivity.this);
            SearchActivity.this.checkQueries(0);
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        shouldShowAuthorText();
    }

    @Override
    public void setUpViews(boolean inTitle, boolean inAuthor, String queryText, String authorText,
                           int printType, int resultsPage, boolean firstLoad) {
        bookSearchEditText.setText(queryText);
        authorEditText.setText(authorText);
        authorEditText.setOnEditorActionListener(this);
        titleCheckBox.setChecked(inTitle);
        authorCheckBox.setChecked(inAuthor);
        shouldShowAuthorText();
        this.firstLoad = firstLoad;

        switch (printType){
            case 0:
                booksRadioButton.setChecked(true);
                break;
            case 1:
                magazineRadioButton.setChecked(true);
                break;
            case 2:
                allRadioButton.setChecked(true);
                break;
        }

        List<Integer> values = new ArrayList<>();
        values.add(10);
        values.add(20);
        values.add(40);

        ArrayAdapter<Integer> adapterSpinner = new ArrayAdapter<Integer>(SearchActivity.this,
                R.layout.spinner_text,values);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        resultsCountSpinner.setAdapter(adapterSpinner);
        resultsCountSpinner.setSelection(resultsPage);

        pagesLinearLayout.setVisibility(View.GONE);
        Picasso.with(SearchActivity.this).load(R.drawable.search_book).into(emptyImageView);
        booksRecyclerView.setVisibility(View.GONE);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        booksRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(SearchActivity.this,R.drawable.line_black_divider));
        booksRecyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);

        presenter.observeBD();
        presenter.getBooks();

        titleCheckBox.setOnClickListener(this);
        authorCheckBox.setOnClickListener(this);
        booksRadioButton.setOnClickListener(this);
        magazineRadioButton.setOnClickListener(this);
        allRadioButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        retryButton.setOnClickListener(this);
    }

    @Override
    public void shouldShowAuthorText() {
        if (titleCheckBox.isChecked() && authorCheckBox.isChecked()){
            authorEditText.setVisibility(View.VISIBLE);
        } else {
            authorEditText.setVisibility(View.GONE);
            authorEditText.getText().clear();
        }
    }

    @Override
    public void checkQueries(int type) {
        if (presenter.getID() != presenter.getStatusPresenterID()) return;
        if (presenter.getStatus() == 1) return;
        bookSearchEditText.clearFocus();
        authorEditText.clearFocus();
        emptyImageView.setVisibility(View.GONE);
        if (!isConnected){
            UtilMethods.showToast(SearchActivity.this,getString(R.string.cant_search_offline));
            presenter.setSearching(true);
            presenter.setType(type);
            if (type == 3) {
                presenter.setSearching(false);
                presenter.getBooksBD();
            }
            return;
        }

        queryText = bookSearchEditText.getText().toString().trim();
        authorText = authorEditText.getText().toString().trim();
        inTitle = titleCheckBox.isChecked();
        inAuthor = authorCheckBox.isChecked();

        switch (presenter.verifyQueries(queryText,authorText,inTitle,inAuthor)){
            case 0:
                UtilMethods.showToast(SearchActivity.this,getString(R.string.text_search));
                return;
            case 1:
                UtilMethods.showToast(SearchActivity.this, getString(R.string.author_search));
                return;
        }

        presenter.setQueryText(queryText);
        presenter.setInTitle(titleCheckBox.isChecked());
        presenter.setInAuthor(authorCheckBox.isChecked());
        presenter.setAuthorText(authorText);
        if (booksRadioButton.isChecked()) presenter.setPrintType(0);
        if (magazineRadioButton.isChecked()) presenter.setPrintType(1);
        if (allRadioButton.isChecked()) presenter.setPrintType(2);
        presenter.setResultsPage(resultsCountSpinner.getSelectedItemPosition());

        presenter.setSearchType(type);
    }

    @Override
    public void performSearch(int type) {
        if (presenter.verifyQueries(queryText, authorText,inTitle,inAuthor) == 2 &&  presenter.buildParams(type)) {
            retryButton.setVisibility(View.GONE);
            booksRecyclerView.setVisibility(View.GONE);
            emptyImageView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            pagesLinearLayout.setVisibility(View.GONE);
            presenter.setStatus(1);
            presenter.setBooks();
        }
    }

    @Override
    public void showBooks(List<Book> books, int pageIndex) {
        progressBar.setVisibility(View.GONE);
        if (books.isEmpty()){
            Picasso.with(SearchActivity.this).load(R.drawable.no_results).transform(transformation).into(emptyImageView);
            emptyImageView.setVisibility(View.VISIBLE);
            pagesLinearLayout.setVisibility(View.GONE);
            if (pageIndex != 1) {
                pagesLinearLayout.setVisibility(View.VISIBLE);
                pageNumberTextView.setText("Page " + pageIndex);
                nextButton.setEnabled(false);
            }
        } else {
            adapter.setBooks(books);
            if (presenter.getStatus() == 1) booksRecyclerView.smoothScrollToPosition(0);
            booksRecyclerView.setVisibility(View.VISIBLE);
            pagesLinearLayout.setVisibility(View.VISIBLE);
            pageNumberTextView.setText("Page " + pageIndex);
            nextButton.setEnabled(true);
        }
        presenter.setStatus(2);
    }

    @Override
    public void showError() {
        progressBar.setVisibility(View.GONE);
        Picasso.with(SearchActivity.this).load(R.drawable.no_internet_connection).transform(transformation).into(emptyImageView);
        emptyImageView.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.VISIBLE);
        pagesLinearLayout.setVisibility(View.GONE);
        presenter.setStatus(2);
    }

    @Override
    public void onClick(View view) {
        if (view == titleCheckBox){
            inTitle = titleCheckBox.isChecked();
            shouldShowAuthorText();
        } else if (view == authorCheckBox){
            inAuthor = authorCheckBox.isChecked();
            shouldShowAuthorText();
        } else if (view == previousButton){
            checkQueries(2);
        } else if (view == nextButton){
            checkQueries(1);
        } else if (view == retryButton){
            checkQueries(presenter.getType() == 0 ? 3 : presenter.getType());
        }
    }

    @Override
    protected void onStop() {
        queryText = bookSearchEditText.getText().toString().trim();
        authorText = authorEditText.getText().toString().trim();
        presenter.setQueryText(queryText);
        presenter.setAuthorText(authorText);
        super.onStop();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO ||
                event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            UtilMethods.hideKeyboard(SearchActivity.this);
            checkQueries(0);
            return true;
        }
        return false;
    }
}
