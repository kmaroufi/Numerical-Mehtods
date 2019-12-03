function [B] = nextPerm(n, A)
    cnt = n;
    while (cnt >= 0 && A(cnt - 1) > A(cnt))
        cnt = cnt - 1;
    end
    t = cnt;
    while (t <= n && A(cnt - 1) < A(t))
        t = t + 1;
    end
    t = t - 1;
    cnt = cnt - 1;
    tmp = A(t);
    A(t) = A(cnt);
    A(cnt) = tmp;
    A(cnt+1:n) = fliplr(A(cnt+1:n));
    B = A;
end

