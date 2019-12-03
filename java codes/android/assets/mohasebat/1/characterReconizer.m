function [a,b,c,d,e,f] = characterReconizer(inputString)
a = 0; b = 0; c = 0;d = 0; e = 0; f = 0;
if(~isempty(strfind(inputString,'a')))
    a = 1;
end
if(~isempty(strfind(inputString,'b')))
    b = 1;
end
if(~isempty(strfind(inputString,'c')))
   c = 1;
end
if(~isempty(strfind(inputString,'d')))
    d = 1;
end
if(~isempty(strfind(inputString,'e')))
    e = 1;
end
if(~isempty(strfind(inputString,'f')))
    f = 1;
end
end