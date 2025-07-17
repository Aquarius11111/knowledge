package com.example.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tbl_folder_with_doc")
public class FolderWithDoc{

    @Schema(description = "关系ID")
    @TableId(type = IdType.AUTO)
    private Integer relationId;

    @Schema(description = "文档ID")
    private Integer documentId;

    @Schema(description = "文件夹ID")
    private Integer folderId;
}
