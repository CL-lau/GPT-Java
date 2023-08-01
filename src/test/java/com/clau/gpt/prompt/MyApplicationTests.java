package com.clau.gpt.prompt;

import com.clau.gpt.prompt.service.PromptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


import java.io.IOException;


public class MyApplicationTests {

    public PromptService promptService;

    @Test
    public void testMyService() throws Exception {
        promptService = new PromptService();
        String result = promptService.getPrompt("教授");
        System.out.println(result);
    }
}

