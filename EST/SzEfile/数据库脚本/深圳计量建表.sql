/*电量数据表*/
create table EST_DDY_SzdlData
(
  id               NUMBER(19) not null,
  jldbh            NUMBER(19) not null,
  yhbh             varchar2(50),
  jldh             varchar2(50),
  dbzcbh           varchar2(50),
  areacode  NUMBER(10) not null,
  datatime  DATE,  
  ct         NUMBER(5),
  pt         NUMBER(5),
  updateTime Date,
  pap_r      NUMBER(11,4), 
  prp_r      NUMBER(11,4),
  rap_r      NUMBER(11,4),
  rrp_r      NUMBER(11,4) 
);

--index
CREATE INDEX idx_dldata_local ON EST_DDY_SzdlData(jldbh,datatime,areacode); 

 -- Create sequence 
create sequence EST_DDY_SzdlData_seq
minvalue 1
maxvalue 999999999999999999999999999
start with 1000
increment by 1
cache 20;



CREATE TABLE EST_DDY_SZHISDATA
(
  ID        NUMBER(19) NOT NULL, 
  jldbh            NUMBER(19) not null,
  yhbh             varchar2(50),
  jldh             varchar2(50),
  dbzcbh           varchar2(50),
  
	DATATIME  TIMESTAMP NOT NULL,
	IA        NUMBER(12,5),
	IB        NUMBER(12,5),
	IC        NUMBER(12,5),
	UA        NUMBER(12,5),
	UB        NUMBER(12,5),
	UC        NUMBER(12,5),
	Q         NUMBER(12,5),
	QA        NUMBER(12,5),
	QB        NUMBER(12,5),
	QC        NUMBER(12,5),
	P         NUMBER(12,5),
	PA        NUMBER(12,5),
	PB        NUMBER(12,5),
	PC        NUMBER(12,5),
  S         NUMBER(12,5),
	SA        NUMBER(12,5),
	SB        NUMBER(12,5),
	SC        NUMBER(12,5),
	PF        NUMBER(12,5),
	PFA       NUMBER(12,5),
	PFB       NUMBER(12,5),
	PFC       NUMBER(12,5),
	PT        NUMBER(10,2),
	CT        NUMBER(10,2),
	AREACODE  NUMBER(10) NOT NULL
)
partition by range (DATATIME)
(
  partition PAR20160301 values less than (TIMESTAMP'2016-03-02 00:00:00')
)
;


--index
CREATE INDEX idx_hisdata_local ON EST_DDY_SZHISDATA(jldbh,datatime,areacode) local; 

ALTER TABLE EST_DDY_SZHISDATA
	ADD CONSTRAINT UQ_EST_DDY_SZHISDATA UNIQUE (jldbh, DATATIME)
;

create sequence EST_DDY_SZHISDATA_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1000
increment by 1
cache 20;
