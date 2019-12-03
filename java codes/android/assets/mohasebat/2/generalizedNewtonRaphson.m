function [answer] = generalizedNewtonRaphson(n, x, F)
x = cellstr(x);
x = transpose(x);
F = cellstr(F);
F = transpose(F);
    answer = linspace(1, 1, n);
    J = repmat(diff('2*x+1', sym('x')), n, n);
    s = linspace(sym('x'), sym('x'), n);
    for i = 1:n
        s(i) = sym(x(i));
    end
    for i = 1:n
       for j = 1:n
           J(i, j) = diff(F(i), s(j));
       end
    end
    for i = 1:100
       deltaX = -(eval(subs(F, s, answer))')\eval(subs(J, s, answer));
       answer = answer + deltaX;
    end
end
