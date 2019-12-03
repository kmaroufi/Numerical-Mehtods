function rslt = Gauss_Legendre(f, a, b, n , ac )
    P = sym('tmp',[1,n+2]);
    x = sym('x');
    P(1)= 1;
    P(2)= x;
    
    for i=3:n+2
        c1 = (2*i-3)*x*P(i-1) ;
        c2 = (i-2)*P(i-2) ;
        P(i) = eval( (c1-c2)/(i-1) );
    end
   
    xs = solve(P(n+1))';
    ws = 2./((1-xs.*xs).*(subs(diff(P(n+1),x),xs).^2));
    
    for i = 1:n 
       s = subs(diff(P(n+1),x),xs(i)) ;
       ws(i) = 2./((1-xs(i)^2)*s^2);
       ws(i) = round( ws(i) * (10^ac) ) / (10^ac) ;
    end
    
    xs = xs*((b-a)/2) + (b+a)/2 ;
    xs =  round( xs * 10^(ac)) / (10^ac) ;
    
    rslt = 0 ;
    for i = 1:n
        rslt = rslt + subs(f,xs(i)) * ws(i) ;
    end
    rslt = eval(rslt * (b-a)/2);
    rslt = round(rslt*(10^ac))/(10^ac) ;
    
    rslt = string(rslt);
end

% 'x^4' , -1 , 1 , 2 ,5