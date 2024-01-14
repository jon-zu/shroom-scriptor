package org.example.fmt;

public record Mob (int id) implements ShroomFmt {
    @Override
    public void append(StringBuilder sb) {
        sb.append("#o");
        sb.append(this.id);
        sb.append("#");
    }
}
