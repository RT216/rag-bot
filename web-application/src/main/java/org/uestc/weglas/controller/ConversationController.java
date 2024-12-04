package org.uestc.weglas.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.uestc.weglas.core.enums.ResultEnum;
import org.uestc.weglas.core.model.Conversation;
import org.uestc.weglas.core.model.ConversationChatDetail;
import org.uestc.weglas.core.service.ConversationService;
import org.uestc.weglas.core.service.ChatService;
import org.uestc.weglas.util.BaseResult;
import org.uestc.weglas.util.exception.AssertUtil;
import org.uestc.weglas.util.exception.ManagerBizException;

import java.util.List;
import java.util.ArrayList;

/**
 * @author yingxian.cyx
 * @date Created in 2024/10/11
 */
@Slf4j // 添加日志支持
@RestController
@RequestMapping("/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;
    
    @Autowired
    private ChatService chatService;

    /**
     * 新建会话+第一条聊天记录
     *
     * @param conversation
     * @return
     */
    @PostMapping("/add.json")
    public BaseResult<Conversation> addConversation(@RequestBody Conversation conversation) {

        conversationService.add(conversation);
        return BaseResult.success(conversation);
    }

    /**
     * Lecture 4 返回会话列表
     *
     * @param model Spring MVC的Model对象
     * @return 会话列表
     */
    @GetMapping("/list.json")
    public BaseResult<Conversation> queryAll(Model model) {
        try {
            List<Conversation> conversations = conversationService.queryAll();
            // 添加到model中供视图使用
            model.addAttribute("conversations", conversations);
            // 使用values构造函数返回列表结果
            return new BaseResult<>(true, ResultEnum.SUCCESS.getMessage(), conversations);
        } catch (ManagerBizException e) {
            log.error("查询会话列表失败: {}", e.getMessage());
            return BaseResult.fail(ResultEnum.INVOKE_FAIL);
        } catch (Exception e) {
            log.error("查询会话列表发生未知错误", e);
            return BaseResult.fail(ResultEnum.INVOKE_FAIL);
        }
    }

    /**
     * Lecture 4 新建一条聊天记录
     *
     * @param chat 聊天详情
     * @return 创建的聊天记录
     */
    @PostMapping("/addChat.json")
    public BaseResult<ConversationChatDetail> addChat(@RequestBody ConversationChatDetail chat) {
        try {
            // 1. 获取当前会话
            Conversation conversation = conversationService.queryById(chat.getConversationId());
            AssertUtil.notNull(conversation);

            // 2. 调用ChatService处理对话
            ConversationChatDetail aiResponse = chatService.chat(conversation, chat);

            // 3. 返回用户消息和AI回复的列表
            List<ConversationChatDetail> chatList = new ArrayList<>();
            chatList.add(chat);        // 用户消息
            chatList.add(aiResponse);  // AI回复

            return new BaseResult<>(true, "Operation successful", chatList);
            
        } catch (Exception e) {
            log.error("添加聊天记录失败", e);
            return BaseResult.fail(ResultEnum.INVOKE_FAIL);
        }
    }



    

}

