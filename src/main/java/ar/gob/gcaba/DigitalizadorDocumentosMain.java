package ar.gob.gcaba;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ar.gob.gcaba.console.TextAreaAppender;
import ar.gob.gcaba.constants.TipoRegistroEnum;
import ar.gob.gcaba.service.DigitalizadorService;


@SuppressWarnings("serial")
public class DigitalizadorDocumentosMain extends JFrame{

    public DigitalizadorDocumentosMain(){
        consoleLog = new JTextArea();
        consoleApender = new TextAreaAppender(consoleLog);
        Logger.getRootLogger().addAppender(consoleApender);
        initComponents();
    }
	
//    @Resource(name ="digitalizadorService")
	static DigitalizadorService digitalizadorService;
	JPanel topPanel;
	JFileChooser fileChooser;
	JButton btnExaminar;
	JButton btnDetener;
	JTextField filePath;
	File folder;
	JButton btnIniciar;
	List<File> pdfFiles;
	JComboBox comboBox;
	private JTextArea consoleLog;
	@Autowired
	private static Logger logger;
	TextAreaAppender consoleApender;
	Thread processThread;
	
	public void initComponents(){
		topPanel = new JPanel();
		
		getContentPane().add(topPanel, BorderLayout.NORTH);
		btnExaminar = new JButton("Examinar");
		JLabel lblSeleccionarCarpetaOrigen = new JLabel("Seleccionar carpeta origen:");
		JLabel lblIndicarDestino = new JLabel("Indicar Destino:");
		
		filePath = new JTextField();
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(TipoRegistroEnum.values()));
		
		btnIniciar= new JButton("Iniciar");
		btnIniciar.setEnabled(false);
		
		JPanel centerPanel = new JPanel();
		
		JButton btnLimpiarConsola = new JButton("Limpiar consola");
		
		btnDetener= new JButton("Detener");
		btnDetener.setEnabled(false);
		
		//Set top Panel Layout
		GroupLayout gl_panel = new GroupLayout(topPanel);
		gl_panel.setHorizontalGroup(
		    gl_panel.createParallelGroup(Alignment.LEADING)
		        .addGroup(gl_panel.createSequentialGroup()
		            .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
		                .addGroup(gl_panel.createSequentialGroup()
		                    .addGap(33)
		                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
		                        .addComponent(lblSeleccionarCarpetaOrigen)
		                        .addComponent(lblIndicarDestino))
		                    .addPreferredGap(ComponentPlacement.UNRELATED)
		                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
		                        .addGroup(gl_panel.createSequentialGroup()
		                            .addComponent(filePath, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
		                            .addPreferredGap(ComponentPlacement.RELATED, 58, Short.MAX_VALUE))
		                        .addGroup(gl_panel.createSequentialGroup()
		                            .addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
		                            .addPreferredGap(ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
		                            .addComponent(btnIniciar, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
		                            .addGap(18)))
		                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
		                        .addComponent(btnExaminar, 0, 0, Short.MAX_VALUE)
		                        .addComponent(btnDetener, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		                    .addGap(6))
		                .addGroup(gl_panel.createSequentialGroup()
		                    .addContainerGap()
		                    .addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
		                        .addComponent(btnLimpiarConsola)
		                        .addComponent(centerPanel, GroupLayout.PREFERRED_SIZE, 446, GroupLayout.PREFERRED_SIZE))))
		            .addContainerGap(16, GroupLayout.PREFERRED_SIZE))
		);
		gl_panel.setVerticalGroup(
		    gl_panel.createParallelGroup(Alignment.LEADING)
		        .addGroup(gl_panel.createSequentialGroup()
		            .addGap(6)
		            .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
		                .addComponent(filePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
		                .addComponent(lblSeleccionarCarpetaOrigen)
		                .addComponent(btnExaminar))
		            .addPreferredGap(ComponentPlacement.UNRELATED)
		            .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
		                .addComponent(lblIndicarDestino)
		                .addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
		                .addComponent(btnIniciar)
		                .addComponent(btnDetener))
		            .addGap(18)
		            .addComponent(centerPanel, GroupLayout.PREFERRED_SIZE, 262, GroupLayout.PREFERRED_SIZE)
		            .addPreferredGap(ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
		            .addComponent(btnLimpiarConsola))
		);
        centerPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
        consoleLog = consoleApender.getConsoleLog();
        DefaultCaret caret = (DefaultCaret)consoleLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        consoleLog.setRows(20);
        consoleLog.setColumns(2);
        consoleLog.setEditable(false);
		
        scrollPane.setViewportView(consoleLog);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
		topPanel.setLayout(gl_panel);
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		setTitle("Digitalizacion de Archivos PDF a documentos vinculados");
		setVisible(true);
		setSize(500, 428);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Add Action listeners para los botones
		btnExaminar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                logger.info("Examinando carpetas para elegir carpeta origen de PDFs...");
				fileChooser.showDialog(topPanel, "Seleccionar");
				folder = fileChooser.getSelectedFile();
				if (folder!=null){
                    filePath.setText(folder.getAbsolutePath());
                    filePath.setEditable(false);
                    btnIniciar.setEnabled(true);
                    logger.info("Folder origen establecido como: "+folder.getAbsolutePath());
				}
			}
		});
		
		btnIniciar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
			    btnIniciar.setEnabled(false);
			    btnDetener.setEnabled(true);
			    btnIniciar.setText("Procesando");
			    processThread = new Thread( new Runnable() {
                    
                    @Override
                    public void run() {
                                
                        logger.info("Iniciando lectura documentos a vincular...");
                        pdfFiles = digitalizadorService.obtenerListaPDFFiles(folder);
                        if (!pdfFiles.isEmpty()) {
                            logger.info("Iniciando proceso de carga de PDFs en el folder " + folder.getAbsolutePath());
                            String tipoRegistro = comboBox.getSelectedItem().toString();
                            digitalizadorService.procesarLecturaArchivos(pdfFiles, tipoRegistro);
                        } else {
                            JOptionPane.showMessageDialog(null, "No se encuentran Archivos PDF en la ruta especificada", "Aviso", 
                                    JOptionPane.WARNING_MESSAGE);
                        }
                        btnIniciar.setEnabled(true);
                        btnIniciar.setText("Iniciar");
                        
                    }
                });
                processThread.start();
			}
		});
		
		btnDetener.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                btnDetener.setEnabled(false);
//                running=false;
                processThread.interrupt();
                
            }
        });
		
		btnLimpiarConsola.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                consoleLog.setText(null);
            }
        });
	}
	
    public static void main (String []args){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        digitalizadorService = (DigitalizadorService) ctx.getBean("digitalizadorService");
        logger = ctx.getBean(Logger.class);

        DigitalizadorDocumentosMain ddm = new DigitalizadorDocumentosMain();
	}
	
	
	
}

