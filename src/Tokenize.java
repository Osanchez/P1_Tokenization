package src;

import java.io.*;
import java.util.*;


public class Tokenize {

    private List<String> tokens;
    private List<String> stop_words;
    private Map<String, Integer> frequencies = new HashMap<>();

    public Tokenize() {
        stop_words = new ArrayList();
        tokens = new ArrayList();
    }

    public static boolean hasVowel(String check) {
        ArrayList<String> vowels = new ArrayList<>();
        vowels.add("a");
        vowels.add("e");
        vowels.add("i");
        vowels.add("o");
        vowels.add("u");

        boolean hasVowel = false;

        String[] listCheck = check.split("");

        for (String character : listCheck) {
            if (vowels.contains(character)) {
                return true;
            }
        }
        return hasVowel;
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
        x = 0; //resets the counter
        for (String token : tokens) {
            int token_length = token.length();
            if(token_length >= 3) {
                if (token.substring(token.length() - 1, token.length()).equals("s")) { //checks the last character in the string token
                    String checkPreceding = token.substring(0, token_length - 2);
                    String suffix = token.substring(token_length - 2, token_length);
                    boolean containsVowel = hasVowel(checkPreceding);
                    if (containsVowel && !suffix.equals("ss") && !suffix.equals("us")) {
                        String updated_token = token.substring(0, token_length - 1);
                        updated_tokens[x] = updated_token;
                    }
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

        //- Replace eed, eedly by ee if it is in the part of the word after the first non vowel following a vowel (e.g., agreed→agree, feed→feed).
        x = 0;
        for (String token : tokens) {
            int length = token.length();
            if (token.endsWith("eed")) {
                String remaining = token.substring(0, length - 3);
                if (length >= 5) {
                    String checkPreceding = remaining.substring(0, length - 3);
                    boolean hasPattern = checkPreceding.matches(".*[aeiou][bcdfghjklmnpqrstvwxyz]+");
                    System.out.println("Follows Rules: " + hasPattern);
                    if (hasPattern) {
                        String newToken = token.substring(0, length - 3) + "ee";
                        updated_tokens[x] = newToken;
                    } else {
                        updated_tokens[x] = token;
                    }
                }
            } else if (token.endsWith("eedly")) {
                String remaining = token.substring(0, length - 5);
                if (length >= 7) {
                    String checkPreceding = remaining.substring(length - 7, length - 5);
                    boolean hasPattern = checkPreceding.matches(".*[aeiou][bcdfghjklmnpqrstvwxyz]+");
                    if (hasPattern) {
                        String newToken = token.substring(0, length - 5) + "ee";
                        updated_tokens[x] = newToken;
                    } else {
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

        // - Delete ed, edly, ing, ingly if the preceding word part contains a vowel, and then if the word ends in at, bl, or iz add e (e.g., fished → fish, pirating → pirate),
        //or if the word ends with a double letter that is not ll, ss, or zz, remove the last letter (e.g., falling→fall, dripping→drip), or if the word is short, add e (e.g., hoping→hope).
        x = 0;
        for (String token : tokens) {
            int length = token.length();
            String double_letters = "lsz";

            if (token.endsWith("ed") && hasVowel(token.substring(0, length - 2))) { //checks for vowel in preceding word part
                String updated_token = token.substring(0, length - 2); //remove suffix
                String[] last_letters = updated_token.substring(updated_token.length() - 2, updated_token.length()).split(""); //gets last 2 letters up updated token
                if (updated_token.endsWith("at") || updated_token.endsWith("bl") || updated_token.endsWith("iz")) { //adds e if contains given suffixes
                    updated_token += "e";
                } else if (last_letters[0].equals(last_letters[1]) && !double_letters.contains(last_letters[0])) { //if last 2 letters are the same and not l,s, or z removes 1 letter
                    updated_token = updated_token.substring(0, updated_token.length() - 1); //remove a letter
                } else if (updated_token.length() < 4) { //if short add e
                    updated_token += "e";
                }
                updated_tokens[x] = updated_token;
            }

            else if (token.endsWith("edly") && hasVowel(token.substring(0, length - 4))) { //checks for vowel in preceding word part
                String updated_token = token.substring(0, length - 4); //remove suffix
                String[] last_letters = updated_token.substring(updated_token.length() - 2, updated_token.length()).split(""); //gets last 2 letters up updated token
                if (updated_token.endsWith("at") || updated_token.endsWith("bl") || updated_token.endsWith("iz")) { //adds e if contains given suffixes
                    updated_token += "e";
                } else if (last_letters[0].equals(last_letters[1]) && !double_letters.contains(last_letters[0])) { //if last 2 letters are the same and not l,s, or z removes 1 letter
                    updated_token = updated_token.substring(0, updated_token.length() - 1); //remove a letter
                } else if (updated_token.length() < 4) { //if short add e
                    updated_token += "e";
                }
                updated_tokens[x] = updated_token;
            }

            else if (token.endsWith("ing") && hasVowel(token.substring(0, length - 3))) { //checks for vowel in preceding word part
                String updated_token = token.substring(0, length - 3); //remove suffix
                String[] last_letters = updated_token.substring(updated_token.length() - 2, updated_token.length()).split(""); //gets last 2 letters up updated token
                if (updated_token.endsWith("at") || updated_token.endsWith("bl") || updated_token.endsWith("iz")) { //adds e if contains given suffixes
                    updated_token += "e";
                } else if (last_letters[0].equals(last_letters[1]) && !double_letters.contains(last_letters[0])) { //if last 2 letters are the same and not l,s, or z removes 1 letter
                    updated_token = updated_token.substring(0, updated_token.length() - 1); //remove a letter
                } else if (updated_token.length() < 4) { //if short add e
                    updated_token += "e";
                }
                updated_tokens[x] = updated_token;
            }

            else if (token.endsWith("ingly") && hasVowel(token.substring(0, length - 4))) { //checks for vowel in preceding word part
                String updated_token = token.substring(0, length - 4); //remove suffix
                String[] last_letters = updated_token.substring(updated_token.length() - 2, updated_token.length()).split(""); //gets last 2 letters up updated token
                if (updated_token.endsWith("at") || updated_token.endsWith("bl") || updated_token.endsWith("iz")) { //adds e if contains given suffixes
                    updated_token += "e";
                } else if (last_letters[0].equals(last_letters[1]) && !double_letters.contains(last_letters[0])) { //if last 2 letters are the same and not l,s, or z removes 1 letter
                    updated_token = updated_token.substring(0, updated_token.length() - 1); //remove a letter
                } else if (updated_token.length() < 4) { //if short add e
                    updated_token += "e";
                }
                updated_tokens[x] = updated_token;
            }
            x++;
        }
        //update tokens
        tokens.clear();
        for (String token : updated_tokens) {
            tokens.add(token);
        }
        System.out.println("Updated Tokens 5: " + tokens);
    }

    public void exportToFile(String fileName) {
        BufferedWriter writer = null;
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);
            writer = new BufferedWriter(fileWriter);

            for(String token : tokens) {
                writer.write(token);
                writer.newLine();
            }
            System.out.println("Wrote tokens to file successfully...");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null) {
                    writer.close();
                }
                if(fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void frequencyExportToFile(String fileName) {

        for(String token : tokens) {
            if(frequencies.containsKey(token)) {
                int count = frequencies.get(token);
                frequencies.put(token, count + 1);
            } else {
                frequencies.put(token, 1);
            }
        }

        Object[] sorted = frequencies.entrySet().toArray();
        Arrays.sort(sorted, (o1, o2) -> ((Map.Entry<String, Integer>) o2).getValue()
                .compareTo(((Map.Entry<String, Integer>) o1).getValue()));


        BufferedWriter writer = null;
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);
            writer = new BufferedWriter(fileWriter);

            int x = 1;
            for(Object e : sorted) {
                if(x == 200) { //filthy way of only printing first 200 terms
                    break;
                }
                writer.write(((Map.Entry<String, Integer>) e).getKey() + " : " + ((Map.Entry<String, Integer>) e).getValue());
                writer.newLine();
                x++;
            }

            System.out.println("Wrote terms to file successfully...");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null) {
                    writer.close();
                }
                if(fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

