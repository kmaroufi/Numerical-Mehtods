function out = forthOrderAdamsMulton(f,x,y,h)
n = 6;
ansi1 = taylorr(f,n,x,y,-h);
xi1 = x - h;
yi1 = ansi1(n);
fi1 = f(xi1,yi1);
xi2 = xi1 - h;
yi2 = ansi1(n);
fi2 = f(xi2,yi2);
xi3 = xi2 - h;
yi3 = ansi1(n);
fi3 = f(xi3,yi3);
outStar = y + h * (55 * f - 59 * fi1 + 37*fi2 - 9 * fi3)/24;
foutStar = f(x + h,outStar);
out = y + h * (9 * foutStar + 19 * f - 5 * fi1 + fi2)/24;
end

