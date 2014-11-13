package me.jathusan.wheelzo.util;

import java.text.DecimalFormat;

public class FormatUtil {

    public static String formatDate(String date) {
        String[] parts = date.split(" ");
        String newDate = parts[0];
        String newTime = parts[1];

        parts = newDate.split("-");
        String year = parts[0];
        String month = getMonthForNumber(Integer.parseInt(parts[1]));
        String day = parts[2];

        newTime = convertToStandardTime(newTime);

        return month + " " + day + ", " + year + " at " + newTime;
    }

    public static String formatDollarAmount(double price) {
        if (price == 0.0){
            return "  Free  ";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        return "$" + df.format(price);
    }

    public static String convertToStandardTime(String time) {

        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        String minutes = parts[1];

        if (hour > 12) {
            return (24 - hour) + ":" + minutes + " pm";
        }
        return hour + ":" + minutes + " am";
    }

    public static String getMonthForNumber(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "N/A";
        }
    }

}
