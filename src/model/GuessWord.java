package model;

import java.util.Vector;

/**
 * Class description: This class only create to hold value for guess word game
 */
public class GuessWord {

        public String word;
        public String mean;
        public String trueAnswer;
        public Vector<String> availableAnswer;

        public GuessWord(String word, String mean, Vector<String> block, int index){
            this.word = word;
            this.mean = mean;
            this.trueAnswer = block.elementAt(index);
            this.availableAnswer = block;
        }

}
