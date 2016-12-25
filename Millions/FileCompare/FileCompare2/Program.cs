using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;

namespace FileCompare2
{
    class Program
    {
        static void Main(string[] args)
        {
            //DirectoryInfo d = new DirectoryInfo(@"E:\yingkou");
            //if(d.Exists == true)
            //{
            //    FileInfo[] fi = d.GetFiles();
            //}
            try
            {
                List<string> listTerm = new List<string>();
                string[] files = Directory.GetFiles(@"E:\yingkou\data\终端参数设置");
                foreach (string s in files)
                {
                    string consno = s.Substring(s.IndexOf("cons_no-") + 8);
                    consno = consno.Substring(0, consno.IndexOf("-protocol_code"));
                    listTerm.Add(consno);
                }

                HashSet<string> set = new HashSet<string>();
                foreach(string s in listTerm)
                {
                    set.Add(s);
                }

                Console.WriteLine(set.Count);

                List<string> listDetail = new List<string>();
                files = Directory.GetFiles(@"E:\yingkou\data\终端详细信息");
                foreach (string s in files)
                {
                    string consno = s.Substring(s.IndexOf("cons_no-") + 8);
                    consno = consno.Substring(0, consno.IndexOf("-terminalTypeCode"));
                    listDetail.Add(consno);
                }

                foreach (string consno in listTerm)
                {
                    if (listDetail.Contains(consno) == false)
                    {
                        Debug.WriteLine(consno);
                        Console.WriteLine(consno);
                    }
                }
            }
            catch (Exception ex)
            {
                Debug.WriteLine("ex:" + ex.Message);
            }
        }
    }
}
