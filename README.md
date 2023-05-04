# ProTester

**Project Members:**

- Ehor Shulga
- Herman Smolar
- Julia Borovets
- Mariia Kovalenko
- Vadim Dudka
- Vladislav Tochanenko
- Volodymyr Zhuk

**Project Mentors:**

- Serhii Yablonovskyi
- Yevhenii Deineka

**Presentation (PDF)**

[ProTester Presentation](ProTester.pdf)

# How to understand and run project


**Project overview**

* There are 2 modules : **frontend** and **backend**.
* Each module contains its own _pom.xml_ files , which are responsible for creating _jar_ files.
* **Backend module** is a simple Spring Boot project with Spring Web dependency.
All dependencies connected with Spring you have to import in __pom.xml__ created in this folder.
* **Frontend** module is an _Angular_ _app_ , located in maven module.
* The main _pom.xml_ file,located in root folder, builds two separate projects in one.

**Running the project**
* After cloning repo , go to and press "_maven->ua.project.protester->lifecycle->_**install**".
First it will build _Angular_ project (and install all necessary files) and then integrate _Angular_ to _Spring Boot_ app.
        P.S. _Alternative way to run_ : go to console and run : **_mvn install_**
* After maven installing you will be able to run application as a single module,where you would have 
_Spring Boot_ and _Angular_ running on one port (8080).
* After each new version we should go to maven tools and _install_ project.

**Developing the project**
* When developing new features you have to run backend and frontend modules on two separate servers
* To correct work with two servers you should go  _"maven->ua.project.protester(root)->**clean**"._
(Because we need to build them separately).
* You have to run an _Angular_ app via the _ng serve_ command from 
"_frontend->src->main->protester-gui_".
    
    (Or "_edit configurations -> '+' -> **npm**_" )
 
      1.package.json (go "_frontend->src->main->protester-gui->package.json_" and add it to path).
      2.script -> start.
      3.Start it from Run button.
* To run a separate SpringBoot server you should go to "maven->backend->lifecycle->**install**".
* Run as SpringBootApplication from _Run_ button.

**Setup proxy**
* In "_frontend->src->main->protester-gui->src->_" **proxy.conf.json**  you can setup proxy to be able
to get information from backend services when working with frontend.

**Heroku** 

https://pro-tester.herokuapp.com/
