package com.shoaibnwar.hotelandrestaurant.DataseHelper;

/**
 * Created by gold on 7/13/2018.
 */

public class OrderFoodDbHelper {

    public String getId() {
        return id;
    }

    public String getTrackinId() {
        return trackinId;
    }

    public String getItemname() {
        return itemname;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public String getQuantities() {
        return quantities;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getDetail() {
        return detail;
    }

    public String getOrderSumPrice() {
        return orderSumPrice;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTrackinId(String trackinId) {
        this.trackinId = trackinId;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setQuantities(String quantities) {
        this.quantities = quantities;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setOrderSumPrice(String orderSumPrice) {
        this.orderSumPrice = orderSumPrice;
    }

    String id;
    String trackinId;
    String itemname;
    String unitPrice;
    String quantities;
    String totalPrice;
    String detail;
    String orderSumPrice;

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    String currentTime;

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    String currentDate;

    public String getAssignStatus() {
        return assignStatus;
    }

    public void setAssignStatus(String assignStatus) {
        this.assignStatus = assignStatus;
    }

    String assignStatus;

    public String getItemImagePosition() {
        return itemImagePosition;
    }

    public void setItemImagePosition(String itemImagePosition) {
        this.itemImagePosition = itemImagePosition;
    }

    String itemImagePosition;

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    String roomNumber;

    public String getRoomNumber() {
        return roomNumber;
    }
}
