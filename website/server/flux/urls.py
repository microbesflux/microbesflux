from django.conf.urls.defaults import *
from django.core.management import setup_environ
from django.contrib import auth

#from view.pathway_view import pathway_add
from view.pathway_view import pathway_add_check
from view.pathway_view import pathway_add
from view.pathway_view import pathway_update
from view.pathway_view import pathway_info
from view.pathway_view import pathway_fetch

# Optimization model
from view.model_view import user_obj_fetch
from view.model_view import user_obj_update
from view.model_view import sv_fetch
from view.model_view import user_bound_fetch
from view.model_view import user_bound_update
from view.model_view import optimization
from view.model_view import dfba_solve
from view.model_view import file_upload
from view.model_view import svg
from view.model_view import sbml
from view.model_view import test_ssh

# User/Password related
from view.user_view import index_page
from view.user_view import user_login
from view.user_view import user_add
from view.user_view import user_logout
from view.user_view import index_page
from view.user_view import user_summary
from view.user_view import user_password_retrieve
from view.user_view import user_password_change

# Related to a collection (model+pathway+userfile)
from view.collection_view import collection_save
from view.collection_view import collection_select # not in JSONview
from view.collection_view import collection_create # not in JSONview
from view.collection_view import collection_rename # not in JSONview
from view.collection_view import collection_summary
from view.collection_view import collection_saveas
from view.collection_view import collection_info

from task.task import task_list
from task.task import task_add
from task.task import task_remove
from task.task import task_mail
from task.task import task_mark

# from debug import test_mail
# from jsonview import file_upload
# from jsonview import hello
"""
#from auth import login
#from auth import logout
#from auth import index
"""
urlpatterns = patterns('',
    (r'^pathway/add/', pathway_add),        # Test covered
    (r'^pathway/update/', pathway_update),  # Test covered
    (r'^pathway/stat/', pathway_info),      # Test covered
    (r'^pathway/fetch/', pathway_fetch),    # Test covered
    (r'^pathway/check/', pathway_add_check),    # Going to cover
    (r'^model/objective/fetch/', user_obj_fetch),   # Test covered
    (r'^model/objective/update/', user_obj_update), # Test covered
    (r'^model/bound/fetch/', user_bound_fetch),
    (r'^model/bound/update/', user_bound_update),
    (r'^model/sv/fetch/', sv_fetch),
    (r'^model/optimization/', optimization),
    (r'^model/dfba/', dfba_solve),
    (r'^model/svg/', svg),
    (r'^model/scp/', test_ssh),
    (r'^model/sbml/', sbml),
    (r'^user/login/', user_login),
    (r'^user/logout/', user_logout),
    (r'^user/add/', user_add),
    (r'^user/summary/', user_summary),
    (r'^user/password/retrieve/', user_password_retrieve),
    (r'^user/password/change/', user_password_change),
    (r'^$', index_page),
    (r'^gwt/(?P<path>.*)$', 'django.views.static.serve', {'document_root': '/Users/youxu/Coding/Workspace/KeggSVN/war/'}),
    (r'^model/upload/', file_upload),
    (r'^collection/list/', collection_summary),
    (r'^collection/select/', collection_select),    # Test covered
    (r'^collection/create/', collection_create),    # Test covered
    (r'^collection/saveas/', collection_saveas),
    (r'^collection/rename/', collection_rename),
    (r'^collection/save/', collection_save),        # Test covered
    (r'^collection/stat/', collection_info),
    (r'^task/list/', task_list),
    (r'^task/add/', task_add),
    (r'^task/remove/', task_remove),
    (r'^task/mark/', task_mark),
    (r'^task/mail/', task_mail),
    # (r'^test/testmail/', test_mail),
)
