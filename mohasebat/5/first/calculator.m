% number: 1 for taylor ,2 for Euler, 3 for modified Euler, 4 for midpoint, 5 for heun, 6 for thirdorderrungkutta %
% 7 for forthorderrungkutta , 8 for thirdorderadamsmulton , 9 for forthorderadamsmulton
%
function [out,out2] = calculator(equation,x_0,y_0,d,x_f,number,orderofTaylor,nvpa)
syms f x y h
n = (x_f - x_0)/d + 0.000001;
out = [];
f = sym(equation);
f = symfun(f,[x y]);
if(number == 1)
    x_00 = x_0;
    y_01 = y_0;
    ans2 = taylorr(f,orderofTaylor,x,y,h);
    y2 = ans2(orderofTaylor);
    for i = 1:n
        out(i) = subs(y2,[x,y,h],[x_00,y_01,d]);
        x_00 = x_00 + d;
        y_01 = out(i);
    end
end
if(number == 2)
    x_00 = x_0;
    y_01 = y_0;
    ans2 = taylorr(f,2,x,y,h);
    y2 = ans2(2);
    for i = 1:n
        out(i) = subs(y2,[x,y,h],[x_00,y_01,d]);
        x_00 = x_00 + d;
        y_01 = out(i);
    end
end
if(number == 3)
    x_00 = x_0;
    y_01 = y_0;
    y2 = modifiedEuler(f,x,y,h);
    for i = 1:n
        out(i) = subs(y2,[x,y,h],[x_00,y_01,d]);
        x_00 = x_00 + d;
        y_01 = out(i);
    end
end
if(number == 4)
    x_00 = x_0;
    y_01 = y_0;
    y2 = midpoint(f,x,y,h);
    for i = 1:n
        out(i) = subs(y2,[x,y,h],[x_00,y_01,d]);
        x_00 = x_00 + d;
        y_01 = out(i);
    end
end
if(number == 5)
    x_00 = x_0;
    y_01 = y_0;
    y2 = heun(f,x,y,h);
    for i = 1:n
        out(i) = subs(y2,[x,y,h],[x_00,y_01,d]);
        x_00 = x_00 + d;
        y_01 = out(i);
    end
end
if(number == 6)
    x_00 = x_0;
    y_01 = y_0;
    y2 = thirdOrderRungKutta3(f,x,y,h);
    for i = 1:n
        out(i) = subs(y2,[x,y,h],[x_00,y_01,d]);
        x_00 = x_00 + d;
        y_01 = out(i);
    end
end
if(number == 7)
    x_00 = x_0;
    y_01 = y_0;
    y2 = forthOrderRungKutta(f,x,y,h);
    for i = 1:n
        out(i) = subs(y2,[x,y,h],[x_00,y_01,d]);
        x_00 = x_00 + d;
        y_01 = out(i);
    end
end
if(number == 8)
    x_00 = x_0;
    y_01 = y_0;
    y2 = thirtOrderAdamsMulton(f,x,y,h);
    for i = 1:n
        out(i) = subs(y2,[x,y,h],[x_00,y_01,d]);
        x_00 = x_00 + d;
        y_01 = out(i);
    end
end
if(number == 9)
    x_00 = x_0;
    y_01 = y_0;
    y2 = forthOrderAdamsMulton(f,x,y,h);
    for i = 1:n
        out(i) = subs(y2,[x,y,h],[x_00,y_01,d]);
        x_00 = x_00 + d;
        y_01 = out(i);
    end
end
out = [y_0,out];
for i = 1:n + 1
    out2(1,i) = x_0 + (i-1)*d;
end

u = out;
out = string('a');
for i = 1:length(u)
    a = u(i);
    a = vpa(a, nvpa);
    out(i) = char(a);
end

u = out2;
out2 = string('a');
for i = 1:length(u)
    a = u(i);
    a = vpa(a, nvpa);
    out2(i) = char(a);
end
end


    


