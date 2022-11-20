package com.abbkit.module.xsearch;


import com.abbkit.kernel.model.Model;
import lombok.Data;

/**
 * Created by J on 2020/3/22.
 */
@Data
public abstract class BaseDoc implements Doc, Model {

    private float score;

    @Override
    public float score() {
        return getScore();
    }
}
