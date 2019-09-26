package com.stingluc.booksearch.features.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.stingluc.booksearch.R;
import com.stingluc.booksearch.data.entities.Book;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder> {

    private List<Book> books;
    private Context context;

    final Transformation transformation = new Transformation() {
        @Override
        public Bitmap transform(Bitmap source) {

            int targetWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,120,
                    context.getResources().getDisplayMetrics());
            int targetHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,180,
                    context.getResources().getDisplayMetrics());

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

    @Inject
    public SearchAdapter(Context context){
        this.context = context;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_search,parent,false);
        return new SearchAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapterViewHolder holder, int position) {
        Book currentBook = books.get(position);
        holder.titleTextView.setText(currentBook.getTitle());
        holder.authorTextView.setText(currentBook.getAuthor());
        holder.publishedTextView.setText(currentBook.getPublished());
        holder.categoryTextView.setText(currentBook.getCategory());
        holder.languageTextView.setText(currentBook.getLanguage());

//        Timber.i("" + currentBook.getTitle() + currentBook.getAuthor() + currentBook.getPublished() + currentBook.getCategory() +
//                currentBook.getLanguage());

        if (TextUtils.isEmpty(currentBook.getImageURL())) {
            Picasso.with(holder.thumbnailImageView.getContext()).load(R.drawable.book_icon).transform(transformation).
                    into(holder.thumbnailImageView);
        } else {
            Picasso.with(holder.thumbnailImageView.getContext()).load(currentBook.getImageURL()).placeholder(R.drawable.book_icon).
                    transform(transformation).into(holder.thumbnailImageView);
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class SearchAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.thumbnail_image_view)
        ImageView thumbnailImageView;
        @BindView(R.id.title_text_view)
        TextView titleTextView;
        @BindView(R.id.author_text_view)
        TextView authorTextView;
        @BindView(R.id.published_text_view)
        TextView publishedTextView;
        @BindView(R.id.category_text_view)
        TextView categoryTextView;
        @BindView(R.id.language_text_view)
        TextView languageTextView;

        public SearchAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

}
