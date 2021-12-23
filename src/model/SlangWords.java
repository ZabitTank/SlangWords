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



}
