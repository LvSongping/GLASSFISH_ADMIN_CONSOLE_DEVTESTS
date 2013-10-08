#How to run the dev test cases
================================
#Annotation: 
All of the test cases can't be ran on the windows platform because 
the firefox can't support the ajax and JSF based on the windows platform very well.
If you want to ran the tests, please check out all of the codes to the linux, ubuntu or
mac platform.

#Preparation and Steps:
1). Download the firefox and install it, On my platform, I have downloaded and installed 
the firefox revision 19.0

2). Download the selenium IDE plugin and installed, On my platform, I have installed the 
selenium IDE 2.4.0

3). Checkout all of the latest version of glassfish source(https://svn.java.net/svn/glassfish~svn/trunk/main), 
This is because the tests will be contribute to the glassfish team in the future if they need. 

4). Checkout the the tests from the github(https://github.com/LvSongping/GLASSFISH_ADMIN_CONSOLE_DEVTESTS/tree/master/auto-test) 
and put the all of the codes under /GF_SOURCE/appserv/admingui.

5). Open a terminal and access to the directory of /GF_SOURCE/appserv/admingui/auto-tests, Then 
execute the command as "mvn eclipse:eclipse -DeclipseResource=true" to build the project as an
eclipse project.(I use the eclipse IDE to develop the code)

6). Import the java project to the eclipse and then you can run all of the tests as you wish.
