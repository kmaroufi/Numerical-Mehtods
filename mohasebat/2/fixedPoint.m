function [v, x, points] = fixedPoint(fun, a, n)
    g = strcat(fun, '+x');
    points = [];
    pointsX = [];
    pointsY = [];
    for i = 1:n
        x = eval(subs(g, a));
        points = [points; a x];
        pointsX = [pointsX a];
        pointsY = [pointsY x];
        if (abs(a - x) < 0.01)
            v = true;
            plot(pointsX, pointsY, '*');
            break;
        else
            v = false;
        end
        a = x;
    end
end