function [B] = inverse(n, A)
    d = determinant(n, A);
    B = (1. / d) * cofactor(n, A)';
end

