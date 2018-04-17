package hello.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import hello.model.Foo;

@Service
public class FooService {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Foo> findAll() { 
    	return new ArrayList<Foo>();
    }
    // ...
}