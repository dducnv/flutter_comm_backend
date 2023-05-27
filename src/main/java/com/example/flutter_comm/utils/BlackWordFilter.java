package com.example.flutter_comm.utils;

import com.example.flutter_comm.dto.BlackWorkDto;
import com.example.flutter_comm.entity.BlackWord;

import java.util.List;

public class BlackWordFilter {


    private TreeNode root;
    private int badWordStart;
    private int badWordEnd;
    private boolean isSuspicionFound;
    private boolean[] asteriskMark;


    public BlackWordFilter() {
        root = new TreeNode();

     }


    public void start(List<BlackWord> blackWordList)  {
        // bad words list file name

        blackWordList.forEach(word -> {
            addToTree(word.getWord().toLowerCase(), 0, root);
        });
    }



    public void addToTree(String badWordLine, int characterIndex, TreeNode node) {
        if (characterIndex < badWordLine.length()) {
            Character c = badWordLine.charAt(characterIndex);
            if (!node.containsChild(c)) {
                node.addChild(c);
            }
            node = node.getChildByLetter(c);
            // check if this is the last letter
            if (characterIndex == (badWordLine.length() - 1)) {
                // mark this letter as the end of a bad word
                node.setEnd(true);
            } else {
                // add next letter
                addToTree(badWordLine, characterIndex + 1, node);
            }
        }
    }



    public  BlackWorkDto filterBadWords(String userInput) {
        init(userInput.length());
        // for each character in a bad word
        for (int i = 0; i < userInput.length(); i++) {
            searchAlongTree(userInput.toLowerCase(), i, root);
        }
        return applyAsteriskMark(userInput);
    }

    private void init(int length) {
        asteriskMark = new boolean[length];
        for (int i = 0; i < length; i++) {
            asteriskMark[i] = false;
        }
        badWordStart = -1;
        badWordEnd = -1;
        isSuspicionFound = false;
    }

    private void searchAlongTree(String pUserInput, int characterIndex,
                                 TreeNode node) {
        if (characterIndex < pUserInput.length()) {
            // get the corresponding letter
            Character letter = pUserInput.charAt(characterIndex);
            if (node.containsChild(letter)) {
                // find a word whose first letter is equal to one of the bad
                // words' first letter
                if (isSuspicionFound == false) {
                    isSuspicionFound = true;
                    badWordStart = characterIndex;

                }
                // if this is the final letter of a bad word
                if (node.getChildByLetter(letter).isEnd()) {
                    badWordEnd = characterIndex;
                    markAsterisk(badWordStart, badWordEnd);
                }

                node = node.getChildByLetter(letter);
                searchAlongTree(pUserInput, characterIndex + 1, node);
            } else {
                // initialize some parameters
                isSuspicionFound = false;
                badWordStart = -1;
                badWordEnd = -1;
            }
        }
    }

    /**
     * Replace some of the letters in userInput as * according to asteriskMark
     *
     * @param userInput
     * @return string with bad words filtered
     */
    private BlackWorkDto applyAsteriskMark(String userInput) {
        StringBuilder filteredBadWords = new StringBuilder(userInput);
        char[] newChar = new char[asteriskMark.length];
        for (int i = 0; i < asteriskMark.length; i++) {
            if (asteriskMark[i] == true) {
                newChar[i] = filteredBadWords.charAt(i);
                filteredBadWords.setCharAt(i, '*');
            } else {
                newChar[i] = ' ';

            }

        }
        String regex = "\\s{2,}"; // Tìm các ký tự cách (\s) xuất hiện từ 2 đến nhiều lần ({2,})
        String replace = ", ";
        String blackWord = String.valueOf(newChar).trim().replaceAll(regex, replace);
        System.out.println(blackWord);
        return BlackWorkDto.builder()
                .isHadBlackWord(blackWord.length() >0)
                .blackWords(blackWord)
                .build();
    }

    /**
     * Identify the letters of userInput that should be marked as "*"
     *
     * @param badWordStart
     * @param badWordEnd
     */
    private void markAsterisk(int badWordStart, int badWordEnd) {
        System.out.println("Start: " + badWordStart);
        System.out.println("End: " + badWordEnd);

        for (int i = badWordStart; i <= badWordEnd; i++) {
            asteriskMark[i] = true;

        }
    }

}
