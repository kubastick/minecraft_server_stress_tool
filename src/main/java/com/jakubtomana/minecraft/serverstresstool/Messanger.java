package com.jakubtomana.minecraft.serverstresstool;
class Color{
    public static final String WHITE="[37m";
    public static final String GREEN="[32m";
    public static final String RED="[31m";
}
/** Class used to easly port this tool output to other platforms */
public class Messanger {

    private final static boolean ENABLE_COLOR = true;
    protected static void println(String msg)
    {
        System.out.println(msg);
    }
    protected static void println(String msg,String color)
    {
        if(ENABLE_COLOR)
                print((char) 27 + color);
        System.out.println(msg);
        if(ENABLE_COLOR)
                print((char) 27 + Color.WHITE);

    }
    protected static void println(int msg)
    {
        System.out.println(msg);
    }
    protected static void setColor(String color)
    {
        if(ENABLE_COLOR) {
            print((char) 27 + color);
        }
    }
    protected static void print(String msg)
    {
        System.out.print(msg);
    }
}
