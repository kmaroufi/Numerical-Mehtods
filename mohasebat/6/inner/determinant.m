function [d] = determinant(n, A)
    d = 0;
    sign = 1;
    if (n == 0)
        d = 1;
    else
        for i=1:n
           tmp = A;
           tmp([1], :) = [];
           tmp(:, [i]) = [];
           d = d + sign * A(1, i) * determinant(n-1, tmp);
           sign = -sign;
        end
    end
end

