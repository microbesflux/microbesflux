from django.core.files.storage import FileSystemStorage
from django.http import HttpResponse
from flux.models import Task

########### Helper functions ########### 
import sys, smtplib
from email.MIMEText import MIMEText
from email.MIMEMultipart import MIMEMultipart
from django.http import HttpResponse

COMMASPACE = ', '
def send_mail(address, attachments, title = ""):
    msg = MIMEMultipart()
    msg['Subject'] = 'Mail from MicrobesFlux --' + title
    msg['From'] = 'tanglab@seas.wustl.edu'
    msg['To'] = address
    
    fromaddr = "tanglab@seas.wustl.edu"
    toaddrs = [address, ] 
    content = MIMEText("Dear MicrobesFlux User:  Thank you for using our website. -- MicrobesFlux")
    msg.attach(content)
    fs = FileSystemStorage()
    for fname in attachments:
        fp = fs.open(fname, "rb")
        content = MIMEText(fp.read())
        content.add_header('Content-Disposition', 'attachment; filename="'+fname+'"')
        fp.close()
        msg.attach(content)

    server = smtplib.SMTP('localhost')
    server.sendmail(fromaddr, toaddrs, msg.as_string())
    server.quit()


def generate_report(name, suffix):
    fs = FileSystemStorage()
    
    amplfile   = name + ".ampl"
    amplresult = name + suffix + "_result.txt"
    mapresult  = name + ".map"
    
    # Step 0. Write the report file
    finaloutput = fs.open(name + suffix + "_report.txt", "w") 

    #### Step 0.1 Read the variable correspondence
    fmap  = fs.open(mapresult, "r")
    d = {}
    for l in fmap:
        vname, oldname = l.split()[:2]
        d[vname]  = oldname
    fmap.close()

    ### Step 1: transfer _header to _report 
    fheader = fs.open(name + "_header.txt", "r")
    for l in fheader:
        finaloutput.write(l)
    fheader.close()

    ### Step 2: transfer ampl to report
    fampl = fs.open(amplfile, "r")
    for l in fampl:
        finaloutput.write(l)
    fampl.close()
    
    ### Step 3: conversions 
    finaloutput.write("\n\n ===  Name conversions between variables and fluxes === \n")
    fmap = fs.open(mapresult, "r")
    for l in fmap:
        finaloutput.write(l)
    fmap.close()
    
    finaloutput.write("\n\n======== Results ========= \n")
    
    fampl_result = fs.open(amplresult, "r")
    fl = fampl_result.xreadlines()
    for l in fl:
        print "Read l is", l
        
        temp = l.split()
        print " temp is ", temp
        if len(temp) == 2 and l[0]=="V":
            name, value = l.split()
            finaloutput.write(d[name])
            finaloutput.write( "\t -> \t ")
            finaloutput.write( value)
            finaloutput.write( "\n" )
        else:
            finaloutput.write(l)
    fampl_result.close()
    finaloutput.close()

########## Methods for external use ####
def task_list(request):
    all_task = Task.objects.all()
    total = len(all_task)
    if total > 50:
        all_task = all_task[total-50:]  # take last 50
    l = []
    for t in all_task:
        l.append(str(t))
    return HttpResponse(content = '\n'.join(l), status = 200, content_type = "text/html")

def task_remove(request):
    tid = request.GET['tid']
    try:
        to_remove = Task.objects.get(task_id = tid)
        to_remove.delete()
        return HttpResponse(content = "Task Removed", status = 200, content_type = "text/html")
    except Task.DoesNotExist:
        return HttpResponse(content = "No such task", status = 200, content_type = "text/html")

def task_add(request):
    t_type = request.GET['type']
    t_file = request.GET['task']
    t_email = request.GET['email']
    t_addif = ""
    if request.GET.has_key('file'):
        t_addif = request.GET['file']
    t = Task(task_type = t_type, main_file = t_file, email = t_email, additional_file = t_addif, status = "TODO")
    t.save()
    return HttpResponse(content = """ Task added  """, status = 200, content_type = "text/html")

def task_mark(request):
    tid = request.GET['tid']
    try:
        to_mark = Task.objects.get(task_id = tid)
        to_mark.status = "Enqueue"
        # TODO: send out a piece of email, saying it is enqueued
        to_mark.save()
        return HttpResponse(content = "Task Marked", status = 200, content_type = "text/html")
    except Task.DoesNotExist:
        return HttpResponse(content = "No such task", status = 200, content_type = "text/html")

def task_mail(request):
    tid = request.GET['tid']
    try:
        to_mail = Task.objects.get(task_id = tid)
        newf = to_mail.main_file.split(".")[0]  # take the base name
        address = to_mail.email
        if to_mail.task_type == "fba":
            report_file = newf + "_fba_report.txt"
            generate_report(newf, "_fba")
            send_mail(address, [report_file,], title = "FBA")    
        elif to_mail.task_type == "dfba":
            report_file = newf + "_dfba_report.txt"
            generate_report(newf, "_dfba")
            send_mail(address, [report_file,], title = "dFBA")
        else:
            svgfile = newf + "_plot.svg"
            send_mail(address, [svgfile,], title = "SVG")
        to_mail.delete()
        return HttpResponse(content = """ Mail sent """, status = 200, content_type = "text/html")
    except Task.DoesNotExist:
        return HttpResponse(content = "No such task", status = 200, content_type = "text/html")
