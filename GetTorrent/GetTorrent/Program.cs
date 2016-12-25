using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace GetTorrent
{
    class Program
    {
        static void Main(string[] args)
        {
            HashSet<string> urls = new HashSet<string>();//防止重复
            //读取文件夹中的html文件
            string[] files = Directory.GetFiles(@"C:\Users\Li\Desktop\新建文件夹");
            foreach (string file in files)
            {
                if (file.EndsWith("html"))
                {
                    //Console.WriteLine("file: " + file);
                    StreamReader sr = new StreamReader(file);
                    string line = sr.ReadLine();
                    while (line != null)
                    {
                        //按正则表达式获取
                        //<A href="http://www.disknic.com/file/119bdb3939c18d62.html" target=_blank >
                        //<A href="http://amtnic.com/file/2e609db295309d5d.html" target=_blank >
                        //\S	匹配任意不是空白符的字符。等价于 [^ \f\n\r\t\v]。
                        //*?    重复任意次，但尽可能少重复。如 "acbacb"  正则  "a.*?b" 只会取到第一个"acb" 原本可以全
                        //      部取到但加了限定符后，只会匹配尽可能少的字符 ，而"acbacb"最少字符的结果就是"acb" 。
                        //string url = GetMatchValue(line, @"http://\S*(disknic.com|amtnic.com)\S+html");
                        List<string> url = GetMatchValues(line, @"http://\S*?(disknic.com|amtnic.com)\S+?html");
                        if (url != null)
                        {
                            //Console.WriteLine("获得href中的值：{0}", url);
                            //urls.Add(url);
                            foreach(string l in url)
                            {
                                urls.Add(l);
                            }
                        }
                        line = sr.ReadLine();
                    }
                }
            }
            //Console.ReadLine();
            List<string> failedUrls = new List<string>();
            //使用http访问，登录，下载文件
            string cookie = "PHPSESSID=uobnuq2nm5skmv0cias08pc4g4; CNZZDATA2710406=cnzz_eid%3D1847799670-1482583272-%26ntime%3D1482583272; C_user_id=2016122421504693985; C_user=701044";
            foreach (string url in urls)
            {
                //<HTML>< HEAD >
                //< META HTTP - EQUIV = "refresh" CONTENT = "0;url=http://dufile.com/file/6d92dd60e44e7092.html" ></ HEAD >
                //< BODY ></ BODY ></ HTML >
                string resp = GetHttpResponse(url, null);
                string turl = GetMatchValue(resp, @"http://\S*dufile.com\S+html");
                Console.WriteLine(turl);
                if (turl != null)
                {
                    resp = GetHttpResponse(turl, cookie);
                    //<a href="http://vip961.sufile.net:3657/down/6d92dd60e44e7092.rar?key=zUivUFazg1RSysTYyLpxXBvP2mr4bGIEzoLSDQGfFUkmK2VDqqsYo6vQMNlXHnlq400nol5L%2B%2BCUZnIdzrXLcYSoM2i0965gteiNyGnLpBQeVgTrSOUIo5%2BGtWlmfeNnifJFIaeUZlR0n2x1GvW0rEmcrdTqAig" id="downs" target="_blank">
                    string durl = GetMatchValue(resp, @"http://\S*sufile.net\S+?""");
                    Console.WriteLine(durl);
                    if(durl == null)//稍后再试一次
                    {
                        failedUrls.Add(turl);
                        Console.WriteLine("Failed: " + turl);
                        continue;
                    }
                    string fileName = GetMatchValue(durl, @"down/(\S+)\?key",1);
                    DownloadFile(durl, "C_user_id=2016122421504693985", @"E:\dufile\" + fileName);
                    Console.WriteLine("download file: " + fileName);
                }
            }

            foreach(string turl in failedUrls)
            {
                string resp = GetHttpResponse(turl, cookie);
                //<a href="http://vip961.sufile.net:3657/down/6d92dd60e44e7092.rar?key=zUivUFazg1RSysTYyLpxXBvP2mr4bGIEzoLSDQGfFUkmK2VDqqsYo6vQMNlXHnlq400nol5L%2B%2BCUZnIdzrXLcYSoM2i0965gteiNyGnLpBQeVgTrSOUIo5%2BGtWlmfeNnifJFIaeUZlR0n2x1GvW0rEmcrdTqAig" id="downs" target="_blank">
                string durl = GetMatchValue(resp, @"http://\S*sufile.net\S+?""");
                //Console.WriteLine(durl);
                if (durl == null)
                {
                    Console.WriteLine("Failed: " + turl);
                    continue;
                }
                string fileName = GetMatchValue(durl, @"down/(\S+)\?key", 1);
                DownloadFile(durl, "C_user_id=2016122421504693985", @"E:\dufile\" + fileName);
                Console.WriteLine("download file: " + fileName);
            }
        }

        static List<string> GetMatchValues(string text, string regexStr)
        {
            List<string> values = new List<string>();
            Match mt = Regex.Match(text, regexStr);
            while (mt.Success == true)
            {
                values.Add(mt.Value);
                mt = mt.NextMatch();
            }
            return values;
        }

        static string GetMatchValue(string text, string regexStr)
        {
            Match mt = Regex.Match(text, regexStr);
            if (mt.Success == true)
            {
                return mt.Value;
            }
            return null;
        }

        static string GetMatchValue(string text, string regexStr, int group)
        {
            Match mat = Regex.Match(text, regexStr);
            if (group < mat.Groups.Count)
            {
                return mat.Groups[group].Value;
            }
            return null;
        }

        static string GetHttpResponse(string url, string cookie)
        {
            WebRequest wRequest = WebRequest.Create(url);
            wRequest.Method = "GET";
            wRequest.ContentType = "text/html;charset=UTF-8";
            if (string.IsNullOrEmpty(cookie) == false)
            {
                wRequest.Headers.Add(HttpRequestHeader.Cookie, cookie);
            }
            WebResponse wResponse = wRequest.GetResponse();
            Stream stream = wResponse.GetResponseStream();
            StreamReader reader = new StreamReader(stream, System.Text.Encoding.Default);
            string str = reader.ReadToEnd();   //url返回的值  
            reader.Close();
            wResponse.Close();
            return str;
        }

        static void DownloadFile(string url, string cookie, string saveFile)
        {
            // 设置参数
            HttpWebRequest request = WebRequest.Create(url) as HttpWebRequest;
            request.Method = "GET";
            if (string.IsNullOrEmpty(cookie) == false)
            {
                request.Headers.Add(HttpRequestHeader.Cookie, cookie);
            }
            //发送请求并获取相应回应数据
            HttpWebResponse response = request.GetResponse() as HttpWebResponse;
            //直到request.GetResponse()程序才开始向目标网页发送Post请求
            Stream responseStream = response.GetResponseStream();
            //创建本地文件写入流
            Stream stream = new FileStream(saveFile, FileMode.Create);
            byte[] bArr = new byte[1024];
            int size = responseStream.Read(bArr, 0, (int)bArr.Length);
            while (size > 0)
            {
                stream.Write(bArr, 0, size);
                size = responseStream.Read(bArr, 0, (int)bArr.Length);
            }
            stream.Close();
            responseStream.Close();
        }
    }
}
