function [answer] = LUDoolittle(n, A, B, nvpa)
    if (abs(determinant(n, A)) < 0.000001)
        error('system is homogeneous');
    else
        L = eye(n);
        U = eye(n);
        answer = B;
        A = horzcat(A, B);
        A = nonZeroDiagonal(n, A);
        for i=1:n
           for j = 1:i-1
               L(i, j) = A(i, j);
               for k = 1:j-1
                   L(i, j) = L(i, j) - L(i, k) * U(k, j);
               end
               L(i, j) = L(i, j) / U(j, j);
           end
           for j = i:n
              U(i, j) = A(i, j);
              for k = 1:i-1
                 U(i, j) = U(i, j) - L(i, k) * U(k, j); 
              end
           end
        end
        Y = A(:,[n+1]);
        for i = 2:n
            for j = 1:i-1
                Y(i, 1) = Y(i, 1) - L(i, j) * Y(j, 1);
            end
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

