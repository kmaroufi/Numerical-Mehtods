function out = midpoint(f,x,y,h)
k1 = h * f;
k2 = h* f(x + h/2 , y + k1/2);
out = y + k2;
end