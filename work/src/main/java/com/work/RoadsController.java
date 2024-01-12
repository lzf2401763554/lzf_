package com.work;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

/**
 * 公路信息系统控制类
 */
public class RoadsController<T> {
    Roads roads = new Roads();
    RoadsJDBC roadsJDBC = new RoadsJDBC();
    public static int itemsPerPage = 20;//每页显示的数据条数
    List<Roads> list = roadsJDBC.getAll();
    @FXML
    private ContextMenu cMenu;
    @FXML
 private TableView tableView;
    @FXML
    private Pagination pageTable;
@FXML
private TableColumn<Roads,Integer> id;
    @FXML
    private TableColumn<Roads,String> name;
    @FXML
    private TableColumn<Roads,String> location_;
    @FXML
    private TableColumn<Roads,String> sp;
    @FXML
    private TableColumn<Roads,String> terminal;
    @FXML
    private TableColumn<Roads,String> length;
    @FXML
    private TableColumn<Roads,String> situation;
    @FXML
    private TableColumn<Roads,String> grade;
@FXML
private Button button1;
    @FXML
    private TextField id1;
    @FXML
    private TextField name1;
    @FXML
    private TextField location1;
    @FXML
    private TextField sp1;
    @FXML
    private TextField terminal1;
    @FXML
    private TextField length1;
    @FXML
    private MenuButton grade1;

    public RoadsController() throws SQLException {
    }

    /**
     * 设置添加按钮不可选，初始化公路基本信息的数据库，加载分页
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @FXML
    private void initialize() throws SQLException {
button1.setDisable(true);
roadsJDBC.init();
        initializePagination();
        loadPageData(0);
    }

    /**
     * 初始化分页，设置分页监听，点击页码会刷新分页，加载对应页码
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    private void initializePagination() throws SQLException {
// 设置分页控件的页数
        pageTable.setPageCount(roadsJDBC.calculatePageCount());
// 监听页码变化事件，当页码变化时刷新表格数据
        pageTable.currentPageIndexProperty().addListener((observable,
                                                          oldValue, newValue) -> {
// 根据新的页码加载数据
            try {
                loadPageData(newValue.intValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 加载相应页码的数据
     * @param pageIndex 要加载的页码
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    private void loadPageData(int pageIndex) throws SQLException {
        tableView.getItems().clear();
        list = roadsJDBC.getPage(pageIndex, itemsPerPage);
        updateTableView();
    }

    /**
     * 更新界面表的数据，设置多选
     */
    private void updateTableView() {
        cMenu.getItems().get(0).setVisible(true);
        cMenu.getItems().get(1).setVisible(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
       tableView.getItems().addAll(list);
        id.setCellValueFactory(new PropertyValueFactory<>("road_id"));
        name.setCellValueFactory(new PropertyValueFactory<>("road_name"));
        location_.setCellValueFactory(new PropertyValueFactory<>("location"));
        sp.setCellValueFactory(new PropertyValueFactory<>("starting_point"));
        terminal.setCellValueFactory(new PropertyValueFactory<>("terminal"));
        length.setCellValueFactory(new PropertyValueFactory<>("length"));
        situation.setCellValueFactory(new PropertyValueFactory<>("accident_situation"));
        grade.setCellValueFactory(new PropertyValueFactory<>("road_grade"));
        list.removeAll(list);
    }

    /**
     * 根据新的总页数刷新分页，加载当页数据
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    private void refreshTableView() throws SQLException{

// 获取当前页码lse
        int currentPageIndex = pageTable.getCurrentPageIndex();
// 获取新的总页数
        int newTotalPages = roadsJDBC.calculatePageCount();
// 更新分页控件的页数
        pageTable.setPageCount(newTotalPages);
// 重新加载当前页数据
        loadPageData(currentPageIndex);
// 如果当前页为空且不是第一页，将页码减一，重新加载上一页数据
        if (tableView.getItems().isEmpty() &&
                currentPageIndex > 0) {
            loadPageData(currentPageIndex - 1);
            pageTable.setCurrentPageIndex(currentPageIndex - 1);
        }
    }
    /**
     * 根据选择的道路等级，设置选择框的信息，并调用isNull（）
     * @param event 事件，用于道路等级选择
     */
    @FXML
    private void set_(ActionEvent event){
        grade1.setText(((MenuItem)event.getSource()).getText());
        isNull();
    }
    /**
     * 判断所有框是否填写，根据结果设置添加或修改是否可选
     */
    @FXML
    private void isNull(){
        if(name1.getText()!=null
                && length1.getText()!=null
                && location1.getText()!=null
                && sp1.getText()!=null
                && terminal1.getText()!=null
                && grade1.getText()!=null) {
            if (!id1.getText().isEmpty()
                    && !name1.getText().isEmpty()
                    && !length1.getText().isEmpty()
                    && !location1.getText().isEmpty()
                    && !sp1.getText().isEmpty()
                    && !terminal1.getText().isEmpty()
                    && !grade1.getText().isEmpty()) button1.setDisable(false);
            else button1.setDisable(true);
        }
        else button1.setDisable(true);
    }

    /**
     * 判断调用添加还是更新
     * @param event 事件，用于获取选择按钮的信息
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @FXML
    private void select(ActionEvent event) throws SQLException {
        Button button1 = (Button) event.getSource();
        if (button1.getText().equals("新增信息")) add();
        else updateTable();
    }

    /**
     * 右键选择更新后，添加按钮改为更新按钮，并让所有框获取对应数据
     */
    @FXML
    private void update(){
        button1.setText("保存编辑");
        roads = (Roads) tableView.getSelectionModel().getSelectedItems().get(0);
        id1.setText("" + roads.getRoad_id());
        name1.setText(roads.getRoad_name());
        location1.setText(roads.getLocation());
        sp1.setText(roads.getStarting_point());
        terminal1.setText(roads.getTerminal());
        length1.setText(roads.getLength());
        grade1.setText(roads.getRoad_grade());
        isNull();
        id1.setDisable(true);
    }

    /**
     * 根据主键和实体更新，刷新界面表
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    private void updateTable()throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("确定要保存编辑吗？");
// 显示对话框，并等待按钮返回
        Optional<ButtonType> result = alert.showAndWait();
// 判断返回的按钮类型是确定还是取消，再据此分别进一步处理
        if (result.get() == ButtonType.OK) {
            if (!location1.getText().equals(LoginController.Information.getInstance().getILocation())){
                Alert alert1 = new Alert(Alert.AlertType.WARNING);
                alert1.setHeaderText("辖区权限不足！");
                alert1.showAndWait();
                return;
            }
            tableView.getItems().removeAll(roadsJDBC.getAll());
            Roads road = new Roads(Integer.parseInt(id1.getText()), name1.getText(),location1.getText(), sp1.getText(), terminal1.getText(),length1.getText(),"",grade1.getText());
            roadsJDBC.updateByKey(road.getRoad_id(), road);
           refresh_();
        }
    }
    /**
     * 根据实体插入数据，刷新界面表
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    private void add() throws SQLException {
        if (!location1.getText().equals(LoginController.Information.getInstance().getILocation())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("辖区权限不足！");
            alert.showAndWait();
            return;
        }
        Roads road = new Roads(Integer.parseInt(id1.getText()), name1.getText(),location1.getText(), sp1.getText(), terminal1.getText(),length1.getText(),"无事故",grade1.getText());
        if (roadsJDBC.insert(road) == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("ID重复，新增失败！");
            alert.showAndWait();
        }
        refreshTableView();
    }

    /**
     * 根据主键删除数据，刷新界面表
     */
    @FXML
    private void delete()  {
        List<Roads> selectedStudents = new ArrayList<>
                (tableView.getSelectionModel().getSelectedItems());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("确定要删除选定的公路信息吗？");
// 显示对话框，并等待按钮返回
        Optional<ButtonType> result = alert.showAndWait();
// 判断返回的按钮类型是确定还是取消，再据此分别进一步处理
        if (result.get() == ButtonType.OK) {
            try {
                tableView.getItems().removeAll(roadsJDBC.getAll());
// 删除选定的学生信息
                for (Roads r : selectedStudents) roadsJDBC.deleteByKey(r.getRoad_id());
//刷新数据表格
                refreshTableView();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 根据获取的条件进行模糊查询，刷新界面表
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @FXML
    private void check()throws SQLException {
      String i = JOptionPane.showInputDialog("请输入要查询的公路出险情况！");
        String sql = "where ";
        if (!id1.getText().isEmpty()) sql += "road_id like '"  + id1.getText() + "%' and ";
        if (!name1.getText().isEmpty()) sql += "road_name like" + "'%" + name1.getText() + "%' and ";
        if (!location1.getText().isEmpty()) sql += "location like" + "'%" + location1.getText() + "%' and ";
        if (!sp1.getText().isEmpty()) sql += "starting_point like" + "'%" + sp1.getText() + "%' and ";
        if (!terminal1.getText().isEmpty()) sql += "terminal like" + "'%" + terminal1.getText() + "%' and ";
        if (!length1.getText().isEmpty()) sql += "length like" + "'%" + length1.getText() + "%' and ";
        if (!grade1.getText().isEmpty()) sql += "road_grade like" + "'%" + grade1.getText() + "%' and ";
        if (i!=null)if (!i.isEmpty())sql+="accident_situation like" + "'%"+ i +"%' and ";
        StringBuffer stringBuffer = new StringBuffer(sql);
        if (!sql.equals("where ")){
            stringBuffer.delete(stringBuffer.length() - 4, stringBuffer.length()-1);
            sql=stringBuffer.toString();
        }
        else sql="";
      list = roadsJDBC.getByCondition(sql);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("查询到"+list.size()+"条数据！");
        alert.showAndWait();
        tableView.getItems().removeAll(roadsJDBC.getAll());
        updateTableView();
        pageTable.setVisible(false);
    }

    /**
     * 点击刷新按钮，刷新界面
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @FXML
    private void refresh_() throws SQLException {
        id1.setDisable(false);
        id1.setText("");
        name1.setText("");
        location1.setText("");
        sp1.setText("");
        terminal1.setText("");
        length1.setText("");
        grade1.setText("");
        button1.setText("新增信息");
        button1.setDisable(true);
        pageTable.setVisible(true);
        roadsJDBC.init();
        refreshTableView();
    }

    /**
     * 导入文件并导入数据库
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @FXML
    private void import_()throws SQLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导入文件");
        fileChooser.setInitialDirectory(new File("E:\\idea\\untitled\\work\\src\\main\\resources"));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("All Files", "*.*")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Data files (*.data)", "*.data")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx")));
        // 显示文件保存对话框
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile == null) return;
        if (selectedFile.getName().contains(".txt")) list = (List<Roads>) readListFromFile1(selectedFile);
        if (selectedFile.getName().contains(".data")) list = (List<Roads>) readListFromFile2(selectedFile);
        if (selectedFile.getName().contains(".xlsx")) list = (List<Roads>) readListFromExcel(selectedFile);
        tableView.getItems().clear();
        pageTable.setVisible(false);
        String num1 = "";
        String num2 = "";
        String num3[];
        Map<Integer,Roads> map = new HashMap<>();
        for (Roads roads1 : list)map.put(roads1.getRoad_id(),roads1);
        for (Map.Entry<Integer,Roads> roads1 : map.entrySet()) {

            if (!roads1.getValue().getLocation().equals(LoginController.Information.getInstance().getILocation())) {
                num1 += roads1.getKey() + ",";
                break;
            }

            if (roadsJDBC.insert(roads1.getValue()) == 0) {
                num2 += roads1.getKey() + ",";
            }
        }
        updateTableView();
        cMenu.getItems().get(0).setVisible(false);
        cMenu.getItems().get(1).setVisible(false);
        if (!num1.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("公路ID:"+num1+"辖区权限不足！");
            alert.showAndWait();
        }
        if (!num2.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("公路ID:"+ num2  +"ID重复！");
            alert.showAndWait();
            Alert alert3 = new Alert(Alert.AlertType.CONFIRMATION);
            alert3.setHeaderText("是否进行覆盖？");
// 显示对话框，并等待按钮返回
            Optional<ButtonType> result = alert3.showAndWait();
// 判断返回的按钮类型是确定还是取消，再据此分别进一步处理
            if (result.get() == ButtonType.OK) {
                String num4="";
               num3=num2.split(",");
               for (String s:num3){
                   if (map.get(Integer.parseInt(s)).getLocation().equals(LoginController.Information.getInstance().getILocation())){
                       roadsJDBC.updateByKey(Integer.parseInt(s), map.get(Integer.parseInt(s)));
                   }
                   else num4+=s+",";
               }
              if (!num4.isEmpty()) {
                  Alert alert4 = new Alert(Alert.AlertType.WARNING);
                  alert4.setHeaderText("公路ID:" + num4 + "辖区权限不足！");
                  alert4.showAndWait();
              }
            }
        }
    }
    /**
     * 导出文件
     */
    @FXML
    private void export_(){
        List<String> list1 = new ArrayList<>();
List<Roads> roadsList = new ArrayList<>();
for (Roads roads1:(List<Roads>)tableView.getSelectionModel().getSelectedItems()){
    roads =
            new Roads(roads1.getRoad_id(),roads1.getRoad_name(),roads1.getLocation(),roads1.getStarting_point(),roads1.getTerminal(),roads1.getLength(),roads1.getAccident_situation(),roads1.getRoad_grade());
roadsList.add(roads1);
}
for (Roads roads1:roadsList){list1.add(roads1.toString());}
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出文件");
        fileChooser.setInitialDirectory(new File("E:\\idea\\untitled\\work\\src\\main\\resources"));
        fileChooser.setInitialFileName("information");
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("All Files", "*.*")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Text files (*.txt)",  "*.txt")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Data files (*.data)",  "*.data")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Excel files (*.xlsx)",  "*.xlsx")));
        // 显示文件保存对话框
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile==null)return;
if (selectedFile.getName().contains(".txt"))writeListToFile1(list1,selectedFile);
if (selectedFile.getName().contains(".data"))writeListToFile2((List<T>) roadsList,selectedFile);
if (selectedFile.getName().contains(".xlsx"))writeListToExcel((List<T>) roadsList,selectedFile);

    }

    /**
     * 字符流写入
     * @param list 写入的数据
     * @param selectedFile 目标文件
     */
    public  void writeListToFile1(List<String> list,File selectedFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
            for (String element : list) {
                writer.write(element);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 字符流读入
     * @param file 目标文件
     * @return 返回读入数据
     */
    public  List<T> readListFromFile1(File file) {
        List<String> list = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            list = reader.lines().toList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<T> list1 = new ArrayList<>();
        for (String s:list){
            String[] strings = s.split(",");
            roads = new Roads(Integer.parseInt(strings[0]),strings[1],strings[2],strings[3],strings[4],strings[5],strings[6],strings[7]);
            list1.add((T)roads);
        }
        return list1;
    }

    /**
     * 对象流写入
     * @param list 写入数据
     * @param file 目标文件
     */
    public void writeListToFile2(List<T> list,File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对象流读入
     * @param file 目标文件
     * @return 返回读入数据
     */
    public List<T> readListFromFile2(File file) {
        List<T> list = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            list = (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Excel写入
     * @param dataList 写入数据
     * @param file 目标文件
     */
    public  void writeListToExcel(List<T> dataList,File file) {
        EasyExcel.write(file,Roads.class).sheet("公路基本信息").doWrite(dataList);
    }

    /**
     * EXcel读入
     * @param file 目标文件
     * @return 返回读入数据
     */
    public  List<T> readListFromExcel(File file) {
        List<T> list = new ArrayList();
        try {

            EasyExcel.read(file,Roads.class,new ExcelDataListener(list)).sheet("公路基本信息").doRead();
        }catch (Exception e){e.printStackTrace();}
        return list;
    }
    public  class ExcelDataListener extends AnalysisEventListener<T> {
        private List<T> dataList;

        public ExcelDataListener(List<T> dataList) {
            this.dataList = dataList;
        }


        @Override
        public void invoke(T data, AnalysisContext context) {

            dataList.add(data);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {

            // 数据读取完成后的操作
        }
    }

}
