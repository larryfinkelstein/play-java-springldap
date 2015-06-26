This is your new Play application
=================================

This README file will be packaged with your application, when using `activator dist`.

Testing Locally with in-Memory LDAP Browser
===========================================

c:\java\unboundid-ldapsdk-2.3.8-se\tools\in-memory-directory-server.bat ^
--baseDN "dc=example,dc=com" --port 389 ^
--ldifFile ^
C:\Users\larryf\Documents\Workspace\Sandbox\play-java-springldap\test\resources\example.ldif ^
--additionalBindDN "cn=Directory Manager" --additionalBindPassword "Password"

