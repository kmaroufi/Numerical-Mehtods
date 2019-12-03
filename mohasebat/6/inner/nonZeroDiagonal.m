function [B] = nonZeroDiagonal(n, A)
    perm = 1:n;
    while (isNonZeroDiagonal(n, A) == false)
       perm = nextPerm(n, perm);
       A = A(perm, :); 
    end
    B = A;
end

