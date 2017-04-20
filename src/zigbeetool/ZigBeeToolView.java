/*
 * ZigBeeToolView.java
 */

package zigbeetool;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import javax.swing.text.BadLocationException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.ButtonGroup;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * The application's main frame.
 */
public class ZigBeeToolView extends FrameView {
    private SerialHelper serialHelper = new SerialHelper();
    public int heighty;
    public Thread checksum;
    public responseStruct resp;
    public ResourceMap resourceMap;
    private SharedData sharedData = SharedData.getSingletonObject();


byte[] SYST_RST = {(byte)0xfe,0x01,0x41,0x00,0x00,0x40};
byte[] WRT_CONFG = {(byte)0xfe,0x03,0x26,0x05,0x03,0x01,0x03,0x21};
byte[] ZDO_STRT = {(byte) 0xfe,0x02,0x25,0x40,0x00,0x00,0x67};	//7
byte[] AF_reg = {(byte)0xfe,0x11,0x24,0x00,0x01,0x09,0x01,0x01,0x05,0x01,0x00,0x02,0x02,0x07,0x12,0x34,0x15,0x00,0x01,0x00,0x00,0x0C}; //22
byte[] ROUTER={(byte)0xFE, 0x03, 0x26, 0x05, (byte)0x87, 0x01, 0x01, (byte)0xA7};
byte[] LOG_CORD = {(byte)0xfe,0x03,0x26,0x05,(byte)0x87,0x01,0x00,(byte)0xa6};
byte[] EPID = {(byte) 0xfe,0x0a,0x26,0x05,0x2d,0x08,0x00,0x00,0x00,0x10,0x77,(byte)0xc2,0x50,0x00,(byte)0xf9};
byte[] PANID = {(byte)0xfe,0x04,0x26,0x05,(byte)0x83,0x02,0x12,0x34,(byte)0x80};
byte[] IEEE_ADDRS = {(byte) 0xfe, 0x01, 0x26, 0x06, 0x01, 0x20};
byte[] AF_Data = {(byte)0xfe,0x0d,0x24,0x01,(byte)0xff,(byte)0xff,0x01,0x01,0x02,0x07,0x00,0x00,0x00,0x03,0x0,0x0,0x0,0x2E};//16
byte[] AF_REG_Router = {(byte)0xFE, 0x11, 0x24, 0x00 ,0x01, 0x09, 0x01, 0x01,0x05 ,0x01 ,0x00,0x00, 0x04 ,0x0 ,0x0 ,0x15 ,0x0 ,0x12, 0x34,0x2,0x7,0x0b};
//byte[] AF_reg = {(byte)0xFE, 0xD, 0x24, 0x00 ,0x01, 0x09, 0x01, 0x01,0x05 ,0x01 ,0x00,0x00, 0x02 ,0x07 ,0x00 ,0x04 ,0x0 ,0xD};
//byte[] AF_REG_Router = {(byte)0xFE, 0xD, 0x24, 0x00 ,0x01, 0x09, 0x01, 0x01,0x05 ,0x01 ,0x00,0x00, 0x04 ,0x00 ,0x00 ,0x02 ,0x07 ,0xD};
byte[] LOG_END_DEVICE={(byte) 0xFE, 0x03, 0x26, 0x05, (byte)0x87, 0x01, 0x02, (byte)0xA4};
byte[] currentSending;
Document doc;
SimpleAttributeSet Tx;
SimpleAttributeSet Rx;




    public ZigBeeToolView(SingleFrameApplication app) {
        super(app);

        initComponents();
        initialGUISettings();
        datatxnTxtArea.setEditable(false);


              doc = datatxnTxtArea.getDocument();
       
        Tx = new SimpleAttributeSet();
            StyleConstants.setForeground(Tx, Color.BLUE);
           // StyleConstants.setBackground(Tx, Color.YELLOW);
           // StyleConstants.setBold(Tx, true);
          
             Rx = new SimpleAttributeSet();
            StyleConstants.setForeground(Rx, Color.RED);
//            StyleConstants.setBackground(Rx, Color.YELLOW);
//            StyleConstants.setBold(Rx, true);

      
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              //  statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
               // statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
       // statusAnimationLabel.setIcon(idleIcon);
      //  progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                   //     statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                   // statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                   // statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = ZigBeeToolApp.getApplication().getMainFrame();
            aboutBox = new ZigBeeToolAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        ZigBeeToolApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatxnTxtArea = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        cmbComSelector = new javax.swing.JComboBox();
        connectBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        cmdTxtField = new javax.swing.JTextField();
        sendBtn = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        panIDtxtField = new javax.swing.JTextField();
        extPanIDlabel = new javax.swing.JLabel();
        extPanIDtxtField = new javax.swing.JTextField();
        rBtnEndDevice = new javax.swing.JRadioButton();
        rBtnCoOrd = new javax.swing.JRadioButton();
        rBtnRouter = new javax.swing.JRadioButton();
        btnConfigureDevice = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dataField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        timeStampChkBx = new javax.swing.JCheckBoxMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(zigbeetool.ZigBeeToolApp.class).getContext().getResourceMap(ZigBeeToolView.class);
        datatxnTxtArea.setToolTipText(resourceMap.getString("datatxnTxtArea.toolTipText")); // NOI18N
        datatxnTxtArea.setName("datatxnTxtArea"); // NOI18N
        jScrollPane1.setViewportView(datatxnTxtArea);

        jTabbedPane1.addTab(resourceMap.getString("jScrollPane1.TabConstraints.tabTitle"), jScrollPane1); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel2.border.title"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        cmbComSelector.setName("cmbComSelector"); // NOI18N
        cmbComSelector.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbComSelectorItemStateChanged(evt);
            }
        });

        connectBtn.setIcon(resourceMap.getIcon("connectBtn.icon")); // NOI18N
        connectBtn.setText(resourceMap.getString("connectBtn.text")); // NOI18N
        connectBtn.setInheritsPopupMenu(true);
        connectBtn.setMargin(new java.awt.Insets(2, 7, 2, 7));
        connectBtn.setName("connectBtn"); // NOI18N
        connectBtn.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                connectBtnItemStateChanged(evt);
            }
        });
        connectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbComSelector, 0, 101, Short.MAX_VALUE)
                    .addComponent(connectBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbComSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(connectBtn)
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbComSelector, connectBtn});

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel3.border.title"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        cmdTxtField.setFont(resourceMap.getFont("cmdTxtField.font")); // NOI18N
        cmdTxtField.setText(resourceMap.getString("cmdTxtField.text")); // NOI18N
        cmdTxtField.setName("cmdTxtField"); // NOI18N
        cmdTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTxtFieldActionPerformed(evt);
            }
        });
        cmdTxtField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmdTxtFieldFocusGained(evt);
            }
        });

        sendBtn.setIcon(resourceMap.getIcon("sendBtn.icon")); // NOI18N
        sendBtn.setText(resourceMap.getString("sendBtn.text")); // NOI18N
        sendBtn.setName("sendBtn"); // NOI18N
        sendBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sendBtn)
                    .addComponent(cmdTxtField, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmdTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sendBtn)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel4.border.title"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        panIDtxtField.setText(resourceMap.getString("panIDtxtField.text")); // NOI18N
        panIDtxtField.setName("panIDtxtField"); // NOI18N

        extPanIDlabel.setText(resourceMap.getString("extPanIDlabel.text")); // NOI18N
        extPanIDlabel.setName("extPanIDlabel"); // NOI18N

        extPanIDtxtField.setName("extPanIDtxtField"); // NOI18N

        rBtnEndDevice.setText(resourceMap.getString("rBtnEndDevice.text")); // NOI18N
        rBtnEndDevice.setName("rBtnEndDevice"); // NOI18N

        rBtnCoOrd.setText(resourceMap.getString("rBtnCoOrd.text")); // NOI18N
        rBtnCoOrd.setName("rBtnCoOrd"); // NOI18N

        rBtnRouter.setText(resourceMap.getString("rBtnRouter.text")); // NOI18N
        rBtnRouter.setName("rBtnRouter"); // NOI18N

        btnConfigureDevice.setIcon(resourceMap.getIcon("btnConfigureDevice.icon")); // NOI18N
        btnConfigureDevice.setText(resourceMap.getString("btnConfigureDevice.text")); // NOI18N
        btnConfigureDevice.setName("btnConfigureDevice"); // NOI18N
        btnConfigureDevice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigureDeviceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(rBtnCoOrd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rBtnRouter)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rBtnEndDevice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnConfigureDevice, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panIDtxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(extPanIDlabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(extPanIDtxtField, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(panIDtxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(extPanIDtxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(extPanIDlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rBtnCoOrd)
                    .addComponent(rBtnRouter)
                    .addComponent(rBtnEndDevice)
                    .addComponent(btnConfigureDevice))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel1.border.title"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        dataField.setText(resourceMap.getString("dataField.text")); // NOI18N
        dataField.setName("dataField"); // NOI18N
        dataField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataFieldActionPerformed(evt);
            }
        });

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jTextField2.setText(resourceMap.getString("jTextField2.text")); // NOI18N
        jTextField2.setName("jTextField2"); // NOI18N

        jCheckBox1.setText(resourceMap.getString("jCheckBox1.text")); // NOI18N
        jCheckBox1.setEnabled(false);
        jCheckBox1.setName("jCheckBox1"); // NOI18N
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setName("jPanel5"); // NOI18N

        jRadioButton1.setText(resourceMap.getString("jRadioButton1.text")); // NOI18N
        jRadioButton1.setName("jRadioButton1"); // NOI18N

        jRadioButton2.setText(resourceMap.getString("jRadioButton2.text")); // NOI18N
        jRadioButton2.setName("jRadioButton2"); // NOI18N

        jRadioButton3.setText(resourceMap.getString("jRadioButton3.text")); // NOI18N
        jRadioButton3.setName("jRadioButton3"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton1)
                .addGap(28, 28, 28)
                .addComponent(jRadioButton2)
                .addGap(31, 31, 31)
                .addComponent(jRadioButton3)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jRadioButton1)
                .addComponent(jRadioButton3)
                .addComponent(jRadioButton2))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setName("jPanel6"); // NOI18N

        jRadioButton5.setText(resourceMap.getString("jRadioButton5.text")); // NOI18N
        jRadioButton5.setEnabled(false);
        jRadioButton5.setName("jRadioButton5"); // NOI18N

        jRadioButton4.setText(resourceMap.getString("jRadioButton4.text")); // NOI18N
        jRadioButton4.setEnabled(false);
        jRadioButton4.setName("jRadioButton4"); // NOI18N

        jRadioButton6.setText(resourceMap.getString("jRadioButton6.text")); // NOI18N
        jRadioButton6.setName("jRadioButton6"); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addComponent(jRadioButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(jRadioButton4)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jRadioButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jRadioButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jRadioButton5))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(dataField, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(dataField, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox1)
                            .addComponent(jLabel2))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel5, jPanel6});

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, 0, 109, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        mainPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel2, jPanel3, jPanel4});

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(zigbeetool.ZigBeeToolApp.class).getContext().getActionMap(ZigBeeToolView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jMenu2.setText(resourceMap.getString("jMenu2.text")); // NOI18N

        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text")); // NOI18N
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem5.setText(resourceMap.getString("jMenuItem5.text")); // NOI18N
        jMenuItem5.setDoubleBuffered(true);
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem7.setText(resourceMap.getString("jMenuItem7.text")); // NOI18N
        jMenuItem7.setName("jMenuItem7"); // NOI18N
        jMenu2.add(jMenuItem7);

        menuBar.add(jMenu2);

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        timeStampChkBx.setSelected(true);
        timeStampChkBx.setText(resourceMap.getString("timeStampChkBx.text")); // NOI18N
        timeStampChkBx.setName("timeStampChkBx"); // NOI18N
        jMenu1.add(timeStampChkBx);

        jMenuItem4.setText(resourceMap.getString("jMenuItem4.text")); // NOI18N
        jMenuItem4.setName("jMenuItem4"); // NOI18N
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem6.setText(resourceMap.getString("jMenuItem6.text")); // NOI18N
        jMenuItem6.setName("jMenuItem6"); // NOI18N
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        menuBar.add(jMenu1);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        lblStatus.setText(resourceMap.getString("lblStatus.text")); // NOI18N
        lblStatus.setName("lblStatus"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblStatus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE))
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbComSelectorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbComSelectorItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbComSelectorItemStateChanged

    private void cmdTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTxtFieldActionPerformed

      //  cmdTxtField.setText("");
}//GEN-LAST:event_cmdTxtFieldActionPerformed

    private void connectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectBtnActionPerformed
        // TODO add your handling code here:
        if(connectBtn.getText().contentEquals("Connect")){
        boolean connect=false;
            try {
                connect = serialHelper.connect((String) cmbComSelector.getSelectedItem(),9600);
            } catch (IOException ex) {
                Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(connect){
                connectBtn.setText("Disconnect");
                connectBtn.setIcon(resourceMap.getIcon("disconnect.icon"));
                EnableDisableCalButtons(true);
                final responseProcessor r = new responseProcessor();
              
           if(sendThis(IEEE_ADDRS, r.OP_IEEE_ADDRS))
                {
                    byte[] desc = new byte[resp.dataBuffer.length];
                    desc= resp.dataBuffer;
                    String p = "";
                    String q = "";
            for(int b=1;b<desc.length;b++){
                q = Byte.toString(desc[b]);
                q = Long.toHexString(Long.parseLong(q));
                if(q.length()>2) q=  q.substring(q.length()-2, q.length());
                if(q.length()==1) q="0".concat(q);
                p=p.concat(q+ " ");
            }
                   lblStatus.setText("Connected device (IEEE Address) - "+ p.toUpperCase() );
                   lblStatus.setForeground(Color.BLUE);
                }

            }}


        else{
            //    startTread(false);
            serialHelper.disconnect();
            //           comPortListener.yield();
            connectBtn.setText("Connect");
            connectBtn.setIcon(resourceMap.getIcon("disconnect.icon"));
            EnableDisableCalButtons(false);

        }
       
            
}//GEN-LAST:event_connectBtnActionPerformed

    private void sendBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendBtnActionPerformed

       if(cmdTxtField.getText().contains("")) {
            JOptionPane.showMessageDialog(this.getFrame(),"Please provide the command. \nFor eg: FE0141000040 is for reset", "Error", JOptionPane.ERROR_MESSAGE);
        }
 else{
         checksum = new Thread(new Runnable() {
        public void run  ()
        {
                int failed = 0;
       //  memviewMode();

         try{
 progressBar.setIndeterminate(true);
        EnableDisableCalButtons(false);

 byte[] sendBytes = new byte[cmdTxtField.getText().length()/2];

        long h= Long.parseLong(cmdTxtField.getText(),16);
        for(int y=cmdTxtField.getText().length()/2;y>0;y--){
            sendBytes[y-1]=(byte) (h & 0xff);
            h=h>>8;
        }
        OutputStream os = serialHelper.getSerialOutputStream();
        InputStream is = serialHelper.getSerialInputStream();
        //            if(is.markSupported()){
        //                is.mark(0);
        //            }

        while(failed<100){
            try {
                
                os.flush();// is.reset();
                os.write(sendBytes);
            } catch (IOException ex) {
             //   Logger.getLogger(EnergyMeterFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            Thread.sleep(200);
            try {
                int y =is.available();
                if ( y!= 0) {
                    {
                        byte[] p = new byte[y];
                        is.read(p);
                        String hd ="";//new String(p);
                        for(int v =0;v<p.length;v++){
                            String kl = Byte.toString(p[v]);
                            String m = Long.toHexString(Long.parseLong(kl));
                            if(m.length()>2){
                                m=m.substring(m.length()-2);
                            }
                            hd=hd.concat(" "+ m);
                        }
                        //  logData += " : 0x" + Integer.toHexString(uartRxHandler.currentData[uartRxHandler.bytesToProcess  + i]).toUpperCase() + " ";
                        String t = hd.toUpperCase();
                        //                        if(t.length()>2){
                        //                            t=t.substring(t.length()-2);
                        //                        }
                        //                        System.out.println(t);
                      if(timeStampChkBx.isSelected()){
                           doc.insertString( doc.getLength(),getDateTime(),null);
                       }
                        doc.insertString(doc.getLength(), t, null);
                       // ZigBeeToolView.datatxnTxtArea.setText(  ZigBeeToolView.datatxnTxtArea.getText().concat(t));
                    }
                     doc.insertString(doc.getLength(),System.getProperty("line.separator"), null);
   datatxnTxtArea.setCaretPosition(datatxnTxtArea.getDocument().getLength());
//
                    //  is.reset();
                    break;
                }
               // serialHelper.disconnect();
                failed++;
                //   firmwareHandler.WriteRequest(sendBytes);
            } catch (IOException ex) {
                Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(failed==100){
           GiveResponse("Attempted 100 times, but failed", Color.RED);
           checksum.yield();

        }
 else {
           
 }
        progressBar.setIndeterminate(false);
        EnableDisableCalButtons(true);

                }catch(Exception chksum){
              //   lblChecksumValue.setText(" N/A");

                }

                 }});
                 checksum.start();
        }
        //   firmwareHandler.WriteRequest(sendBytes);
    }//GEN-LAST:event_sendBtnActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

        datatxnTxtArea.setText("");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
    RequestPW rpw = new RequestPW(null, true);
            rpw.setLocationRelativeTo(this.getFrame());
           // Image icon2 = Toolkit.getDefaultToolkit().getImage(ProdProgrammerView.class.getResource("resources/login_icon.gif"));
              rpw.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void connectBtnItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_connectBtnItemStateChanged
    //  EnableDisableCalButtons(false);
    }//GEN-LAST:event_connectBtnItemStateChanged

    private void cmdTxtFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmdTxtFieldFocusGained
        cmdTxtField.setText("");
    }//GEN-LAST:event_cmdTxtFieldFocusGained

    private void btnConfigureDeviceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigureDeviceActionPerformed
    final responseProcessor r = new responseProcessor();
  //   boolean sent=false;
        if(panIDtxtField.getText().length() !=4 && panIDtxtField.getText().length()!=0){
   new JOptionPane().showMessageDialog(this.getFrame(), "Please provide 2 bytes for PAN ID \n","Error",  JOptionPane.ERROR_MESSAGE);

}
 else{
        Thread dhum = new Thread(new Runnable() {
        public void run  ()
        {
            Thread dhum1=null;
            

//        OutputStream os = serialHelper.getSerialOutputStream();
        try {
            String hj = panIDtxtField.getText();
           if(hj.length()!=0){
               int dummy;
                           
              dummy = Integer.parseInt(panIDtxtField.getText(),16);
                          
              PANID[6]=(byte)((dummy & 0xFF00) >> 8);

            PANID[7]=(byte)(dummy & 0xFF); //9844172044
           // PANID[8] = checksumCalculator(PANID);
            }
if(rBtnCoOrd.isSelected()){
            boolean sent=false;
            lblStatus("Resetting device...",Color.blue,0);
            sendThis(SYST_RST , r.OP_RESET);
            Thread.sleep(1000);
            
            //if(sent)
            {
                lblStatus("Writing Config...",Color.blue,0);
                sent=sendThis(WRT_CONFG,r.OP_WR_CFG );
                Thread.sleep(1000);
            }
            
          //  if(sent)
            {
            lblStatus("Resetting device...",Color.blue,0);

            sent=sendThis(SYST_RST , r.OP_RESET);
            Thread.sleep(1000);
    }

            //if(sent)
            {
            lblStatus("Log Coordinates...",Color.blue,0);
            sendThis(LOG_CORD, r.OP_WR_LOG_TYPE);
            Thread.sleep(1000);
    }
           // if(sent)
            {
            lblStatus("Setting Extended PAN IDs...",Color.blue,0);
            sent=sendThis(EPID , r.OP_WR_EPAN_ID);
            Thread.sleep(1000);
    }
           //if(sent)
           {
            lblStatus("Setting PAN IDs...",Color.blue,0);

            sent=sendThis(PANID , r.OP_WR_PAN_ID);
            Thread.sleep(1000);
    }
            //if(sent)
            {
            lblStatus("Setting AF Registers...",Color.blue,0);

            sent=sendThis(AF_reg , r.OP_AF_REG);
            Thread.sleep(1000);
    }
          // if(sent)
           {
            lblStatus("Finalizing Configuration...",Color.blue,0);

            sent=sendThis(ZDO_STRT , r.OP_ZDO_START);
             Thread.sleep(1000);
    }

//            if(sent){
            GiveResponse("Done...", Color.blue);
            try {
                sound(2000, 500, 1.0);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
            }
//            }
//            else GiveResponse("Failed to Configure", Color.RED);

            }
            else if(rBtnRouter.isSelected()){
            lblStatus("Resetting device...",Color.blue,0);
            sendThis(SYST_RST, r.OP_RESET);
            Thread.sleep(1000);
            lblStatus("Writing Config...",Color.blue,0);
            sendThis(WRT_CONFG , r.OP_WR_CFG);              // need to confirm the op command
            Thread.sleep(1000);
            lblStatus("Resetting device...",Color.blue,0);

            sendThis(SYST_RST, r.OP_RESET);
            Thread.sleep(1000);
            lblStatus("Log Coordinates...",Color.blue,0);
            sendThis(ROUTER, r.OP_WR_LOG_TYPE);
            Thread.sleep(1000);
            lblStatus("Setting Extended PAN IDs...",Color.blue,0);
            sendThis(EPID , r.OP_WR_EPAN_ID);
            Thread.sleep(1000);
            lblStatus("Setting PAN IDs...",Color.blue,0);

            sendThis(PANID , r.OP_WR_PAN_ID);
            Thread.sleep(1000);
            lblStatus("Setting AF Registers...",Color.blue,0);

            sendThis(AF_REG_Router, r.OP_AF_REG);
            Thread.sleep(1000);
            lblStatus("Finalizing Configuration...",Color.blue,0);
            sendThis(ZDO_STRT, r.OP_ZDO_START);
            Thread.sleep(1000);
            GiveResponse("Done...", Color.blue);
            try {
                sound(2000, 500, 1.0);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
            }
            }

            else if(rBtnEndDevice.isSelected()){
            lblStatus("Resetting device...",Color.blue,0);
            sendThis(SYST_RST, r.OP_RESET);
            Thread.sleep(1000);
            lblStatus("Writing Config...",Color.blue,0);
            sendThis(WRT_CONFG , r.OP_WR_CFG);              // need to confirm the op command
            Thread.sleep(1000);
            lblStatus("Resetting device...",Color.blue,0);

            sendThis(SYST_RST, r.OP_RESET);
            Thread.sleep(1000);
            lblStatus("Log Coordinates...",Color.blue,0);
            sendThis(LOG_END_DEVICE, r.OP_WR_LOG_TYPE);
            Thread.sleep(1000);
            lblStatus("Setting Extended PAN IDs...",Color.blue,0);
            sendThis(EPID , r.OP_WR_EPAN_ID);
            Thread.sleep(1000);
            lblStatus("Setting PAN IDs...",Color.blue,0);

            sendThis(PANID , r.OP_WR_PAN_ID);
            Thread.sleep(1000);
            lblStatus("Setting AF Registers...",Color.blue,0);

            sendThis(AF_reg, r.OP_AF_REG);
            Thread.sleep(1000);
            lblStatus("Finalizing Configuration...",Color.blue,0);

            sendThis(ZDO_STRT, r.OP_ZDO_START);
            Thread.sleep(1000);
            GiveResponse("Done...", Color.blue);
            try {
                sound(2000, 500, 1.0);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
                    try {
                        dhum1 = new Thread(new Runnable() {
        public void run  ()
        {
                    try {
                        try {
                                try {
                                    KeepChecking();
                                } catch (BadLocationException ex) {
                                    Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
                                }
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
                    }

                            }});
                            dhum1.start();
                    } catch (Exception ex) {
                        Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
                    }


        } catch (InterruptedException ex) {
            Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
        }
  //          catch (IOException ex) {
//            Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
//        }
       }

            private void KeepChecking() throws IOException, InterruptedException, BadLocationException {
                //InputStream is = serialHelper.getSerialInputStream();
                while(true){
                    Thread.sleep(100);
//                int y =serialHelper.getSerialInputStream().available();
                if (serialHelper.data_available) {
                    serialHelper.data_available=false;
                    {
                        byte[] p = new byte[serialHelper.len];
                        System.out.println("Number of bytes seen = "+serialHelper.len);
                        System.arraycopy(serialHelper.buffer, 0, p, 0, p.length);
                        String hd ="";//new String(p);
                        for(int v =0;v<p.length;v++){
                            String kl = Byte.toString(p[v]);
                            String m = Long.toHexString(Long.parseLong(kl));
                            if(m.length()>2){
                                m=m.substring(m.length()-2);
                            }
                            hd=hd.concat(" "+ m);
                        }
                        //  logData += " : 0x" + Integer.toHexString(uartRxHandler.currentData[uartRxHandler.bytesToProcess  + i]).toUpperCase() + " ";
                        String t = hd.toUpperCase();
                        //                        if(t.length()>2){
                        //                            t=t.substring(t.length()-2);
                        //                        }
                        //                        System.out.println(t);
                      if(timeStampChkBx.isSelected()){
                           doc.insertString( doc.getLength(),getDateTime(),Rx);
                       }
                        doc.insertString(doc.getLength(), t, Rx);
                       // ZigBeeToolView.datatxnTxtArea.setText(  ZigBeeToolView.datatxnTxtArea.getText().concat(t));
                    }
                     doc.insertString(doc.getLength(),System.getProperty("line.separator"), Rx);
                    datatxnTxtArea.setCaretPosition(datatxnTxtArea.getDocument().getLength());
                    try {
                        //
                        //  is.reset();
                        serialHelper.getSerialInputStream().close();
                        //  break;
                    } catch (IOException ex) {
                        Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
       }
            }
        });
       dhum.start();

    }
    }//GEN-LAST:event_btnConfigureDeviceActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
       deviceStatusFrame dq = new deviceStatusFrame();
       dq.setLocationRelativeTo(this.getFrame());
       dq.setVisible(true);

    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        communicationFrame cf = new communicationFrame();
        cf.setLocationRelativeTo(this.getFrame());
        cf.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        jTextField2.setEnabled(!jCheckBox1.isSelected());
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
      FileDialog fileDialog = new FileDialog(new Frame(), "Save the text file...", FileDialog.SAVE);
        fileDialog.setFilenameFilter(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });
        String fName = getDateTime();
        fName= fName.replace(":", "-");
        fName= "ZigBee"+fName.replace(" ", "");
        
        fileDialog.setFile(fName+".txt");

        fileDialog.setLocationByPlatform(true);
        fileDialog.setVisible(true);
       
        String directory= fileDialog.getDirectory();

        String fileName=fileDialog.getFile();
        if(directory!=null){
        directory=directory.replace(File.separatorChar, '\\');

        fileName=fileName.replace(File.separatorChar, '\\');
         String fullPath = directory + "\\"+fileName;
        File p = new File(fullPath);
        try {
            p.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileOutputStream report = null;
            try {
               report = new FileOutputStream(p);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
            }
            byte[] output = datatxnTxtArea.getText().getBytes();
            try {
                report.write(output);
                report.close();
            } catch (IOException ex) {
                Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


        
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void dataFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dataFieldActionPerformed
 
    }//GEN-LAST:event_dataFieldActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        PacketFrame h = new PacketFrame();
        h.setLocationRelativeTo(this.getFrame());
        h.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed


         byte[] sendBytes = new byte[dataField.getText().length()/2];
        try {
            byte[] kl = dataField.getText().getBytes("UTF-8");
          System.out.print("hhh");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
        }
        long h= Long.parseLong(dataField.getText(),16);
        for(int y=dataField.getText().length()/2;y>0;y--){
            sendBytes[y-1]=(byte) (h & 0xff);
            h=h>>8;
        }
        final byte[] newAF_data = new byte[15+sendBytes.length];
        System.arraycopy(AF_Data, 0, newAF_data, 0, newAF_data.length);
        newAF_data[13] = (byte) (sendBytes.length);
        newAF_data[1] = (byte) (sendBytes.length+10);
        System.arraycopy(sendBytes, 0, newAF_data, 14, sendBytes.length);

        newAF_data[newAF_data.length-1] = checksumCalculator(newAF_data);

        


         Thread ted = new Thread(new Runnable() {

        public void run()
        {
        //   ListSerialPorts();
            if(!jCheckBox1.isSelected())  {
               long j =  Long.parseLong(jTextField2.getText());
               for(int n =0; n<j;n++){

               sendThis(newAF_data, 100);
                }
            }

        }
         });
         ted.start();

     

//        OutputStream os = serialHelper.getSerialOutputStream();
//        InputStream is = serialHelper.getSerialInputStream();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnConfigureDevice;
    public javax.swing.JComboBox cmbComSelector;
    public javax.swing.JTextField cmdTxtField;
    public javax.swing.JButton connectBtn;
    public javax.swing.JTextField dataField;
    public static javax.swing.JTextPane datatxnTxtArea;
    public javax.swing.JLabel extPanIDlabel;
    public javax.swing.JTextField extPanIDtxtField;
    public javax.swing.JButton jButton1;
    public javax.swing.JCheckBox jCheckBox1;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JMenu jMenu1;
    public javax.swing.JMenu jMenu2;
    public javax.swing.JMenuItem jMenuItem1;
    public javax.swing.JMenuItem jMenuItem2;
    public javax.swing.JMenuItem jMenuItem3;
    public javax.swing.JMenuItem jMenuItem4;
    public javax.swing.JMenuItem jMenuItem5;
    public javax.swing.JMenuItem jMenuItem6;
    public javax.swing.JMenuItem jMenuItem7;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JPanel jPanel3;
    public javax.swing.JPanel jPanel4;
    public javax.swing.JPanel jPanel5;
    public javax.swing.JPanel jPanel6;
    public javax.swing.JRadioButton jRadioButton1;
    public javax.swing.JRadioButton jRadioButton2;
    public javax.swing.JRadioButton jRadioButton3;
    public javax.swing.JRadioButton jRadioButton4;
    public javax.swing.JRadioButton jRadioButton5;
    public javax.swing.JRadioButton jRadioButton6;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTextField jTextField2;
    public javax.swing.JLabel lblStatus;
    public javax.swing.JPanel mainPanel;
    public javax.swing.JMenuBar menuBar;
    public javax.swing.JTextField panIDtxtField;
    private javax.swing.JProgressBar progressBar;
    public javax.swing.JRadioButton rBtnCoOrd;
    public javax.swing.JRadioButton rBtnEndDevice;
    public javax.swing.JRadioButton rBtnRouter;
    public javax.swing.JButton sendBtn;
    public javax.swing.JPanel statusPanel;
    public javax.swing.JCheckBoxMenuItem timeStampChkBx;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;

    private void initialGUISettings() {
        connectBtn.setEnabled(false);
        datatxnTxtArea.setToolTipText("Text in Blue - Transmitted data; Text in Red - Recieved data");
        resourceMap = getResourceMap();
        Image mainLogo = Toolkit.getDefaultToolkit().getImage(ZigBeeToolView.class.getResource("resources/MainIcon.png"));
        this.getFrame().setIconImage(mainLogo);

        this.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getFrame().setResizable(false);
        connectBtn.setIcon(resourceMap.getIcon("connect.icon"));
        this.getFrame().setTitle("ZigBee Configuration Tool Beta v1.0.1");
        ButtonGroup group = new ButtonGroup();
        group.add(rBtnCoOrd);
        group.add(rBtnEndDevice);
        group.add(rBtnRouter);
        rBtnCoOrd.setSelected(true);


         ButtonGroup dataFormat = new ButtonGroup();
         dataFormat.add(jRadioButton4);
         dataFormat.add(jRadioButton5);
         dataFormat.add(jRadioButton6);
         jRadioButton6.setSelected(true);

         ButtonGroup pattern = new ButtonGroup();
         pattern.add(jRadioButton1);
          pattern.add(jRadioButton2);
           pattern.add(jRadioButton3);
            jRadioButton1.setSelected(true);


        // group read/write  button
//        buttonGroup1.add(radBtnRead);
//        buttonGroup1.add(radBtnWrite);
      //  EnableDisableCalButtons(false);
         Thread ted = new Thread(new Runnable() {

        public void run()
        {
           ListSerialPorts();
        }
         });
         ted.start();
     

       
    }

    private void ListSerialPorts() {

        lblStatus.setText("Detecting serial ports in this machine...");
        lblStatus.setForeground(Color.BLUE);

          Thread te = new Thread(new Runnable() {

        public void run()
        {
           progressBar.setIndeterminate(true);
         String[] serialPorts  = serialHelper.getSerialPorts();

         if(serialPorts.length==0){

             lblStatus.setText("No Communication port available. Please connect one and restart application.");
             lblStatus.setForeground(Color.red);
             connectBtn.setEnabled(false);
         }
 else{
             connectBtn.setEnabled(true);
 }

        cmbComSelector.removeAllItems();
       // cmbComSelector.addItem("Select COM Port");
        if (serialPorts != null)
        {
                for (int i = 0; i < serialPorts.length; i++)
                {
                    cmbComSelector.addItem(serialPorts[i]);
                }

        }
        else
        {
            EnableDisableCalButtons(false);
            lblStatus.setText("No serial ports available");
        }

        ZigBeeToolApp.getApplication().view.getFrame().repaint();

          if(serialPorts.length==1){
             lblStatus.setText("Only 1 Com port avialable - "+ serialPorts[0]);
             connectBtn.doClick();
          }
 else{
           lblStatus.setText("Avaialble Com Ports : "+serialPorts.length) ;
 }

progressBar.setIndeterminate(false);
              }});

              te.start();
        
    }

    private void EnableDisableCalButtons(boolean b) {
        cmdTxtField.setEnabled(b);
        sendBtn.setEnabled(b);
        btnConfigureDevice.setEnabled(b);
        panIDtxtField.setEnabled(b);
        extPanIDtxtField.setEnabled(b);
        dataField.setEnabled(b);
        jTextField2.setEnabled(b);
       // jButton1.setEnabled(b);


    }

    public void lblStatus(String string,Color g,int time){
        Icon ic=null;
        lblStatus.setForeground(g);//setBackground(g);
        lblStatus.setText(string);
         lblStatus.setVisible(true);
        if(g==Color.RED){
         // ic=ProdProgrammerApp.getApplication().getView().getres().getIcon("wrong.icon");
         // viewModeBtn.setIcon(resourceMap.getIcon("lessImage.icon"));

        }
 else{
          // ic=ProdProgrammerApp.getApplication().getView().getres().getIcon("correct.icon");
       }
       lblStatus.setIcon(ic);
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
           // Exceptions.printStackTrace(ex);
        }
       if(time!=0){
       lblStatus.setText("");
        lblStatus.setIcon(null);
         lblStatus.setVisible(false);
        }
    }

     private void GiveResponse(final String string, final Color color) {
          Thread te = new Thread(new Runnable() {

        public void run()
        {
        lblStatus(string, color,3000);
        }
         });
         te.start();
    }

       private  final static String getDateTime()
{

    DateFormat df = new SimpleDateFormat("HH:mm:ss -");
   // df.setTimeZone(TimeZone.getgetTimeZone("PST"));
    return df.format(new Date());
}

    private boolean sendThis(byte[] command , int com) {
        boolean  ret=false;
        int failed = 0;
          currentSending=command;
          String q = "";
          for(int v =0;v<command.length;v++){
                            String kl = Byte.toString(command[v]);
                            String m = Long.toHexString(Long.parseLong(kl));
                            if(m.length()>2){
                                m=m.substring(m.length()-2);
                            }
                            if(m.length()<2){
                                m="0".concat(m);
                            }
                            q=q.concat(" "+ m);
                        }
        try {
            doc.insertString(doc.getLength(), getDateTime(), Tx);
            doc.insertString(doc.getLength(), q.toUpperCase(), Tx);
            doc.insertString(doc.getLength(), System.getProperty("line.separator"), Tx);

            //  memviewMode();
        } catch (BadLocationException ex) {
            Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
        }
       //  memviewMode();

         try{
        progressBar.setIndeterminate(true);
        EnableDisableCalButtons(false);
        while(failed<10){
            try {

                serialHelper.getSerialOutputStream().flush();// is.reset();
                serialHelper.getSerialOutputStream().write(currentSending);
            } catch (IOException ex) {
             //   Logger.getLogger(EnergyMeterFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Thread.sleep(400);
            try {
                
                if ( serialHelper.data_available) {
                    serialHelper.data_available=false;
                    {
                        byte[] p = new byte[serialHelper.len];
                        System.out.println("Number of bytes seen = "+serialHelper.len);
                        System.arraycopy(serialHelper.buffer, 0, p, 0, p.length);
                       
                        serialHelper.getSerialInputStream().read(p);
                        responseProcessor rp = new responseProcessor();
                        resp = new responseStruct();

                        resp.startByte = p[0];
                        resp.cmdLength = p[1];
                        resp.cmd0 = p[2];
                        resp.cmd1 =  p[3];
                        resp.dataBuffer = new byte[resp.cmdLength];
                        System.arraycopy(p, 4, resp.dataBuffer, 0, resp.cmdLength);
                        resp.checksum = p[p.length-1];

                        if(rp.processData(com , resp) == 1) ret = true;




                        String hd ="";//new String(p);
                        for(int v =0;v<p.length;v++){
                            String kl = Byte.toString(p[v]);
                            String m = Long.toHexString(Long.parseLong(kl));
                            if(m.length()>2){
                                m=m.substring(m.length()-2);
                            }
                            if(m.length()<2){
                                m = "0".concat(m);
                            }
                            hd=hd.concat(" "+ m);
                        }
                        //  logData += " : 0x" + Integer.toHexString(uartRxHandler.currentData[uartRxHandler.bytesToProcess  + i]).toUpperCase() + " ";
                        String t = hd.toUpperCase();
                        //                        if(t.length()>2){
                        //                            t=t.substring(t.length()-2);
                        //                        }
                        //                        System.out.println(t);
                       if(timeStampChkBx.isSelected()){
                           doc.insertString( doc.getLength(),getDateTime(),Rx);
                       }
                        doc.insertString(doc.getLength(), t,Rx );
                       // ZigBeeToolView.datatxnTxtArea.setText(  ZigBeeToolView.datatxnTxtArea.getText().concat(t));
                    }
                     doc.insertString(doc.getLength(),System.getProperty("line.separator"), Rx);

//                    ZigBeeToolView.datatxnTxtArea.setText(  ZigBeeToolView.datatxnTxtArea.getText().concat(System.getProperty("line.separator")));
                    datatxnTxtArea.setCaretPosition(datatxnTxtArea.getDocument().getLength());
//
                    //  is.reset();
                    serialHelper.getSerialInputStream().close();
                    break;
                }
               // serialHelper.disconnect();
                failed++;
                //   firmwareHandler.WriteRequest(sendBytes);
            } catch (IOException ex) {
                Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(failed==10){
           GiveResponse("Attempted 10 times, but failed", Color.RED);
           checksum.yield();

        }
 else {

 }
        progressBar.setIndeterminate(false);
        EnableDisableCalButtons(true);

                }catch(Exception chksum){
              //   lblChecksumValue.setText(" N/A");

                }

//                 }});
//                 checksum.start();

          return ret;
    }

    byte checksumCalculator(byte[] p){
        byte ret= 0;
        byte[] newByte = new byte[p.length-2];
        System.arraycopy(p, 1, newByte, 0, newByte.length);
        for(int y=0;y<newByte.length;y++){
           
            ret ^= newByte[y];
        }

        return ret;


    }

  public String sendCmd(byte[] command , int com) {
        boolean  ret=false;
        int failed = 0;
         String t="";
          currentSending=command;
        try{
        EnableDisableCalButtons(false);
        OutputStream os = serialHelper.getSerialOutputStream();
        InputStream is = serialHelper.getSerialInputStream();
        while(failed<100){
            try {

                os.flush();// is.reset();
                os.write(currentSending);
            } catch (IOException ex) {
             }
            Thread.sleep(400);
            try {
                is=is = serialHelper.getSerialInputStream();
                int y =is.available();
                if ( y!= 0) {
                        byte[] p = new byte[y];
                        is.read(p);
                        responseProcessor rp = new responseProcessor();
                        resp = new responseStruct();
                        resp.startByte = p[0];
                        resp.cmdLength = p[1];
                        resp.cmd0 = p[2];
                        resp.cmd1 =  p[3];
                        resp.dataBuffer = new byte[resp.cmdLength];
                        System.arraycopy(p, 4, resp.dataBuffer, 0, resp.cmdLength);
                        resp.checksum = p[p.length-1];
                        if(rp.processData(com , resp) == 1) ret = true;
                        String hd ="";//new String(p);
                        for(int v =0;v<p.length;v++){
                            String kl = Byte.toString(p[v]);
                            String m = Long.toHexString(Long.parseLong(kl));
                            if(m.length()>2){
                                m=m.substring(m.length()-2);
                            }
                            hd=hd.concat(" "+ m);
                        }
                          t = hd.toUpperCase();


                    is.close();
                    return t;
                   // break;
                }
               // serialHelper.disconnect();
                failed++;
                //   firmwareHandler.WriteRequest(sendBytes);
            } catch (IOException ex) {
            //    Logger.getLogger(ZigBeeToolView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(failed==100){
          return "failed..";

        }
 else {

 }
        progressBar.setIndeterminate(false);
        EnableDisableCalButtons(true);

                }catch(Exception chksum){
              //   lblChecksumValue.setText(" N/A");

                }

//                 }});
//                 checksum.start();

          return t ;
    }


     private void sound(int hz, int msecs, double vol)
throws IllegalArgumentException, LineUnavailableException {

if (vol > 1.0 || vol < 0.0)
throw new IllegalArgumentException("Volume out of range 0.0- 1.0");

byte[] buf = new byte[msecs * 8];

for (int i=0; i<buf.length; i++) {
double angle = i / (8000.0 / hz) * 2.0 * Math.PI;
buf[i] = (byte)(Math.sin(angle) * 127.0 * vol);
}

// shape the front and back ends of the wave form
for (int i=0; i<20 && i < buf.length / 2; i++) {
buf[i] = (byte)(buf[i] * i / 20);
buf[buf.length - 1 - i] = (byte)(buf[buf.length - 1 - i] *
i / 20);
}

AudioFormat af = new AudioFormat(8000f,8,1,true,false);
SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
sdl.open(af);
sdl.start();
sdl.write(buf,0,buf.length);
sdl.drain();
sdl.close();
}
}
