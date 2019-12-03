function [v, x, points] = newtonRaphson(fun, a)
    s = fun;
    points = [];
    pointsX = [];
    pointsY = [];
    for i = 1:100
        y = eval(subs(s, a));
        dy = eval(subs(diff(sym(s)), a));
        a = a - y / dy;
        x = a;
        y = eval(subs(s, x));
        points = [points; x y];
        pointsX = [pointsX x];
        pointsY = [pointsY y];
        if (abs(y) < 0.01)
            v = true;
            plot(pointsX, pointsY, '*');
            break;
        else
            v = false;
        end
    end
end