package project.pane;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import project.VO.MemberVO;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CustomCmtPane {
	
	private JTextField textField;
	private JTextArea textArea;
	JPanel panel;
	
	public CustomCmtPane() {
		panel = new JPanel();
		panel.setBounds(14, 159, 1394, 794);
		panel.setLayout(null); 
		
		MemberVO mvo=new MemberVO();
		
		JLabel memIdLabel = new JLabel("아이디");
		memIdLabel.setBounds(51, 17, 62, 18);
		panel.add(memIdLabel); 
		
		JLabel dateLabel = new JLabel("일시");
		dateLabel.setBounds(546, 17, 94, 18);
		panel.add(dateLabel); 
		
		JButton likeBtn = new JButton("좋아요");
		likeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		
		
		likeBtn.setBounds(427, 13, 105, 27);
		panel.add(likeBtn);
		
		JButton deleteBtn = new JButton("삭제");
		deleteBtn.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		deleteBtn.setBounds(308, 13, 105, 27);
		panel.add(deleteBtn);
		
		JTextArea comtText = new JTextArea();
		comtText.setBounds(14, 46, 736, 90);
		comtText.setLineWrap(true);
		JScrollPane jScollPane = new JScrollPane(comtText,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				 ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScollPane.setBounds(14,46,644,106);
		panel.add(jScollPane);
		
	}
	
	
	
}