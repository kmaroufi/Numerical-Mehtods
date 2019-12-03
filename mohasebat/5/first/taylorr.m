function out = taylorr(f,n,x,y,h)
arrayOfdif = sym.empty;
arrayOfdif(1) = y;
arrayOfdif(2) = f;
if(n > 2)
for i = 3:n
    arrayOfdif(i) = diff(f,x);
    f = diff(f,x);
end
end
arrayOfeachStep = sym.empty;
for i = 0:(n - 1)
    arrayOfeachStep(i+ 1) = (h ^ i) * arrayOfdif(i + 1) /factorial(i);
end
stepbystepArray = sym.empty;
stepbystepArray(1) = arrayOfeachStep(1);
for i = 2:n
    stepbystepArray(i) = stepbystepArray(i - 1) + arrayOfeachStep(i);
end
out = stepbystepArray;
end
    


