package com.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("tbl_document")
public class Document {
    @TableId(type= IdType.AUTO)
    private Integer documentId;
    private String title;
    private String category;
    private String summary;
    private String content;
    private String author;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDateTime publishTime;
    private Integer views;
    private Integer likes;
    private Integer status;
    private String tags;

    @TableField(exist = false)
    private List<String> tagList;
}
