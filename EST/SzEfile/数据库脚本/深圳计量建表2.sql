CREATE TABLE EST_DDY_SZHISDATA_Q
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
CREATE INDEX idx_hisdata_local_Q ON EST_DDY_SZHISDATA_Q(jldbh,datatime,areacode) local; 

CREATE TABLE EST_DDY_SZHISDATA_P
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
CREATE INDEX idx_hisdata_local_P ON EST_DDY_SZHISDATA_P(jldbh,datatime,areacode) local; 


CREATE TABLE EST_DDY_SZHISDATA_I
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
CREATE INDEX idx_hisdata_local_I ON EST_DDY_SZHISDATA_I(jldbh,datatime,areacode) local; 



CREATE TABLE EST_DDY_SZHISDATA_U
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
CREATE INDEX idx_hisdata_local_U ON EST_DDY_SZHISDATA_U(jldbh,datatime,areacode) local; 


execute p_est_core_adddaypar('EST_DDY_SZHISDATA_U');
execute p_est_core_adddaypar('EST_DDY_SZHISDATA_I');
execute p_est_core_adddaypar('EST_DDY_SZHISDATA_P');
execute p_est_core_adddaypar('EST_DDY_SZHISDATA_Q');