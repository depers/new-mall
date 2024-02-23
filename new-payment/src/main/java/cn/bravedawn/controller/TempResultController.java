package cn.bravedawn.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempResultController {

	@GetMapping("/alipayResult")
	public String alipayResult(HttpServletRequest request, HttpServletResponse response) {

		return "alipayResult";
	}
}
