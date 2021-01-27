package com.flash.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dairy")
public class Dairy {
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;
  private String title;
  private String description;
  private String content;

  @JsonFormat(pattern="yyyy-MM-dd")
  private LocalDateTime created;
}
