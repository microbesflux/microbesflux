import sys, smtplib
from email.MIMEText import MIMEText
from email.MIMEMultipart import MIMEMultipart
from django.http import HttpResponse

COMMASPACE = ', '

def test_mail(request):
    msg = MIMEMultipart()
    msg['Subject'] = 'Test Multipart from tanglab'
    msg['From'] = 'tanglab@seas.wustl.edu'
    msg['To'] = 'xu.mathena@gmail.com,xueyang.feng@gmail.com'
    msg.preamble = 'This is a test message again'
    fromaddr = "xu.mathena@gmail.com"
    toaddrs  = ['xu.mathena@gmail.com', 'xueyang.feng@gmail.com']
    
    content = MIMEText("Dear Tanglab User:  Attached are the AMPL and SBML files we generated for you. -- Tanglab")
    msg.attach(content)

    fp = open('/research-www/engineering/tanglab/flux/temp/rak1.ampl', 'rb')
    ampl = MIMEText(fp.read())
    ampl.add_header('Content-Disposition', 'attachment; filename="rak1.ampl"')
    fp.close()
    msg.attach(ampl)

    fp = open('/research-www/engineering/tanglab/flux/temp/ilo1.sbml', 'rb')
    sbml = MIMEText(fp.read())
    sbml.add_header('Content-Disposition', 'attachment; filename="ilo1.sbml"')
    fp.close()
    msg.attach(sbml)
    
    server = smtplib.SMTP('localhost')
    server.sendmail(fromaddr, toaddrs, msg.as_string())
    server.quit()
    return HttpResponse(content = " Even New Email sent. ", status = 200, content_type = "text/html")

if __name__ == "__main__":
    test_mail(None)
