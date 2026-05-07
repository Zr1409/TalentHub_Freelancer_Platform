package com.backend.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeRemainingUtils {
    public static long calculateRemainingTimeInHours(Date endDate) {
        if (endDate == null) {
            return 0;
        }

        Date now = new Date();
        if (now.after(endDate)) {
            return 0;
        }

        long diffInMillis = endDate.getTime() - now.getTime();
        return TimeUnit.MILLISECONDS.toHours(diffInMillis);
    }


    public static String formatRemainingTime(long hours) {
        if (hours <= 0) {
            return "Đã hết hạn";
        }

        long days = hours / 24;
        long remainingHours = hours % 24;

        if (days > 0) {
            return days + " ngày " + remainingHours + " giờ";
        } else {
            return remainingHours + " giờ";
        }
    }

    public static String getFormattedTimeRemaining(Date endDate) {
        long hours = calculateRemainingTimeInHours(endDate);
        return formatRemainingTime(hours);
    }

    public static String getRelativeTimeFormatted(Date date) {
        if (date == null) {
            return "";
        }
        long now = System.currentTimeMillis();
        long timeCreated = date.getTime();
        long diff = now - timeCreated;

        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);
        long diffWeeks = diffDays / 7;
        long diffMonths = diffDays / 30;
        long diffYears = diffDays / 365;

        if (diffMinutes < 1) {
            return "vừa xong";
        } else if (diffMinutes < 60) {
            return diffMinutes + " phút trước";
        } else if (diffHours < 24) {
            return diffHours + " giờ trước";
        } else if (diffDays < 7) {
            return diffDays + " ngày trước";
        } else if (diffDays < 30) {
            return diffWeeks + " tuần trước";
        } else if (diffMonths < 12) {
            return (diffMonths > 0 ? diffMonths : 1) + " tháng trước";
        } else {
            return diffYears + " năm trước";
        }
    }
}