function out = forthOrderRungKutta(f,x,y,h)
k1 = h * f;
k2 = h * f(x + h / 2 , y + k1/2);
k3 = h * f(x + h / 2 , y + k2/2);
k4 = h * f(x + h,y + k3);
out = y + (k1 + 2 * k2 + 2 * k3 + k4)/6;
end