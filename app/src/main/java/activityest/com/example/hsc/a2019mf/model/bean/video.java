package activityest.com.example.hsc.a2019mf.model.bean;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * @description: 视频实体类
 */

public class video extends BmobObject {
    private String VideoTitle;
    private String username;
    private BmobFile VideoFile;
    private BmobFile VideoImage;
    private String VideoTime;
    public video(){
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }


    public String getVideoTitle() {
        return VideoTitle;
    }
    public void setVideoTitle(String videoTitle) {
        this.VideoTitle = videoTitle;
    }

    public BmobFile getVideoFile() {
        return VideoFile;
    }

    public void setVideoFile(BmobFile videoFile) {
        VideoFile = videoFile;
    }


    public BmobFile getVideoImage() {
        return VideoImage;
    }

    public void setVideoImage(BmobFile videoImage) {
        VideoImage = videoImage;
    }

    public String getVideoTime() {
        return VideoTime;
    }

    public void setVideoTime(String videoTime) {
        VideoTime = videoTime;
    }
}
