package com.github.martins1930.intelproj.service.impl;

import com.github.martins1930.intelproj.service.Name;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 2/12/14.
 */
@Component
public class NameImpl implements Name {

    @Override
    public String getName() {
        return "Hiii";
    }

}
