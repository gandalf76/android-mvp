package alex.com.mybooks.view;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.Iterator;
import java.util.List;

import alex.com.mybooks.BR;

public class BookBinder extends BaseObservable {

    private String title;

    private String price;

    private String authors;

    private String description;

    @Bindable
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    @Bindable
    public String getAuthors() {
        return this.authors;
    }

    public void setAuthors(List<String> authors) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = authors.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        this.authors = sb.toString();
        notifyPropertyChanged(BR.authors);
    }

    @Bindable
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }
}
