function [answer] = gausseElimination(n, A, B,nvpa)
    answer = B;
    if (abs(determinant(n, A)) < 0.000001)
        error('system is homogeneous');
    else
        tmp = horzcat(A, B);
        tmp = nonZeroDiagonal(n, tmp);
        tmp = bottomToZero(n, tmp);
        tmp = upToZero(n, tmp);
        for i = 1:n
            answer(i, 1) = tmp(i, n+1) / tmp(i, i);
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

