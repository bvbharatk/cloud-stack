using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Deployment.WindowsInstaller;
using System.Management.Automation.Runspaces;
using System.Management.Automation;
using System.Collections.ObjectModel;
using System.Text.RegularExpressions;
using System.Reflection;
using System.Runtime.InteropServices;

namespace ACS
{
    public class CustomActions
    {
        static String cn = "ApacheCloudStack";
        static string appId = "\"{727beb1c-6e7c-49b2-8fbd-f03dbe481b08}\"";

        [CustomAction]
        public static ActionResult InstallCertificate(Session session)
        {
            session.Log(" Adding certificate to Port ");
            String showSslcertCmd = "netsh http show sslcert 0.0.0.0:8250";
            String resultsslCertCmd = executePowerShellScript(showSslcertCmd);
            if (resultsslCertCmd.Contains("0.0.0.0:8250"))
            {
                ShowErrorMessage(" Certificate is already added to port. Please retry after unchecking the checkbox (create and add certificate to port) or delete the certificate from port ", session);
                return ActionResult.Failure;
            }
            session.Log("Verifying certificate exists or not");
            string checkCertificateExistsCmd = "[bool](dir cert:\\LocalMachine\\My\\ | ? { $_.subject -like \"cn=" + cn + "\" })";
            Boolean IsCertificateExists = false;
            try
            {
                IsCertificateExists = Convert.ToBoolean(executePowerShellScript(checkCertificateExistsCmd));
            }
            catch (InvalidCastException)
            {
                ShowErrorMessage("Checking certificate existence returns invalid response. Please retry after unchecking the checkbox (create and add certificate to port) and add the certificate manually if not already added", session);
                return ActionResult.SkipRemainingActions;
            }

            string thumbprint = null;
            if (!IsCertificateExists)
            {
                session.Log("Generating new Certificate ");
                string createCertificateCmd = "New-SelfSignedCertificate -DnsName " + cn + " -CertStoreLocation Cert:\\LocalMachine\\My";
                thumbprint = getThumbprint(executePowerShellScript(createCertificateCmd));
            }
            else
            {
                session.Log("Certificate with cn=" + cn + " already exists, we are using the same");
                String getThumbprintCmd = "Get-ChildItem -Path Cert:\\LocalMachine\\My -DnsName " + cn + "";
                thumbprint = getThumbprint(executePowerShellScript(getThumbprintCmd));
            }

            string addCertToPortCmd = "netsh http add sslcert ipport=0.0.0.0:8250 certhash=" + thumbprint + " appid=" + appId;
            String addCertToPortCmdResult = executePowerShellScript(addCertToPortCmd);
            if (addCertToPortCmdResult.Contains("failed"))
            {
                ShowErrorMessage("Adding certificate to port returns invalid response. Please retry after unchecking the checkbox (create and add certificate to port) and add the certificate manually if not already added", session);
                return ActionResult.SkipRemainingActions;
            }

            return ActionResult.Success;
        }

        private static void ShowErrorMessage(string error, Session session)
        {
            Record record = new Record();
            record.FormatString = string.Format(error);
            session.Log(error);
            session.Message(
                InstallMessage.Error, record);
        }

        [CustomAction]
        public static ActionResult RemoveCertificate(Session session)
        {
            string deleteSslCertCmd = "netsh http delete sslcert ipport=0.0.0.0:8250";
            string deleteSslCertCmdResult = executePowerShellScript(deleteSslCertCmd);
            if (deleteSslCertCmdResult.Contains("successfully"))
            {
                return ActionResult.Success;
            }
            else
            {
                return ActionResult.Failure;
            }
        }

        private static string getThumbprint(string text)
        {
            string[] lines = Regex.Split(text, "\r\n");
            return Regex.Split(lines[7], " ")[0];
        }


        private static String executePowerShellScript(String scriptText)
        {
            Runspace runspace = RunspaceFactory.CreateRunspace();
            runspace.Open();
            Pipeline pipeline = runspace.CreatePipeline();
            pipeline.Commands.AddScript(scriptText);
            pipeline.Commands.Add("Out-String");
            Collection<PSObject> results = pipeline.Invoke();
            runspace.Close();
            StringBuilder stringBuilder = new StringBuilder();
            foreach (PSObject obj in results)
            {
                stringBuilder.AppendLine(obj.ToString());
            }
            return stringBuilder.ToString();
        }
    }
}