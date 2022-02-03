package BotAI;

public class textToCode {
    public String toCode (String input, int degree) {
        StringBuilder output = new StringBuilder();
        String sym = "абвгдеёжзийклмнопрстуфхцчшщъыэюя";
        for (int i = 0; i < input.length(); i++) {
            for (int j = 0; j < sym.length(); j++) {
                if (input.charAt(i) == sym.charAt(j)) {
                    int newPos = j + degree;
                    while (newPos >= sym.length())
                        newPos -= sym.length();
                    while (newPos < 0)
                        newPos += sym.length();
                    output.append(sym.charAt(newPos));
                    break;
                }
                if (j == sym.length() - 1)
                    output.append(input.charAt(i));
            }
        }
        return output.toString();
    }
}
