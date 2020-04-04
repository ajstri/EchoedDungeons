/*
Copyright 2020 EchoedAJ

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package utils;

import java.io.PrintStream;

/**
 *
 */
public class Logger {

    private static final PrintStream err = System.err;
    private static final PrintStream other = System.out;

    private static final String title = "[EchoedBot";

    /**
     *
     * @param message
     */
    public static void info(String message) {
        other.println(Constants.ANSI_BLUE + title + "/INFO] " + message + Constants.ANSI_RESET);
    }

    /**
     *
     * @param message
     */
    public static void warning(String message) {
        other.println(Constants.ANSI_YELLOW + title + "/WARNING]" + message + Constants.ANSI_RESET);
    }

    /**
     *
     * @param message
     * @param e
     */
    public static void error(String message, Exception e) {
        err.println(Constants.ANSI_RED + title + "/ERROR]" + message + Constants.ANSI_RESET);

        System.out.println("Here is the error: \n");
        e.getMessage();
        e.printStackTrace();
    }

    /**
     *
     * @param message
     * @param stage
     */
    public static void debug(String message, String stage) {
        other.println(Constants.ANSI_PURPLE + title + "/DEBUG/" + stage + "] " + message + Constants.ANSI_RESET);
    }

    /**
     * Logs a custom message
     * @param message message to log
     * @param header header of message, eg. "[EchoedBot/Utils]"
     * @param color color of message
     */
    public static void blank(String header, String color, String message) {
        other.println(color + header + message + Constants.ANSI_RESET);
    }
}
