function [answer] = inverseMatrix(n, A, B, nvpa)
    if (abs(determinant(n, A)) < 0.000001)
        error('system is homogeneous');
    else
        answer = inverse(n, A) * B;
    end
    
u = answer;
answer = string('a');
for i = 1:length(u)
    a = u(i);
    a = vpa(a, nvpa);
    answer(i) = char(a);
end
end

