package tyxo.functions.fuli.model;

import io.realm.RealmObject;

/**
 * Created by Zj on 2015/12/28.
 */
public class ImageFuli extends RealmObject {

    public ImageFuli(String url) {
        this.url = url;
    }
    public ImageFuli(){

    }
    private String url;

    private int type  ;  // 0 表示gank，1表示豆瓣

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
