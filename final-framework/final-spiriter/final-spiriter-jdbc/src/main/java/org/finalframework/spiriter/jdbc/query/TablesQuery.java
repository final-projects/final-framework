package org.ifinal.finalframework.spiriter.jdbc.query;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author likly
 * @version 1.0.0
 *
 * @since 1.0.0
 */
@Data
public class TablesQuery {
    @NotEmpty
    private String schema;
}

