package project.publicMain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import project.DAO.MemberDAO;
import project.VO.MemberVO;
import project.admin.main.AdminMain;
import project.user.main.UserMain;

public class PublicMain {
	public static UserMain um;
	public static AdminMain am;
	
	final String REG_EXP_ID = "^[0-9a-zA-Z]{4,14}$";
	final String REG_EXP_BIRTHDAY = "^[0-9]{6}$";
	final String REG_EXP_EMAIL = "^([A-za-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";
	
	Scanner scan;
	private String memId;
	private String memPw;
	private String memName;
	private String memBirthday;
	private String memEmail;
	private MemberVO mvo;
	private MemberDAO mdao;
	
	PublicMain(){	//생성자
		mdao = new MemberDAO();	//MemberDAO에 새로 받은 값을 mdao에 저장
		scan = new Scanner(System.in);	//Scanner클래스의 객체 생성
		menu();	//메뉴 메소드 호출
	}
	
	public void menu() {
		System.out.println("------- JAVA 잡아! -------");
		System.out.println("로그인 또는 회원가입을 선택해주세요.");
		System.out.println();
		System.out.println("> 1. 로그인");
		System.out.println("> 2. 회원가입");
		while(true) {	//조건문이 '참'일 동안 괄호 안의 내용 반복적으로 수행
			System.out.print(">> 선택 : ");
			String input = scan.next();	//입력받은 값을 input에 집어 넣기
			if(input.equals("1")) {	//if문으로 input값 검증. String에서는 ==는 주소값 비교, equals는 1차적으로는 주소값, 2차적으로는 값(value)을 비교
				//로그인
				login();	//1과 일치하면 login 메서드 호출
				break;	//while문과 if문에서 나가고 login 메서드로 감
			}else if(input.equals("2")) {
				//회원가입
				join();	//2와 일치하면 join 메서드 호출
				break;	//while문과 if문에서 나가고 join 메서드로 감
			}else System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");		//1도 2도 아닌 값을 입력한 경우 출력하는 메시지
		}
	}	//end menu()
	
	//login()과 join()은 따로라서 mvo = new MemberVO(); 두번함
	public void login() {
		mvo = new MemberVO();	//MemberVO에 새로 받은 값을 mvo에 저장할 것임
		System.out.println("> 로그인");
		while(true) {	//조건문이 '참'일 동안 괄호 안의 내용 반복적으로 수행			
			System.out.println("> 아이디와 비밀번호를 입력해주세요.");
			
			System.out.print("> 아이디 : ");			
			memId = scan.next();	//입력받은 값은 memId 변수에 담김
			System.out.println(memId);
			
			System.out.println("> 비밀번호 : ");
			memPw = scan.next(); //입력받은 값은 memPw 변수에 담김
			
				mvo = mdao.login(memId, memPw);	//mdao의 login 메서드의 매개변수로 입력받았던 값을 반환
				if(mvo == null) {	//반환값이 null이면 존재하지 않는 아이디 또는 비밀번호
					System.out.println("아이디 또는 비밀번호가 잘못되었습니다. 다시 입력해주세요.");
				}else {	//반환값이 존재한다면 관리자와 사용자 화면으로 각각 이동
					if(memId.equals("admin")) {
						am = new AdminMain();
					} else {
						//um = new UserMain(mvo); 이거 주석 없애고 UserMain 생성자 매개변수도 MemberVO 해줘야함@@@@@@@@@@@@@@@@@@@@@@@@@@@@
						um = new UserMain(mvo);
					}
					break;
				}
		}
	}
	
	public void join() {
		mvo = new MemberVO();	//MemberVO에 새로 받은 값을 mvo에 저장할 것임
		System.out.println("> 회원가입");
		System.out.println("> 다음 정보들을 입력해 주세요.");		
		while(true) {	//조건문이 '참'일 동안 괄호 안의 내용 반복적으로 수행
			System.out.print("> 아이디(영문, 숫자만 가능 / 4-14자) : ");
			memId = scan.next();	//입력받은 값은 memId 변수에 담김
			if(!Pattern.matches(REG_EXP_ID, memId)) {	//정규식 검증  (정규식과 입력받은 memId를 비교)
				System.out.println("영문, 숫자를 이용하여 4-14자로 맞춰서 다시 입력해주세요.");
			} else {
				if(mdao.isExist(memId, 1)) { //true면 사용 가능
					mvo.setMemId(memId);	//mvo에 받은 id값 그대로 저장됨
					break;
				} else {	//false면 중복
					System.out.println("이미 존재하는 아이디입니다. 다시 입력해주세요.");
				}
			}	//end if()
		}	//end while()
		
		while(true) {
			System.out.print("> 비밀번호(4-14자) : ");
			memPw = scan.next();
			if(!(memPw.length()>=4 && memPw.length()<=14)) {	//길이가 4~14 사이어야 함.
				System.out.println("4-14자로 맞춰서 다시 입력해주세요.");
			} else {
				mvo.setMemPw(memPw);	////mvo에 받은 pw값 그대로 저장됨
				break;
			}	//end if()
		}	//end while()
		
		while(true) {
			System.out.print("> 이름(2-5자) : ");
			memName = scan.next();
			if(!(memName.length()>=2 && memName.length()<=5)) {	//길이가 2~5 사이어야 함.
				System.out.println("2-5자로 맞춰서 다시 입력해주세요.");
			} else {
				mvo.setMemName(memName);
				break;
			}
		}

		while(true) {
			System.out.print("> 생년월일(yymmdd) : ");
			memBirthday = scan.next();
			if(!Pattern.matches(REG_EXP_BIRTHDAY, memBirthday)) {	//정규식 검증  (정규식과 입력받은 memBirthday를 비교)
				System.out.println("숫자와 형식을 맞춰서 다시 입력해주세요.");
			} else {
					try {
						mvo.setMemBirthday(new SimpleDateFormat("yyMMdd").parse(memBirthday));	//앞에 날짜형식 지정해 주고 날짜 parse해서 저장 
					} catch (ParseException e) {
						e.printStackTrace();
					}					
				break;
			}
		}

		while(true) {
			System.out.print("> 이메일(아이디@도메인) : ");
			memEmail = scan.next();
			if(!Pattern.matches(REG_EXP_EMAIL, memEmail)) {	//정규식 검증  (정규식과 입력받은 memEmail을 비교)
				System.out.println("이메일 형식이 틀립니다. 다시 입력해주세요.");
			} else {
				if(mdao.isExist(memEmail, 2)) { //true면 사용 가능
					mvo.setMemEmail(memEmail);
					break;					
				} else {	//false면 중복
					System.out.println("이미 존재하는 이메일입니다. 다시 입력해주세요.");
				}
			}
		}
		
		boolean complete = mdao.join(mvo);	//위의 내용들
		if(complete) {
			System.out.println("회원가입이 완료되었습니다.");menu();	//완료 했으므로 menu() 메서드 호출
		} else {
			System.out.println("회원가입이 실패했습니다.");
		}
		
	}	//end join()
	

	public static void main(String[] args) {
		new PublicMain();
	}

	
}
