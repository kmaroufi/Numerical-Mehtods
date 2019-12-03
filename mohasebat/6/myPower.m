function [evalue, evector] = myPower(n, A, X, steps,nvpa)
    for i=1:steps
       Y = A * X;
       mx = 0;
       for j=1:n
          if (abs(Y(j, 1)) > mx) 
              mx = abs(Y(j, 1));
          end
       end
       X = (1 / mx) * Y;
       evalue = mx;
       evector = X;
    end
    
        u = evalue;
        evalue = string('a');
        for i = 1:length(u)
            a = u(i);
            a = vpa(a, nvpa);
            evalue(i) = char(a);
        end
        
        u = evector;
        evector = string('a');
        for i = 1:length(u)
            a = u(i);
            a = vpa(a, nvpa);
            evector(i) = char(a);
        end
    
end

