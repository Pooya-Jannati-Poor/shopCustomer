package ir.arinateam.shopcustomer.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumbersSeparator {

    public String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
        formatter.applyPattern("#,###.##");
        return formatter.format(d);
    }

}
