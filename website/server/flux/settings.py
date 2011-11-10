ADMINS = (
    ('Eric Xu', 'youxu@wustl.edu'),
)

MANAGERS = ADMINS

# CHANGE THE LINE BELOW
DATABASE_ENGINE = 'mysql'           
DATABASE_NAME = 'your_db_name'             # Or path to database file if using sqlite3.
DATABASE_USER = 'your_db_user'             # Not used with sqlite3.
DATABASE_PASSWORD = 'your_db_pass'         # Not used with sqlite3.
DATABASE_HOST = 'your_db_host'             # Set to empty string for localhost. Not used with sqlite3.
DATABASE_PORT = ''             # Set to empty string for default. Not used with sqlite3.

TIME_ZONE = 'America/Chicago'

LANGUAGE_CODE = 'en-us'

SITE_ID = 1
USE_I18N = True

# Absolute path to the directory that holds media.
# Example: "/home/media/media.lawrence.com/"
# MEDIA_ROOT = '/home/eric/keggdfba/flux/gwt/'
MEDIA_ROOT = '/research-www/engineering/tanglab/flux/temp/'
# URL that handles the media served from MEDIA_ROOT. Make sure to use a
# trailing slash if there is a path component (optional in other cases).
# Examples: "http://media.lawrence.com", "http://example.com/media/"
# STATIC_DOC_ROOT = '/home/eric/keggdfba/flux/gwt/'
# MEDIA_URL = '/gwt/'

# URL prefix for admin media -- CSS, JavaScript and images. Make sure to use a
# trailing slash.
# Examples: "http://foo.com/media/", "/media/".
ADMIN_MEDIA_PREFIX = '/media/'

# Make this unique, and don't share it with anybody.

# CHANGE THE LINE BELOW
SECRET_KEY = 'your_secrt_key'

# List of callables that know how to import templates from various sources.
TEMPLATE_LOADERS = (
    'django.template.loaders.filesystem.load_template_source',
    'django.template.loaders.app_directories.load_template_source',
#     'django.template.loaders.eggs.load_template_source',
)

MIDDLEWARE_CLASSES = (
    'django.middleware.common.CommonMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
)

ROOT_URLCONF = 'flux.urls'

TEMPLATE_DIRS = (
    # Put strings here, like "/home/html/django_templates" or "C:/www/django/templates".
    # Always use forward slashes, even on Windows.
    # Don't forget to use absolute paths, not relative paths.
)

INSTALLED_APPS = (
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.sites',
    'django.contrib.admin',
    'flux',
)

SESSION_ENGINE = 'django.contrib.sessions.backends.file'
# CHANGE THE LINE BELOW
SESSION_FILE_PATH = '/research-www/engineering/tanglab/flux/temp/'
LOGIN_URL = '/'
