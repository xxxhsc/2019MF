package activityest.com.example.hsc.a2019mf.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

import activityest.com.example.hsc.a2019mf.R;
import activityest.com.example.hsc.a2019mf.model.bean.article;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

public class CollectRecycleAdapter extends RecyclerView.Adapter<CollectRecycleAdapter.myViewHodler>{
    private Context context;
    private ArrayList<article> articleEntityArrayList;
    private CollectRecycleAdapter.OnItemClickLitener mOnItemClickLitener;
    private int articleType = 0;     // 第一种ViewType，文章
    private int videoType = 1;       // 第二种ViewType，视频




    // 设置点击事件的接口，利用接口回调，来完成点击事件
    public interface OnItemClickLitener {
        void onItemClick(View view, String objectId);

        void onItemLongClick(View view, String objectId);
    }



    public void setOnItemClickLitener(CollectRecycleAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public CollectRecycleAdapter(Context context,ArrayList<article> articleEntityArrayList){
//        将传递过来的数据，赋值给本地变量
        this.context=context;
        this.articleEntityArrayList=articleEntityArrayList;
    }



    @Override
    public CollectRecycleAdapter.myViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==articleType) {
            View itemView = View.inflate(context, R.layout.item_article_list, null);
            return new CollectRecycleAdapter.myViewHodler(itemView);
        }else {
            View itemView = View.inflate(context, R.layout.item_video_news, null);
            return new CollectRecycleAdapter.myViewHodler(itemView);
        }

    }
    class  myViewHodler extends RecyclerView.ViewHolder{
        private ImageView ArticleItemImg;
        private TextView ArticleItemTitle;
        private TextView ArticleItemAthur;
        private TextView ArticleItemTime;
        private ImageView VideoItemHeadImage;
        private TextView VideoItemTitle,VideoItemTitle2;
        private TextView VideoItemAuthor;
        private VideoView VideoItemVideo;
        private ImageView VideoItemImage;
        private TextView VideoItemTime;


        public myViewHodler(View itemView) {
            super(itemView);
            ArticleItemImg=itemView.findViewById(R.id.article_img);
            ArticleItemTitle=itemView.findViewById(R.id.article_title);
            ArticleItemAthur=itemView.findViewById(R.id.article_author);
            ArticleItemTime=itemView.findViewById(R.id.article_time);

            VideoItemHeadImage=itemView.findViewById(R.id.video_headimage);
//            VideoItemTitle=itemView.findViewById(R.id.video_title);
//            VideoItemTitle2=itemView.findViewById(R.id.video_title2);
            VideoItemAuthor=itemView.findViewById(R.id.video_author);
//            VideoItemVideo=itemView.findViewById(R.id.video_view);
//            VideoItemImage=itemView.findViewById(R.id.video_image);
            VideoItemTime=itemView.findViewById(R.id.video_time);


        }
    }
    @Override
    public void onBindViewHolder(@NonNull final CollectRecycleAdapter.myViewHodler holder, int i) {
//        final article data=articleEntityArrayList.get(articleEntityArrayList.size()-i-1);
        final article data=articleEntityArrayList.get(i);
        BmobFile bmobfile=data.getArticleImage();
        if (bmobfile!=null) {
            bmobfile.download(new DownloadFileListener() {
                @Override
                public void onProgress(Integer integer, long l) {
                }
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        //设置圆形头像并显示
                        holder.ArticleItemImg.setImageBitmap(BitmapFactory.decodeFile(s)); //根据地址解码并显示图片
                    } else {
                        Log.e("用户图片下载失败", "原因：", e);
                    }
                }
            });
        }
        holder.ArticleItemTitle.setText(data.getArticleTitle());
        holder.ArticleItemAthur.setText(data.getArticleAuthor());
        holder.ArticleItemTime.setText(data.getCreatedAt());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                String objectId =data.getObjectId();
                mOnItemClickLitener.onItemClick(holder.itemView, objectId);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = holder.getLayoutPosition();
                String objectId =data.getObjectId();
                mOnItemClickLitener.onItemLongClick(holder.itemView, objectId);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return articleEntityArrayList.size();
    }

    // 暴露接口，更新数据源，并修改hasMore的值，如果有增加数据，hasMore为true，否则为false
    public void updateList(ArrayList<article> newDatas) {
        // 在原有的数据之上增加新数据
        if (newDatas != null) {
            articleEntityArrayList.addAll(newDatas);
        }
        notifyDataSetChanged();
    }



}
