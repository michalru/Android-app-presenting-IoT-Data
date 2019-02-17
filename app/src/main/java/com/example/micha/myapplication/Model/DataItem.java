package com.example.micha.myapplication.Model;

import java.util.List;
//Klasa tymczasowa dla glownej kategorii(lokalizacja + pozycja)
public class DataItem {

    private String categoryName;
    private String isChecked = "NO";
    private List<SubCategoryItem> subCategory;


    public DataItem() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public List<SubCategoryItem> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(List<SubCategoryItem> subCategory) {
        this.subCategory = subCategory;
    }
}
