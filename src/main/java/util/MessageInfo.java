package util;

/*
    Author:leia
    Write The Code Change The World    
*/public class MessageInfo {
    int id;
    String title;
    String writeTime;

    public MessageInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(String writeTime) {
        this.writeTime = writeTime;
    }

    public MessageInfo(int id, String title, String writeTime) {
        this.id = id;
        this.title = title;
        this.writeTime = writeTime;
    }
}
