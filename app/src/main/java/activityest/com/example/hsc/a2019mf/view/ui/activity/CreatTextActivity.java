package activityest.com.example.hsc.a2019mf.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import activityest.com.example.hsc.a2019mf.R;
import activityest.com.example.hsc.a2019mf.model.bean.article;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class CreatTextActivity extends AppCompatActivity {
    @BindView(R.id.write_text_back)
    TextView writeTextBack;
    @BindView(R.id.write_send)
    TextView writeSend;
    @BindView(R.id.creation_write_title)
    TextInputEditText creationWriteTitle;
    @BindView(R.id.creation_write_text)
    TextInputEditText creationWriteText;
    private String username ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_writing_text);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        ButterKnife.bind(this);
    }

    @OnClick({R.id.write_text_back, R.id.write_send})

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.write_text_back:
                finish();
                break;
            case R.id.write_send:
                if(!creationWriteTitle.getText().toString().trim().equals("")&&!creationWriteText.getText().toString().trim().equals(""))    //不为空
                {
                    upload(username);
                }
                else{
                    Toast.makeText(CreatTextActivity.this,"要发布的内容不完整",Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
        }
    }

    private void upload(String username) {
        article ac = new article();
        ac.setArticleTitle(creationWriteTitle.getText().toString());
        ac.setArticleContent(creationWriteText.getText().toString());
        ac.setArticleAuthor(username);
        ac.setArticleClass("");
        ac.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Log.e("用户图片列表添加成功","原因：",e);
                    Toast.makeText(CreatTextActivity.this,"发布成功！" ,Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("用户图片列表添加失败","原因：",e);
                    Toast.makeText(CreatTextActivity.this,"发布失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}