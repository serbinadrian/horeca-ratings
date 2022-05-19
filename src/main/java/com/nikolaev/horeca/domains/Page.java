package com.nikolaev.horeca.domains;

import com.nikolaev.horeca.misc.PagerLabel;

import java.util.List;

public class Page<T> {
    private List<T> elements;
    private List<PagerLabel> labels;

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
}
