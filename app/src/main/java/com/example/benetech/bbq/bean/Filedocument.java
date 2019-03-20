package com.example.benetech.bbq.bean;

public class Filedocument {
    private String FileName;
    private String FilePath;
    private Boolean isSelected=false;


    public Filedocument(String fileName, String filePath) {
        FileName = fileName;
        FilePath = filePath;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
