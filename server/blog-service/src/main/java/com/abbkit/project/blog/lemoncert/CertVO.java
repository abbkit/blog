package com.abbkit.project.blog.lemoncert;

import com.abbkit.kernel.model.Model;
import lombok.Data;

/**
 * Created by J on 2020/3/21.
 */
@Data
public class CertVO implements Model {

    private String cert;

    private String expired;

}
