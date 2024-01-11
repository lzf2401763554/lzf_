package com.work;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HighwayApplication extends Application {
    /**
     * 加载并显示登录界面
     * @param stage 舞台变量
     * @throws IOException 如果发生异常，抛出输入/输出异常
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HighwayApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 479, 340);
        stage.setTitle("公路信息管理系统——刘柱锋开发");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *  使用launch（）方法，加载窗口
     * @param args 无实际意义
     */
    public static void main(String[] args) {
        launch();
    }
}