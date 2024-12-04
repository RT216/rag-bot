package org.uestc.weglas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.uestc.weglas.core.model.Conversation;
import org.uestc.weglas.core.service.ConversationService;

@Controller
public class HomeController {

    @Autowired
    private ConversationService conversationService;

    @RequestMapping(value = "/")
    public String index(Model model) {

        model.addAttribute("conversations", conversationService.queryAll());
        model.addAttribute("newConversation", new Conversation());

        return "conversations/list";
    }

    @GetMapping("/conversations/detail")
    public String conversationDetail(@RequestParam Integer conversationId, Model model) {

        Conversation conversation = conversationService.queryById(conversationId);
        model.addAttribute("conversation", conversation);
        model.addAttribute("conversations", conversationService.queryAll());

        return "conversations/detail";
    }
}
