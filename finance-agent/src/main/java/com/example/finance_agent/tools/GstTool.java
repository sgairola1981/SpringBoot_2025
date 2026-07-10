package com.example.finance_agent.tools;

import com.example.finance_agent.model.GstStatus;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class GstTool {

    private final JdbcTemplate jdbcTemplate;

    public GstTool(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Tool(
            name = "checkGSTStatus",
            description = "Checks GST reconciliation status of a voucher."
    )
    public GstStatus checkGSTStatus(String voucherNo) {

        String sql = """
                SELECT
                    LIA_VOU_NO,
                    CASE
                        WHEN PMNT_VOU_DT IS NULL THEN 0
                        ELSE 1
                    END GST_STATUS
                FROM T_WA_GST_INWARD_MST
                WHERE LIA_VOU_NO = ?
                FETCH FIRST 1 ROW ONLY
                """;

        try {

            return jdbcTemplate.query(sql, rs -> {

                if (!rs.next()) {

                    return new GstStatus(
                            voucherNo,
                            false,
                            false,
                            "GST data not available"
                    );
                }

                boolean completed = rs.getInt("GST_STATUS") == 1;

                return new GstStatus(
                        voucherNo,
                        true,
                        completed,
                        completed ? "GST Completed" : "GST Pending"
                );

            }, voucherNo);

        } catch (DataAccessException ex) {

            return new GstStatus(
                    voucherNo,
                    false,
                    false,
                    "GST data not available"
            );
        }
    }
}