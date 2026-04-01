package TTDH.Webbanhangdientu.authentication;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    // Lưu trữ Bucket (cái xô) cho từng địa chỉ IP
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        // Cấu hình: Tối đa 5 requests / 1 phút. Sau 1 phút sẽ tự động bơm lại đầy xô.
        Refill refill = Refill.greedy(5, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(5, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket resolveBucket(String ip) {
        return cache.computeIfAbsent(ip, k -> createNewBucket());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Lấy IP của người dùng
        String ip = request.getRemoteAddr();
        Bucket bucket = resolveBucket(ip);

        // Kiểm tra xem IP này còn lượt request không (mỗi lần gọi tốn 1 lượt)
        if (bucket.tryConsume(1)) {
            // Còn lượt -> Cho đi tiếp
            filterChain.doFilter(request, response);
        } else {
            // Hết lượt -> Chặn lại và báo lỗi 429
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"Bạn đã gửi quá nhiều yêu cầu. Vui lòng đợi 1 phút và thử lại.\"}");
        }
    }
}