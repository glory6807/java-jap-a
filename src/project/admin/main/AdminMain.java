package project.admin.main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import project.VO.MemberVO;
import project.admin.complBoardMng.ComplBdMng;
import project.admin.complBoardMng.ComplPost;
import project.admin.itemManager.ItemManager;
import project.admin.memberInfo.MemberInfo;
import project.admin.qnaBoardMng.QnaBdMng;
import project.publicMain.PublicMain;
import project.user.chat.MultiClient;
import project.user.complBoard.UserComplBoard;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;

//위계 : 프레임(창)-최상위패널(contentPane:버튼과 아이디, 로고가있음)-기능(게시판 등)패널
public class AdminMain extends JFrame {
	public static MemberVO mvo;
	
	public JPanel functionPanel;
	JPanel contentPane; // 최상위 패널	
	JPanel memberInfoPane; // 계정관리 JPanel
	JPanel qnaBoardPane; // 질답게시판 관리 JPanel;
	JPanel complBoardPane;// 건의 게시판 관리 JPanel
	JPanel ItemPane; // 아이템관리 JPanel	
	// 버튼에 따라 바뀌는부분
	
	public AdminMain() { 
		mvo=new MemberVO();
		mvo.setMemId("admin");	//왼쪽 상단에 "admin" 이라고 표출하기 위해서 vo에서 받아옴
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//창 크기 조절을 위한 화면 해상도 정보 얻기
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		  System.out.println("해상도 : " + res.width + " x " + res.height);
		
		//프레임에 최상위패널 추가 (java잡아 로고와 버튼들이 출력될 패널임)
		contentPane = new JPanel();
		setContentPane(contentPane);
		getContentPane().setLayout(null);	//레이아웃을 뭘로 할지 모를 때, 사용자가 원하는 위치에		
		
		//창은 화면 해상도 기준. 최상위패널도 같음. 앞에 두 개는 화면상 출력물 위치(x좌표, y좌표)이고 뒤의 두 개는 출력물 크기(가로, 세로)
		setBounds(res.width/8,0,(res.width*3)/4,(res.height*15)/16); //프레임(창)크기
		//최상위패널 크기조절
		getContentPane().setBounds(res.width/8,0,(res.width*3)/4,(res.height*15)/16);
		
		//자바잡아 로고
		JLabel programNameLabel = new JLabel("Java 잡아 !");
		programNameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 50));
		programNameLabel.setBounds(594,12,352,86);
		contentPane.add(programNameLabel);		
		
		//게시판 노출 외 부분 패널...계속 바뀜
		functionPanel=new JPanel();
		functionPanel.setBounds(14, 159, 1394, 794);
		functionPanel.setLayout(null);
		contentPane.add(functionPanel);
		
		//로그인 하고 메인 진입시 Jpanel에 노출되는 화면(클래스)
		ComplBdMng cbm=new ComplBdMng();
		functionPanel.removeAll();
		functionPanel.add(cbm.getJPanel());
		functionPanel.revalidate();
		functionPanel.repaint();	
		
		//vo에서 받아온 "admin"이라는 아이디 표출
		JLabel userNameLabel = new JLabel(mvo.getMemId()+"님!");
		userNameLabel.setFont(new Font("굴림", Font.PLAIN, 26));
		userNameLabel.setBounds(41, 105, 181, 42);
		contentPane.add(userNameLabel);
		
		//##############버튼에 따라 바뀔 부분##################
		JButton userAdminBtn = new JButton("계정관리");
		userAdminBtn.setBounds(604, 94, 147, 53);
		contentPane.add(userAdminBtn);
		
		userAdminBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MemberInfo mi=new MemberInfo();
				functionPanel.removeAll();
				functionPanel.add(mi.getJPanel());
				functionPanel.revalidate();
				functionPanel.repaint();
			}
		});	
		
		JButton chattingAdminBtn= new JButton("채팅관리");
		chattingAdminBtn.setBounds(775, 94, 147, 53);
		contentPane.add(chattingAdminBtn);
		
		JButton qnaBoardAdminBtn = new JButton("질답게시판관리");
		qnaBoardAdminBtn.setBounds(939, 94, 147, 53);
		contentPane.add(qnaBoardAdminBtn);
		qnaBoardAdminBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QnaBdMng qbm=new QnaBdMng();
				functionPanel.removeAll();
				functionPanel.add(qbm.getJPanel());
				functionPanel.revalidate();
				functionPanel.repaint();
			}
		});
	
		JButton complBoardAdminBtn = new JButton("건의게시판관리");
		complBoardAdminBtn.setBounds(1100, 94, 147, 53);
		contentPane.add(complBoardAdminBtn);
		complBoardAdminBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ComplBdMng cbm=new ComplBdMng();
				functionPanel.removeAll();
				functionPanel.add(cbm.getJPanel());
				functionPanel.revalidate();
				functionPanel.repaint();
			}
		});
		
		JButton itemStoreAdminBtn = new JButton("아이템상점관리");
		itemStoreAdminBtn.setBounds(1261, 94, 147, 53);
			contentPane.add(itemStoreAdminBtn);
			itemStoreAdminBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ItemManager im = new ItemManager();
					functionPanel.removeAll();
					functionPanel.add(im.getJPanel());
					functionPanel.revalidate();
					functionPanel.repaint();
				}
			});	
			
	
//		JButton itemStoreAdminBtn = new JButton("아이템상점관리");
//		itemStoreAdminBtn.addMouseListener(new MouseListener() {
//			
//			@Override
//			public void mouseReleased(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void mousePressed(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void mouseExited(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void mouseEntered(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				ItemManager im = new ItemManager();
//				PublicMain.am.changePanel(im.getJPanel());
//			}
//		});
//		
//			itemStoreAdminBtn.setBounds(1261, 94, 147, 53);
//			contentPane.add(itemStoreAdminBtn);
		setVisible(true);
	}
	
	public void changePanel(JPanel jp){	//버튼 눌러서 특정 클래스 호출 뒤 다른 클래스로 바꿀 때 사용
		functionPanel.removeAll();
		functionPanel.add(jp);
		functionPanel.revalidate();
		functionPanel.repaint();		
	}
	
//	public void drawMain() {
//		
//	}
//	public void setUserId(String id) {//MemberVO로 바꾸기
//		
//	}
	
//	public static void main(String[] args) {
//		new UserMain();
//	}
	
}
