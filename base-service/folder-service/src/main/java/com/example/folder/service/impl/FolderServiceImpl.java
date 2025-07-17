package com.example.folder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.domain.Document;
import com.example.domain.Folder;
import com.example.domain.FolderWithDoc;
import com.example.folder.feign.DocumentFeignClient;
import com.example.folder.service.FolderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.folder.service.FolderWithDocService;
import com.example.mapper.FolderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class FolderServiceImpl extends ServiceImpl<FolderMapper, Folder> implements FolderService {

    @Autowired
    private FolderWithDocService folderWithDocService;

    @Autowired
    private DocumentFeignClient documentFeignClient;

    @Override
    public List<String> getFolderNamesByAccount(String account) {
        LambdaQueryWrapper<Folder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Folder::getAccount, account);
        List<Folder> folders = this.list(queryWrapper);
        return folders.stream().map(Folder::getFolderName).toList();
    }

    @Override
    public List<Document> getDocumentListByFolderNameAndAccount(String account, String folderName) {
        LambdaQueryWrapper<Folder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Folder::getAccount, account);
        queryWrapper.eq(Folder::getFolderName, folderName);
        Folder folder = this.getOne(queryWrapper);
        if (folder == null) {
            return null;
        }

        Integer folderId = folder.getId();
        LambdaQueryWrapper<FolderWithDoc> folderWithDocQueryWrapper = new LambdaQueryWrapper<>();
        folderWithDocQueryWrapper.eq(FolderWithDoc::getFolderId, folderId);
        List<FolderWithDoc> folderWithDocs = folderWithDocService.list(folderWithDocQueryWrapper);

        List<Integer> documentIds = folderWithDocs.stream().map(FolderWithDoc::getDocumentId).toList();
        if (documentIds.isEmpty()) {
            return null;
        }

        LambdaQueryWrapper<Document> documentQueryWrapper = new LambdaQueryWrapper<>();
        documentQueryWrapper.in(Document::getDocumentId, documentIds);
        List<Document> documents = null;
        for (Integer documentId : documentIds) {
            Document document = documentFeignClient.getDocumentById(documentId).getData();
            documents.add(document);
        }

        return documents;

    }

    @Override
    public void removeByFolderNameAndAccount(String folderName, String account) {
        LambdaQueryWrapper<Folder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Folder::getAccount, account);
        queryWrapper.eq(Folder::getFolderName, folderName);
        Folder folder = this.getOne(queryWrapper);

        folderWithDocService.removeByFolderId(folder.getId());

        this.removeById(folder.getId());

    }

    @Override
    public Integer getIdByFolderNameAndAccount(String folderName, String account) {
        LambdaQueryWrapper<Folder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Folder::getAccount, account);
        queryWrapper.eq(Folder::getFolderName, folderName);
        Folder folder = this.getOne(queryWrapper);
        if (folder == null) {
            return null;
        }
        return folder.getId();
    }
}
