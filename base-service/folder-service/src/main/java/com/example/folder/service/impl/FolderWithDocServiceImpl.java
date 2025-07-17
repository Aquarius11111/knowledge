package com.example.folder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.FolderWithDoc;
import com.example.folder.service.FolderWithDocService;
import com.example.mapper.FolderWithDocMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FolderWithDocServiceImpl extends ServiceImpl<FolderWithDocMapper, FolderWithDoc> implements FolderWithDocService {
    @Override
    public void removeByDocIdAndFolderId(Integer documentId, Integer folderId) {
        LambdaQueryWrapper<FolderWithDoc> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FolderWithDoc::getDocumentId, documentId).eq(FolderWithDoc::getFolderId, folderId);
        this.remove(wrapper);
    }

    @Override
    public void removeByFolderId(Integer id) {
        LambdaQueryWrapper<FolderWithDoc> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FolderWithDoc::getFolderId, id);
        this.remove(wrapper);
    }
}
