package com.example.benetech.bbq.basesqlite;

import android.database.Cursor;

import com.example.benetech.bbq.bean.Document;

public interface DocumentDao {

    void insertDocument(Document ci);
    void deleteDocument(Integer id);
    void modifiyDocument(Document ci);
    Cursor getAllDocument(Integer channel);
    Document getDocument(Integer id);
    Document getTopDocument();
}
