package activityest.com.example.hsc.a2019mf.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import activityest.com.example.hsc.a2019mf.R;
import activityest.com.example.hsc.a2019mf.model.bean.advertisement;
import activityest.com.example.hsc.a2019mf.model.bean.video;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.jzvd.JzvdStd;

public class
VideoRecycleAdapter extends RecyclerView.Adapter<VideoRecycleAdapter.myViewHodler> {
    @NonNull
    private Context context;
    private ArrayList<video> videoEntitieslist;
    private VideoRecycleAdapter.OnItemClickLitener mOnItemClickLitener;
    private String ss;

    // 设置点击事件的接口，利用接口回调，来完成点击事件
    public interface OnItemClickLitener {
        void onItemClick(View view, String objectId,String path);

    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public VideoRecycleAdapter(Context context,ArrayList<video> videoEntitieslist){
//        将传递过来的数据，赋值给本地变量
        this.context=context;
        this.videoEntitieslist=videoEntitieslist;

    }

    @Override
    public myViewHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView=View.inflate(context, R.layout.item_video_news,null);
        return  new myViewHodler(itemView);
    }

    class  myViewHodler extends RecyclerView.ViewHolder{
        private ImageView VideoItemHeadImage;
        private TextView VideoItemTitle,VideoItemTitle2;
        private TextView VideoItemAuthor;
        private VideoView VideoItemVideo;
        private ImageView VideoItemImage;
        private TextView VideoItemTime;
        private JzvdStd player;
        public myViewHodler(View itemView) {
            super(itemView);
            VideoItemHeadImage=itemView.findViewById(R.id.video_headimage);
//            VideoItemTitle=itemView.findViewById(R.id.video_title);
            VideoItemAuthor=itemView.findViewById(R.id.video_author);
//            VideoItemVideo=itemView.findViewById(R.id.video_view);
            VideoItemTime=itemView.findViewById(R.id.video_time);
            player=itemView.findViewById(R.id.videoplayer);


        }
    }

    //    绑定数据
    @Override
    public void onBindViewHolder(@NonNull final myViewHodler myViewHodler, int i) {
        final video data=videoEntitieslist.get(i);
//        myViewHodler.VideoItemTitle2.setText(data.getVideoTitle());
        myViewHodler.VideoItemAuthor.setText(data.getUsername());
        myViewHodler.VideoItemTime.setText(data.getVideoTime());
        BmobQuery<advertisement> query=new BmobQuery<>();
        query.addWhereEqualTo("username", data.getUsername());
        query.findObjects(new FindListener<advertisement>() {
            @Override
            public void done(List<advertisement> list, BmobException e) {
                if(e == null){
                    for (advertisement ad : list){
                        if(ad.getUsername() != null && data.getUsername().equals(ad.getUsername())){
                            BmobFile icon= ad.getPicture();
                            icon.download(new DownloadFileListener() {
                                @Override
                                public void onProgress(Integer integer, long l) {

                                }
                                @Override
                                public void done(String s, BmobException e) {
                                    if(e == null){
                                        //设置圆形头像并显示
                                        ss=s;
                                        myViewHodler.VideoItemHeadImage.setImageBitmap(BitmapFactory.decodeFile(s)); //根据地址解码并显示图片
                                        Log.e("头像图片为",s,e);
                                    }
                                    else{
                                        Log.e("用户图片下载失败","原因：",e);
                                    }
                                }
                            });
                            break;
                        }
                    }

                }else{
                    Log.e("用户图片下载失败","原因：",e);
                }
            }
        });
        BmobFile bmobfile1=data.getVideoFile();
        BmobFile bmobfile2=data.getVideoImage();
        myViewHodler.player.setUp(bmobfile1.getFileUrl(), data.getVideoTitle());

        Glide.with(context).load(bmobfile2.getFileUrl()).into(myViewHodler.player.thumbImageView);

//        if (bmobfile!=null) {
//            bmobfile.download(new DownloadFileListener() {
//                @Override
//                public void onProgress(Integer integer, long l) {
//
//                }
//
//                @Override
//                public void done(String s, BmobException e) {
//                    if (e == null) {
//                        //设置并显示
//                        myViewHodler.player.thumbImageView.setImageBitmap(getVideoThumb(s)); //根据地址解码并显示第一张图片
//
//                        Log.e("用户视频下载失败", "原因：", e);
//
//                    } else {
//                        Log.e("用户视频下载失败", "原因：", e);
//                    }
//                }
//            });
//        }
        myViewHodler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = myViewHodler.getLayoutPosition();
                String objectId =data.getObjectId();
                String path=data.getVideoFile().getUrl();
                Log.e("adapter视频地址为", path);
                mOnItemClickLitener.onItemClick(myViewHodler.itemView,objectId, path);
            }
        });
    }

    /**
     * 获取视频文件截图
     *
     * @param path 视频文件的路径
     * @return Bitmap 返回获取的Bitmap
     */
    public  static Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return  media.getFrameAtTime();
    }

    public long getVideotime(String path) throws IOException {

        MediaPlayer meidaPlayer = new MediaPlayer();

        meidaPlayer.setDataSource(path);

        meidaPlayer.prepare();

        long time = meidaPlayer.getDuration();//获得了视频的时长（以毫秒为单位）
        return time;
    }

    /**
     * 将毫秒转时分秒
     *
     * @param time
     * @return
     */
    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }


    @Override
    public int getItemCount() {
        return videoEntitieslist.size();
    }
}
