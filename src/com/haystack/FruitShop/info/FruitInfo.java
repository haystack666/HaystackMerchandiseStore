package com.haystack.FruitShop.info;

public class FruitInfo {
    private String FruitId;
    private String FruitName;
    private String FruitType;
    private String FruitIntroduce;
    private String FruitPrice;
    private String FruitQuantity;

    public FruitInfo(String fruitId, String fruitName, String fruittype, String fruitIntroduce, String fruitPrice, String fruitQuantity) {
        this.FruitId = fruitId;
        this.FruitName = fruitName;
        this.FruitType = fruittype;
        this.FruitIntroduce = fruitIntroduce;
        this.FruitPrice = fruitPrice;
        this.FruitQuantity = fruitQuantity;
    }

    public FruitInfo() {

    }

    public String getFruitId() {
        return FruitId;
    }

    public void setFruitId(String fruitId) {
        FruitId = fruitId;
    }

    public String getFruitName() {
        return FruitName;
    }

    public void setFruitName(String fruitName) {
        FruitName = fruitName;
    }

    public String getFruitType() {
        return FruitType;
    }

    public void setFruitType(String fruittype) {
        FruitType = fruittype;
    }

    public String getFruitIntroduce() {
        return FruitIntroduce;
    }

    public void setFruitIntroduce(String fruitIntroduce) {
        FruitIntroduce = fruitIntroduce;
    }

    public String getFruitPrice() {
        return FruitPrice;
    }

    public void setFruitPrice(String fruitPrice) {
        FruitPrice = fruitPrice;
    }

    public String getFruitQuantity() {
        return FruitQuantity;
    }

    public void setFruitQuantity(String fruitQuantity) {
        FruitQuantity = fruitQuantity;
    }

    @Override
    public String toString() {
        return "FruitInfo{" +
                "FruitId=" + FruitId +
                ", FruitName='" + FruitName + '\'' +
                ", FruitType='" + FruitType + '\'' +
                ", FruitIntroduce='" + FruitIntroduce + '\'' +
                ", FruitPrice=" + FruitPrice +
                ", FruitQuantity=" + FruitQuantity +
                '}';
    }
}

