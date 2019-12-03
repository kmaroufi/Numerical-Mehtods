function [v, x, points] = bisection(fun, a, b)
    pointsX = [];
    pointsY = [];
    points = [];
    s = fun;
    for i = 1:100
        x = (a + b) / 2.;
        y = eval(subs(s, x));
        points = [points; x y];
        pointsX = [pointsX x];
        pointsY = [pointsY y];
        if (eval(subs(s, a)) * y < 0)
            b = x;
        else
            a = x;
        end
        if (abs(y) < 0.000001)
            v = true;
            break;
        else
            v = false;
        end
    end
    plot(pointsX, pointsY, '*');
end

