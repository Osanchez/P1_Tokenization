import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tokenize {

    private List<String> tokens;
    private List<String> stop_words;

    public Tokenize() {
        stop_words = new ArrayList();
        tokens = new ArrayList();
    }

    public void tokenization(String fileName) {
        String update_raw_text = "";
        File file = new File(fileName);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String read_text = null;

            while ((read_text = reader.readLine()) != null) { //read all lines of the document
                update_raw_text += read_text;
            }

            //Part A section 1
            System.out.println("---------- Implement Tokenization ----------");
            System.out.println("Original Text: " + update_raw_text); //testing
            update_raw_text = update_raw_text.replaceAll("[^a-zA-Z0-9.]", " "); //replace special chars with white spaces
            update_raw_text = update_raw_text.replace(".", ""); //remove periods
            update_raw_text = update_raw_text.trim().replaceAll(" +", " "); //replace 2+ whitespaces with one
            update_raw_text = update_raw_text.toLowerCase(); //make all characters lower case
            String[] formatted_text_list = update_raw_text.split(" "); //turn raw formatted text into list of words (tokens)

            //testing
            System.out.println("Cleaned Text: " + update_raw_text); //print all lines of the document read
            System.out.print("Tokens: ");

            //update class variable
            for (String word : formatted_text_list) {
                tokens.add(word);
            }

            System.out.println(tokens); //print formatted text array

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public void stopwordRemoval(String fileName) {
        String raw_stop_words = "";
        File file = new File(fileName);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String read_text = null;

            while ((read_text = reader.readLine()) != null) { //read all lines of the document
                raw_stop_words += read_text + " "; //convert stop word lines to a single string of all stop words
            }

            //create a list of stop words to be used later for removal
            String[] stop_words_list = raw_stop_words.split(" "); //create an array of all stop words

            //testing
            System.out.println("---------- Stop Word Removal ----------");
            System.out.print("Stop Word List: ");

            //update class variable
            for (String stopWord : stop_words_list) {
                stop_words.add(stopWord);
            }

            System.out.println(stop_words);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Part A section 2
        for (String word : stop_words) { //remove stop words from formatted text
            tokens.removeAll(Collections.singleton(word)); //fancy way of removing all occurrences
        }
        System.out.println("Cleaned Tokens: " + tokens);
    }

    /*
    Porter Stemming
    Step 1a
    - Replace sses by ss (e.g., stresses→stress).
    - Delete s if the preceding word part contains a vowel not immediately before the s (e.g., gaps→gap but gas→gas).
    - Replace ied or ies by i if preceded by more than one letter, otherwise by ie (e.g., ties→tie, cries→cri).
    - If suffix is us or ss do nothing (e.g., stress→stress).

    Step 1b
    - Replace eed, eedly by ee if it is in the part of the word after the first non vowel following a vowel (e.g., agreed→agree, feed→feed).
    - Delete ed, edly, ing, ingly if the preceding word part contains a vowel, and then if the word ends in at, bl, or iz add e (e.g., fished → fish, pirating → pirate),
    or if the word ends with a double letter that is not ll, ss, or zz, remove the last letter (e.g., falling→fall, dripping→drip), or if the word is short, add e (e.g., hoping→hope).
    */

    public void porterStemming() {
        System.out.println("---------- Porter Stemming ----------");

        //------------------------ Part 1a ------------------------
        //TODO: might not be able to include s for part 1b
        List<String> vowels = new ArrayList<>();
        vowels.add("a");
        vowels.add("e");
        vowels.add("i");
        vowels.add("o");
        vowels.add("u");

        String[] updated_tokens = new String[tokens.size()];
        int x = 0;

        //- Replace sses by ss (e.g., stresses→stress).
        for (String token : tokens) {
            if (token.length() >= 4) {
                if (token.substring(token.length() - 4, token.length()).equals("sses")) {
                    updated_tokens[x] = token.substring(0, token.length() - 2);
                } else {
                    updated_tokens[x] = token;
                }
            } else {
                updated_tokens[x] = token;
            }
            x++;
        }

        //update tokens
        tokens.clear();
        for (String token : updated_tokens) {
            tokens.add(token);
        }

        System.out.println("Updated Tokens 1: " + tokens);


        //Delete s if the preceding word part contains a vowel not immediately before the s (e.g., gaps→gap but gas→gas).
        //If suffix is us or ss do nothing (e.g., stress→stress). *This is handled here as well using the vowel and double s array*
        x = 0; //resets the counter
        for (String token : tokens) {
            boolean hasVowel = false;
            if (token.substring(token.length() - 1, token.length()).equals("s")) { //checks the last character in the string token
                String substring = token.length() > 2 ? token.substring(token.length() - 2) : token; //gets the suffix (2 characters)
                //checks to see if preceding character is a vowel
                String[] split_substring = substring.split("");
                for (String vowel : vowels) {
                    if (split_substring[0].equals(vowel)) {
                        hasVowel = true;
                        break;
                    }
                }

                if (hasVowel == false) {
                    updated_tokens[x] = token.substring(0, token.length() - 1); //removes last character, which is the s
                } else {
                    updated_tokens[x] = token;
                }
            }
            x++;
        }

        System.out.println("Updated Tokens 2: " + Arrays.toString(updated_tokens));

        //update tokens
        tokens.clear();
        for (String token : updated_tokens) {
            tokens.add(token);
        }

        //Replace ied or ies by i if preceded by more than one letter, otherwise by ie (e.g., ties→tie, cries→cri).
        x = 0; //reset counter
        for (String token : tokens) {
            if (token.length() >= 3) {
                if (token.substring(token.length() - 3, token.length()).contains("ied") || token.substring(token.length() - 3, token.length()).contains("ies")) {
                    String remaining_characters = token.substring(0, token.length() - 3);
                    int length = remaining_characters.length();
                    if (length > 1) {
                        updated_tokens[x] = remaining_characters + "i";
                    } else {
                        updated_tokens[x] = remaining_characters + "ie";
                    }
                }
            }
            x++;
        }

        System.out.println("Updated Tokens 3: " + Arrays.toString(updated_tokens));

        //update tokens
        tokens.clear();
        for (String token : updated_tokens) {
            tokens.add(token);
        }

        //------------------------ Part 1b ------------------------
        //TODO: THIS SECTION
        String[] delete_suffies = {"ed", "edly", "ing", "ingly"};
        String[] add_suffixes = {"at", "bl", "iz"};

        //- Replace eed, eedly by ee if it is in the part of the word after the first non vowel following a vowel (e.g., agreed→agree, feed→feed).
        x = 0;
        for(String token : tokens) {
            int length = token.length();
            if (token.endsWith("eed")) {
                System.out.println("\n" + token);
                System.out.println("Ends with 'eed' --> " + token);
                String remaining = token.substring(0, length - 3);
                if(length >= 5) {
                    String checkPreceding = remaining.substring(0 , length - 3);
                    boolean hasPattern = checkPreceding.matches("[bcdfghjklmnpqrstvwxz]+[aeiou]");
                    System.out.println("Follows Rules: " + hasPattern);
                    if(hasPattern) {
                        String newToken = token.substring(0, length - 3) + "ee";
                        System.out.println("new token --> " + newToken);
                        updated_tokens[x] = newToken;
                    } else {
                        System.out.println("new token unchanged --> " + token);
                        updated_tokens[x] = token;
                    }
                }
            }
            else if(token.endsWith("eedly")) {
                System.out.println("\n" + token);
                System.out.println("Ends with 'eedly' --> " + token);
                String remaining = token.substring(0, length - 5);
                if(length >= 7) {
                    String checkPreceding = remaining.substring(length - 7 , length - 5);
                    String[] precedingCharacters = checkPreceding.split("");
                    if(!vowels.contains(precedingCharacters[0]) && vowels.contains(precedingCharacters[1])) {
                        String newToken = token.substring(0, length - 5) + "ee";
                        System.out.println("new token --> " + newToken);
                        updated_tokens[x] = newToken;
                    } else {
                        System.out.println("new token unchanged --> " + token);
                        updated_tokens[x] = token;
                    }
                }
            }
            x++;
        }
        //update tokens
        tokens.clear();
        for (String token : updated_tokens) {
            tokens.add(token);
        }
        System.out.println("Updated Tokens 4: " + tokens);
    }
}

