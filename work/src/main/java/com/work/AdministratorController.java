package com.work;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdministratorController {
    private String iPhone;
    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField age;
    @FXML
    private TextField username;
    @FXML
    private TextField phone;
    @FXML
    private TextField area;
    @FXML
    private TextField password;
    @FXML
    private MenuButton sex;
    @FXML
    private Button jbxx;
    @FXML
    private Button wxgl;
    @FXML
    private Button sgxx;
    @FXML
    private Button update_;
    @FXML
    private Label label;
@FXML
private AnchorPane an;

    /**
     * 初始化界面，设置修改个人信息的修改不可选，显示登录该系统的管理员的辖区和名称
     */
    @FXML
    private void initialize(){
        update_.setDisable(true);
       label.setText("辖区：" + LoginController.Information.getInstance().getILocation() + "\n" + "管理员：" + LoginController.Information.getInstance().getIName());
    }

    /**
     * 根据点击相应按钮，调出相应系统界面，点击后该按钮不可选直到相应系统界面关闭
     * @throws IOException 如果发生异常，抛出输入/输出异常
     */
    @FXML
    private void information() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("roads.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 863, 487);
        stage.setTitle("公路基本信息");
        stage.setScene(scene);
        stage.show();
        jbxx.setDisable(true);
        stage.setOnCloseRequest(event -> {
            jbxx.setDisable(false);
            // 在窗口关闭时执行需要的操作
        });
    }
    /**
     * 根据点击相应按钮，调出相应系统界面，点击后该按钮不可选直到相应系统界面关闭
     * @throws IOException 如果发生异常，抛出输入/输出异常
     */
    @FXML
    private void maintenance() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("maintenance.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1020, 502);
        stage.setTitle("公路维修管理");
        stage.setScene(scene);
        stage.show();
        wxgl.setDisable(true);
        stage.setOnCloseRequest(event -> {
            wxgl.setDisable(false);
            // 在窗口关闭时执行需要的操作
        });
    }
    /**
     * 根据点击相应按钮，调出相应系统界面，点击后该按钮不可选直到相应系统界面关闭
     * @throws IOException 如果发生异常，抛出输入/输出异常
     */
    @FXML
    private void accident() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("accident.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 898, 429);
        stage.setTitle("公路事故信息");
        stage.setScene(scene);
        stage.show();
        sgxx.setDisable(true);
        stage.setOnCloseRequest(event -> {
           sgxx.setDisable(false);
            // 在窗口关闭时执行需要的操作
        });
    }

    /**
     * 点击个人信息选项后，输出个人信息，但密码不可查看
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @FXML
    private void putInformation() throws SQLException {
       LoginJDBC loginJDBC = new LoginJDBC();
       List list = loginJDBC.getByCondition("*","where username='" + LoginController.Information.getInstance().getUserName() + "'");
        Administrator administrator=(Administrator)list.get(0);
       JOptionPane.showMessageDialog(null,"ID："+administrator.getAdministrator_id()
                +"\n姓名："+administrator.getAdministrator_name()+"\n年龄："+administrator.getAge()+"\n性别："+administrator.getSex()
                +"\n电话："+administrator.getPhone()+"\n辖区："+administrator.getCity()+"\n用户名："+administrator.getUsername()+"\n密码：******");
    }

    /**
     * 点击修改个人信息选项后，然后显示修改界面
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @FXML
    private void updateA()throws SQLException{
       an.setVisible(true);
        LoginJDBC loginJDBC = new LoginJDBC();
        List list = loginJDBC.getByCondition("*","where username='" + LoginController.Information.getInstance().getUserName() + "'");
        Administrator administrator=(Administrator)list.get(0);
        id.setText(administrator.getAdministrator_id()+"");
        name.setText(administrator.getAdministrator_name());
        age.setText(administrator.getAge()+"");
        sex.setText(administrator.getSex());
        username.setText(administrator.getUsername());
        password.setText(administrator.getPassword());
        area.setText(administrator.getCity());
        phone.setText(administrator.getPhone());
        isNull();
        iPhone=phone.getText();
    }

    /**
     * 根据选择的性别，设置选择框的信息，并调用isNull（）
     * @param event 事件，用于性别选择
     */
    @FXML
    private void set_(ActionEvent event){
        sex.setText(((MenuItem)event.getSource()).getText());
        isNull();
    }

    /**
     * 点击修改选项后，然后根据文本框的数据更新
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @FXML
    private void update_A()throws SQLException{
        LoginJDBC loginJDBC = new LoginJDBC();
    List list = loginJDBC.getByCondition(" phone ","");
  if(!list.contains(phone.getText())||iPhone.equals(phone.getText())) {
      loginJDBC.update_1(new Administrator(Integer.parseInt(id.getText()), name.getText(), Integer.parseInt(age.getText()), sex.getText(), phone.getText(), area.getText(), username.getText(), password.getText()));
  JOptionPane.showMessageDialog(null,"修改成功！");
      LoginController.Information.getInstance().setIName(name.getText());
      initialize();
  shut_();
  }
else JOptionPane.showMessageDialog(null,"电话号码重复！");
}
    /**
     * 判断所有框是否填写，根据结果设置修改是否可选
     */
    @FXML
    private void isNull(){
    if(name.getText()!=null
            && sex.getText()!=null
            && age.getText()!=null
            && password.getText()!=null
            && phone.getText()!=null) {
        if (!name.getText().isEmpty()
                && !sex.getText().isEmpty()
                && !age.getText().isEmpty()
                && !password.getText().isEmpty()
                && !phone.getText().isEmpty()) update_.setDisable(false);
        else update_.setDisable(true);
    }
    else update_.setDisable(true);
}

    /**
     * 点击关闭按钮，关闭修改界面
     */
    @FXML
    private void shut_(){
        an.setVisible(false);
    }
}

