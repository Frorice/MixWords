package com.note.frorice.mixwords;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by frorice on 2016/10/20.
 */

public class WordsBookAdapter extends RecyclerView.Adapter<WordsBookAdapter.WordsBookViewHolder>{

    private List<WordsBook> WordsBooks;
    private Context context;

    public WordsBookAdapter(List<WordsBook> WordsBooks,Context context) {
        this.WordsBooks = WordsBooks;
        this.context=context;
    }


    //自定义ViewHolder类
    static class WordsBookViewHolder extends RecyclerView.ViewHolder{
        CardView wordsBookCardView;
        TextView cardTitle;
        TextView cardAuthor;
        TextView cardRecordDate;

        public WordsBookViewHolder(final View itemView) {
            super(itemView);
            wordsBookCardView = (CardView) itemView.findViewById(R.id.words_book_card_view);
            cardTitle         = (TextView) itemView.findViewById(R.id.card_title);
            cardAuthor        = (TextView) itemView.findViewById(R.id.card_author);
            cardRecordDate    = (TextView) itemView.findViewById(R.id.card_recordDate);

        }


    }
    @Override
    public WordsBookAdapter.WordsBookViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.wordsbook_card_layout,viewGroup,false);
        WordsBookViewHolder nvh=new WordsBookViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(WordsBookAdapter.WordsBookViewHolder personViewHolder, int i) {
        personViewHolder.wordsBookCardView.setId(i);
        personViewHolder.cardTitle.setText(WordsBooks.get(i).getTitle());
        personViewHolder.cardAuthor.setText(WordsBooks.get(i).getAuthor());
        personViewHolder.cardRecordDate.setText(WordsBooks.get(i).getRecordDate());

        //为cardView设置点击事件
        personViewHolder.wordsBookCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,wordsBookActivity.class);
                int book = v.getId();
                Bundle bundle = new Bundle();
                bundle.putString("targetIntent", "wordsBook");
                bundle.putString("title", WordsBooks.get(book).getTitle());
                bundle.putString("bookId", WordsBooks.get(book).getId());
                bundle.putString("author", WordsBooks.get(book).getAuthor());
                bundle.putString("recordDate", WordsBooks.get(book).getRecordDate());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return WordsBooks.size();
    }
}
