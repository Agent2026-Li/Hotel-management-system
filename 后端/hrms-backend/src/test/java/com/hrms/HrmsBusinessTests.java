package com.hrms;

import com.hrms.common.BusinessException;
import com.hrms.common.CurrentUser;
import com.hrms.dto.response.AuthUser;
import com.hrms.entity.BillItem;
import com.hrms.utils.AmountUtils;
import com.hrms.utils.IdGenerator;
import com.hrms.utils.StatusMapper;
import com.hrms.utils.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 后端基础业务测试，验证新分层结构、工具类和 Spring 上下文可正常工作。
 */
@SpringBootTest
class HrmsBusinessTests {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private IdGenerator idGenerator;

    @Test
    void contextLoads() {
        assertTrue(tokenService.getValidHours() > 0);
    }

    @Test
    void roomStatusMapsBetweenInternalAndFrontendValues() {
        assertEquals("vacant", StatusMapper.toFrontend("VC"));
        assertEquals("occupied", StatusMapper.toFrontend("OC"));
        assertEquals("reserved", StatusMapper.toFrontend("RS"));
        assertEquals("dirty", StatusMapper.toFrontend("VD"));
        assertEquals("maintenance", StatusMapper.toFrontend("OOO"));

        assertEquals("VC", StatusMapper.toInternal("vacant"));
        assertEquals("OC", StatusMapper.toInternal("occupied"));
        assertEquals("RS", StatusMapper.toInternal("reserved"));
        assertEquals("VD", StatusMapper.toInternal("dirty"));
        assertEquals("OOO", StatusMapper.toInternal("maintenance"));
    }

    @Test
    void unsupportedRoomStatusIsRejected() {
        assertThrows(BusinessException.class, () -> StatusMapper.toInternal("unknown-status"));
    }

    @Test
    void amountUtilsUsesBigDecimalForRoomAndServiceAmounts() {
        BillItem roomFee = new BillItem();
        roomFee.category = "ROOM";
        roomFee.amount = new BigDecimal("299.00");
        roomFee.quantity = 2;

        BillItem minibar = new BillItem();
        minibar.category = "MINIBAR";
        minibar.amount = new BigDecimal("25.50");
        minibar.quantity = 1;

        List<BillItem> items = List.of(roomFee, minibar);

        assertEquals(new BigDecimal("598.00"), AmountUtils.roomAmount(items));
        assertEquals(new BigDecimal("25.50"), AmountUtils.serviceAmount(items));
        assertEquals("paid", AmountUtils.billStatus(new BigDecimal("623.50"), new BigDecimal("623.50")));
        assertEquals("unpaid", AmountUtils.billStatus(new BigDecimal("623.50"), new BigDecimal("100.00")));
    }

    @Test
    void idGeneratorCreatesReadableBusinessIds() {
        assertTrue(idGenerator.reservationId().startsWith("R"));
        assertTrue(idGenerator.checkInId().startsWith("CI"));
        assertTrue(idGenerator.billId().startsWith("B"));
        assertTrue(idGenerator.housekeepingId().startsWith("HK"));
    }

    @Test
    void tokenServiceGeneratesAndParsesCurrentUser() {
        AuthUser user = new AuthUser();
        user.username = "admin";
        user.name = "系统管理员";
        user.role = "ADMIN";

        String token = tokenService.generate(user);
        Optional<CurrentUser> currentUser = tokenService.parse(token);

        assertFalse(token.isBlank());
        assertTrue(currentUser.isPresent());
        assertEquals("admin", currentUser.get().username());
        assertEquals("系统管理员", currentUser.get().name());
        assertEquals("ADMIN", currentUser.get().role());
    }
}
