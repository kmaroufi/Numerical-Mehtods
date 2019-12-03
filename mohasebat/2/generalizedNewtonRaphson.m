function [answer] = generalizedNewtonRaphson(n, x, F)
x = cellstr(x);
x = transpose(x);
F = cellstr(F);
F = transpose(F);
    answer = linspace(0, 0, n);
    s = linspace(sym('x'), sym('x'), n);
    for i = 1:n
        s(i) = sym(x(i));
    end
    J = jacobian(F, s);
    for i = 1:10
       JT = eval(subs(J, s, answer));
       FT = eval(subs(F, s, answer));
       y = - inv(JT) * FT';
       answer = answer + y';
    end
end
