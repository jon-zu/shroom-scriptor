package org.example.fmt;

public record Text(String txt) implements ShroomFmt{
    @Override
    public void append(StringBuilder sb) {
        if(this.txt.isEmpty())
            return;

        var iter = txt.lines().iterator();
        sb.append(iter.next());

        while(iter.hasNext()) {
            sb.append("\r\n");
            sb.append(iter.next());
        }
    }
}
