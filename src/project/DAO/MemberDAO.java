package project.DAO;

import java.sql.*;
import java.text.*;
import java.util.*;
import project.DBconn.DBconn;
import project.VO.MemberVO;

public class MemberDAO {
	private Statement stmt;
	private PreparedStatement pstmt, pstmt2;
	private ResultSet rs;
	private boolean result;
	private String query, query2;
	private MemberVO mvo;
	SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
	
	public MemberVO login(String memId, String memPw) {	//vo 데이터를 반환받을 예정임. 매개변수의 자료형과 반환받는 자료형이 일치해야함.
		mvo = new MemberVO();	//(확실X) main에서 저장한 mvo값으로 검증
		query = "SELECT * FROM t_member WHERE mem_id = '" + memId + "'";
		try {
			stmt = DBconn.getConnection().createStatement();	//커넥션할당 : driverManager.getConnection() 쿼리문전송:createStatement()
			rs = stmt.executeQuery(query);	//select구문이라서  rs임. 쿼리문 실행 결과가 rs에 담김.
			if (rs.next()) {	//존재하고
				if(memPw.equals(rs.getString("mem_pw"))) {	//비밀번호가 일치하면 유저의 모든 정보 리턴
					mvo.setMemId(rs.getString("mem_id"));
					mvo.setMemPw(rs.getString("mem_pw"));
					mvo.setMemName(rs.getString("mem_name"));
					mvo.setMemBirthday(rs.getDate("mem_birthday"));
					mvo.setMemEmail(rs.getString("mem_email"));
					mvo.setMemLike(rs.getInt("mem_like"));
					mvo.setMemPoint(rs.getInt("mem_point"));
					mvo.setMemPntCnt(rs.getInt("mem_pnt_cnt"));
					mvo.setMemBdPntTf(rs.getInt("mem_pnt_bd_tf"));
					mvo.setMemCtPntTf(rs.getInt("mem_pnt_ct_tf"));
					
						if(rs.getString("mem_pnt_ct_enddate") != null) {	//현재 제한걸려있음
							mvo.setMemCtPntEnddate(rs.getDate("mem_pnt_ct_enddate"));
						}
						if(rs.getString("mem_pnt_bd_enddate") != null) {
							mvo.setMemBdPntEnddate(rs.getDate("mem_pnt_bd_enddate"));
						}	
						
					} else mvo = null;	//비밀번호가 일치하지 않으면 null
				} else {		//존재하지 않으면 null 리턴
					mvo = null;
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBconn.close(stmt);
			}	//end try()
		//페널티 풀기
		unpenalty();	//unpenalty 메서드로 이동	
		return mvo;	//검증 끝난 mvo return해줌
	}//end login
	
	public boolean join(MemberVO mvo) {
		query = "INSERT INTO t_member (mem_id, mem_pw, mem_name, mem_birthday, mem_email) VALUES(?, ?, ?, ?, ?)";	//select문이 아니라서 rs 안썼음
		try {
			pstmt = DBconn.getConnection().prepareStatement(query);
			pstmt.setString(1, mvo.getMemId());
			pstmt.setString(2, mvo.getMemPw());
			pstmt.setString(3, mvo.getMemName());
			pstmt.setString(4, new SimpleDateFormat("yyMMdd").format(mvo.getMemBirthday()));
			pstmt.setString(5, mvo.getMemEmail());
			if (pstmt.executeUpdate() == 1) {
				result = true;
				} else {
				result = false;
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { DBconn.close(pstmt);}
		return result;
	}
	
	//회원가입 시 아이디, 이메일 중복확인	
	//iOrE가 1이면 아이디, 2면 이메일 중복 확인
	public boolean isExist(String idEmail, int iOrE) {
		boolean result = false; //아이디 또는 이메일 존재 할 시  false, 사용 가능 할 시 true
		if(iOrE == 1) {	//아이디 중복 확인
			query = "SELECT * FROM t_member WHERE mem_id = '" + idEmail + "'";
			try {
				stmt = DBconn.getConnection().createStatement();
				rs = stmt.executeQuery(query);
				if (rs.next()) {	//존재하면 false 리턴
					result = false;
					} else {		//없으면 true 리턴
						result = true;
					}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBconn.close(stmt);
				}
			
		} else if(iOrE == 2) { // 이메일 중복 확인
			query = "SELECT * FROM t_member WHERE mem_email = '" + idEmail + "'";
			try {
				stmt = DBconn.getConnection().createStatement();
				rs = stmt.executeQuery(query);
				if (rs.next()) {	//존재하면 false 리턴
					result = false;
					} else {		//없으면 true 리턴
						result = true;
					}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBconn.close(stmt);
				}
		}
		return result;
	}
	
	//계정관리에서 사용 (특정 페이지의 일반 사용자 리스트)
	public List<MemberVO> getNormalMem(int page) {
		List<MemberVO> list = new ArrayList<MemberVO>();
		query = "SELECT mem_id FROM(SELECT ROWNUM num, mem_id FROM (SELECT * FROM t_member ORDER BY mem_id) WHERE mem_pnt_bd_tf = 0 AND mem_pnt_ct_tf = 0) WHERE num/10 >"+(page-1)+" AND num/10 <="+page;	
		//regexp_like : Oracle의 정규식 함수 중 하나. Like처럼 특정 패턴과 매칭되는 경과를 검색하는 함수로 정규식을 이용하여 특정 패턴을 찾음
		try {
			stmt = DBconn.getConnection().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				MemberVO mvo = new MemberVO();
				mvo.setMemId(rs.getString("mem_id"));
				list.add(mvo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {DBconn.close(stmt, rs); }
		return list;
	}
	
	//계정관리에서 사용 (특정 페이지의 제한 사용자 리스트)
	public List<MemberVO> getPntMem(int page) {
		List<MemberVO> list = new ArrayList<MemberVO>();
		query = "SELECT mem_id FROM(SELECT ROWNUM num, mem_id FROM (SELECT * FROM t_member ORDER BY mem_id) WHERE mem_pnt_bd_tf != 0 OR mem_pnt_ct_tf != 0) WHERE num/10 >"+(page-1)+" AND num/10 <="+page;
		try {
			stmt = DBconn.getConnection().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				MemberVO mvo = new MemberVO();
				mvo.setMemId(rs.getString("mem_id"));
				list.add(mvo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {DBconn.close(stmt, rs); }
		return list;
	}
	
	//계정관리에서 사용 (일반 사용자 리스트 검색)
	public List<MemberVO> getNormalMem(String search, int page) {
		List<MemberVO> list = new ArrayList<MemberVO>();
		query = "SELECT mem_id FROM(SELECT ROWNUM num, mem_id FROM (SELECT * FROM t_member WHERE regexp_like(mem_id,'"+search+"') ORDER BY mem_id) WHERE mem_pnt_bd_tf = 0 AND mem_pnt_ct_tf = 0) WHERE num/10 >"+(page-1)+" AND num/10 <="+page;
		
		try {
			stmt = DBconn.getConnection().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				MemberVO mvo = new MemberVO();
				mvo.setMemId(rs.getString("mem_id"));
				list.add(mvo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {DBconn.close(stmt, rs); }
		return list;
	}
	
	//계정관리에서 사용 (제한 사용자 리스트 검색)
	public List<MemberVO> getPntMem(String search, int page) {
		List<MemberVO> list = new ArrayList<MemberVO>();
		query = "SELECT mem_id FROM(SELECT ROWNUM num, mem_id FROM (SELECT * FROM t_member WHERE regexp_like(mem_id,'"+search+"') ORDER BY mem_id) WHERE mem_pnt_bd_tf != 0 OR mem_pnt_ct_tf != 0) WHERE num/10 >"+(page-1)+" AND num/10 <="+page;
		try {
			stmt = DBconn.getConnection().createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				MemberVO mvo = new MemberVO();
				mvo.setMemId(rs.getString("mem_id"));
				list.add(mvo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {DBconn.close(stmt, rs); }
		return list;
	}

	public MemberVO getOne(String userId) {
		query = "SELECT * FROM t_member WHERE mem_id = '" + userId + "'";
		MemberVO mvo = null;
		try {
			stmt = DBconn.getConnection().createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {	//rs는 커서객체처럼 사용해야함
				mvo = new MemberVO();
				mvo.setMemId(rs.getString("mem_id"));
				mvo.setMemPw(rs.getString("mem_pw"));
				mvo.setMemName(rs.getString("mem_name"));				
				mvo.setMemEmail(rs.getString("mem_email"));
				mvo.setMemLike(rs.getInt("mem_like"));
				mvo.setMemPoint(rs.getInt("mem_point"));
				mvo.setMemPntCnt(rs.getInt("mem_pnt_cnt"));
				mvo.setMemBdPntTf(rs.getInt("mem_pnt_bd_tf"));
				mvo.setMemCtPntTf(rs.getInt("mem_pnt_ct_tf"));
				if(rs.getDate("mem_birthday") != null) {
					mvo.setMemBirthday(rs.getDate("mem_birthday"));
				}
				if(rs.getDate("mem_pnt_ct_enddate") != null) {
					mvo.setMemCtPntEnddate(rs.getDate("mem_pnt_ct_enddate"));
				}
				if(rs.getDate("mem_pnt_bd_enddate") != null) {
					mvo.setMemBdPntEnddate(rs.getDate("mem_pnt_bd_enddate"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {DBconn.close(stmt);}
		return mvo;
	}	
		
	//계정정보관리에서 사용
	public boolean modify(MemberVO mvo) {
		query = "UPDATE t_member SET mem_pw = ?, mem_name = ?, mem_birthday = ?, mem_email = ? WHERE mem_id = ?";
		try {
			pstmt = DBconn.getConnection().prepareStatement(query);
			pstmt.setString(1, mvo.getMemPw());
			pstmt.setString(2, mvo.getMemName());
			pstmt.setString(3, new SimpleDateFormat("yyMMdd").format(mvo.getMemBirthday()));
			pstmt.setString(4, mvo.getMemEmail());
			pstmt.setString(5, mvo.getMemId());

			if (pstmt.executeUpdate() == 1) {
				result = true;
				} else {
					result = false;
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { DBconn.close(pstmt);}
		return result;
	}
	//계정정보관리에서 사용
	public boolean delete(String userId) {
		try {
			stmt = DBconn.getConnection().createStatement();
			query = "DELETE t_member WHERE mem_id = '"+ userId + "'";
			if (stmt.executeUpdate(query) == 1) {
				result = true;
				} else {
					result = false;
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { DBconn.close(stmt);}
		
		return result;
	}
	//계정정보관리에서 사용
	//매개변수로 아이디, 1또는2 받아서 1이면 게시판, 2면 채팅 제한...
	//이런식으로 코딩하면 좋을듯
	public boolean penalty(String userId, int whichOne) {
		query2 = "SELECT mem_pnt_cnt FROM t_member WHERE mem_id = ?";	//pntcnt가 4 이상이면 해당 계정delete 하기
		if(whichOne == 1) {	//1이면 게시판제한
			query = "UPDATE t_member SET mem_pnt_bd_tf = 1, mem_pnt_bd_enddate = (SYSDATE+30), mem_pnt_cnt = mem_pnt_cnt+1 WHERE mem_id = ?";
		} else if(whichOne == 2) {	//2면 채팅제한
			query = "UPDATE t_member SET mem_pnt_ct_tf = 1, mem_pnt_ct_enddate = (SYSDATE+30), mem_pnt_cnt = mem_pnt_cnt+1 WHERE mem_id = ?";
		}
		try {
			pstmt = DBconn.getConnection().prepareStatement(query);
			pstmt.setString(1, userId);
			if (pstmt.executeUpdate() == 1) {
				result = true;
				} else {
					result = false;
				}
			pstmt2 = DBconn.getConnection().prepareStatement(query2);
			pstmt2.setString(1, userId);
			rs = pstmt2.executeQuery();
			if(rs.next()) {
				if(rs.getInt("mem_pnt_cnt") >= 4) {
					System.out.println("삭제시도");
					if(delete(userId)) {
					System.out.println("경고 4회 누적으로 계정이 삭제되었습니다.");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { DBconn.close(pstmt2, rs);}
		return result;
	}
	
	
	
	public void unpenalty() {	//페널티 풀어주기 (관리자 로그인 시 또는 사용자 로그인 시)
		query = "UPDATE t_member SET mem_pnt_bd_tf = 0, mem_pnt_bd_enddate = null WHERE mem_pnt_bd_enddate <= SYSDATE";
		query2 = "UPDATE t_member SET mem_pnt_ct_tf = 0, mem_pnt_ct_enddate = null WHERE mem_pnt_ct_enddate <= SYSDATE";
		try {//
			pstmt = DBconn.getConnection().prepareStatement(query);
			pstmt2 = DBconn.getConnection().prepareStatement(query2);
			pstmt.executeUpdate();
			pstmt2.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {DBconn.close(pstmt2);}
		
		
	}
	
	
	public int memLike (String memId) {

	      query = "SELECT mem_like FROM t_member WHERE mem_id = '" + memId + "'";
	      MemberVO mvo = new MemberVO();
	         try {
	            stmt = DBconn.getConnection().createStatement();
	            rs = stmt.executeQuery(query);
	            if(rs.next()) {
	               mvo.setMemLike(rs.getInt("mem_like"));
	            }
	         } catch (SQLException e) {
	            e.printStackTrace();
	         }
	         return mvo.getMemLike();

		}
	
	public boolean memLikeUpdate(String memId, int itemPrice) {
	      query = "UPDATE t_member SET mem_like = mem_like - ? WHERE mem_id = ?";
	      try {
	         pstmt = DBconn.getConnection().prepareStatement(query);
	         pstmt.setInt(1, itemPrice);
	         pstmt.setString(2, memId);

	         if (pstmt.executeUpdate() == 1) {
	            result = true;
	            } else {
	               result = false;
	            }
	      } catch (SQLException e) {
	         e.printStackTrace();
	      } finally { DBconn.close(pstmt);}
	      return result;
	   }
}