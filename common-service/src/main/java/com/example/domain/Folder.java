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
@TableName("tbl_folder")
public class Folder {

    @Schema(description = "文件夹ID")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @Schema(description = "文件夹名称")
    private String folderName;

    @Schema(description = "文件夹所属账号")
    private String account;

}