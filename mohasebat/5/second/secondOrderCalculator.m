function [out,out2,out3] = secondOrderCalculator(equation1,x_0,y_0,f_0,x_f,number,d, nvpa)
syms f x y h g z k 
n = (x_f - x_0)/d + 0.000001;
output = zeros(2,int16(n));
u1 = y;
u2 = f;
u1prim = sym(equation1);

u2prim = symfun(u1prim,[x y f]);
g = u2prim;
if(number == 1)
    x_00 = x_0;
    y_00 = y_0;
    f_00 = f_0;
    y2 = EulerSecondOrder(f,g,z,y,h);
    for i = 1:n
      out(i) = subs(y2,[x,y,f,h,z],[x_00,y_00,f_00,d,z_00]);
      res = out(i);
      x_00 = x_00 + d;
      y_00 = y_00 + res(1);
      z_00 = z_00 + res(2);
    end
end
if(number == 2)
    x_00 = x_0;
    y_00 = y_0;
    f_00 = f_0;
    y2 = forthOrderRungKuttaSecondOrder(f,g,x,y,z,h);
    for i = 1:n
      output(:,i) = subs(y2,[f,x,y,z,h],[f_00,x_00,y_00,f_00,d]);
      y_00 = output(1,i);
      x_00 = x_00 + d;
      f_00 = output(2,i);
    end
end
out = output(1,:);
out2 = output(2,:);

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


for i = 1:n + 1
    out3(1,i) = x_0 + (i-1)*d;
end

u = out3;
out3 = string('a');
for i = 1:length(u)
    a = u(i);
    a = vpa(a, nvpa);
    out3(i) = char(a);
end
end

