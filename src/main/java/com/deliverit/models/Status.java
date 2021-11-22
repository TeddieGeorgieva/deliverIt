package com.deliverit.models;

public enum Status {
    PREPARING, ON_THE_WAY, COMPLETED;

    @Override
    public String toString() {
        switch (this) {
            case PREPARING:
                return "Preparing";
            case ON_THE_WAY:
                return "On the way";
            case COMPLETED:
                return "Completed";
            default:
                return "";
        }
    }
}
