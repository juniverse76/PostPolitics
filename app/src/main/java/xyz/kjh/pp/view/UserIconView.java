package xyz.kjh.pp.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import xyz.kjh.pp.R;
import xyz.kjh.pp.service.model.res.UserM;

/**
 * Created by juniverse on 24/03/2017.
 */

public class UserIconView extends FrameLayout
{
    public UserIconView(@NonNull Context context) {
        super(context);
        init();
    }

    public UserIconView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserIconView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    ImageView iconView;
    TextView ageView;
    private void init()
    {
        iconView = new ImageView(getContext());
        iconView.setImageResource(R.drawable.ic_person);
        addView(iconView);

        ageView = new TextView(getContext());
        ageView.setBackgroundResource(R.drawable.user_icon_age_bg);
        ageView.setTextColor(0xffffffff);
        ageView.setTextSize(8);
        ageView.setPadding(4, 2, 4, 2);
        ageView.setTypeface(null, Typeface.BOLD);
        ageView.setText("00");
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        addView(ageView, params);
    }

    public void setUserInfo(UserM info)
    {
        if (info == null) return;

        if (info.sex == UserM.GENDER_MALE)
            iconView.setImageResource(R.drawable.ic_man);
        else if (info.sex == UserM.GENDER_FEMALE)
            iconView.setImageResource(R.drawable.ic_woman);
        else
            iconView.setImageResource(R.drawable.ic_person);

        if (info.status == UserM.STATUS_PROGRESSIVE)
            iconView.setColorFilter(getResources().getColor(R.color.progressive));
        else if (info.status == UserM.STATUS_CONSERVATIVE)
            iconView.setColorFilter(getResources().getColor(R.color.conservative));
        else
            iconView.setColorFilter(getResources().getColor(R.color.secret));

        ageView.setText("" + info.age);

//        if (info.party)
//            setBackgroundResource();
    }

}
