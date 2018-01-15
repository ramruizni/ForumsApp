package com.example.rayku.firebasetutorialone;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public final class AdapterForums extends RecyclerView.Adapter<AdapterForums.ViewHolder> implements Filterable{

    private ArrayList<Forum> forums, filteredData;
    private StorageReference storageReference;

    AdapterForums(ArrayList<Forum> forums) {
        this.forums = forums;
        this.storageReference = FirebaseStorage.getInstance().getReference().child("forumImages");
        this.filteredData = forums;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ActivityInfo.class);
                TextView phantomView = view.findViewById(R.id.phantomCheatView);
                String theCheat = phantomView.getText().toString();
                intent.putExtra("forumID", theCheat);
                view.getContext().startActivity(intent);
            }
        });

        return ViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = filteredData.get(position).title;
        String description = filteredData.get(position).description;

        holder.setTitle(title);
        holder.setDescription(description);

        holder.setImage(filteredData.get(position).ID, storageReference); // maybe this is an issue. forumIDs? what about filteredData?
        holder.setPhantomView(filteredData.get(position).ID);
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView descView;
        private final ImageView imageView;
        private final TextView phantomCheatView;

        static ViewHolder newInstance(View itemView) {
            TextView titleView = itemView.findViewById(R.id.titleView);
            TextView descView = itemView.findViewById(R.id.descView);
            ImageView imageView = itemView.findViewById(R.id.imageView);
            TextView phantomCheatView = itemView.findViewById(R.id.phantomCheatView);
            return new ViewHolder(itemView, titleView, descView, imageView, phantomCheatView);
        }

        private ViewHolder(View itemView, TextView titleView, TextView descView, ImageView imageView, TextView phantomCheatView) {
            super(itemView);
            this.titleView = titleView;
            this.descView = descView;
            this.imageView = imageView;
            this.phantomCheatView = phantomCheatView;
        }

        void setTitle(String text) {
            titleView.setText(text);
        }
        void setDescription(String text){ descView.setText(text); }
        void setImage(String forumID, StorageReference storageReference){
            storageReference.child(forumID+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    try {
                        GlideApp.with(itemView.getRootView().getContext())
                                .load(uri)
                                .centerCrop()
                                .into(imageView);
                    } catch(Exception e) { e.printStackTrace(); }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    GlideApp.with(itemView.getRootView().getContext())
                            .load(R.drawable.the_forum_bg)
                            .centerCrop()
                            .into(imageView);
                }
            });
        }
        void setPhantomView(String forumID){ phantomCheatView.setText(forumID); }
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