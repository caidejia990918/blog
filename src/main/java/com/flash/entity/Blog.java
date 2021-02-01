package com.flash.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("blog")
public class Blog {
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;
  private String title;
  private String writer;

  private String description;
  public String content;
  @JsonFormat(pattern="yyyy-MM-dd HH:mm")
  private LocalDateTime created;
  private String picture;
  private Long watch;
  private Long comment;
  private String text;
  private String category;


}
