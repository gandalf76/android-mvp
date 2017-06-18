package alex.com.mybooks.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import alex.com.mybooks.R;
import alex.com.mybooks.databinding.BookItemBinding;
import alex.com.mybooks.model.Book;
import alex.com.mybooks.view.BookBinder;

public class BookAdapter extends AbstractRecyclerViewScrollingAdapter<Book> {

    private OnBookAdapterListener bookAdapterListener;

    private BookItemBinding binding;

    public BookAdapter(RecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener, OnBookAdapterListener onBookAdapterListener) {
        super(recyclerView, new ArrayList<Book>(), onLoadMoreListener);
        this.bookAdapterListener = onBookAdapterListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        this.binding = BookItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(this.binding);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder viewHolder, int position) {
        final Book book =  super.getDataSet().get(position);
        ((ViewHolder)viewHolder).bindData(book);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookAdapterListener.onBookSelected(book.getId());
            }
        });
    }

    public void addBooks(List<Book> books, int totalPageCount) {
        super.addItems(books, totalPageCount);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private BookItemBinding bookItemBinding;

        public ViewHolder(BookItemBinding bookItemBinding) {
            super(bookItemBinding.getRoot());
            this.bookItemBinding = bookItemBinding;
        }

        public void bindData(Book book) {
            Glide.with(bookItemBinding.getRoot().getContext())
                    .load(book.getBookInfo().getImage() != null ? book.getBookInfo().getImage().getThumbnail() : null)
                    .into(bookItemBinding.ivBookCover);

            BookBinder bookBinder = new BookBinder();
            bookBinder.setTitle(book.getBookInfo().getTitle());

            String priceLabel;
            if (book.getSaleInfo().getListPrice() != null) {
                priceLabel = String.format("%s %s", book.getSaleInfo().getListPrice().getCurrencyCode(), book.getSaleInfo().getListPrice().getAmount());
            } else {
                priceLabel = bookItemBinding.getRoot().getContext().getString(R.string.not_for_sale);
            }

            bookBinder.setPrice(priceLabel);
            bookItemBinding.setBook(bookBinder);
        }
    }

    public interface OnBookAdapterListener {
        void onBookSelected(String id);
    }
}