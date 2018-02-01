public class main {
    public static void main(String[] args) {
        tokenization();
    }

    private static void tokenization() {
        Tokenize tokenize = new Tokenize();
        tokenize.tokenization("src/files/tokenization-input-part-A.txt");
        tokenize.stopwordRemoval("src/files/stopwords.txt");
        tokenize.porterStemming();
    }

}
