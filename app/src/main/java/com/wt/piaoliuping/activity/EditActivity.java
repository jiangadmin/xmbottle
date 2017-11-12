package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;
import com.wt.piaoliuping.utils.DisplayUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/29.
 */

public class EditActivity extends BaseTitleActivity {
    @BindView(R.id.text_right_title)
    TextView textRightTitle;
    @BindView(R.id.text_edit)
    EditText textEdit;

    private String title;
    private String hint;
    private String content;
    private String key;
    private int type = 0; //0 单行 1 多行

    @Override
    public void initView() {
        title = getIntent().getStringExtra("title");
        hint = getIntent().getStringExtra("hint");
        content = getIntent().getStringExtra("content");
        key = getIntent().getStringExtra("key");
        type = getIntent().getIntExtra("type", 0);

        if (type == 0) {
            textEdit.setSingleLine(true);
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(this, 200));
            layoutParams.setMargins(DisplayUtil.dip2px(this, 10), DisplayUtil.dip2px(this, 10), DisplayUtil.dip2px(this, 10), 0);
            textEdit.setLayoutParams(layoutParams);
            textEdit.setGravity(Gravity.TOP);
            textEdit.setPadding(DisplayUtil.dip2px(this, 10), DisplayUtil.dip2px(this, 10), DisplayUtil.dip2px(this, 10), DisplayUtil.dip2px(this, 10));
        }
        textEdit.setHint(hint);
        textEdit.setText(content);
        setTitle(title);
        textRightTitle.setText("保存");
        textRightTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_edit;
    }

    @OnClick(R.id.text_right_title)
    public void onViewClicked() {
        Intent intent = new Intent();
        intent.putExtra("key", key);
        intent.putExtra("value", textEdit.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
