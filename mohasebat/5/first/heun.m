function out = heun(f,x,y,h)
k1 = h * f;
k2 = h * f(x + 2 * h / 3 , y + 2 * k1/3);
out = y + (k1 + 3 * k2)/4;
end