package com.example.folder.controller;

import com.example.common.R;
import com.example.domain.Document;
import com.example.domain.Folder;
import com.example.domain.FolderWithDoc;
import com.example.folder.service.FolderService;
import com.example.folder.service.FolderWithDocService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "FolderController:文件夹控制器")
@RestController
@Slf4j
@RequestMapping("/favorite")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private FolderWithDocService folderWithDocService;

    @Operation(summary = "根据account获取收藏夹名称列表")
    @GetMapping("/folders")
    public R<List<String>> getFolderNamesByAccount(@RequestParam("account") String account) {
        log.info("获取收藏夹名称列表，account={}", account);
        List<String> folderNames = folderService.getFolderNamesByAccount(account);

        R<List<String>> result = R.success();
        result.setData(folderNames);
        return result;
    }

    @Operation(summary = "获取某个收藏夹的全部文档信息")
    @GetMapping("/list")
    public R<List<Document>> getFolderWithDocList(@RequestParam("account") String account,
                                                  @RequestParam("folder") String folderName) {

        log.info("获取某个收藏夹的全部文档信息，account={}, folderName={}", account, folderName);

        List<Document> documentList = folderService.getDocumentListByFolderNameAndAccount(account, folderName);

        return R.success(documentList);

    }

    @Operation(summary = "创建收藏夹")
    @PostMapping("/create")
    public R<String> createFolder(@RequestBody Folder folder) {
         log.info("创建收藏夹，folder={}", folder);
         folderService.save(folder);
         return R.success("收藏夹创建成功");
    }

    @Operation(summary = "删除收藏夹")
    @DeleteMapping("/delete")
    public R<String> deleteFolder(@RequestBody Folder folder) {
         log.info("删除收藏夹，folder={}", folder);
         folderService.removeByFolderNameAndAccount(folder.getFolderName(), folder.getAccount());
         return R.success("收藏夹删除成功");
    }

    @Operation(summary = "取消收藏文档")
    @DeleteMapping("/cancel")
    public R<String> cancelFavorite(@RequestBody FolderWithDoc folderWithDoc){
        log.info("取消收藏文档，folderWithDoc={}", folderWithDoc);
        folderWithDocService.removeByDocIdAndFolderId(folderWithDoc.getDocumentId(), folderWithDoc.getFolderId());
        return R.success("取消收藏成功");
    }

    @Operation(summary = "收藏文档")
    @PostMapping("/star")
    public R<String> favorite(@RequestBody Map<String, Object> map){
        log.info("收藏文档:{}", map);
        Integer documentId = (Integer) map.get("documentId");
        String folderName = (String) map.get("folderName");
        String account = (String) map.get("account");

        Integer folderId = folderService.getIdByFolderNameAndAccount(folderName, account);

        FolderWithDoc folderWithDoc = new FolderWithDoc();
        folderWithDoc.setDocumentId(documentId);
        folderWithDoc.setFolderId(folderId);
        folderWithDocService.save(folderWithDoc);
        return R.success("收藏成功");
    }

}
