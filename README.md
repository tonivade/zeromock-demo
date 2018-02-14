# zeromock-demo

Mock Http Server demo.

In the beginning the intention was create a mock library for http services, to implement integration tests for REST clients but in the end, I have implemented an entire REST API framework.

The reason for implement a new http mock library was simple. The existing alternatives are to complex and dificult to read, in my opinion.

```java
  HttpService service1 = new HttpService("hello")
      .when(get().and(path("/hello")).and(param("name")), ok(this::helloWorld))
      .when(get().and(path("/hello")).and(param("name").negate()), badRequest("missing parameter name"));

  HttpService service2 = new HttpService("test")
      .when(get().and(path("/test")).and(acceptsXml()), ok("<body/>").andThen(contentXml()))
      .when(get().and(path("/test")).and(acceptsJson()), contentJson().compose(ok("{ }")));
  
  MockHttpServer server = listenAt(8080).mount("/path", service1.combine(service2));

```

The API is implemented using functions and chaining them using function composition. I think is clear and easy to read.

So, this project is an POC of a rest API implemented using my mock library. Right now, obviously, it's not production ready, but I'm very exited about this project. You don't need a servlet container, it's not coupled with nothing, you can integrate it wherever you want, spring, spring-boot, guice, even java ee.
