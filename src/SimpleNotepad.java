
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.awt.Font;
import javax.swing.JCheckBoxMenuItem;

public class SimpleNotepad extends JFrame {

	private JPanel contentPane;
	private static TrayIcon trayIcon = null;
	static SystemTray tray = SystemTray.getSystemTray();
	private JTextArea textArea;
	private FileReader fileReader;
	private BufferedReader bufferedReader;
	private FileWriter fileWriter;
	private BufferedWriter bufferedWriter;
	private String strAll = "";
	private String orgaddr="";
	private static String name="New";
	private JPopupMenu popMenu;
	private JDialog dialog;
	private static JLabel donatelabel1;
	private static JLabel donatelabel2;
	private static JLabel donatelabel3;
	private static JLabel donatelabel4;
	private static SimpleNotepad frame;
	Toolkit toolkit=Toolkit.getDefaultToolkit();  
	Clipboard clipBoard=toolkit.getSystemClipboard();  
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
					frame= new SimpleNotepad();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					ImageIcon logo=new ImageIcon(SimpleNotepad.class.getResource("img/logo.png"));
					frame.setIconImage(logo.getImage());
	}
	/**
	 * Create the frame.
	 */
	public SimpleNotepad() {
		super("简单记事本");
		super.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
					exit();
			 }
			 
			});
		setBounds(100, 100, 1024, 768);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu menu = new JMenu("文件");
		menuBar.add(menu);
		
		JMenuItem mi_open = new JMenuItem("打开(O)...Ctrl+O");
		mi_open=setmiicon("img/open.png",mi_open,20,20);
		menu.add(mi_open);
		mi_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mi_open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openFile();
			}
		});
		JMenuItem mi_save = new JMenuItem("保存(S)...Ctrl+S");
		mi_save=setmiicon("img/save.png",mi_save,20,20);
		menu.add(mi_save);
		mi_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK));
		mi_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveFile();
			}
		});
		
		JMenuItem mi_resave = new JMenuItem("另存为(R)...Ctrl+R");
		mi_resave=setmiicon("img/resave.png",mi_resave,20,20);
		menu.add(mi_resave);
		mi_resave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,InputEvent.CTRL_MASK));
		mi_resave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resaveFile();
			}
		});
		
		JSeparator separator = new JSeparator();
		menu.add(separator);
		
		JMenuItem mi_exit = new JMenuItem("退出");
		mi_exit=setmiicon("img/closeall.png",mi_exit,20,20);
		
		menu.add(mi_exit);
		mi_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exit();
			}
		});
		
		JMenu mi_edit = new JMenu("编辑");
		menuBar.add(mi_edit);
		
		JMenuItem mi_copy = new JMenuItem("复制(C)...Ctrl+C");
		mi_copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mi_copy=setmiicon("img/copy.png",mi_copy,20,20);
		mi_edit.add(mi_copy);
		mi_copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				copy();
			}
		});
		
		JMenuItem mi_cut = new JMenuItem("剪切(X)...Ctrl+X");
		mi_cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mi_cut=setmiicon("img/cut.png",mi_cut,20,20);
		mi_edit.add(mi_cut);
		mi_cut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cut();
			}
		});
		
		JMenuItem mi_paste = new JMenuItem("粘贴(V)...Ctrl+V");
		mi_paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		mi_paste=setmiicon("img/paste.png",mi_paste,20,20);
		mi_edit.add(mi_paste);
		mi_paste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				paste();
			}
		});
		
		JMenu menu_2 = new JMenu("格式");
		menuBar.add(menu_2);
		
		final JCheckBoxMenuItem mi_reline = new JCheckBoxMenuItem("自动换行");
		menu_2.add(mi_reline);
		mi_reline.setState(true);
		mi_reline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(mi_reline.getState()==true){
					textArea.setLineWrap(true);
					textArea.setWrapStyleWord(true);   }
				if(mi_reline.getState()==false) {
					textArea.setLineWrap(false);
				}
			}
		});
		JMenuItem mi_font = new JMenuItem("字体");
		menu_2.add(mi_font);
		mi_font.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				font();
			}
		});
		
		JMenu menu_3 = new JMenu("关于");
		menuBar.add(menu_3);
		
		JMenuItem mi_shownote = new JMenuItem("说明");
		menu_3.add(mi_shownote);
		mi_shownote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.requestFocus();
				int choice=JOptionPane.showConfirmDialog(frame, 
					" 作者正在学习，以便日后做好更多功能         \n"+
					" 路漫漫其修远兮，吾将上下而求索               \n"+
					" 如需帮助，请邮箱联系，详见版权，谢谢！  \n",  
		                "说明", JOptionPane.CLOSED_OPTION,
		                JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		JMenuItem donated = new JMenuItem("捐赠");
		menu_3.add(donated);
		donated.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				donateshow();
			}
		});
		JMenuItem mi_copyright = new JMenuItem("版权");
		menu_3.add(mi_copyright);
		mi_copyright.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.requestFocus();
				int choice=JOptionPane.showConfirmDialog(frame, 
					"               本软件由 梦丶随心飞 独立开发            \n"+
					" 作者：梦丶随心飞 \n"+
					" 时间：2018-11-10                            \n"+
					" 邮箱：420443292@qq.com                      \n"+
					" 说明：软件中部分功能参考了大佬的代码，不足之处         \n" +
					" 请多多见谅，谢谢！  \n",  
		                "版权声明", JOptionPane.CLOSED_OPTION,
		                JOptionPane.INFORMATION_MESSAGE);
			}
		});
		popMenu = new JPopupMenu();
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		JScrollPane scroll = new JScrollPane(textArea); 
		textArea.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		scroll.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scroll.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		textArea.addMouseListener(new MouseRightClick());
		
		JMenuItem ropen = new JMenuItem("打开");
		ropen=setmiicon("img/open.png",ropen,20,20);
		ropen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openFile();
			}
		});
		
		JMenuItem rcopy = new JMenuItem("复制");
		rcopy=setmiicon("img/copy.png",rcopy,20,20);
		rcopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				copy();
			}
		});
		
		JMenuItem rcut = new JMenuItem("剪切");
		rcut=setmiicon("img/cut.png",rcut,20,20);
		rcut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cut();
			}
		});
		
		JMenuItem rpaste = new JMenuItem("粘贴");
		rpaste=setmiicon("img/paste.png",rpaste,20,20);
		rpaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				paste();
			}
		});
		
		JMenuItem rexit = new JMenuItem("退出");
		rexit=setmiicon("img/closeall.png",rexit,20,20);
		rexit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exit();
			}
		});
		
		popMenu.add(ropen);popMenu.addSeparator();
		popMenu.add(rcopy);popMenu.addSeparator();
		popMenu.add(rcut);popMenu.addSeparator();
		popMenu.add(rpaste);popMenu.addSeparator();
		popMenu.addSeparator();//设置一个分隔线
		popMenu.add(rexit);
		contentPane.add(scroll, BorderLayout.CENTER);
		
		JToolBar toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.EAST);
		toolBar.setOrientation(SwingConstants.VERTICAL);
		JButton open=new JButton();
		open=seticon("img/open.png", "打开",open,30,30);
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openFile();
			}
		});
		JButton save = new JButton();
		save=seticon("img/save.png","保存",save,30,30);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveFile();
			}
		});
		JButton copy = new JButton();
		copy=seticon("img/copy.png","复制",copy,30,30);
		copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				copy();
			}
		});
		JButton cut = new JButton();
		cut=seticon("img/cut.png","剪切",cut,30,30);
		cut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cut();
			}
		});
		JButton paste = new JButton();
		paste=seticon("img/paste.png","粘贴",paste,30,30);
		paste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				paste();
			}
		});
		JButton close = new JButton();
		close=seticon("img/close.png","最小化到托盘",close,30,30);
		close.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					frame.setVisible(false);
					ImageIcon trayImg = new ImageIcon(SimpleNotepad.class.getResource("img/logo.png"));
					trayIcon = new TrayIcon(trayImg.getImage(), "简单记事本", new PopupMenu());
					trayIcon.setImageAutoSize(true);
					tray.add(trayIcon);
				} catch (AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				trayIcon.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount() == 1&&e.getButton()==MouseEvent.BUTTON3){
							PopupMenu popupMenu = new PopupMenu();
							MenuItem m1=new MenuItem("打开面板");
					         popupMenu.add(m1);
					         popupMenu.addSeparator();
					         MenuItem m2=new MenuItem("退出");
					         popupMenu.add(m2);
					         trayIcon.setPopupMenu(popupMenu);
					         m1.addActionListener(new ActionListener() {
						 			public void actionPerformed(ActionEvent arg0) {
						 				init();
									}
								});
					         m2.addActionListener(new ActionListener() {
					 			public void actionPerformed(ActionEvent arg0) {
					 				exit();
								}
							});
							}
						else{
							init();
							}
					}
				});	
			}
 
		});
		toolBar.add(open);
		toolBar.add(save);
		toolBar.add(copy);
		toolBar.add(cut);
		toolBar.add(paste);
		toolBar.add(close);
	}
	public static JButton seticon(String img,String tip,JButton btn,int width,int height){
		ImageIcon Img_new=new ImageIcon(SimpleNotepad.class.getResource(img));
		Img_new=new ImageIcon(Img_new.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
		btn.setIcon(Img_new);
		btn.setToolTipText(tip);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		return btn;
	}
	public static JMenuItem setmiicon(String img,JMenuItem mi,int width,int height){
		ImageIcon Img_new=new ImageIcon(SimpleNotepad.class.getResource(img));
		Img_new=new ImageIcon(Img_new.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
		mi.setIcon(Img_new);
		mi.setFocusPainted(false);
		mi.setBorderPainted(false);
		return mi;
	}
	public void init(){
		tray.remove(trayIcon);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		ImageIcon logo=new ImageIcon(SimpleNotepad.class.getResource("img/logo.png"));
		frame.setIconImage(logo.getImage());
		frame.setExtendedState(JFrame.NORMAL);
		frame.toFront();
	}
	public void openFile(){
		JFileChooser fc=new JFileChooser();
		fc.setDialogTitle("打开");
		int r=fc.showOpenDialog(this);
		if(r==JFileChooser.APPROVE_OPTION){
			File file=fc.getSelectedFile();
			fc.setVisible(true);
				orgaddr = fc.getSelectedFile().getAbsolutePath();
				name=fc.getSelectedFile().getName();;
				frame.setTitle("简单记事本"+"-"+name);
				try {
				fileReader = new FileReader(orgaddr);
				bufferedReader = new BufferedReader(fileReader);
				String str = "";
				strAll = "";
				while((str = bufferedReader.readLine()) != null)
				{
					strAll += str + "\r\n";
				}
				textArea.setText(strAll);
				bufferedReader.close();
				fileReader.close();
			} catch (Exception e2) {
				// TODO: handle exception
//				e2.printStackTrace();
			}
		}
	}
	public void resaveFile(){
		JFileChooser fc_save = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "文本文档(*.txt)", "txt");
		fc_save.setFileFilter(filter);
		fc_save.setDialogTitle("保存到");
		fc_save.setVisible(true);
		int option = fc_save.showSaveDialog(frame);
		if(option==JFileChooser.APPROVE_OPTION){	
			File file = fc_save.getSelectedFile();
			String fname = fc_save.getName(file);	
			if(fname.indexOf(".txt")==-1){
				file=new File(fc_save.getCurrentDirectory(),fname+".txt");
			}
		try {
			fileWriter = new FileWriter(file);
			bufferedWriter = new BufferedWriter(fileWriter);
			StringReader ir=new java.io.StringReader(this.textArea.getText());
			BufferedReader br=new BufferedReader(ir);
			 String brString= br.readLine();
			while(brString!=null){
				bufferedWriter.write(brString);
				bufferedWriter.newLine();
				brString= br.readLine();
			}
			ir.close();
			br.close();
			bufferedWriter.close();
			fileWriter.close();
			saveexit();
		} catch (Exception e2) {
			// TODO: handle exception
//			e2.printStackTrace();
		}
	  }
	}
	public void saveFile(){
		if(orgaddr.equals("")){
			resaveFile();
		}
		else{
			try {
				fileWriter = new FileWriter(orgaddr);
				bufferedWriter = new BufferedWriter(fileWriter);
				StringReader ir=new java.io.StringReader(this.textArea.getText());
				BufferedReader br=new BufferedReader(ir);
				 String brString= br.readLine();
				while(brString!=null){
					bufferedWriter.write(brString);
					bufferedWriter.newLine();
					brString= br.readLine();
				}
				ir.close();
				br.close();
				bufferedWriter.close();
				fileWriter.close();
				saveexit();
			} catch (Exception e2) {
				// TODO: handle exception
	//			e2.printStackTrace();
			}
		}
	}
	public void exit(){
		if(textArea.getText().trim().equals("")||textArea.getText().equals(strAll)){
			System.exit(0);
		}
		int choice=JOptionPane.showConfirmDialog(null, "文件已修改，是否保存？",
                "提示", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
		if(choice==JOptionPane.YES_OPTION){
		saveFile();}
		else if(choice==JOptionPane.NO_OPTION){
		System.exit(0);}
		else if(choice==JOptionPane.CANCEL_OPTION){
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);}
	}
	public void saveexit(){
		int choice=JOptionPane.showConfirmDialog(null, "保存成功，是否关闭？",
                "消息", JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
		if(choice==JOptionPane.YES_OPTION){
					System.exit(0);
		}
		else if(choice==JOptionPane.NO_OPTION){
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);}
	}
	class MouseRightClick extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
		if(e.getButton()==e.BUTTON3)
		popMenu.show(e.getComponent(),e.getX(),e.getY());
		}
	}
	public void copy(){
	 textArea.requestFocus();  
     String text= textArea.getSelectedText();  
     StringSelection selection=new StringSelection(text);  
     clipBoard.setContents(selection,null);  
	}
	public void cut(){
		 textArea.requestFocus();  
         String text= textArea.getSelectedText();  
         StringSelection selection=new StringSelection(text);  
         clipBoard.setContents(selection,null);  
         textArea.replaceRange("",textArea.getSelectionStart(), textArea.getSelectionEnd());  
	}
	public void paste(){
		textArea.requestFocus();  
        Transferable contents=clipBoard.getContents(this);  
        if(contents==null)return;  
        String text="";  
        try {
			text=(String)contents.getTransferData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        textArea.replaceRange(text,textArea.getSelectionStart(),textArea.getSelectionEnd());  
	}
    public void font()  {   
    	final JDialog fontDialog=new JDialog(this,"字体设置",false);  
        Container con=fontDialog.getContentPane();  
        con.setLayout(new FlowLayout(FlowLayout.LEFT));  
        JLabel fontLabel=new JLabel("字体(F)：");  
        fontLabel.setPreferredSize(new Dimension(100,20));
        JLabel styleLabel=new JLabel("字形(Y)：");  
        styleLabel.setPreferredSize(new Dimension(100,20));  
        JLabel sizeLabel=new JLabel("大小(S)：");  
        sizeLabel.setPreferredSize(new Dimension(100,20));  
        final JLabel sample=new JLabel("简单记事本 SimpleNotepad");   
        final JTextField fontText=new JTextField(9);  
        fontText.setPreferredSize(new Dimension(200,20));  
        final JTextField styleText=new JTextField(8);  
        styleText.setPreferredSize(new Dimension(200,20));  
        final int style[]={Font.PLAIN,Font.BOLD,Font.ITALIC,Font.BOLD+Font.ITALIC};  
        final JTextField sizeText=new JTextField(5);  
        sizeText.setPreferredSize(new Dimension(200,20));  
        JButton okButton=new JButton("确定");  
        JButton cancel=new JButton("取消");  
        cancel.addActionListener(new ActionListener()  
        {   public void actionPerformed(ActionEvent e)  
            {   fontDialog.dispose();     
            }  
        });  
        Font currentFont=textArea.getFont();  
        fontText.setText(currentFont.getFontName());  
        fontText.selectAll();  
        if(currentFont.getStyle()==Font.PLAIN)  
            styleText.setText("常规");  
        else if(currentFont.getStyle()==Font.BOLD)  
            styleText.setText("粗体");  
        else if(currentFont.getStyle()==Font.ITALIC)  
            styleText.setText("斜体");  
        else if(currentFont.getStyle()==(Font.BOLD+Font.ITALIC))  
            styleText.setText("粗斜体");  
        styleText.selectAll();  
        String str=String.valueOf(currentFont.getSize());  
        sizeText.setText(str);  
        sizeText.selectAll();  
        final JList fontList,styleList,sizeList;  
        GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();  
        final String fontName[]=ge.getAvailableFontFamilyNames();  
        fontList=new JList(fontName);  
        fontList.setFixedCellWidth(86);  
        fontList.setFixedCellHeight(20);  
        fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  
        final String fontStyle[]={"常规","粗体","斜体","粗斜体"};  
        styleList=new JList(fontStyle);  
        styleList.setFixedCellWidth(86);  
        styleList.setFixedCellHeight(20);  
        styleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  
        if(currentFont.getStyle()==Font.PLAIN)  
            styleList.setSelectedIndex(0);  
        else if(currentFont.getStyle()==Font.BOLD)  
            styleList.setSelectedIndex(1);  
        else if(currentFont.getStyle()==Font.ITALIC)  
            styleList.setSelectedIndex(2);  
        else if(currentFont.getStyle()==(Font.BOLD+Font.ITALIC))  
            styleList.setSelectedIndex(3);  
        final String fontSize[]={"8","9","10","11","12","14","16","18","20","22","24","26","28","36","48","72"};  
        sizeList=new JList(fontSize);  
        sizeList.setFixedCellWidth(43);  
        sizeList.setFixedCellHeight(20);  
        sizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  
        fontList.addListSelectionListener(new ListSelectionListener()  
        {   public void valueChanged(ListSelectionEvent event)  
            {   fontText.setText(fontName[fontList.getSelectedIndex()]);  
                fontText.selectAll();  
                Font sampleFont1=new Font(fontText.getText(),style[styleList.getSelectedIndex()],Integer.parseInt(sizeText.getText()));  
                sample.setFont(sampleFont1);  
            }  
        });  
        styleList.addListSelectionListener(new ListSelectionListener()  
        {   public void valueChanged(ListSelectionEvent event)  
            {   int s=style[styleList.getSelectedIndex()];  
                styleText.setText(fontStyle[s]);  
                styleText.selectAll();  
                Font sampleFont2=new Font(fontText.getText(),style[styleList.getSelectedIndex()],Integer.parseInt(sizeText.getText()));  
                sample.setFont(sampleFont2);  
            }  
        });  
        sizeList.addListSelectionListener(new ListSelectionListener()  
        {   public void valueChanged(ListSelectionEvent event)  
            {   sizeText.setText(fontSize[sizeList.getSelectedIndex()]);  
                //sizeText.requestFocus();  
                sizeText.selectAll();     
                Font sampleFont3=new Font(fontText.getText(),style[styleList.getSelectedIndex()],Integer.parseInt(sizeText.getText()));  
                sample.setFont(sampleFont3);  
            }  
        });  
        okButton.addActionListener(new ActionListener()  
        {   public void actionPerformed(ActionEvent e)  
            {   Font okFont=new Font(fontText.getText(),style[styleList.getSelectedIndex()],Integer.parseInt(sizeText.getText()));  
                textArea.setFont(okFont);  
                fontDialog.dispose();  
            }  
        });  
        JPanel samplePanel=new JPanel();  
        samplePanel.setBorder(BorderFactory.createTitledBorder("示例"));  
        samplePanel.add(sample);  
        JPanel panel1=new JPanel();  
        JPanel panel2=new JPanel();  
        JPanel panel3=new JPanel();  
        panel2.add(fontText);  
        panel2.add(styleText);  
        panel2.add(sizeText);  
        panel2.add(okButton);  
        panel3.add(new JScrollPane(fontList));
        panel3.add(new JScrollPane(styleList));  
        panel3.add(new JScrollPane(sizeList));  
        panel3.add(cancel);  
        con.add(panel1);  
        con.add(panel2);  
        con.add(panel3);  
        con.add(samplePanel);  
        fontDialog.setSize(350,340); 
        Point topLeft = frame.getLocationOnScreen();
        Dimension parentSize = frame.getSize();
        Dimension thisSize = fontDialog.getSize();
        int x, y;
        x = ((parentSize.width - thisSize.width) / 2) + topLeft.x;
        y = ((parentSize.height - thisSize.height) / 2) + topLeft.y;
        fontDialog.setLocation(x, y);
        fontDialog.setResizable(false);  
        fontDialog.setVisible(true);  
    } 
    public void donateshow(){
    	donatelabel1=new JLabel();
		donatelabel1=donateseticon(donatelabel1, "img/donate1.png", 300, 420);
		donatelabel1.setSize(300, 420);
		donatelabel1.setLocation(74, 30);
		
		donatelabel2=new JLabel();
		donatelabel2=donateseticon(donatelabel2, "img/donate2.png", 300, 420);
		donatelabel2.setSize(300, 420);
		donatelabel2.setLocation(420, 30);
		
		
		donatelabel3=new JLabel();
		donatelabel3=donateseticon(donatelabel3, "img/love.png", 64, 54);
		donatelabel3.setSize(64, 54);
		donatelabel3.setLocation(46, 469);
		
		dialog = new JDialog(frame,"捐赠"); // 定义一个JDialog对话框
		dialog.getContentPane().add(donatelabel1);
		dialog.getContentPane().add(donatelabel2);
		dialog.getContentPane().add(donatelabel3);
		dialog.setSize(800, 600);                             // 设置对话框大小
		dialog.setLocation(350, 250);                        // 设置对话框位置
		dialog.getContentPane().setLayout(null);                 // 设置布局管理器
		donatelabel4 = new JLabel("您的支持是我进步的无限动力！谢谢。");
		donatelabel4.setFont(new Font("隶书", Font.BOLD, 36));
		donatelabel4.setBounds(108, 452, 670, 103);
		dialog.getContentPane().add(donatelabel4);
		dialog.setVisible(true);
		dialog.setLocationRelativeTo(null);
    }
	public static JLabel donateseticon(JLabel j,String img,int width,int height){
		ImageIcon Img_new=new ImageIcon(SimpleNotepad.class.getResource(img));
		Img_new=new ImageIcon(Img_new.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
		j=new JLabel(Img_new);
		return j;
	}
}
