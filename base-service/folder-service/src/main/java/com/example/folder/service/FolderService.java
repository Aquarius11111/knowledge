package com.example.folder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.Document;
import com.example.domain.Folder;

import java.util.List;

public interface FolderService extends IService<Folder> {
    List<String> getFolderNamesByAccount(String account);

    List<Document> getDocumentListByFolderNameAndAccount(String account, String folderName);

    void removeByFolderNameAndAccount(String folderName, String account);
}
