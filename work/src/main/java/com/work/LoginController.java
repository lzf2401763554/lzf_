package com.work;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Data;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class LoginController {
 private static String userName="";
 private static String name="";
 private static String location="";
 @FXML
 private  AnchorPane login;
@FXML
 private TextField yhm;
 @FXML
 private TextField mm;
 @FXML
 private Button dl;

 /**
  * 界面初始化，设置登录不可选
  */
 @FXML
 private void initialize(){
 dl.setDisable(true);
}

 /**
  * 判断所有文本框是否填写，根据结果设置登录是否可选
  */
 @FXML
 private void isNull(){
 if(!yhm.getText().isEmpty() && !mm.getText().isEmpty())dl.setDisable(false);
 else dl.setDisable(true);
}

 /**
  *登录功能，根据多个查询结果判断用户名是否存在，存在则匹配对应密码，匹配成功则打开系统界面
  * @throws SQLException 如果发生异常，抛出SQL异常
  * @throws IOException 如果发生异常，抛出输入/输出异常
  */
 @FXML
 private void login_() throws SQLException, IOException {
 LoginJDBC jdbc = new LoginJDBC();
if (!jdbc.getByCondition("username","").contains(yhm.getText())){ //判断用户名是否存在
 Alert alert = new Alert(Alert.AlertType.WARNING);
 alert.setHeaderText("用户名不存在！");
 alert.showAndWait();
 return;
}
//获取密码
String password =(String) jdbc.getByCondition("password","where username='" + yhm.getText() + "'").get(0);
if(password.equals(mm.getText())){
 //设置后续系统的基本信息（管理员名称，辖区，用户名）
 name = (String) jdbc.getByCondition("administrator_name","where username='" + yhm.getText() + "'").get(0);
 userName = yhm.getText();
 location = (String) jdbc.getByCondition("area","where username='" + yhm.getText() + "'").get(0);
 Information.getInstance().setIName(name);
 Information.getInstance().setUserName(userName);
 Information.getInstance().setILocation(location);
 //打开后续系统界面
 Stage stage = new Stage();
 FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("administrator.fxml"));
 Scene scene = new Scene(fxmlLoader.load(), 600, 400);
 stage.setTitle("公路信息管理系统——刘柱锋开发");
 stage.show();
 stage.setScene(scene);
 Stage stage1 = (Stage)(dl.getScene().getWindow());
stage1.hide();//后续系统打开，登录界面隐藏
 login.setDisable(true);
 stage.setOnCloseRequest(event -> {
  // 事件监听，在后续系统界面关闭时显示登录界面并清除密码
  login.setDisable(false);
  stage1.show();
  mm.setText("");
  isNull();
 });
}
else {
 Alert alert = new Alert(Alert.AlertType.WARNING);
 alert.setHeaderText("密码错误！");
 alert.showAndWait();
}
}

 /**
  * 注册功能，判断是否注册过，用户名是否存在以及信息检查
  * @throws SQLException 如果发生异常，抛出SQL异常
  */
 @FXML
private void enroll() throws SQLException {
 LoginJDBC loginJDBC = new LoginJDBC();
 List list = loginJDBC.getByCondition("administrator_id", "");
 List phone1 = loginJDBC.getByCondition("phone", "");
 String id = JOptionPane.showInputDialog("请输入管理员ID！");
 if (id==null)return;
 String phone = JOptionPane.showInputDialog("请输入管理员电话！");
 if (phone==null)return;
  if (list.contains(id)&&phone1.contains(phone)) {
   list = loginJDBC.getByCondition("username", "where administrator_id=" + id);

   if (!(list.get(0) == null || list.get(0).equals(""))) {
    JOptionPane.showMessageDialog(null, "该用户ID已注册！");
    return;
   }
   while (true) {
    String uName = JOptionPane.showInputDialog("请输入要注册的用户名！");
    if (uName==null)return;
    String uPassword = JOptionPane.showInputDialog("请输入要注册的密码");
    if (uPassword==null)return;
    list = loginJDBC.getByCondition("username", "");
    if (list.contains(uName)) JOptionPane.showMessageDialog(null, "该用户名已存在！");
    else {loginJDBC.update_(id, uName, uPassword);JOptionPane.showMessageDialog(null,"注册成功！");return;}
   }
  }
  else {
   JOptionPane.showMessageDialog(null,"管理员ID或电话错误！");
  }
}

@Data
 public static class  Information{
private  String iName=LoginController.name;
private String userName = LoginController.userName;
private  String iLocation=LoginController.location;
  // 在类加载时就创建实例
  private static final Information instance = new Information();

  // 私有构造函数，防止外部通过构造函数创建新实例
  private Information() {}

 /**
  * 单例模式，用于后续界面，让后续界面获取管理员的部分信息
  * @return 返回储存了信息的内部私有构造的实体
  */
  // 公共方法用于获取实例
  public static Information getInstance() {

   return instance;

 }
}

 /**
  * 忘记密码操作，根据管理员ID、电话以及用户名忘记密码，判断信息是否错误和用户是否注册
  * @throws SQLException 如果发生异常，抛出SQL异常
  */
 @FXML
 private void updatePassword()throws SQLException{
 LoginJDBC loginJDBC = new LoginJDBC();
 List list = loginJDBC.getByCondition("administrator_id", "");
 List phone1 = loginJDBC.getByCondition("phone", "");
 String id = JOptionPane.showInputDialog("请输入管理员ID！");
 if (id==null)return;
 String phone = JOptionPane.showInputDialog("请输入管理员电话！");
 if (phone==null)return;
 if (list.contains(id)&&phone1.contains(phone)) {
  list = loginJDBC.getByCondition("username", "where administrator_id=" + id);

  if (!(list.get(0) == null || list.get(0).equals(""))) {
   while (true) {
    String uName = JOptionPane.showInputDialog("请输入用户名！");
    if (uName==null)return;
    String uPassword = JOptionPane.showInputDialog("请输入要修改的密码");
    if (uPassword==null)return;
    list = loginJDBC.getByCondition("username", "where administrator_id="+id);
    if (!list.contains(uName)) JOptionPane.showMessageDialog(null, "用户名错误！");
    else {loginJDBC.update_(id, uName, uPassword);JOptionPane.showMessageDialog(null,"修改成功！");return;}
   }
  }
  else {
   JOptionPane.showMessageDialog(null, "该用户ID未注册！");
  }
 }
 else {
  JOptionPane.showMessageDialog(null,"管理员ID或电话错误！");
 }

}
}
