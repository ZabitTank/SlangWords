import model.GuessWord;
import model.SlangWords;
import model.UserHistoryLog;
import view.UserUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Vector;

/**
 * Create by Pham Le Ha - 19127385
 * Date 12/21/2021 - 11:48 AM
 * Description: ...
 */
public class App {
    private SlangWords data;
    final UserHistoryLog dataLog;
    final UserUI ui;

    public App(){
        data = new SlangWords();
        dataLog = new UserHistoryLog();
        ui = new UserUI();
        ui.setListener(this.listener);
        ui.addWindowListener(windowAdapter);
    }


    final ActionListener listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userCurrentAction = null;
            if (e.getActionCommand().equals("add")) {
                String word = ui.wordTextField.getText();
                String mean = ui.meanTextField.getText();

                if (word.isEmpty() || mean.isEmpty()) {
                    JOptionPane.showMessageDialog(ui, "Invalid value Word or Mean");

                } else if (data.isExist(word, mean)) {
                    JOptionPane.showMessageDialog(ui, "This slang is already exist");

                } else {
                    Vector<String> result = data.getValue(word);

                    if (result.isEmpty()) {
                        data.addSlangWord(SlangWords.REPLACE, word, mean, 0);
                        JOptionPane.showMessageDialog(ui, "Add new word successful");

                        userCurrentAction = dataLog.addHistory(UserHistoryLog.U_ADD, word + ": ", mean);

                    } else {
                        int userChoice = JOptionPane.showConfirmDialog(ui, "DO YOU WANT REPLACE ?", "DUPLICATE OR REPLACE", JOptionPane.YES_NO_OPTION);

                        if (userChoice == JOptionPane.YES_OPTION) {
                            JList choice = new JList(result);
                            choice.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                            JOptionPane.showMessageDialog(ui, choice, "Select one mean to replace", JOptionPane.PLAIN_MESSAGE);
                            data.addSlangWord(SlangWords.REPLACE, word, mean, choice.getSelectedIndex());

                            userCurrentAction = dataLog.addHistory(UserHistoryLog.U_REPLACE, word + ": ", choice.getSelectedValue() + " by " + mean);
                        } else {
                            System.out.println(result.size());
                            data.addSlangWord(SlangWords.DUPLICATE, word, mean, result.size());

                            userCurrentAction = dataLog.addHistory(UserHistoryLog.U_ADD, word + ": ", mean);
                        }
                    }
                }
            } else if (e.getActionCommand().equals("delete")) {
                String word = ui.wordTextField.getText();
                if (word.isEmpty()) JOptionPane.showMessageDialog(ui, "Empty word to delete");
                else {
                    Vector<String> result = data.getValue(word);
                    if (result.isEmpty()) {
                        JOptionPane.showMessageDialog(ui, "This word don't exist");
                    } else {
                        JList choice = new JList(result);
                        int index =-1;
                        if (result.size() == 1) {
                            index = 0;
                        } else {
                            choice.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                            if(JOptionPane.showConfirmDialog(ui, choice, "Select one mean to delete", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                                index = choice.getSelectedIndex();
                        }
                        if(index!= -1) {
                            int confirm = JOptionPane.showConfirmDialog(ui, "Do you want delete word: " + word+": "+result.elementAt(index)
                                    , "Confirm", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                userCurrentAction = dataLog.addHistory(UserHistoryLog.U_DELETE, word+": ",result.elementAt(index));
                                data.delete(word, choice.getSelectedIndex());
                            }
                        }

                    }
                }
            } else if (e.getActionCommand().equals("mean")) {
                String mean = ui.meanTextField.getText();
                if (mean.isEmpty()) JOptionPane.showMessageDialog(ui, "Empty mean to search");
                else {
                    Vector<String> result = data.getWord(mean);
                    ui.updateResultLog(result);
                    userCurrentAction = dataLog.addHistory(UserHistoryLog.U_SEARCH_VALUE, "", mean);
                }
            } else if (e.getActionCommand().equals("word")) {
                String word = ui.wordTextField.getText();
                if (word.isEmpty()) JOptionPane.showMessageDialog(ui, "Empty word to search");
                else {
                    Vector<String> result = data.getValue(word);
                    ui.updateResultLog(result);
                    userCurrentAction = dataLog.addHistory(UserHistoryLog.U_SEARCH_WORD, word, "");
                }
            } else if (e.getActionCommand().equals("navigate")) {

                JFileChooser f = new JFileChooser(new File(UserHistoryLog.HISTORY_FOLDER_PATH));
                f.showSaveDialog(ui);
                File selectFile = f.getSelectedFile();
                if(selectFile!=null) {
                    String fileName = selectFile.getName();

                    if (!fileName.endsWith("uhdl")) {
                        JOptionPane.showMessageDialog(ui, "Invalid selected file");
                    } else {
                        UserHistoryLog navigateDataLog = UserHistoryLog.read(selectFile.getAbsolutePath());
                        ui.naviGateHistoryLog(navigateDataLog.getUserLog());
                    }
                }

            } else if (e.getActionCommand().equals("return")) {
                ui.updateHistoryLog();

            } else if (e.getActionCommand().equals("reset")) {
                data = SlangWords.getOriginal();
                userCurrentAction = dataLog.addHistory(UserHistoryLog.U_RESET, null, null);

            } else {
                if (e.getActionCommand().equals("random")) {
                    GuessWord randomWord = data.guess(SlangWords.GUESS_WORD);
                    JOptionPane.showMessageDialog(ui, randomWord.word + " is " + randomWord.trueAnswer, "Today random word",
                            JOptionPane.PLAIN_MESSAGE);
                    userCurrentAction = dataLog.addHistory(UserHistoryLog.U_RANDOM_WORD, null, null);
                } else {
                    String title = "";
                    GuessWord randomQuestion = null;
                    if (e.getActionCommand().equals("guess_word")) {
                        randomQuestion = data.guess(SlangWords.GUESS_WORD);
                        title = "Guess this word: " + randomQuestion.word;
                        userCurrentAction = dataLog.addHistory(UserHistoryLog.U_GUESS_WORD, null, null);

                    } else if (e.getActionCommand().equals("guess_mean")) {
                        randomQuestion = data.guess(SlangWords.GUESS_MEAN);
                        title = "Guess this mean: " + randomQuestion.mean;
                        userCurrentAction = dataLog.addHistory(UserHistoryLog.U_GUESS_MEAN, null, null);

                    }
                    JList choiceList = new JList(randomQuestion.availableAnswer);
                    choiceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                    JOptionPane.showMessageDialog(ui, choiceList, title, JOptionPane.PLAIN_MESSAGE);
                    String answer = (String) choiceList.getSelectedValue();

                    if (answer != null) {
                        if (answer.equals(randomQuestion.trueAnswer))
                            JOptionPane.showMessageDialog(ui, "You choose the right one!");
                        else
                            JOptionPane.showMessageDialog(ui, "The true answer is " + randomQuestion.trueAnswer);
                    }
                }
            }
            if(userCurrentAction!=null) ui.addHistoryLog(userCurrentAction);
        }
    };

    WindowAdapter windowAdapter = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            data.saveDataStructure();
            dataLog.saveFile();
            System.exit(0);
        }
    };

    public static void main(String[] args) {
        App app = new App();

    }
}
