package src;

public class main {
    public static void main(String[] args) {
        tokenization();
    }

    private static void tokenization() {
        //PART A
        Tokenize tokenizeA = new Tokenize();
        tokenizeA.tokenization("files/tokenization-input-part-A.txt");
        tokenizeA.stopwordRemoval("files/stopwords.txt");
        tokenizeA.porterStemming();
        tokenizeA.exportToFile("tokenized.txt");

        //PART B
        Tokenize tokenizeB = new Tokenize();
        tokenizeB.tokenization("files/tokenization-input-part-B.txt");
        tokenizeB.stopwordRemoval("files/stopwords.txt");
        tokenizeB.porterStemming();
        tokenizeB.frequencyExportToFile("terms.txt");
    }

}
