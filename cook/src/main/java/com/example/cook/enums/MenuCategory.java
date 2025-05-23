package com.example.cook.enums;

public enum MenuCategory {
    SET("เซต"),
    RAMEN("ราเมง"),
    BENTO("เบนโตะ"),
    DRINKS("เครื่องดื่ม"),
    DESSERT("ของหวาน");

    private final String displayName;

    MenuCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static MenuCategory fromDisplayName(String displayName) {
        for (MenuCategory category : MenuCategory.values()) {
            if (category.getDisplayName().equals(displayName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No enum constant with displayName " + displayName);
    }

}
