package com.suygecu.teamly_service.teamly;


import service.teamly.PropertyOperation;

public final class PropertyOps {

    private PropertyOps() {}

    public static PropertyOperation add(String code, Object value) {
        PropertyOperation op = new PropertyOperation();
        op.setMethod("add");
        op.setCode(code);
        op.setValue(value);
        return op;
    }

    public static PropertyOperation update(String code, Object value) {
        PropertyOperation op = new PropertyOperation();
        op.setMethod("update");
        op.setCode(code);
        op.setValue(value);
        return op;
    }
}
