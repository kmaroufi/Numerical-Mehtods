function out = EulerSecondOrder(f,g,z,y,h)
out(1) = y + f * h;
out(2) = z + g* h;
end