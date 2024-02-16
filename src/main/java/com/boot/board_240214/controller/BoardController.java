package com.boot.board_240214.controller;

import com.boot.board_240214.model.Board;
import com.boot.board_240214.model.User;
import com.boot.board_240214.repository.BoardRepository;
import com.boot.board_240214.service.BoardService;
import com.boot.board_240214.validator.BoardValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/board")
@Slf4j
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardValidator boardValidator;
    @Autowired
    private BoardService boardService;

    @GetMapping(value = "/list")
//    public String list(Model model) {
//    public String list(Model model, @PageableDefault(size = 2) Pageable pageable) {
    public String list(Model model, @PageableDefault(size = 30) Pageable pageable, @RequestParam(required = false, defaultValue = "") String searchText) {
        log.info("@# list()");

//        List<Board> boards = boardRepository.findAll();
//        Page<Board> boards = boardRepository.findAll(PageRequest.of(1, 20));
//        Page<Board> boards = boardRepository.findAll(PageRequest.of(0, 20));
//        Page<Board> boards = boardRepository.findAll(pageable);
        // 최신글부터
        Sort sort = Sort.by(Sort.Order.desc("id"));

        // Pageable에 정렬 조건 추가
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);


        int startPage = 1;
        int endPage = boards.getTotalPages();
        model.addAttribute("boards", boards);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
//        boards.getTotalPages();
//        boards.getTotalElements()

        return "board/list";
    }

    @GetMapping(value = "/form")
//    public String form(Model model) {
//    public String form(Model model, @RequestParam Long id) {
    public String form(Model model, @RequestParam(required = false) Long id) {
        log.info("@# GetMapping form()");

        if (id == null) {
            model.addAttribute("board", new Board());
        } else {
//           Optional<Board> board = boardRepository.findById(id);
              Board board = boardRepository.findById(id).orElse(null);
              model.addAttribute("board",board);
        }

        return "board/form";
    }

    @PostMapping(value = "/form")
//    public String form(@ModelAttribute Board board, Model model) {
    public String form(@Valid Board board, BindingResult bindingResult, Authentication authentication) {
        log.info("@# PostMapping form()");

        boardValidator.validate(board, bindingResult);
        if (bindingResult.hasErrors()) {
            return "board/form";
        }
//        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boardService.save(username, board);

        return "redirect:/board/list";
    }


}