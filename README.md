- # General
    - #### Team#: 161
    
    - #### Names: Gen Pei
    
    - #### Project 5 Video Demo Link: https://www.youtube.com/watch?v=QgeYzN3phlk

    - #### Instruction of deployment:
    1. First clone whole project from github use command line or use git (local machine).
    2. Use Maven create war package (at where xml file locate).
    3. Copy war file to correct location (tomcat webapps). On local machine need to specify where it is by using IntelliJ.
    4. Show tomcat web apps by using http://<AWS public IP>:8080/manager/html or command line. On local machine just run it through IntelliJ.
    5. Since we did not use any other framework, we don't neet to specify it.

    - #### Collaborations and Work Distribution:
      Gen Pei: All tasks


- # Connection Pooling
    - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
    Thesis: Basically, I just change almost all of my queries to prepared statement, and add some prepareCall statement (stored procedure). Then I enabled JDBC connection pooling for all servlets which involves jdbc connection.
    link to file:
    
    Single-Version
    
    AddMovie.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp/src/AddMovie.java
    
    CheckOut.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp/src/CheckOut.java
    
    Confirmation.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp/src/Confirmation.java
    
    DashBoard.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp/src/DashBoard.java
    
    HeroSuggestion.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp/src/HeroSuggestion.java
    
    LoginServlet.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp/src/LoginServlet.java
    
    MovieList.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp/src/MovieList.java
    
    SearchPage.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp/src/SearchPage.java
    
    ShowCart.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp/src/ShowCart.java
    
    SingleMovie.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp/src/SingleMovie.java
    
    SingleStarServlet.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp/src/SingleStarServlet.java
    
    Scaled-Version
    
    AddMovie.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp-scaled/src/AddMovie.java
    
    CheckOut.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp-scaled/src/CheckOut.java
    
    Confirmation.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp-scaled/src/Confirmation.java
    
    DashBoard.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp-scaled/src/DashBoard.java
    
    HeroSuggestion.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp-scaled/src/HeroSuggestion.java
    
    LoginServlet.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp-scaled/src/LoginServlet.java
    
    MovieList.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp-scaled/src/MovieList.java
    
    SearchPage.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp-scaled/src/SearchPage.java
    
    ShowCart.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp-scaled/src/ShowCart.java
    
    SingleMovie.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp-scaled/src/SingleMovie.java
    
    SingleStarServlet.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp-scaled/src/SingleStarServlet.java
    
    - #### Explain how Connection Pooling is utilized in the Fabflix code.
    In my Fablix, there are a lot of servlets involves JDBC connection, by using Connection Pooling, it would save the time cost of establish connection once by once. Since user may search movies really frequently, reuse those connections is an efficient method.
    
    - #### Explain how Connection Pooling works with two backend SQL.
    In my Fablix, if the connection is for writing purpose, it will be re-directed to the sql on the master instance, but if it for reading purpose, it will read data from the local instance (where the request be re-directed, could be master or slave).
    

- # Master/Slave
    - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.
    AddMovie.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp-scaled/src/AddMovie.java
    Confirmation.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp-scaled/src/Confirmation.java
    DashBoard.java: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/webapp-scaled/src/DashBoard.java


    - #### How read/write requests were routed to Master/Slave SQL?
    So user send request to balancer with port 80, balancer will randomly choose master or slave if the request is a read request. Otherwise, it will goes to master instance.
    

- # JMeter TS/TJ Time Logs
    - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.
    I wrote the log_processing using JAVA, it will parse the time_used.txt (should locate at the root of project folder) and calculate Total/Average TJ and TS.


- # JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](img/1-1.png)             | 442                        | 175.3                               | 176.03                    | Connection is not stable in the beginning, but after few minutes, it became stable. The average time cost is low since we only have 1 thread.           |
| Case 2: HTTP/10 threads                        | ![](img/1-2.png)             | 1799                       | 1586.3                              | 1586.0                    | Graph looks pretty stable, average time cost is long           |
| Case 3: HTTPS/10 threads                       | ![](img/1-3.png)             | 1789                       | 1682.2                              | 1681.8                    | Graph is not stable at all, I guess the reason is re-direction. The average time cost is long.           |
| Case 4: HTTP/10 threads/No connection pooling  | ![](img/1-4.png)             | 1778                       | 1564.4                              | 1564.0                    | Graph looks pretty stable, average time cost is long. Even I did not enable pooling, the time cost seems not that different.           |

| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](img/2-1.png)             | 285                        | 173.3                               | 172.6                     | Graph looks pretty stable, average time cost is low, I guess the reason is there are 2 instance and pooling is enabled.           |
| Case 2: HTTP/10 threads                        | ![](img/2-2.png)             | 895                        | 759.6                               | 759.2                     | Connection is not stable in the beginning, but after few seconds, it became stable. The average time cost is longer than above because we have 10 threads, but much shorter than single-version.          |
| Case 3: HTTP/10 threads/No connection pooling  | ![](img/2-3.png)             | 902                        | 804.7                               | 804.0                     | With out pooling, the time cost became longer, but still shorter than single version.           |

- # Logs file
Single-instance

Case 1: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/tree/master/logs

Case 2: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/logs/single_http_10_Pool_time_used.txt

Case 3: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/logs/single_https_10_Pool_time_used.txt

Case 4: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/logs/single_http_10_noPool_time_used.txt

Scaled Version

Case 1: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/logs/scaled_http_1_Pool_time_used.txt

Case 2: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/logs/scaled_http_10_time_used.txt

Case 3: https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-161/blob/master/logs/scaled_http_10_noPool_time_used.txt


- # Filters and https
For testing purpose, I disabled filters for both webapp and webapp-scaled, comment out HTTPSonly for webapp-scaled.