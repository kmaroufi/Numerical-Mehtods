function rslt = Trapezoidal(f,a,b,h,ac)
    
    
    rslt = 0 ;
    for i = a:h:b
        if ( vpa(i) == vpa(a) || vpa(i) == vpa(b))
            rslt = rslt + subs(f,i);
        else
            rslt = rslt + 2*subs(f,i);
        end
        rslt = round( rslt *(10^ac) )/(10^ac);
    end
    rslt = double(rslt *(h/2)) ;
    rslt = round( rslt *(10^ac) )/(10^ac);
    
    rslt = string(rslt);
end

% '1/x^2' , 1 , 3 , 1 , 5 