package com.java_academy.gui;

import com.java_academy.logic.json_model.JsonMessage;
import com.java_academy.logic.json_model.MarkedIndexes;
import com.java_academy.logic.json_model.Message;
import com.java_academy.logic.model.MessageObject;
import com.java_academy.logic.state_machine.core.OnMessageReceiverListener;
import com.java_academy.logic.tools.JsonParser;
import com.java_academy.network.Connector;
import com.java_academy.network.socket_provider.ClientSocketProvider;
import com.java_academy.network.socket_provider.core.SocketProvider;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public class Controller implements Initializable {

    @FXML
    GridPane gridPaneShips;
    @FXML
    GridPane gridPaneShots;
    @FXML
    Button nukeButton;
    @FXML
    Button randomizer;
    @FXML
    TextField ip;
    @FXML
    TextField host;
    @FXML
    Label label;

    private Connector connector;

    private final View view = new View();
    private final Model model = new Model();
    private Map<Integer, Boolean> board;

    public void createFleetRandomly(Map<Integer, Boolean> board, boolean isMy) {
        if(isMy) {
            for (Node n : gridPaneShips.getChildren()) {
                if (n instanceof Pane) {
                    for(Map.Entry<Integer, Boolean> entry: board.entrySet()){
                        if((new Integer(entry.getKey() + 100)).equals(transformationOfSourceIntoInteger(((Pane)n).getId()))) {
                            if(entry.getValue()) {
                                if(board.size() > 9) {
                                    view.drawShips((Pane)n);
                                } else {
                                    view.drawShot((Pane)n);
                                }
                            } else {
                                view.drawMiss((Pane)n);
                            }
                        }
                    }
                }
            }
        } else {
            for (Node n : gridPaneShots.getChildren()) {
                if (n instanceof Pane) {
                    for (Map.Entry<Integer, Boolean> entry : board.entrySet()) {
                        if (entry.getKey().equals(transformationOfSourceIntoInteger(((Pane) n).getId()))) {
                            if (entry.getValue()) {
                                view.drawShot((Pane) n);
                            } else {
                                view.drawMiss((Pane) n);

                                setButtonsDisabled(true);
                                connector.sendMessage(new MessageObject(null, "to stanPosredni"));
                            }
                        }
                    }
                }
            }
        }
        randomizer.setDisable(true);
    }

    public void showNuke() {
        System.out.println("nukeeee");
    }

    public void onShootHandled(MouseEvent event) {
        Object source = event.getSource();
        Integer id = transformationOfSourceIntoInteger(source);
//        System.out.println("Kliknalem id: " + id);
        connector.sendMessage(new MessageObject(null, ""+id));
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        connector.sendMessage(new MessageObject(null, ""+id));
    }

    public void onShipPlaceHandled(MouseEvent event) {
        System.out.println("event = ship placed");
    }

    public Integer transformationOfSourceIntoInteger(Object o) {
        return Integer.valueOf(o.toString().replaceAll("\\D+", ""));
    }

    public void connectToServer() {
        //view.setLabelText("new.game",label);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 3000);
        startListeningFromServer();
        connector.connect(inetSocketAddress);
        connector.sendMessage(new MessageObject(null, "dziala"));
        connector.sendMessage(new MessageObject(null,"polaczylem sie prosze o statki"));
        setButtonsDisabled(false);

    }

    private void startListeningFromServer() {
        connector.addMessageReceiverListenerToSocketProvider(new OnMessageReceiverListener() {
            @Override
            public void onMessageReceived(Supplier<String> messageSupplier) {
                String json = messageSupplier.get();
                System.out.println(json);

                JsonMessage jsonMsg = JsonParser.decide(json);
                if (jsonMsg instanceof MarkedIndexes) {
                    MarkedIndexes mi = ((MarkedIndexes)jsonMsg);
                    if(mi.isMyBoard()) {
                        board = mi.getMap();
                        createFleetRandomly(board, true);
                    } else {
                        board = mi.getMap();
                        createFleetRandomly(board, false);
                    }
                } else {
                    //view.setLabelText(((Message)jsonMsg).getMessage(),label);

                    if(((Message)jsonMsg).getMessage().equals("not.your.turn")) {
                        setButtonsDisabled(true);
                    }
                    if(((Message)jsonMsg).getMessage().equals("your.turn")) {
                        setButtonsDisabled(false);
                    }

                    if(((Message)jsonMsg).getMessage().equals("you.win") || ((Message)jsonMsg).getMessage().equals("you.lose")) {
                        //TODO SEND message to server about end of game from each player
                    }
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Socket socket = new Socket();
        SocketProvider socketProvider = new ClientSocketProvider(socket);
        connector = new Connector(socketProvider);
        setButtonsDisabled(true);
       // view.setLabelText("hello.world",label);
    }

    private void setButtonsDisabled(boolean flag) {
        randomizer.setDisable(flag);
        nukeButton.setDisable(flag);
        gridPaneShips.setDisable(flag);
        gridPaneShots.setDisable(flag);
    }
}
