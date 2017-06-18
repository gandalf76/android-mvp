package alex.com.mybooks.adapters;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import alex.com.mybooks.R;

public abstract class AbstractRecyclerViewScrollingAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //The minimum number of items to have below the current scroll position
    // before loading more data.
    private final int VISIBLE_THRESHOLD = 10;

    //view holder type for the standard item
    private final int ITEM_VIEW_TYPE_BASIC = 0;

    //view holder for the progressbar
    private final int ITEM_VIEW_TYPE_FOOTER = 1;

    private List<T> dataSet;

    private int firstVisibleItem;

    private int visibleItemCount;

    private int totalItemCount;

    private int previousTotal = 0;

    private int totalPageCount;

    private int visibleThreshold = VISIBLE_THRESHOLD;

    private boolean loading = true;

    private int currentPage = 0;

    public AbstractRecyclerViewScrollingAdapter(RecyclerView recyclerView, List<T> dataSet, final OnLoadMoreListener onLoadMoreListener) {
        this.dataSet = dataSet;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    totalItemCount = linearLayoutManager.getItemCount();
                    visibleItemCount = linearLayoutManager.getChildCount();
                    firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                    //when loading, let's check that total item count has changed; in that case we assume it's finished loading
                    if (loading) {
                        if (totalItemCount > previousTotal + 1) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }

                    //if loading has terminated and there is no other page, remove progress bar and notify loading is finished. No more paging.
                    if (!loading && totalPageCount -1 == currentPage) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadFinished();
                        }
                        return;
                    }

                    //if it's not currently loading data, let's check if it's reached the minimum threshold needed for loading more data
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {

                        currentPage++;
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore(currentPage);
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    public int getFirstVisibleItem() {
        return firstVisibleItem;
    }

    public void resetItems(List<T> newDataSet) {
        clearItems();
        addItems(newDataSet, 0);
    }

    public void clearItems() {
        loading = true;
        firstVisibleItem = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
        previousTotal = 0;

        dataSet.clear();
    }

    public void addItems(List<T> newDataSetItems, int totalPageCount) {
        this.totalPageCount = totalPageCount;
        dataSet.addAll(newDataSetItems);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        if (!dataSet.contains(item)) {
            dataSet.add(item);
            notifyItemInserted(dataSet.size() - 1);
        }
    }

    public void removeItem(T item) {
        int indexOfItem = dataSet.indexOf(item);
        if (indexOfItem != -1) {
            this.dataSet.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
        }
    }

    public T getItem(int index) {
        if (dataSet != null && dataSet.get(index) != null) {
            return dataSet.get(index);
        } else {
            throw new IllegalArgumentException("Item with index " + index + " doesn't exist, dataSet is " + dataSet);
        }
    }

    public List<T> getDataSet() {
        return dataSet;
    }

    public void setVisibleThreshold(int threshold) {
        this.visibleThreshold = threshold;
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position) != null ? ITEM_VIEW_TYPE_BASIC : ITEM_VIEW_TYPE_FOOTER;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_BASIC) {
            return onCreateBasicItemViewHolder(parent, viewType);
        } else if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            return onCreateFooterViewHolder(parent, viewType);
        } else {
            throw new IllegalStateException("Invalid type, this type ot items " + viewType + " can't be handled");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder genericHolder, int position) {
        if (getItemViewType(position) == ITEM_VIEW_TYPE_BASIC) {
            onBindBasicItemView(genericHolder, position);
        } else {
            onBindFooterView(genericHolder, position);
        }
    }

    public abstract RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindBasicItemView(RecyclerView.ViewHolder genericHolder, int position);

    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        //noinspection ConstantConditions
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_bar, parent, false);
        return new ProgressViewHolder(v);
    }

    public void onBindFooterView(RecyclerView.ViewHolder genericHolder, int position) {
        ((ProgressViewHolder) genericHolder).progressBar.setIndeterminate(true);
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            this.progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int currentPage);

        void onLoadFinished();
    }

}