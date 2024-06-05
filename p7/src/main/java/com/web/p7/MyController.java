package com.web.p7;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
@Controller
public class MyController {
 @Autowired
 private memberRep mrep;
 
 @GetMapping("/login")
 public String login() {
	 return "login";
 }
 @GetMapping("/member")
 public String member() { 
	 return "member";
 }
 @GetMapping("/member/insert")
 public String memberInsert(@RequestParam("id") String id,
		 					@RequestParam("pw") String pw,
		 					@RequestParam("name") String name,
		 					@RequestParam("phone") String phone, RedirectAttributes re) {
 if( mrep.existsById(id) ) {
	 re.addAttribute("msg", id+"는 이미 사용되고 있는 아이디입니다.");
	 re.addAttribute("url", "back");
 }
 else {
	 member m = new member();
	 m.id = id; m.pw = pw; m.name = name; m.phone = phone; m.mileage = 0;
	 mrep.save(m);
	 
	 re.addAttribute("msg", id+"님, 반갑습니다!! (로그인 화면으로 이동)");
	 re.addAttribute("url", "/login");
 }
 return "redirect:/popup";
 }
 @GetMapping("/popup")
 public String popup(@RequestParam("msg") String msg, 
		 			 @RequestParam("url") String url, Model mo) {
  mo.addAttribute("msg",msg);
  mo.addAttribute("url",url);
  return "popup";
 }
 
 @GetMapping("/login/check")
 public String loginCheck(HttpSession se, 
		 				@RequestParam("id") String id,
		 				Model mo, RedirectAttributes re) {
	 if(mrep.existsById(id)) {
		 se.setAttribute("id", id);
		 return "redirect:/menu"; 
	 }
	 else {
		 re.addAttribute("msg", id
				 +"는 미등록 아이디입니다. 확인 후 로그인 부탁드립니다.");
		 re.addAttribute("url", "/login");
		 return "redirect:/popup";
	 }
 }
 
 @GetMapping("/menu")
 public String menu(HttpSession se, Model mo) {
  mo.addAttribute("id", se.getAttribute("id"));
  return "menu";
 }
 
 @GetMapping("/myinfo")
 public String myinfo(HttpSession se, Model mo) { 
  String id = (String)se.getAttribute("id"); 
  mo.addAttribute("m",mrep.findById(id).get());
  return "myinfo";
 }
 
} // class