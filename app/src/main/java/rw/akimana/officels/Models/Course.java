package rw.akimana.officels.Models;

import java.util.Date;

public class Course {
    private String id, name, assNum, chapNum, weight, created, updated;

    public String getId() {
        return id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssNum() {
        return assNum;
    }

    public void setAssNum(String assNum) {
        this.assNum = assNum;
    }

    public String getChapNum() {
        return chapNum;
    }

    public void setChapNum(String chapNum) {
        this.chapNum = chapNum;
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
