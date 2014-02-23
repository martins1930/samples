package com.github.martins1930.intelproj.ui;

import com.github.martins1930.intelproj.service.Name;
import com.github.martins1930.intelproj.types.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by martin on 2/11/14.
 */
@Controller
@RequestMapping(value = "/")
public class FacadeController {

    @Autowired
    private Name name;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("home", "Welcome from spring!!");
        return "index";
    }

    @RequestMapping(value = "/element", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getElement(){
        return "{ \"name\" : \"pepe\" }";
    }

    @RequestMapping(value = "/person", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Person obtainPerson(){
        final Person person = new Person();
        person.setName(name.getName());
        person.setAge(12);
        return person;
    }
}
