package experiments;


public class NGramBadHash extends BStringBadHash<String> {
    public NGramBadHash(String[] str) {
        super(str);
    }

    public NGramBadHash update(String word) {

        String[] str = new String[this.str.size()];

        for (int i = 0; i < str.length - 1; i++) {
            str[i] = this.str.peek(i + 1);
        }
        str[str.length - 1] = word;

        return new NGramBadHash(str);
    }

    public String getFirstWord() {
        return this.str.peek();
    }

    public static Class<String> getLetterType() {
        return String.class;
    }

    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        for (String chr : this) {
            build.append(chr + " ");
        }
        build.delete(build.length() - 1, build.length());
        return "\"" + build.toString() + "\"";
    }

}
