#include <iostream>

class Box {

private:
    unsigned length;
    unsigned width;
    unsigned height;

public:
    Box() = default;
    Box(Box& box) = default;
    Box(unsigned int length, unsigned int width, unsigned int height) : length(length), width(width), height(height) {}
    Box& operator=(const Box& box) {
        this->length = box.length;
        this->width = box.width;
        this->height = box.height;
    }

    unsigned getVolume() {
        return length * width * height;
    }

    void operator*(int scaleValue) {
        length *= scaleValue;
        width *= scaleValue;
        height *= scaleValue;
    }

    bool operator==(Box& other) {
        return (length == other.length || length == other.height || length == other.width)
                && (width == other.length || width == other.width || width == other.height)
                && (height == other.length || height == other.width || height == other.height);
    }

    bool isBigger(Box other) {
        return getVolume() > other.getVolume();
    }

    bool isSmaller(Box other) {
        return getVolume() < other.getVolume();
    }
};

class Cube {

private:
    int side;

public:
    Cube() : side(0) {}
    Cube(int side) : side(side) {}
    operator Box() {
        return Box(side, side, side);
    }
};

int main()
{
    Cube cube(5);
    Box box = cube;
    return EXIT_SUCCESS;
}
