package com.work;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

/**
 * 事故信息系统控制类
 */
public class AccidentController<T> {
    RoadsJDBC roadsJDBC=new RoadsJDBC();
    Accidents accidents = new Accidents();
   AccidentJDBC accidentJDBC = new AccidentJDBC();
    public static int itemsPerPage = 20;//每页显示的数据条数
    List<Accidents> list = accidentJDBC.getAll();
    @FXML
    private TableView tableView;
    @FXML
    private Pagination pageTable;
    @FXML
    private TableColumn<Accidents,Integer> id;
    @FXML
    private TableColumn<Accidents,Integer> id_;
    @FXML
    private TableColumn<Accidents,String> date_;
    @FXML
    private TableColumn<Accidents,String> description;
    @FXML
    private TableColumn<Accidents,String> situation;
    @FXML
    private Button button1;
    @FXML
    private ContextMenu cMenu;
    @FXML
    private TextField id1;
    @FXML
    private TextField id_1;
    @FXML
    private TextField description1;
    @FXML
    private MenuButton situation1;
    @FXML
    private DatePicker date_1;

    public AccidentController() throws SQLException {
    }
    /**
     * 设置添加按钮不可选，加载分页
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @FXML
    private void initialize() throws SQLException {

        button1.setDisable(true);
        initializePagination();
        loadPageData(0);
    }
    /**
     * 更新界面表的数据，设置多选
     */
    private void updateTableView() {
        cMenu.getItems().get(0).setVisible(true);
        cMenu.getItems().get(1).setVisible(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getItems().addAll(list);
        id.setCellValueFactory(new PropertyValueFactory<>("accident_id"));
        id_.setCellValueFactory(new PropertyValueFactory<>("road_id"));
        date_.setCellValueFactory(new PropertyValueFactory<>("accident_data"));
        description.setCellValueFactory(new PropertyValueFactory<>("accident_description"));
        situation.setCellValueFactory(new PropertyValueFactory<>("accident_situation"));
        list.removeAll(list);
    }
    /**
     *  初始化分页，设置分页监听，点击页码会刷新分页，加载对应页码
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    private void initializePagination() throws SQLException {
// 设置分页控件的页数
        pageTable.setPageCount(accidentJDBC.calculatePageCount());
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
        list = accidentJDBC.getPage(pageIndex, itemsPerPage);
        updateTableView();
    }
    /**
     * 根据新的总页数刷新分页，加载当页数据
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    private void refreshTableView() throws SQLException{
// 获取当前页码
        int currentPageIndex = pageTable.getCurrentPageIndex();
// 获取新的总页数
        int newTotalPages = accidentJDBC.calculatePageCount();
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
     * 根据选择的损伤程度，设置选择框的信息，并调用isNull（）
     * @param event 事件，用于损伤程度选择
     */
    @FXML
    private void set_(ActionEvent event){
        situation1.setText(((MenuItem)event.getSource()).getText());
        isNull();
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
     * 判断所有框是否填写，根据结果设置添加或修改是否可选
     */
    @FXML
    private void isNull(){
        if(id1.getText()!=null
                && id_1.getText()!=null
                && date_1.getEditor().getText()!=null
                && description1.getText()!=null
                && situation1.getText()!=null) {
            if (!id1.getText().isEmpty()
                    && !id1.getText().isEmpty()
                    && !id_1.getText().isEmpty()
                    && !date_1.getEditor().getText().isEmpty()
                    && !description1.getText().isEmpty()
                    && !situation1.getText().isEmpty()) button1.setDisable(false);
            else button1.setDisable(true);
        }
        else button1.setDisable(true);
    }
    /**
     * 根据实体插入数据，刷新界面表
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    private void add() throws SQLException {
        RoadsJDBC roadsJDBC = new RoadsJDBC();
        if (roadsJDBC.getByCondition(" where road_id="+id_1.getText()).isEmpty()){
            Alert alert1 = new Alert(Alert.AlertType.WARNING);
            alert1.setHeaderText("辖区权限不足或该公路ID不存在！");
            alert1.showAndWait();
            return;
        }
        accidents = new Accidents(Integer.parseInt(id1.getText()), Integer.parseInt(id_1.getText()),date_1.getEditor().getText(),description1.getText(),situation1.getText());
        if (accidentJDBC.insert(accidents) == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("ID重复，新增失败！");
            alert.showAndWait();
        }
        roadsJDBC.init();
        refreshTableView();
    }
    /**
     * 根据主键删除数据，刷新界面表
     */
    @FXML
    private void delete()  throws SQLException{
        List<Accidents> maintenances = new ArrayList<>
                (tableView.getSelectionModel().getSelectedItems());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("确定要删除选定的公路信息吗？");
// 显示对话框，并等待按钮返回
        Optional<ButtonType> result = alert.showAndWait();
// 判断返回的按钮类型是确定还是取消，再据此分别进一步处理
        if (result.get() == ButtonType.OK) {
            try {
                tableView.getItems().removeAll(accidentJDBC.getAll());
// 删除选定的学生信息
                for (Accidents m : maintenances) accidentJDBC.deleteByKey(m.getAccident_id());
//刷新数据表格
                refreshTableView();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        roadsJDBC.init();
    }
    /**
     * 右键选择更新后，添加按钮改为更新按钮，并让所有框获取对应数据
     */
    @FXML
    private void update(){
        button1.setText("保存编辑");
        accidents = (Accidents) tableView.getSelectionModel().getSelectedItems().get(0);
        id1.setText("" + accidents.getAccident_id());
        id_1.setText(""+accidents.getRoad_id());
        date_1.getEditor().setText(accidents.getAccident_data());
        description1.setText(accidents.getAccident_description());
       situation1.setText(accidents.getAccident_situation());
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
            RoadsJDBC roadsJDBC = new RoadsJDBC();
            if (roadsJDBC.getByCondition(" where road_id="+id_1.getText()).isEmpty()){
                Alert alert1 = new Alert(Alert.AlertType.WARNING);
                alert1.setHeaderText("辖区权限不足或该公路ID不存在！");
                alert1.showAndWait();
                return;
            }
            tableView.getItems().removeAll(accidentJDBC.getAll());
            Accidents road = new Accidents(Integer.parseInt(id1.getText()), Integer.parseInt(id_1.getText()),date_1.getEditor().getText(),description1.getText(),situation1.getText());
            accidentJDBC.updateByKey(road.getAccident_id(), road);
            roadsJDBC.init();
            refresh_();
        }
    }
    /**
     * 根据获取的条件进行模糊查询，刷新界面表
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @FXML
    private void check()throws SQLException {
        String sql = "where ";
        if (!id1.getText().isEmpty()) sql += "accident_id like '"  + id1.getText() + "%' and ";
        if (!id_1.getText().isEmpty()) sql += "road_id like" + "'%" + id_1.getText() + "%' and ";
        if (!description1.getText().isEmpty()) sql += "accident_description like" + "'%" + description1.getText() + "%' and ";
        if (!date_1.getEditor().getText().isEmpty())sql += "accident_data like" + "'%" + date_1.getEditor().getText() + "%' and ";
        if (!situation1.getText().isEmpty()) sql += "accident_situation like" + "'%" + situation1.getText() + "%' and ";
        StringBuffer stringBuffer = new StringBuffer(sql);
        if (!sql.equals("where ")){
            stringBuffer.delete(stringBuffer.length() - 4, stringBuffer.length()-1);
            sql=stringBuffer.toString();
        }
        else sql="";
        list = accidentJDBC.getByCondition(sql);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("查询到"+list.size()+"条数据！");
        alert.showAndWait();
        tableView.getItems().removeAll(accidentJDBC.getAll());
        updateTableView();
        pageTable.setVisible(false);
    }
    /**
     * 点击刷新按钮，刷新界面
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @FXML
    private void refresh_() throws SQLException {
        id1.setText("");
        id1.setDisable(false);
        id_1.setText("");
        situation1.setText("");
        date_1.getEditor().setText("");
        description1.setText("");
        button1.setText("新增信息");
        button1.setDisable(true);
        pageTable.setVisible(true);
        refreshTableView();
    }
    /**
     * 导入文件并导入数据库
     * @throws SQLException 如果发生异常，抛出SQL异常
     */
    @FXML
    private void import_() throws SQLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导入文件");
        fileChooser.setInitialDirectory(new File("E:\\idea\\untitled\\work\\src\\main\\resources"));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("All Files", "*.*")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Text files (*.txt)",  "*.txt")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Data files (*.data)",  "*.data")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Excel files (*.xlsx)",  "*.xlsx")));
        // 显示文件保存对话框
        File selectedFile = fileChooser.showOpenDialog(button1.getScene().getWindow());
        if (selectedFile==null)return;
        if (selectedFile.getName().contains(".txt"))list=(List<Accidents>) readListFromFile1(selectedFile);
        if (selectedFile.getName().contains(".data"))list=(List<Accidents>) readListFromFile2(selectedFile);
        if (selectedFile.getName().contains(".xlsx"))list=(List<Accidents>)readListFromExcel(selectedFile);
        tableView.getItems().clear();
        pageTable.setVisible(false);
        String num1 = "";
        String num2 = "";
        String num3[];
        RoadsJDBC roadsJDBC = new RoadsJDBC<>();
        Map<Integer,Accidents> map = new HashMap<>();
        for (Accidents roads1 : list)map.put(roads1.getAccident_id(),roads1);
        for (Map.Entry<Integer,Accidents>  roads1: map.entrySet()) {
            if (roadsJDBC.getByCondition(" where road_id="+roads1.getValue().getRoad_id()).isEmpty()) {
                num1 += roads1.getValue().getRoad_id() + ",";
                break;
            }

            if (accidentJDBC.insert(roads1.getValue()) == 0) {
                num2 += roads1.getKey() + ",";
            }
        }
        updateTableView();
        cMenu.getItems().get(0).setVisible(false);
        cMenu.getItems().get(1).setVisible(false);
        if (!num1.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("公路ID:"+num1+"辖区权限不足或该公路ID不存在！");
            alert.showAndWait();
        }
        if (!num2.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("事故ID:"+ num2  +"ID重复！");
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
                    if (!roadsJDBC.getByCondition(" where road_id="+map.get(Integer.parseInt(s)).getRoad_id()).isEmpty()){
                        accidentJDBC.updateByKey(Integer.parseInt(s), map.get(Integer.parseInt(s)));
                    }
                    else num4+=s+",";
                }
                if (!num4.isEmpty()) {
                    Alert alert4 = new Alert(Alert.AlertType.WARNING);
                    alert4.setHeaderText("事故ID:" + num4 + "辖区权限不足！");
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
        List<Accidents> roadsList = new ArrayList<>();
        for (Accidents roads1:(List<Accidents>)tableView.getSelectionModel().getSelectedItems()){
            accidents =
                    new Accidents(roads1.getAccident_id(),roads1.getRoad_id(),roads1.getAccident_data(),roads1.getAccident_description(),roads1.getAccident_situation());
            roadsList.add(roads1);
        }
        for (Accidents roads1:roadsList){list1.add(roads1.toString());}
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出文件");
        fileChooser.setInitialDirectory(new File("E:\\idea\\untitled\\work\\src\\main\\resources"));
        fileChooser.setInitialFileName("accident");
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
            accidents = new Accidents(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]),strings[2],strings[3],strings[4]);
            list1.add((T)accidents);
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
        EasyExcel.write(file,Accidents.class).sheet("公路事故信息").doWrite(dataList);
    }
    /**
     * EXcel读入
     * @param file 目标文件
     * @return 返回读入数据
     */
    public  List<T> readListFromExcel(File file) {
        List<T> list = new ArrayList();
        try {

            EasyExcel.read(file,Accidents.class,new AccidentController.ExcelDataListener(list)).sheet("公路事故信息").doRead();
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
