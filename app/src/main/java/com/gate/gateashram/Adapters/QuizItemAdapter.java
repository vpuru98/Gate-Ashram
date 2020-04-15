package com.gate.gateashram.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gate.gateashram.Interface.OnQuizStatusChange;
import com.gate.gateashram.Models.QuizItem;
import com.gate.gateashram.R;

import java.util.List;

public class QuizItemAdapter extends ArrayAdapter<QuizItem> {

    private static int DEFAULT_OPTION_COLOR = R.color.light_gray;
    private static int SELECTED_OPTION_COLOR = R.color.dark_gray;
    private static int CORRECT_OPTION_COLOR = R.color.green;
    private static int WRONG_OPTION_COLOR = R.color.red;

    private Context mContext;
    private String mMode;
    private OnQuizStatusChange mOnQuizStatusChange;

    public QuizItemAdapter(@NonNull Context context, int resource, @NonNull List<QuizItem> objects, String mode,
                           OnQuizStatusChange onQuizStatusChange) {
        super(context, 0, objects);
        mContext = context;
        mMode = mode;
        mOnQuizStatusChange = onQuizStatusChange;
    }

    private void resetOptionEvals(ImageView option_a_eval, ImageView option_b_eval, ImageView
                                  option_c_eval, ImageView option_d_eval){
        option_a_eval.setVisibility(View.GONE);
        option_b_eval.setVisibility(View.GONE);
        option_c_eval.setVisibility(View.GONE);
        option_d_eval.setVisibility(View.GONE);
    }
    
    private void resetOptionBackgrounds(RelativeLayout option_a_layout, RelativeLayout option_b_layout, RelativeLayout
            option_c_layout, RelativeLayout option_d_layout){
        option_a_layout.setBackgroundColor(mContext.getResources().getColor(DEFAULT_OPTION_COLOR));
        option_b_layout.setBackgroundColor(mContext.getResources().getColor(DEFAULT_OPTION_COLOR));
        option_c_layout.setBackgroundColor(mContext.getResources().getColor(DEFAULT_OPTION_COLOR));
        option_d_layout.setBackgroundColor(mContext.getResources().getColor(DEFAULT_OPTION_COLOR));
    }

    private void resetOptionClickListeners(RelativeLayout option_a_layout, RelativeLayout option_b_layout, RelativeLayout
            option_c_layout, RelativeLayout option_d_layout){
        option_a_layout.setOnClickListener(null);
        option_b_layout.setOnClickListener(null);
        option_c_layout.setOnClickListener(null);
        option_d_layout.setOnClickListener(null);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.quiz_item, parent, false);
        }

        final QuizItem item = getItem(position);

        TextView question = (TextView) convertView.findViewById(R.id.question);
        final ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView year = (TextView) convertView.findViewById(R.id.year);
        TextView marks = (TextView) convertView.findViewById(R.id.marks);
        TextView topic_label = (TextView) convertView.findViewById(R.id.topic_label);
        TextView topic_text = (TextView) convertView.findViewById(R.id.topic_text);
        TextView explanation_label = (TextView) convertView.findViewById(R.id.explanation_label);
        TextView explanation_text = (TextView) convertView.findViewById(R.id.explanation_text);
        final RelativeLayout option_a_layout = (RelativeLayout) convertView.findViewById(R.id.option_a_layout);
        TextView option_a_text = (TextView) convertView.findViewById(R.id.option_a_text);
        ImageView option_a_eval = (ImageView) convertView.findViewById(R.id.response_eval_a);
        final RelativeLayout option_b_layout = (RelativeLayout) convertView.findViewById(R.id.option_b_layout);
        TextView option_b_text = (TextView) convertView.findViewById(R.id.option_b_text);
        ImageView option_b_eval = (ImageView) convertView.findViewById(R.id.response_eval_b);
        final RelativeLayout option_c_layout = (RelativeLayout) convertView.findViewById(R.id.option_c_layout);
        TextView option_c_text = (TextView) convertView.findViewById(R.id.option_c_text);
        ImageView option_c_eval = (ImageView) convertView.findViewById(R.id.response_eval_c);
        final RelativeLayout option_d_layout = (RelativeLayout) convertView.findViewById(R.id.option_d_layout);
        TextView option_d_text = (TextView) convertView.findViewById(R.id.option_d_text);
        ImageView option_d_eval = (ImageView) convertView.findViewById(R.id.response_eval_d);

        resetOptionEvals(option_a_eval, option_b_eval, option_c_eval, option_d_eval);
        resetOptionBackgrounds(option_a_layout, option_b_layout, option_c_layout, option_d_layout);
        resetOptionClickListeners(option_a_layout, option_b_layout, option_c_layout, option_d_layout);

        question.setText("Q" + (position + 1) + ". " + item.getmQuestion());
        year.setText("GATE-" + item.getmYear());
        marks.setText(item.getmMarks() + " marks");
        topic_text.setText(item.getmTopic());
        explanation_text.setText(item.getmExplanation());
        option_a_text.setText("A. " + item.getmOptions().get(0));
        option_b_text.setText("B. " + item.getmOptions().get(1));
        option_c_text.setText("C. " + item.getmOptions().get(2));
        option_d_text.setText("D. " + item.getmOptions().get(3));

        if(item.getmImageUrl() != null && !("".equals(item.getmImageUrl()))){
            image.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(item.getmImageUrl()).addListener(new RequestListener< Drawable >(){

                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable>
                        target, boolean isFirstResource) {
                    image.setBackground(mContext.getResources().getDrawable(R.drawable.ic_error_black_24dp));
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                               DataSource dataSource, boolean isFirstResource) {
                    image.setImageDrawable(resource);
                    return false;
                }
            }).into(image);
        }
        else{
            image.setVisibility(View.GONE);
        }

        if(mMode.equals("Evaluate")){
            if("A".equals(item.getmResponse())){
                option_a_layout.setBackgroundColor(mContext.getResources().getColor(WRONG_OPTION_COLOR));
                option_a_eval.setBackground(mContext.getResources().getDrawable(R.drawable.ic_clear_black_24dp));
                option_a_eval.setVisibility(View.VISIBLE);
            }
            if("B".equals(item.getmResponse())){
                option_b_layout.setBackgroundColor(mContext.getResources().getColor(WRONG_OPTION_COLOR));
                option_b_eval.setBackground(mContext.getResources().getDrawable(R.drawable.ic_clear_black_24dp));
                option_b_eval.setVisibility(View.VISIBLE);
            }
            if("C".equals(item.getmResponse())){
                option_c_layout.setBackgroundColor(mContext.getResources().getColor(WRONG_OPTION_COLOR));
                option_c_eval.setBackground(mContext.getResources().getDrawable(R.drawable.ic_clear_black_24dp));
                option_c_eval.setVisibility(View.VISIBLE);
            }
            if("D".equals(item.getmResponse())){
                option_d_layout.setBackgroundColor(mContext.getResources().getColor(WRONG_OPTION_COLOR));
                option_d_eval.setBackground(mContext.getResources().getDrawable(R.drawable.ic_clear_black_24dp));
                option_d_eval.setVisibility(View.VISIBLE);
            }

            if("A".equals(item.getmAnswer())){
                option_a_layout.setBackgroundColor(mContext.getResources().getColor(CORRECT_OPTION_COLOR));
                option_a_eval.setBackground(mContext.getResources().getDrawable(R.drawable.ic_check_black_24dp));
                option_a_eval.setVisibility(View.VISIBLE);
            }
            if("B".equals(item.getmAnswer())){
                option_b_layout.setBackgroundColor(mContext.getResources().getColor(CORRECT_OPTION_COLOR));
                option_b_eval.setBackground(mContext.getResources().getDrawable(R.drawable.ic_check_black_24dp));
                option_b_eval.setVisibility(View.VISIBLE);
            }
            if("C".equals(item.getmAnswer())){
                option_c_layout.setBackgroundColor(mContext.getResources().getColor(CORRECT_OPTION_COLOR));
                option_c_eval.setBackground(mContext.getResources().getDrawable(R.drawable.ic_check_black_24dp));
                option_c_eval.setVisibility(View.VISIBLE);
            }
            if("D".equals(item.getmAnswer())){
                option_d_layout.setBackgroundColor(mContext.getResources().getColor(CORRECT_OPTION_COLOR));
                option_d_eval.setBackground(mContext.getResources().getDrawable(R.drawable.ic_check_black_24dp));
                option_d_eval.setVisibility(View.VISIBLE);
            }

            if(!topic_text.getText().equals("")){
                topic_label.setVisibility(View.VISIBLE);
                topic_text.setVisibility(View.VISIBLE);
            }
            else{
                topic_label.setVisibility(View.GONE);
                topic_text.setVisibility(View.GONE);
            }

            if(!explanation_text.getText().equals("")){
                explanation_label.setVisibility(View.VISIBLE);
                explanation_text.setVisibility(View.VISIBLE);
            }
            else{
                explanation_label.setVisibility(View.GONE);
                explanation_text.setVisibility(View.GONE);
            }
        }
        else{
            if("A".equals(item.getmResponse())){
                option_a_layout.setBackgroundColor(mContext.getResources().getColor(SELECTED_OPTION_COLOR));
            }
            if("B".equals(item.getmResponse())){
                option_b_layout.setBackgroundColor(mContext.getResources().getColor(SELECTED_OPTION_COLOR));
            }
            if("C".equals(item.getmResponse())){
                option_c_layout.setBackgroundColor(mContext.getResources().getColor(SELECTED_OPTION_COLOR));
            }
            if("D".equals(item.getmResponse())){
                option_d_layout.setBackgroundColor(mContext.getResources().getColor(SELECTED_OPTION_COLOR));
            }

            option_a_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if("A".equals(item.getmResponse())){
                        item.setmResponse(null);
                        mOnQuizStatusChange.updateQuizStatus(position);
                        option_a_layout.setBackgroundColor(mContext.getResources().getColor(DEFAULT_OPTION_COLOR));
                    }
                    else{
                        if(item.getmResponse() == null){
                            mOnQuizStatusChange.updateQuizStatus(position);
                        }
                        resetOptionBackgrounds(option_a_layout, option_b_layout, option_c_layout, option_d_layout);
                        option_a_layout.setBackgroundColor(mContext.getResources().getColor(SELECTED_OPTION_COLOR));
                        item.setmResponse("A");
                    }
                }
            });
            option_b_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if("B".equals(item.getmResponse())){
                        item.setmResponse(null);
                        mOnQuizStatusChange.updateQuizStatus(position);
                        option_b_layout.setBackgroundColor(mContext.getResources().getColor(DEFAULT_OPTION_COLOR));
                    }
                    else{
                        if(item.getmResponse() == null){
                            mOnQuizStatusChange.updateQuizStatus(position);
                        }
                        resetOptionBackgrounds(option_a_layout, option_b_layout, option_c_layout, option_d_layout);
                        option_b_layout.setBackgroundColor(mContext.getResources().getColor(SELECTED_OPTION_COLOR));
                        item.setmResponse("B");
                    }
                }
            });
            option_c_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if("C".equals(item.getmResponse())){
                        item.setmResponse(null);
                        mOnQuizStatusChange.updateQuizStatus(position);
                        option_c_layout.setBackgroundColor(mContext.getResources().getColor(DEFAULT_OPTION_COLOR));
                    }
                    else{
                        if(item.getmResponse() == null){
                            mOnQuizStatusChange.updateQuizStatus(position);
                        }
                        resetOptionBackgrounds(option_a_layout, option_b_layout, option_c_layout, option_d_layout);
                        option_c_layout.setBackgroundColor(mContext.getResources().getColor(SELECTED_OPTION_COLOR));
                        item.setmResponse("C");
                    }
                }
            });
            option_d_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if("D".equals(item.getmResponse())){
                        item.setmResponse(null);
                        mOnQuizStatusChange.updateQuizStatus(position);
                        option_d_layout.setBackgroundColor(mContext.getResources().getColor(DEFAULT_OPTION_COLOR));
                    }
                    else{
                        if(item.getmResponse() == null){
                            mOnQuizStatusChange.updateQuizStatus(position);
                        }
                        resetOptionBackgrounds(option_a_layout, option_b_layout, option_c_layout, option_d_layout);
                        option_d_layout.setBackgroundColor(mContext.getResources().getColor(SELECTED_OPTION_COLOR));
                        item.setmResponse("D");
                    }
                }
            });

            topic_label.setVisibility(View.GONE);
            topic_text.setVisibility(View.GONE);
            explanation_label.setVisibility(View.GONE);
            explanation_text.setVisibility(View.GONE);
        }

        return convertView;
    }


}
