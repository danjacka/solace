# solace

Start server with `lein run`:

```
$ lein run
2015-02-20 21:25:41.031:INFO:oejs.Server:jetty-7.6.8.v20121106
2015-02-20 21:25:41.061:INFO:oejs.AbstractConnector:Started SelectChannelConnector@0.0.0.0:5000
```

Send your mood like this:

```
$ curl -v --data "mood=5" http://localhost:5000/
* Hostname was NOT found in DNS cache
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 5000 (#0)
> POST / HTTP/1.1
> User-Agent: curl/7.37.1
> Host: localhost:5000
> Accept: */*
> Content-Length: 6
> Content-Type: application/x-www-form-urlencoded
> 
* upload completely sent off: 6 out of 6 bytes
< HTTP/1.1 201 Created
< Date: Thu, 19 Mar 2015 08:13:56 GMT
< Content-Type: text/plain;charset=ISO-8859-1
< Content-Length: 7
* Server Jetty(7.6.8.v20121106) is not blacklisted
< Server: Jetty(7.6.8.v20121106)
< 
* Connection #0 to host localhost left intact
Created
```