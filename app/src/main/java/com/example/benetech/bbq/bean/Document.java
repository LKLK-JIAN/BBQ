package com.example.benetech.bbq.bean;

import java.io.Serializable;

public class Document implements Serializable {
    private int id;                 //id
    private int channel;            //通道
    private String file_title;      //标题
    private String file_time;       //时间
    private String remarks;         //备注
    public boolean isSelect;


    public Document() {
    }


    public Document (String file_title, String file_time, String remarks) {
        this.file_title = file_title;
        this.file_time = file_time;
        this.remarks = remarks;
    }
    public Document(int id, String file_title, String file_time) {
        this.id = id;
        this.file_title = file_title;
        this.file_time = file_time;
    }


    public Document(int id, String file_title, String file_time, String remarks) {
        this.id = id;
        this.file_title = file_title;
        this.file_time = file_time;
        this.remarks = remarks;
    }

    public void setFile_title(String file_title) {
        this.file_title = file_title;
    }

    public void setFile_time(String file_time) {
        this.file_time = file_time;
    }

    public String getFile_title() {
        return file_title;
    }

    public String getFile_time() {
        return file_time;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
