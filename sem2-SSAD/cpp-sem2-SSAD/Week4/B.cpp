#include <iostream>

class Shape {
public:
    virtual double area() = 0;
    virtual double perimeter() = 0;
};

class Rectangle : public Shape {
private:
    double width = 0;
    double height = 0;
public:
    Rectangle() = default;
    Rectangle(double width, double height) : width(width), height(height) {}

    double area() override {
        return width * height;
    }

    double perimeter() override {
        return (width + height) * 2;
    }
};

class Circle : public Shape {
private:
    double radius = 0;
    const double PI = 3.14;
public:
    Circle() = default;
    explicit Circle(double radius): radius(radius) {}

    double area() {
        return PI * radius * radius;
    }

    double perimeter() {
        return 2 * PI * radius;
    }
};

int main() {
    Rectangle rectangle(5.0, 3.0);
    Circle circle(4.0);
    Shape *shape = &rectangle;

    // Demonstrate static casting [1]

    Rectangle* const rectPtr = static_cast<Rectangle* const>(shape);

    // Demonstrate dynamic casting [2]


    // Demonstrate const casting [3]


    int intValue = 42;
    // Demonstrate reinterpret casting [4]

    return EXIT_SUCCESS;
}
