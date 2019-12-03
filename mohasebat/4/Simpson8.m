function rslt = Simpson8(f,a,b,h,ac)
    format long ;
    
    rslt = 0 ;
    c = 0 ;
    for i = a:h:b
        if ( vpa(i) == vpa(a) || vpa(i) == vpa(b) )
            rslt = rslt + subs(f,i);
        elseif ( mod(c,3) == 0 )
            rslt = rslt + 2*subs(f,i);
        else
            rslt = rslt + 3*subs(f,i);
        end
        c = c+1 ;
        rslt = round( rslt *(10^ac) )/(10^ac);
    end
    rslt = double(rslt *(3*h/8)) ;
    rslt = round( rslt *(10^ac) )/(10^ac);
    
    rslt = string(char(rslt));
end

% 'cos(x)/(1+x^2)' , 0 , 0.6 , 0.1 , 5 