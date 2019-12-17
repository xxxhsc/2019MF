package activityest.com.example.hsc.a2019mf.model.bean;


import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class article extends BmobObject {
    private String ArticleTitle;
    private String ArticleAuthor;
    private String ArticleContent;
    private String ArticleClass;
    private BmobFile ArticleImage;



    public String getArticleAuthor() {
        return ArticleAuthor;
    }

    public String getArticleClass() {
        return ArticleClass;
    }

    public String getArticleContent() {
        return ArticleContent;
    }

    public String getArticleTitle() {
        return ArticleTitle;
    }

    public BmobFile getArticleImage() {
        return ArticleImage;
    }



    public void setArticleAuthor(String articleAuthor) {
        ArticleAuthor = articleAuthor;
    }

    public void setArticleClass(String articleClass) {
        ArticleClass = articleClass;
    }

    public void setArticleContent(String articleContent) {
        ArticleContent = articleContent;
    }

    public void setArticleTitle(String articleTitle) {
        ArticleTitle = articleTitle;
    }

    public void setArticleImage(BmobFile articleImage) {
        ArticleImage = articleImage;
    }
}
