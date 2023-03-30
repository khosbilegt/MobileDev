package com.example.recyclerview;

import static com.example.recyclerview.MainActivity.EXTRA_MESSAGE;
import static com.example.recyclerview.MainActivity.INDEX_MESSAGE;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder>  {
    private LayoutInflater mInflater;
    private final LinkedList<String> mWordList;
    private final LinkedList<String> mDescList;
    public WordListAdapter(Context context,
                           LinkedList<String> wordList, LinkedList<String> descList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList = wordList;
        this.mDescList = descList;

    }



    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView wordItemView;
        public final TextView descriptionItemView;
        final WordListAdapter mAdapter;
        final Context context;

        public WordViewHolder(View itemView, WordListAdapter adapter, Context context) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.word);
            descriptionItemView = itemView.findViewById(R.id.description);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            int mPosition = getLayoutPosition();
            mAdapter.notifyDataSetChanged();
            Intent intent = new Intent(context, RecipeActivity.class);
            intent.putExtra(INDEX_MESSAGE, mPosition);
            context.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public WordListAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.wordlist_item,
                parent, false);
        return new WordViewHolder(mItemView, this, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull WordListAdapter.WordViewHolder holder, int position) {
        String mCurrent = mWordList.get(position);
        String mCurrentDesc = mDescList.get(position);
        holder.wordItemView.setText(mCurrent);
        holder.descriptionItemView.setText(mCurrentDesc);
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }
}
