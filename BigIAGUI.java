import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.*;
import org.jfree.chart.title.Title;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.axis.Axis;
import org.apache.derby.jdbc.EmbeddedDriver;
import com.fazecast.jSerialComm.*;
import java.util.Scanner;
import org.apache.derby.jdbc.EmbeddedDriver;
import com.fazecast.jSerialComm.*;
import java.util.Scanner;

public class BigIAGUI extends JFrame {
    // Objects which will be included in the project window
    private JMenuBar menuBar;        // a menu bar
    private JPanel panel1;           // a panel
    private JPanel panel2;           // a panel 
    private JTextField textfield1;   // a text field
    private JButton button1;         // a button (Add)
    private JButton button2;         // another button (Save...)
    private JButton button3;         // another button (Clear Array)
    private JButton button4;         // another button (Update Element)
    private JButton button5;         // another button (Delete Element)
    private JCheckBox checkbox1;     // a checkbox
    private JCheckBox checkbox2;
    private JCheckBox checkbox3;
    private JCheckBox checkbox4;
    private JCheckBox checkbox5;
    private JTable table1;
    XYSeries series = new XYSeries("Soil Humidity(%)");
    XYSeries series1 = new XYSeries("Soil Temperature(C)");
    XYSeries series2 = new XYSeries("Air Humidity(%)");
    XYSeries series3 = new XYSeries("Air Temperature(C)");
    XYSeries series4 = new XYSeries("Sunlight");


    XYSeriesCollection dataset = new XYSeriesCollection();
    private String[] calcs = {"Mean","Mode","Max","Min","Range"};
    JComboBox combobox1 = new JComboBox(calcs);
    private String[] calData = {"AirHData","AirTData","SoilHData","SoilTData","SunlightData"};
    JComboBox combobox2 = new JComboBox(calData);
    String ChartTitle = "Chart Title";
    JFreeChart chart = ChartFactory.createXYLineChart(ChartTitle,"Time","Data",dataset);

    boolean stop = false;
    
    private String[] colNames = {"Air Humidity" , "Air Temperature", "Soil Humidity", "Soil Temperature", "Sunlight"};
    // Variables linked to the array
    private int COUNTER = 0;
    
    // Variables linked to the textfile
    public static String INITIALFILEPATH = "C:\\Users\\1lamots\\Desktop\\";
    private String FILEPATH = INITIALFILEPATH;    
    private String selected;

    String dataStreamObj[] = new String[5];
           

    /*
     * Main
     */ 
    public static void main(String[] args){
        //System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        javax.swing.SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() {
                new BigIAGUI();
                
            }
        });
    }      
         DefaultTableModel model1 = new DefaultTableModel(colNames,0);

    /*
     * Constructor
     */
    public BigIAGUI(){

        this.setTitle("My Project");
        
        this.setSize(100,400);
        
        generateMenu();
        this.setJMenuBar(menuBar);
        
        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(820,400));
        contentPane.setBackground(new Color(225,225,225));
        
        JTable table1 = new JTable(model1);

         
        JScrollPane scrollpane3 = new JScrollPane(table1);
        scrollpane3.setBounds(2,15,818,140);
        scrollpane3.setVisible(true);    
        
        combobox1.setBounds(320,160,150,30);
        combobox2.setBounds(480,160,150,30);

       
        panel1 = new JPanel(null);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(1),"Data"));
        panel1.setBounds(0,0,820,205);
        panel1.setOpaque(false);
        panel1.setForeground(new Color(0,0,0));
        panel1.setEnabled(true);
        panel1.setFont(new Font("sansserif",0,12));
        panel1.setVisible(true);
        
           
        panel2 = new JPanel(null);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(1),null));
        panel2.setBounds(317,155,820,47);
        panel2.setOpaque(false);
        panel2.setForeground(new Color(0,0,0));
        panel2.setEnabled(true);
        panel2.setFont(new Font("sansserif",0,12));
        panel2.setVisible(true);

        button1 = new JButton();
        button1.setBounds(10,160,150,35);
        button1.setBackground(new Color(190,239,175));
        button1.setForeground(new Color(0,0,0));
        button1.setEnabled(true);
        button1.setFont(new Font("sansserif",0,12));
        button1.setText("Collect Data");
        button1.setVisible(true);
        button1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae){
                stop = false;

         
                getInsertData();
            }
        });
       
        button2 = new JButton();
        button2.setBounds(165,160,150,35);
        button2.setBackground(new Color(190,239,175));
        button2.setForeground(new Color(0,0,0));
        button2.setEnabled(false);
        button2.setFont(new Font("sansserif",0,12));
        button2.setText("Stop Collecting");
        button2.setVisible(true);
        button2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae){
                stop = true;
                button1.setEnabled(true);
                button2.setEnabled(false);
                System.out.println(stop);
            }
        });
        
        button3 = new JButton();
        button3.setBounds(10,215,150,35);
        button3.setBackground(new Color(190,239,175));
        button3.setForeground(new Color(0,0,0));
        button3.setEnabled(true);
        button3.setFont(new Font("sansserif",0,12));
        button3.setText("Update Graph");
        button3.setVisible(true);
        button3.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae){
                updateGraph();
            }
        });
        
        button4 = new JButton();
        button4.setBounds(650,160,150,35);
        button4.setBackground(new Color(190,239,175));
        button4.setForeground(new Color(0,0,0));
        button4.setEnabled(true);
        button4.setFont(new Font("sansserif",0,12));
        button4.setText("Calculate");
        button4.setVisible(true);
        button4.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae){

                doCalculation();
            }
        });
        
        int checkboxH = 215;
        int checkboxW = 175;
        checkbox1 = new JCheckBox();
        checkbox1.setBounds(checkboxW,checkboxH,100,15);
        checkbox1.setOpaque(false);
        checkbox1.setForeground(new Color(0,0,0));
        checkbox1.setEnabled(true);
        checkbox1.setFont(new Font("sansserif",0,10));
        checkbox1.setText("Air Humidity");
        checkbox1.setToolTipText("Select to isolate Air Humitidy");
        checkbox1.setVisible(true);
        
        checkbox2 = new JCheckBox();
        checkbox2.setBounds(checkboxW+100,checkboxH,100,15);
        checkbox2.setOpaque(false);
        checkbox2.setForeground(new Color(0,0,0));
        checkbox2.setEnabled(true);
        checkbox2.setFont(new Font("sansserif",0,10));
        checkbox2.setText("Air Temperature");
        checkbox2.setToolTipText("Select to isolate Air Temperature");
        checkbox2.setVisible(true);
        
               
        checkbox3 = new JCheckBox();
        checkbox3.setBounds(checkboxW+200,checkboxH,100,15);
        checkbox3.setOpaque(false);
        checkbox3.setForeground(new Color(0,0,0));
        checkbox3.setEnabled(true);
        checkbox3.setFont(new Font("sansserif",0,10));
        checkbox3.setText("Soil Humidity");
        checkbox3.setToolTipText("Select to isolate Soil Humidity");
        checkbox3.setVisible(true);
        
               
        checkbox4 = new JCheckBox();
        checkbox4.setBounds(checkboxW+300,checkboxH,100,15);
        checkbox4.setOpaque(false);
        checkbox4.setForeground(new Color(0,0,0));
        checkbox4.setEnabled(true);
        checkbox4.setFont(new Font("sansserif",0,10));
        checkbox4.setText("Soil Temperature");
        checkbox4.setToolTipText("Select to isolate Soil Temperature");
        checkbox4.setVisible(true);
        
               
        checkbox5 = new JCheckBox();
        checkbox5.setBounds(checkboxW+400,checkboxH,100,15);
        checkbox5.setOpaque(false);
        checkbox5.setForeground(new Color(0,0,0));
        checkbox5.setEnabled(true);
        checkbox5.setFont(new Font("sansserif",0,10));
        checkbox5.setText("Sunlight");
        checkbox5.setToolTipText("Select to isolate Sunlight");
        checkbox5.setVisible(true);
        dataset.addSeries(series);
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
        dataset.addSeries(series4);
        this.add(new ChartPanel(chart),BorderLayout.SOUTH);
     
        contentPane.add(panel1);
        contentPane.add(panel2);
        contentPane.add(scrollpane3);
        contentPane.add(combobox1);
        contentPane.add(combobox2);
        contentPane.add(checkbox1);
        contentPane.add(checkbox2);
        contentPane.add(checkbox3);
        contentPane.add(checkbox4);
        contentPane.add(checkbox5);
        contentPane.add(button1);
        contentPane.add(button2);
        contentPane.add(button3);
        contentPane.add(button4);

        //add panel to JFrame and set window position
        this.add(contentPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getRootPane().setDefaultButton(button1);
        this.pack();
        this.setVisible(true);
        

        updateTable();
        this.textfield1.requestFocusInWindow();
    }

    private void doCalculation(){
        Statement stmt;
        ResultSet rs = null;
        String createSQL = "";
        selected= (String)combobox1.getSelectedItem();

        System.out.println("This = "+selected);
        switch(selected){
        
        case("Mean"): 
            // Connect to the database 
            openConn();
            
            try {        
                // Read records from the table
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT AVG("+(String)combobox2.getSelectedItem()+") FROM allData");
                if(rs.next()){
                infoBox("Average is " + Integer.toString(rs.getInt(1)),"Calculation");

            }
                conn.commit();   
                
                //Close the connection
                closeConn();
    
            } catch (SQLException ex) {
                infoBox("exception: " + ex,"Exception");
    
            }  
            break;
        case("Mode"): 
            // Connect to the database 
            openConn();
            
            try {        
                // Read records from the table
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT "+(String)combobox2.getSelectedItem()+", COUNT(*) FROM allData "+ 
                   "GROUP BY "+(String)combobox2.getSelectedItem()+" "+
                   "ORDER BY COUNT(*) DESC"+
                   "");
                if(rs.next()){
                infoBox("Mode is " + Integer.toString(rs.getInt(1)),"Calculation");

            }
                conn.commit();   
                
                //Close the connection
                closeConn();
    
            } catch (SQLException ex) {
                infoBox("exception: " + ex,"Exception");

                System.out.println("exception: " + ex);
    
            }  
            break;
        case("Min"):
            openConn();
            
            try {        
                // Read records from the table
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT MIN("+(String)combobox2.getSelectedItem()+") FROM allData");
                if(rs.next()){
                infoBox("Minimum is " + Integer.toString(rs.getInt(1)),"Calculation");

            }
                conn.commit();   
                
                //Close the connection
                closeConn();
    
            } catch (SQLException ex) {
                infoBox("exception: " + ex,"Exception");
      
                System.out.println("exception: " + ex);
    
            }  
            break;
        case("Max"):
            openConn();
            
            try {        
                // Read records from the table
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT MAX("+(String)combobox2.getSelectedItem()+") FROM allData");
                if(rs.next()){
                infoBox("Maximum is " + Integer.toString(rs.getInt(1)),"Calculation");

            }
                conn.commit();   
                
                //Close the connection
                closeConn();
    
            } catch (SQLException ex) {
                infoBox("exception: " + ex,"Exception");
                System.out.println("exception: " + ex);
    
            }  
            break;
        case("Range"):
            openConn();
            
            try {        
                // Read records from the table
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT MAX("+(String)combobox2.getSelectedItem()+"),MIN("+(String)combobox2.getSelectedItem()+") FROM allData");
                if(rs.next()){
                infoBox("Range is is " + ((rs.getInt(1)) - (rs.getInt(2))),"Calculation");

            }
                conn.commit();   
                
                //Close the connection
                closeConn();
    
            } catch (SQLException ex) {
                infoBox("exception: " + ex,"Exception");
                System.out.println("exception: " + ex);
    
            }  
            break;
        
        }
        
    
    
    }  
    private void updateGraph(){
            series.clear();
            series1.clear();
            series2.clear();
            series3.clear();
            series4.clear();
            Statement stmt;
            ResultSet rs = null;
            String createSQL = "";
            // Connect to the database 
            openConn();
            
            try {        
                // Read records from the table
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT AirHData,AirTData,SoilHData,SoilTData,SunlightData FROM allData");
                System.out.println("------------------------------------------------------------------------------");
                int count = 0;
                int count1 = 0;
                int count2 = 0;
                int count3 = 0;
                int count4 = 0;
                
                                

                while (rs.next()) {

                    if(checkbox1.isSelected()){
                       //chart.getTitle().setText("Air Humitidy(%) VS. Time(sec)");

                       series.add(count=count+10,rs.getInt(1));
                    } 
                    if(checkbox2.isSelected()){
                      //chart.getTitle().setText("Air Temperature(C) VS. Time(sec)");

                       series1.add(count1=count1+10,rs.getInt(2));
                    }
                    if(checkbox3.isSelected()){
                        //chart.getTitle().setText("Soil Humidity(%) VS. Time(sec)");
                       series2.add(count2=count2+10,rs.getInt(3));
                    }
                    if(checkbox4.isSelected()){
                        //chart.getTitle().setText("Soil Temperature(C) VS. Time(sec)");
                       series3.add(count3=count3+10,rs.getInt(4));
                    }
                    if(checkbox5.isSelected()){
                       // chart.getTitle().setText("Sunlighs(lux) VS. Time(sec)");
                       series4.add(count4=count4+10,rs.getInt(5));
                    }


                }
                conn.commit();      
                //Close the connection
                closeConn();
    
            } catch (SQLException ex) {
                infoBox("exception: " + ex,"Exception");
                System.out.println("exception: " + ex);
    
            }  
    
    
    
    }
   
    private void generateMenu(){
        menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu help = new JMenu("Help");

        JMenuItem open = new JMenuItem("Open...");
        JMenuItem save = new JMenuItem("Save as...");
        JMenuItem del = new JMenuItem("Delete Contents...");

        JMenuItem quit = new JMenuItem("Quit   ");
        JMenuItem about = new JMenuItem("About My Project...");
        JMenuItem data = new JMenuItem("Sunlight Data Meaning");
        
        // attach an icon to the Save as... menu itm
        save.setIcon(new ImageIcon("save.jpg"));
        save.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent evt) {
                FILEPATH = INITIALFILEPATH;
                SelectFile('s');
                if(FILEPATH != INITIALFILEPATH){ 
                    WriteTextFile();
            
                }
            }
        });
            open.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent evt) {
                FILEPATH = INITIALFILEPATH;
                SelectFile('r');
                if(FILEPATH != INITIALFILEPATH){ 
                    ReadTextFile();
                    updateTable();
            
                }                
            }
        });  
        
        data.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
            infoBox("Sunlight is represented by a value from 0-100. 0 being low sunlight and 100 being high sunlight","Info");
            
            }
        
        
        });
        
        del.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent evt){
            
        try{
            PreparedStatement pstmt;
                    // Connect to the database 
                openConn(); 
    
                pstmt = conn.prepareStatement("DELETE FROM allData");
                pstmt.executeUpdate();   conn.commit();

                // Close the connection
                closeConn();
                
              
            } catch (SQLException ex) {
                infoBox("exception: " + ex,"Exception");
                System.out.println("exception: " + ex);
                
            }
            
            updateTable();
            updateGraph();
            }
        
        
        });
        file.add(open);
        file.add(save);
        file.addSeparator();
        file.add(quit);
        file.add(del);
        
        help.add(about);
        help.add(data);
        menuBar.add(file);
        menuBar.add(help);
    }
    
    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    //DATABASE STUFF
    public static int dataStream[] = new int[5];
    static boolean check = false;
    static int testy = 123;
    static String curDB = "DATA";
    static String MESSAGE;
    static Driver derbyEmbeddedDriver = new EmbeddedDriver();
    static Connection conn = null;
    static int dataReceived[] = new int[5];
    static int curData;
    static int r = 0;
    public void getData(){
            SerialPort ports[] = SerialPort.getCommPorts();
            SerialPort port = ports[0];           
            if(port.openPort()){
                infoBox("Succefully Opened Port","Success"); 
                button1.setEnabled(false);
                button2.setEnabled(true);
            }else{
                
                infoBox("Unable to open port","Error");
                return;
            }
            
            port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING,0,0);
      
            Scanner data = new Scanner(port.getInputStream());
            Scanner sc = new Scanner(System.in);
            createDataBase();
    
            while(data.hasNextLine()){
                curData = (data.nextInt());
                System.out.println("curData = " + curData);
                if(stop == true){
                    port.closePort();
                    check = false;
                    return;
                }
                if(check == true){
                    dataReceived[r] = curData;
                    System.out.println("DataRec = " +dataReceived[r]);
                    r++;
                }
                if(curData == 2525){
                    check = true;
                }
                if(r == 5){
                    insertData();
                    check = false;
                    r = 0;
                } 
            }
    }
    
    private void getInsertData(){
        Thread thread = new Thread(){
        @Override public void run(){   
                getData();      
            
        }               
        };
        thread.start();
    }
    
    
    private void insertData(){
           try {
                PreparedStatement pstmt;
                    // Connect to the database 
                openConn(); 
    
                // Add records to the table 
                pstmt = conn.prepareStatement("INSERT INTO allData(AirHdata,AirTdata, SoilHdata, SoilTdata, Sunlightdata) VALUES "
                + "("+dataReceived[0]+","+dataReceived[1]+","+dataReceived[2]+","+dataReceived[3]+","+dataReceived[4]+")");
                pstmt.executeUpdate();
                
                conn.commit();
                //Disconnect from database
                closeConn();
                
                        for(int i =0; i<5;i++){
                            dataStreamObj[i] = Integer.toString(dataReceived[i]);
                            
                        }
                                System.out.println("test1");
                       updateGraph();
                       model1.addRow(dataStreamObj);   
                // Close the connection
                closeConn();
                
                
            } catch (SQLException ex) {
                infoBox("exception: " + ex,"Exception");
                infoBox("Probem hai", "I");
                System.out.println("exception: " + ex);
                
            }
        
        
    }
    
    public static void openConn() {  

        try {
            // Try to connect to the database 
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
            conn = DriverManager.getConnection("jdbc:derby:" + curDB +";");
            conn.setAutoCommit(false);
            MESSAGE = "DB \"" + curDB + "\" selected.";
        
        } catch (SQLException EX) {
           try {
                // Create the DB if it doesn't exist yet 
                DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
                conn = DriverManager.getConnection("jdbc:derby:" + curDB +";create=true"); 
                conn.setAutoCommit(false);
                MESSAGE = "DB \"" + curDB + "\" created and selected.";
            
            } catch (SQLException EX2) {
                infoBox("exception: " + EX2,"Exception");
                                infoBox("Probem hai", "II");

                //MESSAGE = "exception: " + EX2;
       
            }
        
        }        
        
        
    }
    
    public static void closeConn() {   
        try {
            
            if (conn != null){
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
                //conn.close();
                
            } 
            
        } catch (SQLException EX) {
            if ((EX.getErrorCode() == 50000) && ("XJ015".equals(EX.getSQLState()))) {
                System.out.println("Derby shut down normally");
                
            } else {
                //infoBox("exception: " + EX.getMessage(),"Exception");
                           //     infoBox("Probem hai", "III");

                //System.err.println("Derby did not shut down normally");
                //System.err.println(EX.getMessage());
                
            }
            
        }
        
        System.out.println();
        
    }
    
    public static void createDataBase(){
        Statement stmt;
        String createSQL = "";
        // Open connection to current DB
        openConn();
        System.out.println(MESSAGE);
        
        // Try to create the table "allData" IF it doesn't already exist
        try{
            stmt = conn.createStatement();
              createSQL = "CREATE TABLE allData ("
            + " AirHdata int, AirTdata int, SoilHdata int, SoilTdata int, Sunlightdata int)";
   
            stmt.execute(createSQL);
            System.out.println("Table allData was created");      
        
            conn.commit();
         
        } catch (SQLException EX) {
           //infoBox("exception: " + EX,"Exception");
            System.out.println(EX.getMessage());
        
        }
        
        // Close the connection
        closeConn();

    }
 
    public void updateTable(){
        model1.setRowCount(0); 
             
            Statement stmt;
            ResultSet rs = null;
            String createSQL = "";
                
            // Connect to the database 
            openConn();
            
            try {        
                // Read records from the table
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM allData");
                while (rs.next()) {
                    for(int i = 0; i<5;i++){
                    dataStreamObj[i] = Integer.toString(rs.getInt(i+1));  
                    }
                    
                       model1.addRow(dataStreamObj);

                }                
                conn.commit();    
                //Close the connection
                closeConn();
    
            } catch (SQLException ex) {
                infoBox("exception: " + ex,"Exception");
                System.out.println("exception: " + ex);
    
            }  
            
        
    }
    private void saveButtonClicked () {
        FILEPATH = INITIALFILEPATH;
        SelectFile('s');
        if(FILEPATH != INITIALFILEPATH){
            WriteTextFile();
    
        }
    }
    private void WriteTextFile(){
        
        try (BufferedWriter MYFILE = new BufferedWriter(new FileWriter(FILEPATH))){    // use (FILEPATH,true) to append to the file           
            Statement stmt;
            ResultSet rs = null;
            String createSQL = "";        
            // Connect to the database 
            openConn();    
            try {        
                // Read records from the table
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM allData");
                int x = 0;

                while (rs.next()) {
                    for(int i = 0; i<5;i++){             
                            MYFILE.write(Integer.toString(rs.getInt(i+1)));
                            MYFILE.newLine();
                        
                        }

                }
                conn.commit();                   
                //Close the connection
                closeConn();
    
            } catch (SQLException ex) {
                infoBox("exception: " + ex,"Exception");
                System.out.println("exception: " + ex);
    
            }  
            MYFILE.close();                
        }catch(Exception e){
            infoBox("exception: " + e,"Exception");
            System.out.println(e);
            return;
        }
                
        JOptionPane.showMessageDialog(this,"Writing completed successfully.");
        
    }
    String textData[] = new String[5];

    private void ReadTextFile(){
        ResultSet rs = null;
        String createSQL = "";            
        COUNTER = 0;
        String LINE = null;
        try (BufferedReader MYFILE = new BufferedReader(new FileReader(FILEPATH))){
        try{
                PreparedStatement pstmt;
                    // Connect to the database 
                openConn(); 
    
                pstmt = conn.prepareStatement("DELETE FROM allData");
                pstmt.executeUpdate();
                // Add records to the table -- Another syntax
                while ((LINE = MYFILE.readLine()) != null){
                    textData[COUNTER] = LINE;
                    System.out.println(textData[COUNTER]);
                    if(COUNTER == 4){
                        pstmt = conn.prepareStatement("INSERT INTO allData(AirHdata,AirTdata, SoilHdata, SoilTdata, Sunlightdata) VALUES "
                        + "("+textData[0]+","+textData[1]+","+textData[2]+","+textData[3]+","+textData[4]+")");
                        pstmt.executeUpdate();
    
                        COUNTER = -1;
    
                    }
                    
                    
                    COUNTER++;                
    
                }
            conn.commit();

                // Close the connection
                closeConn();
                
              
            } catch (SQLException ex) {
                infoBox("Unable to Open Text File. Make sure it is in the appropriate format.: ","Error");

                //infoBox("exception: " + ex,"Exception");
                System.out.println("exception: " + ex);
                
            }   
        
            //DELETE FROM allData;           
            MYFILE.close();

        }catch(Exception e){
            infoBox("exception: " + e,"Exception");
            System.out.println(e);
            return;
        }

                
    } 
    private void deleteRows(){
    
    }
    private void SelectFile(char MODE){
        JFileChooser FILECHOOSER = new JFileChooser();
        FILECHOOSER.setCurrentDirectory(new File (FILEPATH));
        
        FileNameExtensionFilter FILTER;
        String EXTENSION;
       
            FILTER = new FileNameExtensionFilter("Text Files","txt");
            EXTENSION = ".txt";           
        
        FILECHOOSER.addChoosableFileFilter(FILTER);
        FILECHOOSER.setFileFilter(FILTER);

        int RESULT;
        if(MODE=='s'){
            RESULT = FILECHOOSER.showSaveDialog(null);
        }else{
            RESULT = FILECHOOSER.showOpenDialog(null);
        }
        
        if (RESULT == JFileChooser.APPROVE_OPTION) {
            File selectedFile = FILECHOOSER.getSelectedFile();
            FILEPATH = selectedFile.getAbsolutePath();
            if (FILEPATH.substring(FILEPATH.length()-4).compareTo(EXTENSION) !=0){
                FILEPATH = FILEPATH + EXTENSION;
            }
        }
        
    }
}




