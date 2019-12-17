package activityest.com.example.hsc.a2019mf.view.ui.activity;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import activityest.com.example.hsc.a2019mf.R;
import activityest.com.example.hsc.a2019mf.model.bean.User;
import activityest.com.example.hsc.a2019mf.model.bean.advertisement;
import activityest.com.example.hsc.a2019mf.model.bean.article;
import activityest.com.example.hsc.a2019mf.model.bean.interst;
import activityest.com.example.hsc.a2019mf.utils.CommentDialog;
import activityest.com.example.hsc.a2019mf.utils.FollowStateButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class ArticleActivity extends AppCompatActivity {
    @BindView(R.id.news_detail_title)
    TextView textnews_title;
    @BindView(R.id.textnews_back)
    ImageView textnews_back;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.title)
    LinearLayout title;
    @BindView(R.id.news_detail_head)
    ImageView newsDetailHead;
    @BindView(R.id.news_detail_author)
    TextView newsDetailAuthor;
    @BindView(R.id.news_detail_times)
    TextView newsDetailTimes;
    @BindView(R.id.news_detail_img)
    ImageView newsDetailImg;
    @BindView(R.id.news_detail_text)
    TextView newsDetailText;
    @BindView(R.id.fl_comment_icon)
    FrameLayout flCommentIcon;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.textView0)
    TextView textView0;
    @BindView(R.id.detail_dialog)
    TextView detailDialog;
    @BindView(R.id.tv_comment_count)
    TextView tvCommentCount;
    @BindView(R.id.iv_collect)
    ImageView ivCollect;
    @BindView(R.id.iv_like)
    ImageView ivLike;
    @BindView(R.id.followstatebutton)
    FollowStateButton followStateButton;
    @BindView(R.id.txguanzhu)
    TextView txguanzhu;


    static int flag1 = 0;
    static int flag2 = 0;

    private int type = 0; // 0为加关注 1 关注
    private String username = "";
    private String fatherusername = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        //获取从MyFragment_basicMessage中传过来的值
        Intent intent = getIntent();
        String objectId = intent.getStringExtra("objectId");
        show_info(objectId);
        ButterKnife.bind(this);

    }

    private void show_info(String objectId) {

        Log.e("objectId", objectId);
        if (BmobUser.getCurrentUser(User.class) != null) {
            username = BmobUser.getCurrentUser(User.class).getUsername();
            BmobQuery<interst> queryinterst = new BmobQuery<>();
            queryinterst.addWhereEqualTo("username", username);
            queryinterst.findObjects(new FindListener<interst>() {
                @Override
                public void done(List<interst> list, BmobException e) {
                    if (e == null) {
                        for (interst ad : list) {
                            if (ad.getFatherusername().equals(username)) {
                                followStateButton.alreadyFollow("已关注");
                                type = 1;
                                Log.e("关注","txguanzhu  type="+type);
                            }
                        }
                    } else {
                        Log.e("BMOB", e.toString());

                    }
                }
            });
        }
        BmobQuery<article> bmobQuery = new BmobQuery<article>();
        bmobQuery.getObject(objectId, new QueryListener<article>() {
            @Override
            public void done(final article data, BmobException e) {
                if (e == null) {
                    fatherusername = data.getArticleAuthor();
                    textnews_title.setText(data.getArticleTitle());
                    newsDetailAuthor.setText(data.getArticleAuthor());
                    newsDetailTimes.setText(data.getCreatedAt());
                    newsDetailText.setText(data.getArticleContent());
                    uploadnewsDetailImg(data.getArticleImage());
                    uploadHeadImage(data.getArticleAuthor());
                    if (data.getArticleAuthor().equals(username)) {
                        followStateButton.setVisibility(View.GONE);
                    }
                } else {

                }
            }
        });
    }


    private void uploadnewsDetailImg(BmobFile file) {
        file.download(new DownloadFileListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    //设置圆形头像并显示
                    newsDetailImg.setImageBitmap(BitmapFactory.decodeFile(s)); //根据地址解码并显示图片
                    Log.e("文章图片为", s, e);
                } else {
                    Log.e("文章图片下载失败", "原因：", e);
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });
    }

    private void uploadHeadImage(final String username) {
        BmobQuery<advertisement> query = new BmobQuery<>();
        query.addWhereEqualTo("username", username);
        query.findObjects(new FindListener<advertisement>() {
            @Override
            public void done(List<advertisement> list, BmobException e) {
                if (e == null) {
                    for (advertisement ad : list) {
                        if (ad.getUsername() != null && username.equals(ad.getUsername())) {
                            BmobFile icon = ad.getPicture();
                            icon.download(new DownloadFileListener() {
                                @Override
                                public void onProgress(Integer integer, long l) {
                                }

                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        //设置圆形头像并显示
                                        newsDetailHead.setImageBitmap(BitmapFactory.decodeFile(s)); //根据地址解码并显示图片
                                        Log.e("头像图片为", s, e);
                                    } else {
                                        Log.e("用户图片下载失败", "原因：", e);
                                    }
                                }
                            });
                            break;
                        }
                    }

                } else {
                    Log.e("用户图片下载失败", "原因：", e);
                }
            }
        });

    }



    @OnClick()
    public void onViewClicked() {
    }

    @OnClick({R.id.txguanzhu,R.id.detail_dialog, R.id.tv_comment_count, R.id.iv_collect, R.id.iv_like, R.id.textnews_back})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        if (BmobUser.getCurrentUser(User.class) != null) {
            username = BmobUser.getCurrentUser(User.class).getUsername();
            switch (view.getId()) {
                case R.id.txguanzhu:
                    break;
                case R.id.detail_dialog:
                    CommentDialog commentDialog = new CommentDialog(ArticleActivity.this);
                    commentDialog.show();
                    break;
                case R.id.tv_comment_count:
                    break;
                case R.id.iv_collect:
                    flag2++;
                    if (flag2 % 2 == 1) {
                        ivCollect.setImageResource(R.drawable.ic_star_yellow_24dp);
                    } else {
                        ivCollect.setImageResource(R.drawable.ic_star_black_24dp);
                    }
                    break;
                case R.id.iv_like:
                    flag1++;
                    if (flag1 % 2 == 1) {
                        ivLike.setImageResource(R.drawable.ic_thumb_up_red_24dp);
                    } else {
                        ivLike.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                    }
                    break;
                case R.id.textnews_back:
                    finish();
                    break;
            }
        } else {
            Toast.makeText(ArticleActivity.this, "请登录", Toast.LENGTH_SHORT).show();
            intent = new Intent(ArticleActivity.this, LoginActivity.class); //参数1:Fragment所依存的Activity,参数2：要跳转的Activity
            ArticleActivity.this.startActivity(intent); //这里一定要获取到所在Activity再startActivity()；
        }
    }


    public void addinterst(String fatherusername, String username) {
        interst p2 = new interst();
        p2.setFatherusername(fatherusername);
        p2.setUsername(username);
        p2.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Log.e("添加数据成功，返回objectId为：", objectId);
                } else {
                    Log.e("添加数据成功，返回objectId为：", objectId);
                }
            }
        });
    }

    public void deleteinterst(String fatherusername, String username) {
        BmobQuery<interst> queryinterst = new BmobQuery<>();
        interst p2 = new interst();
        queryinterst.addWhereEqualTo("username", username);
        queryinterst.findObjects(new FindListener<interst>() {
            @Override
            public void done(List<interst> list, BmobException e) {
                if (e == null) {
                    for (interst it : list) {
                        if (it.getFatherusername().equals(fatherusername)) {
                            p2.setObjectId(it.getObjectId());
                            p2.delete(new UpdateListener() {

                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Log.e("删除成功:", p2.getUpdatedAt());
                                    } else {
                                        Log.e("删除失败：", e.getMessage());
                                    }
                                }

                            });
                        }
                    }
                } else {
                    Log.e("BMOB", e.toString());

                }
            }
        });
    }



}
