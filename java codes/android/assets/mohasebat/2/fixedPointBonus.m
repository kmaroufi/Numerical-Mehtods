function [v, x, points] = fixedPointBonus(fun, a, n, st, ed)
    g = strcat(fun, '+x');
    points = [];
    pointsX = [];
    pointsY = [];
    for i = 1:n
        x = eval(subs(g, a));
        if (x > ed || x < st)
            v = false;
            break;
        end
        points = [points; a x];
        pointsX = [pointsX a];
        pointsY = [pointsY x];
        if (abs(a - x) < 0.000001)
            v = true;
            break;
        else
            v = false;
        end
        a = x;
    end
    plot(pointsX, pointsY, '*');
end