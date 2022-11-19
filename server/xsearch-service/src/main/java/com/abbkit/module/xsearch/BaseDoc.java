package com.abbkit.module.xsearch;


import com.abbkit.kernel.model.Model;

/**
 * Created by J on 2020/3/22.
 */
public abstract class BaseDoc implements Doc, Model {

    private float score;

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public float score() {
        return getScore();
    }
}
