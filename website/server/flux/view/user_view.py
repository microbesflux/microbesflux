from django.contrib.auth.decorators import login_required
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.models import User
from django.contrib.auth.views import password_reset, password_change, password_change_done

from django.contrib.auth.tokens import PasswordResetTokenGenerator

from django.http import HttpResponse
from view.json import Json
from view.foundations import *

from email.MIMEText import MIMEText
from email.MIMEMultipart import MIMEMultipart
import smtplib, cPickle

from django.http import HttpResponse
from flux.models import Profile

def user_login(request):
    username = request.POST['username']
    password = request.POST['password']
    user = authenticate(username = username, password = password)
    if user is not None:
        if user.is_active:
            login(request, user)
            request.session['provided_email'] = username
            return HttpResponse(content = """Successfully Login """ + request.session.session_key, status = 200, content_type = "text/html")
        else:
            return HttpResponse(content = """Inactive Login""", status = 200, content_type = "text/html")
    else:
        return HttpResponse(content = """Wrong name/password Login""", status = 200, content_type = "text/html")

def user_password_retrieve(request):
    username = request.GET['username_forgot']
    u = User.objects.get(email = username)
    token_generator = PasswordResetTokenGenerator()
    
    token = token_generator.make_token(u)
    u.set_password(token)
    u.save()
    
    msg = MIMEMultipart()
    msg['Subject'] = 'Mail from the MicrobesFlux -- Password reset'
    msg['From'] = 'tanglab@seas.wustl.edu'
    msg['To'] = username
    msg.preamble = 'Reset your password'
    fromaddr = "tanglab@seas.wustl.edu"
    toaddrs = [username, ]
    content = MIMEText("Dear MicrobesFlux User:  we have changed your password to " + token + ". -- MicrobesFlux")
    msg.attach(content)
    server = smtplib.SMTP('localhost')
    server.sendmail(fromaddr, toaddrs, msg.as_string())
    server.quit()
    return HttpResponse(content = """New Email sent!""", status = 200, content_type = "text/html")
    
def user_add(request):
    uname = request.POST['username']
    pwd = request.POST['password']
    exist = True
    try:
        user = User.objects.get(email=uname)
    except User.DoesNotExist:
        user = User.objects.create_user(username = uname, email = uname, password = pwd)
        user.save()
        exist = False
    if not exist:    
        return HttpResponse(content = """Successfully added""", status = 200, content_type = "text/html")
    else:
        return HttpResponse(content = """Email exist""", status = 200, content_type = "text/html")
        
@login_required
def user_password_change(request):
    uname = request.user.username
    password = request.POST['newpassword']
    try:
        u = User.objects.get(username__exact = uname)
        u.set_password(password)
        u.save()
        return HttpResponse(content = """Password changed successfully""", status = 200, content_type = "text/html")
    except User.DoesNotExist:
        return HttpResponse(content = """Cannot change password""", status = 200, content_type = "text/html")


@login_required
def user_logout(request):
    logout(request)
    return HttpResponse(content = """Logout successfully""", status = 200, content_type = "text/html")

def index_page(request):
    return HttpResponse(content = """<html><body> <p> A simple login page <br/> try eric 123 </p>
    <form name="input" action="user/login/" method="post">
        Username:  <input type="text" name="username" /><br />
        Password: <input type="password" name="password" />
        <input type="submit" value="Submit" />
    </form>
    </body>
    </html>
    """, status = 200, content_type = "text/html")


#### TODO: revise this to the new Ajax API standard
@login_required
def user_summary(request):
    u = request.user
    plist = Profile.objects.filter(user = u, status="submitted")
    result = ""
    r = Json("array")
    for p in plist:
        ri = Json("object")
        ri.add_pair("date", str(p.submitted_date))
        ri.add_pair("model", str(p.name))
        ri.add_pair("type", str(p.model_type))
        ri.add_pair("status", str(p.status))
        r.add_item(ri)
    
    result += "{ response:     { status : 0, startRows: 0 , endRow: "
    if (len(plist) > 0):
        result += str(len(plist) - 1)
    else:
        result += "0"
    result += " , totalRows: "
    result += str(len(plist))
    result += " , data :"
    result += repr(r)
    result += "} } "
    q = request.GET
    nq = dict(q)
    result = get_callback(nq) + "(" + result + ")"
    return HttpResponse(content = result, status = 200, content_type = "text/html")
