package com.enterprise.docqa.common.dto;

public record StandardResponse(String status, String message, Object payload) {
    public static StandardResponse ok(String message, Object payload) {
        return new StandardResponse("OK", message, payload);
    }

    public static StandardResponse error(String message) {
        return new StandardResponse("ERROR", message, null);
    }
}
