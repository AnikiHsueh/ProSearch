package net.anumbrella.lkshop.api.entity;


public class Pro {
    private int id;
    private int indirectCateId;
    private String indirectCateName;
    private int directCateId;
    private String directCateName;
    private String name;
    private String detail;
    private String price;
    private int quantity;
    private String picture;
    private int isOnSale;
    private String createTime;
    private int creator;
    private String lastModifyTime;
    private int lastModifier;
    private String serialNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndirectCateId() {
        return indirectCateId;
    }

    public void setIndirectCateId(int indirectCateId) {
        this.indirectCateId = indirectCateId;
    }

    public String getIndirectCateName() {
        return indirectCateName;
    }

    public void setIndirectCateName(String indirectCateName) {
        this.indirectCateName = indirectCateName;
    }

    public int getDirectCateId() {
        return directCateId;
    }

    public void setDirectCateId(int directCateId) {
        this.directCateId = directCateId;
    }

    public String getDirectCateName() {
        return directCateName;
    }

    public void setDirectCateName(String directCateName) {
        this.directCateName = directCateName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getIsOnSale() {
        return isOnSale;
    }

    public void setIsOnSale(int isOnSale) {
        this.isOnSale = isOnSale;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public int getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(int lastModifier) {
        this.lastModifier = lastModifier;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
}
