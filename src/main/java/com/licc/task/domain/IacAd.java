package com.licc.task.domain;

import java.util.Date;

import com.jumore.dove.dao.anno.AutoIncrease;
import com.jumore.dove.dao.anno.Column;
import com.jumore.dove.dao.anno.Id;
import com.jumore.dove.dao.anno.Table;

import lombok.Data;

@Data
@Table(name = "iac_ad")
public class IacAd {
  @Id
  @AutoIncrease
  private Long id;
  @Column(name = "user_id")
  private Long userId;

  private String url;
  @Column(name = "praise_num")
  private Integer praiseNum;
  @Column(name = "browse_num")
  private Integer browseNum;
  private Integer status;
  @Column(name = "max_browse_num")
  private Integer maxBrowseNum;
  @Column(name = "max_praise_num")
  private Integer maxPraiseNum;
  @Column(name = "delete_flag")
  private Boolean deleteFlag;
  /**
   * 创建时间
   */
  @Column(name = "create_time")
  private Date createTime;
  /**
   * 修改时间
   */
  @Column(name = "update_time")
  private Date updateTime;
}
