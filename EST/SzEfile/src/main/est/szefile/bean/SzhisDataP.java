package est.szefile.bean;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 配变15粒度的相关数据，包括三相电压、三相电流、总有功、总无功容量等数据 按日分区
 * 
 * @author 殷闯
 * @version 1.0
 * @created 17-12月-2013 15:01:22
 */
@Entity
@Table(name = "EST_DDY_SZHISDATA_P")
public class SzhisDataP extends SzhisData {

}