package csc207.todo_list;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;

public class TodoListPanel extends JPanel implements ActionListener {
    public static final String DONE = " (done)";
    public static final String SAVE_DIR = "saves";
    public static final String SAVEFILE_TODO_LIST_JSON = SAVE_DIR + File.separator + "todo_list.json";
    private final JTextField textField;
    private final DefaultListModel<Task> textModel;
    private TODOList tasks = new TODOList();

    public TodoListPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        textField = new JTextField(20);
        textField.addActionListener(this); // JTextFields fire an ActionEvent when the user types Enter

        textModel = new DefaultListModel<Task>();
        loadJsonFromFile();

        JList<Task> textList = new JList<>(textModel);
        JScrollPane scrollPane = new JScrollPane(textList);

        ListSelectionModel listSelectionModel = textList.getSelectionModel();
        listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSelectionModel.addListSelectionListener(new  ListSelectionListener() {
                                                        /**
                                                         * Called whenever the value of the selection changes.
                                                         *
                                                         * @param e the event that characterizes the change.
                                                         */
                                                        @Override
                                                        public void valueChanged(ListSelectionEvent e) {
                                                            selectItem(textList);
                                                        }
                                                    }
                                                    );

        textList.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_DELETE || evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    tasks.removeTask(textList.getSelectedValue());
                    update();
                } else if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
                    textList.getSelectedValue().toggleDone();
                    update();
                }
            }
        });

        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {

            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });

        JButton edit = new JButton("Edit");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textList.getSelectedValue().edit(textField.getText());
                update();
            }
        });

        add(textField);
        add(scrollPane);
        add(save);
        add(edit);
    }

    private void loadJsonFromFile() {
        ensureJsonExists();
        JSONArray jsonArray = readJsonFile();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString("task");
            int date = jsonObject.getInt("date");
            boolean completed = jsonObject.getBoolean("completed");
            int priority = jsonObject.getInt("priority");
            if (completed) {
                name += DONE;
            }
            textModel.addElement(new  Task(name, date, priority));
        }
    }

    private static void ensureJsonExists() {
        Path resourcesDir = Paths.get(SAVE_DIR);
        if (!Files.exists(resourcesDir)) {
            try {
                Files.createDirectories(resourcesDir);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create save file directory", e);
            }
        }

        Path resourcesJsonFile = Paths.get(SAVEFILE_TODO_LIST_JSON);
        if (!Files.exists(resourcesJsonFile)) {
            try {
                Files.createFile(resourcesJsonFile);
                Files.write(resourcesJsonFile, "[]".getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create todo_list.json file", e);
            }
        }
    }

    private JSONArray readJsonFile() {
        String jsonString;
        try {
            jsonString = Files.readString(Paths.get(SAVEFILE_TODO_LIST_JSON));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new JSONArray(jsonString);
    }

    private void save() {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < textModel.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            String name = textModel.getElementAt(i).getName();
            int priority = textModel.getElementAt(i).getPriority();
            int date =  textModel.getElementAt(i).getDueDate();
            boolean done = textModel.getElementAt(i).isDone();
            jsonObject.put("task", name.replace(DONE, "").trim());
            jsonObject.put("completed", done);
            jsonObject.put("priority", priority);
            jsonObject.put("date", date);
            jsonArray.put(jsonObject);
        }

        try {
            FileWriter fileWriter = new FileWriter(SAVEFILE_TODO_LIST_JSON);
            String json = jsonArray.toString();
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void selectItem(JList<Task> textList) {
        int selectedIndex = textList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedText = textModel.getElementAt(selectedIndex).getName();
            textField.setText(selectedText);
        }
    }

    public void actionPerformed(ActionEvent evt) {
        String text = textField.getText();
        tasks.addTask(new Task(text, 0, 20000101));
        update();
        textField.selectAll();
    }

    public void sortBy(int ver){
        tasks.sortTasks(ver);
        update();
    }

    public void update(){
        textModel.clear();
        for (Task t : tasks.getTasks()) {
            textModel.addElement(t);
        }
    }

    public void filter(){
        textModel.clear();
        for (Task t : tasks.getTasks()) {
            if (!t.isDone()){
                textModel.addElement(t);
            }
        }
    }
}
