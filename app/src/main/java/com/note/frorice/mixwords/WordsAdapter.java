package com.note.frorice.mixwords;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by frorice on 2016/10/20.
 */

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordsViewHolder>{

    private List<words> Words;
    private Context context;

    public WordsAdapter(List<words> Words,Context context) {
        this.Words = Words;
        this.context=context;
    }


    //自定义ViewHolder类
    static class WordsViewHolder extends RecyclerView.ViewHolder{
        CardView WordCardView;
        TextView cardName;
        ImageButton cardStar;

        public WordsViewHolder(final View itemView) {
            super(itemView);
            WordCardView = (CardView) itemView.findViewById(R.id.word_book_card_view);
            cardName      = (TextView) itemView.findViewById(R.id.card_name);
            cardStar      = (ImageButton) itemView.findViewById(R.id.card_star);
        }


    }
    @Override
    public WordsAdapter.WordsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.word_card_layout,viewGroup,false);
        WordsViewHolder nvh=new WordsViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(WordsAdapter.WordsViewHolder personViewHolder, int i) {
        personViewHolder.WordCardView.setId(i);
        personViewHolder.cardName.setText(Words.get(i).getName());

        //为cardView设置点击事件
        personViewHolder.WordCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int word = v.getId();
                v.setMinimumHeight(200);
                Words.get(word).getInterpretation();

            }
        });

    }

    @Override
    public int getItemCount() {
        return Words.size();
    }
}
