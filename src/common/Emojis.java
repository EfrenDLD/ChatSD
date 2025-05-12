package common;

public class Emojis {
    public static String replace(String text) {
        return text.replace(":)", "😊")
                .replace(":(", "😢")
                .replace("<3", "❤️")
                .replace(":D", "😄");
    }
}
