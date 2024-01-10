package it.lanos.eventbuddy.util;

import android.content.Context;

import it.lanos.eventbuddy.R;

public class Parser {
    public static String formatDate(String date, Context context){
        String[] dateTime =date.split("/");
        String month;
        switch(dateTime[1]){
            case "01": {
                month = context.getString(R.string.january);
                break;
            }
            case "02": {
                month = context.getString(R.string.february);
                break;
            }
            case "03": {
                month = context.getString(R.string.march);
                break;
            }
            case "04": {
                month = context.getString(R.string.april);
                break;
            }
            case "05": {
                month = context.getString(R.string.may);
                break;
            }
            case "06": {
                month = context.getString(R.string.june);
                break;
            }
            case "07": {
                month = context.getString(R.string.july);
                break;
            }
            case "08": {
                month = context.getString(R.string.august);
                break;
            }
            case "09": {
                month = context.getString(R.string.september);
                break;
            }
            case "10": {
                month = context.getString(R.string.october);
                break;
            }
            case "11": {
                month = context.getString(R.string.november);
                break;
            }
            case "12": {
                month = context.getString(R.string.december);
                break;
            }
            default:
                month = "";
        }
        if(dateTime[2].length() == 2)
            dateTime[2] = "20"+dateTime[2];

        date = month+", "+dateTime[0]+" "+dateTime[2];
        return date;
    }

    public static String formatTime(String date){
        String[] dateTime = date.split("/");
        return dateTime[3];
    }

    public static String formatLocation(String location) {
        String firstSplit = location.split("/")[0];
        String[] secondSplit = firstSplit.split(",");
        String showLocation = secondSplit[0]+",\n"+secondSplit[1];
        return showLocation;
    }

    public static double[] getCord(String location) {
        String firstSplit = location.split("/")[1];
        String[] secondSplit = firstSplit.split("_");
        double[] cord = new double[2];
        cord[0] = Double.parseDouble(secondSplit[0]);
        cord[1] = Double.parseDouble(secondSplit[1]);

        return cord;

    }

    public static String formatSortDate(String date) {
        String[] splitDate = date.split("/");
        String formattedDate = "20"+splitDate[2]+"-"+splitDate[1]+"-"+splitDate[0]+" "+splitDate[3];
        return formattedDate;
    }

    public static String formatDateForEventList(Context context, String date){
        String[] dateTime =date.split("/");
        String month;
        switch(dateTime[1]){
            case "01": {
                month = context.getString(R.string.jan_abr);
                break;
            }
            case "02": {
                month = context.getString(R.string.feb_abr);
                break;
            }
            case "03": {
                month = context.getString(R.string.march_abr);
                break;
            }
            case "04": {
                month = context.getString(R.string.apr_abr);
                break;
            }
            case "05": {
                month = context.getString(R.string.may_abr);
                break;
            }
            case "06": {
                month = context.getString(R.string.june_abr);
                break;
            }
            case "07": {
                month = context.getString(R.string.july_abr);
                break;
            }
            case "08": {
                month = context.getString(R.string.aug_abr);
                break;
            }
            case "09": {
                month = context.getString(R.string.sept_abr);
                break;
            }
            case "10": {
                month = context.getString(R.string.oct_abr);
                break;
            }
            case "11": {
                month = context.getString(R.string.nov_abr);
                break;
            }
            case "12": {
                month = context.getString(R.string.dec_abr);
                break;
            }
            default:
                month = "";
        }
        return dateTime[0]+"\n"+month;
    }
}
