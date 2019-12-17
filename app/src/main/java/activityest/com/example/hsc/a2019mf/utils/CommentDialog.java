package activityest.com.example.hsc.a2019mf.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import activityest.com.example.hsc.a2019mf.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by WW on 2017/2/23.
 * 评论对话框
 */
public class CommentDialog extends Dialog implements View.OnClickListener {

    @BindView(R.id.tv_commit)
    TextView tv_commit;//提交
    @BindView(R.id.et_comment)
    EditText et_comment;//评论内容

    @BindView(R.id.view_line)
    View view_line;//竖线

    @BindView(R.id.cb_anonymous)
    CheckBox cb_anonymous;//转发
    private Context context;
    private OnCommitListener listener;

    public CommentDialog(Context context) {
        this(context, R.style.inputDialog);
        this.context = context;
    }

    public CommentDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_dialog_layout);

        //弹框底部
        android.view.WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        this.getWindow().setAttributes(lp);

        ButterKnife.bind(this);
        initListener();
    }

    private void initListener() {
        //设置显示对话框时的返回键的监听
        this.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0)
                    CommentDialog.this.cancel();
                return false;
            }
        });
        //设置EditText内容改变的监听
        et_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    tv_commit.setTextColor(R.color.color_back);
                    tv_commit.setClickable(true);
                } else {
                    tv_commit.setTextColor(R.color.gray);
                    tv_commit.setClickable(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //匿名
        cb_anonymous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (null != listener) {
                    listener.onAnonymousChecked(buttonView, isChecked);
                }
            }
        });
        tv_commit.setOnClickListener(this);//提交
    }

    public void setOnCommitListener(OnCommitListener listener) {
        this.listener = listener;
    }

    public interface OnCommitListener {

        void onCommit(EditText et, View v);//提交数据


        void onAnonymousChecked(CompoundButton buttonView, boolean isChecked);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commit:
                if (null != listener) {
                    listener.onCommit(et_comment, v);
                }
                break;
        }
    }

}
