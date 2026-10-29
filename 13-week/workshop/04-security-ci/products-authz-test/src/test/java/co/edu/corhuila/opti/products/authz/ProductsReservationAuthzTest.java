package co.edu.corhuila.opti.products.authz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Proxy;
import java.util.Set;

import org.junit.jupiter.api.Test;

import co.edu.corhuila.opti.products.adapter.in.http.ApiException;
import co.edu.corhuila.opti.products.adapter.in.http.AuthenticatedUser;
import co.edu.corhuila.opti.products.adapter.in.http.ErrorCode;
import co.edu.corhuila.opti.products.adapter.in.http.RequestRules;
import co.edu.corhuila.opti.products.adapter.in.http.Roles;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Story: only ADMIN or SERVICE may reserve or release stock in opti-products-api.
 * Endpoint: POST /api/v1/frames/{id}/reservations, which FrameController guards with
 * RequestRules.requireRole(http, Roles.ADMIN, Roles.SERVICE). These tests call that same real rule.
 */
class ProductsReservationAuthzTest {

    @Test
    void sellerTokenCannotReserveStock() {
        HttpServletRequest request = requestOf(new AuthenticatedUser("seller-1", Set.of(Roles.SELLER)));

        ApiException refused = assertThrows(ApiException.class,
                () -> RequestRules.requireRole(request, Roles.ADMIN, Roles.SERVICE));

        assertEquals(ErrorCode.FORBIDDEN, refused.code());
    }

    @Test
    void workflowServiceTokenCanReserveStock() {
        AuthenticatedUser workflow = new AuthenticatedUser("opti-workflow", Set.of(Roles.SERVICE));

        assertSame(workflow, RequestRules.requireRole(requestOf(workflow), Roles.ADMIN, Roles.SERVICE));
    }

    private static HttpServletRequest requestOf(AuthenticatedUser user) {
        return (HttpServletRequest) Proxy.newProxyInstance(HttpServletRequest.class.getClassLoader(),
                new Class<?>[] {HttpServletRequest.class},
                (proxy, method, args) -> method.getName().equals("getAttribute") ? user : null);
    }
}
