
package com.company.inputoutput;

import java.util.List;

public class ToStringHelper {

    private String separator;

    public ToStringHelper(String separator) {
        this.separator = separator;
    }

   public String toString(List<?> l) {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for (Object object : l) {
            sb.append(sep).append(object.toString());
            sep = separator;
        }
        return sb.toString();
    }

}
