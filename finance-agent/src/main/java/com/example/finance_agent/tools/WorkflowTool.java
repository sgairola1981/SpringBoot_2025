package com.example.finance_agent.tools;

import com.example.finance_agent.model.WorkflowStatus;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class WorkflowTool {

    private final JdbcTemplate jdbcTemplate;

    public WorkflowTool(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Tool(
            name = "getWorkflowStatus",
            description = "Returns workflow approval status of a voucher."
    )
    public WorkflowStatus getWorkflowStatus(String voucherNo) {

        String sql = """
                SELECT
                    VOUCHER_NO  AS voucherNo,
                    STATUS      AS workflowStatus,
                    APPROVED_BY AS approvedBy
                FROM FA_WORKFLOW
                WHERE VOUCHER_NO = ?
                """;

        try {

            WorkflowStatus status = jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(WorkflowStatus.class),
                    voucherNo
            );

            if (status != null) {
                status.setFound(true);
                status.setMessage("Success");
            }

            return status;

        } catch (EmptyResultDataAccessException ex) {

            WorkflowStatus status = new WorkflowStatus();
            status.setFound(false);
            status.setVoucherNo(voucherNo);
            status.setMessage("Workflow data not available");
            return status;

        } catch (Exception ex) {

            WorkflowStatus status = new WorkflowStatus();
            status.setFound(false);
            status.setVoucherNo(voucherNo);
            status.setMessage("Workflow data not available");
            return status;
        }
    }
}