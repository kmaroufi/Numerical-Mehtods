function out = forthOrderRungKuttaSecondOrder(f,g,x,y,z,h)
f1 = f;
g1 = g;
f2 = z + h/2 * g1;
g2 = g(x + h/2,y+ h * f1/2 , z + h/2* g1);
f3 = z + h/2 * g2;
g3 = g(x + h/2,y+ h * f2/2 , z + h/2 * g2);
f4 = z + h * g3;
g4 = g(x + h,y+ h * f3 , z + h * g3);
out(1) = y + h * (f1 + 2 * f2 + 2 * f3 + f4)/6;
out(2) = z + h * (g1 + 2 * g2 + 2 * g3 + g4)/6;
end