package Client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import tool.*;

/**
 * The GUI class of the client
 *
 * @author Liu Dairui
 */
public class ClientGUI {

    public static String username = null;
    /**
     * client side communication thread socket
     */
    private ClientThread clientThread;
    private JFrame main_window;

    private static JTextArea textAreaRecord;// The text record area
    private static JButton btnConnect;// connect/stop button
    private JTextField textUsername;// user name
    private JTextField textUserChecked;// enter user name
    private static JTextArea textAreaMsg;// message area
    private static JButton btnSend;// send button
    private static JButton btnList;// list button
    private static JButton btnKick;// kick button
    private static JButton btnStats;// stats button

    /**
     * Create the application.
     */
    public ClientGUI() {
        initialize();
        // initialize the user variable
        // generate the user name randomly
        Random rand = new Random();
        // set the text in the user name area
        textUsername.setText(rand.nextInt(100) + "");// generate the random number between 0-99

    }

    /**
     * Initialize the contents of the frame.It is client side GUI
     */
    private void initialize() {
        // initialize the frame
        main_window = new JFrame();
        main_window.setResizable(false);
        main_window.setTitle("Client");
        main_window.setBounds(Configuration.x, Configuration.y, Configuration.width, Configuration.height);
        main_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main_window.getContentPane().setLayout(null);

        // user id area
        JLabel username_text = new JLabel("User ID:");
        username_text.setBounds(Configuration.idText.x, Configuration.idText.y, Configuration.idText.width,
                Configuration.idText.height);
        main_window.getContentPane().add(username_text);
        textUsername = new JTextField();
        textUsername.setBounds(Configuration.idInput.x, Configuration.idInput.y, Configuration.idInput.width,
                Configuration.idInput.height);
        main_window.getContentPane().add(textUsername);
        textUsername.setColumns(10);

        // connect button
        btnConnect = new JButton("Connect");
        btnConnect.setBounds(Configuration.btConnect.x, Configuration.btConnect.y, Configuration.btConnect.width,
                Configuration.btConnect.height);
        main_window.getContentPane().add(btnConnect);
        // deal with the connect process
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // judge the state of the user
                if (btnConnect.getText().equals("Connect")) {
                    username = textUsername.getText();
                    // create the client thread
                    clientThread = new ClientThread();
                    clientThread.start();
                } else {
                    clientThread.logout();
                }

            }
        });
        // list button
        btnList = Tool.addButton(main_window, "LIST", Configuration.btList, Configuration.btwidth,
                Configuration.btheight);
        btnList.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (clientThread != null) {
                    // using the sendChatMas method to send message
                    clientThread.list();
                }
            }
        });
        // input text field
        textUserChecked = new JTextField();
        textUserChecked.setBounds(Configuration.idChecked.x, Configuration.idChecked.y, Configuration.idChecked.width,
                Configuration.idChecked.height);
        main_window.getContentPane().add(textUserChecked);
        textUsername.setColumns(10);
        // kick button
        btnKick = Tool.addButton(main_window, "KICK", Configuration.btKick, Configuration.btwidth,
                Configuration.btheight);

        btnKick.addActionListener(e -> {
            String id = textUserChecked.getText();
            if (clientThread != null) {
                // using the sendChatMas method to send message
                clientThread.kick(id.trim());
            }
        });
        // stats button
        btnStats = Tool.addButton(main_window, "STATS", Configuration.btStats, Configuration.btwidth,
                Configuration.btheight);
        btnStats.addActionListener(e -> {
            String id = textUserChecked.getText();
            if (clientThread != null) {
                // using the sendChatMas method to send message
                clientThread.stats(id.trim());
            }
        });
        // send button
        btnSend = Tool.addButton(main_window, "BROADCAST", Configuration.btSend, Configuration.btwidth * 2,
                Configuration.btheight);
        main_window.getContentPane().add(btnSend);
        // add listener in the button
        btnSend.addActionListener(arg0 -> {
            if (clientThread != null) {
                // using the sendChatMas method to send message
                clientThread.broadcast();
            }
        });
        //message record which receive message
        JScrollPane msgRecord = new JScrollPane();
        msgRecord.setBounds(Configuration.msgRecord.x, Configuration.msgRecord.y, Configuration.msgRecord.width,
                Configuration.msgRecord.height);
        main_window.getContentPane().add(msgRecord);

        textAreaRecord = new JTextArea();
        // It can not be edited
        textAreaRecord.setEditable(false);
        msgRecord.setViewportView(textAreaRecord);

        JScrollPane msgSend = new JScrollPane();
        msgSend.setBounds(Configuration.msgSend.x, Configuration.msgSend.y, Configuration.msgSend.width,
                Configuration.msgSend.height);
        main_window.getContentPane().add(msgSend);

        textAreaMsg = new JTextArea();
        msgSend.setViewportView(textAreaMsg);

        // update the UI
        try {
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
            // update the windows style
            SwingUtilities.updateComponentTreeUI(this.main_window);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add message in the record area
     *
     * @param msg the message that need to be added,it will add local time before
     *            the message
     */
    public static void addMsg(String msg) {
        Tool.addMsgRec(textAreaRecord, msg);
    }

    /**
     * Set the state of button, and the text of the connect button
     *
     * @param text when user is not connected, the text is "connect", else it is
     *             "stop"
     * @param open if the button is open to user
     */
    public static void setButton(String text, boolean open) {
        btnConnect.setText(text);
        btnSend.setEnabled(open);
        btnKick.setEnabled(open);
        btnList.setEnabled(open);
        btnStats.setEnabled(open);
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            try {
                ClientGUI window = new ClientGUI();
                window.main_window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * @return the content in text area
     */
    public static String getTextAreaText() {
        return textAreaMsg.getText();
    }

}