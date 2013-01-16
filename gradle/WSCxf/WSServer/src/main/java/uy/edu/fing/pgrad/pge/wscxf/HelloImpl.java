package uy.edu.fing.pgrad.pge.wscxf;

import javax.jws.WebService;

@WebService(endpointInterface = "uy.edu.fing.pgrad.pge.wscxf.Hello")
public class HelloImpl implements Hello {

    @Override
    public String sayHi(String name) {
        return "Hello " + name;
    }
}
