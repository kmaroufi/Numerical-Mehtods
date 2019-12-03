function rslt = Customized_Simpson(f,a,b,h,ac)
    n = ( b-a ) / h ;
    n = n +1 ;
    
    if ( mod(n,2) == 0 )
        rslt = Simpson3(f,a,b,h,ac);
    elseif ( n == 3 )
        rslt = Simpson8(f,a,b,h,ac);
    else
        rslt1 = Simpson3(f,a,b-2*h,h,ac);
        rslt2 = Simpson8(f,b-2*h,b,h,ac);
        rslt = rslt1 + rslt2 ;
    end
    rslt = round( rslt *(10^ac) )/(10^ac);
    
    rslt = string(rslt);
end

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
end

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
    
end

% 'cos(x)/(1+x^2)' , 0 , 0.6 , 0.1 , 5 