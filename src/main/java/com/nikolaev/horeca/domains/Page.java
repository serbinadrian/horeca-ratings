package com.nikolaev.horeca.domains;

import com.nikolaev.horeca.misc.PagerLabel;

import java.util.List;

public class Page<T> {
    private List<T> elements;
    private List<PagerLabel> labels;
    private boolean isNull;
    private boolean isSingle;

    public List<T> getElements() {
        return elements;
    }

    public List<PagerLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<PagerLabel> labels) {
        this.labels = labels;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public void setSingle(boolean single) {
        isSingle = single;
    }
}
