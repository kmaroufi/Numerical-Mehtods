function [answer] = cramer(n, A, B,nvpa)
    answer = B;
    d = determinant(n, A);
    if (abs(d) < 0.000001)
        error('system is homogeneous');
    else
        for i=1:n
            tmp = A;
            tmp(:, [i]) = B;
            answer(i, 1) = determinant(n, tmp) / d;
        end
    end
    
u = answer;
answer = string('a');
for i = 1:length(u)
    a = u(i);
    a = vpa(a, nvpa);
    answer(i) = char(a);
end
end