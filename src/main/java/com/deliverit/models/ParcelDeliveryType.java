package com.deliverit.models;

public enum ParcelDeliveryType {
    DELIVER_TO_ADDRESS, PICK_UP_FROM_WAREHOUSE;

    @Override
    public String toString() {
        switch (this) {
            case DELIVER_TO_ADDRESS:
                return "Deliver_to_address";
            case PICK_UP_FROM_WAREHOUSE:
                return "Pick_up_from_warehouse";
            default:
                return "";
        }
    }
}
