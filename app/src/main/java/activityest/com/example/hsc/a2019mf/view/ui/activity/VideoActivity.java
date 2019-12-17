package activityest.com.example.hsc.a2019mf.view.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
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
import activityest.com.example.hsc.a2019mf.model.bean.video;
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
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import de.hdodenhof.circleimageview.CircleImageView;


public class VideoActivity extends AppCompatActivity {
    @BindView(R.id.news_detail_head)
    CircleImageView VideoHead;
    @BindView(R.id.news_detail_author)
    TextView VideoAuthor;
    @BindView(R.id.news_detail_times)
    TextView VideoTimes;
    @BindView(R.id.bt_)
    TextView bt;
    @BindView(R.id.guanzhu)
    LinearLayout guanzhu;
    @BindView(R.id.detail_dialog)
    TextView detailDialog;
    @BindView(R.id.tv_comment_count)
    TextView tvCommentCount;
    @BindView(R.id.fl_comment_icon)
    FrameLayout flCommentIcon;
    @BindView(R.id.iv_collect)
    ImageView ivCollect;
    @BindView(R.id.iv_like)
    ImageView ivLike;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.videoplayer)
    JzvdStd player;
    @BindView(R.id.VideoTitle)
    TextView VideoTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String objectId = intent.getStringExtra("objectId");
        String path = intent.getStringExtra("path");
        show_info(objectId, path);
//        player.setUp(path, "测试");
//        Glide.with(VideoActivity.this).load("https://bmob-cdn-26347.bmobpay.com/2019/12/04/1449f4c240c82ab5808b1ea27102b7f1.mp4").into(player.thumbImageView);

//        //直接进入全屏
//        player.startFullscreen(this, JCVideoPlayerStandard.class, videoUrl, "");
//        //模拟用户点击开始按钮，NORMAL状态下点击开始播放视频，播放中点击暂停视频
//        player.startButton.performClick();

    }

    private void show_info(String objectId, String path) {

        BmobQuery<video> bmobQuery = new BmobQuery<video>();
        bmobQuery.getObject(objectId, new QueryListener<video>() {
            @Override
            public void done(final video data, BmobException e) {
                if (e == null) {
                    VideoAuthor.setText(data.getUsername());
                    VideoTitle.setText(data.getVideoTitle());
                    Log.e("activity视频地址为", path);
                    player.setUp(path, data.getVideoTitle());
//                    Glide.with(VideoActivity.this).load("https://bmob-cdn-26347.bmobpay.com/2019/12/04/1449f4c240c82ab5808b1ea27102b7f1.mp4").into(player.thumbImageView);
                    BmobQuery<advertisement> query = new BmobQuery<>();
                    query.addWhereEqualTo("username", data.getUsername());
                    query.findObjects(new FindListener<advertisement>() {
                        @Override
                        public void done(List<advertisement> list, BmobException e) {
                            if (e == null) {
                                for (advertisement ad : list) {
                                    if (ad.getUsername() != null && data.getUsername().equals(ad.getUsername())) {
                                        BmobFile icon = ad.getPicture();
                                        icon.download(new DownloadFileListener() {
                                            @Override
                                            public void onProgress(Integer integer, long l) {

                                            }

                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null) {
                                                    //设置圆形头像并显示
                                                    VideoHead.setImageBitmap(BitmapFactory.decodeFile(s)); //根据地址解码并显示图片
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

                } else {

                }
            }
        });
    }

    /**
     * 获取视频文件截图
     *
     * @param path 视频文件的路径
     * @return Bitmap 返回获取的Bitmap
     */
    public static Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @OnClick({R.id.guanzhu, R.id.detail_dialog, R.id.tv_comment_count, R.id.iv_collect, R.id.iv_like})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        if (BmobUser.getCurrentUser(User.class) != null) {
            switch (view.getId()) {
                case R.id.guanzhu:
                    break;
                case R.id.detail_dialog:
                    break;
                case R.id.tv_comment_count:
                    break;
                case R.id.iv_collect:
                    break;
                case R.id.iv_like:
                    break;
            }
        } else {
            Toast.makeText(VideoActivity.this, "请登录", Toast.LENGTH_SHORT).show();
            intent = new Intent(VideoActivity.this, LoginActivity.class); //参数1:Fragment所依存的Activity,参数2：要跳转的Activity
            VideoActivity.this.startActivity(intent); //这里一定要获取到所在Activity再startActivity()；
        }
    }
}
