package ru.netology.graphics.image;

public class Schema implements TextColorSchema {

    public static char[] symbol = {'@', '$', '№', '#', '=', '/', '!', '.'};;

    @Override
    public char convert(int color) {
        int coefficient = (int) Math.round(256 / symbol.length-1);
        return symbol[color / coefficient];
    }
}