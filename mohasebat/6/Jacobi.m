function [answer] = Jacobi(n, A, B, X, steps, nvpa)
    answer = X;
    if (abs(determinant(n, A)) < 0.000001)
        error('system is homogeneous');
    else
        A = horzcat(A, B);
        A = nonZeroDiagonal(n, A);
        B = A(:,[n+1]);
        for i=1:steps
            for j=1:n
               X(j, 1) = B(j, 1);
               for k=1:n
                   if (k ~= j)
                       X(j, 1) = X(j, 1) - A(j, k) * answer(k, 1);
                   end
               end
               X(j, 1) = X(j, 1) / A(j, j);
            end
            answer = X;
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

