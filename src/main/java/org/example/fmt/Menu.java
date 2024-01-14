package org.example.fmt;

import java.util.List;

public record Menu<T extends ShroomFmt>(Text header, List<T> items, boolean inline) implements ShroomFmt {
    public Menu(Text header, List<T> items) {
        this(header, items, false);
    }

    public Menu(List<T> items) {
        this(null, items, false);
    }

    @Override
    public void append(StringBuilder sb) {
        final String NEW_LINE = "\r\n";
        if (this.header != null) {
            this.header.append(sb);
            if (!this.inline)
                sb.append(NEW_LINE);
        }

        for (int i = 0; i < this.items.size(); i++) {
            sb.append("#L");
            sb.append(i);
            sb.append("#");
            this.items.get(i).append(sb);
            sb.append("#l");

            if (!this.inline)
                sb.append(NEW_LINE);
        }
    }
}
