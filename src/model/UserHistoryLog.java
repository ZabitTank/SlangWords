package model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Create by Pham Le Ha - 19127385
 * Date 12/20/2021 - 11:31 PM
 * Description: Handle user history log by vector<String> userLog, save userLog by ObjectOutputStream.
 */
public class UserHistoryLog  {
    //static field
    public static final String U_ADD = "Add word";
    public static final String  U_REPLACE = "Replace value of word";
    public static final String  U_DELETE = "Delete word";
    public static final String U_SEARCH_WORD = "Search word";
    public static final String U_SEARCH_VALUE = "Search value";
    public static final String U_RANDOM_WORD = "Random word";
    public static final String U_GUESS_WORD = "Guess word";
    public static final String U_GUESS_MEAN = "Guess mean";
    public static final String U_RESET = "Reset original data";
    public static final String HISTORY_FOLDER_PATH = "./User History Logs";

    // private field
    private Vector<String> userLog;


    public static UserHistoryLog read(String fileName) {
        UserHistoryLog result = new UserHistoryLog();
        try {
            FileInputStream fw = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fw));

            result.userLog = (Vector<String>) ois.readObject();

            ois.close();
            fw.close();
        }catch(IOException | ClassNotFoundException e){
            System.out.println("Save UserLog failed : "+ e.getMessage());
        }
        return result;
    }

    // constructor
    public UserHistoryLog() {
        userLog = new Vector<>();
    }

    // getter
    public Vector<String> getUserLog() {
        return userLog;
    }

    // methods
    public String addHistory(String action , String key, String mean){
        String result = java.time.LocalTime.now().toString().substring(0,8) + " " + action;
        if(key!=null) result += " " + key;
        if(mean != null) result += mean;

        userLog.add(result);

        return result;
    }

    public void saveFile(){
        // Create folder if it is not existed
        File directory = new File(HISTORY_FOLDER_PATH);
        if (!directory.exists()){
            directory.mkdir();
        }

        String out = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss").format(new Date());
        String fileName = HISTORY_FOLDER_PATH+"/hl"+out+".uhdl";

        try {
            FileOutputStream fw = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fw));

            oos.writeObject(this.userLog);
            oos.flush();

            oos.close();
            fw.close();
        }catch(IOException e){
            System.out.println("Save UserLog failed : "+ e.getMessage());
        }

    }
}
