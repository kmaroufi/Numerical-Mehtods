function rslt = Simpson3(f,a,b,h,ac)
    format long ;
    
    rslt = 0 ;
    c = 0 ;
    for i = a:h:b
        if ( vpa(i) == vpa(a) || vpa(i) == vpa(b) )
            rslt = rslt + subs(f,i);
        elseif ( mod(c,2) == 0 )
            rslt = rslt + 2*subs(f,i);
        else
            rslt = rslt + 4*subs(f,i);
        end
        rslt = round( rslt *(10^ac) )/(10^ac);
        c = c+1 ;
    end
    rslt = double(rslt *(h/3)) ;
    rslt = round( rslt *(10^ac) )/(10^ac);
    
    rslt = string(rslt);
end

% 'cos(x)/(1+x^2)' , 0 , 0.6 , 0.1 , 5 