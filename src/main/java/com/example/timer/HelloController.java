package com.example.timer;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class HelloController {

    @FXML
    public MenuItem operationReboot;

    @FXML
    public Label secLabel;

    @FXML
    public Label minLabel;

    @FXML
    private MenuItem fifteenMin;

    @FXML
    private MenuItem fiveHours;

    @FXML
    private MenuItem fiveMin;

    @FXML
    private MenuItem fortyFiveMin;

    @FXML
    private MenuButton getTime;

    @FXML
    private MenuItem oneHour;

    @FXML
    private MenuItem oneHourAndHalf;

    @FXML
    private MenuButton operation;

    @FXML
    private MenuItem operationTXT;

    @FXML
    private MenuItem operationTurnOff;

    @FXML
    private MenuItem tenMin;

    @FXML
    private MenuItem thirtyMin;

    @FXML
    private MenuItem threeHours;

    @FXML
    private Button turnOff;

    @FXML
    private Button turnOn;

    @FXML
    private MenuItem twentyMin;

    @FXML
    private MenuItem twoHours;

    @FXML
    private Label timerLabel;


    private AtomicInteger timer = new AtomicInteger();
    private Timeline timeline;
    private IntegerProperty timeSeconds;

    @FXML
    void initialize() {

//--------------------------------------------Задать время--------------------------------


        fiveMin.setOnAction(actionEvent -> {
            getTime.setText("5 минут");
            timer.set(300);
        });
        tenMin.setOnAction(actionEvent -> {
            getTime.setText("10 минут");
            timer.set(600);
        });
        fifteenMin.setOnAction(actionEvent -> {
            getTime.setText("15 минут");
            timer.set(900);
        });
        twentyMin.setOnAction(actionEvent -> {
            getTime.setText("20 минут");
            timer.set(1200);
        });
        thirtyMin.setOnAction(actionEvent -> {
            getTime.setText("30 минут");
            timer.set(1800);
        });
        fortyFiveMin.setOnAction(actionEvent -> {
            getTime.setText("45 минут");
            timer.set(2700);
        });
        oneHour.setOnAction(actionEvent -> {
            getTime.setText("1 час");
            timer.set(3600);
        });
        oneHourAndHalf.setOnAction(actionEvent -> {
            getTime.setText("1 час 30 минут");
            timer.set(5400);
        });
        twoHours.setOnAction(actionEvent -> {
            getTime.setText("2 часа");
            timer.set(7200);
        });
        threeHours.setOnAction(actionEvent -> {
            getTime.setText("3 часа");
            timer.set(10800);
        });
        fiveHours.setOnAction(actionEvent -> {
            getTime.setText("5 часов");
            timer.set(18000);
        });

//--------------------------------------------Выбрать операцию--------------------------------
//        operationTXT.setOnAction(actionEvent -> {
//            operation.setText("Запустить test.txt");
//        });
        operationTurnOff.setOnAction(actionEvent -> {
            operation.setText("Завершение работы ПК");
        });
        operationReboot.setOnAction(actionEvent -> {
            operation.setText("Перезагрузка ПК");
        });

//--------------------------------При нажатиии на кнопку Включить таймер - она меняется на кнопку "Выыключить такймер"-----------------------------------

        turnOn.setOnAction(actionEvent -> {
            if (turnOn.getText().equals("Включить таймер") && !operation.getText().equals("Выполнить") && !getTime.getText().equals("Задать время")) {
                turnOn.setText("Выключить таймер");
                turnOn.setTextFill(Paint.valueOf("#832A27"));

                //Если время и операция выбраны - запускаем таймер, привязанный через IntegerProperties к Label с остатком времени

                if (timer.get()>0  && !operation.getText().equals("Выполнить")){
                    timeSeconds = new SimpleIntegerProperty(timer.get());
                    timerLabel.textProperty().bind(timeSeconds.divide(3600).asString());
                    minLabel.textProperty().bind(timeSeconds.divide(60).subtract(timeSeconds.divide(3600).multiply(60)).asString());
                    secLabel.textProperty().bind(timeSeconds.subtract(timeSeconds.divide(60).multiply(60)).asString());
                    timeline = new Timeline();
                    timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(timer.get()+1), new KeyValue(timeSeconds, 0)));
                    timeline.playFromStart();

                    //По окончании таймера - выполняет операцию, которая указана в поле "Выполнить". ВНИМАНИЕ! Выполняетсся та операция, которая выбрана в данный момент,
//                    т.е.можно сменить операцию после запуска таймера.

                    timeline.setOnFinished(actionEvent1 -> {
//                        if (operation.getText().equals("Запустить test.txt")) {
//                            try {
//                                Runtime.getRuntime().exec("cmd /c start /MIN C:\\JavaApps\\Timer\\test.txt");
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
                        if (operation.getText().equals("Завершение работы ПК")){
                            try {
                                Runtime.getRuntime().exec(new String[]{"shutdown", "-s" });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (operation.getText().equals("Перезагрузка ПК")){
                            try {
                                Runtime.getRuntime().exec(new String[]{"shutdown", "-r" });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }else {
//если таймер запущен - кнопка его запуска заменена на кнопку выключения таймера.
//тут происходит обратное, меняем кнопку обратно, состояния полей приводим к дефолтным
                turnOn.setText("Включить таймер");
                turnOn.setTextFill(Paint.valueOf("#22830f"));
                timeline.stop();
                timeline = null;
                timerLabel.textProperty().unbind();
                timerLabel.setText(" ");
                secLabel.textProperty().unbind();
                secLabel.setText(" ");
                minLabel.textProperty().unbind();
                minLabel.setText(" ");
                getTime.setText("Задать время");
                operation.setText("Выполнить");
                turnOff.setText("Пауза");

            }
        });

//кнопка паузы таймера
        turnOff.setOnAction(actionEvent -> {
            if (turnOff.getText().equals("Пауза")) {
                timeline.pause();
                turnOff.setText("Продолжить");
            }else {
                timeline.play();
                turnOff.setText("Пауза");
            }
        });


    }
}
