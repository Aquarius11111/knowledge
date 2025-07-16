package com.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "文档类别返回DTO")
public class CategoryDTO {
    @Schema(description = "类别ID")
    private Integer id;

    @Schema(description = "类别名称")
    private String name;

    @Schema(description = "类别描述")
    private String description;

    @Schema(description = "状态：1启用，0禁用")
    private Integer status; // 转换 Boolean 为 Integer

    @Schema(description = "文档数量")
    private Integer documentCount;

    @Schema(description = "创建时间")
    private Date createTime;
}