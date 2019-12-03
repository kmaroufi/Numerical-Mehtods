function out = modifiedEuler(f,x,y,h)
ans = taylorr(f,2,x,y,h);
yi1Star = ans(2);
yi1Starprim = f(x+h,yi1Star);
yi1 = y + h/2 *(f + yi1Starprim);
out = yi1;
end
