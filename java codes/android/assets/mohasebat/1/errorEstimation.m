function out = errorEstimation(input1)
stringes = num2str(input1,15);
pointOfDot = find(stringes == '.');
if(~isempty(pointOfDot))
    sizeOfArray = size(stringes);
    out = double(10 ^ (-(sizeOfArray(2) - pointOfDot)));
else
    out = 1;
end
        
