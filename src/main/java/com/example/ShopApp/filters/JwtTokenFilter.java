package com.example.ShopApp.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${api.prefix}")
    private String apiPrefix;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        if(isBypassToken(request)){
            filterChain.doFilter(request, response); //enable bypass Tất cả đều có thể qua
            return;
        }
        
    }
 //   Kiểm tra xem yêu cầu HTTP có nằm trong danh sách các đường dẫn và phương thức HTTP được cho phép bypass (bỏ qua) xác thực hay không.
    private boolean isBypassToken(@NonNull HttpServletRequest request){
        final List<Pair<String,String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/products",apiPrefix),"GET"),
                Pair.of(String.format("%s/categories", apiPrefix), "GET"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login",apiPrefix), "POST")
        );
        for(Pair<String, String> bypassToken: bypassTokens){
            if(request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())){
                return true;
            }
        }
        return false;
    }
}
