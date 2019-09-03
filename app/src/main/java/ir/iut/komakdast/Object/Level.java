package ir.iut.komakdast.Object;

import android.content.Context;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Level {

    @Expose
    private int id;
    @Expose
    private boolean resolved;
    @Expose
    private String type;
    @Expose
    private String video;
    @Expose
    private String pics;
    @Expose
    private String answer;


    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideoPath(int packageId, Context context) {
        return "file://" + context.getFilesDir().getPath() + "/Packages/package_" + packageId + "/" + id + "/"
                + video;

    }

    public ArrayList<String> getImagesPath(int packageId, Context context) {

        ArrayList<String> list = new ArrayList<String>();

        for (String pic : pics.split(",")) {

            String path =
                    "file://" + context.getFilesDir().getPath() + "/Packages/package_" + packageId + "/" + id + "/"
                            + pic;

            list.add(path);
        }

        return list;

    }

    public String getAnswerImgPath(int packageId, Context context) {
        if (type.equals("4pics"))
            return "file://" + context.getFilesDir().getPath() + "/Packages/package_" + packageId + "/" + id + "/"
                    + answer;
        else
            return "file://" + context.getFilesDir().getPath() + "/Packages/package_" + packageId + "/" + id + "/"
                    + pics;

    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
