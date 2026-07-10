package com.example.finance_agent.config;

import com.example.finance_agent.tools.BudgetTool;
import com.example.finance_agent.tools.GstTool;
import com.example.finance_agent.tools.PaymentTool;
import com.example.finance_agent.tools.VoucherTool;
import com.example.finance_agent.tools.WorkflowTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder,
                          VoucherTool voucherTool,
                          WorkflowTool workflowTool,
                          GstTool gstTool,
                          BudgetTool budgetTool,
                          PaymentTool paymentTool) {

        return builder
                .defaultTools(
                        voucherTool,
                        workflowTool,
                        gstTool,
                        budgetTool,
                        paymentTool
                )
                .build();
    }

}