package com.note.frorice.mixwords;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.note.frorice.mixwords.R.id.card_checkbox;
import static com.note.frorice.mixwords.wordsBookActivity.wbActivity;

/**
 * Created by frorice on 2016/10/20.
 */

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordsViewHolder>{

    private List<words> Words;
    private Context context;

    public WordsAdapter(List<words> Words,Context context) {
        this.Words   = Words;
        this.context = context;
    }


    //自定义ViewHolder类
    static class WordsViewHolder extends RecyclerView.ViewHolder{
        CardView WordCardView;
        TextView cardName;
        ImageButton cardStar;
        TextView cardUK;
        TextView cardUSA;
        TextView cardMean;
        AppCompatCheckBox cardCheckBox;

        public WordsViewHolder(final View itemView) {
            super(itemView);
            WordCardView = (CardView) itemView.findViewById(R.id.word_book_card_view);
            cardName     = (TextView) itemView.findViewById(R.id.card_name);
            cardStar     = (ImageButton) itemView.findViewById(R.id.card_star);
            cardUK       = (TextView) itemView.findViewById(R.id.card_pronun_UK);
            cardUSA      = (TextView) itemView.findViewById(R.id.card_pronun_USA);
            cardMean     = (TextView) itemView.findViewById(R.id.card_mean);
            cardCheckBox = (AppCompatCheckBox) itemView.findViewById(card_checkbox);
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
            personViewHolder.cardStar.setId('1');
            personViewHolder.cardStar.setImageResource(R.drawable.ic_grade_black_24dp);
        }else{
            personViewHolder.cardStar.setId('0');
        }
        if(Words.get(i).getIsDone()==1){
            personViewHolder.cardCheckBox.setChecked(true);
            personViewHolder.cardName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        try {
            JSONObject interpretation = new JSONObject(Words.get(i).getInterpretation());
            if(interpretation.has("uk-phonetic")){
                String pronunUk = interpretation.getString("uk-phonetic");
                personViewHolder.cardUK.setText("英 【"+pronunUk+"】");
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
                TransitionManager.beginDelayedTransition((RecyclerView) v.getParent().getParent(), new Fade());
                ViewGroup.LayoutParams params =  v.getLayoutParams();
                if(params.height == MarginLayoutParams.WRAP_CONTENT){
                    params.height = 80;
                    v.findViewById(R.id.card_checkbox).setVisibility(View.INVISIBLE);
                }else{
                    params.height = MarginLayoutParams.WRAP_CONTENT;
                    v.findViewById(R.id.card_checkbox).setVisibility(View.VISIBLE);
                }

                v.setLayoutParams(params);

            }
        });
        //收起单词卡片
        ViewGroup.LayoutParams params =  personViewHolder.WordCardView.getLayoutParams();
        params.height = 80;
        personViewHolder.WordCardView.setLayoutParams(params);

        //为star设置点击事件
        personViewHolder.cardStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardView card = (CardView) v.getParent().getParent().getParent();
                TextView cardName =  (TextView) card.findViewById(R.id.card_name);
                ImageButton cardStar = (ImageButton) v;
                if(v.getId() != '1'){
                    v.setId('1');
                    cardStar.setImageResource(R.drawable.ic_grade_black_24dp);
                    wbActivity.model.toggleStar(cardName.getText().toString(),"1");
                }else{
                    v.setId('0');
                    cardStar.setImageResource(R.drawable.ic_star_border_black_24dp);
                    wbActivity.model.toggleStar(cardName.getText().toString(),"0");
                    //星标单词界面点击星星后应删除卡片
                    if(wbActivity.actName.equals("星标单词")){
                        TransitionManager.beginDelayedTransition((RecyclerView)card.getParent().getParent(),new Fade());
                        card.setVisibility(View.GONE);
                    }
                }



            }
        });
        //为checkbox设置事件
        personViewHolder.cardCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton v,
                                         boolean isChecked){
                CardView card = (CardView) v.getParent().getParent().getParent();
                TextView cardName =  (TextView) card.findViewById(R.id.card_name);
                if(isChecked){
                    cardName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    wbActivity.model.toggleDone(cardName.getText().toString(),"1");
                    //未完成单词界面点击checkbox选中后应删除卡片
                    if(wbActivity.actName.equals("未完成")){
                        TransitionManager.beginDelayedTransition((RecyclerView)card.getParent().getParent(),new Fade());
                        card.setVisibility(View.GONE);
                    }
                }else{
                    cardName.setPaintFlags(0);
                    wbActivity.model.toggleDone(cardName.getText().toString(),"0");
                    //已完成单词界面点击checkbox取消选中后应删除卡片
                    if(wbActivity.actName.equals("已完成")){
                        TransitionManager.beginDelayedTransition((RecyclerView)card.getParent().getParent(), new Fade());

                        card.setVisibility(View.GONE);
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return Words.size();
    }
}
