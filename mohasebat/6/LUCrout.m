function [answer] = LUCrout(n, A, B, nvpa)
    if (abs(determinant(n, A)) < 0.000001)
        error('system is homogeneous');
    else
        L = eye(n);
        U = eye(n);
        answer = B;
        A = horzcat(A, B);
        A = nonZeroDiagonal(n, A);
        L(:,[1]) = A(:,[1]);
        for i = 2:n
            U(1, i) = A(1, i) / L(1, 1);
        end
        for i = 2:n
           for j = 2:i
              L(i, j) = A(i, j) - L(i, 1:j - 1) * U(1:j - 1, j); 
           end
           for j = i-1:n
                U(i, j) = (A(i, j) - L(i, 1:i - 1) * U(1:i - 1, j)) / L(i, i);
            end
        end
        L
        Y = A(:,[n+1]);
        for i = 1:n
            for j = 1:i-1
                Y(i, 1) = Y(i, 1) - L(i, j) * Y(j, 1);
            end
            Y(i, 1) = Y(i, 1) / L(i, i);
        end
        for i = (n:-1:1)
           answer(i, 1) = Y(i, 1);
           for j = (n:-1:i+1)
              answer(i, 1) = answer(i, 1) - U(i, j) * answer(j, 1);
           end
           answer(i, 1) = answer(i, 1) / U(i, i);
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

