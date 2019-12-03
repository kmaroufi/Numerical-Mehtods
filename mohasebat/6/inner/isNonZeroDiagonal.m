function [t] = isNonZeroDiagonal(n, A)
    t = true;
    for i =1:n
        if (abs(A(i, i)) < 0.000001)
            t = false;
        end
    end
end

