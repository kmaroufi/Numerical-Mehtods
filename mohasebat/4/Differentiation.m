function [rslt,rich] = Differentiation(f,v,d,o,h,ac)
    rslt = Dif(f,v,d,o,h,ac);
    rslth2 = Dif (f,v,d,o,h/2,ac) ;
    
    
    rslt = round( vpa(rslt)*10^ac ) / (10^ac) ;
    rslth2 = round( vpa(rslth2)*10^ac ) / (10^ac) ;
    
    rslt = vpa(round(vpa(rslt)*10^ac)/(10^ac));
    rich = vpa( round(((4*rslth2-rslt)/3)*10^ac)/(10^ac)  );
    
    rslt = string(char(rslt));
    rich = string(char(rich));
end

function rslt = Dif(f,v,d,o,h,ac)
   fi = round(subs(f,v)*(10^ac))/(10^ac);
   fp1 = round(subs(f,v+h)*(10^ac))/(10^ac);
   fm1 = round(subs(f,v-h)*(10^ac))/(10^ac);
   fp2 = round(subs(f,v+h*2))/(10^ac);
   fm2 = round(subs(f,v-h*2))/(10^ac);
   fp3 = round(subs(f,v+h*3))/(10^ac);
   fm3 = round(subs(f,v-h*3))/(10^ac);
   
   if ( o == 2 )
       if ( d == 1 )
            rslt1 = (fp1-fm1)/(2*h);
            rslt2 = (-3*fi+4*fp1-fp2)/(2*h);
            bst = subs( diff(f,sym('x')) , v );
            
            if ( abs(bst-rslt1) < abs(bst-rslt2) )
                rslt = rslt1 ;
            else
                rslt = rslt2 ;
            end
       elseif ( d == 2 )
           rslt = (fp1-2*fi+fm1)/(h*h) ;
       elseif ( d == 3 )
           rslt = (fp2-2*fp1+2*fm1-fm2)/(2*h*h);
       else
           rslt = (fp2-4*fp1+6*fi-4*fm1+fm2)/(h*h*h*h);
       end
    end
    
    if ( o == 4 )
       if ( d == 1 )
           rslt = (-fp2+8*fp1-8*fm1+fm2)/(12*h);
       elseif ( d == 2 )
           rslt = (-fp2+16*fp1-30*fi+16*fm1-fm2)/(12*h*h) ;
       elseif ( d == 3 )
           rslt = (-fp3+8*fp2-13*fp1+13*fm1-8*fm2+fm3)/(8*h*h*h);
       else
           rslt = (-fp3+12*fp2-39*fp1+56*fi-39*fm1+12*fm2-fm3)/(6*h*h*h*h);
       end
    end
end

% 'exp(-x)' , 1 , 2 , 2 , 0.64