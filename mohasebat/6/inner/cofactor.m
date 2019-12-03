function [B] = cofactor(n, A)
    B = A;
    for i=1:n
        for j=1:n
            tmp = A;
            tmp([i], :) = [];
            tmp(:, [j]) = [];
            B(i, j) = (-1)^(i+j) * determinant(n-1, tmp);
        end
    end
end

