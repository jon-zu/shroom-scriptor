package org.example.fmt;

public record ItemId(int itemId, ItemStyle style) implements ShroomFmt {
    @Override
    public void append(StringBuilder sb) {
        switch (this.style) {
            case NAME -> sb.append("#t");
            case ICON -> sb.append("#i:");
            case ICON_NAME -> sb.append("#z:");
            case COUNT -> sb.append("#c:");
        }

        sb.append(this.itemId);
        sb.append("#");
    }

    public  enum ItemStyle {
        NAME,
        ICON,
        ICON_NAME,
        COUNT
    }
    public ItemId(int itemId) {
        this(itemId, ItemStyle.NAME);
    }

    public ItemId withIcon() {
        return new ItemId(this.itemId, ItemStyle.ICON);
    }

    public ItemId withCount() {
        return new ItemId(this.itemId, ItemStyle.COUNT);
    }

    public ItemId withIconText() {
        return new ItemId(this.itemId, ItemStyle.ICON_NAME);
    }
}
