#How to run the dev test cases
================================
#Annotation: 
All of the test cases can't be ran on the windows platform because 
the firefox can't support the ajax and JSF based on the windows platform very well.
If you want to ran the tests, please check out all of the codes to the linux, ubuntu or
mac platform.

#Preparation and Steps:
1). Download the firefox and install it, On my platform, I have downloaded and installed 
the firefox version 19.0

2). Download the selenium IDE plugin and installed, On my platform, I have installed the 
selenium IDE 2.4.0

3). Checkout the the tests from the github(https://github.com/LvSongping/GLASSFISH_ADMIN_CONSOLE_DEVTESTS/tree/master/auto-test) 
to your hard disk.

4). Open a terminal and access to the root directory of auto-tests, Then execute the command 
as "mvn test" to run all of the tests

5). If some of the test cases are failed, you can also rerun the error or failed test cases 
using the command as "mvn test -Dtest=[ClassName]#[MethodName]" to confirm related test 
cases.(if the failure test cases passed at the second time, we can regard the failure test 
case as a passed case)

#Note:
The expected test results listed as follows:
test cases number:110
passed number:110
failed number:0
error number:0

