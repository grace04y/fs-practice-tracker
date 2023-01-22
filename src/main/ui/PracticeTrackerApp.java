package ui;

import model.Element;
import model.Event;
import model.EventLog;
import model.PracticeTracker;
import model.TrainingSession;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Represents the PracticeTracker application
public class PracticeTrackerApp extends JPanel implements ListSelectionListener {
    private PracticeTracker practiceTracker;
    private String date;
    private TrainingSession trainingSession;
    private String elementName;
    private int success;
    private Element element;

    private Scanner scanner;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/practicetracker.json";

    //P3 - GUI
    private JFrame frame;

    //GUI PracticeTracker
    private JList sessionsList;
    private DefaultListModel sessionListModel;
    private JPanel buttonPane;
    private JButton viewSessionButton;
    private int trainingSessionIndex;

    //GUI TrainingSession
    private JFrame viewSessionWindow;
    private JFrame newTrainingSessionWindow;
    private JPanel sessionPane;
    private JPanel sessionInfoPane;

    //GUI Element
    private JList elementList;
    private DefaultListModel elementListModel;
    private JScrollPane elementListScrollPane;
    private JSplitPane splitPane;
    private JScrollPane listScrollPane;


    //EFFECTS: constructs a practice tracker and runs application
    public PracticeTrackerApp() throws FileNotFoundException {
        this.element = new Element(elementName);
        this.trainingSession = new TrainingSession(date);

        scanner = new Scanner(System.in);
        practiceTracker = new PracticeTracker("My practice tracker");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        buttonPane = new JPanel();

        getSplashScreen();
        createAndShowGUI();
    }

    //EFFECT: displays a photo upon start-up of the app
    private void getSplashScreen() {
        JFrame splashScreen = new JFrame();
        ImageIcon splashPhoto = new ImageIcon("./data/splashScreen1.jpg");
        JLabel splashLabel = new JLabel(splashPhoto);

        splashScreen.add(splashLabel);
        splashScreen.setBounds(500, 150, 1085, 1085);
        splashScreen.setVisible(true);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Error: cannot load splash screen");
        }

        splashScreen.setVisible(false);
    }

    //EFFECTS: creates and displays a GUI window
    private void createAndShowGUI() {
        frame = new JFrame("Practice Tracker App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel emptyLabel = new JLabel("");
        emptyLabel.setPreferredSize(new Dimension(1000, 750));
        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);

        getPracticeTrackerButtons();
        getPracticeTrackerScrollPane();

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }


    //EFFECTS: displays dates of all sessions previously recorded
    private void getPracticeTrackerScrollPane() {
//        practiceTracker = new PracticeTracker("Practice Tracker App");
        sessionListModel = new DefaultListModel();

        sessionsList = new JList(sessionListModel);
        sessionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sessionsList.setSelectedIndex(0);
        sessionsList.addListSelectionListener(this);
        sessionsList.setVisibleRowCount(5);

        listScrollPane = new JScrollPane(sessionsList);

        frame.add(listScrollPane);
    }

    //EFFECTS: adds TrainingSessions loaded from file to the scroll pane of TrainingSessions
    private void updatePracticeTrackerScrollPane() {
        List<TrainingSession> trainingSessionList = practiceTracker.getTrainingSessionList();
        for (int i = 0; i < trainingSessionList.size(); i++) {
            if (!sessionListModel.contains(trainingSessionList.get(i))) {
                sessionListModel.addElement(trainingSessionList.get(i).getDate());
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: Displays the information for the selected training session (non-editable)
    public void viewTrainingSession() {
        viewSessionWindow = new JFrame("Session");
        sessionInfoPane = new JPanel();

        trainingSessionIndex = sessionsList.getSelectedIndex();
        trainingSession = practiceTracker.getTrainingSessionList().get(trainingSessionIndex);

        //Displays TrainingSession info
        JLabel dateLabel = new JLabel(trainingSession.getDate());
        JLabel startTimeLabel = new JLabel("\n Start time: " + trainingSession.getStartTime());
        JLabel endTimeLabel = new JLabel("\n End time: " + trainingSession.getEndTime());

        createViewTrainingSessionSplitPane();

        sessionInfoPane.setLayout(new BoxLayout(sessionInfoPane,
                BoxLayout.Y_AXIS));

        sessionInfoPane.add(Box.createHorizontalStrut(5));
        sessionInfoPane.add(new JSeparator(SwingConstants.HORIZONTAL));
        sessionInfoPane.add(Box.createHorizontalStrut(5));

        sessionInfoPane.add(dateLabel);
        sessionInfoPane.add(startTimeLabel);
        sessionInfoPane.add(endTimeLabel);
        getViewElementButton();

        viewSessionWindow.pack();
        viewSessionWindow.setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: creates a new button labelled "View Element"
    public void getViewElementButton() {
        JButton viewElementButton = new JButton("View Element");
        ButtonListener buttonListener = new ButtonListener(viewElementButton);
        viewElementButton.setActionCommand("View Element");
        viewElementButton.addActionListener(buttonListener);
        viewElementButton.setEnabled(true);

        sessionInfoPane.add(viewElementButton);
    }

    //MODIFIES: this
    //EFFECTS: makes an editable window that displays the details of a session
    // (Start time, end time, elements practiced)
    public void addTrainingSession() {
        initTrainingSession();

        newTrainingSessionWindow = new JFrame("New Session");
        sessionPane = new JPanel();

        makeSessionInfoPane();
        getNewElementScrollPane();
        createTrainingSessionSplitPane();

        //Add TrainingSession to PracticeTracker
        practiceTracker.addTrainingSession(trainingSession);

        //Create a panel that uses BoxLayout.
        sessionPane.setLayout(new BoxLayout(sessionPane,
                BoxLayout.LINE_AXIS));

        sessionPane.add(Box.createHorizontalStrut(5));
        sessionPane.add(new JSeparator(SwingConstants.VERTICAL));
        sessionPane.add(Box.createHorizontalStrut(5));

        createNewElementButton();
        createSaveSessionButton();

        sessionPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        newTrainingSessionWindow.add(sessionPane, BorderLayout.PAGE_END);

        newTrainingSessionWindow.pack();
        newTrainingSessionWindow.setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: Creates a button that saves the given session to the PracticeTracker, closes window
    public void createSaveSessionButton() {
        JButton saveSessionButton = new JButton("Save Session");
        ButtonListener buttonListener1 = new ButtonListener(saveSessionButton);
        saveSessionButton.setActionCommand("Save Session");
        saveSessionButton.addActionListener(buttonListener1);
        saveSessionButton.setEnabled(true);

        sessionPane.add(saveSessionButton);
    }

    //MODIFIES: this
    //EFFECTS: Creates a button that adds a new element when pressed
    public void createNewElementButton() {
        JButton newElementButton = new JButton("Add New Element");
        ButtonListener buttonListener = new ButtonListener(newElementButton);
        newElementButton.setActionCommand("Add Element");
        newElementButton.addActionListener(buttonListener);
        newElementButton.setEnabled(true);

        sessionPane.add(newElementButton);
    }

    //MODIFIES: TrainingSession
    //EFFECTS: constructs a TrainingSession with the given date, start and end times
    public void initTrainingSession() {
        String inputDate = JOptionPane.showInputDialog("Date (yyyy-mm-dd)");
        trainingSession = new TrainingSession(inputDate);
        String inputStartTime = JOptionPane.showInputDialog("Start time (hh:mm am/pm)");
        trainingSession.addStartTime(inputStartTime);
        String inputEndTime = JOptionPane.showInputDialog("End time (hh:mm am/pm)");
        trainingSession.addEndTime(inputEndTime);
    }

    //EFFECTS: Creates a splitPane that displays the non-editable and editable elements of a
    // TrainingSession side by side
    private void createTrainingSessionSplitPane() {
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                sessionInfoPane, elementListScrollPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        //Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(100, 50);
        sessionInfoPane.setMinimumSize(minimumSize);
        elementListScrollPane.setMinimumSize(minimumSize);

        newTrainingSessionWindow.add(splitPane);
    }

    //MODIFIES: this
    //EFFECTS: Creates a scroll pane of Elements in a given session
    private void getNewElementScrollPane() {
        elementListModel = new DefaultListModel();

        elementList = new JList(elementListModel);
        elementList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        elementList.setSelectedIndex(0);
        elementList.addListSelectionListener(this);
        elementList.setVisibleRowCount(5);

        elementListScrollPane = new JScrollPane(elementList);
        newTrainingSessionWindow.add(elementListScrollPane);
    }

    //MODIFIES: this
    //EFFECTS: Creates relevant buttons for the PracticeTracker page
    private void getPracticeTrackerButtons() {
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));

        createLoadSessionButton();
        createViewSessionButton();
        createNewSessionButton();
        createSavePracticeTrackerButton();

        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        frame.add(buttonPane, BorderLayout.PAGE_END);
    }

    //MODIFIES: this
    //EFFECTS: Creates a button and adds it to the button pane
    public void createLoadSessionButton() {
        JButton loadPracticeTrackerButton = new JButton("Load Practice Tracker");
        ButtonListener buttonListener3 = new ButtonListener(loadPracticeTrackerButton);
        loadPracticeTrackerButton.setActionCommand("Load Practice Tracker");
        loadPracticeTrackerButton.addActionListener(buttonListener3);
        loadPracticeTrackerButton.setEnabled(true);

        buttonPane.add(loadPracticeTrackerButton);
    }

    //MODIFIES: this
    //EFFECTS: Creates a button and adds it to the button pane
    public void createViewSessionButton() {
        viewSessionButton = new JButton("View Session");
        ButtonListener buttonListener = new ButtonListener(viewSessionButton);
        viewSessionButton.setActionCommand("View Session");
        viewSessionButton.addActionListener(buttonListener);
        viewSessionButton.setEnabled(false);

        buttonPane.add(viewSessionButton);
    }

    //MODIFIES: this
    //EFFECTS: Creates a button and adds it to the button pane
    public void createNewSessionButton() {
        JButton newSessionButton = new JButton("Add New Session");
        ButtonListener buttonListener1 = new ButtonListener(newSessionButton);
        newSessionButton.setActionCommand("Add New Session");
        newSessionButton.addActionListener(buttonListener1);
        newSessionButton.setEnabled(true);

        buttonPane.add(newSessionButton);
    }

    //MODIFIES: this
    //EFFECTS: Creates a button and adds it to the button pane
    public void createSavePracticeTrackerButton() {
        JButton savePracticeTrackerButton = new JButton("Save and Exit");
        ButtonListener buttonListener2 = new ButtonListener(savePracticeTrackerButton);
        savePracticeTrackerButton.setActionCommand("Save and Exit");
        savePracticeTrackerButton.addActionListener(buttonListener2);
        savePracticeTrackerButton.setEnabled(true);

        buttonPane.add(savePracticeTrackerButton);
    }

    //MODIFIES: this
    //EFFECTS: Adds a new element to the list of Elements in a TrainingSession
    private void addNewElement() {
        String inputElementName = JOptionPane.showInputDialog("What element did you practice today?");
        element = new Element(inputElementName);
        String inputElementSuccess = JOptionPane.showInputDialog("Success (1-3):");
        element.rateSuccess(Integer.parseInt(inputElementSuccess));

        trainingSession.addElement(element);
    }

    //MODIFIES: this
    //EFFECTS: Creates a pane that displays session info (date, start time, end time)
    private void makeSessionInfoPane() {
        sessionInfoPane = new JPanel();

        JLabel dateLabel = new JLabel(trainingSession.getDate());
        JLabel startTimeLabel = new JLabel("\n Start time: " + trainingSession.getStartTime());
        JLabel endTimeLabel = new JLabel("\n End time: " + trainingSession.getEndTime());

        sessionInfoPane.setLayout(new BoxLayout(sessionInfoPane,
                BoxLayout.Y_AXIS));

        sessionInfoPane.add(Box.createHorizontalStrut(5));
        sessionInfoPane.add(new JSeparator(SwingConstants.HORIZONTAL));
        sessionInfoPane.add(Box.createHorizontalStrut(5));

        sessionInfoPane.add(dateLabel);
        sessionInfoPane.add(startTimeLabel);
        sessionInfoPane.add(endTimeLabel);

        newTrainingSessionWindow.add(sessionInfoPane);
    }

    //EFFECTS: displays a window showing information about a selected element
    private void displayElementInfo() {
        int elementIndex;
        trainingSession = practiceTracker.getTrainingSessionList().get(trainingSessionIndex);
        elementIndex = elementList.getSelectedIndex();
        element = trainingSession.getElementsList().get(elementIndex);

        JOptionPane.showMessageDialog(null, "Element: " + element.getName()
                + "\n Success: " + element.getSuccess());
    }

    //MODIFIES: this
    //EFFECTS: Creates a split pane that displays information about a TrainingSession
    // and its corresponding list of elements side-by-side
    private void createViewTrainingSessionSplitPane() {
        elementListModel = new DefaultListModel();

        for (Element e : trainingSession.getElementsList()) {
            elementListModel.addElement(e.getName());
        }

        elementList = new JList(elementListModel);
        elementList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        elementList.setSelectedIndex(0);
        elementList.addListSelectionListener(this);
        elementList.setVisibleRowCount(5);

        elementListScrollPane = new JScrollPane(elementList);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                sessionInfoPane, elementListScrollPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        //Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(100, 50);
        sessionInfoPane.setMinimumSize(minimumSize);
        elementListScrollPane.setMinimumSize(minimumSize);

        viewSessionWindow.add(splitPane);
    }


    // _______________________________________________________________________________________

    public class ButtonListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public ButtonListener(JButton button) {
            this.button = button;
        }

        public void printLog() {
            for (Event next: EventLog.getInstance()) {
                System.out.println(next.getDate());
                System.out.println(next.getDescription());
            }
        }

        @Override //ActionListener
        public void actionPerformed(ActionEvent e) {

            if (e.getActionCommand().equals("Add New Session")) {
                addTrainingSession();
            } else if (e.getActionCommand().equals("View Session")) {
                viewTrainingSession();
            } else if (e.getActionCommand().equals("Add Element")) {
                addNewElement();
                elementListModel.addElement(element.getName());
            } else if (e.getActionCommand().equals("View Element")) {
                displayElementInfo();
            } else if (e.getActionCommand().equals("Save Session")) {
                sessionListModel.addElement(trainingSession.getDate());
                newTrainingSessionWindow.dispatchEvent(new WindowEvent(newTrainingSessionWindow,
                        WindowEvent.WINDOW_CLOSING));
            } else if (e.getActionCommand().equals("Save and Exit")) {
                savePracticeTracker();
                printLog();
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            } else if (e.getActionCommand().equals("Load Practice Tracker")) {
                loadWorkRoom();
                updatePracticeTrackerScrollPane();
            }
        }

        //DocumentListener
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }


    }

    @Override
    //EFFECTS: disables given button if nothing is selected
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (sessionsList.getSelectedIndex() == -1) {
                //No selection, disable button.
                viewSessionButton.setEnabled(false);

            } else {
                //Selection, enable button.
                viewSessionButton.setEnabled(true);
            }
        }
    }

    // EFFECTS: saves the workroom to file
    private void savePracticeTracker() {
        try {
            jsonWriter.open();
            jsonWriter.write(practiceTracker);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null, "Saved PracticeTrackerApp sessions to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadWorkRoom() {
        try {
            practiceTracker = jsonReader.read();
            JOptionPane.showMessageDialog(null, "Loaded PracticeTrackerApp sessions from " + JSON_STORE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to read from file: " + JSON_STORE);
        }
    }
}
