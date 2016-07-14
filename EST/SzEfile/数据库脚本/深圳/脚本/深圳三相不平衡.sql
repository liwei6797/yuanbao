-- Create table
create table EST_IC_UNBALANCE_TMP
(
  stdate     TIMESTAMP(6),
  dtmpid     VARCHAR2(50),
  datatime   TIMESTAMP(6),
  ia         NUMBER(10,3),
  ib         NUMBER(10,3),
  ic         NUMBER(10,3),
  unbalance  NUMBER(10,5),
  avgia      NUMBER(10,5),
  avgib      NUMBER(10,5),
  avgic      NUMBER(10,5),
  phasecount NUMBER(1),
  areacode   NUMBER(10)
); 
 
 --index
CREATE INDEX idx_hisdata_P_jldh ON EST_DDY_SZHISDATA_P(jldh); 
CREATE INDEX idx_hisdata_P_local2 ON EST_DDY_SZHISDATA_P(jldh,datatime) local; 
 
begin
  for i in 0 .. 16 loop
    INSERT INTO EST_IC_UNBALANCE_TMP
      (STDATE,
       Dtmpid,
       DATATIME,
       IA,
       IB,
       IC,
       UNBALANCE,
       AVGIA,
       AVGIB,
       AVGIC,
       PHASECOUNT,
       AREACODE)
      SELECT TO_DATE('2016-05-15', 'YYYY-MM-DD') + i STDATE,
             DATA.JLDH,
             DATA.DATATIME,
             ABS(DATA.IA) IA,
             ABS(DATA.IB) IB,
             ABS(DATA.IC) IC,
             (GREATEST(ABS(DATA.IA), ABS(DATA.IB), ABS(DATA.IC)) -
             LEAST(ABS(DATA.IA), ABS(DATA.IB), ABS(DATA.IC))) /
             GREATEST(ABS(DATA.IA), ABS(DATA.IB), ABS(DATA.IC)) UNBALANCE,
             AVGIA,
             AVGIB,
             AVGIC,
             CASE
               WHEN GREATEST(NVL(AVGIA, 0), NVL(AVGIB, 0), NVL(AVGIC, 0)) / AVGIA < 10 THEN
                1
               ELSE
                0
             END + CASE
               WHEN GREATEST(NVL(AVGIA, 0), NVL(AVGIB, 0), NVL(AVGIC, 0)) / AVGIB < 10 THEN
                1
               ELSE
                0
             END + CASE
               WHEN GREATEST(NVL(AVGIA, 0), NVL(AVGIB, 0), NVL(AVGIC, 0)) / AVGIC < 10 THEN
                1
               ELSE
                0
             END PHASECOUNT,
             100 AREACODE
        FROM EST_DDY_SZHISDATA_I DATA
       INNER JOIN MID_POINT3 MP1
          ON DATA.JLDH = MP1.JLDH
        LEFT JOIN (SELECT MP.JLDH,
                          AVG(CASE
                                WHEN ABS(IA) <= 0.1 THEN
                                 NULL
                                ELSE
                                 ABS(IA)
                              END) AVGIA,
                          AVG(CASE
                                WHEN ABS(IB) <= 0.1 THEN
                                 NULL
                                ELSE
                                 ABS(IB)
                              END) AVGIB,
                          AVG(CASE
                                WHEN ABS(IC) <= 0.1 THEN
                                 NULL
                                ELSE
                                 ABS(IC)
                              END) AVGIC
                     FROM EST_DDY_SZHISDATA_I T, MID_POINT3 MP
                    WHERE T.JLDH = MP.JLDH
                         
                      AND T.DATATIME BETWEEN
                          TO_DATE('2016-05-15', 'YYYY-MM-DD') + i AND
                          TO_DATE('2016-05-15' || ' 23:59:59',
                                  'YYYY-MM-DD HH24:MI:SS') + i
                    GROUP BY MP.JLDH) AVGI
          ON DATA.JLDH = AVGI.JLDH
       WHERE DATA.DATATIME BETWEEN TO_DATE('2016-05-15', 'YYYY-MM-DD') + i AND
             TO_DATE('2016-05-15' || ' 23:59:59', 'YYYY-MM-DD HH24:MI:SS') + i
         AND ABS(IA) / AVGIA BETWEEN 0.1 AND 10
         AND ABS(IB) / AVGIB BETWEEN 0.1 AND 10
         AND ABS(IC) / AVGIC BETWEEN 0.1 AND 10;
     commit;
  end loop;
end;