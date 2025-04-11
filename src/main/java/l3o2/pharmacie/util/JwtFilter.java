package l3o2.pharmacie.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import l3o2.pharmacie.api.model.entity.Employe;
import l3o2.pharmacie.api.service.EmployeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filtre pour vérifier le token JWT
 * @author raphaelcharoze
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final EmployeService employeService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String id = jwtUtil.validateTokenAndReturnUsername(token);

        if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Employe employee = this.employeService.getEmployeByEmailPro(employeService.getEmployeById(UUID.fromString(id)).getEmailPro());

            //token avec le nom et les permissions de l'employé
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(employee, employee.getPassword(), employee.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

        }

        chain.doFilter(request, response);
    }
}