package com.example.finance_agent.tools;

import com.example.finance_agent.model.BudgetInfo;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BudgetTool {

    private final JdbcTemplate jdbcTemplate;

    public BudgetTool(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Tool(
            name = "checkBudget",
            description = "Checks available budget for a HOA."
    )
    public BudgetInfo checkBudget(String hoa) {

        String sql = """
                SELECT AVAILABLE_BUDGET
                FROM FA_BUDGET
                WHERE HOA = ?
                """;

        try {

            BigDecimal budget = jdbcTemplate.queryForObject(
                    sql,
                    BigDecimal.class,
                    hoa
            );

            return new BudgetInfo(
                    hoa,
                    true,
                    budget,
                    "Success"
            );

        } catch (EmptyResultDataAccessException ex) {

            return new BudgetInfo(
                    hoa,
                    false,
                    null,
                    "Budget data not available"
            );

        } catch (DataAccessException ex) {

            return new BudgetInfo(
                    hoa,
                    false,
                    null,
                    "Budget data not available"
            );
        }
    }
}