package com.example.cook.enums;

public enum KitchenstationType {
    SET("เซต"),
    RAMEN("ราเมง"),
    BENTO("เบนโตะ"),
    DRINKS("เครื่องดื่ม"),
    DESSERT("ของหวาน");

    private final String displayName;

    KitchenstationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static KitchenstationType fromDisplayName(String displayName) {
        for (KitchenstationType type : KitchenstationType.values()) {
            if (type.getDisplayName().equals(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant with displayName " + displayName);
    }

}