package com.example.rayku.firebasetutorialone;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public final class AdapterForums extends RecyclerView.Adapter<AdapterForums.ViewHolder> implements Filterable{

    private ArrayList<Forum> forums, filteredData;
    private ArrayList<String> forumIDs;
    private StorageReference storageReference;

    AdapterForums(ArrayList<Forum> forums, ArrayList<String> forumIDs) {
        this.forums = forums;
        this.forumIDs = forumIDs;
        this.storageReference = FirebaseStorage.getInstance().getReference().child("forumImages");
        this.filteredData = forums;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum, parent, false);
        return ViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = filteredData.get(position).title;
        String description = filteredData.get(position).description;
        holder.setTitle(title);
        holder.setDescription(description);
        holder.setImage(forumIDs.get(position), storageReference);
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView descView;
        private final ImageView imageView;

        static ViewHolder newInstance(View itemView) {
            TextView titleView = itemView.findViewById(R.id.titleView);
            TextView descView = itemView.findViewById(R.id.descView);
            ImageView imageView = itemView.findViewById(R.id.imageView);
            return new ViewHolder(itemView, titleView, descView, imageView);
        }

        private ViewHolder(View itemView, TextView titleView, TextView descView, ImageView imageView) {
            super(itemView);
            this.titleView = titleView;
            this.descView = descView;
            this.imageView = imageView;
        }

        void setTitle(String text) {
            titleView.setText(text);
        }
        void setDescription(String text){ descView.setText(text); }
        void setImage(String forumID, StorageReference storageReference){

            //String imageKey = forumID+".jpg";

            String imageKey = "-L2auVWUkz58XFqh5OsZ.jpg";

            storageReference.child(imageKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    try {
                        GlideApp
                                .with(itemView.getRootView().getContext())
                                .load(uri)
                                .centerCrop()
                                .into(imageView);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }

    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                FilterResults filterResults = new FilterResults();

                if(charSequence==null || charSequence.length()==0){
                    filterResults.values = forums;
                    filterResults.count = forums.size();
                } else{
                    ArrayList<Forum> filterResultsData = new ArrayList<>();
                    for(Forum forum : forums){
                        if(forum.title.toLowerCase().contains(charSequence.toString().toLowerCase())){
                            filterResultsData.add(forum);
                        }
                    }
                    filterResults.values = filterResultsData;
                    filterResults.count = filterResultsData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredData = (ArrayList<Forum>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}