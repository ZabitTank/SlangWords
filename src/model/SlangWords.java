package model;

import java.io.*;
import java.util.*;

/**
 * Create by Pham Le Ha - 19127385
 * Date 12/20/2021 - 5:35 PM
 * Description: SlangWords data structure contain key (word)  pair to value(mean) by using hashmap (hashmap implement Serializable
 */
public class SlangWords {
    // static properties
    final String PATH_DATA = "./slang.dat";
    final String PATH_REVERSE_DATA = "./reverse_slang.dat";

    public static final String PATH_ORIGINAL_DATA = "./slang.txt";
    public static final int DUPLICATE = 0;
    public static final int REPLACE = 1;
    public static final boolean GUESS_WORD = true;
    public static final boolean GUESS_MEAN = false;

    public static SlangWords getOriginal(){
        SlangWords result = new SlangWords(true);

        try {
            FileReader fileReader = new FileReader(PATH_ORIGINAL_DATA);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            String key = null;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String[] word_mean = line.split("`");

                if (word_mean.length == 2) {
                    result.data.put(word_mean[0], word_mean[1]);
                    key = word_mean[0];
                    i = 1;

                    if(!result.reverseData.containsKey(word_mean[1])) {
                        result.reverseData.put(word_mean[1],word_mean[0]);
                    }
                    else {
                        int index2 =1;
                        while(result.reverseData.containsKey(word_mean[1]+":::"+index2)) index2++;
                        result.reverseData.put(word_mean[1]+":::"+index2,word_mean[0]);
                    }

                } else {
                    result.data.put(key + ":::" + i, word_mean[0]);
                    i++;

                    if(!result.reverseData.containsKey(word_mean[0])) {
                        result.reverseData.put(word_mean[0],key);
                    }
                    else {
                        int index2 =1;
                        while(result.reverseData.containsKey(word_mean[0]+":::"+index2)) index2++;
                        result.reverseData.put(word_mean[0]+":::"+index2,key);
                    }

                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }

        return result;
    }
    // attributes
    private Map<String , String> data;
    // not implement yet
    private Map<String,String> reverseData;

    /**
     *Description: Constructor of SlangWords class
     */
    public SlangWords(boolean empty){
        data = new HashMap<>();
        reverseData = new HashMap<>();
    }

    // load all SlangWord in to data
    public SlangWords() {
        File directory = new File("./slang.dat");

        if(!directory.exists()){
            SlangWords original = getOriginal();
            this.setData(original.data);
            this.setReverseData(original.reverseData);
        }
        else {
            try {
                FileInputStream fis = new FileInputStream(PATH_DATA);
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fis));

                data = (HashMap<String, String>) ois.readObject();

                ois.close();
                fis.close();

                fis = new FileInputStream(PATH_REVERSE_DATA);
                ois = new ObjectInputStream(new BufferedInputStream(fis));

                reverseData = (HashMap<String, String>) ois.readObject();

                ois.close();
                fis.close();

            } catch (FileNotFoundException fileNotFoundException) {
                System.out.println("Error at SlangWords constructor: " + fileNotFoundException.getMessage());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public void setReverseData(Map<String, String> reverseData) {
        this.reverseData = reverseData;
    }

    // methods



    /**
     * Description: saveDataStructure methods used to write data in file with ObjectOutputStream
     */
    public void saveDataStructure(){
        try {
            FileOutputStream fos = new FileOutputStream(PATH_DATA,false);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fos));

            oos.writeObject(data);

            oos.close();
            fos.close();

            fos = new FileOutputStream(PATH_REVERSE_DATA);
            oos = new ObjectOutputStream(new BufferedOutputStream(fos));

            oos.writeObject(reverseData);

            oos.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Description: get means by value. return Array of possible means
     * @param word: is a String
     */

    public Vector<String> getValue(String word){
        Vector<String> result = new Vector<>();

        String s;
        if((s = data.get(word))!= null) {
            result.add(s);

            int i = 1;
            while ((s = data.get(word + ":::" + i)) != null) {
                result.add(s);
                i++;
            }
        }
        return result;
    }

    public Vector<String> getWord(String mean){
        Vector<String> result = new Vector<>();
        String s;

        if((s = reverseData.get(mean))!= null) {
            result.add(s);

            int i = 1;
            while ((s = reverseData.get(mean + ":::" + i)) != null) {
                result.add(s);
                i++;
            }
        }
        return result;
    }

    public void addSlangWord(int choose,String word, String mean, int index){
        if(choose == REPLACE) {
            String newWord = word;
            if(index != 0) {
                newWord = word +":::" + index;
            }
            data.put(newWord,mean);

        }
        else if (choose == DUPLICATE){
            data.put(word+":::"+(index),mean);
        }

        if(!reverseData.containsKey(mean)) {
            reverseData.put(mean,word);
        }
        else {
            int index2 =1;
            while(reverseData.containsKey(mean+":::"+index2)) index2++;
            reverseData.put(mean+":::"+index2,word);
        }
    }

    public void delete(String word,int index){
        String mean = this.data.get(word);

        // delete in data

        // find last index
        int lastIndex =1;
        while(data.containsKey(word+":::"+lastIndex)) lastIndex++;
        lastIndex--;
        if(lastIndex ==0) this.data.remove(word);
        else {
            if(index ==0) {
                data.put(word,data.get(word+":::"+1));
                index++;
            }
            for(int i = index; i < lastIndex;i++){
                data.put(word+":::"+i,data.get(word+":::"+(i+1)));
            }
            data.remove(word+":::"+lastIndex);
        }

        // delete reverseData

        // find how many mean(n)
        int index2=0;
        lastIndex=1;

        while(reverseData.containsKey(mean+":::"+lastIndex)) {
            if(reverseData.get(mean+":::"+lastIndex).equals(word))
                index2 = lastIndex;
            lastIndex++;
        }
        lastIndex--;
        if(lastIndex==0) {
            reverseData.remove(mean);
        }
        else{
            if(index2==0) {
                reverseData.put(mean,reverseData.get(mean+":::"+1));
                index2++;
            }
            for(int i =index2;i< lastIndex;i++) {
                reverseData.put(mean + ":::" + i, reverseData.get(mean + ":::" +(i + 1)));
            }
            reverseData.remove(mean+":::"+lastIndex);
        }
    }

    public boolean isExist(String word , String mean){
        if(data.containsKey(word)) {
            Vector<String> allMean = this.getValue(word);
            if(allMean.isEmpty()) return false;
            return allMean.contains(mean);
        }
        return false;
    }

    public GuessWord guess(boolean wordOrMean){
        Random random = new Random();

        List<String> questions = null;
        String question=null;
        Vector<String> trueAnswers = null;

        if(wordOrMean == GUESS_WORD) {
            questions = new ArrayList<>(reverseData.values());
            question = questions.get(random.nextInt(questions.size()));
            trueAnswers = this.getValue(question);
        }
        else if(wordOrMean == GUESS_MEAN) {
            questions = new ArrayList<>(data.values());
            question = questions.get(random.nextInt(questions.size()));
            trueAnswers = this.getWord(question);
        }

        String trueAnswer = trueAnswers.elementAt(random.nextInt(trueAnswers.size()));

        Vector<String> block = new Vector<>();

        int i = 0;
        while(i < 3) {
            String randomValue = questions.get(random.nextInt(questions.size()));
            if(!randomValue.equals(trueAnswer)) {
                block.add(randomValue);
                i++;
            }
        }

        int index = random.nextInt(4);
        block.insertElementAt(trueAnswer,index);

        return new GuessWord(question,question,block,index);
    }

}
