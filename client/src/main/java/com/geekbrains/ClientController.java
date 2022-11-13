package com.geekbrains;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.geekbrains.ClientApp.log;

public class ClientController implements Initializable {

    @FXML
    private Button authButton;

    @FXML
    private HBox authPanel;

    @FXML
    private ListView<String> listServerFiles;

    @FXML
    private ListView<String> listСlientFiles;

    @FXML
    private TextField loginField;

    @FXML
    private TextField loginRegField;

    @FXML
    private TextField nickRegField;

    @FXML
    private PasswordField passField;

    @FXML
    private TextField passRegField1;

    @FXML
    private TextField passRegField2;

    @FXML
    private Button registerButton;

    @FXML
    private HBox storagePanel;

    private String nickName;

    private static void updateUI(Runnable r) {
        if (Platform.isFxApplicationThread()) {
            r.run();
        } else {
            Platform.runLater(r);
        }
    }

    private void updateServerFilesList(ArrayList<String> fileList) {
        updateUI(() -> {
            listServerFiles.getItems().clear();
            listServerFiles.getItems().addAll(fileList);
        });
    }

    private void updateClientFilesList() {
        updateUI(() -> {
            listСlientFiles.getItems().clear();
            try {
                Files.list(Paths.get("ClientStorage-" + nickName)).map(p -> p.getFileName().toString())
                        .forEach(f -> listСlientFiles.getItems().add(f));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @FXML
    void closeConnection(ActionEvent event) {
        Network.setOpened(false);
        Network.sendMsg(new AuthMsg("/closeConnection"));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Network.stop();
        log.info(nickName +" отключился!");
        System.exit(0);


//
////        ClientApp.launch();

    }

    @FXML
    void deleteOnClient(ActionEvent event) {

        try {
            Files.delete(Paths.get("ClientStorage-" + nickName + "/" + listСlientFiles.getSelectionModel().getSelectedItem()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateClientFilesList();
    }


    @FXML
    void deleteOnServer(ActionEvent event) {
        Network.sendMsg(new DeleteMsg(listServerFiles.getSelectionModel().getSelectedItem()));
    }

    @FXML
    void downloadFromServer(ActionEvent event) {
        Network.sendMsg(new DownloadMsg(listServerFiles.getSelectionModel().getSelectedItem()));

    }

    @FXML
    void sendToServer(ActionEvent event) {
        try {
            Network.sendMsg(new FileMsg(Paths.get("ClientStorage-" + nickName + "/"
                    + listСlientFiles.getSelectionModel().getSelectedItem())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logIn(ActionEvent event) {
        Network.sendMsg(new AuthMsg(loginField.getText(), passField.getText()));
        loginField.clear();
        passField.clear();

    }

    @FXML
    void registerOnServer(ActionEvent event) {
        if (passRegField1.getText().equals(passRegField2.getText())) {
            Network.sendMsg(new RegistrationMsg(loginRegField.getText(),
                    passRegField1.getText(),
                    nickRegField.getText()));
        } else {
            System.out.println("Ошибка ввода данных");
        }

    }

    private void setAuth(boolean isAuth) {
        if (!isAuth) {
            authPanel.setVisible(true);
            authPanel.setManaged(true);
            storagePanel.setVisible(false);
            storagePanel.setManaged(false);
        } else {
            authPanel.setVisible(false);
            authPanel.setManaged(false);
            storagePanel.setVisible(true);
            storagePanel.setManaged(true);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAuth(false);
        Network.start();
        Thread tr = new Thread(() -> {
            try {
                while (Network.isOpened()) {
                    AbstractMsg abstractMsg =  Network.readAbstractMsg();
                    log.info(abstractMsg.getClass().getSimpleName());//LOG

                    if (abstractMsg instanceof RegistrationMsg) {
                        RegistrationMsg registrationMsg = (RegistrationMsg) abstractMsg;
                        if (registrationMsg.message.equals("/notNullUser")) {
                            Platform.runLater(() -> registerButton.setText("Ник занят!"));
                        } else {
                            String nickName = registrationMsg.message.split(" ")[1];
                            Files.createDirectory(Paths.get("ClientStorage-" + nickName));
                            Platform.runLater(() -> registerButton.setText("Успех!"));
                        }
                    }
                    if (abstractMsg instanceof AuthMsg) {
                        AuthMsg authMsg = (AuthMsg) abstractMsg;
                        if (authMsg.message.startsWith("/authOk")) {
                            setAuth(true);
                            nickName = authMsg.message.split(" ")[1];
                            log.info(nickName + " подключился!");
                            break;
                        }
                        if ("/nullUser".equals(authMsg.message)) {
                            Platform.runLater(() -> authButton.setText("Ошибка!"));
                        }
                    }
                }

                Network.sendMsg(new UpdateFileListMsg());
                updateClientFilesList();

                while (true) {
                    AbstractMsg abstractMsg = Network.readAbstractMsg();
                    if (abstractMsg instanceof FileMsg) {
                        FileMsg fileMessage = (FileMsg) abstractMsg;
                        if (!Files.exists(Paths.get("ClientStorage-" + nickName + "/" + fileMessage.getFileName()))) {
                            Files.write(Paths.get("ClientStorage-" + nickName + "/" + fileMessage.getFileName()),
                                    fileMessage.getData(), StandardOpenOption.CREATE);
                            updateClientFilesList();
                        }
                    }
                    if (abstractMsg instanceof UpdateFileListMsg) {
                        UpdateFileListMsg updateFileListMsg = (UpdateFileListMsg) abstractMsg;
                        updateServerFilesList(updateFileListMsg.getServerFileList());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Ошибка");
            } finally {
                Network.setOpened(false);
                Network.stop();
            }
        });
        tr.setDaemon(true);
        tr.start();


    }

}
