package org.finalframework.data.entity;

import lombok.Data;
import org.finalframework.data.annotation.Column;
import org.finalframework.data.annotation.Created;
import org.finalframework.data.annotation.LastModified;
import org.finalframework.data.annotation.PrimaryKey;
import org.finalframework.data.entity.enums.YN;

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
    @Created
    private Date createdTime;
    @LastModified
    private Date lastModifiedTime;
    @Column(insertable = false)
    private YN yn;
}

    


