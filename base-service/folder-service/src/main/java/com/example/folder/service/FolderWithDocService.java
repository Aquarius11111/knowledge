package com.example.folder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.FolderWithDoc;

public interface FolderWithDocService extends IService<FolderWithDoc> {
    void removeByDocIdAndFolderId(Integer documentId, Integer folderId);

    void removeByFolderId(Integer id);
}
