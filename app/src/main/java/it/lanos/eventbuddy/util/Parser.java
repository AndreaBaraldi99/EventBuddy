package it.lanos.eventbuddy.util;

public class Parser {
    public static String formatDate(String date){
        String[] dateTime =date.split("/");
        String month;
        switch(dateTime[1]){
            case "01": {
                month = "January";
                break;
            }
            case "02": {
                month = "February";
                break;
            }
            case "03": {
                month = "March";
                break;
            }
            case "04": {
                month = "April";
                break;
            }
            case "05": {
                month = "May";
                break;
            }
            case "06": {
                month = "June";
                break;
            }
            case "07": {
                month = "July";
                break;
            }
            case "08": {
                month = "August";
                break;
            }
            case "09": {
                month = "September";
                break;
            }
            case "10": {
                month = "October";
                break;
            }
            case "11": {
                month = "November";
                break;
            }
            case "12": {
                month = "December";
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
}
