package activityest.com.example.hsc.a2019mf.model.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class advertisement extends BmobObject {
    private String username;//用户名
    private BmobFile Picture;//用户头像



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BmobFile getPicture() {
        return Picture;
    }

    public void setPicture(BmobFile icon) {
        Picture = icon;
    }

}