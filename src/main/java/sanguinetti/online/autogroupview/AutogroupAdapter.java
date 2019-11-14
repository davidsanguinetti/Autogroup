package sanguinetti.online.autogroupview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.online.autogroupview.R;

public class AutogroupAdapter extends RecyclerView.Adapter<AutogroupAdapter.VHAutogroup> {

    IAutogroupItem[] items;
    ArrayList<AutogroupItem> arItems;
    IAutogroupListener listener;
    HashMap<String, ArrayList<IAutogroupItem>> hmitems;


    public AutogroupAdapter(IAutogroupItem[] items, IAutogroupListener list) {
        this.items = items;

        arItems = new ArrayList<>();

        listener = list;

        organizeItems(items);
    }

    private void organizeItems(IAutogroupItem[] showItems) {
        hmitems = new HashMap<>();
        arItems.clear();

        for (IAutogroupItem i : showItems) {
            String keyColumn = i.getDisplayName().substring(0,1).toUpperCase();

            Set<String> stt = hmitems.keySet();

            boolean existKey = false;

            for (String k : stt) {
                if (k.equalsIgnoreCase(keyColumn)) {
                    existKey = true;
                    keyColumn = k;
                    break;
                }
            }
            if (!existKey) {
                hmitems.put(keyColumn, new ArrayList<IAutogroupItem>());
            }

            for (IAutogroupItem loopItem : showItems) {
                Log.d("AUTOGROUPADAPTER ", "dispname: " + loopItem.getDisplayName());
                String keycoleval = loopItem.getDisplayName().substring(0, 1).toUpperCase();

                if (keyColumn.equalsIgnoreCase(keycoleval) &&
                        !hmitems.get(keyColumn).contains(loopItem)) {
                    hmitems.get(keyColumn).add(loopItem);
                }
            }

        }
        Log.d("Autogroup", hmitems.keySet().size() + "");
        ArrayList<String> keysorted = new ArrayList<>(hmitems.keySet());
        Collections.sort(keysorted);

        for (String k : keysorted) {
            arItems.add(new AutogroupItem(k));
            for (IAutogroupItem ia : hmitems.get(k)) {
                arItems.add(new AutogroupItem(ia));
            }
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VHAutogroup onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch(viewType) {
            case AutogroupItem.HEADER : {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);

                break;
            }
            case AutogroupItem.REGULARITEM: {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_item, parent, false);
                break;
            }
        }
        return new VHAutogroup(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VHAutogroup holder, int position) {
        holder.getTvName().setText(arItems.get(position).getText());
        if (holder.getTvSubtitle() != null)
            holder.getTvSubtitle().setText(arItems.get(position).getSubtext());
        holder.setItem(arItems.get(position).getShownItem());
        holder.setListener(listener);
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        return arItems.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return arItems.size();
    }

    public static class VHAutogroup extends RecyclerView.ViewHolder {

        TextView tvName,
                tvSubtitle;
        IAutogroupItem item;
        IAutogroupListener listener;

        public VHAutogroup(@NonNull View itemView) {
            super(itemView);

            tvName      = itemView.findViewById(R.id.ag_commonitem);
            tvSubtitle  = itemView.findViewById(R.id.ag_subitem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item != null)
                        listener.selectedItem(item);
                }
            });
        }

        public TextView getTvName() {
            return tvName;
        }

        public IAutogroupItem getItem() {
            return item;
        }

        public void setItem(IAutogroupItem item) {
            this.item = item;
        }

        public IAutogroupListener getListener() {
            return listener;
        }

        public TextView getTvSubtitle() {
            return tvSubtitle;
        }

        public void setListener(IAutogroupListener listener) {
            this.listener = listener;
        }
    }

    public void filterby(String st) {
        ArrayList<IAutogroupItem> arshow = new ArrayList<>();

        for (IAutogroupItem i : items) {
            if (i.getDisplayName().toUpperCase().startsWith(st.toUpperCase())) {
                arshow.add(i);
            }
        }

        IAutogroupItem[] ari = new IAutogroupItem[arshow.size()];
        arshow.toArray(ari);
        organizeItems(ari);
    }

    public HashMap<String, ArrayList<IAutogroupItem>> getHmitems() {
        return hmitems;
    }
}
