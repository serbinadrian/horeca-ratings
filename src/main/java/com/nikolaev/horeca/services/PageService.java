package com.nikolaev.horeca.services;

import com.nikolaev.horeca.domains.Page;
import com.nikolaev.horeca.misc.PagerLabel;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PageService<T> {
    private List<PagerLabel> labels;
    private List<List<T>> pages;
    private List<T> data;
    private int currentPageIndex = 1;
    private int size = 0;
    private int pagesCount = 0;
    private int fullPagesCount = 0;
    private int lastPageDataCount = 0;
    private int pageSize = 0;

    public void construct(List<T> data, int pageSize) {
        this.data = data;
        this.pageSize = pageSize;
        if(!data.isEmpty()) {
            this.size = data.size();
            this.labels = new ArrayList<>();
            constructPages();
        }else{
            this.pagesCount = 0;
        }
    }

    public Page<T> getPage(int pageIndex){
        Page<T> page = new Page<>();
        if (this.data.size() <= this.pageSize) {
            page.setSingle(true);
        }
        if (this.data.isEmpty()) {
            page.setNull(true);
        }
        if(pageIndex > this.pagesCount || pageIndex == 0){
            return page;
        }else{
            calculateLabels(pageIndex);
            page.setElements(this.pages.get(this.currentPageIndex - 1));
            page.setLabels(this.labels);
        }
        return page;
    }

    private void calculatePageRatio() {
        this.fullPagesCount = this.size / this.pageSize;
        this.lastPageDataCount = this.size % this.pageSize;
    }

    private List<T> constructOnePage(int from, int pageSize){
        int index = from;
        List<T> page = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            page.add(this.data.get(index));
            index++;
        }
        return page;
    }

    private void calculateLabels(int pageIndex){
        this.currentPageIndex = pageIndex;
        boolean isBackButtonsAccessible = pageIndex != 1;
        boolean isForwardButtonsAccessible =  pageIndex != this.pagesCount;

        int labelsCount = 5;
        this.labels = new ArrayList<>();
        for (int i = 0; i < labelsCount; i++) {
            PagerLabel pagerLabel = null;
            switch (i) {
                case 0 -> pagerLabel = new PagerLabel(isBackButtonsAccessible, false, Integer.toString(1));
                case 1 -> pagerLabel = new PagerLabel(isBackButtonsAccessible, false, Integer.toString(this.currentPageIndex - 1));
                case 2 -> pagerLabel = new PagerLabel(false, true, Integer.toString(this.currentPageIndex));
                case 3 -> pagerLabel = new PagerLabel(isForwardButtonsAccessible, false, Integer.toString(this.currentPageIndex + 1));
                case 4 -> pagerLabel = new PagerLabel(isForwardButtonsAccessible, false, Integer.toString(this.pagesCount));
            }
            this.labels.add(pagerLabel);
        }
    }

    private void constructPages() {
        int index = 0;
        this.pages = new ArrayList<>();
        calculatePageRatio();
        if (this.fullPagesCount > 0) {
            for (int i = 0; i < this.fullPagesCount; i++) {
                this.pages.add(constructOnePage(index, this.pageSize));
                index += this.pageSize;
            }
            this.pagesCount = this.fullPagesCount;
        }
        if(this.lastPageDataCount > 0){
            this.pages.add(constructOnePage(index, this.lastPageDataCount));
            this.pagesCount++;
        }
    }
}
