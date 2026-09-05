//bundling types 

//so opposite would be bundling all variants of a signle type together in a data definition


// Circle - > Draw(), Area()
// Rectangle -> Draw(), Area()
      
#include <stdio.h>
struct Circle {
    double radius;
};

double circle_area(struct Circle c)
{
    return 3.14 * c.radius * c.radius;
}

void circle_draw(struct Circle c)
{
    printf("Drawing circle with radius %.1f\n", c.radius);
}

struct Rectangle {
    double width;
    double height;
};
double rectangle_area(struct Rectangle r)
{
    return r.width * r.height;
}

void rectangle_draw(struct Rectangle r)
{
    printf("Drawing rectangle %.1f x %.1f\n",
           r.width, r.height);
}
