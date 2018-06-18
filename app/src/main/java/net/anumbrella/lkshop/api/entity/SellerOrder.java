package net.anumbrella.lkshop.api.entity;


public class SellerOrder {
    private int id;
    private String cartSerialNo;
    private String proJson;
    private double sum;
    private int sellerId;
    private int custId;
    private int status;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCartSerialNo() {
        return cartSerialNo;
    }

    public void setCartSerialNo(String cartSerialNo) {
        this.cartSerialNo = cartSerialNo;
    }

    public String getProJson() {
        return proJson;
    }

    public void setProJson(String proJson) {
        this.proJson = proJson;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
