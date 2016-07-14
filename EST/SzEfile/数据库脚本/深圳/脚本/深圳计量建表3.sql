create table mid_customer as (select * from MK_MID_CUSTOMER t where (t.kind = 10 or kind = 11) and status=1);
create table mid_yhbh_jldh as select distinct d.yhbh,d.jldh from  est_ddy_szhisdata d;
create table mid_point3 as 
       select l.*,p.*,c.kind,c.voltage_code,c.agreement_capacity,c.actual_capacity, 0.0 as ct,0.0 as pt from mid_yhbh_jldh l 
       left join mk_mid_metering_point p on l.jldh = p.pk_src_object
       left join MID_customer c on l.yhbh = c.pk_src_object;
	   
delete from mid_point3 t where t.voltage_code is null; --删除非专变计量点

update mid_point3 set ct=10/5 where ct_ratio ='01';
update mid_point3 set ct=15/5 where ct_ratio ='02';
update mid_point3 set ct=20/5 where ct_ratio ='03';
update mid_point3 set ct=25/5 where ct_ratio ='04';
update mid_point3 set ct=30/5 where ct_ratio ='05';
update mid_point3 set ct=40/5 where ct_ratio ='06';
update mid_point3 set ct=50/5 where ct_ratio ='07';
update mid_point3 set ct=60/5 where ct_ratio ='08';
update mid_point3 set ct=70/5 where ct_ratio ='09';
update mid_point3 set ct=100/5 where ct_ratio ='10';
update mid_point3 set ct=150/5 where ct_ratio ='11';
update mid_point3 set ct=200/5 where ct_ratio ='12';
update mid_point3 set ct=250/5 where ct_ratio ='13';
update mid_point3 set ct=300/5 where ct_ratio ='14';
update mid_point3 set ct=400/5 where ct_ratio ='15';
update mid_point3 set ct=500/5 where ct_ratio ='16';
update mid_point3 set ct=600/5 where ct_ratio ='17';
update mid_point3 set ct=750/5 where ct_ratio ='18';
update mid_point3 set ct=800/5 where ct_ratio ='19';
update mid_point3 set ct=1000/5 where ct_ratio ='20';
update mid_point3 set ct=1200/5 where ct_ratio ='21';
update mid_point3 set ct=1250/5 where ct_ratio ='22';
update mid_point3 set ct=1500/5 where ct_ratio ='23';
update mid_point3 set ct=1600/5 where ct_ratio ='24';
update mid_point3 set ct=2000/5 where ct_ratio ='25';
update mid_point3 set ct=2500/5 where ct_ratio ='26';
update mid_point3 set ct=3000/5 where ct_ratio ='27';
update mid_point3 set ct=4000/5 where ct_ratio ='28';
update mid_point3 set ct=5000/5 where ct_ratio ='29';
update mid_point3 set ct=10/1 where ct_ratio ='30';
update mid_point3 set ct=15/1 where ct_ratio ='31';
update mid_point3 set ct=20/1 where ct_ratio ='32';
update mid_point3 set ct=25/1 where ct_ratio ='33';
update mid_point3 set ct=30/1 where ct_ratio ='34';
update mid_point3 set ct=40/1 where ct_ratio ='35';
update mid_point3 set ct=50/1 where ct_ratio ='36';
update mid_point3 set ct=60/1 where ct_ratio ='37';
update mid_point3 set ct=75/1 where ct_ratio ='38';
update mid_point3 set ct=100/1 where ct_ratio ='39';
update mid_point3 set ct=120/1 where ct_ratio ='40';
update mid_point3 set ct=150/1 where ct_ratio ='41';
update mid_point3 set ct=200/1 where ct_ratio ='42';
update mid_point3 set ct=250/1 where ct_ratio ='43';
update mid_point3 set ct=300/1 where ct_ratio ='44';
update mid_point3 set ct=400/1 where ct_ratio ='45';
update mid_point3 set ct=500/1 where ct_ratio ='46';
update mid_point3 set ct=600/1 where ct_ratio ='47';
update mid_point3 set ct=750/1 where ct_ratio ='48';
update mid_point3 set ct=800/1 where ct_ratio ='49';
update mid_point3 set ct=1000/1 where ct_ratio ='50';
update mid_point3 set ct=1200/1 where ct_ratio ='51';
update mid_point3 set ct=1250/1 where ct_ratio ='52';
update mid_point3 set ct=1500/1 where ct_ratio ='53';
update mid_point3 set ct=1600/1 where ct_ratio ='54';
update mid_point3 set ct=2000/1 where ct_ratio ='55';
update mid_point3 set ct=2500/1 where ct_ratio ='56';
update mid_point3 set ct=3000/1 where ct_ratio ='57';
update mid_point3 set ct=4000/1 where ct_ratio ='58';
update mid_point3 set ct=5000/1 where ct_ratio ='59';


update mid_point3 set pt=10000/100 where pt_ratio ='02';
update mid_point3 set pt=20000/100 where pt_ratio ='03';
update mid_point3 set pt=25750/100 where pt_ratio ='04';
update mid_point3 set pt=35000/100 where pt_ratio ='05';
update mid_point3 set pt=60000/100 where pt_ratio ='06';
update mid_point3 set pt=66000/100 where pt_ratio ='07';
update mid_point3 set pt=110000/100  where pt_ratio ='08';
update mid_point3 set pt=220000/100 where pt_ratio ='09';

update mid_point3 set pt=null where pt =0;
update mid_point3 set Ct=null where ct =0;

---------------------------------------------------------------------------------------------------------------
	
alter table mid_point3 add voltlevel varchar2(50);
   update mid_point3 t set t.voltlevel = '交流110V' where t.voltage_code = '01';
   update mid_point3 t set t.voltlevel = '交流220V' where t.voltage_code = '02';
   update mid_point3 t set t.voltlevel = '交流380V' where t.voltage_code = '03';
   update mid_point3 t set t.voltlevel = '交流660V' where t.voltage_code = '04';
   update mid_point3 t set t.voltlevel = '交流1000V' where t.voltage_code = '05';
   update mid_point3 t set t.voltlevel = '交流3kV' where t.voltage_code = '06';
   update mid_point3 t set t.voltlevel = '交流6kV' where t.voltage_code = '07';
   update mid_point3 t set t.voltlevel = '交流10kV' where t.voltage_code = '08';
   update mid_point3 t set t.voltlevel = '交流20kV' where t.voltage_code = '09';
   update mid_point3 t set t.voltlevel = '交流35kV' where t.voltage_code = '10';
   update mid_point3 t set t.voltlevel = '交流66kV' where t.voltage_code = '11';
   update mid_point3 t set t.voltlevel = '交流110kV' where t.voltage_code = '12';
   update mid_point3 t set t.voltlevel = '交流220kV' where t.voltage_code = '13';
   update mid_point3 t set t.voltlevel = '交流330kV' where t.voltage_code = '14';
   update mid_point3 t set t.voltlevel = '交流500kV' where t.voltage_code = '15';
   update mid_point3 t set t.voltlevel = '交流750kV' where t.voltage_code = '16';
   update mid_point3 t set t.voltlevel = '交流1000kV' where t.voltage_code = '17';
   update mid_point3 t set t.voltlevel = '交流5V' where t.voltage_code = '20';
   update mid_point3 t set t.voltlevel = '交流6V' where t.voltage_code = '21';
   update mid_point3 t set t.voltlevel = '交流12V' where t.voltage_code = '22';
   update mid_point3 t set t.voltlevel = '交流15V' where t.voltage_code = '23';
   update mid_point3 t set t.voltlevel = '交流24V' where t.voltage_code = '24';
   update mid_point3 t set t.voltlevel = '交流36V' where t.voltage_code = '25';
   update mid_point3 t set t.voltlevel = '交流42V' where t.voltage_code = '26';
   update mid_point3 t set t.voltlevel = '交流48V' where t.voltage_code = '27';
   update mid_point3 t set t.voltlevel = '交流60V' where t.voltage_code = '28';
   update mid_point3 t set t.voltlevel = '交流100V' where t.voltage_code = '29';
   update mid_point3 t set t.voltlevel = '交流127V' where t.voltage_code = '30';
   update mid_point3 t set t.voltlevel = '交流115V' where t.voltage_code = '31';
   update mid_point3 t set t.voltlevel = '交流230V' where t.voltage_code = '32';
   update mid_point3 t set t.voltlevel = '交流400V' where t.voltage_code = '33';
   update mid_point3 t set t.voltlevel = '交流690V' where t.voltage_code = '34';
   update mid_point3 t set t.voltlevel = '交流3150V' where t.voltage_code = '35';
   update mid_point3 t set t.voltlevel = '交流6300V' where t.voltage_code = '36';
   update mid_point3 t set t.voltlevel = '交流10.5kV' where t.voltage_code = '37';
   update mid_point3 t set t.voltlevel = '交流13.8kV' where t.voltage_code = '38';
   update mid_point3 t set t.voltlevel = '交流15.75kV' where t.voltage_code = '39';
   update mid_point3 t set t.voltlevel = '交流18kV' where t.voltage_code = '40';
   update mid_point3 t set t.voltlevel = '交流22kV' where t.voltage_code = '42';
   update mid_point3 t set t.voltlevel = '交流24kV' where t.voltage_code = '43';
   update mid_point3 t set t.voltlevel = '交流26kV' where t.voltage_code = '44';
   update mid_point3 t set t.voltlevel = '交流132kV' where t.voltage_code = '45';
   update mid_point3 t set t.voltlevel = '交流400kV' where t.voltage_code = '46';
   update mid_point3 t set t.voltlevel = '其它交流电压' where t.voltage_code = '49';
   update mid_point3 t set t.voltlevel = '直流24V' where t.voltage_code = '51';
   update mid_point3 t set t.voltlevel = '直流36V' where t.voltage_code = '52';
   update mid_point3 t set t.voltlevel = '直流48V' where t.voltage_code = '53';
   update mid_point3 t set t.voltlevel = '直流110V' where t.voltage_code = '54';
   update mid_point3 t set t.voltlevel = '直流220V' where t.voltage_code = '55';
   update mid_point3 t set t.voltlevel = '直流1.2V' where t.voltage_code = '56';
   update mid_point3 t set t.voltlevel = '直流1.5V' where t.voltage_code = '57';
   update mid_point3 t set t.voltlevel = '直流2V' where t.voltage_code = '58';
   update mid_point3 t set t.voltlevel = '直流2.4V' where t.voltage_code = '59';
   update mid_point3 t set t.voltlevel = '直流3V' where t.voltage_code = '60';
   update mid_point3 t set t.voltlevel = '直流4.5V' where t.voltage_code = '61';
   update mid_point3 t set t.voltlevel = '直流5V' where t.voltage_code = '62';
   update mid_point3 t set t.voltlevel = '直流6V' where t.voltage_code = '63';
   update mid_point3 t set t.voltlevel = '直流9V' where t.voltage_code = '64';
   update mid_point3 t set t.voltlevel = '直流12V' where t.voltage_code = '65';
   update mid_point3 t set t.voltlevel = '直流15V' where t.voltage_code = '66';
   update mid_point3 t set t.voltlevel = '直流30V' where t.voltage_code = '67';
   update mid_point3 t set t.voltlevel = '直流60V' where t.voltage_code = '68';
   update mid_point3 t set t.voltlevel = '直流72V' where t.voltage_code = '69';
   update mid_point3 t set t.voltlevel = '直流160V' where t.voltage_code = '70';
   update mid_point3 t set t.voltlevel = '直流400V' where t.voltage_code = '71';
   update mid_point3 t set t.voltlevel = '直流440V' where t.voltage_code = '72';
   update mid_point3 t set t.voltlevel = '直流630V' where t.voltage_code = '73';
   update mid_point3 t set t.voltlevel = '直流800V' where t.voltage_code = '74';
   update mid_point3 t set t.voltlevel = '直流1000V' where t.voltage_code = '75';
   update mid_point3 t set t.voltlevel = '直流1250V' where t.voltage_code = '76';
   update mid_point3 t set t.voltlevel = '直流1500V' where t.voltage_code = '77';
   update mid_point3 t set t.voltlevel = '直流2000V' where t.voltage_code = '78';
   update mid_point3 t set t.voltlevel = '直流3000V' where t.voltage_code = '79';
   update mid_point3 t set t.voltlevel = '直流500kV' where t.voltage_code = '81';
   update mid_point3 t set t.voltlevel = '直流700kV' where t.voltage_code = '82';
   update mid_point3 t set t.voltlevel = '直流800kV' where t.voltage_code = '83';
   update mid_point3 t set t.voltlevel = '直流115V' where t.voltage_code = '91';
   update mid_point3 t set t.voltlevel = '直流230V' where t.voltage_code = '92';
   update mid_point3 t set t.voltlevel = '直流460V' where t.voltage_code = '93';
   update mid_point3 t set t.voltlevel = '直流600V' where t.voltage_code = '94';
   update mid_point3 t set t.voltlevel = '直流750V' where t.voltage_code = '95';
   update mid_point3 t set t.voltlevel = '其它直流电压' where t.voltage_code = '99';
   update mid_point3 t set t.voltlevel = '不区分电压等级' where t.voltage_code = '00';
commit;




alter table mid_point3 add utilkind varchar(50);
alter table mid_point3 add utilkindcode varchar(50);


update mid_point3 mp
   set mp.utilkindcode =
       (select utilization_kind
          from mid_customer c
         where mp.yhbh = c.pk_src_object);

update mid_point3 set utilkind='大工业用电' where utilkindcode='100';
update mid_point3 set utilkind='普通工业' where utilkindcode='200';
update mid_point3 set utilkind='非工业' where utilkindcode='260';
update mid_point3 set utilkind='商业' where utilkindcode='300';
update mid_point3 set utilkind='趸售' where utilkindcode='400';
update mid_point3 set utilkind='居民生活' where utilkindcode='500';
update mid_point3 set utilkind='非居民' where utilkindcode='600';
update mid_point3 set utilkind='农业生产' where utilkindcode='700';
update mid_point3 set utilkind='农业排灌' where utilkindcode='800';
update mid_point3 set utilkind='其它用电' where utilkindcode='900';
update mid_point3 set utilkind='未知' where utilkind is null;
commit;

CREATE TABLE MID_HYFL
(
  code             varchar2(50),
  name             varchar2(50)
);
 
insert into MID_HYFL(code,name) values('A','农、林、牧、渔业');
insert into MID_HYFL(code,name) values('B','采矿业');
insert into MID_HYFL(code,name) values('C','制造业');
insert into MID_HYFL(code,name) values('D','电力、热力、燃气及水生产和供应业');
insert into MID_HYFL(code,name) values('E','建筑业');
insert into MID_HYFL(code,name) values('F','批发和零售业');
insert into MID_HYFL(code,name) values('G','交通运输、仓储和邮政业');
insert into MID_HYFL(code,name) values('H','住宿和餐饮业');
insert into MID_HYFL(code,name) values('I','信息传输、软件和信息技术服务业');
insert into MID_HYFL(code,name) values('J','金融业');
insert into MID_HYFL(code,name) values('K','房地产业');
insert into MID_HYFL(code,name) values('L','租赁和商务服务业');
insert into MID_HYFL(code,name) values('M','科学研究和技术服务业');
insert into MID_HYFL(code,name) values('N','水利、环境和公共设施管理业');
insert into MID_HYFL(code,name) values('O','居民服务、修理和其他服务业');
insert into MID_HYFL(code,name) values('P','教育');
insert into MID_HYFL(code,name) values('Q','卫生和社会工作');
insert into MID_HYFL(code,name) values('R','文化、体育和娱乐业');
insert into MID_HYFL(code,name) values('S','公共管理、社会保障和社会组织');
insert into MID_HYFL(code,name) values('T','国际组织');
commit;


alter table mid_point3 add hyfl varchar(50);
alter table mid_point3 add hyflcode varchar(50);


update mid_point3 mp
   set mp.hyflcode =
       (select INDUSTRY_CLASSIFICATION
          from mid_customer c
         where mp.yhbh = c.pk_src_object);


update mid_point3 mp
   set mp.hyfl =
       (select name from mid_hyfl c where substr(mp.hyflcode, 0, 1) = c.code);
	   
update mid_point3 set hyfl='未知' where hyfl is null;


alter table EST_IC_UNBALANCE_TMP add unbalance2 number(19, 5);

update EST_IC_UNBALANCE_TMP
   set unbalance2 =
       (GREATEST(IA, IB, IC) - LEAST(IA, IB, IC)) / ((IA + IB + IC) / 3);
	   
	   
--去掉居民生活
create table mid_point4 as select * from mid_point3 t where t.utilkindcode <> '500';  
