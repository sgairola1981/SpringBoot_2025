package com.example.finance_agent.tools;

import com.example.finance_agent.model.PaymentStatus;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PaymentTool {

    private final JdbcTemplate jdbcTemplate;

    public PaymentTool(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Tool(
            name = "checkPaymentStatus",
            description = "Checks whether voucher has already been paid."
    )
    public PaymentStatus paymentStatus(String voucherNo) {

        String sql = """
                SELECT PAYMENT_DATE
                FROM FA_PAYMENT
                WHERE VOUCHER_NO = ?
                """;

        try {

            return jdbcTemplate.query(sql, rs -> {

                if (rs.next()) {

                    Date paymentDate = rs.getDate("PAYMENT_DATE");

                    return new PaymentStatus(
                            voucherNo,
                            true,
                            true,
                            paymentDate,
                            "Success"
                    );
                }

                return new PaymentStatus(
                        voucherNo,
                        true,
                        false,
                        null,
                        "Not Paid"
                );

            }, voucherNo);

        } catch (DataAccessException ex) {

            return new PaymentStatus(
                    voucherNo,
                    false,
                    false,
                    null,
                    "Payment data not available"
            );
        }
    }
}