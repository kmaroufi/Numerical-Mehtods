function [B] = upToZero(n, A)
    B = A;
    for i = (n-1:-1:1)
       for j = (n:-1:i+1)
           if (abs(B(i, j)) > 0.000001)
               tmp = B(j, j) / B(i, j);
               for k = 1:n+1
                   B(i, k) = B(i, k) * tmp - B(j, k);
               end
           end
       end
    end
end