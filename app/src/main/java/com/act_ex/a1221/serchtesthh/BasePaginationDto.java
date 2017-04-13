package com.act_ex.a1221.serchtesthh;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by brost_host17 on 13.04.2017.
 */

public class BasePaginationDto<VacancyDto> {
    @SerializedName("per_page")
    private Integer perPage;

    @SerializedName("page")
    private Integer page;

    @SerializedName("pages")
    private Integer pages;

    @SerializedName("found")
    private Integer found;

    @SerializedName("items")
    private List<VacancyDto> items;

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getFound() {
        return found;
    }

    public void setFound(Integer found) {
        this.found = found;
    }

    public List<VacancyDto> getItems() {
        return items;
    }

    public void setItems(List<VacancyDto> items) {
        this.items = (List<VacancyDto>) items;
    }
}
