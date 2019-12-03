function [answer] = LUCholesky(n, A, B, nvpa)
nvpa = int16(nvpa);
    answer = B;
    if (abs(determinant(n, A)) < 0.000001)
        error('system is homogeneous');
        u = answer;
        answer = string('a');
        for i = 1:length(u)
            a = u(i);
            a = vpa(a, nvpa);
            answer(i) = char(a);
        end
        return;
    end
    %if (all(eig(A) > 0.000001))
     %   error('matrix is not SD');
      %  return;
    %end
    L = zeros(n, n);
    L(1, 1) = sqrt(A(1, 1));
    for i=2:n
       tmp = inverse(i-1, L([1:i-1], [1:i-1])) * A([1:i-1], [i]);
       L([i], [1:i-1]) = tmp';
       L(i, i) = sqrt(A(i, i) - tmp' * tmp);
    end

    A = horzcat(A, B);
    Y = A(:,[n+1]);
    tmpL = L';
    for i = 1:n
        for j = 1:i-1
            Y(i, 1) = Y(i, 1) - L(i, j) * Y(j, 1);
        end
        Y(i, 1) = Y(i, 1) / L(i, i);
    end
    for i = (n:-1:1)
       answer(i, 1) = Y(i, 1);
       for j = (n:-1:i+1)
          answer(i, 1) = answer(i, 1) - tmpL(i, j) * answer(j, 1);
       end
       answer(i, 1) = answer(i, 1) / L(i, i);
    end
    
        u = answer;
        answer = string('a');
        for i = 1:length(u)
            a = u(i);
            a = vpa(a, nvpa);
            answer(i) = char(a);
        end
end

