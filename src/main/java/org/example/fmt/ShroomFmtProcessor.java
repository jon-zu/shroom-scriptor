package org.example.fmt;

import java.util.Iterator;

public class ShroomFmtProcessor implements StringTemplate.Processor<String, IllegalArgumentException> {
    @Override
    public String process(StringTemplate st) throws IllegalArgumentException {
        StringBuilder sb = new StringBuilder();
        Iterator<String> fragmentsIter = st.fragments().iterator();
        for(Object value : st.values()) {
            new Text(fragmentsIter.next()).append(sb);

            if(value instanceof ShroomFmt)
                ((ShroomFmt) value).append(sb);
            else
                throw new IllegalArgumentException("Value must be a ShroomFmt");
        }


        return sb.toString();
    }
}
