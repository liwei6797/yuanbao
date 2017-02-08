using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Linq;
using System.ServiceProcess;
using System.Text;
using System.Threading.Tasks;

namespace OvpnService
{
    public partial class OpenVPNClient : ServiceBase
    {
        Process proc;
        public OpenVPNClient()
        {
            InitializeComponent();
        }

        protected override void OnStart(string[] args)
        {
            proc = Process.Start("openvpn-gui.exe", "--connect client.ovpn");
        }

        protected override void OnStop()
        {
            //if (proc != null)
            //{
            //    proc.Kill();
            //}
        }
    }
}
