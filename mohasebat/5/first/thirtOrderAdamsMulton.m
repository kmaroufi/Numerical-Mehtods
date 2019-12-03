function out =  thirtOrderAdamsMulton(f,x,y,h)
xi1 = x - h;
yi1 = forthOrderRungKutta(f,x,y,-h);
fi1 = f(xi1,yi1);
xi2 = xi1 - h;
yi2 = forthOrderRungKutta(f,xi1,yi1,-h);
fi2 = f(xi2,yi2);
outStar = y + h * (23 * f - 16 * fi1 +5 * fi2)/12;
foutStar = f(x + h, outStar);
out = y + h * (5*foutStar+ 8 * f - fi1)/12;
end