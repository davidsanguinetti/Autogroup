package sanguinetti.online.autogroupview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ferfalk.simplesearchview.SimpleOnQueryTextListener;
import com.ferfalk.simplesearchview.SimpleSearchView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.online.autogroupview.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AutogroupFragment extends BottomSheetDialogFragment implements IAutogroupListener {
    SimpleSearchView searchView;
    RecyclerView rv;
    TextView tvfiltering;

    AutogroupAdapter aaf;

    String filteringBy;
    ArrayList<IAutogroupItem> items;
    IAutogroupListener listener;

    public AutogroupFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        setStyle(DialogFragment.STYLE_NORMAL, R.style.AutogroupDialog);
    }

    /*
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }
    */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_autogroup, container, false);

        searchView  = v.findViewById(R.id.searchView);
        rv          = v.findViewById(R.id.autogroup_rv);
        tvfiltering = v.findViewById(R.id.tv_filteringby);

        ((ImageButton) v.findViewById(R.id.autogroup_ib_search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.showSearch(true);
            }
        });

/*        searchView.enableVoiceSearch(true);
        searchView.showVoice(true);*/

        searchView.setOnQueryTextListener(new SimpleOnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewInserted(query);
                return true;
            }

            @Override
            public boolean onQueryTextCleared() {
                filteringBy = "";
                searchViewInserted(filteringBy);
                return super.onQueryTextCleared();
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteringBy = newText;
                searchViewInserted(filteringBy);

                return true;
            }
        });
        searchView.setOnSearchViewListener(new SimpleSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchView.setQuery(filteringBy, false);
            }

            @Override
            public void onSearchViewClosed() {
                searchViewInserted(filteringBy);
            }

            @Override
            public void onSearchViewShownAnimation() {

            }

            @Override
            public void onSearchViewClosedAnimation() {

            }
        });


        IAutogroupItem[] arit = new IAutogroupItem[items.size()];
        items.toArray(arit);
        aaf = new AutogroupAdapter(arit, this);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(aaf);

        return v;
    }

    private void searchViewInserted(String newText) {
        filteringBy = newText;
        if (filteringBy.isEmpty())
            tvfiltering.setText("");
        else
            tvfiltering.setText("Filtering by '" + filteringBy+ "'");

        aaf.filterby(newText);
    }

    public ArrayList<IAutogroupItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<IAutogroupItem> items) {
        this.items = items;
    }

    public IAutogroupListener getListener() {
        return listener;
    }

    public void setListener(IAutogroupListener listener) {
        this.listener = listener;
    }

    @Override
    public void selectedItem(IAutogroupItem item) {
        listener.selectedItem(item);
        this.dismiss();
    }
}
