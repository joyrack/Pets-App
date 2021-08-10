package com.example.android.pets;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.pets.data.PetContract;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private Cursor mPetData;
    private View mEmptyView;

    public RecyclerViewAdapter(Context context, Cursor data, View emptyView){
        mContext = context;
        mPetData = data;
        mEmptyView = emptyView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mPetData.moveToPosition(position);

        String mPetName = mPetData.getString(mPetData.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME));
        String mPetBreed = mPetData.getString(mPetData.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED));
        int id = mPetData.getInt(mPetData.getColumnIndex(PetContract.PetEntry._ID));

        holder.petName.setText(mPetName);

        if(TextUtils.isEmpty(mPetBreed)){
            mPetBreed = mContext.getString(R.string.unknown_breed);
        }
        holder.petBreed.setText(mPetBreed);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(mContext, EditorActivity.class);

                // Form the content URI that represents the specific pet that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link PetEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.pets/pets/2"
                // if the pet with ID 2 was clicked on.
                Uri currentPetUri = ContentUris.withAppendedId(PetContract.PetEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentPetUri);

                // Launch the {@link EditorActivity} to display the data for the current pet.
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // we set the emptyView in here.
        // We first check whether pet data exists and whether it's count is greater than zero.
        // If so, then we set the visibility of emptyView to be true, otherwise we set it to false
        mEmptyView.setVisibility(mPetData != null && mPetData.getCount() > 0 ? View.GONE : View.VISIBLE);
        return (mPetData == null) ? 0 : mPetData.getCount();
    }

    public Cursor swapCursor(Cursor cursor) {
        if (mPetData == cursor) {
            return null;
        }
        Cursor oldCursor = mPetData;
        mPetData = cursor;
        if (cursor != null) {
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView petName;
        private TextView petBreed;
        private LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            petName = itemView.findViewById(R.id.name);
            petBreed = itemView.findViewById(R.id.breed);
            parentLayout = itemView.findViewById(R.id.list_item_parent_layout);
        }
    }
}
