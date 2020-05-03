package simulator;

import static java.awt.image.ImageObserver.ERROR;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import items.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aditya Samantaray
 */
public class SimulatorForm extends javax.swing.JFrame {

    /**
     * Creates new form SimulatorForm
     */
    public SimulatorForm() {
        initComponents();
        setLocationRelativeTo(null);
        MainQueue.createNew(8);  // create random data main queue
        updateTable(MainQueue.get()); // view initial jobs data in the table 
        simulateBttn.setEnabled(false);
        loadValDefActionPerformed(null);
        thread.start();
    }
    
    /**
     * set assigned jobs by the user to the Main queue and update the table
     * @param queue queue of jobs
     */
    public void setAssignedQueue(Queue queue)
    {
        MainQueue.add(queue);    
        updateTable(MainQueue.get());
    }
    
    private MyTable myTable;  // table on the GUI
    /**
     * update table data
     * @param queue queue of jobs to be shown on the table  
     */
    private void updateTable(Queue queue){
        myTable = new MyTable(queue);
        simTable.setModel(myTable);  
    }
    
    // <editor-fold defaultstate="collapsed" desc="Visuals" >
    
    /**
     * view CPU, Gantt and readyQueue visuals
     * @param job current job processed in the simulation
     * @param readyQueue  current ready queue in the simulation
     */
    private void viewVisuals(Job job , Queue readyQueue)
    {
        cpuVisual(job, Simulation.Time);
        showGantt(job, Simulation.Time);
        showReadyQueue(readyQueue);
    }
    
    /**
     * clear and reset CPU, Gantt and ReadyQueue visuals
     */
    private void clearVisuals()
    {
        cpuClear();
        clearGantt();
        clearReadyQueue();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="CPU Stats Display" >
    
    private static int idleTime =0; // CPU idle time
    /**
     * view current data of the CPU during the simulation
     * @param job current job to be processed by the CPU
     * @param time current time of the simulation
     */
    private void cpuVisual(Job job , int time){
        if(job == null ) 
        {
            cpuCurrentJob.setText(" Idle " ); 
            idleTime++;
        }
        else 
        { 
            cpuCurrentJob.setText("JOB " + job.jobNumber);
        }
        cpuCurrentTime.setText(time + " -> " + (time +1));
        if(time != 0) 
        {
            cpuUtilize.setText(((time - idleTime) *100 / time) +"%");
        }
        else { cpuUtilize.setText(100 +"%"); }
    }
    
    /**
     * reset CPU visual data
     */
    private void cpuClear(){
        cpuCurrentJob.setText("Idle");
        cpuCurrentTime.setText("0");
        cpuUtilize.setText("0%");
        idleTime = 0;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Charts" >
    /**
     * update and show Gantt chart
     * @param job job to be added to the chart
     * @param time time of the simulation
     */
    private void showGantt(Job job , int time){
       GanttChart.addJob(job, time);
//       addToGUI(GanttChart.List);
       for(int i =0 ; i< GanttChart.List.size() ; i++){
           jPanel6.add(GanttChart.List.get(i));
        }
        repaint();
    }
    
    /**
     * remove all the elements from the Gantt chart
     */
    private void clearGantt(){
//        removeFromGUI(GanttChart.List);
        for(int i =0 ; i< GanttChart.List.size() ; i++){
           jPanel6.remove(GanttChart.List.get(i));
        }
        repaint();
        GanttChart.clear();
    }
    
    /**
     * update and show ready queue chart
     * @param list list of jobs to be represented
     */
    private void showReadyQueue(Queue list){
        clearReadyQueue();
        ReadyChart.update(list);
//        addToGUI(ReadyChart.List);
        for(int i =0 ; i< ReadyChart.List.size() ; i++){
           jPanel6.add(ReadyChart.List.get(i));
        }
        repaint();
    }
    
    /**
     * remove all the elements form the ready queue chart
     */
    private void clearReadyQueue(){
//        removeFromGUI(ReadyChart.List);
        for(int i =0 ; i< ReadyChart.List.size() ; i++){
           jPanel6.remove(ReadyChart.List.get(i));
        }
        repaint();
        ReadyChart.clear();
     }
    
    // </editor-fold>
    
  
 
    
    //<editor-fold defaultstate="collapsed" desc="Add Data">
                                          
     
    
    private int [] userData = new int [24];  // data entered by the user
    private int fillJobList(){
        getData(); // collect data from the frame
        ArrayList<Job> joblist = new ArrayList<Job>(); // list of jobs entered by the user
        for(int i=0 ; i<8 ; i++){
            int arriveTime = userData[i*3];
            int burstTime = userData[(i*3)+1];
            int priority = userData[(i*3)+2];
            if(checkJob(arriveTime,burstTime,priority )){
               joblist.add(new Job(i+1, arriveTime, burstTime, priority));
            }
        }
        setAssignedQueue(new Queue(joblist));  // add list of jobs to the main queue
        return 0;
    }
    
    /**
     * Check that the entered data are within limits.
     * limits are set by try and error according to the GUI limits
     * @param arrive arrive time of the job
     * @param burst burst time of the job
     * @param priority priority of the job
     * @return true if data are within limits
     */
    private boolean checkJob(int arrive , int burst , int priority){
        if( arrive < 0  || arrive > 30 ) {return false;}
        if( burst <= 0 || burst > 13 ) {return false;}
        return !(priority <=0  || priority > 127);
    }
       
    /**
     * get the data from the frame and put it in an array
     */
    private void getData (){
        try {
        userData[0] = Integer.parseInt(addField1.getText());
        userData[1] = Integer.parseInt(addField2.getText());
        userData[2] = Integer.parseInt(addField3.getText());
        userData[3] = Integer.parseInt(addField4.getText());
        userData[4] = Integer.parseInt(addField5.getText());
        userData[5] = Integer.parseInt(addField6.getText());
        userData[6] = Integer.parseInt(addField7.getText());
        userData[7] = Integer.parseInt(addField8.getText());
        userData[8] = Integer.parseInt(addField9.getText());
        userData[9] = Integer.parseInt(addField10.getText());
        userData[10] = Integer.parseInt(addField11.getText());
        userData[11] = Integer.parseInt(addField12.getText());
        userData[12] = Integer.parseInt(addField13.getText());
        userData[13] = Integer.parseInt(addField14.getText());
        userData[14] = Integer.parseInt(addField15.getText());
        userData[15] = Integer.parseInt(addField16.getText());
        userData[16] = Integer.parseInt(addField17.getText());
        userData[17] = Integer.parseInt(addField18.getText());
        userData[18] = Integer.parseInt(addField19.getText());
        userData[19] = Integer.parseInt(addField20.getText());
        userData[20] = Integer.parseInt(addField21.getText());
        userData[21] = Integer.parseInt(addField22.getText());
        userData[22] = Integer.parseInt(addField23.getText());
        userData[23] = Integer.parseInt(addField24.getText()); 
        }
        catch (NumberFormatException e)  // insufficeint input will trigger an error message
        { JOptionPane.showMessageDialog( null , "Insufficient input" , "Error" , ERROR); }
    }
    //</editor-fold>
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        addField1 = new javax.swing.JTextField();
        addField4 = new javax.swing.JTextField();
        addField7 = new javax.swing.JTextField();
        addField10 = new javax.swing.JTextField();
        addField13 = new javax.swing.JTextField();
        addField16 = new javax.swing.JTextField();
        addField19 = new javax.swing.JTextField();
        addField22 = new javax.swing.JTextField();
        addField2 = new javax.swing.JTextField();
        addField5 = new javax.swing.JTextField();
        addField8 = new javax.swing.JTextField();
        addField11 = new javax.swing.JTextField();
        addField14 = new javax.swing.JTextField();
        addField20 = new javax.swing.JTextField();
        addField17 = new javax.swing.JTextField();
        addField23 = new javax.swing.JTextField();
        addField3 = new javax.swing.JTextField();
        addField6 = new javax.swing.JTextField();
        addField9 = new javax.swing.JTextField();
        addField12 = new javax.swing.JTextField();
        addField15 = new javax.swing.JTextField();
        addField18 = new javax.swing.JTextField();
        addField21 = new javax.swing.JTextField();
        addField24 = new javax.swing.JTextField();
        loadValNew = new javax.swing.JButton();
        loadValDef = new javax.swing.JButton();
        loadValZero = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        simulateBttn = new javax.swing.JButton();
        simulateRandomBttn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        quantum = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        algorithmsMenu = new javax.swing.JComboBox<>();
        simSpeed = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        simTable = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        avgWaiting = new javax.swing.JLabel();
        cpuCurrentTime = new javax.swing.JLabel();
        cpuUtilize = new javax.swing.JLabel();
        cpuCurrentJob = new javax.swing.JLabel();
        avgTurnaround = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        stopBttn = new javax.swing.JButton();
        algoDisp = new javax.swing.JLabel();
        simDisp = new javax.swing.JLabel();
        quantumDisp = new javax.swing.JLabel();
        finishBttn = new javax.swing.JButton();
        nextStepBttn = new javax.swing.JButton();
        resumeBttn = new javax.swing.JButton();
        anotherSimBttn = new javax.swing.JButton();
        restartBttn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        exitMenu = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("Add Data");

        jLabel3.setText("Arrival");

        jLabel4.setText("Burst");

        jLabel5.setText("Priority");

        jLabel6.setText("#1");

        jLabel7.setText("#2");

        jLabel8.setText("#3");

        jLabel9.setText("#4");

        jLabel10.setText("#5");

        jLabel11.setText("#6");

        jLabel12.setText("#7");

        jLabel13.setText("#8");

        addField1.setEditable(false);
        addField1.setText("0");
        addField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField1ActionPerformed(evt);
            }
        });

        addField4.setText("0");
        addField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField4ActionPerformed(evt);
            }
        });

        addField7.setText("0");
        addField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField7ActionPerformed(evt);
            }
        });

        addField10.setText("0");
        addField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField10ActionPerformed(evt);
            }
        });

        addField13.setText("0");
        addField13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField13ActionPerformed(evt);
            }
        });

        addField16.setText("0");
        addField16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField16ActionPerformed(evt);
            }
        });

        addField19.setText("0");
        addField19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField19ActionPerformed(evt);
            }
        });

        addField22.setText("0");
        addField22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField22ActionPerformed(evt);
            }
        });

        addField2.setText("0");
        addField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField2ActionPerformed(evt);
            }
        });

        addField5.setText("0");
        addField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField5ActionPerformed(evt);
            }
        });

        addField8.setText("0");
        addField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField8ActionPerformed(evt);
            }
        });

        addField11.setText("0");
        addField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField11ActionPerformed(evt);
            }
        });

        addField14.setText("0");
        addField14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField14ActionPerformed(evt);
            }
        });

        addField20.setText("0");
        addField20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField20ActionPerformed(evt);
            }
        });

        addField17.setText("0");
        addField17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField17ActionPerformed(evt);
            }
        });

        addField23.setText("0");
        addField23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField23ActionPerformed(evt);
            }
        });

        addField3.setText("0");
        addField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField3ActionPerformed(evt);
            }
        });

        addField6.setText("0");
        addField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField6ActionPerformed(evt);
            }
        });

        addField9.setText("0");
        addField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField9ActionPerformed(evt);
            }
        });

        addField12.setText("0");
        addField12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField12ActionPerformed(evt);
            }
        });

        addField15.setText("0");
        addField15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField15ActionPerformed(evt);
            }
        });

        addField18.setText("0");
        addField18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField18ActionPerformed(evt);
            }
        });

        addField21.setText("0");
        addField21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField21ActionPerformed(evt);
            }
        });

        addField24.setText("0");
        addField24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addField24ActionPerformed(evt);
            }
        });

        loadValNew.setText("Load Values");
        loadValNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadValNewActionPerformed(evt);
            }
        });

        loadValDef.setText("Default Values");
        loadValDef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadValDefActionPerformed(evt);
            }
        });

        loadValZero.setText("Reset to Zero");
        loadValZero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadValZeroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(186, 186, 186))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(66, 66, 66)
                                .addComponent(addField22))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(66, 66, 66)
                                .addComponent(addField19))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(66, 66, 66)
                                .addComponent(addField16))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(66, 66, 66)
                                .addComponent(addField13))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(66, 66, 66)
                                .addComponent(addField10))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8))
                                .addGap(66, 66, 66)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(addField4)
                                    .addComponent(addField7)
                                    .addComponent(addField1))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(loadValNew)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(addField2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addField5, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addField8, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addField11, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addField14, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addField17, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addField20, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addField23, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(48, 48, 48)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(addField24, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addField21, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addField18, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addField15, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addField12, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addField9, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addField6, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addField3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(loadValDef)
                                .addGap(18, 18, 18)
                                .addComponent(loadValZero)))
                        .addGap(110, 110, 110))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(addField1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addField2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addField3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(addField4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addField5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addField6, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(addField7, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addField8, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addField9, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(addField10, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addField11, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addField12, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(addField13, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addField14, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addField15, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(addField16, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addField17, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addField18, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(addField19, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addField20, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addField21, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(addField22, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addField23, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addField24, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loadValNew)
                    .addComponent(loadValDef)
                    .addComponent(loadValZero))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel18.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel18.setText("Instructions");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setText(" \n - Input the algorithm to be used, the simulation speed and the time \n   quantum value (in case of round robin).\n - If no values are provided, then the default values will be used (FCFS,\n   with simulation speed of 5).\n - Input the job details one by one and use values within permitted \n   range of the simulator as defined below).\n Note\n * The permitted ranges for the values are\n       0 <= Arrival <= 30\n       0 < Burst < 13\n       0 < Priority < 127\n * For a null job, set all three values to zero");
        jScrollPane3.setViewportView(jTextArea2);

        simulateBttn.setText("Start Simulation");
        simulateBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulateBttnActionPerformed(evt);
            }
        });

        simulateRandomBttn.setText("Start with Random Jobs");
        simulateRandomBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulateRandomBttnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(simulateBttn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(simulateRandomBttn, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel18))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(simulateBttn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(simulateRandomBttn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1)))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jLabel14.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel14.setText("Simulation Parameters");

        jLabel15.setText("Algorithm");

        quantum.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2", "3", "4", "5", "6", "7", "8" }));

        jLabel17.setText("Time Quantum (for RR)");

        jLabel16.setText("Simulation Speed");

        algorithmsMenu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FCFS", "SJF", "SRTF", "RR" }));
        algorithmsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                algorithmsMenuActionPerformed(evt);
            }
        });

        simSpeed.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2", "3", "4", "5", "6", "7", "8" }));
        simSpeed.setSelectedIndex(3);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(algorithmsMenu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(simSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(quantum, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(50, 50, 50))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(algorithmsMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(simSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quantum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Details", jPanel1);

        jPanel6.setBackground(new java.awt.Color(204, 204, 255));

        simTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "#", "Arrival", "Burst", "Priority", "Start", "Wait", "Remaining", "Finish", "Turnaround", "%"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(simTable);

        jLabel19.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel19.setText("Job Table");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jLabel21.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel21.setText("CPU Details");

        jLabel22.setText("Current Job");

        jLabel24.setText("Current Time");

        jLabel25.setText("Utilization");

        jLabel26.setText("Average Waiting");

        jLabel27.setText("Average Turnaround");

        avgWaiting.setText("0");

        cpuCurrentTime.setText("0");

        cpuUtilize.setText("0 %");

        cpuCurrentJob.setText("Idle");

        avgTurnaround.setText("0");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel26)
                            .addComponent(cpuCurrentJob, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(avgWaiting, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(cpuCurrentTime, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                                .addComponent(cpuUtilize, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel25)
                                .addGap(68, 68, 68))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(avgTurnaround, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel27))
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addGap(16, 16, 16)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel24)
                    .addComponent(jLabel25))
                .addGap(3, 3, 3)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cpuCurrentJob, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cpuCurrentTime, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cpuUtilize, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(avgTurnaround, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(avgWaiting, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jLabel28.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel28.setText("Control Panel");

        jLabel29.setText("Algorithm :");

        jLabel30.setText("Simulation Speed : ");

        jLabel31.setText("Quantum :");

        stopBttn.setText("Stop");
        stopBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopBttnActionPerformed(evt);
            }
        });

        algoDisp.setText("FCFS");

        simDisp.setText("5");

        quantumDisp.setText("2");

        finishBttn.setText("Finish");
        finishBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishBttnActionPerformed(evt);
            }
        });

        nextStepBttn.setText("Next Step");
        nextStepBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextStepBttnActionPerformed(evt);
            }
        });

        resumeBttn.setText("Resume");
        resumeBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resumeBttnActionPerformed(evt);
            }
        });

        anotherSimBttn.setText("New Simulation");
        anotherSimBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anotherSimBttnActionPerformed(evt);
            }
        });

        restartBttn.setText("Restart");
        restartBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartBttnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(algoDisp, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(quantumDisp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(simDisp, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(restartBttn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(anotherSimBttn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(finishBttn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nextStepBttn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(resumeBttn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stopBttn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(algoDisp)
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(stopBttn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(simDisp)
                            .addComponent(jLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(quantumDisp)))
                    .addComponent(resumeBttn, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nextStepBttn)
                    .addComponent(restartBttn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(finishBttn)
                    .addComponent(anotherSimBttn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setText("<--");

        jLabel20.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel20.setText("Gantt Chart");

        jLabel23.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel23.setText("Ready Queue");

        jLabel32.setText("<--");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(86, 86, 86)
                                        .addComponent(jLabel23))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(38, 38, 38)
                                        .addComponent(jLabel32)))
                                .addGap(280, 280, 280)))
                        .addGap(50, 50, 50))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel20)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(92, 92, 92)
                                .addComponent(jLabel32)))
                        .addGap(18, 18, 18))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(239, 239, 239)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jLabel20)
                .addGap(50, 50, 50)
                .addComponent(jLabel2)
                .addGap(81, 81, 81))
        );

        jTabbedPane1.addTab("Simulation", jPanel6);

        jMenu1.setText("File");

        exitMenu.setText("Exit");
        exitMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuActionPerformed(evt);
            }
        });
        jMenu1.add(exitMenu);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");

        jMenuItem1.setText("About");
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 996, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void algorithmsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_algorithmsMenuActionPerformed
        // TODO add your handling code here:
        Simulation.AlgorithmType = algorithmsMenu.getSelectedItem().toString();
    }//GEN-LAST:event_algorithmsMenuActionPerformed

    private void loadValNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadValNewActionPerformed
        // TODO add your handling code here:
        fillJobList();      
        simulateBttn.setEnabled(true);
    }//GEN-LAST:event_loadValNewActionPerformed
    
    // <editor-fold defaultstate="collapsed" desc="Add Field Actions (not required)">    
    private void addField24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField24ActionPerformed

    private void addField21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField21ActionPerformed

    private void addField18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField18ActionPerformed

    private void addField15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField15ActionPerformed

    private void addField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField12ActionPerformed

    private void addField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField9ActionPerformed

    private void addField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField6ActionPerformed

    private void addField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField3ActionPerformed

    private void addField23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField23ActionPerformed

    private void addField17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField17ActionPerformed

    private void addField20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField20ActionPerformed

    private void addField14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField14ActionPerformed

    private void addField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField11ActionPerformed

    private void addField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField8ActionPerformed

    private void addField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField5ActionPerformed

    private void addField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField2ActionPerformed

    private void addField22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField22ActionPerformed

    private void addField19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField19ActionPerformed

    private void addField16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField16ActionPerformed

    private void addField13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField13ActionPerformed

    private void addField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField10ActionPerformed

    private void addField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField7ActionPerformed

    private void addField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField4ActionPerformed

    private void addField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addField1ActionPerformed

//</editor-fold>

    private void simulateBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulateBttnActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
        Simulation.Stopped = false;  // start the simulation
        simulateBttn.setEnabled(false);
        stopBttn.setEnabled(true);
        resumeBttn.setEnabled(false);
        nextStepBttn.setEnabled(false);
        
    }//GEN-LAST:event_simulateBttnActionPerformed

    private void loadValZeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadValZeroActionPerformed
        // TODO add your handling code here:
        addField1.setText("0");
        addField2.setText("0");
        addField3.setText("0");
        addField4.setText("0");
        addField5.setText("0");
        addField6.setText("0");
        addField7.setText("0");
        addField8.setText("0");
        addField9.setText("0");
        addField10.setText("0");
        addField11.setText("0");
        addField12.setText("0");
        addField13.setText("0");
        addField14.setText("0");
        addField15.setText("0");
        addField16.setText("0");
        addField17.setText("0");
        addField18.setText("0");
        addField19.setText("0");
        addField20.setText("0");
        addField21.setText("0");
        addField22.setText("0");
        addField23.setText("0");
        addField24.setText("0");
        simulateBttn.setEnabled(false);
    }//GEN-LAST:event_loadValZeroActionPerformed

    private void loadValDefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadValDefActionPerformed
        // TODO add your handling code here:
        addField1.setText("0");
        addField2.setText("1");
        addField3.setText("21");
        addField4.setText("24");
        addField5.setText("3");
        addField6.setText("39");
        addField7.setText("15");
        addField8.setText("12");
        addField9.setText("85");
        addField10.setText("1");
        addField11.setText("2");
        addField12.setText("3");
        addField13.setText("12");
        addField14.setText("8");
        addField15.setText("84");
        addField16.setText("26");
        addField17.setText("11");
        addField18.setText("49");
        addField19.setText("16");
        addField20.setText("7");
        addField21.setText("69");
        addField22.setText("11");
        addField23.setText("8");
        addField24.setText("16");
    }//GEN-LAST:event_loadValDefActionPerformed

    private void exitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_exitMenuActionPerformed

    private void anotherSimBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anotherSimBttnActionPerformed
        // TODO add your handling code here:
        stopBttnActionPerformed(null);
        resumeBttn.setEnabled(false);
        algorithmsMenu.setEnabled(true);
        quantum.setEnabled(true);
        nextStepBttn.setEnabled(true);
        finishBttn.setEnabled(true);
        // reset average wait and turnaround
        avgWaiting.setText("0");
        avgTurnaround.setText("0");
        jTabbedPane1.setSelectedIndex(0);
        Simulation.reset();  // reset simulation
        clearVisuals();
    }//GEN-LAST:event_anotherSimBttnActionPerformed

    private void resumeBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resumeBttnActionPerformed
        // TODO add your handling code here:
        //        stopBttnActionPerformed(null);
        //        restartBttn.setEnabled(false);
        //        nextStepBttn.setEnabled(true);
        //        quantum.setEnabled(true);
        //        algorithmsMenu.setEnabled(true);
        //        simulateBttn.setEnabled(true);
        //        finishBttn.setEnabled(true);
        //        MainQueue.reset();
        //        updateTable(MainQueue.get());
        //        Simulation.reset();
        //        avgWaiting.setText("0");
        //        avgTurnaround.setText("0");
        //        clearVisuals();
        Simulation.Stopped = false;
        stopBttn.setEnabled(true);
        nextStepBttn.setEnabled(false);
        simulateBttn.setEnabled(false);
        resumeBttn.setEnabled(false);
    }//GEN-LAST:event_resumeBttnActionPerformed

    private void nextStepBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextStepBttnActionPerformed
        // TODO add your handling code here:
        algoDisp.setText(algorithmsMenu.getSelectedItem().toString());
        simDisp.setText(simSpeed.getSelectedItem().toString());
        quantumDisp.setText(quantum.getSelectedItem().toString());
        if(!Simulation.Finished)
        {
            quantum.setEnabled(false);
            algorithmsMenu.setEnabled(false);
            Job job = Simulation.workStep();
            viewVisuals(job, Simulation.getReadyQueue());
        }
        if(Simulation.Finished){finishBttnActionPerformed(null);}
        String t1 = myTable.getAverageWaiting() + "";
        String t2 = myTable.getAverageTurn() + "";
        if(t1.length() > 5) { t1 = t1.substring(0, 5);} // set max length to 5
        if(t2.length() > 5) { t2 = t2.substring(0, 5);}
        avgWaiting.setText(t1);
        avgTurnaround.setText(t2);
        Simulation.Time++;
    }//GEN-LAST:event_nextStepBttnActionPerformed

    private void finishBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finishBttnActionPerformed
        // TODO add your handling code here:
        while(!Simulation.Finished){
            nextStepBttnActionPerformed(null);  // press next step button till the simluation is finished
        }
        // modify the buttons view
        stopBttnActionPerformed(null);
        stopBttn.setEnabled(false);
        resumeBttn.setEnabled(false);
        finishBttn.setEnabled(false);
        nextStepBttn.setEnabled(false);
        simulateBttn.setEnabled(false);
    }//GEN-LAST:event_finishBttnActionPerformed

    private void stopBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopBttnActionPerformed
        // TODO add your handling code here:
        Simulation.Stopped = true; // stop the simulation
        stopBttn.setEnabled(false);
        nextStepBttn.setEnabled(true);
        simulateBttn.setEnabled(true);
        resumeBttn.setEnabled(true);
    }//GEN-LAST:event_stopBttnActionPerformed

    private void simulateRandomBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulateRandomBttnActionPerformed
        // TODO add your handling code here:
        MainQueue.createNew(8);
        updateTable(MainQueue.get());
        jTabbedPane1.setSelectedIndex(1);
        Simulation.Stopped = false;  // start the simulation
        simulateBttn.setEnabled(false);
        stopBttn.setEnabled(true);
        resumeBttn.setEnabled(false);
        nextStepBttn.setEnabled(false);
    }//GEN-LAST:event_simulateRandomBttnActionPerformed

    private void restartBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartBttnActionPerformed
        // TODO add your handling code here:
        stopBttnActionPerformed(null);
        stopBttn.setEnabled(false);
        restartBttn.setEnabled(false);
        nextStepBttn.setEnabled(true);
        resumeBttn.setEnabled(true);
        finishBttn.setEnabled(true);
        MainQueue.reset();
        updateTable(MainQueue.get());
        Simulation.reset();
        avgWaiting.setText("0");
        avgTurnaround.setText("0");
        clearVisuals();
    }//GEN-LAST:event_restartBttnActionPerformed
    
    // <editor-fold defaultstate="collapsed" desc="Thread - Real Time Simulation" >
    Thread thread = new Thread(() -> {
        while(true)
        {
            if(!Simulation.Finished && !Simulation.Stopped) // stops the simulation
            {
                nextStepBttnActionPerformed(null);  // press next step button
                delay();  // delay time after every step
            }
            else{
                try {
                    Thread.sleep(200); // 150 is here by try and error
                }
                catch (InterruptedException ex) {
                    Logger.getLogger(SimulatorForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    });
    
    /**
     * responsible for the delay time between every step in the
     * simulation.
     */
    public void delay ()
    {
        int num = Integer.parseInt(simSpeed.getSelectedItem()+"");  // get the delay factor from GUI
        try {
            Thread.sleep(150 * (10-num)); // 150 is here by try and error
        } 
        catch (InterruptedException ex) {  
            Logger.getLogger(SimulatorForm.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    // </editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="SimulatorForm Main">
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SimulatorForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new SimulatorForm().setVisible(true);
        });
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Variable declarations (Do not edit)" >
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addField1;
    private javax.swing.JTextField addField10;
    private javax.swing.JTextField addField11;
    private javax.swing.JTextField addField12;
    private javax.swing.JTextField addField13;
    private javax.swing.JTextField addField14;
    private javax.swing.JTextField addField15;
    private javax.swing.JTextField addField16;
    private javax.swing.JTextField addField17;
    private javax.swing.JTextField addField18;
    private javax.swing.JTextField addField19;
    private javax.swing.JTextField addField2;
    private javax.swing.JTextField addField20;
    private javax.swing.JTextField addField21;
    private javax.swing.JTextField addField22;
    private javax.swing.JTextField addField23;
    private javax.swing.JTextField addField24;
    private javax.swing.JTextField addField3;
    private javax.swing.JTextField addField4;
    private javax.swing.JTextField addField5;
    private javax.swing.JTextField addField6;
    private javax.swing.JTextField addField7;
    private javax.swing.JTextField addField8;
    private javax.swing.JTextField addField9;
    private javax.swing.JLabel algoDisp;
    private javax.swing.JComboBox<String> algorithmsMenu;
    private javax.swing.JButton anotherSimBttn;
    private javax.swing.JLabel avgTurnaround;
    private javax.swing.JLabel avgWaiting;
    private javax.swing.JLabel cpuCurrentJob;
    private javax.swing.JLabel cpuCurrentTime;
    private javax.swing.JLabel cpuUtilize;
    private javax.swing.JMenuItem exitMenu;
    private javax.swing.JButton finishBttn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JButton loadValDef;
    private javax.swing.JButton loadValNew;
    private javax.swing.JButton loadValZero;
    private javax.swing.JButton nextStepBttn;
    private javax.swing.JComboBox<String> quantum;
    private javax.swing.JLabel quantumDisp;
    private javax.swing.JButton restartBttn;
    private javax.swing.JButton resumeBttn;
    private javax.swing.JLabel simDisp;
    private javax.swing.JComboBox<String> simSpeed;
    private javax.swing.JTable simTable;
    private javax.swing.JButton simulateBttn;
    private javax.swing.JButton simulateRandomBttn;
    private javax.swing.JButton stopBttn;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
}
