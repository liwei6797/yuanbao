using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Diagnostics;
using System.IO;
using System.Threading;

namespace CmdTimer
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void btnBrowse_Click(object sender, EventArgs e)
        {

            openFileDialog1.Filter = "bat文件|*.bat";
            if (DialogResult.OK == openFileDialog1.ShowDialog())
            {
                textBox1.Text = openFileDialog1.FileName;
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            timer1.Stop();
            RunCmd(textBox1.Text.Trim(), false, true);
            
            timer1.Interval = Convert.ToInt32(textBox2.Text.Trim()) * 1000;
            timer1.Start();
            HideMainForm();
        }

        int times = 0;
        protected void RunCmd(String cmd, Boolean showWindow, Boolean waitForExit)
        {
            var p = new Process();
            var si = new ProcessStartInfo();
            var path = Environment.SystemDirectory;
            path = Path.Combine(path, @"cmd.exe");
            si.FileName = path;
            if (!cmd.StartsWith(@"/")) cmd = @"/c " + cmd;
            si.Arguments = cmd;
            si.UseShellExecute = false;
            si.CreateNoWindow = !showWindow;
            si.RedirectStandardOutput = true;
            si.RedirectStandardError = true;
            p.StartInfo = si;

            p.Start();
            if (waitForExit)
            {
                p.WaitForExit();

                var str = p.StandardOutput.ReadToEnd();
                if (!String.IsNullOrEmpty(str))
                {
                    Console.WriteLine(str.Trim(new Char[] { '\r', '\n', '\t' }).Trim());
                }
                str = p.StandardError.ReadToEnd();
                if (!String.IsNullOrEmpty(str))
                {
                    Console.WriteLine(str.Trim(new Char[] { '\r', '\n', '\t' }).Trim());
                }
            }
            times++;
            label1.Text = "已执行次数:" + times;
        }

        private void ShowMainForm()
        {
            this.Visible = true;                            // 窗体可见
            this.WindowState = FormWindowState.Normal;      // 窗体状态为正常
            TrayNotifyIcon.Visible = true;                  // 托盘图标可见
            this.ShowInTaskbar = true;                      // 在任务栏显示窗体
        }

        // 隐藏窗体和任务栏图标
        private void HideMainForm()
        {
            this.WindowState = FormWindowState.Minimized;   // 最小化时隐藏窗体
            this.Visible = false;                           // 窗体不可见
            this.TrayNotifyIcon.Visible = true;             // 图标在任务栏区域可见
            this.ShowInTaskbar = false;                     // 不在在任务栏显示窗体
        }

        private void TrayNotifyIcon_MouseClick(object sender, MouseEventArgs e)
        {
            // 判断是否单击鼠标左键
            if (e.Button == MouseButtons.Left)
            {
                // 切换显示状态
                if (this.WindowState == FormWindowState.Minimized)
                {
                    ShowMainForm();
                }
                else
                {
                    HideMainForm();
                }
            }

        }


        private void Form1_Deactivate(object sender, EventArgs e)
        {
            // 判断是否为最小化状态
            if (this.WindowState == FormWindowState.Minimized)
            {
                HideMainForm();
            }
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            RunCmd(textBox1.Text.Trim(), false, true);
        }

        private void button3_Click(object sender, EventArgs e)
        {
            timer1.Stop();
        }



    }
}
