function out = thirdOrderRungKutta3(f,x,y,h)
k1 = h * f;
k2 = h * f(x + h / 2 , y + k1/2);
k3 = h * f(x + h,y + 2 * k2 - k1);
out = y + (k1 + 4 * k2 + k3)/6;
end