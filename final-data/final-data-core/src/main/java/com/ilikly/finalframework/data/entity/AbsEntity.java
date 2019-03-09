package com.ilikly.finalframework.data.entity;

import com.ilikly.finalframework.data.annotation.Column;
import com.ilikly.finalframework.data.annotation.CreatedTime;
import com.ilikly.finalframework.data.annotation.LastModifiedTime;
import com.ilikly.finalframework.data.annotation.PrimaryKey;
import com.ilikly.finalframework.data.entity.enums.YN;
import lombok.Data;

import java.util.Date;

/**
 * @author likly
 * @version 1.0
 * @date 2018-10-20 17:17
 * @since 1.0
 */
@Data
public abstract class AbsEntity implements IEntity<Long> {

    private static final long serialVersionUID = -3500516904657883963L;

    @PrimaryKey
    private Long id;
    @CreatedTime
    private Date createdTime;
    @LastModifiedTime
    private Date lastModifiedTime;
    @Column(insertable = false)
    private YN yn;
}

    


