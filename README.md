# multi-feign
each feign client has a separate httpclient/okhttp

### usage guide
1. create an interface, then annotated with @MultiFeignClient   
2. register client    
2.1 register a customized okhttp client(if you use ClientPoolTypeEnum.OK_HTTP in @MultiFeignClient) in Spring   
2.2 register a customized http client(if you use ClientPoolTypeEnum.HTTP_CLIENT in @MultiFeignClient) in Spring       
3. using @RequestLine to annotate method   

