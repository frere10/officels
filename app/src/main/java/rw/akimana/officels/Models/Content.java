package rw.akimana.officels.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Content {
    private String id, title, detail, image, chapiterId, created, updated;
    private ArrayList<Comment> commentArrayList;

    public ArrayList<Comment> getCommentArrayList() {
        return commentArrayList;
    }

    public void setCommentArrayList(ArrayList<Comment> commentArrayList) {
        this.commentArrayList = commentArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String title) {
        this.detail = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getChapiterId() {
        return chapiterId;
    }

    public void setChapiterId(String chapiterId) {
        this.chapiterId = chapiterId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
