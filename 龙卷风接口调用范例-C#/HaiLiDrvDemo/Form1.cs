using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using Microsoft.Win32;        //注册表命名空间
using HaiLiDrvDemo;
using Newtonsoft.Json;
using System.IO;
using System.Threading.Tasks;
using System.Net;

namespace HaiLiDrvDemo
{
    public partial class Form1 : Form
    {

        //要加载的驱动dll实例
        static private int instance;
        //要加载方法的委托
        public delegate int Stock_Init(IntPtr nHwnd, int nMsg, int nWorkMode);
        public delegate int Stock_Quit(IntPtr nHwnd);
        public delegate int GetStockDrvInfo(int nInfo, IntPtr pBuf);
        public delegate int AskStockDay(string pszStockCode, int nTimePeriod);  // 取日线
        public delegate int AskStockMn5(string pszStockCode, int nTimePeriod);  //取五分钟数据
        public delegate int AskStockBase(string pszStockCode);  //取个股资料F10
        public delegate int AskStockNews();  //取财经新闻
        public delegate int AskStockHalt();  //中止补数
        public delegate int AskStockMin(string pszStockCode);  //取分时数据
        public delegate int AskStockPRP(string pszStockCode);  //取分笔数据
        public delegate int AskStockPwr();  //取除权数据
        public delegate int AskStockFin();  //取财务数据


        [DllImport("Kernel32")]
        public static extern int LoadLibrary(String funcname);
        [DllImport("Kernel32")]
        public static extern int GetProcAddress(int handle, String funcname);
        [DllImport("Kernel32")]
        public static extern int FreeLibrary(int handle);


        //private string dirData = "data\\" + DateTime.Now.Year + "\\" + DateTime.Now.ToString("yyyy-MM-dd");
        private string dirFenbi = "";

        private static Delegate GetAddress(int dllModule, string functionname, Type t)
        {
            int addr = GetProcAddress(dllModule, functionname);
            if (addr == 0)
                return null;
            else
                return Marshal.GetDelegateForFunctionPointer(new IntPtr(addr), t);
        }

        public Form1()
        {
            InitializeComponent();
            CreateDir();
        }

        private void CreateDir()
        {
            DateTime dt = GetStockDate();
            dirFenbi = "" + dt.Year + "\\" + dt.ToString("yyyy-MM-dd");
            //Directory.CreateDirectory(dirData);
            Directory.CreateDirectory(dirFenbi);
        }

        private Demo demo = null;
        private void button3_Click(object sender, EventArgs e)
        {
            //demo = new Demo(this.Handle.ToInt32());
            //demo.Test();
            System.Environment.Exit(0);
        }

        private void button1_Click(object sender, EventArgs e)
        {
            CreateDir();
            string DrvDllPath = "";
            //先从注册表中查找驱动DLL的位置，如果没有，再从本地查找接口驱动DLL是否存在
            //可能有异常，放在try块中
            try
            {
                RegistryKey rsg = null;                    //声明变量
                rsg = Registry.LocalMachine.OpenSubKey("SOFTWARE\\StockDrv", true); //true表可修改
                if (rsg.GetValue("Driver") != null)  //如果值不为空
                {
                    DrvDllPath = rsg.GetValue("Driver").ToString();
                }
                rsg.Close();   //关闭
            }
            catch (Exception ex)   //捕获异常
            {
                //this.label1.Text = ex.Message; //显示异常信息
            }
            //MessageBox.Show(DrvDllPath);
            string FRootPath = System.Windows.Forms.Application.StartupPath;
            if (DrvDllPath == "")
                DrvDllPath = FRootPath + "\\StockDrv.dll";
            //MessageBox.Show(FRootPath);
            instance = LoadLibrary(DrvDllPath);
            if (instance == 0)
            {
                MessageBox.Show("装载驱动DLL失败，请检查！");
            }
            Stock_Init Stock_InitProc = (Stock_Init)GetAddress(instance, "Stock_Init", typeof(Stock_Init));
            Stock_InitProc(this.Handle, StockDrv.RCV_MSG_STKDATA, StockDrv.RCV_WORK_SENDMSG);

        }

        protected override void WndProc(ref System.Windows.Forms.Message m)
        {
            if (m.Msg == Demo.MY_MSG_BEGIN)
            {
                MessageBox.Show("Demo开始.");
            }
            else if (m.Msg == Demo.MY_MSG_END)
            {
                MessageBox.Show("Demo结束.");
            }
            else if (m.Msg == HaiLiDrvDemo.StockDrv.RCV_MSG_STKDATA)
            {
                //MessageBox.Show("收到实际驱动消息！");
                switch (m.WParam.ToInt32())
                {
                    case HaiLiDrvDemo.StockDrv.RCV_REPORT:
                        {
                            HaiLiDrvDemo.RCV_DATA pHeader = (HaiLiDrvDemo.RCV_DATA)Marshal.PtrToStructure(m.LParam, typeof(HaiLiDrvDemo.RCV_DATA));
                            listBox1.Items.Add("收到实时行情消息！" + pHeader.m_nPacketNum.ToString());
                            for (int i = 0; i < pHeader.m_nPacketNum; i++)
                            {
                                //report就是一条实时行情数据  
                                HaiLiDrvDemo.RCV_REPORT_STRUCTExV3 report = (HaiLiDrvDemo.RCV_REPORT_STRUCTExV3)Marshal.PtrToStructure(new IntPtr((int)pHeader.m_pData + 158 * i), typeof(HaiLiDrvDemo.RCV_REPORT_STRUCTExV3));
                                //将实时数据存入自己的集合中，或者做别的数量  
                                //System.Text.StringBuilder sb = new System.Text.StringBuilder();
                                //sb.Append(report.m_szName);
                                //string codename = sb.ToString();
                                //注意：避免影响界面的刷新，只显示前20条记录
                                if (i <= 20)
                                {
                                    listBox1.Items.Add("股票代码：" + new string(report.m_szLabel));
                                    listBox1.Items.Add("交易时间：" + report.m_time.ToString() + ",最新价:" + Convert.ToString(report.m_fNewPrice) + ",申卖量5:" + Convert.ToString(report.m_fSellVolume5));
                                }

                                //WriteFile(report, report.m_szLabel, dirData);

                                //注意：避免影响界面的刷新，只显示前20条记录
                                //if (i > 20) break;
                            }
                            break;
                        }
                    case HaiLiDrvDemo.StockDrv.RCV_FILEDATA:
                        {
                            HaiLiDrvDemo.RCV_DATA pHeader = (HaiLiDrvDemo.RCV_DATA)Marshal.PtrToStructure(m.LParam, typeof(HaiLiDrvDemo.RCV_DATA));
                            switch (pHeader.m_wDataType)
                            {
                                // 补日线数据;   
                                case HaiLiDrvDemo.StockDrv.FILE_HISTORY_EX:
                                    //行情中的商品名称等数据存入此处（即补充的数据头）  
                                    for (int i = 0; i < pHeader.m_nPacketNum; i++)
                                    {
                                        //日线数据  
                                        HaiLiDrvDemo.RCV_HISTORY_STRUCTEx history = (HaiLiDrvDemo.RCV_HISTORY_STRUCTEx)Marshal.PtrToStructure(new IntPtr((int)pHeader.m_pData + 32 * i), typeof(HaiLiDrvDemo.RCV_HISTORY_STRUCTEx));
                                        HaiLiDrvDemo.RCV_EKE_HEADEx HeadEx = (HaiLiDrvDemo.RCV_EKE_HEADEx)Marshal.PtrToStructure(new IntPtr((int)pHeader.m_pData + 32 * i), typeof(HaiLiDrvDemo.RCV_EKE_HEADEx));
                                        //时间日期计算  new DateTime(1970, 1, 1, 8, 0, 0, DateTimeKind.Unspecified).Add(TimeSpan.FromTicks(history.m_time * TimeSpan.TicksPerSecond));  
                                        //将日线数据存入自己的集合中，或处理数据  
                                        //前四个字节，用来判断当前记录是包含股票代码的数据头还是实际历史日线数据
                                        if ((uint)history.m_time == StockDrv.EKE_HEAD_TAG)
                                        {
                                            listBox1.Items.Add("日线数据，股票代码：" + new string(HeadEx.m_szLabel));
                                        }
                                        else
                                        {
                                            listBox1.Items.Add("交易时间：" + history.m_time.ToString() + ",最高价:" + Convert.ToString(history.m_fHigh) + ",最低价:" + Convert.ToString(history.m_fLow));
                                        }
                                        //注意：避免影响界面的刷新，只显示前20条记录
                                        if (i > 20) break;
                                    }
                                    break;
                                // 补分时线数据;   
                                case HaiLiDrvDemo.StockDrv.FILE_MINUTE_EX:
                                    //行情中的商品名称等数据存入此处（即补充的数据头）  
                                    for (int i = 0; i < pHeader.m_nPacketNum; i++)
                                    {
                                        //分时线数据  
                                        HaiLiDrvDemo.RCV_MINUTE_STRUCTEx minute = (HaiLiDrvDemo.RCV_MINUTE_STRUCTEx)Marshal.PtrToStructure(new IntPtr((int)pHeader.m_pData + 16 * i), typeof(HaiLiDrvDemo.RCV_MINUTE_STRUCTEx));
                                        HaiLiDrvDemo.RCV_EKE_HEADEx HeadEx = (HaiLiDrvDemo.RCV_EKE_HEADEx)Marshal.PtrToStructure(new IntPtr((int)pHeader.m_pData + 16 * i), typeof(HaiLiDrvDemo.RCV_EKE_HEADEx));
                                        //时间日期计算  new DateTime(1970, 1, 1, 8, 0, 0, DateTimeKind.Unspecified).Add(TimeSpan.FromTicks(history.m_time * TimeSpan.TicksPerSecond));  
                                        //将日线数据存入自己的集合中，或处理数据  
                                        //前四个字节，用来判断当前记录是包含股票代码的数据头还是实际历史日线数据
                                        if ((uint)minute.m_time == StockDrv.EKE_HEAD_TAG)
                                        {
                                            listBox1.Items.Add("分时线数据，股票代码：" + new string(HeadEx.m_szLabel));
                                        }
                                        else
                                        {
                                            listBox1.Items.Add("交易时间：" + minute.m_time.ToString() + ",成交价:" + Convert.ToString(minute.m_fPrice) + ",成交量:" + Convert.ToString(minute.m_fVolume));
                                        }
                                        //注意：避免影响界面的刷新，只显示前20条记录
                                        if (i > 20) break;
                                    }
                                    break;
                                // 补五分钟线数据;   
                                case HaiLiDrvDemo.StockDrv.FILE_5MINUTE_EX:
                                    //行情中的商品名称等数据存入此处（即补充的数据头）  
                                    for (int i = 0; i < pHeader.m_nPacketNum; i++)
                                    {
                                        //五分钟线数据  
                                        HaiLiDrvDemo.RCV_HISMINUTE_STRUCTEx history = (HaiLiDrvDemo.RCV_HISMINUTE_STRUCTEx)Marshal.PtrToStructure(new IntPtr((int)pHeader.m_pData + 32 * i), typeof(HaiLiDrvDemo.RCV_HISMINUTE_STRUCTEx));
                                        HaiLiDrvDemo.RCV_EKE_HEADEx HeadEx = (HaiLiDrvDemo.RCV_EKE_HEADEx)Marshal.PtrToStructure(new IntPtr((int)pHeader.m_pData + 32 * i), typeof(HaiLiDrvDemo.RCV_EKE_HEADEx));
                                        //时间日期计算  new DateTime(1970, 1, 1, 8, 0, 0, DateTimeKind.Unspecified).Add(TimeSpan.FromTicks(history.m_time * TimeSpan.TicksPerSecond));  
                                        //将日线数据存入自己的集合中，或处理数据  
                                        //前四个字节，用来判断当前记录是包含股票代码的数据头还是实际历史日线数据
                                        if ((uint)history.m_time == StockDrv.EKE_HEAD_TAG)
                                        {
                                            listBox1.Items.Add("五分钟线数据，股票代码：" + new string(HeadEx.m_szLabel));
                                        }
                                        else
                                        {
                                            listBox1.Items.Add("交易时间：" + history.m_time.ToString() + ",最高价:" + Convert.ToString(history.m_fHigh) + ",最低价:" + Convert.ToString(history.m_fLow));
                                        }
                                        //注意：避免影响界面的刷新，只显示前20条记录
                                        if (i > 20) break;
                                    }
                                    break;
                                // 补除权数据;   
                                case HaiLiDrvDemo.StockDrv.FILE_POWER_EX:
                                    //行情中的商品名称等数据存入此处（即补充的数据头）  
                                    for (int i = 0; i < pHeader.m_nPacketNum; i++)
                                    {
                                        //除权数据  
                                        HaiLiDrvDemo.RCV_POWER_STRUCTEx history = (HaiLiDrvDemo.RCV_POWER_STRUCTEx)Marshal.PtrToStructure(new IntPtr((int)pHeader.m_pData + 20 * i), typeof(HaiLiDrvDemo.RCV_POWER_STRUCTEx));
                                        HaiLiDrvDemo.RCV_EKE_HEADEx HeadEx = (HaiLiDrvDemo.RCV_EKE_HEADEx)Marshal.PtrToStructure(new IntPtr((int)pHeader.m_pData + 20 * i), typeof(HaiLiDrvDemo.RCV_EKE_HEADEx));
                                        //时间日期计算  new DateTime(1970, 1, 1, 8, 0, 0, DateTimeKind.Unspecified).Add(TimeSpan.FromTicks(history.m_time * TimeSpan.TicksPerSecond));  
                                        //将日线数据存入自己的集合中，或处理数据  
                                        //前四个字节，用来判断当前记录是包含股票代码的数据头还是实际历史日线数据
                                        if ((uint)history.m_time == StockDrv.EKE_HEAD_TAG)
                                        {
                                            listBox1.Items.Add("除权数据，股票代码：" + new string(HeadEx.m_szLabel));
                                        }
                                        else
                                        {
                                            listBox1.Items.Add("时间：" + history.m_time.ToString() + ",每股送:" + Convert.ToString(history.m_fGive) + ",每股红利:" + Convert.ToString(history.m_fProfit));
                                        }
                                        //注意：避免影响界面的刷新，只显示前20条记录
                                        if (i > 20) break;
                                    }
                                    break;
                                // 补基本资料数据;   
                                case HaiLiDrvDemo.StockDrv.FILE_BASE_EX:
                                    listBox1.Items.Add("收到F10资料数据 长度:" + pHeader.m_File.m_dwLen.ToString());
                                    break;
                                // 补新闻资讯数据;   
                                case HaiLiDrvDemo.StockDrv.FILE_NEWS_EX:
                                    listBox1.Items.Add("收到公告新闻数据 长度:" + pHeader.m_File.m_dwLen.ToString());
                                    break;
                                default: break;
                            }
                            break;
                        }
                    case HaiLiDrvDemo.StockDrv.RCV_FENBIDATA:
                        {
                            //接口补充的盘口分笔数据,这个是指当天的盘口分笔
                            HaiLiDrvDemo.RCV_FENBI mHeader = (HaiLiDrvDemo.RCV_FENBI)Marshal.PtrToStructure(m.LParam, typeof(HaiLiDrvDemo.RCV_FENBI));
                            listBox1.Items.Add("当前分笔代码:" + new string(mHeader.m_szLabel));
                            listBox1.Items.Add("共有分笔记录数:" + mHeader.m_nCount.ToString());

                            //Task.Factory.StartNew((obj) =>
                            //{
                            Tuple<RCV_FENBI, IntPtr> tuple = Tuple.Create(mHeader, m.LParam);
                            HaiLiDrvDemo.RCV_FENBI header = tuple.Item1;
                            char[] dest = new char[6];//股票代码
                            Array.Copy(header.m_szLabel, dest, 6);
                            //18515=SH 23123=SZ                            
                            string fileName = dirFenbi + "\\" + (header.m_wMarket == 18515 ? "1" : "2") + new string(dest) + ".btor";
                            WriteBinaryFile(fileName, tuple);
                            // }, );
                            break;
                        }
                    case HaiLiDrvDemo.StockDrv.RCV_MKTTBLDATA:
                        {
                            HaiLiDrvDemo.HLMarketType mHeader = (HaiLiDrvDemo.HLMarketType)Marshal.PtrToStructure(m.LParam, typeof(HaiLiDrvDemo.HLMarketType));
                            listBox1.Items.Add("当前码表市场:" + mHeader.m_wMarket.ToString());
                            listBox1.Items.Add("总码表记录数:" + mHeader.m_nCount.ToString());
                            for (int i = 0; i < mHeader.m_nCount; i++)
                            {
                                HaiLiDrvDemo.RCV_TABLE_STRUCT table = (HaiLiDrvDemo.RCV_TABLE_STRUCT)Marshal.PtrToStructure(new IntPtr((int)m.LParam + 54 + 44 * i), typeof(HaiLiDrvDemo.RCV_TABLE_STRUCT));
                                //将实时数据存入自己的集合中，或者做别的数量  
                                //System.Text.StringBuilder sb = new System.Text.StringBuilder();
                                //sb.Append(report.m_szName);
                                //string codename = sb.ToString();
                                listBox1.Items.Add("股票代码：" + new string(table.m_szLabel));
                                listBox1.Items.Add("股票名称：" + new string(table.m_szName));
                                //注意：避免影响界面的刷新，只显示前20条记录
                                if (i > 20) break;
                            }
                            break;
                        }
                    case HaiLiDrvDemo.StockDrv.RCV_FINANCEDATA:
                        {
                            HaiLiDrvDemo.RCV_DATA pHeader = (HaiLiDrvDemo.RCV_DATA)Marshal.PtrToStructure(m.LParam, typeof(HaiLiDrvDemo.RCV_DATA));
                            listBox1.Items.Add("收到财务数据包，记录数：" + pHeader.m_nPacketNum.ToString());
                            for (int i = 0; i < pHeader.m_nPacketNum; i++)
                            {
                                HaiLiDrvDemo.Fin_LJF_STRUCTEx FinLjf = (HaiLiDrvDemo.Fin_LJF_STRUCTEx)Marshal.PtrToStructure(new IntPtr((int)pHeader.m_pData + 166 * i), typeof(HaiLiDrvDemo.Fin_LJF_STRUCTEx));
                                //将财务数据存入自己的集合中，或者做别的数量  
                                //System.Text.StringBuilder sb = new System.Text.StringBuilder();
                                //sb.Append(report.m_szName);
                                //string codename = sb.ToString();
                                //listBox1.Items.Add(new string(FinLjf.m_szLabel));
                                listBox1.Items.Add("市场类型:" + FinLjf.m_wMarket.ToString() + ",股票代码:" + new string(FinLjf.m_szLabel));
                                listBox1.Items.Add("数据日期:" + FinLjf.BGRQ.ToString() + ",总股本:" + Convert.ToString(FinLjf.ZGB) +
                                ",净资收益率:" + Convert.ToString(FinLjf.JZCSYL));

                                //注意：避免影响界面的刷新，只显示前20条记录
                                if (i > 20) break;
                            }
                            break;
                        }
                    default:
                        break;
                }
                return;
            }

            base.WndProc(ref m);
        }

        private void WriteJsonFile(string fileName, Tuple<RCV_FENBI, IntPtr> tuple)
        {
            using (System.IO.StreamWriter sw = new System.IO.StreamWriter(fileName, !chkOverwrite.Checked))
            {
                for (int i = 0; i < tuple.Item1.m_nCount; i++)
                {
                    HaiLiDrvDemo.RCV_FENBI_STRUCTEx Buf =
                    (HaiLiDrvDemo.RCV_FENBI_STRUCTEx)Marshal.PtrToStructure(
                        new IntPtr((int)tuple.Item2 + 30 + 108 * i),
                        typeof(HaiLiDrvDemo.RCV_FENBI_STRUCTEx));

                    string strSerializeJSON = JsonConvert.SerializeObject(Buf);
                    sw.WriteLine(strSerializeJSON);// 直接追加文件末尾，换行
                }
            }
        }

        private void WriteBinaryFile(string fileName, Tuple<RCV_FENBI, IntPtr> tuple)
        {
            using (FileStream fileStream = new FileStream(fileName, chkOverwrite.Checked ? FileMode.Create : FileMode.Append))
            {
                using (BinaryWriter writer = new BinaryWriter(fileStream))
                {
                    writer.Write(0);
                    for (int i = 0; i < tuple.Item1.m_nCount; i++)
                    {
                        HaiLiDrvDemo.RCV_FENBI_STRUCTEx data =
                             (HaiLiDrvDemo.RCV_FENBI_STRUCTEx)Marshal.PtrToStructure(
                                 new IntPtr((int)tuple.Item2 + 30 + 108 * i),
                                 typeof(HaiLiDrvDemo.RCV_FENBI_STRUCTEx));

                        writer.Write(Reverse(BitConverter.GetBytes(data.m_lTime * 1000L)));
                        writer.Write(Reverse(BitConverter.GetBytes((int)Math.Round(data.m_fNewPrice * 100))));
                        writer.Write(Reverse(BitConverter.GetBytes(0)));
                        writer.Write(Reverse(BitConverter.GetBytes((long)Math.Round(data.m_fVolume * 100L))));
                        writer.Write(Reverse(BitConverter.GetBytes((long)Math.Round(data.m_fAmount * 100L))));
                        writer.Write(Reverse(BitConverter.GetBytes(true)));

                        for (int j = 0; j < 5; j++)
                        {
                            writer.Write(Reverse(BitConverter.GetBytes(data.m_fBuyPrice[j])));
                            writer.Write(Reverse(BitConverter.GetBytes(data.m_fBuyVolume[j])));
                        }
                        for (int j = 0; j < 5; j++)
                        {
                            writer.Write(Reverse(BitConverter.GetBytes(data.m_fSellPrice[j])));
                            writer.Write(Reverse(BitConverter.GetBytes(data.m_fSellVolume[j])));
                        }
                    }
                }
            }
        }

        private byte[] Reverse(byte[] bytes)
        {
            Array.Reverse(bytes);
            return bytes;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Stock_Quit Stock_QuitProc = (Stock_Quit)GetAddress(instance, "Stock_Quit", typeof(Stock_Quit));
            if (Stock_QuitProc != null)
                Stock_QuitProc(this.Handle);

            FreeLibrary(instance);
        }

        private void button4_Click(object sender, EventArgs e)
        {

            AskStockDay AskStockDayProc = (AskStockDay)GetAddress(instance, "AskStockDay", typeof(AskStockDay));
            if (AskStockDayProc != null)
                AskStockDayProc("SH600026", 2);
        }

        private void button5_Click(object sender, EventArgs e)
        {
            AskStockMn5 AskStockMn5Proc = (AskStockMn5)GetAddress(instance, "AskStockMn5", typeof(AskStockMn5));
            if (AskStockMn5Proc != null)
                AskStockMn5Proc("SH600026", 2);
        }

        private void button6_Click(object sender, EventArgs e)
        {
            AskStockBase AskStockBaseProc = (AskStockBase)GetAddress(instance, "AskStockBase", typeof(AskStockBase));
            if (AskStockBaseProc != null)
                AskStockBaseProc("");
        }

        private void button7_Click(object sender, EventArgs e)
        {
            AskStockNews AskStockNewsProc = (AskStockNews)GetAddress(instance, "AskStockNews", typeof(AskStockNews));
            if (AskStockNewsProc != null)
                AskStockNewsProc();
        }

        private void button8_Click(object sender, EventArgs e)
        {
            AskStockHalt AskStockHaltProc = (AskStockHalt)GetAddress(instance, "AskStockHalt", typeof(AskStockHalt));
            if (AskStockHaltProc != null)
                AskStockHaltProc();
        }

        private void button9_Click(object sender, EventArgs e)
        {
            //如果参数代码为空，则补全部分时
            AskStockMin AskStockMinProc = (AskStockMin)GetAddress(instance, "AskStockMin", typeof(AskStockMin));
            if (AskStockMinProc != null)
                AskStockMinProc("SH600026");
        }

        private void button10_Click(object sender, EventArgs e)
        {
            CreateDir();
            //如果参数代码为空，则补全部股票分笔
            AskStockPRP AskStockPRPProc = (AskStockPRP)GetAddress(instance, "AskStockPRP", typeof(AskStockPRP));
            if (AskStockPRPProc != null)
                AskStockPRPProc("");
        }

        private void button11_Click(object sender, EventArgs e)
        {
            AskStockPwr AskStockPwrProc = (AskStockPwr)GetAddress(instance, "AskStockPwr", typeof(AskStockPwr));
            if (AskStockPwrProc != null)
                AskStockPwrProc();
        }

        private void button12_Click(object sender, EventArgs e)
        {
            AskStockFin AskStockFinProc = (AskStockFin)GetAddress(instance, "AskStockFin", typeof(AskStockFin));
            if (AskStockFinProc != null)
                AskStockFinProc();
        }


        private DateTime GetStockDate()
        {
            try
            {
                WebRequest request = HttpWebRequest.Create("http://hq.sinajs.cn/list=sh000001");
                request.Method = "GET";
                using (WebResponse wr = request.GetResponse())
                {
                    StreamReader sr = new StreamReader(wr.GetResponseStream(), Encoding.UTF8);
                    string[] content = sr.ReadToEnd().Split(',');
                    sr.Close();
                    return DateTime.Parse(content[content.Length - 3]);
                }
            }
            catch (Exception ex)
            {
                return DateTime.Now;
            }
            //string[] content = WebRequest.Get("http://hq.sinajs.cn/list=sh000001").execute().returnContent().asString().split(",");
            //return org.apache.commons.lang3.time.DateUtils.parseDate(content[content.length - 3], "yyyy-MM-dd");
        }
    }

    public class Demo
    {
        private int m_hWnd = 0;

        public Demo(int hWnd)
        {
            m_hWnd = hWnd;
        }

        private const int WM_USER = 0x0400;
        public static int MY_MSG_BEGIN = WM_USER + 100;
        public static int MY_MSG_END = WM_USER + 101;

        [DllImport("User32.DLL")]
        public static extern int SendMessage(int hWnd, int Msg, int wParam, int lParam);

        public void Test()
        {
            SendMessage(m_hWnd, MY_MSG_BEGIN, 0, 0);
            for (int i = 0; i < 100000; i++)
            {
                Application.DoEvents();
            }
            SendMessage(m_hWnd, MY_MSG_END, 0, 0);
        }
    }
}