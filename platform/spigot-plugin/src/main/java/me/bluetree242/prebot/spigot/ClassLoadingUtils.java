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

package me.bluetree242.prebot.spigot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassLoadingUtils {

    public static Class<?> getClassThisPlugin(String name, boolean resolve) throws ClassNotFoundException{
        ClassLoader loader = ClassLoadingUtils.class.getClassLoader();
        try {
            Method spigotMethod = loader.getClass().getDeclaredMethod("loadClass0", Boolean.class, Boolean.class, Boolean.class);
            return (Class<?>) spigotMethod.invoke(name, resolve, false, true);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return loader.loadClass(name);
        }

    }
}
