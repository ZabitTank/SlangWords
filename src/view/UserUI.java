package view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * Create by Pham Le Ha - 19127385
 * Date 12/21/2021 - 4:20 AM
 * Description: ...
 */
public class UserUI extends  JFrame{
    private JPanel rootPanel;
    private JPanel logPanel;
    private JPanel historyLogPanel;
    private JPanel navigateLogPanel;
    private JPanel slangPanel;
    private JPanel resultPanel;
    private JList resultList;
    private JPanel userInputPanel;
    public JTextField wordTextField;
    public JTextField meanTextField;
    private JButton searchWordButton;
    private JButton searchMeanButton;
    private JButton addButton;
    private JButton deleteButton;
    private JButton changeHistoryLogButton;
    private JButton currentHistoryLogButton;
    private JList historyLogList;
    private JButton randomWordButton;
    private JButton guessWordButton;
    private JButton guessMeanButton;
    private JButton resetOriginalSlangWordButton;

    public DefaultListModel historyListModel;
    public DefaultListModel oldHistoryListModel;
    public DefaultListModel searchListModel;

    public UserUI(){
        addButton.setActionCommand("add");
        deleteButton.setActionCommand("delete");
        searchMeanButton.setActionCommand("mean");
        searchWordButton.setActionCommand("word");
        changeHistoryLogButton.setActionCommand("navigate");
        currentHistoryLogButton.setActionCommand("return");
        randomWordButton.setActionCommand("random");
        guessWordButton.setActionCommand("guess_word");
        guessMeanButton.setActionCommand("guess_mean");
        resetOriginalSlangWordButton.setActionCommand("reset");

        historyListModel = new DefaultListModel();
        oldHistoryListModel = new DefaultListModel();
        searchListModel = new DefaultListModel();

        historyLogList.setModel(historyListModel);
        resultList.setModel(searchListModel);

        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setContentPane(rootPanel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Slang words");
        pack();
        setVisible(true);
    }

    public void addHistoryLog(String history){
        historyListModel.addElement(history);
    }
    public void updateHistoryLog(){
        historyLogList.setModel(historyListModel);
    }

    public void naviGateHistoryLog(Vector<String> oldHistories){
        oldHistoryListModel = new DefaultListModel();

        for(int i = 0 ; i < oldHistories.size() ;i++){
            oldHistoryListModel.addElement(oldHistories.elementAt(i));
        }
        historyLogList.setModel(oldHistoryListModel);
    }

    public void updateResultLog(Vector<String> searchResult){
        searchListModel.removeAllElements();

        for(int i = 0 ; i < searchResult.size() ; i++ )
            searchListModel.addElement(searchResult.elementAt(i));

    }

    public void setListener(ActionListener listener){

        addButton.addActionListener(listener);
        deleteButton.addActionListener(listener);
        searchMeanButton.addActionListener(listener);
        searchWordButton.addActionListener(listener);
        changeHistoryLogButton.addActionListener(listener);
        currentHistoryLogButton.addActionListener(listener);
        guessMeanButton.addActionListener(listener);
        guessWordButton.addActionListener(listener);
        randomWordButton.addActionListener(listener);
        resetOriginalSlangWordButton.addActionListener(listener);
    }

}
