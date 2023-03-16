Installation and running
---
<h4>Tools</h4>
* JDK 17 <br/>
* Gradle 7.6 <br/>

Troubleshooting:
---
Websocket issue
- Invalid Status code=403 text=Forbidden
- Unable to establish websocket connection to http://localhost:'PORT'/devtools
---
Solution - 1:
- Add dependency `'org.seleniumhq.selenium:selenium-http-jdk-client:4.8.1'`
- Add `System.setProperty("webdriver.http.factory", "jdk-http-client");`

Solution - 2:
- Add options for driver `options.addArguments("--remote-allow-origins=*");`
