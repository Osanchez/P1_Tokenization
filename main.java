public class main {
    public static void main(String[] args) {
        tokenization();
    }

    private static void tokenization() {
        Tokenize tokenize = new Tokenize();
        tokenize.tokenization("files/tokenization-input-part-A.txt");
        tokenize.stopwordRemoval("files/stopwords.txt");
        tokenize.porterStemming();
    }

}
