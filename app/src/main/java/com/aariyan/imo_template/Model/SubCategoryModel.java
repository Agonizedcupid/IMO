package com.aariyan.imo_template.Model;

public class SubCategoryModel {
    private String id,parentId,subCategoryName;
    public SubCategoryModel(){}

    public SubCategoryModel(String id, String parentId, String subCategoryName) {
        this.id = id;
        this.parentId = parentId;
        this.subCategoryName = subCategoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    @Override
    public String toString() {
        return subCategoryName;
    }
}
