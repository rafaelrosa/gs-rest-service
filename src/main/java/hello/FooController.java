package hello;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FooController {

	@RequestMapping(value="/foo")
	public void someControllerMethod(HttpServletRequest request) {
	    request.isUserInRole("someAuthority");
	}

}
