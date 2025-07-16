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
@TableName("tbl_category")
public class Category {

    @Schema(description = "类别ID")
    @TableId(type = IdType.AUTO)
    private Integer ctgId;
    @Schema(description = "类别名称")
    private String ctgName;

    @Schema(description = "类别描述")
    @TableField(fill = FieldFill.INSERT)
    private String ctgDesc;

    @Schema(description = "父类别ID")
    @TableField(fill = FieldFill.INSERT)
    private Integer ctgParentId;

    @Schema(description = "类别是否启用")
    @TableField(fill = FieldFill.INSERT)
    private Boolean ctgEnabled;

    @Schema(description = "类别创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date ctgCreatedTime;

    @Schema(description = "该类别下文件数量")
    private Integer ctgDocCount;


    @TableField(exist = false)
    private List<Document> documentList;

}