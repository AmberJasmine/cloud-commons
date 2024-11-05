package org.example.until.system;

/**
 * @author jiangxin
 * @create 2022-01-19-16:40
 */
public class OsSelect {

    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}