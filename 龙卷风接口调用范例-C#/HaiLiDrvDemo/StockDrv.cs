using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Text;


namespace HaiLiDrvDemo
{
    [JsonObject(MemberSerialization.OptOut)]
    [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Ansi, Pack = 1)]
    public struct RCV_REPORT_STRUCTExV3
    {
        public UInt16 m_cbSize;                                    // 结构大小
        public Int32 m_time;                                       // 交易时间
        public UInt16 m_wMarket;                                   // 股票市场类型
        //m_szLabel，m_szName 的定义根据自己喜好可以定义成
        [JsonIgnore]
        [MarshalAs(   UnmanagedType.ByValArray, SizeConst=10)]   
        public   char[]  m_szLabel; //   代码,以'\0'结尾   数组大小为STKLABEL_LEN，在c++描述中为char[10]     
        [JsonIgnore]
        [MarshalAs(   UnmanagedType.ByValArray,   SizeConst=32)]   
        public   char[]  m_szName;  //   名称,以'\0'结尾   数组大小为STKNAME_LEN,在c++描述中为char[32]     
        /*  也可以定义成
        [MarshalAs(UnmanagedType.ByValTStr,SizeConst = 10)]
        public string m_szLabel;                               // 代码,以'\0'结尾  数组大小为STKLABEL_LEN，在c++描述中为char[10]     
        [MarshalAs(UnmanagedType.ByValTStr,SizeConst = 32)]
        public string m_szName;                                // 名称,以'\0'结尾 组大小为STKNAME_LEN,在c++描述中为char[32]    
        */
        public Single m_fLastClose;                           // 昨收
        public Single m_fOpen;                                // 今开
        public Single m_fHigh;                                // 最高
        public Single m_fLow;                                 // 最低
        public Single m_fNewPrice;                            // 最新
        public Single m_fVolume;                              // 成交量
        public Single m_fAmount;                              // 成交额
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 3)]
        public Single[] m_fBuyPrice;                         // 申买价1,2,3
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 3)]
        public Single[] m_fBuyVolume;                        // 申买量1,2,3
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 3)]
        public Single[] m_fSellPrice;                        // 申卖价1,2,3
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 3)]
        public Single[] m_fSellVolume;                       // 申卖量1,2,3
        public Single m_fBuyPrice4;                          // 申买价4
        public Single m_fBuyVolume4;                         // 申买量4
        public Single m_fSellPrice4;                         // 申卖价4
        public Single m_fSellVolume4;                        // 申卖量4
        public Single m_fBuyPrice5;                          // 申买价5
        public Single m_fBuyVolume5;                         // 申买量5
        public Single m_fSellPrice5;                         // 申卖价5
        public Single m_fSellVolume5;                        // 申卖量5
    };


    //[StructLayout(LayoutKind.Sequential, CharSet = CharSet.Ansi, Pack = 1)]
    public struct RCV_FILE_HEADEx
    {
        public int m_dwAttrib;                      // 文件子类型
        public int m_dwLen;                         // 文件长度
        public int m_dwSerialNoorTime;              //文件序列号或时间.
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 260)]
        public char[] m_szFileName;                        // 文件名 or URL
    }
    //[StructLayout(LayoutKind.Sequential, CharSet = CharSet.Ansi , Pack=1)]
    public struct RCV_DATA
    {
        public int m_wDataType;                     // 文件类型
        public int m_nPacketNum;                    // 记录数,参见注一
        public RCV_FILE_HEADEx m_File;          // 文件接口
        public int m_bDISK;                        // 文件是否已存盘的文件
        public IntPtr m_pData;
    } ;


    //补充数据头  
    //这个在日线、分时数据中都有用到
    public struct RCV_EKE_HEADEx
    {
        public uint m_dwHeadTag; // = EKE_HEAD_TAG  
        public ushort m_wMarket; // 市场类型  
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 10)]
        public char[] m_szLabel; // 股票代码  
    }

    //补充日线数据  
    //[StructLayout(LayoutKind.Explicit)]
    public struct RCV_HISTORY_STRUCTEx
    {
        public int m_time;
        public Single m_fOpen; //开盘  
        public Single m_fHigh; //最高  
        public Single m_fLow; //最低  
        public Single m_fClose; //收盘  
        public Single m_fVolume; //量  
        public Single m_fAmount; //额  
        public UInt16 m_wAdvance; //涨数,仅大盘有效  
        public UInt16 m_wDecline; //跌数,仅大盘有效  
    }
  
    //补充历史五分钟K线数据,每一数据结构都应通过 m_time == EKE_HEAD_TAG,判断是否为 m_head,然后再作解释
    public struct RCV_HISMINUTE_STRUCTEx
    {
        public int m_time;              //UCT
        public Single m_fOpen;          //开盘
        public Single m_fHigh;          //最高
        public Single m_fLow;           //最低
        public Single m_fClose;         //收盘
        public Single m_fVolume;        //量
        public Single m_fAmount;        //额
        public Single m_fActiveBuyVol;  //主动买量如没有计算m_fActiveBuyVol=0
    }

    //补充分时线数据
    public struct RCV_MINUTE_STRUCTEx
    {
        public Int32 m_time; //UCT
        public Single m_fPrice;
        public Single m_fVolume;
        public Single m_fAmount;
    }

    //补充除权数据
    public struct RCV_POWER_STRUCTEx
    {
        public int m_time;         //UCT
        public Single m_fGive;      //每股送
        public Single m_fPei;       //每股配
        public Single m_fPeiPrice;  //配股价,仅当 m_fPei!=0.0f 时有效
        public Single m_fProfit;    //每股红利
    }


    //分笔数据///////////////////////////////////
    [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Ansi, Pack = 1)] 
    public struct RCV_FENBI_STRUCTEx
    {
	    public int		m_lTime;				// hhmmss 例：93056 表明9:
	    public Single	    m_fHigh;				// 最高
	    public Single		m_fLow;					// 最低 
	    public Single		m_fNewPrice;			// 最新 
	    public Single		m_fVolume;				// 成交量
	    public Single		m_fAmount;				// 成交额
	    public int		m_lStroke;				// 保留

        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 5)]
        public Single[]     m_fBuyPrice;            // 申买价1,2,3,4,5
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 5)]
	    public Single[]		m_fBuyVolume;			// 申买量1,2,3,4,5
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 5)]
	    public Single[]		m_fSellPrice;			// 申卖价1,2,3,4,5
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 5)]
	    public Single[]		m_fSellVolume;			// 申卖量1,2,3,4,5

    };

    [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Ansi, Pack = 1)] 
    public struct RCV_FENBI
    {
	    public UInt16 		m_wMarket;					// 股票市场类型
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 10)]
	    public char[]		m_szLabel;	                // 股票代码,以'\0'结尾
	    public Int32		m_lDate;					// 分笔数据的日期 FORMAT:
	    public Single		m_fLastClose;				// 昨收
	    public Single		m_fOpen;					// 今开
	    public UInt16		m_nCount;					//m_Data的数据量分笔笔数
        public IntPtr       m_Data;						//长度为m_nCount
    };


    //码表数据
    [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Ansi, Pack = 1)]
    public struct  RCV_TABLE_STRUCT
    {
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 10)]
        public char[] m_szLabel;            //股票代码,以'\0'结尾,如 "500500"
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 32)]
        public char[] m_szName;             //股票名称,以'\0'结尾,"上证指数"
        public UInt16 m_cProperty;          //每手股数
    }

    //码表数据头结构
    [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Ansi, Pack = 1)]
    public struct HLMarketType
    {
        public UInt16 m_wMarket;     //市场代码
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 16)]
        public char[] m_Name;        //市场名称
        public int m_lProperty;      //市场属性（未定义）
        public int m_lDate;          //数据日期（20030114）
        public UInt16 m_PeriodCount; //交易时段个数
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 5)]
        public UInt16[] m_OpenTime;  //开市时间 1,2,3,4,5
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 5)]
        public UInt16[] m_CloseTime; //收市时间 1,2,3,4,5
        public UInt16 m_nCount;      //该市场的证券个数
        public IntPtr m_Data;        //长度为m_nCount
    }

    [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Ansi, Pack = 1)]
    public struct Fin_LJF_STRUCTEx
    {
        public UInt16 m_wMarket;         // 股票市场类型
        public UInt16 N1;                // 保留字段
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 10)]
        public char[] m_szLabel;         // 股票代码,以'\0'结尾,如 "600050"  10个字节 同通视规范定义
        public int BGRQ;                 // 财务数据的日期 如半年报 季报等 如 20090630 表示 2009年半年报
        public Single ZGB;             // 总股本
        public Single GJG;             // 国家股
        public Single FQFRG;           // 发起人法人股
        public Single FRG;             // 法人股
        public Single BGS;             // B股
        public Single HGS;             // H股
        public Single MQLT;            // 目前流通
        public Single ZGG;             // 职工股
        public Single A2ZPG;           // A2转配股
        public Single ZZC;             // 总资产(千元)
        public Single LDZC;            // 流动资产
        public Single GDZC;            // 固定资产
        public Single WXZC;            // 无形资产
        public Single CQTZ;            // 长期投资
        public Single LDFZ;            // 流动负债
        public Single CQFZ;            // 长期负债
        public Single ZBGJJ;           // 资本公积金
        public Single MGGJJ;           // 每股公积金
        public Single GDQY;            // 股东权益
        public Single ZYSR;            // 主营收入
        public Single ZYLR;            // 主营利润
        public Single QTLR;            // 其他利润
        public Single YYLR;            // 营业利润
        public Single TZSY;            // 投资收益
        public Single BTSR;            // 补贴收入
        public Single YYWSZ;           // 营业外收支
        public Single SNSYTZ;          // 上年损益调整
        public Single LRZE;            // 利润总额
        public Single SHLR;            // 税后利润
        public Single JLR;             // 净利润
        public Single WFPLR;           // 未分配利润
        public Single MGWFP;           // 每股未分配
        public Single MGSY;            // 每股收益
        public Single MGJZC;           // 每股净资产
        public Single TZMGJZC;         // 调整每股净资产
        public Single GDQYB;           // 股东权益比
        public Single JZCSYL;          // 净资收益率
    }

    class StockDrv
    {
        public const int FILE_HISTORY_EX = 2;//补日线数据
        public const int FILE_MINUTE_EX = 4;//补分钟线数据
        public const int FILE_POWER_EX = 6;//补除权数据
        public const int FILE_5MINUTE_EX = 81;//补五分钟线数据
        public const int FILE_BASE_EX = 0x1000;//钱龙兼容基本资料文件，m_szFileName仅包含文件名
        public const int FILE_NEWS_EX = 0x1002;//新闻类，其类型由m_szFileName中子目录名来定
        public const int RCV_WORK_SENDMSG = 4;//工作方式类型定义，窗口消息方式
        public const int RCV_MSG_STKDATA = 0x8001;//指定使用的消息
        public const int RCV_REPORT = 0x3f001234;//股票行情
        public const int RCV_FILEDATA = 0x3f001235;//文件
        public const int RCV_FENBIDATA= 0x3f001258; //分笔数据，买卖盘数据均为空值
        public const int RCV_MKTTBLDATA = 0x3f001259;//接收到市场码表数据
        public const int RCV_FINANCEDATA = 0x3f001300;//接收到财务文件数据
        public const UInt32 EKE_HEAD_TAG = 0xffffffff;//数据头结构标记

    }
}
