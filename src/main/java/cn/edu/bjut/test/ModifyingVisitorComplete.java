package cn.edu.bjut.test;

import java.util.regex.Pattern;

public class ModifyingVisitorComplete {
    private static final String FILE_PATH = "D:\\users\\panshengqiu\\idea2024\\workspace\\jparser-analysis-graduation\\src\\main\\java\\cn\\edu\\bjut\\test\\ReversePolishNotation.java";

    private static final Pattern LOOK_AHEAD_THREE = Pattern.compile("(\\d)(?=(\\d){3}+$)");
    public static void main(String[] args) {

    }
}
