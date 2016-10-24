package com.note.frorice.mixwords;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.note.frorice.mixwords.wordsBookActivity.wbActivity;

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
        TextView cardUK;
        TextView cardUSA;
        TextView cardMean;

        public WordsViewHolder(final View itemView) {
            super(itemView);
            WordCardView = (CardView) itemView.findViewById(R.id.word_book_card_view);
            cardName     = (TextView) itemView.findViewById(R.id.card_name);
            cardStar     = (ImageButton) itemView.findViewById(R.id.card_star);
            cardUK       = (TextView) itemView.findViewById(R.id.card_pronun_UK);
            cardUSA      = (TextView) itemView.findViewById(R.id.card_pronun_USA);
            cardMean     = (TextView) itemView.findViewById(R.id.card_mean);
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
        if(Words.get(i).getIsStar()==1){

            personViewHolder.cardStar.setColorFilter(R.color.colorAccent);
            personViewHolder.cardStar.setImageResource(R.drawable.ic_grade_black_24dp);
        }
        try {
            JSONObject interpretation = new JSONObject(Words.get(i).getInterpretation());
            if(interpretation.has("uk-phonetic")){
                String pronunUk = interpretation.getString("uk-phonetic");
                if(pronunUk.length()>=15){
                    personViewHolder.cardUK.setText("英 【"+pronunUk+"】");
                }else{
                    personViewHolder.cardUK.setText("英 【"+pronunUk+"】\n");
                }

            }
            if(interpretation.has("us-phonetic")){
                String pronunUs = interpretation.getString("us-phonetic");
                personViewHolder.cardUSA.setText("美 【"+pronunUs+"】");
            }

            String explainStr = interpretation.getString("explains");
            String[] explains = explainStr.substring(1,explainStr.length()-1).split(",");
            explainStr = "";
            for(int j=0;j<explains.length;j++){
                explainStr += explains[j]+"\n";
            }
            personViewHolder.cardMean.setText(explainStr);
        }catch (JSONException exp){
            Toast.makeText(wbActivity,exp.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }


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
