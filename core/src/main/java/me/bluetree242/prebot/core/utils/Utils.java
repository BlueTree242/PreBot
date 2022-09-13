/*
 * LICENSE
 * PreBot
 * -------------
 * Copyright (C) 2022 - 2022 BlueTree242
 * -------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END
 */

package me.bluetree242.prebot.core.utils;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String fileseparator() {
        return System.getProperty("file.separator");
    }

    public static String trim(String s, int limit) {
        if (s.length() >= 100) {
            int len = 0;
            StringBuilder returnvalue = new StringBuilder();
            for (String l : s.split("")) {
                len++;
                if (len >= (limit - 3) && len <= limit) {
                    returnvalue.append(".");
                } else {
                    if (len <= limit)
                        returnvalue.append(l);
                }
            }
            return returnvalue.toString();
        }
        return s;
    }

    public static String getTime(long ms) {
        String val = "";
        long remaining = ms;
        long days = TimeUnit.MILLISECONDS.toDays(ms);
        remaining = remaining - TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(remaining);
        remaining = remaining - TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining);
        remaining = remaining - TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(remaining);
        if (days != 0) {
            val = days + " day" + (days <= 1 ? "" : "s");
        }
        if (hours != 0) {
            if (val.equals("")) {
                val = hours + " hour" + (hours <= 1 ? "" : "s");
            } else {
                val = val + ", " + hours + " hour" + (hours <= 1 ? "" : "s");
            }
        }
        if (minutes != 0) {
            if (val.equals("")) {
                val = minutes + " minute" + (minutes <= 1 ? "" : "s");
            } else {
                val = val + ", " + minutes + " minute" + (minutes <= 1 ? "" : "s");
            }
        }
        if (seconds != 0) {
            if (val.equals("")) {
                val = seconds + " second" + (seconds <= 1 ? "" : "s");
            } else {
                val = val + ", " + seconds + " second" + (seconds <= 1 ? "" : "s");
            }
        }
        if (val.equals("")) {
            return "Less than a second";
        }
        return replaceLast(val, ", ", " and ");
    }

    public static String replaceLast(String input, String regex, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            return input;
        }
        int lastMatchStart;
        do {
            lastMatchStart = matcher.start();
        } while (matcher.find());
        matcher.find(lastMatchStart);
        StringBuffer sb = new StringBuffer(input.length());
        matcher.appendReplacement(sb, replacement);
        matcher.appendTail(sb);
        return sb.toString();
    }
}
