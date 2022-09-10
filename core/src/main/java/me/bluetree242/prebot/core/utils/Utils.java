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

}
