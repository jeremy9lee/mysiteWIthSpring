package com.sds.icto.mysite.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.sds.icto.mysite.dao.GuestBookDao;
import com.sds.icto.mysite.dao.MemberDao;
import com.sds.icto.mysite.vo.GuestBook;

@Controller
@EnableWebMvc
public class GuestController {

	@Autowired
	private GuestBookDao dao;

	@RequestMapping("/guestbookform.do")
	public String guestbookForm(Model m) {

		ArrayList<GuestBook> list = null;
		try {
			list = (ArrayList<GuestBook>) dao.selectAllList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.addAttribute("list", list);
		return "/views/guestbook/list.jsp";
	}

	@RequestMapping("/guestinsert.do")
	public String guestbookInsert(Model m, @RequestParam("name") String name,
			@RequestParam("pass") String pass,
			@RequestParam("content") String content) {

		try {
			dao.add(new GuestBook(0, name, pass, content, null));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/guestbookform.do";
	}

	@RequestMapping("/guestdeleteForm.do")
	public String guestbookDeleteForm(Model m, @RequestParam("id") String id) {

		GuestBook gb = null;
		try {
			gb = dao.search(Integer.parseInt(id));
		} catch (NumberFormatException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.addAttribute("no", gb.getNo());
		return "/views/guestbook/deleteform.jsp";
	}

	@RequestMapping("/guestdelete.do")
	public String guestbookDelete(Model m, @RequestParam("no") String no,
			@RequestParam("password") String password)
			throws NumberFormatException, SQLException {

		String realPwd = dao.search(Integer.parseInt(no)).getPassword();

		m.addAttribute("no", no);
		if (realPwd.equals(password)) {
			dao.delete(Integer.parseInt(no));
			return "/guestbookform.do";
		} else {
			String msg = "비밀번호가 맞지 않습니다.";
			m.addAttribute("msg", msg);
			return "/views/guestbook/deleteform.jsp";
		}
	}
	
	
}
