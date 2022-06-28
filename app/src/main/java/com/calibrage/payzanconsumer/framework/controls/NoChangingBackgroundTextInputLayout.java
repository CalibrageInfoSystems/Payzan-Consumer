package com.calibrage.payzanconsumer.framework.controls;

import android.content.Context;
import android.graphics.ColorFilter;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.calibrage.payzanconsumer.R;


/**
 * Created by Calibrage11 on 10/4/2017.
 */

public class NoChangingBackgroundTextInputLayout extends TextInputLayout {
    public View paddingView;

    public NoChangingBackgroundTextInputLayout(Context context) {
        super(context);
    }

    public NoChangingBackgroundTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoChangingBackgroundTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setError(@Nullable CharSequence error) {
        ColorFilter defaultColorFilter = getBackgroundDefaultColorFilter();
        super.setError(error);
        //Reset EditText's background color to default.
        updateBackgroundColorFilter(defaultColorFilter, 0);
    }

    @Override
    public void setHintTextAppearance(@StyleRes int resId) {
        super.setHintTextAppearance(resId);

    }

    @Override
    public void setHint(@Nullable CharSequence hint) {
        super.setHint(hint);
    }

    @Override
    protected void drawableStateChanged() {
        ColorFilter defaultColorFilter = getBackgroundDefaultColorFilter();
        super.drawableStateChanged();
        //Reset EditText's background color to default.
        updateBackgroundColorFilter(defaultColorFilter, 1);
    }

    /**
     * If {@link #getEditText()} is not null & {@link #getEditText()#getBackground()} is not null,
     * update the {@link ColorFilter} of {@link #getEditText()#getBackground()}.
     *
     * @param colorFilter {@link ColorFilter}
     */
    private void updateBackgroundColorFilter(ColorFilter colorFilter, int state) {
        if (getEditText() != null && getEditText().getBackground() != null && state == 0) {
            getEditText().getBackground().setColorFilter(colorFilter);
            getEditText().setBackground(getResources().getDrawable(R.drawable.border_accentcolor_button, null));
        } else if (getEditText() != null && getEditText().getBackground() != null && state == 1) {

            getEditText().getBackground().setColorFilter(colorFilter);
            getEditText().setBackground(getResources().getDrawable(R.drawable.roundededittext, null));

//            getEditText().addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    getEditText().setBackground(getResources().getDrawable(R.drawable.roundededittext,null));
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                }
//            });
        }

    }

    public void addPaddingView() {
        paddingView = new View(getContext());
        int height = (int) getContext().getResources()
                .getDimension(R.dimen.edittext_text_input_layout_padding);
        ViewGroup.LayoutParams paddingParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height);
        super.addView(paddingView, 0, paddingParams);
    }

    /**
     * Get the EditText's default background color.
     *
     * @return {@link ColorFilter}
     */
    @Nullable
    private ColorFilter getBackgroundDefaultColorFilter() {
        ColorFilter defaultColorFilter = null;
        if (getEditText() != null && getEditText().getBackground() != null)
            defaultColorFilter = DrawableCompat.getColorFilter(getEditText().getBackground());
        return defaultColorFilter;
    }

    public void refreshPaddingView() {
        if (paddingView != null) {
            removeView(paddingView);
            paddingView = null;
        }
        addPaddingView();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        refreshPaddingView();
    }

}