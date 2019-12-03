function [v, x ,points] = secant(fun, a, b)
    s = fun;
    points = [];
    pointsX = [];
    pointsY = [];
    for i = 1:100
        ya = eval(subs(s, a));
        yb = eval(subs(s, b));
        x = (a * yb - b * ya) / (yb - ya);
        a = b;
        b = x;
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

