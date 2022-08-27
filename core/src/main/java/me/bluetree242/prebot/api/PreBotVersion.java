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

package me.bluetree242.prebot.api;

/**
 * This class contains fields with current PreBot version information
 */
public class PreBotVersion {
    /**
     * The major version of prebot, this is the 1st part of the version (part with dots)
     */
    public static final String VERSION_MAJOR = "@versionMajor@";
    /**
     * The minor version of prebot, this is the 2nd part of the version (part with dots)
     */
    public static final String VERSION_MINOR = "@versionMinor@";
    /**
     * The revision of prebot, this is the 3rd part of the version (part with dots)
     */
    public static final String VERSION_REVISION = "@versionRevision@";
    /**
     * The classifier of the version, this part is after the parts with dots.
     */
    public static final String VERSION_CLASSIFIER = "@versionClassifier@";
    /**
     * The commit of this version, or LOCAL if it's not built by a CI
     */
    public static final String COMMIT_HASH = "@commit@";
    /**
     * The full PreBot version, this might contain -SNAPSHOT at the end.
     */
    public static final String VERSION = "@version@";
}
