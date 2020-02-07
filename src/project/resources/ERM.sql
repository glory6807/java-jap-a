
/* Drop Tables */

DROP TABLE t_complboard_comt CASCADE CONSTRAINTS;
DROP TABLE t_complboard CASCADE CONSTRAINTS;
DROP TABLE t_useritem CASCADE CONSTRAINTS;
DROP TABLE t_item CASCADE CONSTRAINTS;
DROP TABLE t_message CASCADE CONSTRAINTS;
DROP TABLE t_qnaboard_comt CASCADE CONSTRAINTS;
DROP TABLE t_qnaboard CASCADE CONSTRAINTS;
DROP TABLE t_member CASCADE CONSTRAINTS;




/* Create Tables */

CREATE TABLE t_complboard
(
	compl_no number NOT NULL,
	compl_type number(1),
	compl_title varchar2(100),
	compl_content varchar2(4000),
	compl_id varchar2(14) NOT NULL,
	compl_date date,
	compl_viewcnt number,
	compl_like number,
	-- 건의게시판 비밀글 여부
	compl_is_secret number(1),
	PRIMARY KEY (compl_no)
);


CREATE TABLE t_complboard_comt
(
	compl_no number NOT NULL,
	compl_comt_no number NOT NULL,
	mem_id varchar2(14) NOT NULL,
	compl_comt_content varchar2(4000),
	compl_comt_date date,
	compl_comt_like number,
	PRIMARY KEY (compl_comt_no)
);


CREATE TABLE t_item
(
	item_no number NOT NULL,
	item_type varchar2(50),
	item_name varchar2(50),
	item_price number,
	PRIMARY KEY (item_no)
);


CREATE TABLE t_member
(
	mem_id varchar2(14) NOT NULL,
	mem_pw varchar2(14),
	mem_name varchar2(10),
	mem_birthday date,
	mem_email varchar2(50),
	mem_like number,
	mem_point number,
	mem_pnt_cnt number(1),
	mem_pnt_enddate date,
	mem_pnt_bd_tf number(1),
	mem_pnt_ct_tf number(1),
	PRIMARY KEY (mem_id)
);


CREATE TABLE t_message
(
	message_no number NOT NULL,
	mem_receive_id varchar2(14) NOT NULL,
	mem_send_id varchar2(14) NOT NULL,
	message_content varchar2(4000),
	message_send_date date,
	message_check_tf number(1),
	PRIMARY KEY (message_no)
);


CREATE TABLE t_qnaboard
(
	qna_no number NOT NULL,
	-- 공지사항/일반 게시글
	qna_type number(1),
	qna_title varchar2(100),
	qna_content varchar2(4000),
	qna_date date,
	qna_viewcnt number,
	qna_like number,
	qna_id varchar2(14) NOT NULL,
	PRIMARY KEY (qna_no)
);


CREATE TABLE t_qnaboard_comt
(
	qna_no number NOT NULL,
	qna_comt_no number NOT NULL,
	mem_id varchar2(14) NOT NULL,
	qna_comt_content varchar2(4000),
	qna_comt_date date,
	qna_comt_like number,
	PRIMARY KEY (qna_comt_no)
);


CREATE TABLE t_useritem
(
	mem_id varchar2(14) NOT NULL,
	item_no number NOT NULL
);



/* Create Foreign Keys */

ALTER TABLE t_complboard_comt
	ADD FOREIGN KEY (compl_no)
	REFERENCES t_complboard (compl_no)
;


ALTER TABLE t_useritem
	ADD FOREIGN KEY (item_no)
	REFERENCES t_item (item_no)
;


ALTER TABLE t_complboard
	ADD FOREIGN KEY (compl_id)
	REFERENCES t_member (mem_id)
;


ALTER TABLE t_complboard_comt
	ADD FOREIGN KEY (mem_id)
	REFERENCES t_member (mem_id)
;


ALTER TABLE t_message
	ADD FOREIGN KEY (mem_send_id)
	REFERENCES t_member (mem_id)
;


ALTER TABLE t_message
	ADD FOREIGN KEY (mem_receive_id)
	REFERENCES t_member (mem_id)
;


ALTER TABLE t_qnaboard
	ADD FOREIGN KEY (qna_id)
	REFERENCES t_member (mem_id)
;


ALTER TABLE t_qnaboard_comt
	ADD FOREIGN KEY (mem_id)
	REFERENCES t_member (mem_id)
;


ALTER TABLE t_useritem
	ADD FOREIGN KEY (mem_id)
	REFERENCES t_member (mem_id)
;


ALTER TABLE t_qnaboard_comt
	ADD FOREIGN KEY (qna_no)
	REFERENCES t_qnaboard (qna_no)
;



/* Comments */

COMMENT ON COLUMN t_complboard.compl_is_secret IS '건의게시판 비밀글 여부';
COMMENT ON COLUMN t_qnaboard.qna_type IS '공지사항/일반 게시글';



